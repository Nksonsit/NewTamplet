package com.drkeironbrown.lifecoach.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.GalleryAdapter;
import com.drkeironbrown.lifecoach.model.Gallery;

import java.util.ArrayList;
import java.util.List;

public class GalleryListActivity extends AppCompatActivity {

    private GalleryAdapter adapter;
    private RecyclerView rvGallery;
    private List<Gallery> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        rvGallery.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new GalleryAdapter(this,list);
        rvGallery.setAdapter(adapter);
    }
}
