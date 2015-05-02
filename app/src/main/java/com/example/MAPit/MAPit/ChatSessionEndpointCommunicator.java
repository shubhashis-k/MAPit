package com.example.MAPit.MAPit;

import android.os.AsyncTask;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mapit.backend.chatSessionApi.ChatSessionApi;
import com.mapit.backend.chatSessionApi.model.ChatSession;
import com.mapit.backend.chatSessionApi.model.ChatSessionCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubhashis on 5/3/2015.
 */
public class ChatSessionEndpointCommunicator extends AsyncTask<Data, Void, List<ChatSession>> {

    private ChatSessionApi chatSessionApi;
    @Override
    protected List<ChatSession> doInBackground(Data... params) {
        if(chatSessionApi == null){
            ChatSessionApi.Builder builder = new ChatSessionApi.Builder(AndroidHttp.newCompatibleTransport(),
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
            chatSessionApi = builder.build();
        }

        String name = params[0].getUsername();
        String command = params[0].getCommand();
        String SessionName = params[0].getStringKey();
        String DestinationID = params[0].getExtra();
        String msg = params[0].getExtramsg();

        if(command.equals(Commands.ChatSession_insert.getCommand())){
            ChatSession ChatData = new ChatSession();
            ChatData.setNameofPerson(name);
            ChatData.setMsg(msg);
            ChatData.setDestinationID(DestinationID);
            ChatData.setSessionName(SessionName);

            try {
                chatSessionApi.insertChatMessage(ChatData).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(command.equals(Commands.ChatSession_fetch.getCommand())){
            try {
                ChatSessionCollection chatSessionCollection = chatSessionApi.fetchChatSession(SessionName).execute();

                List<ChatSession>chatSessions = new ArrayList<>();
                chatSessions = chatSessionCollection.getItems();

                return chatSessions;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}