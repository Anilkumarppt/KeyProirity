package com.example.admin.keyproirityapp.model;


public class Message {
    public String idSender;
    public String idReceiver;
    public String text;
    public String name;
    public long timestamp;
    public String contentType, contentLocation;
    private Boolean multimedia = false;
    //

    public Message() {
    }

    public Message(String idSender, String idReceiver, String text, long timestamp, String contentType, String name, String contentLocation) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.text = text;
        this.timestamp = timestamp;
        this.multimedia = false;
        this.contentType = contentType;
        this.name = name;
        this.contentLocation = contentLocation;
    }

    //Constructor for the Multimedia
    public Message(String idSender, String idReceiver, Long timestamp, String contentType, String contentLocation, String text) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.text = text;
        this.timestamp = timestamp;
        this.multimedia = true;
        this.contentType = contentType;
        this.contentLocation = contentLocation;

    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                '}';
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
    //Constructor for Text Message

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
