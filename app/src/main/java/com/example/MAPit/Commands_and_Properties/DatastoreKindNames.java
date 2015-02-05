package com.example.MAPit.Commands_and_Properties;

/**
 * Created by shubhashis on 1/7/2015.
 */
public enum DatastoreKindNames {
    Userinfo ("Userinfo"),
    FriendsData ("FriendsData"),
    Groups ("Groups"),
    PersonsInGroup ("PersonsInGroup"),
    StatusInGroup ("GroupStatus"),
    StatusbyIndividual ("IndividualStatus")
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
