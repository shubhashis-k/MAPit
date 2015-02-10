package com.mapit.backend;

/**
 * Created by shubhashis on 2/7/2015.
 */
public class infoCollector {
    private UserinfoModel userdata;
    private Groups groupdata;

    public UserinfoModel getUserdata() {
        return userdata;
    }

    public void setUserdata(UserinfoModel userdata) {
        this.userdata = userdata;
    }

    public Groups getGroupdata() {
        return groupdata;
    }

    public void setGroupdata(Groups groupdata) {
        this.groupdata = groupdata;
    }
}
