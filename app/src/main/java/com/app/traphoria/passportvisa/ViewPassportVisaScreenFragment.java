package com.app.traphoria.passportvisa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.passportvisa.adapter.PassportAdapter;
import com.app.traphoria.passportvisa.adapter.VisaAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseFragment;
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


public class ViewPassportVisaScreenFragment extends BaseFragment {


    private static final String TAG = "ViewPassportVisaScreenFragment";
    private View view;
    private Toolbar mToolbar;
    private List<PassportDTO> passportList;
    private List<VisaDTO> visaList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView vRecyclerView;
    private RecyclerView.Adapter vAdapter;
    private RecyclerView.LayoutManager vLayoutManager;

    public ViewPassportVisaScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passport_visa_screen_fragment, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.passport_visa_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.members_passport_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        vRecyclerView = (RecyclerView) view.findViewById(R.id.members_visa_recycler);
        vRecyclerView.setHasFixedSize(true);
        vLayoutManager = new LinearLayoutManager(getActivity());
        vRecyclerView.setLayoutManager(vLayoutManager);

        getPassportVisaDetails();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addPassportVisa();
                return false;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_add_passport_visa:
                addPassportVisa();
                break;
        }
    }

    private void getPassportVisaDetails() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_PASSPORT_VISA_DETAILS);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<PassportDTO>>() {
                                }.getType();

                                Type type1 = new TypeToken<ArrayList<VisaDTO>>() {
                                }.getType();

                                passportList = new Gson().fromJson(response.getJSONArray("Passport").toString(), type);
                                visaList = new Gson().fromJson(response.getJSONArray("Visa").toString(), type1);
                                setPassportDetails();
                            } catch (Exception e) {
                                setPassportDetails();
                                //setPassportDetails();
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
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

    private void setPassportDetails() {

        if (passportList != null & visaList != null) {
            setViewVisibility(R.id.no_record_rl, view, View.GONE);
            if (passportList != null && passportList.size() != 0) {
                mAdapter = new PassportAdapter(getActivity(), passportList);
                mRecyclerView.setAdapter(mAdapter);

                ((PassportAdapter) mAdapter).setOnItemClickListener(new PassportAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent i;
                        switch (v.getId())
                        {
                            case R.id.edit_btn:
                                break;
                        }

                    }
                });
            }

            if (visaList != null && visaList.size() != 0) {
                vAdapter = new VisaAdapter(getActivity(), visaList);
                vRecyclerView.setAdapter(vAdapter);

                ((VisaAdapter) mAdapter).setOnItemClickListener(new VisaAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Intent i;
                        switch (v.getId()) {
                            case R.id.edit_btn:
                                break;
                        }

                    }
                });


            }

        } else {
            setViewVisibility(R.id.members_passport_recycler, view, View.GONE);
            setViewVisibility(R.id.members_visa_recycler, view, View.GONE);
            setViewVisibility(R.id.no_record_rl, view, View.VISIBLE);
        }

    }


    private void addPassportVisa() {
        startActivity(new Intent(getActivity(), AddPassportVisaScreen.class));
    }
}