package com.example.MAPit.MAPit;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import com.mapit.backend.timeBasedSharingApi.TimeBasedSharingApi;
import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.timeBasedSharingApi.model.TimeBasedSharing;
import com.mapit.backend.timeBasedSharingApi.model.TimeBasedSharingCollection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhashis on 5/24/2015.
 */
public class TimeBasedEndpointCommunicator extends AsyncTask<Pair<Data, TimeBasedSharing>, Void, ArrayList<TimeBasedSharing>>{

    TimeBasedSharingApi timeBasedSharingApi;
    @Override
    protected ArrayList<TimeBasedSharing> doInBackground(Pair<Data, TimeBasedSharing>... params) {
        if(timeBasedSharingApi == null){
            TimeBasedSharingApi.Builder builder = new TimeBasedSharingApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            timeBasedSharingApi = builder.build();
        }

        String mail = params[0].first.getUsermail();
        String command = params[0].first.getCommand();
        TimeBasedSharing tbS = params[0].second;



        if(command.equals(Commands.timeBased_getAllInfo.getCommand())){
            try {
                TimeBasedSharingCollection tbCollection = timeBasedSharingApi.getAllTimeBasedSharing(mail).execute();

                ArrayList<TimeBasedSharing> tbData = (ArrayList<TimeBasedSharing>) tbCollection.getItems();
                return tbData;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(command.equals(Commands.locService_setInfo.getCommand())){
            try {
                timeBasedSharingApi.insertTimeBasedSharing(tbS).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }
}
