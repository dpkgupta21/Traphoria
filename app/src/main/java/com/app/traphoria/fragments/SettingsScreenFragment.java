package com.app.traphoria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.traphoria.R;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsScreenFragment extends Fragment {


    private View view;

    private Activity mActivity;

    public SettingsScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings_screen_fragment, container, false);
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mActivity = NavigationDrawerActivity.mActivity;

    }
}