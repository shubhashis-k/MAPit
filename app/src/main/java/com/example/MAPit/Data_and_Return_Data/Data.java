package com.example.MAPit.Data_and_Return_Data;

import android.content.Context;

/**
 * Created by shubhashis on 2/5/2015.
 */
public class Data {
    private Context context;
    private String usermail, username;
    private String Command;
    private String extra, extramsg;
    private String StringKey;

    public String getExtramsg() {
        return extramsg;
    }

    public void setExtramsg(String extramsg) {
        this.extramsg = extramsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getStringKey() {
        return StringKey;
    }

    public void setStringKey(String stringKey) {
        StringKey = stringKey;
    }
}
