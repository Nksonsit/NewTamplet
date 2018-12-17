package com.drkeironbrown.lifecoach.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.PayMoney;
import com.drkeironbrown.lifecoach.model.UpdateNotificationReq;
import com.drkeironbrown.lifecoach.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        if (getIntent().getStringExtra("url") == null || getIntent().getStringExtra("url").trim().length() == 0) {
            Functions.showToast(WebActivity.this, "Wrong payment link", MDToast.TYPE_ERROR);
            finish();
        }

        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e("payment", url);
                if (url.contains("cancel")) {
                    Intent intent = new Intent();
                    intent.putExtra("msg", "Payment Cancel");
                    setResult(1010, intent);
                    finish();
                } else if (url.contains("success")) {
                    Functions.showToast(WebActivity.this, "Payment Success", MDToast.TYPE_SUCCESS);
                    updateFullPayApi();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(getIntent().getStringExtra("url"));
    }

/*    private void setPaymentDone() {
        PayMoney payMoney = new PayMoney();
        payMoney.setCatId(getIntent().getIntExtra("catId", 0));
        payMoney.setAmount("1.0");
        payMoney.setDeviceData("");
        payMoney.setNonce("");
        payMoney.setType(PaymentClickType);
        payMoney.setUserId(PrefUtils.getUserFullProfileDetails(WebActivity.this).getUserId());

        RestClient.get().payMoney(payMoney).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 1) {
                        //Functions.showToast(WebActivity.this, response.body().getMessage(), MDToast.TYPE_SUCCESS);
                        Intent intent = new Intent();
                        intent.putExtra("isSuccess", true);
                        intent.putExtra("pType", PaymentClickType);
                        setResult(1012, intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("msg", response.body().getMessage());
                        setResult(1010, intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("msg", getString(R.string.try_again));
                    setResult(1010, intent);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Intent intent = new Intent();
                intent.putExtra("msg", getString(R.string.try_again));
                setResult(1010, intent);
                finish();
            }
        });
    }*/

    private void updateFullPayApi() {
        if (Functions.isConnected(WebActivity.this)) {
            final UpdateNotificationReq updateNotificationReq = new UpdateNotificationReq();
            updateNotificationReq.setUserId(PrefUtils.getUserFullProfileDetails(WebActivity.this).getUserId());
            RestClient.get().updateNotification(updateNotificationReq).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null && response.body().getStatus() == 1) {
                        User user = PrefUtils.getUserFullProfileDetails(WebActivity.this);
                        user.setIsFullPay(1);
                        PrefUtils.setUserFullProfileDetails(WebActivity.this, user);

                        Functions.showToast(WebActivity.this, response.body().getMessage(), MDToast.TYPE_SUCCESS);
                        Intent intent = new Intent();
                        intent.putExtra("isSuccess", true);
                        setResult(1012, intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("msg", getString(R.string.try_again));
                        setResult(1010, intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Intent intent = new Intent();
                    intent.putExtra("msg", getString(R.string.try_again));
                    setResult(1010, intent);
                    finish();
                }
            });
        }
    }
}
