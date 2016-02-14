package com.app.traphoria.customViews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.view.View;


public class CustomSnackbar {

    public static void showSnackbar(View mView, String message) {

        Snackbar snackbar = Snackbar
                .make(mView, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }



}