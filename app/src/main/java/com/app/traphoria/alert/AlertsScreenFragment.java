package com.app.traphoria.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.NotificationDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.app.traphoria.R;
import com.app.traphoria.alert.adapter.MessagesAdapter;
import com.app.traphoria.alert.adapter.NotificationAdapter;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlertsScreenFragment extends Fragment implements View.OnClickListener, SwipeMenuListView.OnMenuItemClickListener {


    private View view;
    private String TAG = "Alert Screen";

    private Activity mActivity;
    private Toolbar mToolbar;
    private LinearLayout notification_ll, message_ll;
    private TextView message_tv, notification_tv, no_trip_tv;
    private ImageView notification_icon, message_icon;
    private SwipeMenuListView notification_lv;

    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private NotificationAdapter notificationAdapter;


    private List<NotificationDTO> notificationList;

    public AlertsScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.alert_fragment_screen, container, false);
        mActivity = NavigationDrawerActivity.mActivity;
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.messages_rv);
        message_ll = (LinearLayout) view.findViewById(R.id.message_ll);
        notification_ll = (LinearLayout) view.findViewById(R.id.notification_ll);
        message_icon = (ImageView) view.findViewById(R.id.message_icon);
        notification_icon = (ImageView) view.findViewById(R.id.notification_icon);
        message_tv = (TextView) view.findViewById(R.id.message_tv);
        notification_tv = (TextView) view.findViewById(R.id.notification_tv);
        no_trip_tv = (TextView) view.findViewById(R.id.no_trip_tv);
        messagesAdapter = new MessagesAdapter(mActivity);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(messagesAdapter);
        notification_lv = (SwipeMenuListView) view.findViewById(R.id.notification_lv);
        getNotificationList();
        assignClicks();
        return view;

    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        // create "check" item
                        SwipeMenuItem checkItem = new SwipeMenuItem(
                                mActivity);
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
                                mActivity);
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
                                mActivity);
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

    private void assignClicks() {
        message_ll.setOnClickListener(this);
        notification_ll.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.message_ll:
                message_icon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.sky_message_icon));
                notification_icon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.black_notifiaction));

                message_tv.setTextColor(mActivity.getResources().getColor(R.color.purple));
                notification_tv.setTextColor(mActivity.getResources().getColor(R.color.dark_grey));
                recyclerView.setVisibility(View.VISIBLE);
                notification_lv.setVisibility(View.INVISIBLE);

                break;
            case R.id.notification_ll:
                message_icon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.black_msj_icon));
                notification_icon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.purple_notificaion));

                message_tv.setTextColor(mActivity.getResources().getColor(R.color.dark_grey));
                notification_tv.setTextColor(mActivity.getResources().getColor(R.color.purple));
                recyclerView.setVisibility(View.INVISIBLE);
                notification_lv.setVisibility(View.VISIBLE);

                getNotificationList();
                break;

        }
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
                break;
            case 0:

                switch (index) {
                    case 0:
                        Snackbar.make(view, "Check Clicked", Snackbar.LENGTH_SHORT).show();
                        doMemberAction(position, 1);
                        break;
                    case 1:
                        Snackbar.make(view, "Cross Clicked", Snackbar.LENGTH_SHORT).show();
                        doMemberAction(position, 0);
                        break;
                }
                break;
        }

        return false;
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
            no_trip_tv.setVisibility(View.GONE);
            notificationAdapter = new NotificationAdapter(mActivity, notificationList);
            createSwipeMenu();
            notification_lv.setAdapter(notificationAdapter);
            notification_lv.setOnMenuItemClickListener(this);
        } else {
            notification_lv.setVisibility(View.GONE);
            no_trip_tv.setVisibility(View.VISIBLE);
        }

    }


    private void doMemberAction(int position, int status) {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.DO_APPROVE_DECLINE_MEMBER);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));
            params.put("notification_id", notificationList.get(position).getNotification_id());
            params.put("sender_id", notificationList.get(position).getSender_id());
            params.put("status", "" + status);

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                if (Utils.getWebServiceStatus(response)) {
                                    Toast.makeText(getActivity(), "Action done", Toast.LENGTH_LONG).show();
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


    private void getMessageList() {

    }

}