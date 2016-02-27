package com.app.traphoria.trip;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.trip.adapter.ViewTripGroupDetailsAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.TripDetailsDTO;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyTripGroupDetailsScreen extends BaseActivity {

    private String TAG = "TRIP DETAILS";
    private Toolbar mToolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private ViewTripGroupDetailsAdapter mViewTripGroupDetailsAdapter;
    private TripDetailsDTO tripDetails;
    private DisplayImageOptions options;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group_trip_details);
        initView();
    }


    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.my_trips);
        recyclerView = (RecyclerView) findViewById(R.id.trip_member_rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.login_bg)
                .showImageForEmptyUri(R.drawable.login_bg)
                .build();
        String tripID = getIntent().getStringExtra("tripID");
        getTripDetails(tripID);

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


    private void getTripDetails(String tripID) {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_TRIP_DETAILS);
            params.put("trip_id", tripID);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.ShowLog(TAG, "Response -> " + response.toString());

                            try {
                                tripDetails = new Gson().fromJson(response.getJSONObject("trip").toString(), TripDetailsDTO.class);
                                setTripDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(MyTripGroupDetailsScreen.this);
                }
            });

            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.hideProgressDialog();

        } else

        {
            Utils.showNoNetworkDialog(this);
        }
    }


    private void setTripDetails() {


        if (!tripDetails.getTrip_type().equalsIgnoreCase("1")) {
            setImageResourseBackground(R.id.trip_type_icon, R.drawable.group_icon);
        } else {
            setImageResourseBackground(R.id.trip_type_icon, R.drawable.single_group_icon);
        }

        setTextViewText(R.id.dest_name, tripDetails.getCountry_name());
        setTextViewText(R.id.date, tripDetails.getStart_date() + " - " + tripDetails.getEnd_date());
        setTextViewText(R.id.expiry, "Visa Expires on: " + tripDetails.getExpire_date());
        ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
        ImageLoader.getInstance().displayImage(tripDetails.getCountry_image(), imageView,
                options);

        if (tripDetails.getTrip_users() != null && tripDetails.getTrip_users().size() > 0) {
            mViewTripGroupDetailsAdapter = new ViewTripGroupDetailsAdapter(tripDetails.getTrip_users(), this);
            recyclerView.setAdapter(mViewTripGroupDetailsAdapter);
        }
    }


}
