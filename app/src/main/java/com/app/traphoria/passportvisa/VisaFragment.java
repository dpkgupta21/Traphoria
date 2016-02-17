package com.app.traphoria.passportvisa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.lacaldabase.CountryDataSource;
import com.app.traphoria.lacaldabase.PassportTypeDataSource;
import com.app.traphoria.lacaldabase.VisaTypeDataSource;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.model.VisaTypeDTO;
import com.app.traphoria.passportvisa.adapter.VisaTypeAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VisaFragment extends BaseFragment implements FetchInterface {

    private View view;

    private String visaID="";
    public VisaFragment() {

    }

    public static VisaFragment newInstance() {
        VisaFragment fragment = new VisaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.fragment_visa, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setClick(R.id.visa_type, view);
        setClick(R.id.visa_country, view);
        setClick(R.id.visa_expire_on_tv, view);
        setClick(R.id.save_btn, view);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.visa_type:

                openVisaTypeDialog();
                break;

            case R.id.visa_country:
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setFetchVehicleInterface(this);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getActivity().getFragmentManager(), "");
                break;
            case R.id.visa_expire_on_tv:
                showCalendarDialog();
                break;

            case R.id.save_btn:
                //addVisa(visaID);
                break;
        }
    }


    private void openVisaTypeDialog() {

        try {
            List<VisaTypeDTO> visaTypeList = new VisaTypeDataSource(getActivity()).getVisaType();
            visaTypeDialog(visaTypeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void visaTypeDialog(final List<VisaTypeDTO> passportTypeList) {

        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.relation_dialog);
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        VisaTypeAdapter countryListAdapter = new VisaTypeAdapter(getActivity(), passportTypeList);
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
    public void vehicleName(String text) {
        setViewText(R.id.visa_country, text, view);
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



//    private void addVisa(String passportID) {
//        Utils.hideKeyboard(getActivity());
//        if (Utils.isOnline(getActivity())) {
//            if (validateForm()) {
//                Map<String, String> params = new HashMap<>();
//                params.put("action", WebserviceConstant.ADD_EDIT_PASSPORT);
//                params.put("country_id", new CountryDataSource(getActivity()).getWhereData(getViewText(R.id.passprt_country, view)).getId());
//                params.put("passport_type_id", new PassportTypeDataSource(getActivity()).getWhereData(getViewText(R.id.passprt_type, view)).getId());
//                params.put("passport_no", getViewText(R.id.passport_no_spinner, view));
//                params.put("expire_date", getViewText(R.id.txt_expiry, view));
//                params.put("user_id", PreferenceHelp.getUserId(getActivity()));
//                params.put("passport_id",passportID);
//
//                CustomProgressDialog.showProgDialog(getActivity(), null);
//                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Utils.ShowLog(TAG, "Response -> " + response.toString());
//
//                                try {
//                                    if (Utils.getWebServiceStatus(response)) {
//                                        // Toast.makeText(AddMemberScreen.this, "Member added Successfully.", Toast.LENGTH_LONG).show();
//                                        //openMemberFragment();
//                                    } else {
//                                        Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
//                                    }
//
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                CustomProgressDialog.hideProgressDialog();
//                            }
//                        }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        CustomProgressDialog.hideProgressDialog();
//                        Utils.showExceptionDialog(getActivity());
//                    }
//                });
//
//                AppController.getInstance().getRequestQueue().add(postReq);
//                postReq.setRetryPolicy(new DefaultRetryPolicy(
//                        30000, 0,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                CustomProgressDialog.showProgDialog(getActivity(), null);
//
//            }
//        } else {
//            Utils.showNoNetworkDialog(getActivity());
//        }
//
//
//    }


    private boolean validateForm() {
        return true;
    }



}
