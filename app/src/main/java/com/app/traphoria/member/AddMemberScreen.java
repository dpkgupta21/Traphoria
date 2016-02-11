package com.app.traphoria.member;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.lacaldabase.RelationDataSource;
import com.app.traphoria.member.adapter.RelationAdapter;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import org.json.JSONObject;

import java.lang.reflect.Type;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);
        init();
        setToolBar();

    }


    private void init() {

        setClick(R.id.btn_add_member);
        setClick(R.id.relation);
    }

    private void setToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.add_member);
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
                openRelationDialog();
                break;
        }
    }


    private void addMember() {

        Utils.hideKeyboard(AddMemberScreen.this);
        if (Utils.isOnline(AddMemberScreen.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_MEMBER);
                params.put("name", getEditTextText(R.id.member_name));
                params.put("relation", getTextViewText(R.id.relation));
                params.put("mobile", getEditTextText(R.id.register_mbl));
                params.put("mobilecode", "91");
                params.put("user_id", PreferenceHelp.getUserId(AddMemberScreen.this));

                final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        Toast.makeText(AddMemberScreen.this, "Member added Successfully.", Toast.LENGTH_LONG).show();
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
            Utils.showDialog(this, "Message", "Please enter member name");
            return false;
        } else if (getTextViewText(R.id.relation).equals("")) {
            Utils.showDialog(this, "Message", "Please enter relation");
            return false;
        } else if (getEditTextText(R.id.register_mbl).equals("")) {
            Utils.showDialog(this, "Message", "Please enter phone number");
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


}
