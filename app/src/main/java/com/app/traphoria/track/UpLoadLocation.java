package com.app.traphoria.track;


import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.gps.GPSTracker;
import com.app.traphoria.model.UserLocationDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpLoadLocation {

    private Context context;

    public UpLoadLocation(Context context) {
        this.context = context;
        GPSTracker gpsTracker = new GPSTracker(context);
    }

    public void upload() {
        if (Utils.isOnline(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.ADD_LOCATION);
            params.put("user_id", PreferenceHelp.getUserId(context));
            params.put("address", "C-370 Malviya nagar Jaipur Raj 302022");
            params.put("lat", "" + TraphoriaPreference.getLatitude(context));
            params.put("lng", "" + TraphoriaPreference.getLongitude(context));

            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog("Upload Address", "got some response = " + response.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

    }

}