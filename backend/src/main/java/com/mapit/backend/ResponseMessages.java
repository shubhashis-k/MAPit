package com.mapit.backend;

/**
 * Created by shubhashis on 1/3/2015.
 */
public class ResponseMessages {
    public String response_message;

    public final String Userinfo_creation_OK = "OK";
    public final String Userinfo_creation_duplicate = "DUPLICATE";
    public final String Userinfo_update_OK = "Update OK";

    public final String Friend_Request_Pending = "Friend Request Sent!";
    public final String Friend_Request_Accepted = "Friend Request Accepted!";
    public final String Friend_Request_Deleted = "Friend Request Deleted!";

    public void setMessage(String message)
    {
        response_message = message;
    }

    public String getMessage()
    {
        return response_message;
    }
}