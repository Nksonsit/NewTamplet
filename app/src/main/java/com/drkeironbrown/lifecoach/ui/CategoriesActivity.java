package com.drkeironbrown.lifecoach.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drkeironbrown.lifecoach.R;
import com.drkeironbrown.lifecoach.adapter.CategoryAdapter;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {

    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;
    private android.support.v7.widget.RecyclerView rvCategories;
    private android.widget.LinearLayout llEmptyView;
    private List<Category> list;
    private CategoryAdapter adapter;
    private TfTextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        this.llEmptyView = (LinearLayout) findViewById(R.id.llEmptyView);
        this.rvCategories = (RecyclerView) findViewById(R.id.rvCategories);
        this.toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        this.txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.txtEmpty = (TfTextView) findViewById(R.id.txtEmpty);
        this.imgBack = (ImageView) findViewById(R.id.imgBack);

        list = new ArrayList<>();
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(this, list);
        rvCategories.setAdapter(adapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtTitle.setText("Categories");

        if (Functions.isConnected(this)) {
            RestClient.get().getCategories().enqueue(new Callback<BaseResponse<List<Category>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<Category>>> call, Response<BaseResponse<List<Category>>> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1 && response.body().getData() != null) {
                            list = response.body().getData();
                            adapter.setDataList(list);
                            rvCategories.setVisibility(View.VISIBLE);
                            llEmptyView.setVisibility(View.GONE);

                        } else {
                            rvCategories.setVisibility(View.GONE);
                            txtEmpty.setVisibility(View.VISIBLE);

                        }
                    } else {
                        rvCategories.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<Category>>> call, Throwable t) {
                    rvCategories.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Functions.showToast(CategoriesActivity.this, getString(R.string.check_internet), MDToast.TYPE_ERROR);
            rvCategories.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText(R.string.check_internet);
        }
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }
}
