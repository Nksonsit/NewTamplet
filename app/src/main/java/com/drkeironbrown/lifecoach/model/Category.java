package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;

public class Category implements Serializable {
    private long CategoryId;
    private String CategoryName;
    private String CategoryDetail;
    private String CategoryAudioPath;
    private String CategoryBannerPath;
    private String CategoryPrice;
    private String IsSubData;
    private String SubLinks;
    private String SubCategory;

    public String getCategoryImagePath() {
        return CategoryBannerPath;
    }

    public void setCategoryImagePath(String categoryImagePath) {
        CategoryBannerPath = categoryImagePath;
    }

    public String getIsSubData() {
        return IsSubData;
    }

    public void setIsSubData(String isSubData) {
        IsSubData = isSubData;
    }

    public String getSubLinks() {
        return SubLinks;
    }

    public void setSubLinks(String subLinks) {
        SubLinks = subLinks;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getCategoryPrice() {
        return CategoryPrice;
    }

    public void setCategoryPrice(String categoryPrice) {
        CategoryPrice = categoryPrice;
    }

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

    public String getCategoryAudioPath() {
        return CategoryAudioPath;
    }

    public void setCategoryAudioPath(String categoryAudioPath) {
        CategoryAudioPath = categoryAudioPath;
    }
}
