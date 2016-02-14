package com.app.traphoria.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.utility.BaseActivity;

public class ChatScreen extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTitle, relation;

    private EditText msg_et;
    private ImageView send_msg_btn;
    private ListView chat_lv;
    private ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_screen);
        initView();
        assignClick();
    }

    private void assignClick() {

        send_msg_btn.setOnClickListener(this);

    }


    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_btn);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.chat);
        send_msg_btn = (ImageView) findViewById(R.id.send_msg_btn);
        msg_et = (EditText) findViewById(R.id.msg_et);
        chat_lv = (ListView) findViewById(R.id.chat_list);
        chatAdapter = new ChatAdapter(this);
        chat_lv.setAdapter(chatAdapter);
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


    }
}
