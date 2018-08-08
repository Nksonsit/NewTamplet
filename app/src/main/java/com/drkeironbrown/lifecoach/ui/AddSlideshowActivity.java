package com.drkeironbrown.lifecoach.ui;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.ImageAdapter;
import com.drkeironbrown.lifecoach.custom.TfEditText;
import com.drkeironbrown.lifecoach.model.Image;

import java.util.ArrayList;
import java.util.List;

public class AddSlideshowActivity extends AppCompatActivity {

    private RecyclerView rvImage;
    private TfEditText edtSlideshowName;
    private ImageAdapter adapter;
    private List<Image> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slideshow);
        rvImage = (RecyclerView) findViewById(R.id.rvImage);
        edtSlideshowName = (TfEditText) findViewById(R.id.edtSlideshowName);
        rvImage.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Image());
        }
        adapter = new ImageAdapter(this, list, false);
        rvImage.setAdapter(adapter);

    }
}
