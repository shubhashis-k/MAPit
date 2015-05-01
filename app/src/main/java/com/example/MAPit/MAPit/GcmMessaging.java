package com.example.MAPit.MAPit;

import android.content.Context;
import android.os.AsyncTask;

import com.mapit.backend.messagingApi.MessagingApi;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by shubhashis on 5/1/2015.
 */
public class GcmMessaging extends AsyncTask <Void, Void, Void>{
    private static MessagingApi messageService = null;
    private GoogleCloudMessaging gcm;
    private Context context;

    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "968750656543";

    public GcmMessaging(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (messageService == null) {
            MessagingApi.Builder builder = new MessagingApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                    // otherwise they can be skipped
                    .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end of optional local run code

            messageService = builder.build();
        }

        String msg = "I am Groot";
        try {
            messageService.messagingEndpoint().sendMessage(msg).execute();
        }catch(Exception e){

        }
        return null;
    }

}
