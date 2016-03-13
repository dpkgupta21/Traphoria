package com.app.traphoria.passportvisa;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseActivity;

public class AddPassportVisaScreen extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private Activity mActivity;
    private String userId;
    private TextView visa_btn, passport_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_passport_visa_screen);

        mActivity = AddPassportVisaScreen.this;
        initView();
        assignClick();
        init();
    }

    private void init() {
        String id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");
        boolean isEditPassport = getIntent().getBooleanExtra("isEditPassport", false);
        boolean isEditVisa = getIntent().getBooleanExtra("isEditVisa", false);
        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.equalsIgnoreCase("")) {
            userId = PreferenceHelp.getUserId(mActivity);
        }


        if ((id != null && !id.equalsIgnoreCase("") && !id.equalsIgnoreCase("0"))
                && (type != null && !type.equalsIgnoreCase(""))) {

            if (type.equalsIgnoreCase("P")) {
                visa_btn.setBackgroundResource(R.drawable.grey_background);
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackgroundResource(R.drawable.purple_background);
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                openFragment(0, id, userId);
            } else {
                visa_btn.setBackgroundResource(R.drawable.purple_background);
                visa_btn.setTextColor(getResources().getColor(R.color.white));
                passport_btn.setBackgroundResource((R.drawable.grey_background));
                passport_btn.setTextColor(getResources().getColor(R.color.black));
                openFragment(1, id, userId);
            }
        } else {
            if (type == null) {
                visa_btn.setBackgroundResource(R.drawable.grey_background);
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackgroundResource(R.drawable.purple_background);
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                openFragment(0, "", userId);
            } else if (type.equalsIgnoreCase("P")) {
                visa_btn.setBackgroundResource(R.drawable.grey_background);
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackgroundResource(R.drawable.purple_background);
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                openFragment(0, "", userId);
            } else if (type.equalsIgnoreCase("V")) {
                visa_btn.setBackgroundResource(R.drawable.purple_background);
                visa_btn.setTextColor(getResources().getColor(R.color.white));
                passport_btn.setBackgroundResource((R.drawable.grey_background));
                passport_btn.setTextColor(getResources().getColor(R.color.black));
                openFragment(1, "", userId);
            }
        }

        if (isEditPassport) {
            setViewEnable(R.id.passport_btn, false);
            setViewEnable(R.id.visa_btn, false);

        } else if (isEditVisa) {
            setViewEnable(R.id.passport_btn, false);
            setViewEnable(R.id.visa_btn, false);

        }

    }

    private void assignClick() {

        visa_btn.setOnClickListener(this);
        passport_btn.setOnClickListener(this);
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        passport_btn = (TextView) findViewById(R.id.passport_btn);
        visa_btn = (TextView) findViewById(R.id.visa_btn);
        mTitle.setText(R.string.pasport_visa);
        passport_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
        passport_btn.setTextColor(getResources().getColor(R.color.white));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.visa_btn:

                visa_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
                visa_btn.setTextColor(getResources().getColor(R.color.white));
                passport_btn.setBackground(getResources().getDrawable(R.drawable.grey_background));
                passport_btn.setTextColor(getResources().getColor(R.color.black));
                openFragment(1, "", userId);
                break;

            case R.id.passport_btn:
                visa_btn.setBackground(getResources().getDrawable(R.drawable.grey_background));
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                openFragment(0, "", userId);
                break;

        }
    }


    private void openFragment(int flag, String id, String userId) {
        Fragment fragment = null;
        boolean isEditPassport = getIntent().getBooleanExtra("isEditPassport", false);
        boolean isEditVisa = getIntent().getBooleanExtra("isEditVisa", false);
        boolean isMember = getIntent().getBooleanExtra("isMember", false);

        if (flag == 0) {
            fragment = PassportFragment.newInstance(id, isEditPassport, userId, isMember);
        } else {
            fragment = VisaFragment.newInstance(id, isEditVisa, userId, isMember);
        }

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.commit();
        }
    }


}
