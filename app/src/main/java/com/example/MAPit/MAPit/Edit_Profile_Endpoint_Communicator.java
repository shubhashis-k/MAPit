package com.example.MAPit.MAPit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.userinfoModelApi.model.ResponseMessages;
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;
import com.mapit.backend.userinfoModelApi.UserinfoModelApi;

import java.io.IOException;

/**
 * Created by shubhashis on 1/8/2015.
 */


public class Edit_Profile_Endpoint_Communicator extends AsyncTask <Pair<Context, UserinfoModel>, Void, ResponseMessages> {
    private Context maincontext;
    private UserinfoModelApi userinfo_api;
    private UserinfoModel userdata;
    private manipulate_Edit_Profile manipulator;

    @Override
    protected ResponseMessages doInBackground(Pair<Context, UserinfoModel>... params) {
        if(userinfo_api == null) {  // Only do this once
            UserinfoModelApi.Builder builder = new UserinfoModelApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            userinfo_api = builder.build();
        }
        maincontext = params[0].first;
        userdata = params[0].second;

        try {
            ResponseMessages response = new ResponseMessages();
            response = userinfo_api.setUserInfo(Commands.Userinfo_update.getCommand(), userdata).execute();
            return response;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(ResponseMessages response){
        Log.v("response", String.valueOf(maincontext));

        //modification needed here
        //modification needed here
         manipulator = (manipulate_Edit_Profile) maincontext;
        manipulator.setResponseMessage(response);
        //modification needed here
        //modification needed here
    }

    public interface manipulate_Edit_Profile {
        public void setResponseMessage(ResponseMessages response);
    }

}