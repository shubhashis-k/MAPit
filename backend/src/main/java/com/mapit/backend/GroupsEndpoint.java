package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

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
    public ResponseMessages RemoveGroup(Groups group) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Key groupKey = getGroupKey(group);
        datastore.delete(groupKey);

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


    @ApiMethod(name = "getGroupKey", path = "getGroupKeyPath", httpMethod = ApiMethod.HttpMethod.POST)
    public Key getGroupKey(Groups group) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query Reuqest_Query = new Query();
        Query.Filter Creator_Filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_creatormail.getProperty(), Query.FilterOperator.EQUAL, group.getCreatorMail());
        Query.Filter GroupName_Filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_groupname.getProperty(), Query.FilterOperator.EQUAL, group.getGroupName());
        Query.Filter Group_Filter = Query.CompositeFilterOperator.and(Creator_Filter, GroupName_Filter);

        Reuqest_Query = new Query(DatastoreKindNames.Groups.getKind()).setFilter(Group_Filter);

        PreparedQuery queryResult = datastore.prepare(Reuqest_Query);

        Key requestKey = null;

        for (Entity result : queryResult.asIterable()) {
            requestKey = result.getKey();
        }


        return requestKey;
    }
}