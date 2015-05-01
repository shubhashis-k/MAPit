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
    public final String Friend_Request_Exists = "Friend Request Already Sent!";

    public final String Duplicate_Group = "Group Exists!";
    public final String Group_Created = "Group Created!";
    public final String Group_Available = "Group Available!";
    public final String Group_Deleted = "Group Deleted!";

    public final String Person_not_in_group = "Person is not in the Group!";
    public final String Person_in_group = "Person is in the Group!";
    public final String Person_added_Pending = "Reuquest Sent! Status Pending";
    public final String Person_added_Accepted = "Person added!";
    public final String Person_removed = "Person removed!";

    public final String GCM_registered = "Registered";
    public final String GCM_notregistered = "Not registered";

    public void setMessage(String message)
    {
        response_message = message;
    }

    public String getMessage()
    {
        return response_message;
    }
}