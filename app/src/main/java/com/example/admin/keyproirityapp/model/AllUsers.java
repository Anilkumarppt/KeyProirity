package com.example.admin.keyproirityapp.model;

public class AllUsers {
    public String name;
    public String email;
    public String avata;
    public String mobile;
    public Status status;
    String uid;
    Long timestamp;

    public AllUsers() {
        status = new Status();
        status.setOnline(status.isOnline);
        status.setTimestamp(status.timestamp);
    }
    public AllUsers(String name, String email, String avata, String mobile, Status status) {
        this.name = name;
        this.email = email;
        this.avata = avata;
        this.mobile = mobile;
        this.status = status;


    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getStatus() {
        return timestamp;
    }

    public void setStatus(Long timestamp) {
        this.status.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}
