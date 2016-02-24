package com.app.traphoria.navigationDrawer;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.traphoria.R;
import com.app.traphoria.adapter.SideMenuListAdapter;
import com.app.traphoria.alert.AlertsScreenFragment;
import com.app.traphoria.customViews.CustomAlert;
import com.app.traphoria.locationservice.LocationScreenFragment;
import com.app.traphoria.lacaldabase.Handler;
import com.app.traphoria.login.LoginScreen;
import com.app.traphoria.member.MembersScreenFragment;
import com.app.traphoria.preference.PreferenceConstant;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.trip.MyTripListScreenFragment;
import com.app.traphoria.search.SearchDestinationFragment;
import com.app.traphoria.settings.SettingsScreenFragment;
import com.app.traphoria.task.TaskScreenFragment;
import com.app.traphoria.passportvisa.ViewPassportVisaScreenFragment;
import com.app.traphoria.utility.SessionManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


public class NavigationDrawerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentSelectedPosition;
    public static Activity mActivity;
    private TextView mTitle, mRightTextView;
    private ImageView back_btn, down_btn, logout_btn;
    private ListView mListView;
    public static Context context;
    private View navigationHeaderView;
    private boolean backPressedToExitOnce = false;

    private SideMenuListAdapter menuListAdapter;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_nav_drawer_activity);
        initViews();
        assignClickOnView();

        int fragmentNumber = getIntent().getIntExtra("fragmentNumber", 0);
        displayView(fragmentNumber);

        setHeaderValues();


    }


    private void initViews() {

        new Thread(new Handler(getApplicationContext())).start();
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
        logout_btn = (ImageView) mNavigationView.findViewById(R.id.logout_btn);
        mListView = (ListView) mNavigationView.findViewById(R.id.side_menu_list);
        navigationHeaderView = (View) mNavigationView.findViewById(R.id.header);
        down_btn = (ImageView) findViewById(R.id.down_btn);
        menuListAdapter = new SideMenuListAdapter(this);
        mListView.setAdapter(menuListAdapter);
        mListView.setOnItemClickListener(this);
        mDrawerToggle.syncState();

        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
                .build();


    }

    private void assignClickOnView() {


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOutDialog();
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
                title = PreferenceHelp.getUserName(NavigationDrawerActivity.this);
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
                fragment = new MyTripListScreenFragment();
                title = "My Trips";
                break;
            case 4:
                fragment = new TaskScreenFragment();
                title = "Task";
                break;
            case 5:
                down_btn.setVisibility(View.VISIBLE);
                fragment = new MembersScreenFragment();
                title = PreferenceHelp.getUserName(this);
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

    private void setHeaderValues() {
        ((TextView) navigationHeaderView.findViewById(R.id.txt_name)).setText(PreferenceHelp.getUserName(this));


        ((TextView) navigationHeaderView.findViewById(R.id.txt_age_gender)).setText(PreferenceHelp.getUserAgeSex(this).equalsIgnoreCase("M")?"Male":"Female");
        ImageView imageView = (ImageView) navigationHeaderView.findViewById(R.id.img_user_image);
        ImageLoader.getInstance().displayImage(PreferenceHelp.getUserImage(this), imageView,
                options);




        ((ImageView)navigationHeaderView.findViewById(R.id.call_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT
                        && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
                    // The phone has SIM card
                    // No SIM card on the phone

                    //String phnum = car.getNumber();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + PreferenceHelp.getFamily(NavigationDrawerActivity.this)));
                    if (ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);

                } else {
                    Toast.makeText(NavigationDrawerActivity.this, "No Sim Card",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

        ((ImageView)navigationHeaderView.findViewById(R.id.call_cancel_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT
                        && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
                    // The phone has SIM card
                    // No SIM card on the phone

                    //String phnum = car.getNumber();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + PreferenceHelp.getEmergency(NavigationDrawerActivity.this)));
                    if (ActivityCompat.checkSelfPermission(NavigationDrawerActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);

                } else {
                    Toast.makeText(NavigationDrawerActivity.this, "No Sim Card",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
             super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);


        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(NavigationDrawerActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);
        }
    }


    private void showLogOutDialog() {
        new CustomAlert(NavigationDrawerActivity.this)
                .doubleButtonAlertDialog(
                        getString(R.string.you_logout),
                        getString(R.string.ok_button),
                        getString(R.string.canceled), "dblBtnCallbackResponse", 1000);
    }

    public void dblBtnCallbackResponse(Boolean flag, int code) {
        if (flag) {
            SessionManager.logoutUser(mActivity);
//            TraphoriaPreference.removeObjectIntoPref(NavigationDrawerActivity.this, PreferenceConstant.USER_INFO);
//            Intent intent = new Intent(NavigationDrawerActivity.this, LoginScreen.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }

    }
}
