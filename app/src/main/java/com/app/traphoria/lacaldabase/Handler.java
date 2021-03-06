package com.app.traphoria.lacaldabase;


import android.content.Context;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.MenuDTO;
import com.app.traphoria.model.NotificationDurationDTO;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.model.TripCountryDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.model.VisaTypeDTO;
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

    public static final int NOTIFICATION_DURATION_HANDLER = 1003;
    private static final String TAG = "Handler";
    private Context mActivity;
    private android.os.Handler handler;

    public Handler(android.os.Handler handler, Context mActivity) {
        this.mActivity = mActivity;
        this.handler = handler;
    }

    @Override
    public void run() {
        getTripCountryValues();
        getNotificationDuration();
        getRelationValues();
        getMemberValues();
        getPassportType();
        getVisaType();
        getPassportVisa();
    }


    private void getRelationValues() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_RELATION_LIST);
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


    private void getNotificationDuration() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_NOTIFICATION_DURATION);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<NotificationDurationDTO>>() {
                                }.getType();
                                List<NotificationDurationDTO> tripCountryList = new Gson().fromJson(response.getJSONArray("duration").toString(), type);
                                insertNotificationList(tripCountryList);
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


    private void getPassportType() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_PASSPORT_TYPE);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<PassportTypeDTO>>() {
                                }.getType();
                                List<PassportTypeDTO> tripCountryList = new Gson().fromJson(response.getJSONArray("passport_type").toString(), type);
                                insertPassportType(tripCountryList);
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

    private void getVisaType() {
        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_VISA_TYPE);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<VisaTypeDTO>>() {
                                }.getType();
                                List<VisaTypeDTO> tripCountryList = new Gson().fromJson(response.getJSONArray("visa_type").toString(), type);
                                insertVisaType(tripCountryList);
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


    private void getPassportVisa() {

        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_PASSPORT_VISA_DETAILS);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            List<PassportDTO> passportList = null;
                            List<VisaDTO> visaList = null;
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<PassportDTO>>() {
                                }.getType();

                                Type type1 = new TypeToken<ArrayList<VisaDTO>>() {
                                }.getType();


                                passportList = new Gson().fromJson(response.getJSONArray("Passport").toString(), type);
                                visaList = new Gson().fromJson(response.getJSONArray("Visa").toString(), type1);
                                insetPassportVisaDetails(passportList, visaList);
                            } catch (Exception e) {
                                insetPassportVisaDetails(passportList, visaList);
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

    private void insertNotificationList(List<NotificationDurationDTO> list) {
        new NotificationDataSource(mActivity).insertNotificationDuration(list);
        handleNotificationtResponse(true);
    }


    private void insertPassportType(List<PassportTypeDTO> list) {
        new PassportTypeDataSource(mActivity).insertPassportType(list);
    }

    private void insertVisaType(List<VisaTypeDTO> list) {
        new VisaTypeDataSource(mActivity).insertVisaType(list);
    }

    private void insetPassportVisaDetails(List<PassportDTO> passportList, List<VisaDTO> visaList) {
        if (passportList != null && passportList.size() > 0) {
            new PassportDataSource(mActivity).insertPassport(passportList);
        }
        if (visaList != null && visaList.size() > 0) {
            new VisaDataSource(mActivity).insertVisa(visaList);
        }
    }

    private void handleNotificationtResponse(boolean isNotificationInsert) {
        Utils.ShowLog(TAG, "handleNotificationDurationResponse");
        Message msg = handler.obtainMessage(NOTIFICATION_DURATION_HANDLER, isNotificationInsert);
        handler.sendMessage(msg);

    }
}
