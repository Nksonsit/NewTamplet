package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.custom.TfEditText;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.custom.WebViewDialog;
import com.drkeironbrown.lifecoach.helper.AdvancedSpannableString;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.LoginReq;
import com.drkeironbrown.lifecoach.model.RegisterReq;
import com.drkeironbrown.lifecoach.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private android.widget.ImageView imgLife;
    private com.drkeironbrown.lifecoach.custom.TfEditText edtUserName;
    private com.drkeironbrown.lifecoach.custom.TfEditText edtEmail;
    private android.widget.CheckBox cbTermCondition2;
    private android.widget.CheckBox cbGetMail2;
    private com.drkeironbrown.lifecoach.custom.TfButton btnSignUp;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSignIn;
    private TfTextView txtReadTC;
    private TfTextView txtDr;
    private TfTextView txtReadMore;
    private View bottomSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bottomSpace = findViewById(R.id.bottomSpace);
        this.txtReadMore = (TfTextView) findViewById(R.id.txtReadMore);
        this.txtReadTC = (TfTextView) findViewById(R.id.txtReadTC);
        this.txtSignIn = (TfTextView) findViewById(R.id.txtSignIn);
        this.btnSignUp = (TfButton) findViewById(R.id.btnSignUp);
        this.cbGetMail2 = (CheckBox) findViewById(R.id.cbGetMail2);
        this.cbTermCondition2 = (CheckBox) findViewById(R.id.cbTermCondition2);
        this.edtEmail = (TfEditText) findViewById(R.id.edtEmail);
        this.edtUserName = (TfEditText) findViewById(R.id.edtUserName);
        this.imgLife = (ImageView) findViewById(R.id.imgLife);
        txtDr = (TfTextView) findViewById(R.id.txtDr);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


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
                    Functions.showToast(RegisterActivity.this, getString(R.string.check_internet), MDToast.TYPE_ERROR);
                    return;
                }
                if (edtUserName.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, getString(R.string.please_enter_username), MDToast.TYPE_ERROR);
                    return;
                }

                if (edtEmail.getText().toString().trim().length() == 0) {
                    Functions.showToast(RegisterActivity.this, getString(R.string.please_enter_email_id), MDToast.TYPE_ERROR);
                    return;
                }
                if (!Functions.emailValidation(edtEmail.getText().toString().trim())) {
                    Functions.showToast(RegisterActivity.this, getString(R.string.enter_valid_email_id), MDToast.TYPE_ERROR);
                    return;
                }
                if(!cbTermCondition2.isChecked()){
                    Functions.showToast(RegisterActivity.this, getString(R.string.accept_tc), MDToast.TYPE_ERROR);
                    return;
                }

                RegisterReq registerReq = new RegisterReq();
                registerReq.setFCM("");
                registerReq.setDeviceType("Android");
                registerReq.setGetNotification(cbGetMail2.isChecked() ? 1 : 2);
                registerReq.setUsername(edtUserName.getText().toString().trim());
                registerReq.setEmailId(edtEmail.getText().toString().trim());
                RestClient.get().getRegister(registerReq).enqueue(new Callback<BaseResponse<User>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1 && response.body().getData() != null) {
                                PrefUtils.setIsLogin(RegisterActivity.this, true);
                                PrefUtils.setUserFullProfileDetails(RegisterActivity.this,response.body().getData());
                                if (PrefUtils.isFirstTime(RegisterActivity.this)) {
                                    Functions.fireIntent(RegisterActivity.this, FunctionSlideActivity.class, true);
                                } else {
                                    Functions.fireIntent(RegisterActivity.this, DashboardActivity.class, true);
                                }
                                finish();
                            } else {
                                Functions.showToast(RegisterActivity.this, response.body().getMessage(), MDToast.TYPE_ERROR);
                            }
                        } else {
                            Functions.showToast(RegisterActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                        Functions.showToast(RegisterActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                    }
                });
            }
        });
        AdvancedSpannableString spannableString = new AdvancedSpannableString("term and condition");
        spannableString.setUnderLine("term and condition");
        txtReadTC.setText(spannableString);
        txtReadTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewDialog(RegisterActivity.this,"Term and condition","file:///android_res/raw/tc.html");
            }
        });


        AdvancedSpannableString readMore = new AdvancedSpannableString("Read more");
        readMore.setUnderLine("Read more");
        txtReadMore.setText(readMore);
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewDialog(RegisterActivity.this,"Otp - In","file:///android_res/raw/optin.html");
            }
        });


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

        if (Functions.hasSoftKeys(this, getWindowManager())) {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,102);
            bottomSpace.setLayoutParams(param);
        }
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
