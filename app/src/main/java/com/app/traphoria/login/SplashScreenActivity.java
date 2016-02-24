package com.app.traphoria.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.app.traphoria.R;
import com.app.traphoria.WakeLocker;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.Utils;
import com.google.android.gcm.GCMRegistrar;

import java.util.Timer;
import java.util.TimerTask;

import static com.app.traphoria.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.app.traphoria.CommonUtilities.EXTRA_MESSAGE;
import static com.app.traphoria.CommonUtilities.SENDER_ID;

public class SplashScreenActivity extends FragmentActivity {

    private static final String TAG = "SplashScreenActivity";
    private long splashDelay = 3000;
    private Context mContext;
    Timer timer;
    // for push notification
    private AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = SplashScreenActivity.this;


        setContentView(R.layout.activity_splash);

        String pushRegistrationId = TraphoriaPreference.getPushRegistrationId(mContext);
        if (pushRegistrationId == null || pushRegistrationId.equalsIgnoreCase("")) {
           registrationPushNotification();
        }


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                UserDTO userDTO =
                TraphoriaPreference.getObjectFromPref(mContext, PreferenceConstant.USER_INFO);
                Intent i = null;
                if (userDTO == null) {
                    i = new Intent(mContext, LandingScreen.class);
                } else {
                    i = new Intent(mContext, NavigationDrawerActivity.class);
                    //i.putExtra("fragmentName", getString(R.string.interest_screen_title));
                }
                finish();
                startActivity(i);
            }
        };
        timer = new Timer();
        timer.schedule(task, splashDelay);
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        finish();
    }

    // For Push notification
    private void registrationPushNotification() {

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(mContext);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(mContext);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar
                .getRegistrationId(mContext);
        TraphoriaPreference.setPushRegistrationId(mContext, regId);
        Log.i("info", "RegId :" + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            Log.i("info", "RegId :" + regId);
            // Registration is not present, register now with GCM
            GCMRegistrar.register(mContext, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar
                    .isRegisteredOnServer(mContext)) {
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
                        // Register on our server
                        // On server creates a new user
                        // ServerUtilities.register(VehicleSelDialogActivity.this,
                        // "Deepak", "Gupta", regId);
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
        Utils.ShowLog(TAG, "onDestroy");
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(mContext);
        } catch (Exception e) {
            Utils.ShowLog("info", "UnRegister Receiver Error " + e.getMessage());
        }
        super.onDestroy();


    }
}
