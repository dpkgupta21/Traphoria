package com.app.traphoria.trip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.traphoria.R;
import com.app.traphoria.adapter.MyTripAdapter;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MytripScreenFragment extends Fragment implements View.OnClickListener {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;
    private RelativeLayout no_trip_rl;
    private ImageView create_trip;


    private RecyclerView recyclerView;
    private MyTripAdapter myTripAdapter;

    public MytripScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_trips_fragment, container, false);
        mActivity=NavigationDrawerActivity.mActivity;
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        create_trip = (ImageView) view.findViewById(R.id.create_trip);
        no_trip_rl = (RelativeLayout) view.findViewById(R.id.no_trip_rl);
        recyclerView = (RecyclerView) view.findViewById(R.id.trip_list);
        myTripAdapter = new MyTripAdapter(mActivity);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(myTripAdapter);
        create_trip.setOnClickListener(this);
        no_trip_rl.setVisibility(View.GONE);

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


        mActivity = NavigationDrawerActivity.mActivity;

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
        startActivity(new Intent(mActivity, AddNewTripScreen.class));
    }


}