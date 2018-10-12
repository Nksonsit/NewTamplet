package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.net.Uri;
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
import com.drkeironbrown.lifecoach.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgLife;
    private TfEditText edtUserName;
    private CheckBox cbGetMail2;
    private TfButton btnSignIn;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtSignUp;
    private TfTextView txtDr;
    private TfTextView txtReadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.txtReadMore = (TfTextView) findViewById(R.id.txtReadMore);
        this.txtSignUp = (TfTextView) findViewById(R.id.txtSignUp);
        this.btnSignIn = (TfButton) findViewById(R.id.btnSignIn);
        this.cbGetMail2 = (CheckBox) findViewById(R.id.cbGetMail2);
        this.edtUserName = (TfEditText) findViewById(R.id.edtUserName);
        this.imgLife = (ImageView) findViewById(R.id.imgLife);
        txtDr = (TfTextView) findViewById(R.id.txtDr);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(LoginActivity.this, v);
                if (!Functions.isConnected(LoginActivity.this)) {
                    Functions.showToast(LoginActivity.this, getString(R.string.check_internet), MDToast.TYPE_ERROR);
                    return;
                }
                if (edtUserName.getText().toString().trim().length() == 0) {
                    Functions.showToast(LoginActivity.this, getString(R.string.please_enter_username), MDToast.TYPE_ERROR);
                    return;
                }
                LoginReq loginReq = new LoginReq();
                loginReq.setFcm("");
                loginReq.setDeviceType("Android");
                loginReq.setGetNotification(cbGetMail2.isChecked() ? 1 : 2);
                loginReq.setUsername(edtUserName.getText().toString().trim());
                RestClient.get().getLogin(loginReq).enqueue(new Callback<BaseResponse<User>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 1 && response.body().getData() != null) {
                                PrefUtils.setIsLogin(LoginActivity.this, true);
                                PrefUtils.setUserFullProfileDetails(LoginActivity.this,response.body().getData());
                                if (PrefUtils.isFirstTime(LoginActivity.this)) {
                                    Functions.fireIntent(LoginActivity.this, FunctionSlideActivity.class, true);
                                } else {
                                    Functions.fireIntent(LoginActivity.this, DashboardActivity.class, true);
                                }
                                finish();
                            } else {
                                Functions.showToast(LoginActivity.this, response.body().getMessage(), MDToast.TYPE_ERROR);
                            }
                        } else {
                            Functions.showToast(LoginActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                        Functions.showToast(LoginActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                    }
                });
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(LoginActivity.this, v);
                Functions.fireIntent(LoginActivity.this, RegisterActivity.class, true);
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

        AdvancedSpannableString readMore = new AdvancedSpannableString("Read more");
        readMore.setUnderLine("Read more");
        txtReadMore.setText(readMore);
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewDialog(LoginActivity.this,"Otp - In","file:///android_res/raw/optin.html");
            }
        });

    }
}
