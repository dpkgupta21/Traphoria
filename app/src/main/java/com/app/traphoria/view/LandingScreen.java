package com.app.traphoria.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.traphoria.R;

public class LandingScreen extends AppCompatActivity implements View.OnClickListener {
    private TextView sigup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_screen);
        initViews();
        assignClicks();
    }

    private void assignClicks() {
        sigup.setOnClickListener(this);
    }

    private void initViews() {
        sigup = (TextView) findViewById(R.id.sigup);
    }

    public void openLoginScreen(View view) {
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sigup:
                Intent intent = new Intent(LandingScreen.this, MobileNumberVerificationScreen.class);
                startActivity(intent);

                break;
        }
    }
}
