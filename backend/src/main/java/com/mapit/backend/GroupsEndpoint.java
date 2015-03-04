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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.datastore.Text;
import com.mapit.backend.Properties_and_Values.Commands;
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
            groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_Permission.getProperty(), group.getPermission());
            groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_location.getProperty(), group.getLocation());

            if(group.getGroupPic() != null)
            {
                Text image_Data = new Text(group.getGroupPic());
                groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_Picture.getProperty(), image_Data);
            }
            else
            {
                Text image_Data = new Text("");
                groupKind.setUnindexedProperty(DatastorePropertyNames.Groups_Picture.getProperty(), image_Data);
            }

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
    public ArrayList<Search> getMyGroups(@Named("usermail") String mail) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter Creator_Filter = new Query.FilterPredicate(DatastorePropertyNames.Groups_creatormail.getProperty(), Query.FilterOperator.EQUAL, mail);

        Query Request_Query = new Query(DatastoreKindNames.Groups.getKind()).setFilter(Creator_Filter);

        PreparedQuery queryResult = datastore.prepare(Request_Query);

        Key GroupKey = null;

        ArrayList <Search> searchResult = new ArrayList<>();
        ArrayList <Search> JoinedGroupList = getJoinedGroups(mail);

        for (Entity result : queryResult.asIterable()) {

            GroupKey = result.getKey();

            Search s = getDetailedGroupInfo(GroupKey);
            searchResult.add(s);

        }

        searchResult.addAll(JoinedGroupList);

        return searchResult;
    }

    @ApiMethod(name = "getGroupsNotMine", path = "getGroupsNotMinePath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getGroupsNotMine(@Named("usermail") String mail, @Named("searchQuery") String searchQuery) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        SearchEndpoint getQueryResult = new SearchEndpoint();
        ArrayList <Search> QueryList = getQueryResult.getResult(DatastoreKindNames.Groups.getKind(), searchQuery);
        ArrayList <Search> myGroupList = getMyGroups(mail);
        ArrayList <Search> JoinedGroupList = getJoinedGroups(mail);
        ArrayList <Search> FilteredList = new ArrayList<>();
        for(int i = 0 ; i < QueryList.size(); i++){
            if(!myGroupList.contains(QueryList.get(i)) && !JoinedGroupList.contains(QueryList.get(i)))
            {
                Search s = QueryList.get(i);
                Key k = KeyFactory.stringToKey(s.getKey());

                s = getDetailedGroupInfo(k);
                FilteredList.add(s);
            }
        }

        return FilteredList;
    }

    @ApiMethod(name = "JoinOrLeaveGroup", path = "JoinOrLeaveGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ResponseMessages JoinOrLeaveGroup(@Named("usermail") String usermail, @Named("groupKey") String groupKey, @Named("requestType") String requestType) throws EntityNotFoundException {
        ResponseMessages rm = new ResponseMessages();
        PersonsInGroup personsInGroup = new PersonsInGroup();
        PersonsInGroupEndpoint personsInGroupEndpoint = new PersonsInGroupEndpoint();

        personsInGroup.setPersonMail(usermail);
        personsInGroup.setGroupKey(groupKey);

        if(requestType.equals(Commands.Request_Group.getCommand()) || requestType.equals(Commands.Accept_Group.getCommand()))
            rm = personsInGroupEndpoint.requestPersonsInGroup(personsInGroup, requestType);
        else if(requestType.equals(Commands.Leave_Group.getCommand()))
            rm = personsInGroupEndpoint.removePersonsInGroup(personsInGroup);
        return rm;
    }

    @ApiMethod(name = "getJoinedGroups", path = "getJoinedGroupsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getJoinedGroups(@Named("usermail") String usermail) throws EntityNotFoundException{
        PersonsInGroupEndpoint personsInGroupEndpoint = new PersonsInGroupEndpoint();
        ArrayList <Search> result = personsInGroupEndpoint.showJoinedGroups(usermail);

        return result;
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

    @ApiMethod(name = "getDetailedGroupInfo", path = "getDetailedGroupInfoPath", httpMethod = ApiMethod.HttpMethod.POST)
    public Search getDetailedGroupInfo(Key groupKey) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity GroupData = datastore.get(groupKey);

        String groupName = GroupData.getProperty(DatastorePropertyNames.Groups_groupname.getProperty()).toString();

        Search s = new Search();

        s.setKey(groupKey);
        s.setData(groupName);

        Text picText = (Text) GroupData.getProperty(DatastorePropertyNames.Groups_Picture.getProperty());
        String picData = picText.getValue();

        if(picData.length() > 0)
            s.setPicData(picData);

        s.setLatitude(GroupData.getProperty(DatastorePropertyNames.Groups_latitude.getProperty()).toString());
        s.setLongitude(GroupData.getProperty(DatastorePropertyNames.Groups_longitude.getProperty()).toString());
        s.setLocation(GroupData.getProperty(DatastorePropertyNames.Groups_location.getProperty()).toString());
        s.setExtra(GroupData.getProperty(DatastorePropertyNames.Groups_Description.getProperty()).toString());
        s.setExtra1(GroupData.getProperty(DatastorePropertyNames.Groups_Permission.getProperty()).toString());
        s.setExtra2(GroupData.getProperty(DatastorePropertyNames.Groups_creatormail.getProperty()).toString());

        return s;

    }

    @ApiMethod(name = "getRequeststoGroup", path = "getRequeststoGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getRequeststoGroup(@Named("usermail") String usermail) throws EntityNotFoundException {
        ArrayList <Search> MyGroups = getMyGroups(usermail);

        ArrayList <Search> personInfo = new ArrayList<>();
        for(int i = 0 ; i < MyGroups.size() ; i++){
            Search s = MyGroups.get(i);
            String groupKey = s.getKey();

            PersonsInGroupEndpoint personsInGroupEndpoint = new PersonsInGroupEndpoint();

            personInfo.addAll(personsInGroupEndpoint.showRequest(groupKey));
        }

        return personInfo;
    }

    @ApiMethod(name = "getAllGroups", path = "getAllGroupsPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getAllGroups(@Named("Mail") String mail) throws EntityNotFoundException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        ArrayList <Search> searchResult = new ArrayList<>();
        ArrayList <Search> MyGroups = getMyGroups(mail);

        Query Group_Query = new Query(DatastoreKindNames.Groups.getKind());

        PreparedQuery queryResult = datastore.prepare(Group_Query);

        for (Entity result : queryResult.asIterable()) {
            String groupName = result.getProperty(DatastorePropertyNames.Groups_groupname.getProperty()).toString();

            Key groupKey = result.getKey();
            Search s = new Search();

            s.setKey(groupKey);
            s.setData(groupName);

            Text picText = (Text) result.getProperty(DatastorePropertyNames.Groups_Picture.getProperty());
            String picData = picText.getValue();

            if(picData.length() > 0)
                s.setPicData(picData);

            s.setLatitude(result.getProperty(DatastorePropertyNames.Groups_latitude.getProperty()).toString());
            s.setLongitude(result.getProperty(DatastorePropertyNames.Groups_longitude.getProperty()).toString());
            s.setLocation(result.getProperty(DatastorePropertyNames.Groups_location.getProperty()).toString());
            s.setExtra(result.getProperty(DatastorePropertyNames.Groups_Description.getProperty()).toString());
            s.setExtra1(result.getProperty(DatastorePropertyNames.Groups_Permission.getProperty()).toString());
            s.setExtra2(result.getProperty(DatastorePropertyNames.Groups_creatormail.getProperty()).toString());

            if(!MyGroups.contains(s))
                searchResult.add(s);
        }


        return searchResult;
    }

}