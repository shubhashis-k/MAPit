package com.mapit.backend;

/**
 * Created by shubhashis on 1/22/2015.
 */
public class Friends {
    private String mail1;  //The person who requests
    private String mail2;   //The person who Accepts
    private String status;

    public String getMail1() {
        return mail1;
    }

    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    public String getMail2() {
        return mail2;
    }

    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
