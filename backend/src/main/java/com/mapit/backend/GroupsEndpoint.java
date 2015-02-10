package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "groupApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class GroupsEndpoint {


    @ApiMethod(name = "CreateGroup", path = "CreateGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages CreateGroup(Groups group) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        ResponseMessages rm = checkGroup(group);
        String s = rm.getMessage();
        if(s.equals(rm.Group_Available)) {

            Entity groupKind = new Entity(DatastoreKindNames.Groups.getKind());
            groupKind.setProperty(DatastorePropertyNames.Groups_groupname.getProperty(), group.getGroupName());
            groupKind.setProperty(DatastorePropertyNames.Groups_creatormail.getProperty(), group.getCreatorMail());
            groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_latitude.getProperty(), group.getLatitude());
            groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_longitude.getProperty(), group.getLongitude());
            groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_Description.getProperty(), group.getGroupDescription());
            datastore.put(groupKind);

            rm.setMessage(rm.Group_Created);
            return rm;
        }
        else
        {
            rm.setMessage(rm.Duplicate_Group);
            return rm;
        }
    }


    @ApiMethod(name = "RemoveGroup", path = "RemoveGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages RemoveGroup(@Named("GroupKey") String groupKey) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.stringToKey(groupKey);
        datastore.delete(key);

        ResponseMessages rm = new ResponseMessages();
        rm.setMessage(rm.Group_Deleted);
        return rm;
    }

    @ApiMethod(name = "checkGroup", path = "checkGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages checkGroup(Groups group)
    {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query(DatastoreKindNames.Groups.getKind());
        Filter group_name_filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_groupname.getProperty(), Query.FilterOperator.EQUAL, group.getGroupName());
        Filter creator_mail_filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_creatormail.getProperty(), Query.FilterOperator.EQUAL, group.getCreatorMail());
        Filter composite = CompositeFilterOperator.and(group_name_filter, creator_mail_filter);
        q.setFilter(composite);

        PreparedQuery pq = datastore.prepare(q);

        ResponseMessages rm = new ResponseMessages();
        for (Entity result : pq.asIterable()) {

           rm.setMessage(rm.Duplicate_Group);
           return rm;
        }

        rm.setMessage(rm.Group_Available);
        return rm;
    }

    @ApiMethod(name = "getMyGroups", path = "getMyGroupsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getMyGroups(@Named("usermail") String mail) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter Creator_Filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_creatormail.getProperty(), Query.FilterOperator.EQUAL, mail);

        Query Request_Query = new Query(DatastoreKindNames.Groups.getKind()).setFilter(Creator_Filter);

        PreparedQuery queryResult = datastore.prepare(Request_Query);

        Key GroupKey = null;

        ArrayList <Search> searchResult = new ArrayList<>();
        for (Entity result : queryResult.asIterable()) {

            GroupKey = result.getKey();

            String groupName = result.getProperty(DatastorePropertyNames.Groups_groupname.getProperty()).toString();

            Search s = new Search();
            s.setKey(GroupKey);
            s.setData(groupName);
            searchResult.add(s);
        }

        return searchResult;
    }

    @ApiMethod(name = "getGroupsNotMine", path = "getGroupsNotMinePath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getGroupsNotMine(@Named("usermail") String mail, @Named("searchQuery") String searchQuery) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        SearchEndpoint getQueryResult = new SearchEndpoint();
        ArrayList <Search> QueryList = getQueryResult.getResult(DatastoreKindNames.Groups.getKind(), searchQuery);
        ArrayList <Search> myGroupList = getMyGroups(mail);

        ArrayList <Search> FilteredList = new ArrayList<>();
        for(int i = 0 ; i < QueryList.size(); i++){
            if(!myGroupList.contains(QueryList.get(i)))
                FilteredList.add(QueryList.get(i));
        }

        return FilteredList;
    }

    @ApiMethod(name = "getGroupKey", path = "getGroupKeyPath", httpMethod = ApiMethod.HttpMethod.POST)
    public Key getGroupKey(Groups group) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query Request_Query = new Query();
        Query.Filter Creator_Filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_creatormail.getProperty(), Query.FilterOperator.EQUAL, group.getCreatorMail());
        Query.Filter GroupName_Filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_groupname.getProperty(), Query.FilterOperator.EQUAL, group.getGroupName());
        Query.Filter Group_Filter = Query.CompositeFilterOperator.and(Creator_Filter, GroupName_Filter);

        Request_Query = new Query(DatastoreKindNames.Groups.getKind()).setFilter(Group_Filter);

        PreparedQuery queryResult = datastore.prepare(Request_Query);

        Key requestKey = null;

        for (Entity result : queryResult.asIterable()) {
            requestKey = result.getKey();
        }


        return requestKey;
    }

}