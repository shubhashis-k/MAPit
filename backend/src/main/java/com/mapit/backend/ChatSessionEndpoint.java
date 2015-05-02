package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "chatSessionApi",
        version = "v1",
        resource = "chatSession",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class ChatSessionEndpoint {

    @ApiMethod(name = "insertChatMessage", path = "insertChatMessagePath", httpMethod = ApiMethod.HttpMethod.POST)
    public void insertChatMessage(ChatSession chatSession) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity e = new Entity(chatSession.getSessionName());

        e.setProperty(DatastorePropertyNames.ChatSession_personName.getProperty(), chatSession.getNameofPerson());
        e.setProperty(DatastorePropertyNames.ChatSession_message.getProperty(), chatSession.getMsg());

        Date now = new Date();
        e.setProperty(DatastorePropertyNames.ChatSession_msgTime.getProperty(), now);

        datastore.put(e);
        sendMessageToDevice(chatSession);
    }

    @ApiMethod(name = "fetchChatSession", path = "fetchChatSessionPath", httpMethod = ApiMethod.HttpMethod.POST)
    public List<ChatSession> fetchChatSession(@Named("chatSessionName") String ChatSessionName) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        List<ChatSession> msgList = new ArrayList<>();

        Query chatSessionQuery = new Query(ChatSessionName).addSort(DatastorePropertyNames.ChatSession_msgTime.getProperty(), Query.SortDirection.DESCENDING);

        PreparedQuery queryResult = datastore.prepare(chatSessionQuery);

        for (Entity result : queryResult.asIterable()) {
            Date msgTime = (Date) result.getProperty(DatastorePropertyNames.ChatSession_msgTime.getProperty());
            String msg = (String) result.getProperty(DatastorePropertyNames.ChatSession_message.getProperty());
            String name = (String) result.getProperty(DatastorePropertyNames.ChatSession_personName.getProperty());
            ChatSession c = new ChatSession();
            c.setDate(msgTime);
            c.setMsg(msg);
            c.setNameofPerson(name);
            msgList.add(c);
        }

        return msgList;
    }

    @ApiMethod(name = "sendMessageToDevice", path = "sendMessageToDevicePath", httpMethod = ApiMethod.HttpMethod.POST)
    public void sendMessageToDevice(ChatSession chatSession) {
        MessagingEndpoint messagingEndpoint = new MessagingEndpoint();

        MessageData messageData = new MessageData();
        messageData.setMessage(chatSession.getMsg());
        messageData.setRegID(chatSession.getDestinationID());

        try {
            messagingEndpoint.sendMessage(messageData);
        }
        catch(Exception e){

        }

    }
}