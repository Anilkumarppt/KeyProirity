package com.example.admin.keyproirityapp.model;

/**
 * Created by Dell on 9/6/2018.
 */

public class DeviceTokens {
    String deviceToken, name, uid;

    public DeviceTokens(String deviceToken, String name, String uid) {
        this.deviceToken = deviceToken;
        this.name = name;
        this.uid = uid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
