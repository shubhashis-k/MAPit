package com.example.MAPit.Commands_and_Properties;

/**
 * Created by shubhashis on 1/7/2015.
 */

// Format Entity_PropertyName
public enum PropertyNames {
    Userinfo_Username ("Username"),
    Userinfo_Mail ("Mail"),
    Userinfo_Password ("Password"),
    Userinfo_Mobile ("Mobile"),
    Userinfo_Profilepic ("Profilepic"),
    Userinfo_latitude ("Latitude"),
    Userinfo_longitude ("Longitude"),

    Friends_mail1 ("Mail1"),
    Friends_mail2 ("Mail2"),
    Friends_status ("Status"),

    Groups_groupname ("Groupname"),
    Groups_creatormail ("CreatorMail"),
    Groups_latitude ("latitude"),
    Groups_longitude ("longitude"),
    Groups_Description ("description"),

    PersonsInGroup_personMail ("PersonMail"),
    PersonsInGroup_groupKey ("GroupKey"),
    PersonsInGroup_status ("Status"),

    Status_groupKey ("groupKey"),
    Status_personMail ("personMail"),
    Status_latitude ("latitude"),
    Status_longitude ("longitude"),
    Status_text ("text"),
    Status_time ("time"),
    Status_image ("sttusimg"),

    Information_Food ("Food"),
    Information_Education ("Education"),
    Information_Transport ("Transport"),
    Information_Religion ("Religion"),
    Information_Market ("Market"),
    Information_Accomodation ("Accomodation"),
    Marker_Position("pos"),

    Group_Public ("public"),
    Group_Private ("private"),
    Group_Permission ("perm"),
    Group_logged ("log"),
    ;

    private String Property;

    PropertyNames(String Property)
    {
        this.Property = Property;
    }

    public String getProperty ()
    {
        return Property;
    }
}
