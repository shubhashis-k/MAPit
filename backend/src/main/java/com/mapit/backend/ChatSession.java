package com.mapit.backend;

import java.util.Date;

/**
 * Created by shubhashis on 5/3/2015.
 */
public class ChatSession {
    private String sessionName, NameofPerson, msg, destinationID;
    private String date;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getNameofPerson() {
        return NameofPerson;
    }

    public void setNameofPerson(String nameofPerson) {
        NameofPerson = nameofPerson;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(String destinationID) {
        this.destinationID = destinationID;
    }
}
