package com.drkeironbrown.lifecoach.model;

public class UpdateNotificationReq {
    private long UserId;
    private int GetNotification;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getGetNotification() {
        return GetNotification;
    }

    public void setGetNotification(int getNotification) {
        GetNotification = getNotification;
    }
}
