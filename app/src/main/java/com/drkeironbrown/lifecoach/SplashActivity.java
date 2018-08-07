package com.drkeironbrown.lifecoach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.drkeironbrown.lifecoach.helper.Functions;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Functions.fireIntent(this,LoginActivity.class,true);
    }
}
