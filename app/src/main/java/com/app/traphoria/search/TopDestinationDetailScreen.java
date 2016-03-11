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
import com.app.traphoria.model.DestinationDTO;
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

public class TopDestinationDetailScreen extends BaseActivity {

    private String TAG = "TopDestinationDetailScreen";
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_destination_detail_screen);
        init();
    }

    private void init() {
        String topDestinationId = getIntent().getStringExtra("destinationId");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.top_dest);


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


        getTopDestinationDetails(topDestinationId);


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


    private void getTopDestinationDetails(String topDestinationId) {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_TOP_DESTINATION_DETAILS);
            params.put("top_destination_id", topDestinationId);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Utils.ShowLog(TAG, "got some response = " + response.toString());
                                    DestinationDTO destinationDTO = new Gson().fromJson(response.getJSONObject
                                            ("TopDestination").toString(), DestinationDTO.class);
                                    setTopDestinationDetails(destinationDTO);
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
                    Utils.showExceptionDialog(TopDestinationDetailScreen.this);
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


    private void setTopDestinationDetails(DestinationDTO destinationDTO) {

        TextView txtTopDestination = (TextView) findViewById(R.id.txt_top_destination);
        txtTopDestination.setText(destinationDTO.getDescription());

        TextView txtDestinationTitle = (TextView) findViewById(R.id.txt_destination_title);
        txtDestinationTitle.setText(destinationDTO.getTitle());

        final ImageView imgThumbnail = (ImageView) findViewById(R.id.thumbnail);
        try {
            String imageUrl = destinationDTO.getImage();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl, imgThumbnail,
                        options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                                imgThumbnail.setImageResource(R.drawable.login_bg);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, 20, 0, 20);

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                imgThumbnail.setImageResource(R.drawable.loading_fail);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, 20, 0, 20);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                imgThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                imgThumbnail.setImageResource(R.drawable.loading_fail);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                imgThumbnail.setPadding(0, 20, 0, 20);
                            }

                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String s, View view, int i, int i1) {

                            }
                        });
            } else {
                imgThumbnail.setImageResource(R.drawable.loading_fail);
                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgThumbnail.setPadding(0, 20, 0, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
