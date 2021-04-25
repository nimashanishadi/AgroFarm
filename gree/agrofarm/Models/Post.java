package com.example.agrofarm.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    public String postKey;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private Object timeStamp;

    public Post(String title, String description, String picture, String userId,String postKey) {
        this.postKey=postKey;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post() {


    }
    //Getters


    public String getPostKey() {
        return postKey;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    //Setters
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
