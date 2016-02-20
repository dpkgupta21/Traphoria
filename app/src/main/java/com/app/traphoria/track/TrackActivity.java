package com.app.traphoria.track;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.adapter.TrackLocationsAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.gps.GPSTracker;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.UserLocationDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private RecyclerView recyclerView;
    private TrackLocationsAdapter trackLocationsAdapter;
    private List<UserLocationDTO> locationList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        initViews();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationList != null && locationList.size() > 0) {
            for (int i = 0; i < locationList.size(); i++) {
                LatLng location = new LatLng(Double.parseDouble(locationList.get(i).getLat()), Double.parseDouble(locationList.get(i).getLng()));
                mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.place_blue)));

            }

        }

        LatLng current = new LatLng(TraphoriaPreference.getLatitude(this), TraphoriaPreference.getLongitude(this));
        mMap.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromResource(R.drawable.place_violet)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }


    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        mToolbarTitle.setText(R.string.track);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        recyclerView = (RecyclerView) findViewById(R.id.track_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        getUserLocation();

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


    private void getUserLocation() {

        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.USER_LOCATION);
            params.put("user_id", PreferenceHelp.getUserId(this));

            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog("Track Screen", "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<UserLocationDTO>>() {
                                }.getType();

                                locationList = new Gson().fromJson(response.getJSONArray("user_location").toString(), type);
                                setLOcatonList();
                            } catch (Exception e) {
                                setLOcatonList();
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(TrackActivity.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(TrackActivity.this, null);
        } else {
            Utils.showNoNetworkDialog(this);
        }


    }


    private void setLOcatonList() {


        if (locationList != null && locationList.size() > 0) {
            trackLocationsAdapter = new TrackLocationsAdapter(this, locationList);
            recyclerView.setAdapter(trackLocationsAdapter);

        }
        GPSTracker gpsTracker = new GPSTracker(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

}
