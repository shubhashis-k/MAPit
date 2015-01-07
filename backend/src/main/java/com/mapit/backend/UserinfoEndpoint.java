package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Text;

import java.util.ArrayList;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "userinfoModelApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class UserinfoEndpoint {
    public ResponseMessages response_messages;

    @ApiMethod(name = "setUserInfo", path = "setUserInfoPath")
    public ResponseMessages setUserInfo(UserinfoModel userinformation) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity Userinfo_Kind = new Entity(DatastoreKindNames.Userinfo.getKind());
        Userinfo_Kind.setProperty(DatastorePropertyNames.Userinfo_Username.getProperty(), userinformation.getName());
        Userinfo_Kind.setProperty(DatastorePropertyNames.Userinfo_Mail.getProperty(), userinformation.getMail());
        Userinfo_Kind.setUnindexedProperty(DatastorePropertyNames.Userinfo_Password.getProperty(), userinformation.getPassword());

        if(userinformation.getMobilephone() != null)
        {
            Userinfo_Kind.setUnindexedProperty(DatastorePropertyNames.Userinfo_Mobile.getProperty(), userinformation.getMobilephone());
        }
        else
        {
            Userinfo_Kind.setUnindexedProperty(DatastorePropertyNames.Userinfo_Mobile.getProperty(), "");
        }

        if(userinformation.getImagedata() != null)
        {
            Text image_Data = new Text(userinformation.getImagedata());
            Userinfo_Kind.setUnindexedProperty(DatastorePropertyNames.Userinfo_Profilepic.getProperty(), image_Data);
        }
        else
        {
            Userinfo_Kind.setUnindexedProperty(DatastorePropertyNames.Userinfo_Profilepic.getProperty(), "");
        }

        ArrayList <UserinfoModel> checkMail = getUserInfo(userinformation);

        if(checkMail.size() > 0)
        {
            response_messages = new ResponseMessages();
            response_messages.setMessage(response_messages.Userinfo_creation_duplicate);
        }
        else
        {
            datastore.put(Userinfo_Kind);
            response_messages = new ResponseMessages();
            response_messages.setMessage(response_messages.Userinfo_creation_OK);
        }
        return response_messages;
    }


    /* Incomplete method, */
    @ApiMethod(name = "getUserInfo", path = "getUserInfoPath")
    public ArrayList<UserinfoModel> getUserInfo(UserinfoModel userinformation) {

        ArrayList <UserinfoModel> Userinfo_Result = new ArrayList<>();

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Filter Mail_Filter = new Query.FilterPredicate(DatastorePropertyNames.Userinfo_Mail.getProperty(), Query.FilterOperator.EQUAL, userinformation.getMail());
        Query Mail_Query = new Query(DatastoreKindNames.Userinfo.getKind()).setFilter(Mail_Filter);

        PreparedQuery queryResult = datastore.prepare(Mail_Query);


        for (Entity result : queryResult.asIterable())
        {
            UserinfoModel um = new UserinfoModel();

            um.setName((String)result.getProperty(DatastorePropertyNames.Userinfo_Username.getProperty()));
            um.setMail((String)result.getProperty(DatastorePropertyNames.Userinfo_Mail.getProperty()));

            Userinfo_Result.add(um);
        }

        return Userinfo_Result;
    }


}