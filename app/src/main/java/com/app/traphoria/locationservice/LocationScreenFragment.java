package com.app.traphoria.locationservice;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
public class LocationScreenFragment extends BaseFragment{


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
        viewpager = (ViewPager)view.findViewById(R.id.infoviewpager);
        setUpViewPager(viewpager);

        tabLayout = (TabLayout)view.findViewById(R.id.infotabs);
        tabLayout.setupWithViewPager(viewpager);
        setupTabIcons();

    }



    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.embassy));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.embassy_icon, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.hospital));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hospital_icon, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText(getString(R.string.bank_atm));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.bank_icon, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);


        TextView tabFouth = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabFouth.setText(getString(R.string.police_stn));
        tabFouth.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.police_icon, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFouth);

    }




    private void setUpViewPager(ViewPager viewpager) {
        InfoViewPagerAdapter infoViewPagerAdapter
                = new InfoViewPagerAdapter(getActivity().getSupportFragmentManager());
        infoViewPagerAdapter.addFragment(EmbassyFragment.newInstance(),
                getString(R.string.embassy));
        infoViewPagerAdapter.addFragment(HospitalFragment.newInstance(),
                getString(R.string.hospital));
        infoViewPagerAdapter.addFragment(BankATMFragment.newInstance(),
                getString(R.string.bank_atm));
        infoViewPagerAdapter.addFragment(PolicaStnFragment.newInstance(),
                getString(R.string.police_stn));
        viewpager.setAdapter(infoViewPagerAdapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    class InfoViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public InfoViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }



}