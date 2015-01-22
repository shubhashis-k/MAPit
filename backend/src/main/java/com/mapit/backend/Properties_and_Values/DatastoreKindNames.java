package com.mapit.backend.Properties_and_Values;

/**
 * Created by shubhashis on 1/7/2015.
 */
public enum DatastoreKindNames {
    Userinfo ("Userinfo"),
    FriendsData ("FriendsData")
    ;

    private String kind;

    DatastoreKindNames(String kind)
    {
        this.kind = kind;
    }

    public String getKind()
    {
        return kind;
    }
}
