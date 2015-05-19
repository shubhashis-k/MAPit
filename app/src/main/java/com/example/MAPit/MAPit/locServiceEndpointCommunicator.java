package com.example.MAPit.MAPit;

import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.locationServiceApi.model.LocationService;
import com.mapit.backend.locationServiceApi.LocationServiceApi;
import com.example.MAPit.Data_and_Return_Data.Data;

import java.io.IOException;

/**
 * Created by shubhashis on 5/19/2015.
 */
public class locServiceEndpointCommunicator extends AsyncTask<Pair<Data, LocationService>, Void, LocationService>{
    LocationServiceApi locationServiceApi;
    @Override
    protected LocationService doInBackground(Pair<Data, LocationService>... params) {
        if(locationServiceApi == null){
            LocationServiceApi.Builder builder = new LocationServiceApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            locationServiceApi = builder.build();
        }

        String mail = params[0].first.getUsermail();
        String Command = params[0].first.getCommand();

        if(Command.equals(Commands.locService_getInfo.getCommand())){
            try {
                LocationService fetchedInfo = locationServiceApi.getlocationService(mail).execute();
                return fetchedInfo;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(Command.equals(Commands.locService_setInfo.getCommand())){
            LocationService lc = params[0].second;
            try {
                locationServiceApi.insertlocationService(lc).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
