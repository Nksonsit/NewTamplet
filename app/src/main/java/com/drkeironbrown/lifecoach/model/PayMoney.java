package com.drkeironbrown.lifecoach.model;

public class PayMoney {

    private long UserId;
    private String Amount;
    private String DeviceData;
    private String Nonce;
    private int Type;
    private long CatId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDeviceData() {
        return DeviceData;
    }

    public void setDeviceData(String deviceData) {
        DeviceData = deviceData;
    }

    public String getNonce() {
        return Nonce;
    }

    public void setNonce(String nonce) {
        Nonce = nonce;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public long getCatId() {
        return CatId;
    }

    public void setCatId(long catId) {
        CatId = catId;
    }
}
