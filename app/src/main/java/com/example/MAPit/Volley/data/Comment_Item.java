package com.example.MAPit.Volley.data;

/**
 * Created by SETU on 1/22/2015.
 */
public class Comment_Item {
    private int id;
    private String user_name, user_comment, user_image, comment_timeStamp;

    public Comment_Item() {

    }

    public Comment_Item(String user_name, String user_image, String user_comment,
                        String comment_timeStamp) {
        super();
        this.user_name = user_name;
        this.user_image = user_image;
        this.user_comment = user_comment;
        this.comment_timeStamp = comment_timeStamp;
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

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getComment_TimeStamp() {
        return comment_timeStamp;
    }

    public void setComment_TimeStamp(String comment_timeStamp) {
        this.comment_timeStamp = comment_timeStamp;
    }


}