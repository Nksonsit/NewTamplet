package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;
import java.util.List;

public class Gallery implements Serializable{
    private List<Image> Images;
    private int TotalImage;
    private String GalleryName;
    private String GalleryDateTime;
    private int GalleryId;

    public String getGalleryDateTime() {
        return GalleryDateTime;
    }

    public void setGalleryDateTime(String galleryDateTime) {
        GalleryDateTime = galleryDateTime;
    }

    public List<Image> getImages() {
        return Images;
    }

    public void setImages(List<Image> images) {
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
