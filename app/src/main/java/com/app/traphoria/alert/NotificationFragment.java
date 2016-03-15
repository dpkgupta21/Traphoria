package com.app.traphoria.alert;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.alert.adapter.NotificationAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.NotificationDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotificationFragment extends BaseFragment implements SwipeMenuListView.OnMenuItemClickListener {

    private View view;
    private String TAG = "NOTIFICATION";
    private List<NotificationDTO> notificationList;
    private NotificationAdapter notificationAdapter;
    private SwipeMenuListView notification_lv;


    public NotificationFragment() {
    }


    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notification_lv = (SwipeMenuListView) view.findViewById(R.id.notification_lv);
        getNotificationList();
    }


    private void getNotificationList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_NOTIFICATION_LIST);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<NotificationDTO>>() {
                                }.getType();
                                notificationList = new Gson().fromJson(response.getJSONArray("notifications").toString(), type);
                                setNotificationValues();

                            } catch (Exception e) {
                                CustomProgressDialog.hideProgressDialog();
                                setNotificationValues();
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(getActivity(), null);
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }
    }


    private void setNotificationValues() {


        if (notificationList != null && notificationList.size() > 0) {
            setViewVisibility(R.id.no_trip_tv, view, View.GONE);
            notificationAdapter = new NotificationAdapter(getActivity(), notificationList);
            createSwipeMenu();
            notification_lv.setAdapter(notificationAdapter);
            notification_lv.setOnMenuItemClickListener(this);
        } else {
            notification_lv.setVisibility(View.GONE);
            setViewVisibility(R.id.no_trip_tv, view, View.VISIBLE);
        }

    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        // create "check" item
                        SwipeMenuItem checkItem = new SwipeMenuItem(
                                getActivity());
                        // set item background
                        checkItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                                0xFF, 0xFF)));
                        // set item width
                        checkItem.setWidth(convert_dp_to_px(50));
                        // set a icon
                        checkItem.setIcon(R.drawable.check_circle_icon);
                        // add to menu
                        menu.addMenuItem(checkItem);


                        // create "check" item
                        SwipeMenuItem crossItem = new SwipeMenuItem(
                                getActivity());
                        // set item background
                        crossItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                                0xFF, 0xFF)));
                        // set item width
                        crossItem.setWidth(convert_dp_to_px(50));
                        // set a icon
                        crossItem.setIcon(R.drawable.croos_circle_icon);
                        // add to menu
                        menu.addMenuItem(crossItem);
                        break;
                    case 1:
                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getActivity());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF,
                                0xFF, 0xFF)));
                        // set item width
                        deleteItem.setWidth(convert_dp_to_px(50));
                        // set a icon
                        deleteItem.setIcon(R.drawable.circle_delete_icon);
                        // add to menu
                        menu.addMenuItem(deleteItem);
                        break;


                }
            }
        };

// set creator
        notification_lv.setMenuCreator(creator);


    }

    private int convert_dp_to_px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

        switch (menu.getViewType()) {
            case 1:
                Snackbar.make(view, "Delete Clicked", Snackbar.LENGTH_SHORT).show();
                doMemberAction(position, 0, false);
                break;
            case 0:

                switch (index) {
                    case 0:
                        Snackbar.make(view, "Check Clicked", Snackbar.LENGTH_SHORT).show();
                        doMemberAction(position, 1, true);
                        break;
                    case 1:
                        Snackbar.make(view, "Cross Clicked", Snackbar.LENGTH_SHORT).show();
                        doMemberAction(position, 0, true);
                        break;
                }
                break;
        }

        return false;
    }


    private void doMemberAction(int position, int status, boolean isAddMemberAction) {
        if (Utils.isOnline(getActivity())) {
            String senderId=notificationList.get(position).getSender_id();
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.DO_APPROVE_DECLINE_MEMBER);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));
            params.put("notification_id", notificationList.get(position).getNotification_id());
            if (isAddMemberAction) {
                params.put("sender_id", senderId);
            }else{
                params.put("sender_id", "0");
            }
            params.put("status", "" + status);

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                if (Utils.getWebServiceStatus(response)) {
                                    getNotificationList();
                                    //Toast.makeText(getActivity(), "Action done", Toast.LENGTH_LONG).show();
                                } else {
                                    CustomProgressDialog.hideProgressDialog();
                                    Utils.showDialog(getActivity(), "Error", Utils.getWebServiceMessage(response));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(getActivity(), null);
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }


    }

}
