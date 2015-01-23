package com.example.MAPit.Volley.data;

/**
 * Created by SETU on 1/24/2015.
 */
public class MyFriendsItem {
    private String user_name,user_image,user_location;

    public MyFriendsItem() {

    }

    public MyFriendsItem(String user_name, String user_image, String user_location) {
        super();
        this.user_name = user_name;
        this.user_image = user_image;
        this.user_location=user_location;
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