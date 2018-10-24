package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.SubLinksAdapter;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.Category;
import com.drkeironbrown.lifecoach.model.CategoryReq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private RecyclerView rvSubLinks;
    private List<String> subLinksList;
    private SubLinksAdapter subLinksAdapter;
    private TfTextView txtGetStart;

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
        this.txtGetStart = (TfTextView) findViewById(R.id.txtGetStart);
        this.imgBack = (ImageView) findViewById(R.id.imgBack);
        this.rvSubLinks = (RecyclerView) findViewById(R.id.rvSubLinks);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        category = (Category) getIntent().getSerializableExtra("category");


        rvSubLinks.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        subLinksList = new ArrayList<>();
        if (category.getIsSubData().equalsIgnoreCase("1")) {
            if (category.getSubLinks().contains(",")) {
                String[] temp = category.getSubLinks().split(",");
                for (int i = 0; i < temp.length; i++) {
                    subLinksList.add(temp[i]);
                }
            } else {
                subLinksList.add(category.getSubLinks());
            }
            subLinksAdapter = new SubLinksAdapter(this, subLinksList, new SubLinksAdapter.OnSubLinksClick() {
                @Override
                public void onSubLinksClick(int i) {
                    getSubLinkDetail(subLinksList.get(i));
                }
            });
            rvSubLinks.setAdapter(subLinksAdapter);
            rvSubLinks.setVisibility(View.VISIBLE);
            txtGetStart.setVisibility(View.GONE);
        } else if (category.getIsSubData().equalsIgnoreCase("2")) {

            rvSubLinks.setVisibility(View.GONE);
            txtGetStart.setVisibility(View.VISIBLE);
        } else {
            txtGetStart.setVisibility(View.GONE);
            rvSubLinks.setVisibility(View.GONE);
        }


        txtTitle.setText(category.getCategoryName());

        txtCategoryDetail.setText("<![CDATA["+Html.fromHtml(category.getCategoryDetail())+"?]]>");

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

        txtGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryDetailActivity.this, SubCategoryActivity.class);
                intent.putExtra("category", category);
                Functions.fireIntent(CategoryDetailActivity.this, intent, true);
            }
        });
    }

    private void getSubLinkDetail(String sublink) {
        CategoryReq categoryReq = new CategoryReq();
        categoryReq.setCategoryName(sublink);
        RestClient.get().getCategoriesByName(categoryReq).enqueue(new Callback<BaseResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Category>>> call, Response<BaseResponse<List<Category>>> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 1 && response.body().getData() != null && response.body().getData().size() > 0) {
                        Category subLinkDetail = response.body().getData().get(0);
                        Intent intent = new Intent(CategoryDetailActivity.this, CategoryDetailActivity.class);
                        intent.putExtra("category", subLinkDetail);
                        Functions.fireIntent(CategoryDetailActivity.this, intent, true);
                    } else {
                        Functions.showToast(CategoryDetailActivity.this, response.body().getMessage(), MDToast.TYPE_ERROR);
                    }
                } else {
                    Functions.showToast(CategoryDetailActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Category>>> call, Throwable t) {
                Functions.showToast(CategoryDetailActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
