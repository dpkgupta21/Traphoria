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
import com.app.traphoria.model.PassportVisaDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.passportvisa.adapter.PassportAdapter;
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


    private List<PassportVisaDTO> passportVisaList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
                            List<PassportDTO> passportList = null;
                            List<VisaDTO> visaList = null;
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());

                                Type type = new TypeToken<ArrayList<PassportDTO>>() {
                                }.getType();

                                Type type1 = new TypeToken<ArrayList<VisaDTO>>() {
                                }.getType();

                                passportList = new Gson().fromJson(
                                        response.getJSONArray("Passport").toString(), type);
                                visaList = new Gson().fromJson(
                                        response.getJSONArray("Visa").toString(), type1);
                                setPassportDetails(passportList, visaList);
                            } catch (Exception e) {
                                setPassportDetails(passportList, visaList);

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

    private void setPassportDetails(List<PassportDTO> passportList, List<VisaDTO> visaList) {

        if (passportList != null & visaList != null) {
            setViewVisibility(R.id.no_record_rl, view, View.GONE);
            passportVisaList = new ArrayList<>();

            if (passportList != null && passportList.size() != 0) {

                for (int i = 0; i < passportList.size(); i++) {
                    PassportVisaDTO passportVisaDTO = new PassportVisaDTO();

                    passportVisaDTO.setId(passportList.get(i).getPassport_id());
                    passportVisaDTO.setCountry(passportList.get(i).getCountry());
                    passportVisaDTO.setCountryID(passportList.get(i).getCountry_id());
                    passportVisaDTO.setPassportNumber(passportList.get(i).getPassport_no());
                    passportVisaDTO.setExpiryDate(passportList.get(i).getExpire_date());
                    passportVisaDTO.setPassport_visa_type(passportList.get(i).getPassport_type());
                    passportVisaDTO.setPassport_visa_type_id(passportList.get(i).getPassport_type_id());
                    passportVisaDTO.setCountry_image(passportList.get(i).getCountry_image());
                    passportVisaDTO.setType("P");
                    passportVisaList.add(passportVisaDTO);

                }

            }

            if (visaList != null && visaList.size() != 0) {

                for (int i = 0; i < visaList.size(); i++) {
                    PassportVisaDTO passportVisaDTO = new PassportVisaDTO();
                    passportVisaDTO.setId(visaList.get(i).getVisa_id());
                    passportVisaDTO.setCountry(visaList.get(i).getCountry());
                    passportVisaDTO.setCountryID(visaList.get(i).getCountry_id());
                    passportVisaDTO.setExpiryDate(visaList.get(i).getExpire_date());
                    passportVisaDTO.setPassport_visa_type(visaList.get(i).getVisa_type());
                    passportVisaDTO.setPassport_visa_type_id(visaList.get(i).getVisa_type_id());
                    passportVisaDTO.setCountry_image(visaList.get(i).getCountry_image());
                    passportVisaDTO.setEntry_type(visaList.get(i).getEntry_type());
                    passportVisaDTO.setType("V");
                    passportVisaList.add(passportVisaDTO);

                }
            }
            mAdapter = new PassportAdapter(getActivity(), passportVisaList);
            mRecyclerView.setAdapter(mAdapter);

            ((PassportAdapter) mAdapter).setOnItemClickListener(new PassportAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent;
                    switch (v.getId()) {
                        case R.id.edit_btn:
                            intent = new Intent(getActivity(), AddPassportVisaScreen.class);
                            intent.putExtra("id", passportVisaList.get(position).getId());
                            intent.putExtra("type", passportVisaList.get(position).getType());
                            startActivity(intent);
                            break;
                        case R.id.explore:
                            intent = new Intent(getActivity(), VisaFreeCountryDetails.class);
                            intent.putExtra("type","p");
                            intent.putExtra("countryId", passportVisaList.get(position).getCountryID());
                            startActivity(intent);
                            break;
                        case R.id.btn_edit:
                            intent = new Intent(getActivity(), AddPassportVisaScreen.class);
                            intent.putExtra("id", passportVisaList.get(position).getId());
                            intent.putExtra("type", passportVisaList.get(position).getType());
                            startActivity(intent);
                        case R.id.explore_visa:
                            intent = new Intent(getActivity(), VisaFreeCountryDetails.class);
                            intent.putExtra("type","v");
                            intent.putExtra("countryId", passportVisaList.get(position).getCountryID());
                            startActivity(intent);


                    }

                }
            });


        } else {
            setViewVisibility(R.id.members_passport_recycler, view, View.GONE);
            setViewVisibility(R.id.no_record_rl, view, View.VISIBLE);
        }

    }


    private void addPassportVisa() {
        startActivity(new Intent(getActivity(), AddPassportVisaScreen.class));
    }
}