package com.app.traphoria.passportvisa;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.lacaldabase.CountryDataSource;
import com.app.traphoria.lacaldabase.PassportTypeDataSource;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.passportvisa.adapter.PassportTypeAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;

import android.widget.DatePicker;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PassportFragment extends BaseFragment implements FetchInterface {

    private View view;

    private String TAG = "ADD PASSPORT";
    private String passportID = "";
    private String userId;

    public PassportFragment() {
    }


    public static PassportFragment newInstance(String id, boolean isEditPassportFlag,
                                               String userId, boolean isMemberFlag) {
        PassportFragment fragment = new PassportFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putBoolean("isEditPassportFlag", isEditPassportFlag);
        bundle.putString("userId", userId);
        bundle.putBoolean("isMember", isMemberFlag);


        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passportID = getArguments().getString("id");
        userId = getArguments().getString("userId");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_passport, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!passportID.equalsIgnoreCase("")) {
            getPassPortDetails();
        }
        boolean isPassportFlag = getArguments().getBoolean("isEditPassportFlag", false);
        if (isPassportFlag) {
            setViewEnable(R.id.add_btn, view, false);
        } else {
            setViewEnable(R.id.add_btn, view, true);
        }
        setClick(R.id.passprt_type, view);
        setClick(R.id.passprt_country, view);
        setClick(R.id.txt_expiry, view);
        setClick(R.id.save_btn, view);
        setClick(R.id.add_btn, view);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.passprt_type:

                openPassportTypeDialog();
                break;

            case R.id.passprt_country:
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setFetchVehicleInterface(this);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getActivity().getFragmentManager(), "");
                break;
            case R.id.txt_expiry:
                showCalendarDialog();
                break;

            case R.id.add_btn:
                addPassport(passportID, false);
                break;
            case R.id.save_btn:
                addPassport(passportID, true);
                break;
        }
    }


    private void openPassportTypeDialog() {

        try {
            List<PassportTypeDTO> passportTypeList = new PassportTypeDataSource(getActivity()).getPassportType();
            passportTypeDialog(passportTypeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void passportTypeDialog(final List<PassportTypeDTO> passportTypeList) {

        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.relation_dialog);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        PassportTypeAdapter countryListAdapter = new PassportTypeAdapter(getActivity(), passportTypeList);
        listView.setAdapter(countryListAdapter);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                dialog.dismiss();
//                if (dialog != null) {
//                    dialog = null;
//                }
                setViewText(R.id.passprt_type, passportTypeList.get(position).getName(), view);
            }
        });
    }


    @Override
    public void vehicleName(String text, String countryId) {
        setViewText(R.id.passprt_country, text, view);
    }

    public void showCalendarDialog() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view1, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        setViewText(R.id.txt_expiry, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year, view);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }


    private void addPassport(String passportID, final boolean flag) {
        Utils.hideKeyboard(getActivity());
        if (Utils.isOnline(getActivity())) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_EDIT_PASSPORT);
                params.put("country_id", new CountryDataSource(getActivity()).getWhereData("name", getViewText(R.id.passprt_country, view)).getId());
                params.put("passport_type_id", new PassportTypeDataSource(getActivity()).getWhereData("name", getViewText(R.id.passprt_type, view)).getId());
                params.put("passport_no", getViewText(R.id.passport_no_spinner, view));
                params.put("expire_date", getViewText(R.id.txt_expiry, view));
                params.put("user_id", userId);
                params.put("passport_id", passportID);

                CustomProgressDialog.showProgDialog(getActivity(), null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                        WebserviceConstant.SERVICE_BASE_URL,
                        params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());

                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        if (flag) {
                                            boolean isMemberFlag = getArguments().getBoolean("isMember");
                                            if (!isMemberFlag) {
                                                openUserVisaScreen();
                                            } else {
                                                Intent intent = new Intent(getActivity(),
                                                        NavigationDrawerActivity.class);
                                                intent.putExtra("fragmentNumber", 5);
                                                intent.putExtra("memberId", userId);
                                                startActivity(intent);
                                            }
                                        } else {


                                            Intent intent = new Intent(getActivity().getApplicationContext(),
                                                    AddPassportVisaScreen.class);
                                            intent.putExtra("id", "");
                                            intent.putExtra("type", "P");
                                            startActivity(intent);

                                        }
                                    } else {
                                        Utils.customDialog(Utils.getWebServiceMessage(response), getActivity());
                                    }


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

            }
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }


    }


    private boolean validateForm() {

        if (getViewText(R.id.passprt_country, view).equals("")) {
            Utils.customDialog("Please select country", getActivity());
            return false;
        } else if (getViewText(R.id.passprt_type, view).equals("")) {
            Utils.customDialog("Please select passport type", getActivity());
            return false;
        } else if (getViewText(R.id.passport_no_spinner, view).equals("")) {
            Utils.customDialog("Please enter passport number", getActivity());
            return false;
        } else if (getViewText(R.id.txt_expiry, view).equals("")) {
            Utils.customDialog("Please enter expiry date", getActivity());
            return false;
        }


        return true;
    }


    private void openUserVisaScreen() {
        Intent intent = new Intent(getActivity(), NavigationDrawerActivity.class);
        intent.putExtra("fragmentNumber", 2);
        startActivity(intent);
    }


    private void getPassPortDetails() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_PASSPORT_DETAILS);
            params.put("passport_id", passportID);
            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());

                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    PassportDTO passportDTO = new Gson().fromJson(response.getJSONObject("Passport").toString(), PassportDTO.class);
                                    setPassportDetails(passportDTO);
                                } else {
                                    Utils.customDialog(Utils.getWebServiceMessage(response), getActivity());
                                }

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

    private void setPassportDetails(PassportDTO passportDetails) {
        setViewText(R.id.passprt_country,
                new CountryDataSource(getActivity()).
                        getWhereData("id", passportDetails.getCountry_id()).getName(), view);
        setViewText(R.id.passport_no_spinner, passportDetails.getPassport_no(), view);
        setViewText(R.id.txt_expiry, passportDetails.getExpire_date(), view);
        setViewText(R.id.passprt_type, new PassportTypeDataSource(getActivity()).
                getWhereData("id", passportDetails.getPassport_type_id()).getName(), view);



    }

}
