package com.app.traphoria.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.traphoria.R;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        initViews();
        assignClicks();
    }

    private void assignClicks() {
        back_btn.setOnClickListener(this);
    }

    private void initViews() {
        back_btn = (ImageView) findViewById(R.id.back_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    public void performLogin(View view) {
        startActivity(new Intent(this, NavigationDrawerActivity.class));
    }
}
