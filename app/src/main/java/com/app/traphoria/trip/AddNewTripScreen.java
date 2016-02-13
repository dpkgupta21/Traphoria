package com.app.traphoria.trip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.trip.Dialog.DialogFragment;
import com.app.traphoria.trip.Dialog.FetchInterface;
import com.app.traphoria.utility.BaseActivity;

import java.util.Calendar;

public class AddNewTripScreen extends BaseActivity implements FetchInterface {

    private Toolbar mToolbar;
    private TextView mTitle;

    private RadioButton alone_radio_btn, group_radio_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        group_radio_btn = (RadioButton) findViewById(R.id.group_radio_btn);
        alone_radio_btn = (RadioButton) findViewById(R.id.alone_radio_btn);
        setClick(R.id.select_country);
        setClick(R.id.start_date_et);
        setClick(R.id.end_date);
        setClick(R.id.select_passport);
        setClick(R.id.select_visa);
        setClick(R.id.add_member);

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
                setViewVisibility(R.id.add_member_tv, View.VISIBLE);
                setViewVisibility(R.id.add_member, View.VISIBLE);
                break;
            case R.id.group_radio_btn:
                setViewVisibility(R.id.add_member_tv, View.GONE);
                setViewVisibility(R.id.add_member, View.GONE);
                break;
        }


    }


    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.select_country:
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setFetchVehicleInterface(this);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(), "");
                break;

            case R.id.start_date_et:
                showCalendarDialog(0);
                break;

            case R.id.end_date:
                showCalendarDialog(1);
                break;

        }
    }


    public void showCalendarDialog(final int flag) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        if (flag == 0) {
                            setTextViewText(R.id.start_date_et, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        } else {
                            setTextViewText(R.id.end_date, (monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        }
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    @Override
    public void vehicleName(String text) {
        setTextViewText(R.id.select_country, text);
    }
}
