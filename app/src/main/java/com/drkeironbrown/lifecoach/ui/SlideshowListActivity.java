package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
    private TfButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_list);
        btnAdd = (TfButton) findViewById(R.id.btnAdd);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        rvSlideshow = (RecyclerView) findViewById(R.id.rvSlideshow);
        rvSlideshow.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        list = DBOpenHelper.getSlideshowList();
        adapter = new SlideshowAdapter(this, list, new SlideshowAdapter.OnClickItem() {
            @Override
            public void onDeleteClick(int position) {
                DBOpenHelper.deleteSlideshow(list.get(position).getSlideshowId());
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(SlideshowListActivity.this, AddSlideshowActivity.class);
                intent.putExtra("slideshow", list.get(position));
                Functions.fireIntent(SlideshowListActivity.this, intent, true);
            }

            @Override
            public void onPlayClick(int position) {

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
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list = DBOpenHelper.getSlideshowList();
        adapter.setDataList(list);
    }
}