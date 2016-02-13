package com.app.traphoria.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.app.traphoria.model.CountryDetailsDTO;
import com.app.traphoria.model.DestinationDTO;
import com.app.traphoria.model.TaskDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.search.adapter.TopDestinationAdapter;
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

public class TopDestinationsScreen extends BaseActivity {

    private String TAG = "TOP DESTINATIONS";
    private RecyclerView destinations_rv;
    private TopDestinationAdapter mTopDestinationAdapter;
    private Toolbar mToolbar;
    private TextView mTitle;
    private List<DestinationDTO> destinationList;
    private String countryID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        countryID = getIntent().getStringExtra("CountryId");

        getDestinationList();

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


    private void getDestinationList() {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_TOP_DESTINATIONS);
            params.put("country_id", countryID);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<DestinationDTO>>() {
                                }.getType();
                                destinationList = new Gson().fromJson(response.getJSONArray("TopDestination").toString(), type);
                                setDestinationList();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(TopDestinationsScreen.this);
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


    private void setDestinationList() {

        mTopDestinationAdapter = new TopDestinationAdapter(this,destinationList);
        destinations_rv.setAdapter(mTopDestinationAdapter);

        destinations_rv.addOnItemTouchListener(new RecyclerTouchListener(this, destinations_rv, new MyOnClickListener() {
            @Override
            public void onRecyclerClick(View view, int position) {
//                Intent intent = new Intent(this, DestinationDetailScreen.class);
//                intent.putExtra("destinationID", destinationList.get(position).getTop_destination_id());
//                startActivity(intent);
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
