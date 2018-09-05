package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.AdvancedSpannableString;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private com.drkeironbrown.lifecoach.custom.TfTextView txtDr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        txtDr = (TfTextView) findViewById(R.id.txtDr);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (PrefUtils.isLogin(SplashActivity.this)) {
                    if (PrefUtils.isFirstTime(SplashActivity.this)) {
                        Functions.fireIntent(SplashActivity.this, FunctionSlideActivity.class, true);
                    } else {
                        Functions.fireIntent(SplashActivity.this, Dashboard2Activity.class, true);
                    }
                } else {
                    Functions.fireIntent(SplashActivity.this, LoginActivity.class, true);
                }
                finish();
            }
        }.start();

        AdvancedSpannableString advancedSpannableString = new AdvancedSpannableString(txtDr.getText().toString().trim());
        advancedSpannableString.setUnderLine(txtDr.getText().toString().trim());
        txtDr.setText(advancedSpannableString);
        txtDr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://drkeironbrown.com"));
                startActivity(browserIntent);
            }
        });
    }
}
