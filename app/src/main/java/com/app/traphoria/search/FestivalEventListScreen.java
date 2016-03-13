package com.app.traphoria.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.FestivalDTO;
import com.app.traphoria.search.adapter.FestivalEventListAdapter;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.MyOnClickListener;
import com.app.traphoria.utility.RecyclerTouchListener;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FestivalEventListScreen extends BaseActivity {


    private String TAG = "Festival";
    private RecyclerView recyclerView;
    private FestivalEventListAdapter mFestivalEventListAdapter;
    private List<FestivalDTO> festivalList;
    // private String countryID;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.festival_events_screen);
        mActivity = this;
        initViews();
    }

    private void initViews() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.festival_events);

        recyclerView = (RecyclerView) findViewById(R.id.events_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        String countryId = getIntent().getStringExtra("CountryId");
        getFestivalList(countryId);


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


    private void getFestivalList(String countryId) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_EVENT_LIST);
            params.put("country_id", countryId);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Utils.ShowLog(TAG, "got some response = " + response.toString());
                                    Type type = new TypeToken<ArrayList<FestivalDTO>>() {
                                    }.getType();
                                    festivalList = new Gson().fromJson(response.getJSONArray("Event").toString(),
                                            type);
                                    setFestivalList();

                                } else {
                                    setViewVisibility(R.id.events_rv, View.INVISIBLE);
                                    setViewVisibility(R.id.txt_no_events, View.VISIBLE);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(FestivalEventListScreen.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(this, null);
        } else {
            Utils.showNoNetworkDialog(this);
        }

    }


    private void setFestivalList() {

        mFestivalEventListAdapter = new FestivalEventListAdapter(this, festivalList);
        recyclerView.setAdapter(mFestivalEventListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new MyOnClickListener() {
            @Override
            public void onRecyclerClick(View view, int position) {
                Intent intent = new Intent(mActivity, FestivalEventDetailScreen.class);
                intent.putExtra("eventId", festivalList.get(position).getEvent_id());
                startActivity(intent);
            }

            @Override
            public void onRecyclerLongClick(View view, int position) {

            }

            @Override
            public void onItemClick(View view, int position) {

            }
        }));


    }


}
