package com.app.traphoria.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.traphoria.R;

public class AddPassportVisaScreen extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTitle;

    private TextView visa_btn, passport_btn;

    private LinearLayout passport_form, visa_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_passport_visa_screen);
        initView();
        assignClick();
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
        passport_form = (LinearLayout) findViewById(R.id.passport_form);
        visa_form = (LinearLayout) findViewById(R.id.visa_form);
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
                visa_form.setVisibility(View.VISIBLE);
                passport_form.setVisibility(View.GONE);
                break;

            case R.id.passport_btn:
                visa_btn.setBackground(getResources().getDrawable(R.drawable.grey_background));
                visa_btn.setTextColor(getResources().getColor(R.color.black));
                passport_btn.setBackground(getResources().getDrawable(R.drawable.purple_background));
                passport_btn.setTextColor(getResources().getColor(R.color.white));
                visa_form.setVisibility(View.GONE);
                passport_form.setVisibility(View.VISIBLE);
                break;
        }
    }


}
