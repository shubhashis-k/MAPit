package com.example.MAPit.MAPit;

import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.statusApi.StatusApi;
import com.mapit.backend.statusApi.model.StatusCollection;
import com.mapit.backend.statusApi.model.StatusData;
import com.mapit.backend.statusApi.model.StatusDataCollection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhashis on 2/14/2015.
 */
public class StatusEndpointCommunicator extends AsyncTask <Pair<Data, StatusData>, Void, ArrayList <StatusData> > {
    private StatusApi statusApi;
    private String usermail, command, statusKey;
    @Override
    protected ArrayList <StatusData> doInBackground(Pair<Data, StatusData>... params) {
        /*if(statusApi == null) {
            StatusApi.Builder builder = new StatusApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://true-harmony-490.appspot.com/_ah/api/");
            statusApi = builder.build();
        }*/


            if(statusApi == null){
                StatusApi.Builder builder = new StatusApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl(Commands.Ip_address.getCommand()) //Genymotion Config
                                //.setRootUrl("http://192.168.10.1:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                statusApi = builder.build();
            }

    command = params[0].first.getCommand();
    usermail = params[0].first.getUsermail();
    statusKey = params[0].first.getStringKey();

    StatusData statusData = params[0].second;
    if(command.equals(Commands.Status_add.getCommand())){
        try {

            statusApi.addStatus(statusData).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    else if(command.equals(Commands.Status_showGroupStatus.getCommand()) || command.equals(Commands.Status_showIndividualStatus.getCommand())){
        try {
            StatusDataCollection statusDataCollection = statusApi.showStatus(statusData).execute();
            ArrayList <StatusData> statusList = (ArrayList <StatusData>) statusDataCollection.getItems();

            return statusList;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    else if(command.equals(Commands.Status_fetchFriendsStatus.getCommand())){
        try{
            StatusDataCollection statusDataCollection = statusApi.fetchFriendStatus(usermail).execute();
            ArrayList <StatusData> statusList = (ArrayList <StatusData>) statusDataCollection.getItems();

            return statusList;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    else if(command.equals(Commands.Status_Remove.getCommand())){
        try{
            statusApi.removeStatus(statusKey).execute();
        }
        catch (Exception e){

        }
    }

    return null;
    }
}
