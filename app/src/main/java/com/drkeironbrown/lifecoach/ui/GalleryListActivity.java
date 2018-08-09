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
import com.drkeironbrown.lifecoach.adapter.GalleryAdapter;
import com.drkeironbrown.lifecoach.custom.TfButton;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Gallery;

import java.util.ArrayList;
import java.util.List;

public class GalleryListActivity extends AppCompatActivity {

    private GalleryAdapter adapter;
    private RecyclerView rvGallery;
    private List<Gallery> list;
    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private TfButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnAdd = (TfButton) findViewById(R.id.btnAdd);
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        rvGallery.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list = DBOpenHelper.getGalleryList();
        adapter = new GalleryAdapter(this, list, new GalleryAdapter.OnClickItem() {
            @Override
            public void onDeleteClick(int position) {
                DBOpenHelper.deleteGallery(list.get(position).getGalleryId());
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(GalleryListActivity.this, AddSlideshowActivity.class);
                intent.putExtra("slideshow", list.get(position));
                Functions.fireIntent(GalleryListActivity.this, intent, true);
            }

            @Override
            public void onPlayClick(int position) {

            }
        });
        rvGallery.setAdapter(adapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.fireIntent(GalleryListActivity.this, AddSlideshowActivity.class, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
