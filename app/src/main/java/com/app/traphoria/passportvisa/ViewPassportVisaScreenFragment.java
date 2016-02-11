package com.app.traphoria.passportvisa;

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
import com.app.traphoria.adapter.PassportVisaAdapter;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.view.AddPassportVisaScreen;


public class ViewPassportVisaScreenFragment extends Fragment implements View.OnClickListener {


    private View view;

    private Activity mActivity;
    private Toolbar mToolbar;
    private RelativeLayout no_record_rl;
    private ImageView add_passport_visa;


    private RecyclerView recyclerView;
    private PassportVisaAdapter passportVisaAdapter;

    public ViewPassportVisaScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passport_visa_screen_fragment, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        add_passport_visa = (ImageView) view.findViewById(R.id.add_passport_visa);
        no_record_rl = (RelativeLayout) view.findViewById(R.id.no_record_rl);
        recyclerView = (RecyclerView) view.findViewById(R.id.passport_visa_list);
        passportVisaAdapter = new PassportVisaAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(passportVisaAdapter);
        add_passport_visa.setOnClickListener(this);
        no_record_rl.setVisibility(View.GONE);

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
                addPassportVisa();
                return false;

            default:
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_passport_visa:
                addPassportVisa();
                break;
        }
    }

    private void addPassportVisa() {
        startActivity(new Intent(getActivity(), AddPassportVisaScreen.class));
    }
}