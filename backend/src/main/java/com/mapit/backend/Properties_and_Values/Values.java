package com.mapit.backend.Properties_and_Values;

/**
 * Created by Debashis7 on 1/22/2015.
 */
public enum Values {
    Friends_Accepted ("1"),
    Friends_Pending ("0")
    ;
    private String value;

    Values(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
