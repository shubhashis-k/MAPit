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

    Friends_mail1 ("Mail1"),
    Friends_mail2 ("Mail2"),
    Friends_status ("Status")
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
