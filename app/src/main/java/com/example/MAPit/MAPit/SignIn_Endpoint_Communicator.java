package com.example.MAPit.MAPit;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;
import com.mapit.backend.userinfoModelApi.UserinfoModelApi;
import com.mapit.backend.userinfoModelApi.model.UserinfoModelCollection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhashis on 1/8/2015.
 */


public class SignIn_Endpoint_Communicator extends AsyncTask<Pair<Context, UserinfoModel>, Void, UserinfoModelCollection> {
    private Context maincontext;
    private UserinfoModelApi userinfo_api;
    private UserinfoModel userdata;
    private manipulate_Signin manipulator;
    @Override
    protected UserinfoModelCollection doInBackground(Pair<Context, UserinfoModel>... params) {
        if(userinfo_api == null) {  // Only do this once

            OfflineInitializer intializer = new OfflineInitializer();
            UserinfoModelApi.Builder builder = intializer.Initialize();
            userinfo_api = builder.build();

        }
        maincontext = params[0].first;
        userdata = params[0].second;

        try {
            UserinfoModelCollection UserinfoResult = userinfo_api.getUserinfo(Commands.Userinfo_getinfo.getCommand(), userdata).execute();

            return UserinfoResult;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(UserinfoModelCollection result){

        manipulator = (manipulate_Signin) ((Activity) maincontext);

        ArrayList <UserinfoModel> result_list = (ArrayList<UserinfoModel>) result.getItems();
        UserinfoModel logininfo = new UserinfoModel();

        if(result_list.size() > 0)
            logininfo = result_list.get(0);

        manipulator.setResponseMessage(logininfo);
    }

    public interface manipulate_Signin{
        public void setResponseMessage(UserinfoModel logininfo);
    }

}
