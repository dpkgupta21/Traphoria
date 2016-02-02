package com.app.traphoria.fragments;

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

import traphoria.com.app.traphoria.R;
import com.app.traphoria.adapter.TaskAdapter;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.view.AddNewTaskScreen;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaskScreenFragment extends Fragment {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;

    private TaskAdapter mTaskAdapter;
    private RecyclerView recyclerView;

    public TaskScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.task_fragment, container, false);
        mActivity = NavigationDrawerActivity.mActivity;
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.task_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        mTaskAdapter = new TaskAdapter();
        recyclerView.setAdapter(mTaskAdapter);

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


    private void addNewTrip() {
        startActivity(new Intent(mActivity, AddNewTaskScreen.class));
    }


}