package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.CustomPagerAdapter;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Image;

import java.util.List;

public class SlideshowActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TfTextView txtTitle;
    private RelativeLayout toolbar;
    private ViewPager viewPager;
    private List<Image> list;
    private CustomPagerAdapter adapter;
    private boolean isHold = false;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("title"));
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        list = DBOpenHelper.getImagesFromSlideshow(getIntent().getIntExtra("slideshowId", 0));
        adapter = new CustomPagerAdapter(this, list, new CustomPagerAdapter.OnTouch() {
            @Override
            public void onHold() {
                isHold = true;
            }

            @Override
            public void onRelease() {
                isHold = false;
            }
        });
        viewPager.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isHold) {
                    if (viewPager.getCurrentItem() < list.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
                handler.postDelayed(runnable, 3000);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 3000);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                isHold = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
