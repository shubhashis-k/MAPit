package com.example.MAPit.MAPit;

import android.os.AsyncTask;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.GCMEndpointReturnData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.gcmregistrationApi.GcmregistrationApi;
import com.mapit.backend.gcmregistrationApi.model.GCMregistration;
import com.mapit.backend.groupApi.GroupApi;

import java.io.IOException;

/**
 * Created by shubhashis on 5/2/2015.
 */
public class GCMRegIDCheckerEndpointCommunicator extends AsyncTask <Data, Void, GCMEndpointReturnData>{

    private GcmregistrationApi gcmregistrationApi;
    @Override
    protected GCMEndpointReturnData doInBackground(Data... params) {
        if(gcmregistrationApi == null){
            GcmregistrationApi.Builder builder = new GcmregistrationApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            gcmregistrationApi = builder.build();
        }

        String mail = params[0].getUsermail();
        String command = params[0].getCommand();
        String regID = params[0].getExtra();

        if(command.equals(Commands.GCM_getRegID.getCommand())){
            try {
                GCMregistration gcmData = gcmregistrationApi.getGCMregistration(mail).execute();
                GCMEndpointReturnData returnData = new GCMEndpointReturnData();

                if(gcmData != null)
                    returnData.setRegID(gcmData.getRegID());

                return returnData;
            }
            catch (Exception e){

            }
        }
        else if(command.equals(Commands.GCM_setRegID.getCommand())){
            try {
                GCMregistration gcmData = new GCMregistration();
                gcmData.setMail(mail);
                gcmData.setRegID(regID);
                gcmregistrationApi.insertGCMregistration(gcmData).execute();
            }
            catch (Exception e){

            }
        }
        return null;
    }
}
