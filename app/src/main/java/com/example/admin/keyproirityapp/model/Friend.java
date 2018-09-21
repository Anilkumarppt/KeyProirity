package com.example.admin.keyproirityapp.model;


public class Friend extends User {
    public String id;
    public String idRoom;
    public boolean isSelected;

    public Friend() {

    }

    public Friend(String name, String email, String avata, String mobile, String deviceToken) {
        super(name, email, avata, mobile, deviceToken);
    }
}
