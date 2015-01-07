package com.mapit.backend;

/**
 * Created by shubhashis on 1/7/2015.
 */
public enum DatastoreKindNames {
    Userinfo ("Userinfo")

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
