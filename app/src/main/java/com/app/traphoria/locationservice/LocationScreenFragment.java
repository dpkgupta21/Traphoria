package com.app.traphoria.locationservice;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.gps.GPSTracker;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.utility.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationScreenFragment extends BaseFragment {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;

    private ViewPager viewpager;
    private TabLayout tabLayout;

    public LocationScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_location_service, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = NavigationDrawerActivity.mActivity;

        GPSTracker gpsTracker = new GPSTracker(getActivity());

        init();
        openFragment(0);

    }


    private void init() {
        setClick(R.id.ll_embassy, view);
        setClick(R.id.ll_bank, view);
        setClick(R.id.ll_hospital, view);
        setClick(R.id.ll_police, view);
    }

    private void openFragment(int flag) {
        Fragment fragment = null;


        if (flag == 0) {
            fragment = EmbassyFragment.newInstance();
        } else if (flag == 1) {
            fragment = HospitalFragment.newInstance();
        } else if (flag == 2) {
            fragment = BankATMFragment.newInstance();
        } else if (flag == 3) {
            fragment = PoliceStnFragment.newInstance();
        }
        if (fragment != null) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm
                    .beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_NONE);
            ft.commit();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_embassy:
                openFragment(0);
                setViewVisibility(R.id.view_embassy, view, View.VISIBLE);
                setViewVisibility(R.id.view_hospital, view, View.INVISIBLE);
                setViewVisibility(R.id.view_bak, view, View.INVISIBLE);
                setViewVisibility(R.id.view_police, view, View.INVISIBLE);
                break;

            case R.id.ll_hospital:
                openFragment(1);
                setViewVisibility(R.id.view_embassy, view, View.INVISIBLE);
                setViewVisibility(R.id.view_hospital, view, View.VISIBLE);
                setViewVisibility(R.id.view_bak, view, View.INVISIBLE);
                setViewVisibility(R.id.view_police, view, View.INVISIBLE);
                break;

            case R.id.ll_bank:
                openFragment(2);
                setViewVisibility(R.id.view_embassy, view, View.INVISIBLE);
                setViewVisibility(R.id.view_hospital, view, View.INVISIBLE);
                setViewVisibility(R.id.view_bak, view, View.VISIBLE);
                setViewVisibility(R.id.view_police, view, View.INVISIBLE);
                break;

            case R.id.ll_police:
                openFragment(3);
                setViewVisibility(R.id.view_embassy, view, View.INVISIBLE);
                setViewVisibility(R.id.view_hospital, view, View.INVISIBLE);
                setViewVisibility(R.id.view_bak, view, View.INVISIBLE);
                setViewVisibility(R.id.view_police, view, View.VISIBLE);
                break;

        }
    }


}