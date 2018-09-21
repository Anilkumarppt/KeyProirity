package com.example.admin.keyproirityapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 8/31/2018.
 */

public class GroupMessage {
    public String idSender;
    public String idReceiver;
    public String text;
    public long timestamp;
    String senderName;
    List<GroupMessage> groupMessageList;
    private Boolean multimedia = false;
    private String contentType;
    private String contentLocation = "";

    public GroupMessage(String idSender, String idReceiver, String text, long timestamp, Boolean multimedia, String contentType, String senderName) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.text = text;
        this.timestamp = timestamp;
        this.multimedia = multimedia;
        this.contentType = contentType;
        this.senderName = senderName;
        groupMessageList = new ArrayList<>();
    }

    public List<GroupMessage> getGroupMessageList() {
        return groupMessageList;
    }

    public void setGroupMessageList(List<GroupMessage> groupMessageList) {
        this.groupMessageList = groupMessageList;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Boolean multimedia) {
        this.multimedia = multimedia;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
