package com.drkeironbrown.lifecoach.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Category;

import java.io.IOException;

public class CategoryDetailActivity extends AppCompatActivity {

    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private android.widget.ImageView play;
    private android.widget.ImageView pause;
    private android.widget.FrameLayout playpauselayout;
    private android.widget.SeekBar mediaseekbar;
    private TfTextView runtime;
    private TfTextView totaltime;
    private android.widget.FrameLayout seekbarlayout;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtCategoryDetail;
    private MediaPlayer mediaPlayer;
    private Category category;
    private Handler handler;
    private Runnable runnable;
    private android.widget.LinearLayout llAudioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        this.llAudioView = (LinearLayout) findViewById(R.id.llAudioView);
        this.txtCategoryDetail = (TfTextView) findViewById(R.id.txtCategoryDetail);
        this.seekbarlayout = (FrameLayout) findViewById(R.id.seekbar_layout);
        this.totaltime = (TfTextView) findViewById(R.id.total_time);
        this.runtime = (TfTextView) findViewById(R.id.run_time);
        this.mediaseekbar = (SeekBar) findViewById(R.id.media_seekbar);
        this.playpauselayout = (FrameLayout) findViewById(R.id.play_pause_layout);
        this.pause = (ImageView) findViewById(R.id.pause);
        this.play = (ImageView) findViewById(R.id.play);
        this.toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        this.txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.imgBack = (ImageView) findViewById(R.id.imgBack);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        category = (Category) getIntent().getSerializableExtra("category");

        txtTitle.setText(category.getCategoryName());

        txtCategoryDetail.setText(Html.fromHtml(category.getCategoryDetail()));

        mediaPlayer = new MediaPlayer();

        if (category.getCategoryAudioPath() != null && category.getCategoryAudioPath().trim().length() > 0) {
            try {
                mediaPlayer.setDataSource(category.getCategoryAudioPath());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            llAudioView.setVisibility(View.GONE);
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int totalSec = mediaPlayer.getDuration();
                String time = String.format("%02d", ((int) (totalSec / 60))) + ":" + String.format("$02d", ((int) totalSec % 60));
                totaltime.setText(time);
            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentSec = mediaPlayer.getCurrentPosition();
                String time = String.format("%02d", ((int) (currentSec / 60))) + ":" + String.format("$02d", ((int) currentSec % 60));
                runtime.setText(time);
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
