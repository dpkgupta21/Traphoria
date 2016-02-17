package com.app.traphoria.passportvisa;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
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
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.passportvisa.adapter.PassportTypeAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;

import android.widget.DatePicker;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PassportFragment extends BaseFragment implements FetchInterface {

    private View view;

    private String TAG = "ADD PASSPORT";
    private String passportID ="";

    public PassportFragment() {
    }

    public static PassportFragment newInstance() {
        PassportFragment fragment = new PassportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setClick(R.id.passprt_type, view);
        setClick(R.id.passprt_country, view);
        setClick(R.id.txt_expiry, view);
        setClick(R.id.save_btn,view);

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

            case R.id.save_btn:
                addPassport(passportID);
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
    public void vehicleName(String text) {
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


    private void addPassport(String passportID) {
        Utils.hideKeyboard(getActivity());
        if (Utils.isOnline(getActivity())) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_EDIT_PASSPORT);
                params.put("country_id", new CountryDataSource(getActivity()).getWhereData(getViewText(R.id.passprt_country, view)).getId());
                params.put("passport_type_id", new PassportTypeDataSource(getActivity()).getWhereData(getViewText(R.id.passprt_type, view)).getId());
                params.put("passport_no", getViewText(R.id.passport_no_spinner, view));
                params.put("expire_date", getViewText(R.id.txt_expiry, view));
                params.put("user_id", PreferenceHelp.getUserId(getActivity()));
                params.put("passport_id",passportID);

                CustomProgressDialog.showProgDialog(getActivity(), null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());

                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        // Toast.makeText(AddMemberScreen.this, "Member added Successfully.", Toast.LENGTH_LONG).show();
                                        //openMemberFragment();
                                    } else {
                                        Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
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
        return true;
    }

}
