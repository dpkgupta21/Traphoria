package com.app.traphoria.webservice;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginWebService {


    private String TAG = "LOGIN";
    private Context context;
    private String email;
    private String password;
    private String device;
    private String device_id;
    private String lat;
    private String lng;
    private String address;

    public LoginWebService(Context context, String email, String password, String device, String device_id, String lat, String lng,String address) {

        this.context = context;
        this.email = email;
        this.password = password;
        this.address = address;
        this.device = device;
        this.device_id = device_id;
        this.lat = lat;
        this.lng = lng;
        this.address =address;
    }


    public JSONObject doLogin() {
        Utils.hideKeyboard((Activity) context);
        final JSONObject[] responseResult = new JSONObject[1];
        Map<String, String> params = new HashMap<>();
        params.put("action", WebserviceConstant.DO_LOGIN);
        params.put("email", email);
        params.put("password", password);
        params.put("device", device);
        params.put("device_id", device_id);
        params.put("lat", "" + lat);
        params.put("lng", "" + lng);
        params.put("address", "");

        final ProgressDialog pdialog = Utils.createProgressDialog(context, null, false);
        CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.ShowLog(TAG, "Response -> " + response.toString());
                        pdialog.dismiss();
                        try {
                            responseResult[0] = response;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.dismiss();
                Utils.showExceptionDialog(context);
            }
        });
        pdialog.show();
        AppController.getInstance().getRequestQueue().add(postReq);
        postReq.setRetryPolicy(new DefaultRetryPolicy(
                30000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        return responseResult[0];
    }





}
