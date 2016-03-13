package com.app.traphoria.member;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.chat.ChatScreen;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.lacaldabase.MemberDataSource;
import com.app.traphoria.member.adapter.MemberListAdapter;
import com.app.traphoria.member.adapter.MemberPassportAdapter;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.model.PassportDTO;
import com.app.traphoria.model.PassportVisaDTO;
import com.app.traphoria.model.VisaDTO;
import com.app.traphoria.passportvisa.AddPassportVisaScreen;
import com.app.traphoria.passportvisa.VisaFreeCountryDetails;
import com.app.traphoria.passportvisa.adapter.PassportAdapter;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.task.AddNewTaskScreen;
import com.app.traphoria.track.TrackActivity;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MembersScreenFragment extends BaseFragment {


    //private Toolbar mToolbar;
    private final static String TAG = "Member_Screen";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    private List<PassportVisaDTO> passportVisaList;
    private List<MemberDTO> memberList;
    private Dialog mDialog = null;
    private TextView toolbarTitle;
    private Activity mActivity;
    private String memberId;

    public MembersScreenFragment() {
    }

    public static MembersScreenFragment newInstance(String memberId) {
        MembersScreenFragment fragment = new MembersScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString("memberId", memberId);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.members_fragment, container, false);
        //mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);

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
        mActivity = getActivity();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.members_passport_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        memberId = getArguments().getString("memberId");
        setTitleFragment();
        getPassportVisaDetails(memberId);
        setClick(R.id.add_member, view);
        setClick(R.id.message_btn, view);
        setClick(R.id.track_btn, view);
        setClick(R.id.task_btn, view);
        //memberId = PreferenceHelp.getUserId(mActivity);

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
            case R.id.message_btn:

                if (!memberId.equalsIgnoreCase(PreferenceHelp.getUserId(mActivity))) {
                    Intent intent = new Intent(mActivity, ChatScreen.class);
                    intent.putExtra("receiverId", memberId);
                    startActivity(intent);
                } else {
                    Utils.customDialog("Please select member", mActivity);
                }

                break;
            case R.id.track_btn:
                startActivity(new Intent(mActivity, TrackActivity.class));
                break;
            case R.id.task_btn:

                Intent intent = new Intent(mActivity, AddNewTaskScreen.class);
                intent.putExtra("memberId", memberId);
                startActivity(intent);
                break;
            case R.id.add_member:
                addMember();
                break;

        }
    }


    private void addMember() {
        startActivity(new Intent(mActivity, AddMemberScreen.class));
    }


    private void getPassportVisaDetails(String userID) {

        if (Utils.isOnline(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_PASSPORT_VISA_DETAILS);
            params.put("user_id", userID);

            CustomProgressDialog.showProgDialog(mActivity, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (Utils.getWebServiceStatus(response)) {
                                List<PassportDTO> passportList = null;
                                List<VisaDTO> visaList = null;
                                try {
                                    Utils.ShowLog(TAG, "got some response = " + response.toString());
                                    Type type = new TypeToken<ArrayList<PassportDTO>>() {
                                    }.getType();

                                    Type type1 = new TypeToken<ArrayList<VisaDTO>>() {
                                    }.getType();


                                    passportList = new Gson().
                                            fromJson(response.getJSONArray("Passport").toString(), type);
                                    visaList = new Gson().
                                            fromJson(response.getJSONArray("Visa").toString(), type1);
                                    setPassportDetails(passportList, visaList);
                                } catch (Exception e) {
                                    setPassportDetails(passportList, visaList);
                                    e.printStackTrace();
                                }
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(mActivity);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(mActivity, null);
        } else {
            Utils.showNoNetworkDialog(mActivity);
        }


    }


    private void setPassportDetails(final List<PassportDTO> passportList, List<VisaDTO> visaList) {

        if (passportList != null & visaList != null) {

            setViewVisibility(R.id.members_passport_recycler, view, View.VISIBLE);
            setViewVisibility(R.id.no_trip_rl, view, View.GONE);
            passportVisaList = new ArrayList<>();
            if (passportList != null && passportList.size() != 0) {

                for (int i = 0; i < passportList.size(); i++) {
                    PassportVisaDTO passportVisaDTO = new PassportVisaDTO();
                    passportVisaDTO.setId(passportList.get(i).getPassport_id());
                    passportVisaDTO.setCountry(passportList.get(i).getCountry());
                    passportVisaDTO.setCountryID(passportList.get(i).getCountry_id());
                    passportVisaDTO.setPassportNumber(passportList.get(i).getPassport_no());
                    passportVisaDTO.setExpiryDate(passportList.get(i).getExpire_date());
                    passportVisaDTO.setPassport_visa_type(passportList.get(i).getPassport_type());
                    passportVisaDTO.setPassport_visa_type_id(passportList.get(i).getPassport_type_id());
                    passportVisaDTO.setCountry_image(passportList.get(i).getCountry_image());
                    passportVisaDTO.setType("P");
                    passportVisaList.add(passportVisaDTO);

                }

            }

            if (visaList != null && visaList.size() != 0) {

                for (int i = 0; i < visaList.size(); i++) {
                    PassportVisaDTO passportVisaDTO = new PassportVisaDTO();
                    passportVisaDTO.setId(visaList.get(i).getVisa_id());
                    passportVisaDTO.setCountry(visaList.get(i).getCountry());
                    passportVisaDTO.setCountryID(visaList.get(i).getCountry_id());
                    passportVisaDTO.setExpiryDate(visaList.get(i).getExpire_date());
                    passportVisaDTO.setPassport_visa_type(visaList.get(i).getVisa_type());
                    passportVisaDTO.setPassport_visa_type_id(visaList.get(i).getVisa_type_id());
                    passportVisaDTO.setCountry_image(visaList.get(i).getCountry_image());
                    passportVisaDTO.setEntry_type(visaList.get(i).getEntry_type());
                    passportVisaDTO.setType("V");
                    passportVisaList.add(passportVisaDTO);

                }
            }

            if (mAdapter != null) {
                mAdapter = null;
            }
            mAdapter = new MemberPassportAdapter(passportVisaList, mActivity);
            mRecyclerView.setAdapter(mAdapter);

            ((MemberPassportAdapter) mAdapter).setOnItemClickListener(new MemberPassportAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent;
                    switch (v.getId()) {
                        case R.id.thumbnail:
                            String type = passportVisaList.get(position).getType();
                            intent = new Intent(mActivity, AddPassportVisaScreen.class);
                            intent.putExtra("id", passportVisaList.get(position).getId());
                            intent.putExtra("type", type);
                            if (type.equalsIgnoreCase("P")) {
                                intent.putExtra("isEditPassport", true);
                            } else if (type.equalsIgnoreCase("V")) {
                                intent.putExtra("isEditVisa", true);
                            }
                            intent.putExtra("userId", memberId);
                            intent.putExtra("isMember", true);

                            startActivity(intent);
                            break;


                    }

                }
            });

        } else {
            setViewVisibility(R.id.members_passport_recycler, view, View.GONE);
            setViewVisibility(R.id.no_trip_rl, view, View.VISIBLE);
        }

    }


    protected void setTitleFragment() {
        Toolbar mToolbar = (Toolbar) ((AppCompatActivity) mActivity).findViewById(R.id.tool_bar);
        toolbarTitle = ((TextView) mToolbar.findViewById(R.id.toolbar_title));
        toolbarTitle.setText(MemberDataSource.getMemberName(memberId, mActivity));
        ImageView downButton = ((ImageView) mToolbar.findViewById(R.id.down_btn));
        downButton.setVisibility(View.VISIBLE);
        downButton.setOnClickListener(memberListDialog);
    }


    View.OnClickListener memberListDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();
        }
    };


    public void showDialog() {

        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mActivity != null) {
            mDialog = new Dialog(mActivity, R.style.DialogSlideAnim);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view1 = inflater.inflate(R.layout.sliding_dialog, null,
                    false);
            mDialog.setContentView(view1);
            mDialog.setCancelable(true);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            lp.gravity = Gravity.BOTTOM;
            mDialog.getWindow().setAttributes(lp);

            try {
                final ListView listView = (ListView) mDialog
                        .findViewById(R.id.listview);


                memberList = new MemberDataSource(mActivity).getMember();
                final MemberListAdapter adapter = new MemberListAdapter(
                        mActivity, memberList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(memberListItemListener);
                mDialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    AdapterView.OnItemClickListener memberListItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {

            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
            toolbarTitle.setText(memberList.get(i).getName());
            memberId = memberList.get(i).getId();
            getPassportVisaDetails(memberId);

        }
    };


}


