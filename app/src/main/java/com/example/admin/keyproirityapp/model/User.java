package com.example.admin.keyproirityapp.model;


import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String email;
    public String avata;
    public String mobile;
    public Status status;
    public String deviceToken;
    public Message message;
    public ChatModel chatModel;

    public User(String name, String email, String avata, String mobile, Status status, String deviceToken, Message message, ChatModel chatModel) {
        this.name = name;
        this.email = email;
        this.avata = avata;
        this.mobile = mobile;
        this.status = status;
        this.deviceToken = deviceToken;
        this.message = message;
        this.chatModel = chatModel;
    }

    public User() {
        status = new Status();
        message = new Message();
        chatModel = new ChatModel();
        status.isOnline = false;
        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;
        message.setContentType("");
        message.setMultimedia(false);
        message.setContentType("");
    }

    public User(String userName, String email, String avatar, String mobile, String deviceToken) {
        this.name = userName;
        this.email = email;
        this.avata = avatar;
        this.mobile = mobile;
        this.deviceToken = deviceToken;
    }
}
