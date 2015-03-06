package com.example.MAPit.MAPit;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.userinfoModelApi.UserinfoModelApi;

import java.io.IOException;

/**
 * Created by Debashis7 on 1/17/2015.
 */
public class OfflineInitializer {
    public UserinfoModelApi.Builder Initialize() {
        /*
        UserinfoModelApi.Builder builder = new UserinfoModelApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl("https://true-harmony-490.appspot.com/_ah/api/");
        return builder;
        }*/

            UserinfoModelApi.Builder builder = new UserinfoModelApi.Builder(AndroidHttp.newCompatibleTransport(),
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

            return builder;
        }

}

