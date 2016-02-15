package com.app.traphoria.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.TripDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.trip.adapter.MyTripAdapter;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.MyOnClickListener;
import com.app.traphoria.utility.RecyclerTouchListener;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MytripScreenFragment extends BaseFragment {


    private View view;
    private Toolbar mToolbar;
    private String TAG = "TRIP SCREEN";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TripDTO> tripList;

    public MytripScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_trips_fragment, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.passport_visa_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.trip_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getTripList();
        setClick(R.id.create_trip, view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addNewTrip();
                return false;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_trip:
                addNewTrip();
                break;
        }
    }

    private void addNewTrip() {
        startActivity(new Intent(getActivity(), AddNewTripScreen.class));
    }


    private void getTripList() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_TRIP_LIST);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<TripDTO>>() {
                                }.getType();
                                tripList = new Gson().fromJson(response.getJSONArray("trip_list").toString(), type);
                                setTripData();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(getActivity());
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(getActivity(), null);
        } else {
            Utils.showNoNetworkDialog(getActivity());
        }


    }


    private void setTripData() {

        if (tripList != null && tripList.size() != 0) {
            setViewVisibility(R.id.no_trip_rl, view, View.GONE);
            mAdapter = new MyTripAdapter(tripList, getActivity());
            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new MyOnClickListener() {
                @Override
                public void onRecyclerClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), ViewTripGroupDetailsScreen.class);
                    intent.putExtra("tripID", tripList.get(position).getTrip_id());
                    startActivity(intent);
                }

                @Override
                public void onRecyclerLongClick(View view, int position) {

                }

                @Override
                public void onItemClick(View view, int position) {

                }
            }));


        } else {

            setViewVisibility(R.id.trip_list, view, View.GONE);
            setViewVisibility(R.id.no_trip_rl, view, View.VISIBLE);
        }

    }


}