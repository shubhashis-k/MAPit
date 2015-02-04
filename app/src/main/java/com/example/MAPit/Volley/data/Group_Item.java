package com.example.MAPit.Volley.data;

/**
 * Created by SETU on 1/24/2015.
 */
public class Group_Item {
    private String group_name, group_image, group_location;

    public Group_Item() {

    }

    public Group_Item(String group_name, String group_image, String group_location) {
        super();
        this.group_name = group_name;
        this.group_image = group_image;
        this.group_location = group_location;
    }


    public String getGroup_Name() {
        return group_name;
    }

    public void setGroup_Name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_Image() {
        return group_image;
    }

    public void setGroup_Image(String group_image) {
        this.group_image = group_image;
    }

    public String getGroup_location() {
        return group_location;
    }

    public void setGroup_location(String group_location) {
        this.group_location = group_location;
    }

}