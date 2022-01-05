package com.example.collegeadmin.notice;

public class NoticeModel {
    String title,date,time,image,uniqueKey;

    public NoticeModel(String title, String date, String time, String image, String uniqueKey) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.image = image;
        this.uniqueKey = uniqueKey;
    }

    public String getTitle() {
        return title;
    }

    public NoticeModel() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }
}
