package com.example.mohitbhoria.demoproject;

public class Details {
    String postId;
    String userId;
    String postTitle, description, postStatus;
    String address;
    String latLng;
    String postedOn;

    public Details(String postId, String userId, String postTitle, String description, String postStatus, String address, String latLng, String postedOn) {
        this.postId = postId;
        this.userId = userId;
        this.postTitle = postTitle;
        this.description = description;
        this.postStatus = postStatus;
        this.address = address;
        this.latLng = latLng;
        this.postedOn = postedOn;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }
}