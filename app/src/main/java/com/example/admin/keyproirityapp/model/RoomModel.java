package com.example.admin.keyproirityapp.model;

import java.util.ArrayList;
import java.util.List;


public class RoomModel {
    public GroupInfo groupInfo;
    public List<GroupMember> groupMembers;

    public RoomModel() {
        groupInfo = new GroupInfo();
        groupMembers = new ArrayList<>();
    }

    public static class GroupMember {
        public String id;
        public String token;
        public boolean isAdmin;
    }

    public static class GroupInfo {
        public String id;
        public String name;
        public String avtar;
    }
}
