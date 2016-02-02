package com.app.traphoria.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.adapter.ViewTripGroupDetailsAdapter;

public class ViewTripGroupDetailsScreen extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTitle, date, expiry;
    private RecyclerView recyclerView;
    private ViewTripGroupDetailsAdapter mViewTripGroupDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group_trip_details);
        initView();
    }


    private void initView() {

        expiry = (TextView) findViewById(R.id.expiry);
        date = (TextView) findViewById(R.id.date);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.my_trips);
        recyclerView = (RecyclerView) findViewById(R.id.trip_member_rv);
        mViewTripGroupDetailsAdapter = new ViewTripGroupDetailsAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mViewTripGroupDetailsAdapter);
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

        }
    }


}
