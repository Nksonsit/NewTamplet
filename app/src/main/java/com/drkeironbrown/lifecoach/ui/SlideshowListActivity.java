package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.SlideshowAdapter;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Slideshow;

import java.util.ArrayList;
import java.util.List;

public class SlideshowListActivity extends AppCompatActivity {

    private RecyclerView rvSlideshow;
    private List<Slideshow> list;
    private SlideshowAdapter adapter;
    private ImageView imgBack;
    private TfTextView txtTitle;
    private RelativeLayout toolbar;
    private LinearLayout btnAdd;
    private LinearLayout llEmptyView;
    private ImageView imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_list);
        llEmptyView = (LinearLayout) findViewById(R.id.llEmptyView);
        btnAdd = (LinearLayout) findViewById(R.id.btnAdd);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Vision Videos");
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgAdd = (ImageView) findViewById(R.id.imgAdd);
        rvSlideshow = (RecyclerView) findViewById(R.id.rvSlideshow);
        rvSlideshow.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        list = DBOpenHelper.getSlideshowList();
        if (list.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
            rvSlideshow.setVisibility(View.GONE);
        } else {
            llEmptyView.setVisibility(View.GONE);
            rvSlideshow.setVisibility(View.VISIBLE);
        }

        adapter = new SlideshowAdapter(this, list, new SlideshowAdapter.OnClickItem() {
            @Override
            public void onDeleteClick(final int position) {
                Functions.showAlertDialogWithTwoOption(SlideshowListActivity.this, "YES", "NO", "Are you sure want to delete ?", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        if (isYes) {
                            DBOpenHelper.deleteSlideshow(list.get(position).getSlideshowId());
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                            if (list.size() == 0) {
                                llEmptyView.setVisibility(View.VISIBLE);
                                rvSlideshow.setVisibility(View.GONE);
                            } else {
                                llEmptyView.setVisibility(View.GONE);
                                rvSlideshow.setVisibility(View.VISIBLE);
                            }
                            new CountDownTimer(1000, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    adapter.setDataList(list);
                                }
                            }.start();
                        }
                    }
                });
            }

            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(SlideshowListActivity.this, AddSlideshowActivity.class);
                intent.putExtra("slideshow", list.get(position));
                Functions.fireIntent(SlideshowListActivity.this, intent, true);
            }

            @Override
            public void onPlayClick(int position) {
                Intent intent = new Intent(SlideshowListActivity.this, SlideshowActivity.class);
                intent.putExtra("title", list.get(position).getSlideshowName());
                intent.putExtra("slideshowId", list.get(position).getSlideshowId());
                Functions.fireIntent(SlideshowListActivity.this, intent, true);
            }
        });
        rvSlideshow.setAdapter(adapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(SlideshowListActivity.this, AddSlideshowActivity.class, true);
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(SlideshowListActivity.this, AddSlideshowActivity.class, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, Dashboard2Activity.class, false);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateData();
    }

    private void updateData() {
        list = DBOpenHelper.getSlideshowList();
        adapter.setDataList(list);
        if (list.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
            rvSlideshow.setVisibility(View.GONE);
        } else {
            llEmptyView.setVisibility(View.GONE);
            rvSlideshow.setVisibility(View.VISIBLE);
        }

    }
}
