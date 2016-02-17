package com.app.traphoria.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileNumberVerificationScreen extends BaseActivity {

    private static final String TAG = "MobileNumberVerificationScreen";
    private ImageView back_btn;
    private DigitsAuthButton digitsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_number_verification_screen);
        initViews();
        assignClicks();
    }

    private void initViews() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
        Digits.getSessionManager().clearActiveSession();
//        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
//        digitsButton.setText(getString(R.string.send_code));
//        digitsButton.setAuthTheme(R.style.btn_style_small);
//        digitsButton.setBackgroundResource(R.drawable.purple_button_selector);
//        digitsButton.setTextSize(12);
//        digitsButton.setCallback(authCallback);
//        digitsButton.setOnClickListener(onDigitClick);

        setClick(R.id.btn_verify);
    }

//    View.OnClickListener onDigitClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            String phoneNum = ((EditText) findViewById(R.id.mobile_num_et)).getText().toString().trim();
//            if (phoneNum != null && !phoneNum.equalsIgnoreCase("")) {
//                doCheckMobile(phoneNum);
//            } else {
//                Utils.customDialog("Please enter phone number", MobileNumberVerificationScreen.this);
//            }
//        }
//    };


    AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
            if (phoneNumber != null) {
                String countryCode = phoneNumber.replace(TraphoriaPreference.getMobileNumber(MobileNumberVerificationScreen.this), "");
                TraphoriaPreference.setCountryCode(MobileNumberVerificationScreen.this, countryCode);

                //After Successful verification call Signup screen
                Intent intent = new Intent(MobileNumberVerificationScreen.this, SignUpScreen.class);
                intent.putExtra("MOBILE_NUMBER", TraphoriaPreference.getMobileNumber(MobileNumberVerificationScreen.this));
                startActivity(intent);
            }
        }

        @Override
        public void failure(DigitsException exception) {

        }
    };


    private void doCheckMobile(final String phoneNumber) {
        Utils.hideKeyboard(this);

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.CHECK_MOBILE);
            params.put("mobile", phoneNumber);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            CustomProgressDialog.hideProgressDialog();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    TraphoriaPreference.setMobileNumber(MobileNumberVerificationScreen.this,
                                            ((EditText) findViewById(R.id.mobile_num_et)).
                                                    getText().toString().trim());
                                    Digits.authenticate(authCallback,
                                            ((EditText) findViewById(R.id.mobile_num_et)).
                                                    getText().toString().trim());


                                } else {
                                    Utils.showDialog(MobileNumberVerificationScreen.this, "Error", Utils.getWebServiceMessage(response));
                                    startActivity(new Intent(MobileNumberVerificationScreen.this, LandingScreen.class));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(MobileNumberVerificationScreen.this);
                }
            });

            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Utils.showNoNetworkDialog(this);
        }

    }

    private void assignClicks() {
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.back_btn:
                finish();
                break;

            case R.id.btn_verify:
                verifyNumber();
                break;
        }
    }

    private void verifyNumber()
    {

        String phoneNum = ((EditText) findViewById(R.id.mobile_num_et)).getText().toString().trim();
        if (phoneNum != null && !phoneNum.equalsIgnoreCase("")) {
            doCheckMobile(phoneNum);
        } else {
            Utils.customDialog("Please enter phone number", MobileNumberVerificationScreen.this);
        }
    }
}
