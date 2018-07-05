package com.example.admin.keyproirityapp.model;

public class AllUsers {
    public String name;
    public String email;
    public String avata;
    public String mobile;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AllUsers() {
    }

    public AllUsers(String name, String email, String avata, String mobile) {
        this.name = name;
        this.email = email;
        this.avata = avata;
        this.mobile = mobile;
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
