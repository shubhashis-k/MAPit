package com.mapit.backend.Properties_and_Values;

/**
 * Created by shubhashis on 1/7/2015.
 */

// Format Entity_PropertyName
public enum DatastorePropertyNames {
    Userinfo_Username ("Username"),
    Userinfo_Mail ("Mail"),
    Userinfo_Password ("Password"),
    Userinfo_Mobile ("Mobile"),
    Userinfo_Profilepic ("Profilepic"),
    Userinfo_latitude ("Latitude"),
    Userinfo_longitude ("Longitude"),
    Userinfo_location ("location"),

    Friends_mail1 ("Mail1"),
    Friends_mail2 ("Mail2"),
    Friends_status ("Status"),

    Groups_groupname ("Groupname"),
    Groups_creatormail ("CreatorMail"),
    Groups_latitude ("latitude"),
    Groups_longitude ("longitude"),
    Groups_Description ("description"),
    Groups_Picture ("GroupPic"),
    Groups_Permission ("permission"),
    Groups_location ("group_loc"),
    
    PersonsInGroup_personMail ("PersonMail"),
    PersonsInGroup_groupKey ("GroupKey"),
    PersonsInGroup_status ("Status"),

    Status_groupKey ("groupKey"),
    Status_personMail ("personMail"),
    Status_personName ("personName"),
    Status_latitude ("latitude"),
    Status_longitude ("longitude"),
    Status_text ("text"),
    Status_time ("time"),
    Status_image ("sttusimg"),
    Status_location ("statloc"),

    Information_All ("All"),
    Information_Kind ("infoKind"),
    Information_name ("name"),
    Information_description ("desc"),
    Information_latitude ("lat"),
    Information_longitude ("lng"),
    Information_infoPic ("infoPic"),
    Information_detailpic ("detailPic"),
    Information_location ("loc"),

    GCMdata_id ("id"),

    ChatSession_personName ("personName"),
    ChatSession_message ("message"),
    ChatSession_msgTime ("msgTime"),


    ChatSessionList_chatsessionName ("csname"),
    ;

    private String Property;

    DatastorePropertyNames (String Property)
    {
        this.Property = Property;
    }

    public String getProperty ()
    {
        return Property;
    }
}
