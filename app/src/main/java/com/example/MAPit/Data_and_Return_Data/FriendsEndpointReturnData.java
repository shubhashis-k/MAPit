package com.example.MAPit.Data_and_Return_Data;

import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.friendsApi.model.Search;
import com.mapit.backend.friendsApi.model.SearchCollection;

import java.util.ArrayList;

/**
 * Created by shubhashis on 2/5/2015.
 */
public class FriendsEndpointReturnData {
    private String responseMessages;
    private ArrayList <Search> friendList;

    public String getResponseMessages() {
        return responseMessages;
    }

    public void setResponseMessages(String responseMessages) {
        this.responseMessages = responseMessages;
    }

    public ArrayList<Search> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<Search> friendList) {
        this.friendList = friendList;
    }
}
