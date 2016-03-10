package com.app.traphoria.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.CountryDetailsDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CountryDetailScreen extends BaseActivity {

    //private CountryDetailsDTO countryDetails;
    private String TAG = "COUNTRY DETAILS";
    private DisplayImageOptions options;
    private String countryID;
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_detail_screen);
        mActivity = this;
        init();
    }

    private void init() {

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
        countryID = getIntent().getStringExtra("countryId");
        getCountryDetails(countryID);
        setClick(R.id.back_btn);
        setClick(R.id.place_name);
        setClick(R.id.top_dest_tv);
        setClick(R.id.culture_dest_tv);
        setClick(R.id.festival_tv);
    }

    private void assignClicks() {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.back_btn:
                finish();
                break;
            case R.id.top_dest_tv:
                intent = new Intent(this, TopDestinationListScreen.class);
                intent.putExtra("CountryId", countryID);
                startActivity(intent);
                break;

            case R.id.culture_dest_tv:
                intent = new Intent(this, TraditionScreen.class);
                intent.putExtra("CountryId", countryID);
                startActivity(intent);
                break;


            case R.id.festival_tv:
                intent = new Intent(this, FestivalEventListScreen.class);
                intent.putExtra("CountryId", countryID);
                startActivity(intent);

                break;
        }

    }


    private void getCountryDetails(String countryID) {

        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_COUNTRY_DETAILS);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
            params.put("country_id", countryID);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                CountryDetailsDTO countryDetails = new Gson().fromJson(
                                        response.getJSONObject("trip").toString(),
                                        CountryDetailsDTO.class);
                                setCountryDetails(countryDetails);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(mActivity);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(mActivity, null);
        } else {
            Utils.showNoNetworkDialog(mActivity);
        }

    }


    private void setCountryDetails(CountryDetailsDTO countryDetails) {

        setTextViewText(R.id.place_name, countryDetails.getCountry_name());
        ImageView countryImage = (ImageView) findViewById(R.id.thumbnail);
        try {
            ImageLoader.getInstance().displayImage(countryDetails.getCountry_image(), countryImage,
                    options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                            ((ImageView) view).setImageResource(R.drawable.login_bg);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            ((ImageView) view).setImageResource(R.drawable.loading_fail);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageView) view).setPadding(0, 20, 0, 20);
                        }

                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String s, View view, int i, int i1) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}