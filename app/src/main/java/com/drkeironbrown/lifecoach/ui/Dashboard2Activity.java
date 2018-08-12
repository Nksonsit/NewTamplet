package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

public class Dashboard2Activity extends AppCompatActivity {

    private android.widget.LinearLayout toolbar;
    private android.widget.LinearLayout llCategory;
    private android.widget.LinearLayout llShop;
    private android.widget.LinearLayout llGallery;
    private android.widget.LinearLayout llSlideshow;
    private android.widget.LinearLayout llInspirational;
    private android.widget.LinearLayout llPInspirational;
    private android.widget.LinearLayout llJournal;
    private android.widget.LinearLayout llSecondThought;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        PrefUtils.setIsFirstTime(this, false);
        this.llSecondThought = (LinearLayout) findViewById(R.id.llSecondThought);
        this.llJournal = (LinearLayout) findViewById(R.id.llJournal);
        this.llPInspirational = (LinearLayout) findViewById(R.id.llPInspirational);
        this.llInspirational = (LinearLayout) findViewById(R.id.llInspirational);
        this.llSlideshow = (LinearLayout) findViewById(R.id.llSlideshow);
        this.llGallery = (LinearLayout) findViewById(R.id.llGallery);
        this.llShop = (LinearLayout) findViewById(R.id.llShop);
        this.llCategory = (LinearLayout) findViewById(R.id.llCategory);
        this.toolbar = (LinearLayout) findViewById(R.id.toolbar);
        llSlideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, SlideshowListActivity.class, true);
            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, GalleryListActivity.class, true);
            }
        });

        llJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, JournalListActivity.class, true);
            }
        });

        llPInspirational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, PersonalInspirationalActivity.class, true);
            }
        });

        llInspirational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, InspirationalActivity.class, true);
            }
        });

        llSecondThought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, SecondThoughtActivity.class, true);
            }
        });

    }
}
