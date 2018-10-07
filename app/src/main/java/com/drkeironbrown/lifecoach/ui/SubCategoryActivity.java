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
import com.drkeironbrown.lifecoach.adapter.SubCategoryAdapter;
import com.drkeironbrown.lifecoach.api.RestClient;
import com.drkeironbrown.lifecoach.custom.MDToast;
import com.drkeironbrown.lifecoach.custom.TfTextView;
import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.BaseResponse;
import com.drkeironbrown.lifecoach.model.Category;
import com.drkeironbrown.lifecoach.model.SubCategory;
import com.drkeironbrown.lifecoach.model.SubCategoryReq;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryActivity extends AppCompatActivity {

    private RecyclerView rvSubCategory;
    private Category category;
    private List<String> subCategoryList;
    private SubCategoryAdapter subCategoryAdapter;
    private android.widget.ImageView imgBack;
    private com.drkeironbrown.lifecoach.custom.TfTextView txtTitle;
    private android.widget.RelativeLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        this.toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        this.txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.imgBack = (ImageView) findViewById(R.id.imgBack);
        rvSubCategory = (RecyclerView) findViewById(R.id.rvSubCategory);
        category = (Category) getIntent().getSerializableExtra("category");
        subCategoryList = new ArrayList<>();
        if (category.getSubCategory().contains(",")) {
            String[] temp = category.getSubCategory().split(",");
            for (int i = 0; i < temp.length; i++) {
                subCategoryList.add(temp[i]);
            }
        } else {
            subCategoryList.add(category.getSubCategory());
        }
        subCategoryAdapter = new SubCategoryAdapter(this, subCategoryList, new SubCategoryAdapter.OnSubLinksClick() {
            @Override
            public void onSubLinksClick(int i) {
                getSubCatData(subCategoryList.get(i));
            }
        });
        rvSubCategory.setLayoutManager(new LinearLayoutManager(this));
        rvSubCategory.setAdapter(subCategoryAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtTitle.setText(category.getCategoryName());
    }

    @Override
    public void onBackPressed() {
        Functions.fireIntent(this, false);
    }

    private void getSubCatData(final String subCat) {
        SubCategoryReq subCategoryReq = new SubCategoryReq();
        subCategoryReq.setSubCategoryName(subCat);
        RestClient.get().getSubCategories(subCategoryReq).enqueue(new Callback<BaseResponse<List<SubCategory>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SubCategory>>> call, Response<BaseResponse<List<SubCategory>>> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 1 && response.body().getData() != null && response.body().getData().size() > 0) {
                        SubCategory subCategory = response.body().getData().get(0);
                        Intent intent = new Intent(SubCategoryActivity.this, SubCategoryDetailActivity.class);
                        intent.putExtra("subcat", subCategory);
                        Functions.fireIntent(SubCategoryActivity.this, intent, false);
                    } else {
                        Functions.showToast(SubCategoryActivity.this, response.body().getMessage(), MDToast.TYPE_ERROR);
                    }
                } else {
                    Functions.showToast(SubCategoryActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SubCategory>>> call, Throwable t) {
                Functions.showToast(SubCategoryActivity.this, getString(R.string.try_again), MDToast.TYPE_ERROR);
            }
        });
    }
}
