package com.app.traphoria.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.search.adapter.SearchDestinationAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.SerachDTO;
import com.app.traphoria.preference.PreferenceHelp;
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
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchDestinationFragment extends BaseFragment {


    private String TAG = "SEARCH";
    private View view;
    private Toolbar mToolbar;
    private TextView toolbar_right_tv;
    private SearchDestinationAdapter mSearchDestinationAdapter;
    private RecyclerView recyclerView;
    private List<SerachDTO> searchList;

    public SearchDestinationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_destination_fragment, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);


        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.destination_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
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
                Intent intent = new Intent(getActivity(), DestinationDetailScreen.class);
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