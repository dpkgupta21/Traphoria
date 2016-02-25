package com.app.traphoria;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // give your server registration url here
    // public static final String SERVER_URL =
    // "http://192.168.0.105/gcm/gcm.php?shareRegId=1";

    // Google project id
    public static final String SENDER_ID = "249909297075";
    //Api KEY
    public static final String API_KEY = "AIzaSyAo93AacXR5uwmox1tBkcUzhir_QYrQGpg";
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "TRAPHORIA GCM";

    public static final String DISPLAY_MESSAGE_ACTION = "com.app.traphoria.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by the
     * UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
