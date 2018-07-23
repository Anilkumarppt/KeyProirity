package com.example.admin.keyproirityapp.model;

import java.util.ArrayList;

public class ListAllusers {
    private ArrayList<UsersInformation> listFriend;

    public ArrayList<UsersInformation> getListFriend() {
        return listFriend;
    }

    public ListAllusers() {
        listFriend = new ArrayList<>();
    }

    public String getAvataById(String id) {
        for (UsersInformation usersInformation : listFriend) {
            if (id.equals(usersInformation.id)) {
                return usersInformation.avata;
            }
        }
        return "";
    }

    public void setListFriend(ArrayList<UsersInformation> listFriend) {
        this.listFriend = listFriend;
    }

}
