package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;
import com.mapit.backend.Properties_and_Values.Values;

import java.util.logging.Logger;

import javax.inject.Named;


/**
 * An endpoint class we are exposing
 */
@Api(
        name = "friendsApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class FriendsEndpoint {

    @ApiMethod(name = "requestFriends", path = "requestFriendsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages requestFriends(Friends data) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity friends_kind = new Entity(DatastoreKindNames.FriendsData.getKind());
        friends_kind.setProperty(DatastorePropertyNames.Friends_mail1.getProperty(), data.getMail1());
        friends_kind.setProperty(DatastorePropertyNames.Friends_mail2.getProperty(), data.getMail2());
        friends_kind.setProperty(DatastorePropertyNames.Friends_status.getProperty(), Values.Friends_Pending.getValue());

        datastore.put(friends_kind);

        ResponseMessages rm = new ResponseMessages();
        rm.setMessage(rm.Friend_Request_Pending);
        return rm;
    }


    @ApiMethod(name = "makeFriends", path = "makeFriendsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages makeFriends(Friends data) throws EntityNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key k = getRequestKey(data);

        Entity updateStatus = datastore.get(k);
        updateStatus.setProperty(DatastorePropertyNames.Friends_mail1.getProperty(), data.getMail1());
        updateStatus.setProperty(DatastorePropertyNames.Friends_mail2.getProperty(), data.getMail2());
        updateStatus.setProperty(DatastorePropertyNames.Friends_status.getProperty(), Values.Friends_Accepted.getValue());

        datastore.delete(k);
        datastore.put(updateStatus);

        ResponseMessages rm = new ResponseMessages();
        rm.setMessage(rm.Friend_Request_Accepted);
        return rm;
    }

    @ApiMethod(name = "deleteFriends", path = "deleteFriendsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages deleteFriends(Friends data) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key k = getRequestKey(data);

        datastore.delete(k);

        ResponseMessages rm = new ResponseMessages();
        rm.setMessage(rm.Friend_Request_Deleted);
        return rm;

    }

    @ApiMethod(name = "getRequestKey", path = "getRequestKeyPath", httpMethod = ApiMethod.HttpMethod.POST)
    public Key getRequestKey(Friends data) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query Reuqest_Query = new Query();
        Query.Filter Mail1_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail1.getProperty(), Query.FilterOperator.EQUAL, data.getMail1());

        Query.Filter Mail2_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail2.getProperty(), Query.FilterOperator.EQUAL, data.getMail2());

        Query.Filter request_Filter = Query.CompositeFilterOperator.and(Mail1_Filter, Mail2_Filter);

        Reuqest_Query = new Query(DatastoreKindNames.FriendsData.getKind()).setFilter(request_Filter);

        PreparedQuery queryResult = datastore.prepare(Reuqest_Query);

        Key requestKey = null;

        for (Entity result : queryResult.asIterable()) {
            requestKey = result.getKey();
        }

        return requestKey;
    }
}