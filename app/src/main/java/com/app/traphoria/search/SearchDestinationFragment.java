package com.app.traphoria.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.SerachDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.search.adapter.SearchDestinationAdapter;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.MyOnClickListener;
import com.app.traphoria.utility.RecyclerTouchListener;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */

public class SearchDestinationFragment extends BaseFragment {


    private String TAG = "SEARCH";
    private View view;
    private SearchDestinationAdapter mSearchDestinationAdapter;
    private RecyclerView recyclerView;
    private List<SerachDTO> searchList;
    private List<SerachDTO> visibleSearchList;
    private EditText searchText;

    public SearchDestinationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_destination_fragment, container, false);
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);


        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.destination_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        searchText = (EditText) view.findViewById(R.id.edtsearch);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    Iterator<SerachDTO> iterator = visibleSearchList.iterator();
                    ArrayList<SerachDTO> temp = new ArrayList<SerachDTO>();

                    while (iterator.hasNext()) {
                        while (iterator.hasNext()) {
                            SerachDTO serachDTOtBean = iterator.next();
                            String countryTitle = serachDTOtBean.getName();
                            if (countryTitle.toUpperCase().contains(s.toString().toUpperCase())) {
                                temp.add(serachDTOtBean);
                            }
                        }
                        if (mSearchDestinationAdapter != null) {
                            mSearchDestinationAdapter.setSeachList(temp);
                            mSearchDestinationAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getSearchList();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_right_tv:
                break;
        }
    }


    private void getSearchList() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_SEARCH);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));
            params.put("keyword", "");
            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<SerachDTO>>() {
                                }.getType();
                                searchList = new Gson().fromJson(response.getJSONArray("countries").toString(), type);
                                visibleSearchList = searchList;
                                setSearchData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(getActivity(), null);
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }


    }


    private void setSearchData() {
        mSearchDestinationAdapter = new SearchDestinationAdapter(getActivity(), searchList);
        recyclerView.setAdapter(mSearchDestinationAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new MyOnClickListener() {
            @Override
            public void onRecyclerClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CountryDetailScreen.class);
                intent.putExtra("countryId", searchList.get(position).getCountry_id());
                startActivity(intent);
            }

            @Override
            public void onRecyclerLongClick(View view, int position) {

            }

            @Override
            public void onItemClick(View view, int position) {

            }
        }));

    }
}