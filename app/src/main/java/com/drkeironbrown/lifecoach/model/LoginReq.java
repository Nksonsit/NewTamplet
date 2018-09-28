package com.drkeironbrown.lifecoach.model;

public class LoginReq {
    private String Username;
    private int GetNotification;
    private String Fcm;
    private String DeviceType;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getGetNotification() {
        return GetNotification;
    }

    public void setGetNotification(int getNotification) {
        GetNotification = getNotification;
    }

    public String getFcm() {
        return Fcm;
    }

    public void setFcm(String fcm) {
        Fcm = fcm;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }
}
