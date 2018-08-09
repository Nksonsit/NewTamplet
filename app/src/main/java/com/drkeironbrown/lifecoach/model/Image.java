package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;

public class Image implements Serializable {
    private String ImagePath;
    private int ImageId;

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }
}
