package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;
import java.util.List;

public class Slideshow implements Serializable{
    private List<Image> Images;
    private int TotalImage;
    private String SlideshowName;
    private String SlideshowDateTime;
    private int SlideshowId;

    public String getSlideshowDateTime() {
        return SlideshowDateTime;
    }

    public void setSlideshowDateTime(String slideshowDateTime) {
        SlideshowDateTime = slideshowDateTime;
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

    public String getSlideshowName() {
        return SlideshowName;
    }

    public void setSlideshowName(String slideshowName) {
        SlideshowName = slideshowName;
    }

    public int getSlideshowId() {
        return SlideshowId;
    }

    public void setSlideshowId(int slideshowId) {
        SlideshowId = slideshowId;
    }
}
