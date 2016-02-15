package com.app.traphoria.passportvisa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.adapter.PassportVisaAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.PassportVisaDetailsDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.view.AddPassportVisaScreen;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ViewPassportVisaScreenFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "ViewPassportVisaScreenFragment";
    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;
    private RelativeLayout noRecordRl;
    private ImageView imgAddPassportVisa;


    private RecyclerView recyclerView;
    private PassportVisaAdapter passportVisaAdapter;

    public ViewPassportVisaScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passport_visa_screen_fragment, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        imgAddPassportVisa = (ImageView) view.findViewById(R.id.img_add_passport_visa);
        noRecordRl = (RelativeLayout) view.findViewById(R.id.no_record_rl);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_passport_visa_list);
        passportVisaAdapter = new PassportVisaAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(passportVisaAdapter);
        imgAddPassportVisa.setOnClickListener(this);
        noRecordRl.setVisibility(View.GONE);

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


        mActivity = getActivity();

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

                                PassportVisaDetailsDTO passportVisaDetailDTO = new Gson().
                                        fromJson(response.toString(), PassportVisaDetailsDTO.class);

                                setPassportDetails(passportVisaDetailDTO);
                            } catch (Exception e) {
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

    private void setPassportDetails(PassportVisaDetailsDTO passportVisaDetailDTO) {

//        if (passportVisaDetailDTO.getPassport() != null && passportVisaDetailDTO.getVisa() != null) {
//            setViewVisibility(R.id.no_trip_rl, view, View.GONE);
//            if (passportList != null && passportList.size() != 0) {
//                mAdapter = new MemberPassportAdapter(passportList, getActivity());
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            if (visaList != null && visaList.size() != 0) {
//                vAdapter = new MemberVisaAdapter(visaList, getActivity());
//                vRecyclerView.setAdapter(vAdapter);
//
//            }
//
//        } else {
//            setViewVisibility(R.id.members_passport_recycler, view, View.GONE);
//            setViewVisibility(R.id.members_visa_recycler, view, View.GONE);
//            setViewVisibility(R.id.no_trip_rl, view, View.VISIBLE);
//        }


    }


    private void addPassportVisa() {
        startActivity(new Intent(getActivity(), AddPassportVisaScreen.class));
    }
}