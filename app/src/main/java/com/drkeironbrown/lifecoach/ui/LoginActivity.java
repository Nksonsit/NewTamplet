package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.custom.TfEditText;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView txtSignIn;
    private View dashSignIn;
    private TextView txtSignUp;
    private View dashSignUp;
    private TfEditText edtUserName;
    private CheckBox cbTermCondition;
    private CheckBox cbGetMail;
    private TfButton btnSignIn;
    private LinearLayout llLoginView;
    private TfEditText edtUserName2;
    private TfEditText edtEmail;
    private CheckBox cbTermCondition2;
    private CheckBox cbGetMail2;
    private TfButton btnSignUp;
    private LinearLayout llRegisterView;
    private Animation loginViewSlideLeft;
    private Animation loginViewSlideRight;
    private Animation registerViewSlideRight;
    private Animation registerViewSlideLeft;
    private boolean isLoginView = true;
    private ImageView imgLake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        llRegisterView = (LinearLayout) findViewById(R.id.llRegisterView);
        btnSignUp = (TfButton) findViewById(R.id.btnSignUp);
        cbGetMail2 = (CheckBox) findViewById(R.id.cbGetMail2);
        cbTermCondition2 = (CheckBox) findViewById(R.id.cbTermCondition2);
        edtEmail = (TfEditText) findViewById(R.id.edtEmail);
        edtUserName2 = (TfEditText) findViewById(R.id.edtUserName2);
        llLoginView = (LinearLayout) findViewById(R.id.llLoginView);
        btnSignIn = (TfButton) findViewById(R.id.btnSignIn);
        cbGetMail = (CheckBox) findViewById(R.id.cbGetMail);
        cbTermCondition = (CheckBox) findViewById(R.id.cbTermCondition);
        edtUserName = (TfEditText) findViewById(R.id.edtUserName);
        dashSignUp = (View) findViewById(R.id.dashSignUp);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        dashSignIn = (View) findViewById(R.id.dashSignIn);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        imgLake = (ImageView) findViewById(R.id.imgLake);

        Glide.with(this).load(R.drawable.login_bg).into(imgLake);
        Functions.loadCircularImage(this, R.mipmap.ic_launcher_round, imgLogo, null);
        loginViewSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        loginViewSlideRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        registerViewSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        registerViewSlideRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoginView) {
                    isLoginView = true;
                    llLoginView.startAnimation(loginViewSlideRight);
                    llRegisterView.startAnimation(registerViewSlideRight);
                    dashSignIn.setVisibility(View.VISIBLE);
                    dashSignUp.setVisibility(View.INVISIBLE);
                    txtSignIn.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                    txtSignUp.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.black));
                }
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginView) {
                    isLoginView = false;
                    llRegisterView.startAnimation(registerViewSlideLeft);
                    llLoginView.startAnimation(loginViewSlideLeft);
                    dashSignIn.setVisibility(View.INVISIBLE);
                    dashSignUp.setVisibility(View.VISIBLE);
                    txtSignIn.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.black));
                    txtSignUp.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
                }
            }
        });

        loginViewSlideRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                llLoginView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llLoginView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loginViewSlideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llLoginView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        registerViewSlideRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                llRegisterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegisterView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        registerViewSlideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                llRegisterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegisterView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setIsLogin(LoginActivity.this,true);
                if (PrefUtils.isFirstTime(LoginActivity.this)) {
                    Functions.fireIntent(LoginActivity.this, FunctionSlideActivity.class, true);
                } else {
                    Functions.fireIntent(LoginActivity.this, DashboardActivity.class, true);
                }
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setIsLogin(LoginActivity.this,true);
                if (PrefUtils.isFirstTime(LoginActivity.this)) {
                    Functions.fireIntent(LoginActivity.this, FunctionSlideActivity.class, true);
                } else {
                    Functions.fireIntent(LoginActivity.this, DashboardActivity.class, true);
                }
                finish();
            }
        });
    }
}
