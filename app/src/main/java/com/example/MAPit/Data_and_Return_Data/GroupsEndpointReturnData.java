package com.example.MAPit.Data_and_Return_Data;

import java.util.ArrayList;
import com.mapit.backend.groupApi.model.Search;
/**
 * Created by shubhashis on 2/9/2015.
 */
public class GroupsEndpointReturnData {
    private String responseMessages;
    private ArrayList<Search> dataList;

    public String getResponseMessages() {
        return responseMessages;
    }

    public void setResponseMessages(String responseMessages) {
        this.responseMessages = responseMessages;
    }

    public ArrayList<Search> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Search> dataList) {
        this.dataList = dataList;
    }
}
