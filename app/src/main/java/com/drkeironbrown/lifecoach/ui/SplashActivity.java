package com.drkeironbrown.lifecoach.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        PrefUtils.setIsLogin(this,true);
        PrefUtils.setIsLogin(this,true);
        if (PrefUtils.isLogin(this)) {
            if (PrefUtils.isFirstTime(this)) {
                Functions.fireIntent(this, FunctionSlideActivity.class, true);
            } else {
                Functions.fireIntent(this, Dashboard2Activity.class, true);
            }
        } else {
            Functions.fireIntent(this, LoginActivity.class, true);
        }
        finish();
    }
}
