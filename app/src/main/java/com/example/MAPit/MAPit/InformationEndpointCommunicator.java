package com.example.MAPit.MAPit;

import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.informationApi.model.Information;
import com.mapit.backend.informationApi.InformationApi;
import com.mapit.backend.informationApi.model.InformationCollection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhashis on 2/26/2015.
 */
public class InformationEndpointCommunicator extends AsyncTask <Pair<Data,Information>, Void, ArrayList <Information>>{
    private InformationApi informationApi;
    private Information information;

    private String command, category;
    @Override
    protected ArrayList<Information> doInBackground(Pair<Data, Information>... params) {
        /*if(informationApi == null) {
            InformationApi.Builder builder = new InformationApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://true-harmony-490.appspot.com/_ah/api/");
            informationApi = builder.build();
        }*/

        if(informationApi == null){
            InformationApi.Builder builder = new InformationApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            informationApi = builder.build();
        }

        command = params[0].first.getCommand();
        category = params[0].first.getExtra();
        information = params[0].second;

        if(command.equals(Commands.Information_set.getCommand())){
            try {
                informationApi.setInformation(information).execute();
            }
            catch(IOException io){

            }
        }
        else if(command.equals(Commands.Information_get.getCommand())){
            try {
                InformationCollection informationCollection = informationApi.getInformation(category).execute();
                ArrayList <Information> result = (ArrayList<Information>) informationCollection.getItems();

                return result;
            }
            catch(IOException io){

            }
        }
        return null;
    }
}
