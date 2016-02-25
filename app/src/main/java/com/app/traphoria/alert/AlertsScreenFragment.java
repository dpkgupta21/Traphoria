package com.app.traphoria.alert;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.app.traphoria.utility.BaseFragment;
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


public class AlertsScreenFragment extends BaseFragment {


    private View view;
    private String TAG = "Alert Screen";

    private Activity mActivity;
    private Toolbar mToolbar;
    private LinearLayout notification_ll, message_ll;
    private TextView message_tv, notification_tv;
    private ImageView notification_icon, message_icon;
    private int subFragment;

    public AlertsScreenFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subFragment = getArguments().getInt("id");
    }

    public static AlertsScreenFragment newInstance(int id) {
        AlertsScreenFragment fragment = new AlertsScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.alert_fragment_screen, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = NavigationDrawerActivity.mActivity;
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        message_ll = (LinearLayout) view.findViewById(R.id.message_ll);
        notification_ll = (LinearLayout) view.findViewById(R.id.notification_ll);
        message_icon = (ImageView) view.findViewById(R.id.message_icon);
        notification_icon = (ImageView) view.findViewById(R.id.notification_icon);
        message_tv = (TextView) view.findViewById(R.id.message_tv);
        notification_tv = (TextView) view.findViewById(R.id.notification_tv);
        assignClicks();
        openFragment(subFragment);
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
                openFragment(1);
                break;
            case R.id.notification_ll:
                message_icon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.black_msj_icon));
                notification_icon.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.purple_notificaion));

                message_tv.setTextColor(mActivity.getResources().getColor(R.color.dark_grey));
                notification_tv.setTextColor(mActivity.getResources().getColor(R.color.purple));
                openFragment(0);
                break;

        }
    }

    private void openFragment(int flag) {
        Fragment fragment = null;

        if (flag == 0) {
            fragment = NotificationFragment.newInstance();
        } else {
            fragment = MessageFragment.newInstance();
        }
        if (fragment != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.commit();
        }

    }

}