package com.example.MAPit.Commands_and_Properties;

/**
 * Created by shubhashis on 1/7/2015.
 */
public enum Commands {
    Ip_address ("http://10.0.3.2:8080/_ah/api/"),

    Userinfo_getmail ("Get_Mail"),
    Userinfo_getpass ("Get_Pass"),
    Userinfo_getinfo ("Get_Info"),
    Userinfo_update ("Update"),
    Userinfo_create ("Create"),

    Notification_job ("notification"),
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
    Group_fetch_notification ("fetch"),
    Request_Group ("Request"),
    Accept_Group ("Accept"),
    Leave_Group ("Leave"),
    Groups_fetch_all ("grpall"),
    Group_Key("grpKey"),


    Status_add ("addsttus"),
    Status_showGroupStatus ("groupStatus"),
    Status_showIndividualStatus ("individualStatus"),
    Status_fetchFriendsStatus ("friendStatus"),
    Status_Remove("rmsttus"),


    Fragment_Caller ("Caller"),
    Called_From_Home ("Home"),
    Called_From_Info ("Info"),
    Called_From_Status("Status"),
    Called_From_Group ("Group"),
    Called_From_Location("Location"),
    Called_From_MyWall ("MyWall"),

    Status_Job ("Job"),
    Status_Job_Type_Individual ("Individual"),
    Status_Job_Type_Group ("Group"),


    Arraylist_Values ("data"),

    SearchAndADD("searchandadd"),
    ShowInMap("showinmap"),
    ForMarkerView("markerView"),
    Information_set("set"),
    Information_get("get")
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
