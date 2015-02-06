package com.example.MAPit.MAPit;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.friendsApi.FriendsApi;
import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.friendsApi.model.ResponseMessages;
import com.mapit.backend.friendsApi.model.Search;
import com.mapit.backend.friendsApi.model.SearchCollection;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhashis on 2/5/2015.
 */
public class FriendsEndpointCommunicator extends AsyncTask <Pair<Data, Friends>, Void, FriendsEndpointReturnData>{
    private FriendsApi friendsApi;
    private Context context;
    private String usermail, command;

    @Override
    protected FriendsEndpointReturnData doInBackground(Pair<Data, Friends>... params) {
        if(friendsApi == null){
            FriendsApi.Builder builder = new FriendsApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.3.2:8080/_ah/api/") //Genymotion Config
                            //.setRootUrl("http://192.168.10.1:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
        }

    context = params[0].first.getContext();
    usermail = params[0].first.getUsermail();
    command = params[0].first.getCommand();

    Friends friendsData = params[0].second;

    if(command.equals(Commands.Friends_Request.getCommand())){
        try {
            ResponseMessages rm = friendsApi.requestFriends(friendsData).execute();
            String response = rm.getResponseMessage();
            FriendsEndpointReturnData returnData = new FriendsEndpointReturnData();
            returnData.setResponseMessages(response);

            return returnData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    else if(command.equals(Commands.Friends_Make.getCommand())){
            try {
                ResponseMessages rm = friendsApi.makeFriends(friendsData).execute();
                String response = rm.getResponseMessage();
                FriendsEndpointReturnData returnData = new FriendsEndpointReturnData();
                returnData.setResponseMessages(response);

                return returnData;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    else if(command.equals(Commands.Friends_fetch.getCommand())){
        try {
            SearchCollection friendsCollection = friendsApi.fetchFriendList("1", usermail).execute();
            ArrayList <Search> friendList = (ArrayList <Search>) friendsCollection.getItems();

            FriendsEndpointReturnData returnData = new FriendsEndpointReturnData();
            returnData.setFriendList(friendList);

            return returnData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    else if(command.equals(Commands.Friends_fetch_Pending.getCommand())){
        try {
            SearchCollection friendsCollection = friendsApi.fetchFriendList("0", usermail).execute();
            ArrayList <Search> friendList = (ArrayList <Search>) friendsCollection.getItems();

            FriendsEndpointReturnData returnData = new FriendsEndpointReturnData();
            returnData.setFriendList(friendList);

            return returnData;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    return null;
    }

}
