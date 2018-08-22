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
import com.drkeironbrown.lifecoach.custom.WebViewDialog;
import com.drkeironbrown.lifecoach.helper.AdvancedSpannableString;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgLife;
    private TfEditText edtUserName;
    private CheckBox cbTermCondition2;
    private CheckBox cbGetMail2;
    private TfButton btnSignIn;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSignUp;
    private LinearLayout llRegisterView;
    private TfTextView txtReadTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.llRegisterView = (LinearLayout) findViewById(R.id.llRegisterView);
        this.txtSignUp = (TfTextView) findViewById(R.id.txtSignUp);
        this.txtReadTC = (TfTextView) findViewById(R.id.txtReadTC);
        this.btnSignIn = (TfButton) findViewById(R.id.btnSignIn);
        this.cbGetMail2 = (CheckBox) findViewById(R.id.cbGetMail2);
        this.cbTermCondition2 = (CheckBox) findViewById(R.id.cbTermCondition2);
        this.edtUserName = (TfEditText) findViewById(R.id.edtUserName);
        this.imgLife = (ImageView) findViewById(R.id.imgLife);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Glide.with(this).load(R.drawable.life).into(imgLife);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class, true);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(LoginActivity.this, v);
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.fireIntent(LoginActivity.this, FunctionSlideActivity.class, true);
                }
                PrefUtils.setIsLogin(LoginActivity.this, true);
                if (PrefUtils.isFirstTime(LoginActivity.this)) {
                    Functions.fireIntent(LoginActivity.this, FunctionSlideActivity.class, true);
                } else {
                    Functions.fireIntent(LoginActivity.this, DashboardActivity.class, true);
                }
                finish();
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(LoginActivity.this, v);
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class, true);
            }
        });

        AdvancedSpannableString spannableString = new AdvancedSpannableString("term and condition");
        spannableString.setUnderLine("term and condition");
        txtReadTC.setText(spannableString);
        txtReadTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewDialog(LoginActivity.this);
            }
        });
    }
}
