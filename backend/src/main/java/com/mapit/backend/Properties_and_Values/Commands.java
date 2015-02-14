package com.mapit.backend.Properties_and_Values;

/**
 * Created by shubhashis on 1/7/2015.
 */

//format Kind_command
public enum Commands {
    Userinfo_getmail ("Get_Mail"),
    Userinfo_getpass ("Get_Pass"),
    Userinfo_getinfo ("Get_Info"),
    Userinfo_update ("Update"),
    Userinfo_create ("Create"),

    Request_Group ("Request"),
    Accept_Group ("Accept"),
    Leave_Group ("Leave")
    ;

    private String command;

    Commands (String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}
