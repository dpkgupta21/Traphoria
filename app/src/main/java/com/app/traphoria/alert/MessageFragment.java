package com.app.traphoria.alert;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.traphoria.R;
import com.app.traphoria.alert.adapter.MessagesAdapter;
import com.app.traphoria.alert.adapter.NotificationAdapter;
import com.app.traphoria.chat.ChatScreen;
import com.app.traphoria.customViews.CustomProgressDialog;
import com.app.traphoria.model.MessageDTO;
import com.app.traphoria.model.NotificationDTO;
import com.app.traphoria.preference.PreferenceHelp;
import com.app.traphoria.search.TopDestinationDetailScreen;
import com.app.traphoria.utility.BaseFragment;
import com.app.traphoria.utility.MyOnClickListener;
import com.app.traphoria.utility.RecyclerTouchListener;
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


public class MessageFragment extends BaseFragment {


    private View view;
    private String TAG = "MESSAGE";
    private List<MessageDTO> messageList;
    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.messages_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        getMessageList();

    }


    private void getMessageList() {
        if (Utils.isOnline(getActivity())) {
            Map<String, String> params = new HashMap<>();
            params.put("action", WebserviceConstant.GET_MESSAGE_LIST);
            params.put("user_id", PreferenceHelp.getUserId(getActivity()));

            CustomProgressDialog.showProgDialog(getActivity(), null);
            CustomJsonRequest postReq = new CustomJsonRequest(Request.Method.POST, WebserviceConstant.SERVICE_BASE_URL, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Utils.ShowLog(TAG, "got some response = " + response.toString());
                                Type type = new TypeToken<ArrayList<MessageDTO>>() {
                                }.getType();
                                messageList = new Gson().fromJson(response.getJSONArray("conversions").toString(), type);
                                setMessageValues();

                            } catch (Exception e) {
                                CustomProgressDialog.hideProgressDialog();
                                setMessageValues();
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


    private void setMessageValues() {


        if (messageList != null && messageList.size() > 0) {
            setViewVisibility(R.id.no_trip_tv, view, View.GONE);
            messagesAdapter = new MessagesAdapter(getActivity(), messageList);
            recyclerView.setAdapter(messagesAdapter);

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new MyOnClickListener() {
                @Override
                public void onRecyclerClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), ChatScreen.class);
                    intent.putExtra("receiverId", messageList.get(position).getUser_id());
                    startActivity(intent);
                }

                @Override
                public void onRecyclerLongClick(View view, int position) {

                }

                @Override
                public void onItemClick(View view, int position) {

                }
            }));

        } else {
            recyclerView.setVisibility(View.GONE);
            setViewVisibility(R.id.no_trip_tv, view, View.VISIBLE);
        }

    }


}
