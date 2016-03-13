package com.app.traphoria.search;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.customViews.ExpandViewListView;
import com.app.traphoria.model.AccordianDTO;
import com.app.traphoria.model.TraditionDTO;
import com.app.traphoria.search.adapter.CustomArrayAdapter;
import com.app.traphoria.search.adapter.TraditionExpandableAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraditionScreen extends BaseActivity {

    private final static String TAG = "TraditionScreen";
    private TraditionDTO traditionValues;
    private String countryId;
    private DisplayImageOptions options;
    private List<AccordianDTO> accordianList;
    private ExpandableListView mListView;
    private TraditionExpandableAdapter adapter;
    private final int CELL_DEFAULT_HEIGHT = 0;


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
                .showImageOnLoading(R.drawable.login_bg)
                .showImageOnFail(R.drawable.loading_fail)
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
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                    WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (Utils.getWebServiceStatus(response)) {
                                    Utils.ShowLog(TAG, "got some response = " + response.toString());
                                    traditionValues = new Gson().
                                            fromJson(response.getJSONObject("Tradition").toString(),
                                                    TraditionDTO.class);
                                    setTraditionDetails();
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
        final ImageView imgThumbnail = (ImageView) findViewById(R.id.thumbnail);

        try {
            String imageUrl = traditionValues.getImage();
            if (!imageUrl.equalsIgnoreCase("")) {
                ImageLoader.getInstance().displayImage(imageUrl, imgThumbnail,
                        options, new ImageLoadingListener() {

                            @Override
                            public void onLoadingStarted(String s, View view) {

                                imgThumbnail.setImageResource(R.drawable.login_bg);
                                imgThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);

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

        accordianList = traditionValues.getAccordian();

//
//        final List<ExpandListItem> mData = new ArrayList<>();
//        for (int i = 0; i < accordianList.size(); i++) {
//            mData.add(new ExpandListItem(accordianList.get(i).getTitle(),
//                    CELL_DEFAULT_HEIGHT,
//                    accordianList.get(i).getDescription()));
//        }


        //final CustomArrayAdapter adapter = new CustomArrayAdapter(this, mData);

        adapter = new TraditionExpandableAdapter(this, accordianList);

        mListView = (ExpandableListView) findViewById(R.id.main_list_view);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
        ScrollView scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        scroll_view.smoothScrollTo(0, 0);
//        mListView.expandGroup(0);
        setListViewHeight(mListView);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });
        //       setListViewHeightBasedOnChildren(mListView);

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


    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


//    public static void setListViewHeight(ExpandableListView listView) {
//        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }


//    private void setListViewHeight(ExpandableListView listView,
//                                   int group) {
//        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
//
//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
//                View.MeasureSpec.EXACTLY);
//        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
//            View groupItem = listAdapter.getGroupView(i, false, null, listView);
//            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//            totalHeight += groupItem.getMeasuredHeight();
//
//            if (((listView.isGroupExpanded(i)) && (i != group))
//                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
//                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
//                    View listItem = listAdapter.getChildView(i, j, false, null,
//                            listView);
//                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//
//                    totalHeight += listItem.getMeasuredHeight();
//
//                }
//            }
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        int height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
//        if (height < 10)
//            height = 200;
//        params.height = height;
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//
//    }


//    private void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = listView.getPaddingTop()
//                + listView.getPaddingBottom();
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            if (listItem instanceof ViewGroup) {
//                listItem.setLayoutParams(new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//            }
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }


}
