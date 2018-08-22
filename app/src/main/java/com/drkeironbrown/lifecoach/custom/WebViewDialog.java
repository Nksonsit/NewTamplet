package com.drkeironbrown.lifecoach.custom;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;

import java.util.List;


//import static com.eatup.ui.BaseActivity.showProgress;

/**
 * This class shows dialog of special need of user.
 */
public class WebViewDialog {

    // private variables.
    private Context mActivity;
    private Dialog mDialog;
    private ProgressBar progressbar;

    public WebViewDialog(Context baseActivity) {
        mActivity = baseActivity;
        createDialog();
    }

    private void createDialog() {
        mDialog = new Dialog(mActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_web_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        ((TfTextView) mDialog.findViewById(R.id.dialog_tv_title)).setText("Term and condition");
        WebView webView = (WebView) mDialog.findViewById(R.id.fragment_setting_terms_and_conditions_wv);
        TfButton btnOpenSetting = (TfButton) mDialog.findViewById(R.id.btnOpenSetting);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_res/raw/tc.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {

                    if (mDialog.isShowing() && mDialog != null) {
//                        mDialog.dismiss();
                    }
                    System.gc();
                    Runtime.getRuntime().gc();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mDialog != null) {
                    try {
                        if (mDialog.isShowing()) {
//                            mDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mDialog.show();
            }
        });

        mDialog.findViewById(R.id.pop_window_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(mActivity, v);
                mDialog.dismiss();
            }
        });

        btnOpenSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideKeyPad(mActivity, v);
                mDialog.dismiss();
            }
        });

        btnOpenSetting.setText("OK");

        show();
    }

    /**
     * Show.
     */
    public void show() {
        mDialog.show();
    }

}
