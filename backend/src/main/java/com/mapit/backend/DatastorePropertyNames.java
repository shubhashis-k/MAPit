package com.mapit.backend;

/**
 * Created by shubhashis on 1/7/2015.
 */

// Format Entity_PropertyName
public enum DatastorePropertyNames {
    Userinfo_Username ("Username"),
    Userinfo_Mail ("Mail"),
    Userinfo_Password ("Password"),
    Userinfo_Mobile ("Mobile"),
    Userinfo_Profilepic ("Profilepic")

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
