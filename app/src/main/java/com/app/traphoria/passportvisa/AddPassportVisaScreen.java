package com.app.traphoria.passportvisa;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.lacaldabase.PassportTypeDataSource;
import com.app.traphoria.lacaldabase.RelationDataSource;
import com.app.traphoria.member.adapter.RelationAdapter;
import com.app.traphoria.model.PassportTypeDTO;
import com.app.traphoria.passportvisa.adapter.PassportTypeAdapter;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseActivity;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

import java.util.Calendar;
import java.util.List;

public class AddPassportVisaScreen extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTitle;

    private TextView visa_btn, passport_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_passport_visa_screen);

        initView();
        assignClick();
        init();
    }

    private void init() {
        String id = getIntent().getStringExtra("id");
        String type = getIntent().getStringExtra("type");

        if ((id != null && !id.equalsIgnoreCase("")) && (type != null && !type.equalsIgnoreCase(""))) {

            if (type.equalsIgnoreCase("P")) {
                visa_btn.setBackground(getResources().getDrawable(R.drawable.grey_background));
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                openFragment(0, id);
            } else {
                visa_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
                visa_btn.setTextColor(getResources().getColor(R.color.white));
                passport_btn.setBackground(getResources().getDrawable(R.drawable.grey_background));
                passport_btn.setTextColor(getResources().getColor(R.color.black));
                openFragment(1, id);
            }
        } else {
            openFragment(0, "");
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
                openFragment(1, "");
                break;

            case R.id.passport_btn:
                visa_btn.setBackground(getResources().getDrawable(R.drawable.grey_background));
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                openFragment(0, "");
                break;

        }
    }


    private void openFragment(int flag, String id) {
        Fragment fragment = null;

        if (flag == 0) {
            fragment = PassportFragment.newInstance(id);
        } else {
            fragment = VisaFragment.newInstance(id);
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
