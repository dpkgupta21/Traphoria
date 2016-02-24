package com.app.traphoria.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.app.traphoria.login.LandingScreen;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.TraphoriaPreference;

public class SessionManager {

    private static final String TAG = "<SessionManager>";
    private Context _context;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;

        Log.d(TAG, "session manager onstructor called");
    }


    /**
     * Clear session details
     */
    public static void logoutUser(Context mContext) {
        // Clearing all data from Shared Preferences
        TraphoriaPreference.removeObjectIntoPref(mContext, PreferenceConstant.USER_INFO);
        //TraphoriaPreference.setLoggedIn(mContext, false);

        // After logout redirect user to Loing Activity
        Intent i = new Intent(mContext, LandingScreen.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
        Utils.ShowLog(TAG, "logging out user");
    }

    /**
     * Quick check for login
     * *
     */

    // Get Login State
    //public boolean isLoggedIn(Context mContext) {
    //   return DealPreferences.isLoggedIn(mContext);
    //}
}
