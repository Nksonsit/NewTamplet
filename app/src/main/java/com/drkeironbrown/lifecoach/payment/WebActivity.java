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
import android.widget.Toast;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.PayMoney;
import com.drkeironbrown.lifecoach.ui.GalleryListActivity;
import com.drkeironbrown.lifecoach.ui.SlideshowListActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private int PaymentClickType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        PaymentClickType = getIntent().getIntExtra("type",1);
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
                    intent.putExtra("msg","Payment Cancel");
                    setResult(1010,intent);
                    finish();
                } else if (url.contains("success")) {
                    Toast.makeText(WebActivity.this, "Payment Success", Toast.LENGTH_LONG).show();
                    setPaymentDone();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7WJJC9659CTC6");
    }

    private void setPaymentDone() {
        PayMoney payMoney = new PayMoney();
        payMoney.setCatId(getIntent().getIntExtra("catId",0));
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
                        Functions.showToast(WebActivity.this, response.body().getMessage(), MDToast.TYPE_SUCCESS);
                        Intent intent = new Intent();
                        intent.putExtra("isSuccess",true);
                        intent.putExtra("pType",PaymentClickType);
                        setResult(1012,intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("msg",response.body().getMessage());
                        setResult(1010,intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("msg",getString(R.string.try_again));
                    setResult(1010,intent);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Intent intent = new Intent();
                intent.putExtra("msg",getString(R.string.try_again));
                setResult(1010,intent);
                finish();
            }
        });
    }
}
