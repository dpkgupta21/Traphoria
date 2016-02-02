package com.app.traphoria.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.traphoria.R;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_btn;
    private TextView terms_policy;
    private RelativeLayout signup_btn_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_screen);
        initViews();
        assignClicks();
    }

    private void assignClicks() {
        back_btn.setOnClickListener(this);
        signup_btn_rl.setOnClickListener(this);
        terms_policy.setOnClickListener(this);
    }

    private void initViews() {
        signup_btn_rl = (RelativeLayout) findViewById(R.id.signup_btn_rl);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        terms_policy = (TextView) findViewById(R.id.terms_policy);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.signup_btn_rl:
                break;
            case R.id.terms_policy:
                startActivity(new Intent(this, TermsAndConditionScreen.class));
                break;

        }

    }
}
