package com.drkeironbrown.lifecoach.model;

import java.util.List;

public class Slideshow {
    private List<String> Images;
    private int TotalImage;
    private String SlideshowName;
    private int SlideshowId;

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
