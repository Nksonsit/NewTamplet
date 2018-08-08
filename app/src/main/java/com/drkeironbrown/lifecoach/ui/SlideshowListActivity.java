package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.SlideshowAdapter;
import com.drkeironbrown.lifecoach.model.Slideshow;

import java.util.ArrayList;
import java.util.List;

public class SlideshowListActivity extends AppCompatActivity {

    private RecyclerView rvSlideshow;
    private List<Slideshow> list;
    private SlideshowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_list);
        rvSlideshow = (RecyclerView) findViewById(R.id.rvSlideshow);
        rvSlideshow.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new SlideshowAdapter(this,list);
        rvSlideshow.setAdapter(adapter);
    }
}
