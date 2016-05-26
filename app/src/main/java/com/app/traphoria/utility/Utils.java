package com.app.traphoria.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomAlert;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

//    public static void displayImageLoader(String uri, ImageView imgView, DisplayImageOptions options){
//        try {
//            ImageLoader.getInstance().displayImage(uri, imgView,
//                    options, new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String s, View view) {
//
//                            ((ImageView) view).setImageResource(R.drawable.login_bg);
//                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
//                            ((ImageView) view).setPadding(0, 20, 0, 20);
//
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String s, View view, FailReason failReason) {
//                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
//                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
//                            ((ImageView) view).setPadding(0, 20, 0, 20);
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String s, View view) {
////                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
////                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
////                            ((ImageView) view).setPadding(0, 20, 0, 20);
//                        }
//
//                    }, new ImageLoadingProgressListener() {
//                        @Override
//                        public void onProgressUpdate(String s, View view, int i, int i1) {
//
//                        }
//                    });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    public static float px2dp(Resources resource, float px)  {
        return (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, resource.getDisplayMetrics());
    }


    public static final SpannableString underlineText(String value) {
        String upperCaseValue=value.toUpperCase();
        SpannableString content = new SpannableString(upperCaseValue);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        return content;
    }


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

        customDialog("Internet is not available. Please check your network connection.", ctx);
    }

    public static void showExceptionDialog(Context ctx) {

//        showDialog(ctx, "Error",
//                "Some Error occured. Please try later.")
//                .show();

        customDialog("Some Error occured. Please try later.", ctx);

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


    public static void customDialog(String msg, Context context) {
        new CustomAlert(context).singleContextButtonAlertDialog(msg, "Ok", null, 0);

    }

    public static boolean isDOBValid(String fromDate, String toDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date1 = sdf.parse(fromDate);
            Date date2 = sdf.parse(toDate);
            if (date2.after(date1)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFromDateGreater(String fromDate, String toDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = sdf.parse(fromDate);
            Date date2 = sdf.parse(toDate);
            if (date2.after(date1)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isValidEmail(String email) {

        String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
        Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("countrycode.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String secondsToDate(String seconds) {
        int second = Integer.parseInt(seconds);
        Date date = new Date(second * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM | h:mm a");
        return sdf.format(date);
    }


    public static String locationUrl(String latitude, String longitude, String type, String radius) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + radius + "&types=" + type + "&sensor=true&key=AIzaSyAhm1YfISuWPrwKHOBx7OGGCuc6WdSryWk";

        return url;
    }


}
