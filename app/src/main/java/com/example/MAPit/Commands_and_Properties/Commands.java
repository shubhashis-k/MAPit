package com.example.MAPit.Commands_and_Properties;

/**
 * Created by shubhashis on 1/7/2015.
 */
public enum Commands {
    Userinfo_getmail ("Get_Mail"),
    Userinfo_getpass ("Get_Pass"),
    Userinfo_getinfo ("Get_Info"),
    Userinfo_update ("Update"),
    Userinfo_create ("Create"),

    Friends_Request ("Request"),
    Friends_Make ("Make"),
    Friends_Remove ("Remove"),
    Friends_fetch ("Fetch Accepted"),
    Friends_fetch_Pending ("Fetch Pending"),
    Friends_fetch_notfriends ("Fetch Not Friends"),

    Search_users ("Users"),
    Search_Groups ("Groups"),

    Button_addFriend ("Add Friend"),
    Button_removeFriend ("Remove Friend"),

    Group_Create ("Create"),
    Group_Remove ("Remove"),
    Group_fetch_myGroups ("myGroups"),
    Group_fetch_GroupsnotMine ("notmyGroups"),
    Group_Join_Group ("Join"),

    Status_add ("addsttus"),
    Status_showGroupStatus ("groupStatus"),
    Status_showIndividualStatus ("individualStatus"),
    Status_fetchFriendsStatus ("friendStatus"),


    Fragment_Caller ("Caller"),
    Called_From_Home ("Home"),
    Called_From_Info ("Info"),


    Arraylist_Values ("data")

    ;

    private String command;

    Commands (String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}
