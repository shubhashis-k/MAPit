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

    Friends_mail1 ("Mail1"),
    Friends_mail2 ("Mail2"),
    Friends_status ("Status"),

    Groups_groupname ("Groupname"),
    Groups_creatormail ("CreatorMail"),
    Groups_latitude ("latitude"),
    Groups_longitude ("longitude"),

    PersonsInGroup_personMail ("PersonMail"),
    PersonsInGroup_groupKey ("GroupKey"),
    PersonsInGroup_status ("Status"),

    Status_groupKey ("groupKey"),
    Status_personMail ("personMail"),
    Status_latitude ("latitude"),
    Status_longitude ("longitude"),
    Status_text ("text"),
    Status_time ("time")
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
