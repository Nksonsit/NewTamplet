package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.MessageDialog;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.AlarmHelper;
import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.helper.PrefUtils;

import java.util.Random;

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
    private TfTextView txtTitle;
    private LinearLayout llSettings;
    private LinearLayout llLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        PrefUtils.setIsFirstTime(this, false);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.llSecondThought = (LinearLayout) findViewById(R.id.llSecondThought);
        this.llJournal = (LinearLayout) findViewById(R.id.llJournal);
        this.llPInspirational = (LinearLayout) findViewById(R.id.llPInspirational);
        this.llInspirational = (LinearLayout) findViewById(R.id.llInspirational);
        this.llSlideshow = (LinearLayout) findViewById(R.id.llSlideshow);
        this.llGallery = (LinearLayout) findViewById(R.id.llGallery);
        this.llShop = (LinearLayout) findViewById(R.id.llShop);
        this.llCategory = (LinearLayout) findViewById(R.id.llCategory);
        this.llSettings = (LinearLayout) findViewById(R.id.llSettings);
        this.llLogout = (LinearLayout) findViewById(R.id.llLogout);
        this.toolbar = (LinearLayout) findViewById(R.id.toolbar);
        llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, CategoriesActivity.class, true);
            }
        });

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

        llShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, ShopActivity.class, true);
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
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(Dashboard2Activity.this, SettingsActivity.class, true);
            }
        });
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.showAlertDialogWithTwoOption(Dashboard2Activity.this, "YES", "NO", "Are you sure want to logout ?", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if(isYes){
                            PrefUtils.setIsFirstTime(Dashboard2Activity.this, true);
                            PrefUtils.setIsLogin(Dashboard2Activity.this, false);

                            PrefUtils.setIsInspirationalSet(Dashboard2Activity.this, false);
                            PrefUtils.setIsPInspirationalSet(Dashboard2Activity.this, false);

                            Functions.fireIntentWithClearFlag(Dashboard2Activity.this, LoginActivity.class, false);
                            finish();
                        }
                    }
                });
            }
        });


        AlarmHelper alarmHelper = new AlarmHelper();
        if (!PrefUtils.isInspirational(this)) {
            final int min = AppConstant.StartingHour;
            final int max = AppConstant.EndingHour;
            final int random = new Random().nextInt((max - min) + 1) + min;
            PrefUtils.setIsInspirationalSet(this, true);
            alarmHelper.setReminder(this, AppConstant.INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, false, true);
        }
        if (!PrefUtils.isPInspirational(this) && DBOpenHelper.getPInspirationalCount() > 0) {
            final int min = AppConstant.StartingHour;
            final int max = AppConstant.EndingHour;
            final int random = new Random().nextInt((max - min) + 1) + min;
            PrefUtils.setIsPInspirationalSet(this, true);
            alarmHelper.setReminder(this, AppConstant.P_INSPIRATIONAL_NOTI_ID, Dashboard2Activity.class, random, 0, false, false);
        }

        if (getIntent().getStringExtra("msg") != null) {
            new MessageDialog(this,getIntent().getStringExtra("msg"));
        }
    }
}
