package com.app.traphoria.preference;


import android.content.Context;

import com.app.traphoria.model.UserDTO;

public class PreferenceHelp {

    public static String getUserId(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
        if (userDTO != null)
            return userDTO.getId();
        else
            return "0";
    }

    public static String getUserName(Context context) {
        UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
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


}
