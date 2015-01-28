package com.mapit.backend;

import com.google.appengine.api.datastore.Key;

/**
 * Created by shubhashis on 1/28/2015.
 */
public class PersonsInGroup {
    private String personMail;
    private String groupKey;
    private String status;

    public String getPersonMail() {
        return personMail;
    }

    public void setPersonMail(String personMail) {
        this.personMail = personMail;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
