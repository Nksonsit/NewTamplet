package com.drkeironbrown.lifecoach.model;

public class User {
    private int UserId;
    private String Username;
    private String EmailId;
    private int Noti;

    public int getNoti() {
        return Noti;
    }

    public void setNoti(int noti) {
        Noti = noti;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

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
}
