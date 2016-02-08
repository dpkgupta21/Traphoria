package com.app.traphoria.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends BaseActivity {


    private String TAG = "LoginScreen";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        initViews();

    }


    private void initViews() {

        setClick(R.id.btn_login);
        setClick(R.id.back_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_login:
                performLogin();
                break;
        }
    }

    public void performLogin() {

        Utils.hideKeyboard(LoginScreen.this);
        if (Utils.isOnline(LoginScreen.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.DO_LOGIN);
                params.put("email", getEditTextText(R.id.edt_email));
                params.put("password", getEditTextText(R.id.edt_pwd));
                params.put("device", "android");
                params.put("device_id", "");
                params.put("lat", "");
                params.put("lng", "");
                params.put("address", "");
                final ProgressDialog pdialog = Utils.createProgressDialog(this, null, false);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                pdialog.dismiss();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {

                                        UserDTO userDTO = new Gson().fromJson(response.getJSONObject("user").toString(), UserDTO.class);
                                        TraphoriaPreference.putObjectIntoPref(LoginScreen.this, userDTO, PreferenceConstant.USER_INFO);
                                        startActivity(new Intent(LoginScreen.this, NavigationDrawerActivity.class));
                                    } else {
                                        Utils.showDialog(LoginScreen.this, "Error", Utils.getWebServiceMessage(response));
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(LoginScreen.this);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            }
        } else {
            Utils.showNoNetworkDialog(LoginScreen.this);
        }


    }


    public boolean validateForm() {

        if (getEditTextText(R.id.edt_email).equals("")) {
            Utils.showDialog(this, "Message", "Please enter username");
            return false;
        } else if (getEditTextText(R.id.edt_pwd).equals("")) {
            Utils.showDialog(this, "Message", "Please enter password");
            return false;
        }
        return true;
    }

}
