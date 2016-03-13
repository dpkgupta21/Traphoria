package com.app.traphoria.chat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.chat.adapter.ChatAdapter;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.ChatDTO;
import com.app.traphoria.model.MessageDTO;
import com.app.traphoria.model.MessagesDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.utility.BaseActivity;
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

public class ChatScreen extends BaseActivity {

    //private Toolbar mToolbar;
    // private TextView mTitle;
    private ListView chat_lv;
    private ChatAdapter chatAdapter;
    private List<MessagesDTO> chatList;
    private String receiverId;
    private String TAG = "CHAT SCREEN";
    public static boolean isChatScreenOnFlag = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
        initView();
        assignClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChatScreenOnFlag = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isChatScreenOnFlag = false;
    }

    private void assignClick() {
        setClick(R.id.send_msg_btn);
    }


    private void initView() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        TextView mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.chat);
        chat_lv = (ListView) findViewById(R.id.chat_list);

        receiverId = getIntent().getStringExtra("receiverId");
        loadMessageList();

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
            case R.id.send_msg_btn:
                sendMessage();
                break;
        }

    }


    private void loadMessageList() {
        if (Utils.isOnline(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.LOAD_MESSAGE);
            params.put("sender_id", PreferenceHelp.getUserId(this));
            params.put("receiver_id", receiverId);
            CustomProgressDialog.showProgDialog(this, null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                if (Utils.getWebServiceStatus(response)) {
                                    chat_lv.setVisibility(View.VISIBLE);

                                    Type type = new TypeToken<ArrayList<MessagesDTO>>() {
                                    }.getType();
                                    chatList = new Gson().fromJson(
                                            response.getJSONArray("messageList").toString(), type);
                                    setChatList();
                                    scrollMyListViewToBottom();
                                } else {
                                    chat_lv.setVisibility(View.GONE);
                                    setViewVisibility(R.id.no_trip_tv, View.VISIBLE);
                                }
                            } catch (Exception e) {
                                CustomProgressDialog.hideProgressDialog();
                                e.printStackTrace();
                            }
                            CustomProgressDialog.hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomProgressDialog.hideProgressDialog();
                    Utils.showExceptionDialog(ChatScreen.this);
                    //       CustomProgressDialog.hideProgressDialog();
                }
            });
            AppController.getInstance().getRequestQueue().add(postReq);
            postReq.setRetryPolicy(new DefaultRetryPolicy(
                    30000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomProgressDialog.showProgDialog(ChatScreen.this, null);
        } else {
            Utils.showNoNetworkDialog(this);
        }

    }


    private void setChatList() {
        if (chatList != null && chatList.size() > 0) {
            if (chatAdapter == null) {
                chatAdapter = new ChatAdapter(this, chatList);
                chat_lv.setAdapter(chatAdapter);

            } else {
                chatAdapter.setChatList(chatList);
                chatAdapter.notifyDataSetChanged();

            }
        } else {

            setViewVisibility(R.id.no_trip_tv, View.VISIBLE);
        }
    }


    public void sendMessage() {
        Utils.hideKeyboard(this);
        if (!getViewText(R.id.msg_et).equals("")) {
            if (Utils.isOnline(this)) {
                Map<String, String> params = new HashMap<>();
                params.put("action", WebserviceConstant.SEND_MESSAGE);
                params.put("sender_id", PreferenceHelp.getUserId(this));
                params.put("message", getViewText(R.id.msg_et));
                params.put("receiver_id", receiverId);
                CustomProgressDialog.showProgDialog(this, null);
                CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST,
                        WebserviceConstant.SERVICE_BASE_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utils.ShowLog(TAG, "Response -> " + response.toString());
                                CustomProgressDialog.hideProgressDialog();
                                try {
                                    if (Utils.getWebServiceStatus(response)) {
                                        chatList.clear();
                                        chatList = null;
                                        Type type = new TypeToken<ArrayList<MessagesDTO>>() {
                                        }.getType();
                                        chatList = new Gson().
                                                fromJson(response.getJSONObject("messageList").
                                                        getJSONArray("messageList").toString(), type);
                                        setTextViewText(R.id.msg_et, "");
                                        chatAdapter = null;
                                        setChatList();
                                        scrollMyListViewToBottom();
                                    } else {
                                        Utils.customDialog(Utils.getWebServiceMessage(response), ChatScreen.this);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CustomProgressDialog.hideProgressDialog();
                        Utils.showExceptionDialog(ChatScreen.this);
                    }
                });
                AppController.getInstance().getRequestQueue().add(postReq);
                postReq.setRetryPolicy(new DefaultRetryPolicy(
                        30000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                CustomProgressDialog.showProgDialog(ChatScreen.this, null);
            } else {
                Utils.showNoNetworkDialog(this);
            }
        }
    }


    private void scrollMyListViewToBottom() {
        chat_lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chat_lv.setSelection(chatAdapter.getCount() - 1);
            }
        });
    }

}
