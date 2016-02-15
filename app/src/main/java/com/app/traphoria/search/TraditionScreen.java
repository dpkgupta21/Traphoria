package com.app.traphoria.search;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.customViews.ExpandViewListView;
import com.app.traphoria.model.AccordianDTO;
import com.app.traphoria.model.PassportVisaDetailsDTO;
import com.app.traphoria.model.TraditionDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.search.adapter.CustomArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraditionScreen extends BaseActivity {

    private String TAG = "TRADITION";
    private TraditionDTO traditionValues;
    private String countryId;
    private DisplayImageOptions options;
    List<AccordianDTO> accordianList;
    private ExpandViewListView mListView;
    private final int CELL_DEFAULT_HEIGHT = 100;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradition_screen);
        init();
    }


    private void init() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.culture_dest);


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
        countryId = getIntent().getStringExtra("CountryId");
        getTraditions();

    }


    private void getTraditions() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_TRADITION);
            params.put("country_id", countryId);

            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                traditionValues = new Gson().fromJson(response.getJSONObject("Tradition").toString(), TraditionDTO.class);
                                setTraditionDetails();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(TraditionScreen.this);
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


    private void setTraditionDetails() {
        setTextViewText(R.id.txt_country_name, traditionValues.getTitle());
        setTextViewText(R.id.txt_description, traditionValues.getDescription());
        ImageView imageView = (ImageView) findViewById(R.id.tnumbnail);

        ImageLoader.getInstance().displayImage(traditionValues.getImage(), imageView,
                options);


        accordianList = traditionValues.getAccordian();

        List<ExpandListItem> mData = new ArrayList<>();
        for (int i = 0; i < accordianList.size(); i++) {
            mData.add(new ExpandListItem(accordianList.get(i).getTitle(), CELL_DEFAULT_HEIGHT, accordianList.get(i).getDescription()));
        }


        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mData);

        mListView = (ExpandViewListView)findViewById(R.id.main_list_view);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }




}
