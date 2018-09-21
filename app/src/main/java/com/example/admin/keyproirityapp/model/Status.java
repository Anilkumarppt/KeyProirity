package com.example.admin.keyproirityapp.model;


public class Status {
    public boolean isOnline;
    public long timestamp;

    public Status() {
        isOnline = false;
        timestamp = 0;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
