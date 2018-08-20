package com.drkeironbrown.lifecoach.model;

public class Shop {
    private String BookName;
    private int BookImg;
    private String BookPrice;
    private String BookUrl;

    public Shop(String bookName, int bookImg, String bookPrice, String bookUrl) {
        BookName = bookName;
        BookImg = bookImg;
        BookPrice = bookPrice;
        BookUrl = bookUrl;
    }

    public String getBookUrl() {
        return BookUrl;
    }

    public void setBookUrl(String bookUrl) {
        BookUrl = bookUrl;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public int getBookImg() {
        return BookImg;
    }

    public void setBookImg(int bookImg) {
        BookImg = bookImg;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }
}
