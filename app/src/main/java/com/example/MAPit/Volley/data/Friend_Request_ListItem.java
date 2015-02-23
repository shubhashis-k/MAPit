package com.example.MAPit.Volley.data;

/**
 * Created by SETU on 1/23/2015.
 */
public class Friend_Request_ListItem {
    private String user_name,user_image,user_location,usermail,stringKey;

    private String button_type;

    public String getStringKey() {
        return stringKey;
    }

    public void setStringKey(String stringKey) {
        this.stringKey = stringKey;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public String getButton_type() {
        return button_type;
    }

    public void setButton_type(String button_type) {
        this.button_type = button_type;
    }

    public String getUser_Name() {
        return user_name;
    }

    public void setUser_Name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_Imge() {
        return user_image;
    }

    public void setUser_Imge(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

}