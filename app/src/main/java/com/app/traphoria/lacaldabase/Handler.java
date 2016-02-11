package com.app.traphoria.lacaldabase;


import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.app.traphoria.preference.PreferenceHelp;
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
import java.util.List;
import java.util.Map;

public class Handler implements Runnable {

    private static final String TAG = "MenuCountHandler";
    private Context mActivity;

    public Handler(Context mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        getRelationValues();
        getMemberValues();
        getTripCountryValues();
    }





    private void getRelationValues() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_RELATION_LIST);
            params.put("address", "");
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<RelationDTO>>() {
                                }.getType();
                                List<RelationDTO> relationList = new Gson().fromJson(response.getJSONArray("relation").toString(), type);
                                insertRelationList(relationList);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showExceptionDialog(mActivity);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

        }

    }


    private void getMemberValues() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_MEMBER_LIST);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<MemberDTO>>() {
                                }.getType();
                                List<MemberDTO> memberList = new Gson().fromJson(response.getJSONArray("memberlist").toString(), type);
                                insertMemberList(memberList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showExceptionDialog(mActivity);
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

        }


    }


    private void getTripCountryValues() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_TRIP_DATA);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<TripCountryDTO>>() {
                                }.getType();
                                List<TripCountryDTO> tripCountryList = new Gson().fromJson(response.getJSONArray("Country").toString(), type);
                                insertCountryList(tripCountryList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.showExceptionDialog(mActivity);
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {

        }


    }


    private void insertRelationList(List<RelationDTO> list) {

        new RelationDataSource(mActivity).insertRelation(list);
    }


    private void insertMemberList(List<MemberDTO> list) {
        new MemberDataSource(mActivity).insertMember(list);
    }


    private void insertCountryList(List<TripCountryDTO> list) {
        new CountryDataSource(mActivity).insertCountry(list);
    }


}
