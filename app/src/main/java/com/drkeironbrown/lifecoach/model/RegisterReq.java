package com.drkeironbrown.lifecoach.model;

public class RegisterReq {
    private String Username;
    private String EmailId;
    private String FCM;
    private String DeviceType;
    private int GetNotification;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getFCM() {
        return FCM;
    }

    public void setFCM(String FCM) {
        this.FCM = FCM;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public int getGetNotification() {
        return GetNotification;
    }

    public void setGetNotification(int getNotification) {
        GetNotification = getNotification;
    }
}
