package com.drkeironbrown.lifecoach.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.AdvancedSpannableString;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private com.drkeironbrown.lifecoach.custom.TfTextView txtDr;
    private View bottomSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        TedPermission.with(this).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        init();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Functions.showToast(SplashActivity.this, "You can not proceed further without allow.", MDToast.TYPE_INFO);
                        onBackPressed();
                    }
                }).check();
    }

    private void init() {
        bottomSpace = findViewById(R.id.bottomSpace);
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

        if (Functions.hasSoftKeys(this, getWindowManager())) {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,102);
            bottomSpace.setLayoutParams(param);
        }

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
