package com.example.admin.keyproirityapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Room {
    public ArrayList<String> member;
    public Map<String, String> groupInfo;
    public ArrayList<String> memberTokens;

    public Room() {
        member = new ArrayList<>();
        groupInfo = new HashMap<String, String>();
        memberTokens = new ArrayList<>();
    }
}
