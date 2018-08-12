package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;

import java.io.File;

public class ZoomablImageActivity extends AppCompatActivity {

    private android.widget.ImageView imgZoom;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomabl_image);

        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        this.imgBack = (ImageView) findViewById(R.id.imgBack);
        this.imgZoom = (ImageView) findViewById(R.id.imgZoom);
        imgZoom.setOnTouchListener(new ImageMatrixTouchHandler(this));
        Functions.loadImage(this, new File(getIntent().getStringExtra("image")), imgZoom, null);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this,false);
    }
}
