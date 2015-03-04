package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.datastore.Text;
import com.mapit.backend.Properties_and_Values.Commands;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "personsInGroupApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class PersonsInGroupEndpoint {


    @ApiMethod(name = "requestPersonsInGroup", path = "requestPersonsInGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages requestPersonsInGroup(PersonsInGroup personsInGroup, @Named("Command") String Command) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key requestKey = getRequestKey(personsInGroup);

        ResponseMessages rm = new ResponseMessages();

        if(requestKey == null) {

            Entity Person = new Entity(DatastoreKindNames.PersonsInGroup.getKind());
            Person.setProperty(DatastorePropertyNames.PersonsInGroup_personMail.getProperty(), personsInGroup.getPersonMail());
            Person.setProperty(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty(), personsInGroup.getGroupKey());


            if(Command.equals(Commands.Accept_Group.getCommand())) {
                Person.setProperty(DatastorePropertyNames.PersonsInGroup_status.getProperty(), "1");
                rm.setMessage(rm.Person_added_Accepted);
            }
            else
            {
                Person.setProperty(DatastorePropertyNames.PersonsInGroup_status.getProperty(), "0");
                rm.setMessage(rm.Person_added_Pending);
            }

            datastore.put(Person);

            return rm;
        }
        else
        {
            if(Command.equals(Commands.Request_Group.getCommand())) {
                rm.setMessage(rm.Person_in_group);
                return rm;
            }
            else if(Command.equals(Commands.Accept_Group.getCommand())){
                Entity update = datastore.get(requestKey);
                datastore.delete(requestKey);

                update.setProperty(DatastorePropertyNames.PersonsInGroup_personMail.getProperty(), personsInGroup.getPersonMail());
                update.setProperty(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty(), personsInGroup.getGroupKey());
                update.setProperty(DatastorePropertyNames.PersonsInGroup_status.getProperty(), "1");
                datastore.put(update);

                rm.setMessage(rm.Person_added_Accepted);
                return rm;
            }
        }
        return null;
    }


    @ApiMethod(name = "removePersonsInGroup", path = "removePersonsInGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages removePersonsInGroup(PersonsInGroup personsInGroup) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key requestKey = getRequestKey(personsInGroup);
        datastore.delete(requestKey);

        ResponseMessages rm = new ResponseMessages();
        rm.setMessage(rm.Person_removed);

        return rm;
    }


    @ApiMethod(name = "showJoinedGroups", path = "showJoinedGroupsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> showJoinedGroups(@Named("usermail") String usermail) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query PersonsInGroupQuery = new Query(DatastoreKindNames.PersonsInGroup.getKind());
        PersonsInGroupQuery.addProjection(new PropertyProjection(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty(), String.class));

        Filter keyFilter = new FilterPredicate(DatastorePropertyNames.PersonsInGroup_personMail.getProperty(), FilterOperator.EQUAL, usermail);
        PersonsInGroupQuery.setFilter(keyFilter);

        PreparedQuery queryResult = datastore.prepare(PersonsInGroupQuery);


        ArrayList<Search> groupList = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            String keydata = result.getProperty(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty()).toString();
            Key key = KeyFactory.stringToKey(keydata);

            infoCollectorEndpoint getgrpinfo = new infoCollectorEndpoint();
            infoCollector info = getgrpinfo.getinfo(keydata);

            Groups grpinfo = info.getGroupdata();

            Search s = new Search();

            s.setKey(key);
            s.setPicData(grpinfo.getGroupPic());
            s.setData(grpinfo.getGroupName());
            s.setLatitude(grpinfo.getLatitude());
            s.setLongitude(grpinfo.getLongitude());
            s.setLocation(grpinfo.getLocation());
            s.setExtra(grpinfo.getGroupDescription());
            s.setExtra1(grpinfo.getPermission());
            s.setExtra2(grpinfo.getCreatorMail());

            groupList.add(s);
        }
        return groupList;
    }

    @ApiMethod(name = "getRequestKey", path = "getRequestKeyPath", httpMethod = ApiMethod.HttpMethod.POST)
    public Key getRequestKey(PersonsInGroup personsInGroup) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query q = new Query(DatastoreKindNames.PersonsInGroup.getKind());
        Filter groupKeyFilter = new FilterPredicate(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty(), FilterOperator.EQUAL, personsInGroup.getGroupKey());
        Filter personMailFilter = new FilterPredicate(DatastorePropertyNames.PersonsInGroup_personMail.getProperty(), FilterOperator.EQUAL, personsInGroup.getPersonMail());
        Filter composite = CompositeFilterOperator.and(groupKeyFilter, personMailFilter);
        q.setFilter(composite);

        PreparedQuery pq = datastore.prepare(q);

        Key requestKey = null;

        for (Entity result : pq.asIterable()) {
            requestKey = result.getKey();
        }

        return requestKey;
    }


    @ApiMethod(name = "showRequests", path = "showRequestsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> showRequest(@Named("GroupKey") String groupKey) throws EntityNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query PersonsInGroupQuery = new Query(DatastoreKindNames.PersonsInGroup.getKind());
        PersonsInGroupQuery.addProjection(new PropertyProjection(DatastorePropertyNames.PersonsInGroup_personMail.getProperty(), String.class));

        Filter keyFilter = new FilterPredicate(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty(), FilterOperator.EQUAL, groupKey);
        Filter statusFilter = new FilterPredicate(DatastorePropertyNames.PersonsInGroup_status.getProperty(), FilterOperator.EQUAL, "0");

        Filter Request_Filter = Query.CompositeFilterOperator.and(keyFilter, statusFilter);
        PersonsInGroupQuery.setFilter(Request_Filter);

        PreparedQuery queryResult = datastore.prepare(PersonsInGroupQuery);


        ArrayList <Search> personList = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            String personMail = result.getProperty(DatastorePropertyNames.PersonsInGroup_personMail.getProperty()).toString();

            UserinfoEndpoint userinfoEndpoint = new UserinfoEndpoint();
            Key k = userinfoEndpoint.getKeyfromMail(personMail);

            Entity user = datastore.get(k);
            String username = user.getProperty(DatastorePropertyNames.Userinfo_Username.getProperty()).toString();
            String latitude = user.getProperty(DatastorePropertyNames.Userinfo_latitude.getProperty()).toString();
            String longitude = user.getProperty(DatastorePropertyNames.Userinfo_longitude.getProperty()).toString();
            String location = user.getProperty(DatastorePropertyNames.Userinfo_location.getProperty()).toString();

            Text picText = (Text) user.getProperty(DatastorePropertyNames.Userinfo_Profilepic.getProperty());
            String picData = picText.getValue();

            Key gk = KeyFactory.stringToKey(groupKey);

            Entity groupInfo = datastore.get(gk);
            String GroupName = groupInfo.getProperty(DatastorePropertyNames.Groups_groupname.getProperty()).toString();

            Search s = new Search();
            s.setData(username);
            s.setKey(gk);
            s.setLongitude(longitude);
            s.setLatitude(latitude);
            s.setLocation(location);
            s.setPicData(picData);
            s.setExtra(personMail);
            s.setExtra1(GroupName);

            personList.add(s);

        }
        return personList;
    }
}