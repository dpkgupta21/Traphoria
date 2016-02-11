package com.app.traphoria.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.app.traphoria.chat.ChatScreen;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.member.adapter.MemberPassportAdapter;
import com.app.traphoria.member.adapter.MemberVisaAdapter;
import com.app.traphoria.model.PassportVisaDetailsDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.task.AddNewTaskScreen;
import com.app.traphoria.view.TrackScreen;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MembersScreenFragment extends BaseFragment {


    private Toolbar mToolbar;
    private String TAG = "Member_Screen";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView vRecyclerView;
    private RecyclerView.Adapter vAdapter;
    private RecyclerView.LayoutManager vLayoutManager;


    private View view;
    private PassportVisaDetailsDTO detailValues;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MembersScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.members_fragment, container, false);
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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.members_passport_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        vRecyclerView = (RecyclerView) view.findViewById(R.id.members_visa_recycler);
        vRecyclerView.setHasFixedSize(true);
        vLayoutManager = new LinearLayoutManager(getActivity());
        vRecyclerView.setLayoutManager(vLayoutManager);

        getPassportVisaDetails();
        setClick(R.id.add_member, view);

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
                startActivity(new Intent(getActivity(), ChatScreen.class));

                break;
            case R.id.track_btn:
                startActivity(new Intent(getActivity(), TrackScreen.class));
                break;
            case R.id.task_btn:
                startActivity(new Intent(getActivity(), AddNewTaskScreen.class));
                break;


            case R.id.add_member:
                addMember();
                break;

        }
    }


    private void addMember() {
        startActivity(new Intent(getActivity(), AddMemberScreen.class));
    }


    private void getPassportVisaDetails() {

        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_PASSPORT_VISA_DETAILS);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                detailValues = new Gson().fromJson(response.toString(), PassportVisaDetailsDTO.class);
                                setPassportDetails();
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


    private void setPassportDetails() {

        if (detailValues.getVisa() != null && detailValues.getPassport() != null) {
            setViewVisibility(R.id.no_trip_rl, view, View.GONE);
            if (detailValues.getPassport() != null && detailValues.getPassport().size() != 0) {
                mAdapter = new MemberPassportAdapter(detailValues.getPassport(), getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            if (detailValues.getVisa() != null && detailValues.getVisa().size() != 0) {
                vAdapter = new MemberVisaAdapter(detailValues.getVisa(), getActivity());
                vRecyclerView.setAdapter(vAdapter);

            }

        } else {
            setViewVisibility(R.id.members_passport_recycler, view, View.GONE);
            setViewVisibility(R.id.members_visa_recycler, view, View.GONE);
            setViewVisibility(R.id.no_trip_rl, view, View.VISIBLE);
        }


    }


}