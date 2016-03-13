package com.app.traphoria.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.database.DatabaseHelper;
import com.app.traphoria.database.DatabaseManager;
import com.app.traphoria.lacaldabase.MemberDataSource;
import com.app.traphoria.lacaldabase.RelationDataSource;
import com.app.traphoria.model.RelationDTO;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.task.adapter.SelectMemberAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.MemberDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.volley.AppController;
import com.app.traphoria.volley.CustomJsonRequest;
import com.app.traphoria.webservice.WebserviceConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewTaskScreen extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private ListView members_lv;
    private SelectMemberAdapter selectMemberAdapter;
    private List<MemberDTO> memberList;
    private EditText edtDescription;
    private String TAG = "ADD TASK";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_task_screen);
        initView();

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.add_new_task);
        members_lv = (ListView) findViewById(R.id.members_lv);
        edtDescription = (EditText) findViewById(R.id.task_description);
        edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setTextViewText(R.id.count, getViewText(R.id.task_description).length() + "/150");

            }
        });
        getMembersList(getIntent().getStringExtra("memberId"));

        setClick(R.id.btn_add_task);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_task:

                String ids = "";
                for (int i = 0; i < memberList.size(); i++) {
                    if (memberList.get(i).getSelected().equalsIgnoreCase("Y")) {
                        ids = ids + memberList.get(i).getId() + ",";
                    }
                }
                String senderId = ids.substring(0, ids.length() - 1);

                addTask(senderId);
                break;

        }
    }


    private void getMembersList(String memberId) {
        try {
            memberList = new MemberDataSource(AddNewTaskScreen.this).getMember();
            if(memberId!=null && !memberId.equals(""))
            {
                for(int i=0;i<memberList.size();i++)
                {
                    if(memberList.get(i).getId().equalsIgnoreCase(memberId))
                    {
                        memberList.get(i).setSelected("Y");
                    }
                }
            }

            setMemberList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setMemberList() {
        selectMemberAdapter = new SelectMemberAdapter(this, memberList);
        members_lv.setAdapter(selectMemberAdapter);
        members_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (memberList.get(position).getSelected().equalsIgnoreCase("N")) {
                    memberList.get(position).setSelected("Y");
                } else {
                    memberList.get(position).setSelected("N");
                }
                selectMemberAdapter.notifyDataSetChanged();
            }
        });
    }


    private void addTask(String ids) {
        if (Utils.isOnline(AddNewTaskScreen.this)) {
            if (validateForm(ids)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.ADD_TASK);
                params.put("user_id", PreferenceHelp.getUserId(AddNewTaskScreen.this));
                params.put("title", getEditTextText(R.id.task_name));
                params.put("description", getEditTextText(R.id.task_description));
                params.put("user_ids", ids);

                CustomProgressDialog.showProgDialog(AddNewTaskScreen.this, null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        //Toast.makeText(AddNewTaskScreen.this, "Task added Successfully.", Toast.LENGTH_LONG).show();
                                        openTaskFragment();
                                    } else {
                                        Utils.showDialog(AddNewTaskScreen.this, "Error", Utils.getWebServiceMessage(response));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                CustomProgressDialog.hideProgressDialog();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressDialog.hideProgressDialog();
                        Utils.showExceptionDialog(AddNewTaskScreen.this);
                        //       CustomProgressDialog.hideProgressDialog();
                    }
                });
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                CustomProgressDialog.showProgDialog(AddNewTaskScreen.this, null);

            }

        } else {
            Utils.showNoNetworkDialog(AddNewTaskScreen.this);
        }


    }

    private boolean validateForm(String ids) {

        if (getEditTextText(R.id.task_name).equals("")) {
            Utils.customDialog("Please enter task name.", this);
            return false;
        } else if (getEditTextText(R.id.task_description).equals("")) {
            Utils.customDialog("Please enter description.", this);
            return false;
        } else if (ids.equals("")) {
            Utils.customDialog("Please select member.", this);
            return false;
        }
        return true;
    }


    private void openTaskFragment() {
        Intent intent = new Intent(AddNewTaskScreen.this, NavigationDrawerActivity.class);
        intent.putExtra("fragmentNumber", 4);
        startActivity(intent);
    }
}