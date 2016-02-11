package com.app.traphoria.search;

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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DestinationDetailScreen extends BaseActivity {

    private CountryDetailsDTO countryDetails;
    private String TAG = "COUNTRY DETAILS";
    private DisplayImageOptions options;
    private String countryID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_detail_screen);
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
                .showImageOnLoading(R.drawable.slide_img)
                .showImageOnFail(R.drawable.slide_img)
                .showImageForEmptyUri(R.drawable.slide_img)
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
                intent = new Intent(this, TopDestinationsScreen.class);
                intent.putExtra("CountryId", countryID);
                startActivity(intent);
                break;

            case R.id.culture_dest_tv:
                break;

            case R.id.festival_tv:
                intent = new Intent(this, FestivalEventsScreen.class);
                intent.putExtra("CountryId", countryID);
                startActivity(intent);

                break;
        }

    }


    private void getCountryDetails(String countryID) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_COUNTRY_DETAILS);
            params.put("user_id", PreferenceHelp.getUserId(this));
            params.put("country_id", countryID);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                countryDetails = new Gson().fromJson(response.getJSONObject("trip").toString(), CountryDetailsDTO.class);
                                setCountryDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(DestinationDetailScreen.this);
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


    private void setCountryDetails() {

        setTextViewText(R.id.place_name,countryDetails.getCountry_name());
        ImageView countryImage = (ImageView) findViewById(R.id.thumbnail);
        ImageLoader.getInstance().displayImage(countryDetails.getCountry_image(), countryImage,
                options);

    }
}
