package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.custom.TfEditText;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

public class RegisterActivity extends AppCompatActivity {

    private android.widget.ImageView imgLife;
    private com.drkeironbrown.lifecoach.custom.TfEditText edtUserName;
    private com.drkeironbrown.lifecoach.custom.TfEditText edtEmail;
    private android.widget.CheckBox cbTermCondition2;
    private android.widget.CheckBox cbGetMail2;
    private com.drkeironbrown.lifecoach.custom.TfButton btnSignUp;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSignIn;
    private android.widget.LinearLayout llRegisterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.llRegisterView = (LinearLayout) findViewById(R.id.llRegisterView);
        this.txtSignIn = (TfTextView) findViewById(R.id.txtSignIn);
        this.btnSignUp = (TfButton) findViewById(R.id.btnSignUp);
        this.cbGetMail2 = (CheckBox) findViewById(R.id.cbGetMail2);
        this.cbTermCondition2 = (CheckBox) findViewById(R.id.cbTermCondition2);
        this.edtEmail = (TfEditText) findViewById(R.id.edtEmail);
        this.edtUserName = (TfEditText) findViewById(R.id.edtUserName);
        this.imgLife = (ImageView) findViewById(R.id.imgLife);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Glide.with(this).load(R.drawable.life).into(imgLife);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(RegisterActivity.this, v);
                if (!Functions.isConnected(RegisterActivity.this)) {
                    Functions.fireIntent(RegisterActivity.this, FunctionSlideActivity.class, true);
                }
                PrefUtils.setIsLogin(RegisterActivity.this, true);
                if (PrefUtils.isFirstTime(RegisterActivity.this)) {
                    Functions.fireIntent(RegisterActivity.this, FunctionSlideActivity.class, true);
                } else {
                    Functions.fireIntent(RegisterActivity.this, DashboardActivity.class, true);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
