package com.app.traphoria.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import traphoria.com.app.traphoria.R;

public class MobileNumberVerificationScreen extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mobile_num_form, send_btn_rl, verification_code_form, verify_btn_rl;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_number_verification_screen);
        initViews();
        assignClicks();
    }

    private void initViews() {
        mobile_num_form = (RelativeLayout) findViewById(R.id.mobile_num_form);
        send_btn_rl = (RelativeLayout) findViewById(R.id.send_btn_rl);
        verification_code_form = (RelativeLayout) findViewById(R.id.verification_code_form);
        verify_btn_rl = (RelativeLayout) findViewById(R.id.verify_btn_rl);
        back_btn = (ImageView) findViewById(R.id.back_btn);

    }

    private void assignClicks() {
        send_btn_rl.setOnClickListener(this);
        verify_btn_rl.setOnClickListener(this);
        back_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.send_btn_rl:
                mobile_num_form.setVisibility(View.GONE);
                verification_code_form.setVisibility(View.VISIBLE);
                break;

            case R.id.verify_btn_rl:
                finish();
                Intent intent = new Intent(this, SignUpScreen.class);
                startActivity(intent);
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
