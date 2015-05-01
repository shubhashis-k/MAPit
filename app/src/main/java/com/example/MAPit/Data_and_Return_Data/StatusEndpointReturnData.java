package com.example.MAPit.Data_and_Return_Data;

import com.mapit.backend.statusApi.model.StatusData;

import java.util.ArrayList;

/**
 * Created by shubhashis on 2/14/2015.
 */
public class StatusEndpointReturnData {
    private String response;
    private ArrayList <StatusData> statusList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<StatusData> getStatusList() {
        return statusList;
    }

    public void setStatusList(ArrayList<StatusData> statusList) {
        this.statusList = statusList;
    }
}
