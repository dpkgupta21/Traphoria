package com.app.traphoria.search;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.FestivalDTO;
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

public class FestivalEventDetailScreen extends BaseActivity {

    private String TAG = "FestivalEventDetailScreen";
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.festival_event_detail_screen);
        init();
    }

    private void init() {

        String eventId = getIntent().getStringExtra("eventId");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.festival_and_events);


        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.loading_fail)
                .build();


        getFestivalEventDetails(eventId);


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


    private void getFestivalEventDetails(String eventId) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_EVENT_DETAILS);
            params.put("event_id", eventId);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Utils.ShowLog(TAG, "got some response = " + response.toString());
                                    FestivalDTO festivalDTO = new Gson().fromJson(response.getJSONObject
                                            ("Event").toString(), FestivalDTO.class);
                                    setFestivalEventDetails(festivalDTO);
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
                    Utils.showExceptionDialog(FestivalEventDetailScreen.this);
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


    private void setFestivalEventDetails(FestivalDTO festivalDTO) {

        final ImageView destinationImage = (ImageView) findViewById(R.id.thumbnail);

        try {

            String imageUrl = festivalDTO.getImage();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl, destinationImage,
                        options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                                destinationImage.setImageResource(R.drawable.login_bg);
                                destinationImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                destinationImage.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                destinationImage.setImageResource(R.drawable.loading_fail);
                                destinationImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                destinationImage.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                destinationImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                destinationImage.setImageResource(R.drawable.loading_fail);
                                destinationImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                destinationImage.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                                        0, WebserviceConstant.IMAGE_MARGIN);

                            }

                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String s, View view, int i, int i1) {

                            }
                        });
            } else {
                destinationImage.setImageResource(R.drawable.loading_fail);
                destinationImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                destinationImage.setPadding(0, WebserviceConstant.IMAGE_MARGIN,
                        0, WebserviceConstant.IMAGE_MARGIN);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String formattedDateString = festivalDTO.getStart_date();
        if (formattedDateString != null && !formattedDateString.equalsIgnoreCase("")) {
            if (festivalDTO.getEnd_date() != null && !festivalDTO.getEnd_date().equalsIgnoreCase("")) {
                formattedDateString += " to ";
                formattedDateString += festivalDTO.getEnd_date();
            }
        } else {
            if (festivalDTO.getEnd_date() != null && !festivalDTO.getEnd_date().equalsIgnoreCase("")) {
                formattedDateString += festivalDTO.getEnd_date();
            }
        }
        setViewText(R.id.txt_date, formattedDateString);
        setTextViewText(R.id.txt_event_title, festivalDTO.getTitle());

        String mimeType = "text/html";
        String encoding = "utf-8";
        TextView txtTopDestination = (TextView) findViewById(R.id.txt_top_destination);
        txtTopDestination.setText(festivalDTO.getDescription());

    }
}
