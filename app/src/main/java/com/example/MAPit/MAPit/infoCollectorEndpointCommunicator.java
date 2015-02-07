package com.example.MAPit.MAPit;

/**
 * Created by shubhashis on 2/7/2015.
 */

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.infoCollectorApi.InfoCollectorApi;
import com.mapit.backend.infoCollectorApi.model.InfoCollector;

import java.io.IOException;


/**
 * Created by shubhashis on 2/7/2015.
 */
public class infoCollectorEndpointCommunicator extends AsyncTask<String, Void, InfoCollector> {
    private InfoCollectorApi infoCollectorApi;
    private InfoCollector infoCollector;
    private String key, command;
    @Override
    protected InfoCollector doInBackground(String... params) {
        if(infoCollectorApi == null){
            InfoCollectorApi.Builder builder = new InfoCollectorApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            infoCollectorApi = builder.build();
        }

        key = params[0];
        try {
            infoCollector = infoCollectorApi.getinfo(key).execute();
            return infoCollector;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

    }
}
