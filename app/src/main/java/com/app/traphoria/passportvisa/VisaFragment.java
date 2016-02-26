package com.app.traphoria.passportvisa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.model.VisaTypeDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.passportvisa.adapter.VisaTypeAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VisaFragment extends BaseFragment implements FetchInterface {

    private View view;
    private String TAG = "ADD_EDIT_VISA";
    private String visaID = "";

    public VisaFragment() {

    }

    public static VisaFragment newInstance(String id) {
        VisaFragment fragment = new VisaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visaID = getArguments().getString("id");
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

        if (!visaID.equalsIgnoreCase("")) {
            visaDetails();
        }
        setClick(R.id.visa_type, view);
        setClick(R.id.visa_country, view);
        setClick(R.id.visa_expire_on_tv, view);
        setClick(R.id.save_btn, view);
        setClick(R.id.visa_entry_country, view);

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
                addVisa(visaID);
                break;
            case R.id.visa_entry_country:
                showEntryTypeDialog();
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
                setViewText(R.id.visa_type, passportTypeList.get(position).getName(), view);
            }
        });
    }


    @Override
    public void vehicleName(String text, String countryId) {
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

                        setViewText(R.id.visa_expire_on_tv, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, view);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }


    public void showEntryTypeDialog() {
        final CharSequence[] items = {"Single", "Multiple"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Entry Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                // Do something with the selection
                setViewText(R.id.visa_entry_country, items[item].toString(), view);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void addVisa(String passportID) {
        Utils.hideKeyboard(getActivity());
        if (Utils.isOnline(getActivity())) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_EDIT_VISA);
                params.put("user_id", PreferenceHelp.getUserId(getActivity()));
                params.put("country_id", new CountryDataSource(getActivity()).getWhereData("name", getViewText(R.id.visa_country, view)).getId());
                params.put("visa_type_id", new VisaTypeDataSource(getActivity()).getWhereData("name", getViewText(R.id.visa_type, view)).getId());
                params.put("entry_type", getViewText(R.id.visa_entry_country, view));
                params.put("expire_date", getViewText(R.id.visa_expire_on_tv, view));
                params.put("passport_id", passportID);

                CustomProgressDialog.showProgDialog(getActivity(), null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());

                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        openUserVisaScreen();
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

        if (getViewText(R.id.visa_country, view).equals("")) {
            Utils.customDialog("Please select country", getActivity());
            return false;
        } else if (getViewText(R.id.visa_type, view).equals("")) {
            Utils.customDialog("Please select visa type", getActivity());
            return false;
        } else if (getViewText(R.id.visa_entry_country, view).equals("")) {
            Utils.customDialog("Please select entry type", getActivity());
            return false;
        } else if (getViewText(R.id.visa_expire_on_tv, view).equals("")) {
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


    private void visaDetails() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_VISA_DETAILS);
            params.put("visa_id", visaID);
            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());

                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    VisaDTO visaDTO = new Gson().fromJson(response.getJSONObject("Visa").toString(), VisaDTO.class);
                                    setVisaDetails(visaDTO);
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

    private void setVisaDetails(VisaDTO visaDetails) {

        setViewText(R.id.visa_country, new CountryDataSource(getActivity()).getWhereData("id", visaDetails.getCountry_id()).getName(), view);
        setViewText(R.id.visa_type, new VisaTypeDataSource(getActivity()).getWhereData("id", visaDetails.getVisa_type_id()).getName(), view);
        setViewText(R.id.visa_entry_country, visaDetails.getEntry_type(), view);
        setViewText(R.id.visa_expire_on_tv, visaDetails.getExpire_date(), view);


    }


}
