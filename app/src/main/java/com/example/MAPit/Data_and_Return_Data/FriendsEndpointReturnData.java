package com.example.MAPit.Data_and_Return_Data;

import com.mapit.backend.friendsApi.model.Friends;

import java.util.ArrayList;

/**
 * Created by shubhashis on 2/5/2015.
 */
public class FriendsEndpointReturnData {
    private String responseMessages;
    private ArrayList <Friends> friendList;

    public String getResponseMessages() {
        return responseMessages;
    }

    public void setResponseMessages(String responseMessages) {
        this.responseMessages = responseMessages;
    }

    public ArrayList<Friends> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<Friends> friendList) {
        this.friendList = friendList;
    }
}
