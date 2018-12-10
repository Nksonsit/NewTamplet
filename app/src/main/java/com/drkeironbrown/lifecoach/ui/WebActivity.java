package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.drkeironbrown.lifecoach.R;

public class WebActivity extends AppCompatActivity{

    String TAG = this.getClass().getSimpleName();
    private WebView webView;

    class MyJavaScriptInterface {

        double amount = 0.01;
        String currency = "USD";

        public MyJavaScriptInterface(String currency, double amount) {
            this.currency = currency;
            this.amount = amount;
        }

        @JavascriptInterface
        public void setResponse(String response) {

            Intent intent = new Intent();
            intent.putExtra("payment", response);
            setResult(1012, intent);
        }

        @JavascriptInterface
        public double getAmount() {
            return amount;
        }

        @JavascriptInterface
        public String getCurrency() {
            return currency;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        init();
    }

    void init() {
        this.webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new MyJavaScriptInterface("USD", getIntent().getDoubleExtra("pay", 0.01)), "Android");
        webView.loadUrl("file:///android_asset/paypal_payment.html");


/*
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript("javascript: {document.getElementById('amount').innerHTML = '" + value + "';};", null);
                    view.evaluateJavascript("javascript: {document.getElementById('txt1').value  = '" + value + "';};", null);
                    view.evaluateJavascript("javascript: {document.getElementById('txt2').value  = 'USD';};", null);
                }

            }
        });
*/
    }
/*
    public AlertDialog.Builder getPaymentDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);

        SpannableString title = Utils.getStyle(WebActivity.this, getString(R.string.successPayment), Constants.FONTS.HALOHAND);
        SpannableString okAction = Utils.getStyle(WebActivity.this, "OK", Constants.FONTS.LIGHT);

*//*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setTitle(Html.fromHtml("<font color='black'>" + title + "</font>", Html.FROM_HTML_OPTION_USE_CSS_COLORS));
        } else {
            builder.setTitle(Html.fromHtml("<font color='black'>" + title + "</font>"));
        }
*//*
        builder.setTitle(title);
        builder.setMessage(null);
        builder.setPositiveButton(okAction, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        actionBarBinding.ivAction1.callOnClick();
                    }
                });
            }
        });

        return builder;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_action1) {
            onBackPressed();
        }
    }*/
}
