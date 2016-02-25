package com.app.traphoria;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.traphoria.login.LandingScreen;
import com.app.traphoria.model.UserDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.TraphoriaPreference;
import com.google.android.gcm.GCMBaseIntentService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import static com.app.traphoria.CommonUtilities.displayMessage;
import static com.app.traphoria.CommonUtilities.SENDER_ID;


public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        TraphoriaPreference.setPushRegistrationId(context, registrationId);
        displayMessage(context, "Your device registred with GCM");
        // Log.d("NAME", MainActivity.name);
        // ServerUtilities.register(context, MainActivity.name,
        //	MainActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        //ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {

        String message = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
        Log.i(TAG, "Received message :" + message);

        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context,
                getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void generateNotification(Context context, String message) {
//        int icon = R.mipmap.ic_launcher;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        // Notification notification = new Notification(icon, message, when);
//
//        String title = message;
//
//        Intent notificationIntent = new Intent(context, LandingScreen.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);
//        //notification.setLatestEventInfo(context, title, message, intent);
//        //notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        //  notification.defaults |= Notification.DEFAULT_SOUND;
//
//        // Vibrate if vibrate is enabled
//        //  notification.defaults |= Notification.DEFAULT_VIBRATE;
//
//
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            notification = new Notification();
//            notification.icon = R.mipmap.ic_launcher;
//            try {
//                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
//                deprecatedMethod.invoke(notification, context, title, message, intent);
//            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
//                    | InvocationTargetException e) {
//                Log.w(TAG, "Method not found", e);
//            }
//        } else {
//            // Use new API
//            Notification.Builder builder = new Notification.Builder(context)
//                    .setContentIntent(intent)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title).setContentText(message);
//            notification = builder.build();
//        }
//
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notificationManager.notify(0, notification);


        if (message != null) {
            int i = (int) Calendar.getInstance().getTimeInMillis();
            int icon = R.mipmap.ic_launcher;
            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(icon);
            builder.setContentText(message);
            builder.setWhen(when);
            String title = context.getString(R.string.app_name);
            builder.setContentTitle(title);
            UserDTO userDTO = TraphoriaPreference.getObjectFromPref(context, PreferenceConstant.USER_INFO);
            Intent notificationIntent = null;
            PendingIntent intent = null;
            String[] messageArray = message.split(":");
            builder.setContentText(messageArray[3]);

            if (userDTO != null) {
                if (messageArray[1].equalsIgnoreCase("CHAT")) {

                    notificationIntent = new Intent(context,
                            NavigationDrawerActivity.class);
                    notificationIntent.putExtra("fragmentNumber", 1);
                    notificationIntent.putExtra("subFragmentNumber", 0);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(notificationIntent);

                } else if (messageArray[1].equalsIgnoreCase("addMember")) {
                    notificationIntent = new Intent(context,
                            NavigationDrawerActivity.class);
                    notificationIntent.putExtra("fragmentNumber", 1);
                    notificationIntent.putExtra("subFragmentNumber", 1);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(notificationIntent);
                }

                builder.setContentIntent(intent);
            }
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;

            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(i, notification);


        }


    }

}