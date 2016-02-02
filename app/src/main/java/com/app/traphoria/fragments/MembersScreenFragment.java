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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import traphoria.com.app.traphoria.R;
import com.app.traphoria.adapter.MembersAdapter;
import com.app.traphoria.chat.ChatScreen;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.view.AddMemberScreen;
import com.app.traphoria.view.AddNewTaskScreen;

/**
 * A placeholder fragment containing a simple view.
 */
public class MembersScreenFragment extends Fragment implements View.OnClickListener {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;
    private RelativeLayout no_trip_rl;
    private ImageView down_btn;
    private ImageView add_member, track_btn, task_btn, message_btn;


    private RecyclerView recyclerView;
    private MembersAdapter membersAdapter;

    public MembersScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.members_fragment, container, false);
        mActivity = NavigationDrawerActivity.mActivity;
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        add_member = (ImageView) view.findViewById(R.id.add_member);
        no_trip_rl = (RelativeLayout) view.findViewById(R.id.no_trip_rl);
        recyclerView = (RecyclerView) view.findViewById(R.id.members_list);
        membersAdapter = new MembersAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(membersAdapter);
        add_member.setOnClickListener(this);
        no_trip_rl.setVisibility(View.GONE);
        down_btn = (ImageView) mActivity.findViewById(R.id.down_btn);


        message_btn = (ImageView) view.findViewById(R.id.message_btn);
        task_btn = (ImageView) view.findViewById(R.id.task_btn);
        track_btn = (ImageView) view.findViewById(R.id.track_btn);
        assignClicks();

        return view;

    }

    private void assignClicks() {
        message_btn.setOnClickListener(this);
        task_btn.setOnClickListener(this);
        track_btn.setOnClickListener(this);
        down_btn.setOnClickListener(this);
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
                addMember();
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
                addMember();
                break;

            case R.id.message_btn:
                startActivity(new Intent(mActivity, ChatScreen.class));

                break;
            case R.id.track_btn:
                break;
            case R.id.task_btn:
                startActivity(new Intent(mActivity, AddNewTaskScreen.class));
                break;
            case R.id.down_btn:
                break;
        }
    }


    private void addMember() {
        startActivity(new Intent(mActivity, AddMemberScreen.class));
    }
}