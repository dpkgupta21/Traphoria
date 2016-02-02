package com.app.traphoria.navigationDrawer;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.adapter.SideMenuListAdapter;
import com.app.traphoria.fragments.AlertsScreenFragment;
import com.app.traphoria.fragments.LocationScreenFragment;
import com.app.traphoria.fragments.MembersScreenFragment;
import com.app.traphoria.fragments.MytripScreenFragment;
import com.app.traphoria.fragments.SearchDestinationFragment;
import com.app.traphoria.fragments.SettingsScreenFragment;
import com.app.traphoria.fragments.TaskScreenFragment;
import com.app.traphoria.fragments.ViewPassportVisaScreenFragment;


public class NavigationDrawerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentSelectedPosition;
    public static Activity mActivity;
    private TextView mTitle, mRightTextView;
    private ImageView back_btn, down_btn;
    private ListView mListView;
    public static Context context;

    private SideMenuListAdapter menuListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_nav_drawer_activity);
        initViews();
        assignClickOnView();
        displayView(0);


    }


    private void initViews() {
        context = this;
        mActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mRightTextView = (TextView) findViewById(R.id.toolbar_right_tv);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        back_btn = (ImageView) mNavigationView.findViewById(R.id.back_btn);
        mListView = (ListView) mNavigationView.findViewById(R.id.side_menu_list);
        down_btn = (ImageView) findViewById(R.id.down_btn);
        menuListAdapter = new SideMenuListAdapter(this);
        mListView.setAdapter(menuListAdapter);
        mListView.setOnItemClickListener(this);
        mDrawerToggle.syncState();
    }

    private void assignClickOnView() {


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }


    private void displayView(int position) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        mRightTextView.setVisibility(View.GONE);
        down_btn.setVisibility(View.GONE);
        switch (position) {

            case 0:
                fragment = new SearchDestinationFragment();
                title = "Alnasir";
                break;
            case 1:
                fragment = new AlertsScreenFragment();
                title = "Alerts";

                break;
            case 2:
                fragment = new ViewPassportVisaScreenFragment();
                title = "Passport & Visa";
                break;
            case 3:
                fragment = new MytripScreenFragment();
                title = "My Trips";
                break;
            case 4:
                fragment = new TaskScreenFragment();
                title = "Task";
                break;
            case 5:
                down_btn.setVisibility(View.VISIBLE);
                fragment = new MembersScreenFragment();
                title = "Abdul Naseer";
                break;
            case 6:
                fragment = new LocationScreenFragment();
                title = "Location Services";
                break;

            case 7:
                fragment = new SettingsScreenFragment();
                title = "Settings";
                break;
            default:
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.body_layout, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(" ");
            mTitle.setText(title);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                displayView(0);
                break;
            case 1:
                displayView(1);

                break;
            case 2:
                displayView(2);

                break;
            case 3:
                displayView(3);

                break;
            case 4:
                displayView(4);

                break;
            case 5:
                displayView(5);

                break;
            case 6:
                displayView(6);

                break;
            case 7:
                displayView(7);

                break;
        }
    }
}
