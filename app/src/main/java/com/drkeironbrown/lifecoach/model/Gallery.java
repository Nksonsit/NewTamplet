package com.drkeironbrown.lifecoach.model;

import java.util.List;

public class Gallery {
    private List<String> Images;
    private int TotalImage;
    private String GalleryName;
    private int GalleryId;

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public int getTotalImage() {
        return TotalImage;
    }

    public void setTotalImage(int totalImage) {
        TotalImage = totalImage;
    }

    public String getGalleryName() {
        return GalleryName;
    }

    public void setGalleryName(String galleryName) {
        GalleryName = galleryName;
    }

    public int getGalleryId() {
        return GalleryId;
    }

    public void setGalleryId(int galleryId) {
        GalleryId = galleryId;
    }
}
