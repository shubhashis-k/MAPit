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


public class Edit_Profile_Endpoint_Communicator extends AsyncTask <Pair<Context, UserinfoModel>, Void, UserinfoModel> {
    private Context maincontext;
    private UserinfoModelApi userinfo_api;
    private UserinfoModel userdata;
    private manipulate_Edit_Profile manipulator;

    @Override
    protected UserinfoModel doInBackground(Pair<Context, UserinfoModel>... params) {
        if(userinfo_api == null) {  // Only do this once

            OfflineInitializer intializer = new OfflineInitializer();
            UserinfoModelApi.Builder builder = intializer.Initialize();
            userinfo_api = builder.build();

        }
        maincontext = params[0].first;
        userdata = params[0].second;

        try {
                userinfo_api.setUserInfo(Commands.Userinfo_update.getCommand(), userdata).execute();
                UserinfoModel EditedInfo = userdata;
                return EditedInfo;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(UserinfoModel editedInfo){

        manipulator = (manipulate_Edit_Profile) maincontext;
        manipulator.setInfo(editedInfo);

    }

    public interface manipulate_Edit_Profile {
        public void setInfo(UserinfoModel editedInfo);
    }

}