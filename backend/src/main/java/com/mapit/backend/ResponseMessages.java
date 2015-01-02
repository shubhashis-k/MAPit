package com.mapit.backend;

/**
 * Created by shubhashis on 1/3/2015.
 */
public class ResponseMessages {
    public String response_message;

    public final String Userinfo_creation_OK = "OK";
    public final String Userinfo_creation_duplicate = "DUPLICATE";

    public void setMessage(String message)
    {
        response_message = message;
    }

    public String getMessage()
    {
        return response_message;
    }
}