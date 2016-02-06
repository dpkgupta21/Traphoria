package com.app.traphoria.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.adapter.SearchDestinationAdapter;
import com.app.traphoria.adapter.TopDestinationAdapter;

public class TopDestinationsScreen extends AppCompatActivity {


    private RecyclerView destinations_rv;
    private TopDestinationAdapter mTopDestinationAdapter;
    private Toolbar mToolbar;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_destinations_screen);

        initViews();
    }

    private void initViews() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.top_dest);

        destinations_rv = (RecyclerView) findViewById(R.id.destinations_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        destinations_rv.setLayoutManager(llm);

        mTopDestinationAdapter = new TopDestinationAdapter(this);
        destinations_rv.setAdapter(mTopDestinationAdapter);
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
