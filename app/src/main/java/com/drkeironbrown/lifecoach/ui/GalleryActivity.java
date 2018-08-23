package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.GridAdapter;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Image;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TfTextView txtTitle;
    private ImageView imgGrid;
    private RelativeLayout toolbar;
    private RecyclerView rvGallery;
    private boolean is4X4 = true;
    private List<Image> list;
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        imgGrid = (ImageView) findViewById(R.id.imgGrid);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        txtTitle.setText(getIntent().getStringExtra("title"));
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final GridLayoutManager gm = new GridLayoutManager(this, 4);
        rvGallery.setLayoutManager(gm);
        list = new ArrayList<>();
        list = DBOpenHelper.getImagesFromGallery(getIntent().getIntExtra("galleryId", 0));
        adapter = new GridAdapter(this, list, false, true, new GridAdapter.OnClick() {
            @Override
            public void onRemoveClick(int position) {
                DBOpenHelper.removeImage(list.get(position).getImageId());
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        rvGallery.setAdapter(adapter);
        imgGrid.setImageResource(R.drawable.ic_action_grid_5x5);
        imgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is4X4) {
                    is4X4 = false;
                    gm.setSpanCount(5);
                    adapter.setIs4x4(false);
                    imgGrid.setImageResource(R.drawable.ic_action_grid_5x5);
                } else {
                    is4X4 = true;
                    gm.setSpanCount(4);
                    adapter.setIs4x4(true);
                    imgGrid.setImageResource(R.drawable.ic_action_grid_4x4);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, GalleryListActivity.class, false);
        finish();
    }
}
