package com.app.traphoria.preference;


import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

public class TraphoriaPreference {


    public static final String PREF_NAME = "TRAP_PREFERENCES";
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
}
