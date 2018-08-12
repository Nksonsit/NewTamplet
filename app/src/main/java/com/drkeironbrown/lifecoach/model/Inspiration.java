package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;

public class Inspiration implements Serializable {
    private int InspirationalId;
    private String Inspirational;

    public int getInspirationalId() {
        return InspirationalId;
    }

    public void setInspirationalId(int inspirationalId) {
        InspirationalId = inspirationalId;
    }

    public String getInspirational() {
        return Inspirational;
    }

    public void setInspirational(String inspirational) {
        Inspirational = inspirational;
    }
}
