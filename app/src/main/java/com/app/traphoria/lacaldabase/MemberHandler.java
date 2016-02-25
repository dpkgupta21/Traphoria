package com.app.traphoria.lacaldabase;


import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.MemberDTO;
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

public class MemberHandler implements Runnable {

    private static final String TAG = "MenuCountHandler";
    private Context mActivity;

    public MemberHandler(Context mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        getMemberValues();
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


    private void insertMemberList(List<MemberDTO> list) {
        new MemberDataSource(mActivity).insertMember(list);
    }
}
