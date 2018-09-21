package com.example.admin.keyproirityapp.model;

public class ChatModel {
    private String time;
    private String message;
    private String imageUrl;
    private String imageThumnailUrl;
    private String avatarUrl;
    private boolean isSender;
    private String userName;

    public ChatModel() {
    }

    public ChatModel(String time, String message, String imageUrl, String imageThumnailUrl, String avatarUrl, boolean isSender, String userName) {
        this.time = time;
        this.message = message;
        this.imageUrl = imageUrl;
        this.imageThumnailUrl = imageThumnailUrl;
        this.avatarUrl = avatarUrl;
        this.isSender = isSender;
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageThumnailUrl() {
        return imageThumnailUrl;
    }

    public void setImageThumnailUrl(String imageThumnailUrl) {
        this.imageThumnailUrl = imageThumnailUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}