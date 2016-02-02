package com.app.traphoria.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.traphoria.R;
import com.app.traphoria.adapter.SelectMemberAdapter;

public class AddNewTaskScreen extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitle;
    private EditText task_name, task_description;
    private ListView members_lv;
    private SelectMemberAdapter selectMemberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        selectMemberAdapter = new SelectMemberAdapter(this);
        members_lv.setAdapter(selectMemberAdapter);
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

}