package com.app.traphoria.search;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.CountryInfoDetailDTO;
import com.app.traphoria.model.TermsAndConditionsDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.search.adapter.SlidingImageAdapter;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CountryInfoDetailScreen extends BaseActivity {

    private String TAG = "CountryInfoDetailScreen";
    private DisplayImageOptions options;
    private Activity mActivity;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_info_detail_screen);
        mActivity = CountryInfoDetailScreen.this;
        init();
    }

    private void init() {


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);


        String countryId = getIntent().getStringExtra("countryId");
        getCountryDetailInfo(countryId);


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

    private void getCountryDetailInfo(String countryId) {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_COUNTRY_DETAIL_INFO);
            params.put("user_id", PreferenceHelp.getUserId(mActivity));
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
                                    CountryInfoDetailDTO countryInfoDetailDTO = new Gson().
                                            fromJson(response.getJSONObject
                                                    ("country").toString(), CountryInfoDetailDTO.class);
                                    setCountryInfoDetail(countryInfoDetailDTO);
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
                    Utils.showExceptionDialog(CountryInfoDetailScreen.this);
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


    private void setCountryInfoDetail(CountryInfoDetailDTO countryInfoDetailDTO) {

        setViewText(R.id.toolbar_title, countryInfoDetailDTO.getCountry_name());
        setViewText(R.id.txt_valid_country_name, countryInfoDetailDTO.getCountry_name());
        setViewText(R.id.txt_description, countryInfoDetailDTO.getCountry_name());
        setTextViewText(R.id.txt_valid_visa_expires_date, "Expires on: " + countryInfoDetailDTO.getExpire_date());


        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(new SlidingImageAdapter(mActivity,
                countryInfoDetailDTO.getCountry_image()));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = countryInfoDetailDTO.getCountry_image().size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage, true);
                currentPage++;
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 2000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
