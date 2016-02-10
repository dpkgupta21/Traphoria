package com.app.traphoria.trip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.traphoria.R;

public class AddNewTripScreen extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Toolbar mToolbar;
    private TextView mTitle;

    private TextView select_country, select_passport, select_visa, add_member_tv, add_member;
    private EditText start_date_et, end_date_et;
    private RadioButton alone_radio_btn, group_radio_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_trip_screen);
        initView();
        assignClick();
    }

    private void assignClick() {

        alone_radio_btn.setOnCheckedChangeListener(this);
        group_radio_btn.setOnCheckedChangeListener(this);

    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.add_new_trip);
        select_country = (TextView) findViewById(R.id.select_country);
        select_passport = (TextView) findViewById(R.id.select_passport);
        select_visa = (TextView) findViewById(R.id.select_visa);
        add_member_tv = (TextView) findViewById(R.id.add_member_tv);
        add_member = (TextView) findViewById(R.id.add_member);
        start_date_et = (EditText) findViewById(R.id.start_date_et);
        end_date_et = (EditText) findViewById(R.id.end_date);
        group_radio_btn = (RadioButton) findViewById(R.id.group_radio_btn);
        alone_radio_btn = (RadioButton) findViewById(R.id.alone_radio_btn);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.alone_radio_btn:

                add_member.setVisibility(View.VISIBLE);
                add_member_tv.setVisibility(View.VISIBLE);
                break;
            case R.id.group_radio_btn:
                add_member.setVisibility(View.GONE);
                add_member_tv.setVisibility(View.GONE);
                break;
        }


    }
}
