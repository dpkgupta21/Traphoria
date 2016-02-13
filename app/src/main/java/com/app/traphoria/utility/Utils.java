package com.app.traphoria.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.app.traphoria.customViews.CustomAlert;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {


    public static final void hideKeyboard(Activity ctx) {

        if (ctx.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static final boolean isOnline(Context context) {

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null

                && conMgr.getActiveNetworkInfo().isAvailable()

                && conMgr.getActiveNetworkInfo().isConnected())
            return true;
        return false;
    }


    public static ProgressDialog createProgressDialog(Context context, String message, boolean isCancelable) {
        ProgressDialog pdialog = new ProgressDialog(context);
        if (message == null)
            pdialog.setMessage("Loading....");
        else
            pdialog.setMessage(message);
        pdialog.setIndeterminate(true);
        pdialog.setCancelable(isCancelable);
        return pdialog;
    }


    public static void showNoNetworkDialog(Context ctx) {

//        showDialog(ctx, "No Network Connection",
//                "Internet is not available. Please check your network connection.")
//                .show();

        custumDialog("Internet is not available. Please check your network connection.", ctx);
    }

    public static void showExceptionDialog(Context ctx) {

//        showDialog(ctx, "Error",
//                "Some Error occured. Please try later.")
//                .show();

        custumDialog("Some Error occured. Please try later.", ctx);

    }


    public static AlertDialog showDialog(Context ctx, String title, String msg) {

        return showDialog(ctx, title, msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

    }


    public static AlertDialog showDialog(Context ctx, String title, String msg,
                                         DialogInterface.OnClickListener listener) {

        return showDialog(ctx, title, msg, "Ok", null, listener, null);
    }


    public static AlertDialog showDialog(Context ctx, String title, String msg,
                                         String btn1, String btn2, DialogInterface.OnClickListener listener) {

        return showDialog(ctx, title, msg, btn1, btn2, listener,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

    }


    public static AlertDialog showDialog(Context ctx, String title, String msg,
                                         String btn1, String btn2,
                                         DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }


    public static void ShowLog(String tag, String response) {
        Log.i(tag, response);
    }


    public static boolean getWebServiceStatus(JSONObject json) {
        try {
            return json.getBoolean("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getWebServiceMessage(JSONObject json) {

        try {
            return json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Error";
    }


    public static void custumDialog(String msg, Context context) {
        new CustomAlert(context).singleButtonAlertDialog(msg, "Ok", null, 0);

    }

}
