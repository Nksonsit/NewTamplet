package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.payment.WebActivity;

public class PayActivity extends AppCompatActivity {

    private TfTextView txtPrice;
    private TfTextView txtPay;
    private TfTextView txtLabelOr;
    private TfTextView txtFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pay);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        this.txtFree = (TfTextView) findViewById(R.id.txtFree);
        this.txtLabelOr = (TfTextView) findViewById(R.id.txtLabelOr);
        this.txtPay = (TfTextView) findViewById(R.id.txtPay);
        this.txtPrice = (TfTextView) findViewById(R.id.txtPrice);

        txtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, WebActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("url", "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=7WJJC9659CTC6");
                intent.putExtra("catId", 0);

                startActivityForResult(intent, 1011);
            }
        });
        txtFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(PayActivity.this, Dashboard2Activity.class, true);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1011 && resultCode == 1013) {
            Functions.fireIntent(PayActivity.this, Dashboard2Activity.class, true);
            finish();
        }

    }
}
