package com.example.admin.keyproirityapp.model;

import java.util.ArrayList;


public class Conversation {
    private ArrayList<Message> listMessageData;
    private ArrayList<GroupMessage> groupMessageArrayList;

    public Conversation() {
        listMessageData = new ArrayList<>();
        groupMessageArrayList = new ArrayList<>();
    }

    public ArrayList<Message> getListMessageData() {
        return listMessageData;
    }

    public ArrayList<GroupMessage> getGroupMessageArrayList() {
        return groupMessageArrayList;
    }

}
