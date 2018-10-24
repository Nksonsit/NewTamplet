package com.drkeironbrown.lifecoach.model;

public class PaidProduct {
    private long UserId;
    private long Type;
    private long CatId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getType() {
        return Type;
    }

    public void setType(long type) {
        Type = type;
    }

    public long getCatId() {
        return CatId;
    }

    public void setCatId(long catId) {
        CatId = catId;
    }
}
