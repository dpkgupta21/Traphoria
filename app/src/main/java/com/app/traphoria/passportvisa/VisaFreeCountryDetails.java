package com.app.traphoria.passportvisa;

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
import com.app.traphoria.model.FreeVisaCountryDTO;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.passportvisa.adapter.FreeVisaCountryAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseActivity;
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

public class VisaFreeCountryDetails extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "FREE VISA DESTINATIONS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_free_country_details);
        init();
    }


    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText("Visa Free Destinations");


        mRecyclerView = (RecyclerView) findViewById(R.id.free_visa);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String countryID = getIntent().getStringExtra("countryId");
        getFreeVisa(countryID);


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


    private void getFreeVisa(String countryId) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_FREE_VISA_COUNTRIES);
            params.put("country_id", countryId);
            params.put("user_id", PreferenceHelp.getUserId(this));
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());

                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Type type = new TypeToken<ArrayList<FreeVisaCountryDTO>>() {
                                    }.getType();
                                    List<FreeVisaCountryDTO> list = new Gson().
                                            fromJson(response.getJSONArray("countries").toString(), type);

                                    setFreeVisaValues(list);
                                } else {

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
                    Utils.showExceptionDialog(VisaFreeCountryDetails.this);
                }
            });

            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(VisaFreeCountryDetails.this, null);

        } else {
            Utils.showNoNetworkDialog(this);
        }


    }


    private void setFreeVisaValues(List<FreeVisaCountryDTO> list) {
        if (list.size() > 0) {
            mAdapter = new FreeVisaCountryAdapter(this, list);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            TextView txt_free_visa = (TextView) findViewById(R.id.txt_free_visa);
            txt_free_visa.setVisibility(View.VISIBLE);

        }

    }
}

