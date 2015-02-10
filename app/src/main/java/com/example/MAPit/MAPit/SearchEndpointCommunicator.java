package com.example.MAPit.MAPit;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.DatastoreKindNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.searchQueriesApi.model.Search;
import com.mapit.backend.searchQueriesApi.SearchQueriesApi;
import com.mapit.backend.searchQueriesApi.model.SearchCollection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhashis on 2/5/2015.
 */
public class SearchEndpointCommunicator extends AsyncTask<Pair<Data, Search>, Void, ArrayList<Search>> {
    public SearchQueriesApi searchQueriesApi;
    public Context context;
    public String usermail, command;


    @Override
    protected ArrayList<Search> doInBackground(Pair<Data, Search>... params) {
        if (searchQueriesApi == null) {
            SearchQueriesApi.Builder builder = new SearchQueriesApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            searchQueriesApi = builder.build();
        }
        context = params[0].first.getContext();
        usermail = params[0].first.getUsermail();
        command = params[0].first.getCommand();

        Search searchData = params[0].second;
        String searchKey = searchData.getData();

        if (command.equals(Commands.Search_users.getCommand())) {
            try {
                SearchCollection searchResult = searchQueriesApi.getResult(DatastoreKindNames.Userinfo.getKind(), searchKey).execute();
                ArrayList<Search> result = (ArrayList<Search>) searchResult.getItems();

                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }



}
