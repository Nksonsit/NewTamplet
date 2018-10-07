package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;

public class SubCategory implements Serializable {
    private long CategoryId;
    private String CategoryName;
    private String CategoryDetail;
    private String CategorySol;

    public long getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(long categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryDetail() {
        return CategoryDetail;
    }

    public void setCategoryDetail(String categoryDetail) {
        CategoryDetail = categoryDetail;
    }

    public String getCategorySol() {
        return CategorySol;
    }

    public void setCategorySol(String categorySol) {
        CategorySol = categorySol;
    }
}
