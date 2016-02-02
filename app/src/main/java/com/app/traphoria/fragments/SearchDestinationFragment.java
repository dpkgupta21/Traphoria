package com.app.traphoria.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import traphoria.com.app.traphoria.R;
import com.app.traphoria.adapter.SearchDetinationAdapter;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchDestinationFragment extends Fragment implements View.OnClickListener {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;
    private TextView toolbar_right_tv;
    private SearchDetinationAdapter mSearchDetinationAdapter;

    private RecyclerView recyclerView;

    public SearchDestinationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_destination_fragment, container, false);
        mActivity = NavigationDrawerActivity.mActivity;
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        toolbar_right_tv = (TextView) mActivity.findViewById(R.id.toolbar_right_tv);
        recyclerView = (RecyclerView) view.findViewById(R.id.destination_list);
        toolbar_right_tv.setVisibility(View.VISIBLE);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        mSearchDetinationAdapter = new SearchDetinationAdapter();
        recyclerView.setAdapter(mSearchDetinationAdapter);
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
            case R.id.toolbar_right_tv:
                break;
        }
    }


}