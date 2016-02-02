package com.app.traphoria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import traphoria.com.app.traphoria.R;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationScreenFragment extends Fragment implements View.OnClickListener {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;

    public LocationScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.location_services_fragment, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = NavigationDrawerActivity.mActivity;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}