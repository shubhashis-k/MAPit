package com.example.MAPit.Commands_and_Properties;

/**
 * Created by shubhashis on 1/7/2015.
 */
public enum Commands {
    Userinfo_getmail ("Get_Mail"),
    Userinfo_getpass ("Get_Pass"),
    Userinfo_getinfo ("Get_Info"),
    Userinfo_update ("Update"),
    Userinfo_create ("Create")

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
