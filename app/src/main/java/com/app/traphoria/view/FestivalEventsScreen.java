package com.app.traphoria.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.adapter.FestivalEventsAdapter;
import com.app.traphoria.adapter.TopDestinationAdapter;

public class FestivalEventsScreen extends AppCompatActivity {


    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private TextView mTitle;
    private FestivalEventsAdapter mFestivalEventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.festival_events_screen);
        initViews();
    }

    private void initViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.festival_events);

        recyclerView = (RecyclerView) findViewById(R.id.events_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        mFestivalEventsAdapter = new FestivalEventsAdapter(this);
        recyclerView.setAdapter(mFestivalEventsAdapter);

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
}
