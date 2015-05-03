package com.example.MAPit.model;

/**
 * Created by SETU on 5/3/2015.
 */
public class ChatInfo {
    private String direction,chat_text,chat_time;
    public ChatInfo(){

    }
    public ChatInfo(String direction,String chat_text,String chat_time){
        super();
        this.direction=direction;
        this.chat_text=chat_text;
        this.chat_time=chat_time;
    }

    public void setDirection(String direction){
        this.direction=direction;
    }
    public String getDirection(){
        return direction;
    }
    public void setChat_text(String chat_text){
        this.chat_text=chat_text;
    }
    public String getChat_text(){
        return chat_text;
    }
    public void setChat_time(String chat_time){
        this.chat_time=chat_time;
    }
    public String getChat_time(){
        return chat_time;
    }
}
