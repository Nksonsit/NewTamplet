package com.drkeironbrown.lifecoach.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout llEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        llEmptyView = (LinearLayout) findViewById(R.id.llEmptyView);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        txtTitle.setText("Vision board");
        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnAdd = (TfButton) findViewById(R.id.btnAdd);
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        rvGallery.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list = DBOpenHelper.getGalleryList();
        if (list.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
            rvGallery.setVisibility(View.GONE);
        } else {
            llEmptyView.setVisibility(View.GONE);
            rvGallery.setVisibility(View.VISIBLE);
        }
        adapter = new GalleryAdapter(this, list, new GalleryAdapter.OnClickItem() {
            @Override
            public void onDeleteClick(final int position) {
                Functions.showAlertDialogWithTwoOption(GalleryListActivity.this, "YES", "NO", "Areyou sure want to delete ?", new Functions.DialogOptionsSelectedListener() {
                    @Override
                    public void onSelect(boolean isYes) {
                        DBOpenHelper.deleteGallery(list.get(position).getGalleryId());
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                        if (list.size() == 0) {
                            llEmptyView.setVisibility(View.VISIBLE);
                            rvGallery.setVisibility(View.GONE);
                        } else {
                            llEmptyView.setVisibility(View.GONE);
                            rvGallery.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }

            @Override
            public void onEditClick(int position) {
                Intent intent = new Intent(GalleryListActivity.this, AddGalleryActivity.class);
                intent.putExtra("gallery", list.get(position));
                Functions.fireIntent(GalleryListActivity.this, intent, true);
            }

            @Override
            public void onSeeClick(int position) {
                Intent intent = new Intent(GalleryListActivity.this, GalleryActivity.class);
                intent.putExtra("title", list.get(position).getGalleryName());
                intent.putExtra("galleryId", list.get(position).getGalleryId());
                Functions.fireIntent(GalleryListActivity.this, intent, true);
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
                Functions.fireIntent(GalleryListActivity.this, AddGalleryActivity.class, true);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list = DBOpenHelper.getGalleryList();
        adapter.setDataList(list);
        if (list.size() == 0) {
            llEmptyView.setVisibility(View.VISIBLE);
            rvGallery.setVisibility(View.GONE);
        } else {
            llEmptyView.setVisibility(View.GONE);
            rvGallery.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
