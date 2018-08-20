package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.ShopAdapter;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private android.support.v7.widget.RecyclerView rvShop;
    private List<Shop> list;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        rvShop = (RecyclerView) findViewById(R.id.rvShop);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle.setText("Shop");
        list = new ArrayList<>();
        list.add(new Shop("Don’t Date A Psycho Personality Tests to Discover the REAL You – eBook", R.drawable.img1, "$5.99","https://www.amazon.com/Dont-Date-Psycho-Personality-Discover-ebook/dp/B01M6ASMLJ/ref=sr_1_6?ie=UTF8&qid=1528933620&sr=8-6&keywords=keiron+brown"));
        list.add(new Shop("Don’t Date A Psycho Personality Tests to Discover the REAL You – Paperback", R.drawable.img2, "$16.95","https://www.amazon.com/Dont-Date-Psycho-Personality-Discover/dp/1536986267/ref=sr_1_7?ie=UTF8&qid=1528933620&sr=8-7&keywords=keiron+brown"));
        list.add(new Shop("Don’t Date A Psycho: Don’t Be One, Don’t Date One – eBook", R.drawable.img3, "$5.99","https://www.amazon.com/Dont-Date-Psycho-Be-One-ebook/dp/B00MA8TQ7S/ref=sr_1_2?ie=UTF8&qid=1528933620&sr=8-2&keywords=keiron+brown"));
        list.add(new Shop("Don’t Date A Psycho: Don’t Be One, Don’t Date One – Paperback", R.drawable.img4, "$18.95","https://www.amazon.com/Dont-Date-Psycho-Be-One/dp/1500695130/ref=sr_1_2_twi_pap_2?ie=UTF8&qid=1528933620&sr=8-2&keywords=keiron+brown"));
        list.add(new Shop("Don’t Date A Psycho: The Workbook for Changing Your Life!", R.drawable.img5, "$16.99","https://www.amazon.com/Dont-Date-Psycho-Workbook-Changing/dp/152390450X/ref=sr_1_1?ie=UTF8&qid=1528933620&sr=8-1&keywords=keiron+brown"));
        adapter = new ShopAdapter(this, list);
        rvShop.setLayoutManager(new LinearLayoutManager(this));
        rvShop.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
