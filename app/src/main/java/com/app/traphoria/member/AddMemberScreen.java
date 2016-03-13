package com.app.traphoria.member;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.lacaldabase.RelationDataSource;
import com.app.traphoria.member.adapter.RelationAdapter;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.settings.adapter.CountryCodeAdapter;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMemberScreen extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private String TAG = "ADD_MEMBER";
    private List<RelationDTO> relationList;
    private Dialog dialog;
    private Dialog dialogCountryCode;
    private Activity mActivity;
    private List<Map<String, String>> countryCodeList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);
        mActivity = this;
        init();
        setToolBar();

    }


    private void init() {

        setClick(R.id.btn_add_member);
        //setClick(R.id.relation);
        setClick(R.id.txt_contry_code_val);
    }

    private void setToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.add_member);
        countryCodeList = getCountryCode();
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_member:

                addMember();
                break;

            case R.id.relation:
                //openRelationDialog();
                break;

            case R.id.txt_contry_code_val:
                openDialogForCountry();
                break;
        }
    }


    private void openDialogForCountry() {
        dialogCountryCode = new Dialog(mActivity);
        dialogCountryCode.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCountryCode.setContentView(R.layout.layout_country_code);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ListView listView = (ListView) dialogCountryCode.findViewById(R.id.list);
        CountryCodeAdapter adapter = new CountryCodeAdapter(mActivity, countryCodeList);
        listView.setAdapter(adapter);
        dialogCountryCode.show();
        listView.setOnItemClickListener(dialogItemClickListener);
    }


    AdapterView.OnItemClickListener dialogItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {
            setViewText(R.id.txt_contry_code_val, countryCodeList.get(i).get("dial_code"));
            dialogCountryCode.dismiss();
        }
    };

    private List<Map<String, String>> getCountryCode() {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(Utils.loadJSONFromAsset(mActivity));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Map<String, String> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                map.put("dial_code", jsonObject.getString("dial_code"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void addMember() {

        Utils.hideKeyboard(AddMemberScreen.this);
        if (Utils.isOnline(AddMemberScreen.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_MEMBER);
                params.put("name", getEditTextText(R.id.member_name));
                params.put("relation", getEditTextText(R.id.relation));
                params.put("mobile", getEditTextText(R.id.register_mbl));
                params.put("mobilecode", getTextViewText(R.id.txt_contry_code_val));
                params.put("user_id", PreferenceHelp.getUserId(AddMemberScreen.this));
                String mobNumber = (getTextViewText(R.id.txt_contry_code_val)
                        + getEditTextText(R.id.register_mbl)).replace("+", "");
                final String formatMobNumber = mobNumber.replace("+", "");

                final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                        WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        if (response.has("sms")) {
                                            try {
//                                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                                                sendIntent.setData(Uri.parse("smsto:"));
//                                                sendIntent.setType("vnd.android-dir/mms-sms");
//                                                sendIntent.putExtra("address", formatMobNumber);
//                                                sendIntent.putExtra("sms_body", response.has("sms"));
//                                                startActivity(sendIntent);
//                                                Toast.makeText(getApplicationContext(), "SMS sent.",
//                                                        Toast.LENGTH_LONG).show();

                                                SmsManager smsManager = SmsManager.getDefault();
                                                smsManager.sendTextMessage(formatMobNumber, null, response.getString("sms"), null, null);
                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(),
                                                        "SMS failed, please try again.",
                                                        Toast.LENGTH_LONG).show();
                                                e.printStackTrace();
                                            }
                                        }
//                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                                        sendIntent.putExtra("sms_body", "default content");
//                                        sendIntent.setType("vnd.android-dir/mms-sms");
//                                        startActivity(sendIntent);
                                        // Toast.makeText(AddMemberScreen.this, "Member added Successfully.", Toast.LENGTH_LONG).show();
                                        openMemberFragment();
                                    } else {
                                        Utils.showDialog(AddMemberScreen.this, "Error", Utils.getWebServiceMessage(response));
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(AddMemberScreen.this);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            }
        } else {
            Utils.showNoNetworkDialog(AddMemberScreen.this);
        }


    }

    public boolean validateForm() {

        if (getEditTextText(R.id.member_name).equals("")) {
            Utils.customDialog("Please enter member name", this);
            return false;
        } else if (getEditTextText(R.id.relation).equals("")) {
            Utils.customDialog("Please enter relation", this);
            return false;
        } else if (getEditTextText(R.id.register_mbl).equals("")) {
            Utils.customDialog("Please enter phone number", this);
            return false;
        }
        return true;
    }


    private void openRelationDialog() {

        try {
            relationList = new RelationDataSource(AddMemberScreen.this).getRelation();
            relationDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void relationDialog() {

        dialog = new Dialog(AddMemberScreen.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.relation_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ListView listView = (ListView) dialog.findViewById(R.id.list);
        RelationAdapter countryListAdapter = new RelationAdapter(this, relationList);
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
                        setTextViewText(R.id.relation, relationList.get(position).getName());
                    }
                }
        );
    }


    private void openMemberFragment() {

        Intent intent = new Intent(AddMemberScreen.this, NavigationDrawerActivity.class);
        intent.putExtra("fragmentNumber", 5);
        startActivity(intent);
    }


}
