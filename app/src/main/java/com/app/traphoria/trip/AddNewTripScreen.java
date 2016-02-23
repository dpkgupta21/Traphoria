package com.app.traphoria.trip;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.lacaldabase.CountryDataSource;
import com.app.traphoria.lacaldabase.MemberDataSource;
import com.app.traphoria.lacaldabase.PassportDataSource;
import com.app.traphoria.lacaldabase.VisaDataSource;
import com.app.traphoria.member.adapter.MemberListAdapter;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.task.adapter.SelectMemberAdapter;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.trip.adapter.PassportAdapter;
import com.app.traphoria.trip.adapter.VisaAdapter;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewTripScreen extends BaseActivity implements FetchInterface {

    private Toolbar mToolbar;
    private TextView mTitle;
    private String TAG = "ADD TRIP";

    private RadioButton alone_radio_btn, group_radio_btn;
    private Dialog dialog;
    private int tripType = 1;
    private Dialog mDialog = null;
    private List<MemberDTO> memberList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_trip_screen);
        initView();
        assignClick();
    }

    private void assignClick() {

        alone_radio_btn.setOnCheckedChangeListener(this);
        group_radio_btn.setOnCheckedChangeListener(this);

    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.add_new_trip);
        group_radio_btn = (RadioButton) findViewById(R.id.group_radio_btn);
        alone_radio_btn = (RadioButton) findViewById(R.id.alone_radio_btn);
        setClick(R.id.select_country);
        setClick(R.id.start_date_et);
        setClick(R.id.end_date);
        setClick(R.id.select_passport);
        setClick(R.id.select_visa);
        setClick(R.id.add_member);
        setClick(R.id.btn_add_trip);
        try {
            memberList = new MemberDataSource(this).getMember();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.alone_radio_btn:
                setViewVisibility(R.id.add_member_tv, View.VISIBLE);
                setViewVisibility(R.id.add_member, View.VISIBLE);
                tripType = 2;
                break;
            case R.id.group_radio_btn:
                setViewVisibility(R.id.add_member_tv, View.GONE);
                setViewVisibility(R.id.add_member, View.GONE);
                tripType = 1;
                break;
        }


    }


    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.select_country:
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setFetchVehicleInterface(this);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(), "");
                break;

            case R.id.start_date_et:
                showCalendarDialog(0);
                break;

            case R.id.end_date:
                showCalendarDialog(1);
                break;
            case R.id.select_passport:
                openPassPortDialog();
                break;

            case R.id.select_visa:
                openVisaDialog();
                break;

            case R.id.add_member:
                openMemberDialog();
                break;

            case R.id.btn_add_trip:
                String member = "";
                if (tripType == 1) {
                    member = "";
                } else {

                    for (int i = 0; i < memberList.size(); i++) {
                        if (memberList.get(i).getSelected().equals("Y")) {
                            member = member + memberList.get(i).getId() + ",";
                        }
                    }
                }
                addTrip(member);
                break;


        }
    }


    public void showCalendarDialog(final int flag) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        if (flag == 0) {
                            setTextViewText(R.id.start_date_et, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        } else {
                            setTextViewText(R.id.end_date, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    @Override
    public void vehicleName(String text) {
        setTextViewText(R.id.select_country, text);
    }


    private void openPassPortDialog() {

        try {
            final List<PassportDTO> passportList = new PassportDataSource(this).getPassport();
            if (dialog != null) {
                dialog = null;
            }
            dialog = new Dialog(this);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.relation_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ListView listView = (ListView) dialog.findViewById(R.id.list);
            PassportAdapter countryListAdapter = new PassportAdapter(this, passportList);
            listView.setAdapter(countryListAdapter);
            dialog.show();
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            if (dialog != null) {
                                dialog = null;
                            }
                            setTextViewText(R.id.select_passport, passportList.get(position).getCountry());
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openVisaDialog() {

        try {
            final List<VisaDTO> visaList = new VisaDataSource(this).getVisa();
            if (dialog != null) {
                dialog = null;
            }

            dialog = new Dialog(this);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.relation_dialog);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ListView listView = (ListView) dialog.findViewById(R.id.list);
            VisaAdapter countryListAdapter = new VisaAdapter(this, visaList);
            listView.setAdapter(countryListAdapter);
            dialog.show();
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog.dismiss();
                            if (dialog != null) {
                                dialog = null;
                            }
                            setTextViewText(R.id.select_visa, visaList.get(position).getCountry());
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openMemberDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            mDialog = new Dialog(this, R.style.DialogSlideAnim);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view1 = inflater.inflate(R.layout.sliding_dialog, null,
                    false);
            mDialog.setContentView(view1);
            mDialog.setCancelable(true);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            lp.gravity = Gravity.BOTTOM;
            mDialog.getWindow().setAttributes(lp);

            final ListView listView = (ListView) mDialog
                    .findViewById(R.id.listview);

            final MemberListAdapter countryListAdapter = new MemberListAdapter(this, memberList);
            listView.setAdapter(countryListAdapter);
            mDialog.show();
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (memberList.get(position).getSelected().equalsIgnoreCase("N")) {
                                memberList.get(position).setSelected("Y");
                            } else {
                                memberList.get(position).setSelected("N");
                            }
                            String members = "";
                            for (int i = 0; i < memberList.size(); i++) {
                                if (memberList.get(i).getSelected().equalsIgnoreCase("Y")) {
                                    members = members + memberList.get(i).getName() + "\n";
                                }
                            }

                            setTextViewText(R.id.add_member, members.equals("") ? "" : members.substring(0, members.length() - 1));
                            countryListAdapter.notifyDataSetChanged();

                        }
                    }
            );


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addTrip(String member) {
        if (Utils.isOnline(this)) {
            if (validateForm(member)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_TRIP);
                params.put("user_id", PreferenceHelp.getUserId(this));
                params.put("country_id", new CountryDataSource(this).getWhereData("name", getTextViewText(R.id.select_country)).getId());
                params.put("passport_id", new PassportDataSource(this).getWhereData("country", getTextViewText(R.id.select_passport)).getPassport_id());
                params.put("visa_id", new VisaDataSource(this).getWhereData("country", getTextViewText(R.id.select_visa)).getVisa_id());
                params.put("start_date", getTextViewText(R.id.start_date_et));
                params.put("end_date", getTextViewText(R.id.end_date));
                params.put("trip_type_id", tripType + "");
                params.put("user_ids", member);

                CustomProgressDialog.showProgDialog(this, null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                CustomProgressDialog.hideProgressDialog();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        openTripFragment();
                                    } else {
                                        Utils.customDialog(Utils.getWebServiceMessage(response), AddNewTripScreen.this);
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressDialog.hideProgressDialog();
                        Utils.showExceptionDialog(AddNewTripScreen.this);
                    }
                });

                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                CustomProgressDialog.showProgDialog(this, null);

            }
        } else {
            Utils.showNoNetworkDialog(AddNewTripScreen.this);
        }

    }


    public boolean validateForm(String members) {

        if (getTextViewText(R.id.select_country).equals("")) {
            Utils.customDialog("Please select country", this);
            return false;
        } else if (getTextViewText(R.id.start_date_et).equals("")) {
            Utils.customDialog("Please enter start date", this);
            return false;
        } else if (getTextViewText(R.id.end_date).equals("")) {
            Utils.customDialog("Please enter end date", this);
            return false;

        } else if (!Utils.isFromDateGreater(getTextViewText(R.id.start_date_et), getTextViewText(R.id.end_date))) {
            Utils.customDialog("Trip start date should be less then trip end date", this);
            return false;
        } else if (getTextViewText(R.id.select_passport).equals("")) {
            Utils.customDialog("Please select passport", this);
            return false;
        } else if (getTextViewText(R.id.select_visa).equals("")) {
            Utils.customDialog("Please select visa", this);
            return false;
        } else if (tripType == 2) {
            if (members.equals("")) {
                Utils.customDialog("Please select member", this);
                return false;
            }
        }
        return true;
    }


    private void openTripFragment() {
        Intent intent = new Intent(this, NavigationDrawerActivity.class);
        intent.putExtra("fragmentNumber", 3);
        startActivity(intent);
    }

}
