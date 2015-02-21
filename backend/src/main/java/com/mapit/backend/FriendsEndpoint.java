package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;
import com.mapit.backend.Properties_and_Values.Values;

import java.util.ArrayList;


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
        //need to check if duplicate request exists
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

        Query Request_Query = new Query();
        Query.Filter Mail1_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail1.getProperty(), Query.FilterOperator.EQUAL, data.getMail1());
        Query.Filter Mail2_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail2.getProperty(), Query.FilterOperator.EQUAL, data.getMail2());
        Query.Filter request_Filter = Query.CompositeFilterOperator.and(Mail1_Filter, Mail2_Filter);

        Request_Query = new Query(DatastoreKindNames.FriendsData.getKind()).setFilter(request_Filter);

        PreparedQuery queryResult = datastore.prepare(Request_Query);

        Key requestKey = null;

        for (Entity result : queryResult.asIterable()) {
            requestKey = result.getKey();
        }

        if(requestKey == null) {

            Query.Filter AnotherMail1_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail1.getProperty(), Query.FilterOperator.EQUAL, data.getMail2());
            Query.Filter AnotherMail2_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail2.getProperty(), Query.FilterOperator.EQUAL, data.getMail1());
            Query.Filter Anotherrequest_Filter = Query.CompositeFilterOperator.and(AnotherMail1_Filter, AnotherMail2_Filter);

            Query Another_Query = new Query(DatastoreKindNames.FriendsData.getKind()).setFilter(Anotherrequest_Filter);

            PreparedQuery AnotherqueryResult = datastore.prepare(Another_Query);

            for (Entity result : AnotherqueryResult.asIterable()) {
                requestKey = result.getKey();
            }
        }


        return requestKey;
    }

    @ApiMethod(name = "fetchFriendList", path = "fetchFriendListPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> fetchFriendList(@Named("usermail") String Mail, @Named("type") String type) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query Request_Query = new Query();

        if(type.equals("1")) {
            Query.Filter Mail1_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail1.getProperty(), Query.FilterOperator.EQUAL, Mail);
            Query.Filter Mail2_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail2.getProperty(), Query.FilterOperator.EQUAL, Mail);
            Query.Filter request_Filter = Query.CompositeFilterOperator.or(Mail1_Filter, Mail2_Filter);

            Request_Query = new Query(DatastoreKindNames.FriendsData.getKind()).setFilter(request_Filter);
        }
        else if(type.equals("0")){
            Query.Filter Mail_Filter = new Query.FilterPredicate(DatastorePropertyNames.Friends_mail2.getProperty(), Query.FilterOperator.EQUAL, Mail);
            Request_Query = new Query(DatastoreKindNames.FriendsData.getKind()).setFilter(Mail_Filter);
        }
        PreparedQuery queryResult = datastore.prepare(Request_Query);

        ArrayList <Search> friendList = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            if (result.getProperty(DatastorePropertyNames.Friends_status.getProperty()).toString().equals(type)) {
                String fmail1 = result.getProperty(DatastorePropertyNames.Friends_mail1.getProperty()).toString();

                if (!fmail1.equals(Mail)) {
                    Search s = getInfo(fmail1);
                    friendList.add(s);
                }

                String fmail2 = result.getProperty(DatastorePropertyNames.Friends_mail2.getProperty()).toString();

                if (!fmail2.equals(Mail)) {
                    Search s = getInfo(fmail2);
                    friendList.add(s);
                }

            }
        }
        return friendList;
    }

    @ApiMethod(name = "fetchListNotFriends", path = "fetchListNotFriends", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> fetchListNotFriends(@Named("usermail") String Mail, @Named("queryString") String queryString) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        SearchEndpoint searchEndpoint = new SearchEndpoint();

        ArrayList <Search> friendList = fetchFriendList(Mail, "1");
        ArrayList <Search> searchedList = searchEndpoint.getResult(DatastoreKindNames.Userinfo.getKind(), queryString);//returns data with user name

        ArrayList <Search> result = new ArrayList<>();

        Search person = getInfo(Mail);

        for(int i = 0 ; i < searchedList.size(); i++){
            if(!friendList.contains(searchedList.get(i)) && !person.getKey().equals(searchedList.get(i).getKey()))
                result.add(searchedList.get(i));
        }
        return result;
    }

    @ApiMethod(name = "getInfo", path = "getInfo", httpMethod = ApiMethod.HttpMethod.POST)
    public Search getInfo(@Named("usermail") String Mail) throws EntityNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        UserinfoEndpoint userMailKeyfetcher = new UserinfoEndpoint();
        Key key = userMailKeyfetcher.getKeyfromMail(Mail);

        Entity userinfo = datastore.get(key);
        Search info = new Search();
        info.setData(userinfo.getProperty(DatastorePropertyNames.Userinfo_Username.getProperty()).toString());

        Text imageText = (Text) userinfo.getProperty(DatastorePropertyNames.Userinfo_Profilepic.getProperty());
        String ImageData = imageText.getValue();
        if(ImageData.length() > 0)
            info.setPicData(ImageData);

        info.setKey(key);

        info.setLatitude(userinfo.getProperty(DatastorePropertyNames.Userinfo_latitude.getProperty()).toString());
        info.setLongitude(userinfo.getProperty(DatastorePropertyNames.Userinfo_longitude.getProperty()).toString());

        return info;
    }
}