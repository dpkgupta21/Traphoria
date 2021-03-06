package com.app.traphoria.preference;


import android.content.Context;
import android.content.SharedPreferences;

import com.app.traphoria.model.UserDTO;

import java.io.IOException;

public class TraphoriaPreference {


    public static final String PREF_NAME = "TRAP_PREFERENCES";

    public static String COUNTRY_CODE = "country_code";
    public static String MOBILE_NUMBER = "mobile_number";
    public static String PUSH_REGISTRATION_ID = "push_registration_id";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LOCATION_UPLOAD = "add_location";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMERGENCY_NUMBER = "emergency_number";
    public static final String SOCIAL_LOGIN = "social_login";


    /**
     * This genric method use to put object into preference<br>
     * How to use<br>
     * Bean bean = new Bean();<br>
     * putObjectIntoPref(context,bean,key)
     *
     * @param context Context of an application
     * @param e       your genric object
     * @param key     String key which is associate with object
     */
    public static <E> void putObjectIntoPref(Context context, E e, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString(key, ObjectSerializer.serialize(e));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        editor.commit();

    }

    public static <E> void removeObjectIntoPref(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();

    }

    /**
     * This method is use to get your object from preference.<br>
     * How to use<br>
     * Bean bean = getObjectFromPref(context,key);
     *
     * @param context
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E getObjectFromPref(Context context, String key) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            return (E) ObjectSerializer.deserialize(context.getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE).getString(key, null));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isSocialLogin(Context context) {
        boolean isSocialLogin = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(
                SOCIAL_LOGIN, false);
        return isSocialLogin;

    }

    public static void setSocialLogin(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SOCIAL_LOGIN, value);
        editor.apply();
    }

    public static String getEmergencyNumber(Context context) {
        String countryCode = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                EMERGENCY_NUMBER, "");
        return countryCode;

    }

    public static void setEmergencyNumber(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMERGENCY_NUMBER, String.valueOf(value));
        editor.apply();
    }


    public static String getCountryCode(Context context) {
        String countryCode = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                COUNTRY_CODE, "");
        return countryCode;

    }

    public static void setCountryCode(Context context, String countryCode) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(COUNTRY_CODE, String.valueOf(countryCode));
        editor.apply();
    }

    public static String getMobileNumber(Context context) {
        String mobNumber = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                MOBILE_NUMBER, "");
        return mobNumber;

    }

    public static void setMobileNumber(Context context, String mobileNumber) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE_NUMBER, String.valueOf(mobileNumber));
        editor.apply();
    }

    public static String getPushRegistrationId(Context context) {
        String pushRegistrationId = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                PUSH_REGISTRATION_ID, "");
        return pushRegistrationId;

    }

    public static void setPushRegistrationId(Context context, String pushRegistrationId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PUSH_REGISTRATION_ID, String.valueOf(pushRegistrationId));
        editor.apply();
    }

    public static void setLatitude(Context context, double latitude) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LATITUDE, String.valueOf(latitude));
        editor.apply();
    }

    public static double getLatitude(Context context) {
        String latitude = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                LATITUDE, "0.0");
        return Double.parseDouble(latitude);
    }

    public static void setLocationUpload(Context context, boolean flag) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOCATION_UPLOAD, flag);
        editor.apply();
    }

    public static boolean getLocationUpload(Context context) {
        boolean flag = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getBoolean(LOCATION_UPLOAD, false);
        return flag;
    }


    public static void setLongitude(Context context, double latitude) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LONGITUDE, String.valueOf(latitude));
        editor.apply();
    }

    public static double getLongitude(Context context) {
        String longitude = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                LONGITUDE, "0.0");
        return Double.parseDouble(longitude);
    }

    public static String getUsername(Context context) {
        String mobNumber = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                USERNAME, "");
        return mobNumber;

    }

    public static void setUsername(Context context, String mobileNumber) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, String.valueOf(mobileNumber));
        editor.apply();
    }

    public static String getPassword(Context context) {
        String mobNumber = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(
                PASSWORD, "");
        return mobNumber;

    }

    public static void setPassword(Context context, String mobileNumber) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PASSWORD, String.valueOf(mobileNumber));
        editor.apply();
    }



}
