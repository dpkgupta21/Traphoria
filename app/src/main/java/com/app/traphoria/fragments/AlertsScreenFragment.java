package com.app.traphoria.fragments;

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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.app.traphoria.R;
import com.app.traphoria.adapter.MessagesAdapter;
import com.app.traphoria.adapter.NotificationAdapter;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class AlertsScreenFragment extends Fragment implements View.OnClickListener, SwipeMenuListView.OnMenuItemClickListener {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;
    private LinearLayout notification_ll, message_ll;
    private TextView message_tv, notification_tv;
    private ImageView notification_icon, message_icon;
    private SwipeMenuListView notification_lv;

    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private NotificationAdapter notificationAdapter;

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
        messagesAdapter = new MessagesAdapter(mActivity);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(messagesAdapter);

        notification_lv = (SwipeMenuListView) view.findViewById(R.id.notification_lv);
        notificationAdapter = new NotificationAdapter(mActivity);
        createSwipeMenu();
        notification_lv.setAdapter(notificationAdapter);

        notification_lv.setOnMenuItemClickListener(this);
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

                        break;
                    case 1:
                        Snackbar.make(view, "Cross Clicked", Snackbar.LENGTH_SHORT).show();

                        break;
                }
                break;
        }

        return false;
    }
}