package com.app.traphoria.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.WakeLocker;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.gps.GPSTracker;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.app.traphoria.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.app.traphoria.CommonUtilities.EXTRA_MESSAGE;
import static com.app.traphoria.CommonUtilities.SENDER_ID;

public class LandingScreen extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LandingScreen";
    private TextView sigup;
    private LoginButton btnFbLogin;
    private Activity mActivity;
    private CallbackManager callbackmanager;
    private AsyncTask<Void, Void, Void> mRegisterTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.landing_screen);
        mActivity = this;
        initViews();
        assignClicks();

        String pushRegistrationId = TraphoriaPreference.getPushRegistrationId(mActivity);
        if (pushRegistrationId == null || pushRegistrationId.equalsIgnoreCase("")) {
            registrationPushNotification();
        }
        GPSTracker gpsTracker = new GPSTracker(this);
    }

    private void assignClicks() {
        sigup.setOnClickListener(this);
    }

    private void initViews() {
        setClick(R.id.btn_facebook_login);
        sigup = (TextView) findViewById(R.id.sigup);
        btnFbLogin = (LoginButton) findViewById(R.id.btn_fb);
        btnFbLogin.setBackgroundResource(R.drawable.backgound_fill);
        btnFbLogin.setText("Hi");
        btnFbLogin.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setFbClick();
        //showHashKey(mActivity.getApplicationContext());
    }

//    public void showHashKey(Context context) {
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo("com.app.traphoria",
//                    PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//
//                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.e("KeyHash:", sign);
//                Toast.makeText(context, sign, Toast.LENGTH_LONG).show();
//            }
//            Log.d("KeyHash:", "****------------***");
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(mActivity);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(mActivity);
    }


    public void openLoginScreen(View view) {
        Intent intent = new Intent(mActivity, LoginScreen.class);
        startActivity(intent);
    }

    private void setFbClick() {
        callbackmanager = CallbackManager.Factory.create();
        btnFbLogin.setReadPermissions("public_profile", "email", "user_birthday");


        btnFbLogin.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println("Success");
                GraphRequest req = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json,
                                                    GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Log.i("info", "onCompleted Error.");
                                } else {
                                    System.out.println("Success");
                                    //String jsonresult = String.valueOf(json);
                                    try {
                                        doSocialLogin("facebook", json.getString("email"),
                                                json.getString("id"), json.getString("name"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
//                                        Utils.sendEmail(getActivity(), "Error", e.getMessage());
                                    }
                                }
                            }
                        }

                );
                Bundle param = new Bundle();
                //, gender, birthday, first_name, last_name, link
                param.putString("fields", "id, name, email");
                req.setParameters(param);
                req.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("", "");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("", "");
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sigup:
                Intent intent = new Intent(mActivity, SignUpScreen.class);
                startActivity(intent);
                break;
            case R.id.btn_facebook_login:
                btnFbLogin.performClick();
                setFbClick();
                break;
        }
    }


    public void doSocialLogin(String socialType, String username, String socialId, String name) {
        Utils.hideKeyboard(mActivity);

        if (Utils.isOnline(mActivity)) {


            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.DO_SOCIAL_LOGIN);
            params.put("name", name);
            params.put("email", username);
            params.put("social_id", socialId);
            params.put("device", "android");
            params.put("device_id", TraphoriaPreference.getPushRegistrationId(mActivity));
            params.put("social_type", socialType);
            params.put("lat", String.valueOf(TraphoriaPreference.getLatitude(mActivity)));
            params.put("lng", String.valueOf(TraphoriaPreference.getLongitude(mActivity)));
            params.put("address", "");

            CustomProgressDialog.showProgDialog(mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());
                            CustomProgressDialog.hideProgressDialog();
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    UserDTO userDTO = new Gson().fromJson(response.
                                            getJSONObject("user").toString(), UserDTO.class);

                                    TraphoriaPreference.putObjectIntoPref(mActivity, userDTO,
                                            PreferenceConstant.USER_INFO);
                                    startActivity(new Intent(mActivity, NavigationDrawerActivity.class));

                                } else {
                                    Utils.showDialog(mActivity, "Message", Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(mActivity);
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            Utils.showNoNetworkDialog(mActivity);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 64206) {
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // For Push notification
    private void registrationPushNotification() {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(mActivity);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(mActivity);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar
                .getRegistrationId(mActivity);

        TraphoriaPreference.setPushRegistrationId(mActivity, regId);
        Log.i("info", "RegId :" + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("info", "RegId :" + regId);
            // Registration is not present, register now with GCM
            GCMRegistrar.register(mActivity, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar
                    .isRegisteredOnServer(mActivity)) {
                // Skips registration.
                Log.i("info", "Already registered with GCM");
            } else {
                Log.i("info", "Not registered with GCM");
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    /**
     * Receiving push messages
     */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message depending upon your app
             * requirement For now i am just displaying it on the screen
             * */

            // Showing received message

            // Releasing wake lock
            WakeLocker.release();
        }
    };


    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(mActivity);
        } catch (Exception e) {
            Utils.ShowLog(TAG, "UnRegister Receiver Error " + e.getMessage());
        }
        super.onDestroy();
    }


}
