package com.app.traphoria.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.traphoria.R;
import com.app.traphoria.navigationDrawer.NavigationDrawerActivity;
import com.app.traphoria.utility.BaseActivity;
import com.app.traphoria.utility.Utils;
import com.app.traphoria.webservice.LoginWebService;

import org.json.JSONObject;

public class LoginScreen extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        initViews();

    }


    private void initViews() {

        setClick(R.id.btn_login);
        setClick(R.id.back_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_login:
                performLogin();
                break;
        }
    }

    public void performLogin() {

        try {

            if (Utils.isOnline(LoginScreen.this)) {

                if (validateForm()) {
                    LoginWebService loginWebService = new LoginWebService(LoginScreen.this, getEditTextText(R.id.edt_email), getEditTextText(R.id.edt_pwd), "android", "fdfdfd", "", "", "");
                    JSONObject response = loginWebService.doLogin();
                    if (Utils.getWebServiceStatus(response)) {
                        startActivity(new Intent(this, NavigationDrawerActivity.class));
                    } else {
                        Utils.showDialog(this, "Error", Utils.getWebServiceMessage(response));
                    }
                }
            } else {
                Utils.showNoNetworkDialog(LoginScreen.this);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    public boolean validateForm() {

        if (getEditTextText(R.id.edt_email).equals("")) {
            Utils.showDialog(this, "Message", "Please enter username");
            return false;
        } else if (getEditTextText(R.id.edt_pwd).equals("")) {
            Utils.showDialog(this, "Message", "Please enter password");
            return false;
        }
        return true;
    }

}
