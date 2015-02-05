package com.example.MAPit.Data_and_Return_Data;

import android.content.Context;

/**
 * Created by shubhashis on 2/5/2015.
 */
public class Data {
    private Context context;
    private String usermail;
    private String Command;

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
}
