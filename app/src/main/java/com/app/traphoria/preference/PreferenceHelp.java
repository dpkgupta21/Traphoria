package com.app.traphoria.preference;


import android.content.Context;

import com.app.traphoria.model.UserDTO;

public class PreferenceHelp {

    public static String USER_INFO = "user_info";


    public static String getUserId(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceHelp.USER_INFO);
        if (userDTO != null)
            return userDTO.getId();
        else
            return "0";
    }

    public static String getSocialLogin(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceHelp.USER_INFO);
        if (userDTO != null)
            return userDTO.getSociallogin();
        else
            return "0";
    }

    public static String getUserName(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceHelp.USER_INFO);
        if (userDTO != null)
            return userDTO.getName();
        else
            return "";
    }


    public static String getUserImage(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
        if (userDTO != null)
            return userDTO.getImage();
        else
            return "";
    }

    public static String getUserAgeSex(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
        if (userDTO != null)
            return userDTO.getGender();
        else
            return "";
    }

    public static String getFamily(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
        if (userDTO != null &&
                userDTO.getFamily_contact() != null &&
                !userDTO.getFamily_contact().equalsIgnoreCase(""))
            return userDTO.getCountrycode() + userDTO.getFamily_contact();
        else
            return "";
    }


    public static String getEmergency(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
        if (userDTO != null)
            return userDTO.getEmergency_number();
        else
            return "";
    }
    public static String getCountryCode(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
        if (userDTO != null)
            return userDTO.getCountrycode();
        else
            return "";
    }

}
