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

public class MobileNumberVerificationScreen extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MobileNumberVerificationScreen";
    private RelativeLayout mobile_num_form, send_btn_rl, verification_code_form, verify_btn_rl;
    private ImageView back_btn;
    private MobileNumberVerificationScreen mContext;
    private DigitsAuthButton digitsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_number_verification_screen);
        mContext = this;
        initViews();
        assignClicks();
    }

    private void initViews() {
        mobile_num_form = (RelativeLayout) findViewById(R.id.mobile_num_form);
        send_btn_rl = (RelativeLayout) findViewById(R.id.send_btn_rl);
        verification_code_form = (RelativeLayout) findViewById(R.id.verification_code_form);
        verify_btn_rl = (RelativeLayout) findViewById(R.id.verify_btn_rl);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        Digits.getSessionManager().clearActiveSession();
        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setText(getString(R.string.send_code));
        digitsButton.setAuthTheme(R.style.btn_style_small);
        digitsButton.setBackgroundResource(R.drawable.purple_button_selector);
        digitsButton.setTextSize(12);
        digitsButton.setCallback(authCallback);
        digitsButton.setOnClickListener(onDigitClick);
    }

    View.OnClickListener onDigitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doCheckMobile(((EditText) findViewById(R.id.mobile_num_et)).
                    getText().toString().trim());
//            TraphoriaPreference.setMobileNumber(mContext, ((EditText) findViewById(R.id.mobile_num_et)).
//                    getText().toString().trim());
//            Digits.authenticate(authCallback, ((EditText) findViewById(R.id.mobile_num_et)).getText().toString().trim());

        }
    };


    AuthCallback authCallback = new AuthCallback() {
        @Override
        public void success(DigitsSession session, String phoneNumber) {
            if(phoneNumber!=null) {
                String countryCode = phoneNumber.replace(TraphoriaPreference.getMobileNumber(mContext), "");
                TraphoriaPreference.setCountryCode(mContext, countryCode);

                //After Successful verification call Signup screen
                Intent intent = new Intent(mContext, SignUpScreen.class);
                intent.putExtra("MOBILE_NUMBER", TraphoriaPreference.getMobileNumber(mContext));
                startActivity(intent);
            }
        }

        @Override
        public void failure(DigitsException exception) {

        }
    };


    private void doCheckMobile(final String phoneNumber) {
        Utils.hideKeyboard(mContext);

        if (Utils.isOnline(mContext)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.CHECK_MOBILE);
            params.put("mobile", phoneNumber);
            CustomProgressDialog.showProgDialog(mContext, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            CustomProgressDialog.hideProgressDialog();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    TraphoriaPreference.setMobileNumber(mContext,
                                            ((EditText) findViewById(R.id.mobile_num_et)).
                                            getText().toString().trim());
                                    Digits.authenticate(authCallback,
                                            ((EditText) findViewById(R.id.mobile_num_et)).
                                                    getText().toString().trim());


                                } else {
                                    Utils.showDialog(mContext, "Error", Utils.getWebServiceMessage(response));
                                    startActivity(new Intent(mContext, LandingScreen.class));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(mContext);
                }
            });

            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Utils.showNoNetworkDialog(mContext);
        }

    }

    private void assignClicks() {
        send_btn_rl.setOnClickListener(this);
        verify_btn_rl.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_btn_rl:
                mobile_num_form.setVisibility(View.GONE);
                verification_code_form.setVisibility(View.VISIBLE);
                break;

            case R.id.verify_btn_rl:
                finish();
                Intent intent = new Intent(this, SignUpScreen.class);
                startActivity(intent);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
