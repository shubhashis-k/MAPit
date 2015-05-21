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
    @ApiMethod(name = "checkChatSession", path = "checkChatSessionPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ChatSession checkChatSession(@Named("SessionName") String sessionName){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query chatSessionQuery = new Query(sessionName);

        List<Entity> sessionList = datastore.prepare(chatSessionQuery).asList(FetchOptions.Builder.withLimit(1));

        ChatSession cs = new ChatSession();
        if(sessionList.size() == 1)
        {
            cs.setMsg("1");
        }
        else{
            cs.setMsg("0");
        }

        return cs;
    }

    @ApiMethod(name = "createChatSession", path = "createChatSessionPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void createChatSession(@Named("SessionName") String sessionName) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        ChatSession cs = checkChatSession(sessionName);
        if(!cs.getMsg().equals("0"))
         return;

        Entity e = new Entity(DatastoreKindNames.ChatSessionList.getKind());
        e.setProperty(DatastorePropertyNames.ChatSessionList_chatsessionName.getProperty(), sessionName);
        datastore.put(e);
    }

    @ApiMethod(name = "searchChatSession", path = "searchChatSessionPath", httpMethod = ApiMethod.HttpMethod.POST)
    public List<ChatSession> searchChatSession(@Named("userMail") String userMail) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        List<ChatSession> sessionList = new ArrayList<>();

        Query q = new Query(DatastoreKindNames.ChatSessionList.getKind());
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            String sessionName = (String)result.getProperty(DatastorePropertyNames.ChatSessionList_chatsessionName.getProperty());

            if(sessionName.substring(0, userMail.length()).equals(userMail)){
                String pmail = sessionName.substring(userMail.length(), sessionName.length());

                ChatSession cs = new ChatSession();
                cs.setSessionName(pmail);

                sessionList.add(cs);
            }
            else if(sessionName.substring(sessionName.length()-userMail.length(), sessionName.length()).equals(userMail))
            {
                String pmail = sessionName.substring(0, sessionName.length()-userMail.length());

                ChatSession cs = new ChatSession();
                cs.setSessionName(pmail);

                sessionList.add(cs);
            }
        }
        return sessionList;
    }

    @ApiMethod(name = "insertChatMessage", path = "insertChatMessagePath", httpMethod = ApiMethod.HttpMethod.POST)
    public void insertChatMessage(ChatSession chatSession) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity e = new Entity(chatSession.getSessionName());

        e.setProperty(DatastorePropertyNames.ChatSession_personName.getProperty(), chatSession.getNameofPerson());
        e.setProperty(DatastorePropertyNames.ChatSession_message.getProperty(), chatSession.getMsg());

        String stringDate = chatSession.getDate();

        DateConverter dc = new DateConverter();
        Date date = dc.StringToDate(stringDate);
        e.setProperty(DatastorePropertyNames.ChatSession_msgTime.getProperty(), date);

        datastore.put(e);
        //sendMessageToDevice(chatSession);
    }

    @ApiMethod(name = "fetchChatSession", path = "fetchChatSessionPath", httpMethod = ApiMethod.HttpMethod.POST)
    public List<ChatSession> fetchChatSession(@Named("chatSessionName") String ChatSessionName) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        List<ChatSession> msgList = new ArrayList<>();

        Query chatSessionQuery = new Query(ChatSessionName).addSort(DatastorePropertyNames.ChatSession_msgTime.getProperty(), Query.SortDirection.ASCENDING);

        PreparedQuery queryResult = datastore.prepare(chatSessionQuery);

        for (Entity result : queryResult.asIterable()) {
            Date date = (Date) result.getProperty(DatastorePropertyNames.ChatSession_msgTime.getProperty());
            String msgTime = date.toString();


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