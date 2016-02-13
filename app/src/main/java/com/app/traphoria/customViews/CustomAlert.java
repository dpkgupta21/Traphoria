package com.app.traphoria.customViews;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.app.traphoria.R;

import java.lang.reflect.Method;


public class CustomAlert {

    private Context context;

    public CustomAlert(Context context) {
        this.context = context;
    }

    public void singleButtonAlertDialog(String msg,
                                        String positiveBtn, final String callbackFunc, final Integer requestCode) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customdialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView txt_msg = (TextView) view.findViewById(R.id.alertMsg);
        txt_msg.setText(msg);

        Button positiveButton = (Button) view.findViewById(R.id.alertBtn);
        positiveButton.setText(positiveBtn);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(callbackFunc)) {
                    try {
                        Class<?>[] paramTypes = {Boolean.class, int.class};
                        Method callback = context.getClass().
                                getMethod(callbackFunc, paramTypes);
                        callback.invoke(context, true, requestCode);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }

    public void doubleButtonAlertDialog(String msg, String positiveBtn, String negativeBtn,
                                        final String callbackFunc, final int requestCode) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doublebtn_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView text_msg = (TextView) view.findViewById(R.id.doubleBtnAlertMsg);
        text_msg.setText(msg);
        Button positive = (Button) view.findViewById(R.id.dblBtnAlert_positveBtn);
        Button negative = (Button) view.findViewById(R.id.dblBtnAlert_negativeBtn);

        positive.setText(positiveBtn);
        negative.setText(negativeBtn);

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(callbackFunc)) {
                    try {
                        Method callbackMethod = context.getClass().getMethod(callbackFunc,
                                Boolean.class, int.class);
                        callbackMethod.invoke(context, true, requestCode);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(callbackFunc)) {
                    try {
                        Method callbackMethod = context.getClass().getMethod(callbackFunc,
                                Boolean.class, int.class);
                        callbackMethod.invoke(context,false, requestCode);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.show();
    }
}
