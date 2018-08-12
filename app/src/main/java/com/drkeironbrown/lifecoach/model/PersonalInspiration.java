package com.drkeironbrown.lifecoach.model;

import java.io.Serializable;

public class PersonalInspiration implements Serializable {
    private int PInspirationalId;
    private String PInspirational;
    private int IsByUser;

    public int getPInspirationalId() {
        return PInspirationalId;
    }

    public void setPInspirationalId(int PInspirationalId) {
        this.PInspirationalId = PInspirationalId;
    }

    public String getPInspirational() {
        return PInspirational;
    }

    public void setPInspirational(String PInspirational) {
        this.PInspirational = PInspirational;
    }

    public int getIsByUser() {
        return IsByUser;
    }

    public void setIsByUser(int isByUser) {
        IsByUser = isByUser;
    }
}
