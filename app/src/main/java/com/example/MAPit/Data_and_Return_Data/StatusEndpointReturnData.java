package com.example.MAPit.Data_and_Return_Data;

import com.mapit.backend.statusApi.model.Status;

import java.util.ArrayList;

/**
 * Created by shubhashis on 2/14/2015.
 */
public class StatusEndpointReturnData {
    private String response;
    private ArrayList <Status> statusList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<Status> getStatusList() {
        return statusList;
    }

    public void setStatusList(ArrayList<Status> statusList) {
        this.statusList = statusList;
    }
}
