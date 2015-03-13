package com.mapit.backend.Properties_and_Values;

/**
 * Created by Debashis7 on 1/22/2015.
 */
public enum Values {
    Friends_Accepted ("1"),
    Friends_Pending ("0"),

    Group_Request_Pending ("0"),
    Group_Request_Accepted ("1"),
    Group_Public ("public"),
    Group_Private ("private"),

    Information_Food ("Food"),
    Information_Education ("Education"),
    Information_Transport ("Transport"),
    Information_Religion ("Religion"),
    Information_Market ("Market"),
    Information_Accomodation ("Accomodation"),
    Information_tourism ("Tourism")

    ;
    private String value;

    Values(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
