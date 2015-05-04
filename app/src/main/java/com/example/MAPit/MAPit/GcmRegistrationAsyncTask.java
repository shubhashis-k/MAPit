package com.example.MAPit.MAPit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.mapit.backend.registrationApi.RegistrationApi;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by shubhashis on 4/30/2015.
 */
class GcmRegistrationAsyncTask extends AsyncTask<Data, Void, String> {
    private static RegistrationApi regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    private Data fetchedData;
    private static final String SENDER_ID = "968750656543";
    private int Count = 0;

    @Override
    protected String doInBackground(Data... params) {
        if (regService == null) {
            RegistrationApi.Builder builder = new RegistrationApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                    // otherwise they can be skipped
                    .setRootUrl(Commands.Ip_address.getCommand())
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end of optional local run code

            regService = builder.build();
        }
        fetchedData = params[0];

        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(fetchedData.getContext());
            }
            String regId = gcm.register(SENDER_ID);
            msg = regId;

            regService.register(regId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error";
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        //Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
        if (!msg.equals("Error")) {
            fetchedData.setExtra(msg);
            Log.v("status", "successful at " + Count);
            new GCMRegIDCheckerEndpointCommunicator().execute(fetchedData);
        }
        else{
            Log.v("status", "unsuccessful at " + Count++);
            new GcmRegistrationAsyncTask().execute(fetchedData);
        }
    }
}