package com.app.traphoria.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private Activity mActivity;
    private String TAG = "ForgetPasswordActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();
        mActivity = this;

    }


    private void initViews() {

        setClick(R.id.btn_forget_password);
        setClick(R.id.back_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_forget_password:

                performLogin();
                break;
        }
    }

    public void performLogin() {

        Utils.hideKeyboard(ForgetPasswordActivity.this);
        if (Utils.isOnline(ForgetPasswordActivity.this)) {
            if (validateForm()) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.FORGOT_PASSWORD);
                params.put("email", getEditTextText(R.id.edt_email));

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
                                        Toast.makeText(mActivity, "Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Utils.customDialog(Utils.getWebServiceMessage(response), ForgetPasswordActivity.this);
                                        // Utils.showDialog(LoginScreen.this, "Error", Utils.getWebServiceMessage(response));
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pdialog.dismiss();
                        Utils.showExceptionDialog(ForgetPasswordActivity.this);
                    }
                });
                pdialog.show();
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            }
        } else {
            Utils.showNoNetworkDialog(ForgetPasswordActivity.this);
        }


    }


    public boolean validateForm() {

        if (getEditTextText(R.id.edt_email).equals("")) {

            Utils.customDialog("Please enter username.", this);
            //Utils.showDialog(this, "Message", "Please enter username");
            return false;
        }
        return true;
    }



}
