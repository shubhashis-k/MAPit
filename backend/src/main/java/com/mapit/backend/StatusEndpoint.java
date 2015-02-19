package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.datastore.Text;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "statusApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class StatusEndpoint {


    @ApiMethod(name = "addStatus", path = "addStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void addStatus(StatusData status) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity e = new Entity(status.getKind());

        if(status.getKind().equals(DatastoreKindNames.StatusInGroup.getKind())){
            e.setProperty(DatastorePropertyNames.Status_groupKey.getProperty(), status.getGroupKey());
        }

        e.setProperty(DatastorePropertyNames.Status_personMail.getProperty(), status.getPersonMail());
        e.setProperty(DatastorePropertyNames.Status_latitude.getProperty(), status.getLatitude());
        e.setProperty(DatastorePropertyNames.Status_longitude.getProperty(), status.getLongitude());
        e.setProperty(DatastorePropertyNames.Status_text.getProperty(), status.getStatus());

        Date now = new Date();
        e.setProperty(DatastorePropertyNames.Status_time.getProperty(), now);

        if(status.getStatusPhoto()!=null) {
            Text statusPhoto = new Text(status.getStatusPhoto());
            e.setUnindexedProperty(DatastorePropertyNames.Status_image.getProperty(), statusPhoto);
        }
        else
        {
            Text statusPhoto = new Text("");
            e.setUnindexedProperty(DatastorePropertyNames.Status_image.getProperty(), statusPhoto);
        }

            datastore.put(e);

    }


    @ApiMethod(name = "removeStatus", path = "removeStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void removeStatus(@Named("statusKey") String statusKey) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key k = KeyFactory.stringToKey(statusKey);
        datastore.delete(k);

    }

    @ApiMethod(name = "showStatus", path = "showStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList <StatusData> showStatus(StatusData status) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query statusQuery = new Query(status.getKind()).addSort(DatastorePropertyNames.Status_time.getProperty(), SortDirection.DESCENDING);

        if(status.getKind().equals(DatastoreKindNames.StatusInGroup.getKind())) {
            Filter groupKeyFilter = new FilterPredicate(DatastorePropertyNames.Status_groupKey.getProperty(), FilterOperator.EQUAL, status.getGroupKey());
            statusQuery.setFilter(groupKeyFilter);
        }
        else if(status.getKind().equals(DatastoreKindNames.StatusbyIndividual.getKind())){
            Filter personMailFilter = new FilterPredicate(DatastorePropertyNames.Status_personMail.getProperty(), FilterOperator.EQUAL, status.getPersonMail());
            statusQuery.setFilter(personMailFilter);
        }


        PreparedQuery queryResult = datastore.prepare(statusQuery);
        ArrayList<StatusData> statusList = new ArrayList<>();

        if(status.getKind().equals(DatastoreKindNames.StatusInGroup.getKind())) {
            for (Entity result : queryResult.asIterable()) {
                StatusData s = new StatusData();
                Key k = result.getKey();
                s.setStatusKey(k);

                String personMail = result.getProperty(DatastorePropertyNames.Status_personMail.getProperty()).toString();
                s.setPersonMail(personMail);

                String personStatus = result.getProperty(DatastorePropertyNames.Status_text.getProperty()).toString();
                s.setStatus(personStatus);

                String latitude = result.getProperty(DatastorePropertyNames.Status_latitude.getProperty()).toString();
                s.setLatitude(latitude);

                String longitude = result.getProperty(DatastorePropertyNames.Status_longitude.getProperty()).toString();
                s.setLongitude(longitude);

                Date publishDate = (Date) result.getProperty(DatastorePropertyNames.Status_time.getProperty());
                s.setPublishDate(publishDate);

                Text imageText = (Text) result.getProperty(DatastorePropertyNames.Status_image.getProperty());
                String ImageData = imageText.getValue();
                if(ImageData.length() > 0)
                    s.setStatusPhoto(ImageData);

                statusList.add(s);


            }
        }
        else if(status.getKind().equals(DatastoreKindNames.StatusbyIndividual.getKind())){
            for (Entity result : queryResult.asIterable()) {
                StatusData s = new StatusData();
                Key k = result.getKey();
                s.setStatusKey(k);

                String personMail = result.getProperty(DatastorePropertyNames.Status_personMail.getProperty()).toString();
                s.setPersonMail(personMail);

                String personStatus = result.getProperty(DatastorePropertyNames.Status_text.getProperty()).toString();
                s.setStatus(personStatus);

                String latitude = result.getProperty(DatastorePropertyNames.Status_latitude.getProperty()).toString();
                s.setLatitude(latitude);

                String longitude = result.getProperty(DatastorePropertyNames.Status_longitude.getProperty()).toString();
                s.setLongitude(longitude);

                Text imageText = (Text) result.getProperty(DatastorePropertyNames.Status_image.getProperty());
                String ImageData = imageText.getValue();
                if(ImageData.length() > 0)
                    s.setStatusPhoto(ImageData);

                Date publishDate = (Date) result.getProperty(DatastorePropertyNames.Status_time.getProperty());
                s.setPublishDate(publishDate);

                statusList.add(s);

            }
        }


        return statusList;

    }

    @ApiMethod(name = "showLatestStatus", path = "showLatestStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public StatusData showLatestStatus(@Named("personMail") String personMail) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query statusQuery = new Query(DatastoreKindNames.StatusbyIndividual.getKind()).addSort(DatastorePropertyNames.Status_time.getProperty(), SortDirection.DESCENDING);

        Filter personMailFilter = new FilterPredicate(DatastorePropertyNames.Status_personMail.getProperty(), FilterOperator.EQUAL, personMail);
        statusQuery.setFilter(personMailFilter);


        PreparedQuery queryResult = datastore.prepare(statusQuery);

        StatusData latestStatus = new StatusData();
        for (Entity result : queryResult.asList(FetchOptions.Builder.withLimit(1))) {

                Key k = result.getKey();
                latestStatus.setStatusKey(k);

                /* fetch person Name*/
                UserinfoEndpoint userinfoEndpoint = new UserinfoEndpoint();
                Key personKey = userinfoEndpoint.getKeyfromMail(personMail);
                Entity personEntity = datastore.get(personKey);
                String personName = personEntity.getProperty(DatastorePropertyNames.Userinfo_Username.getProperty()).toString();



                latestStatus.setPersonName(personName);
                latestStatus.setPersonMail(personMail);

                String personStatus = result.getProperty(DatastorePropertyNames.Status_text.getProperty()).toString();
                latestStatus.setStatus(personStatus);

                String latitude = result.getProperty(DatastorePropertyNames.Status_latitude.getProperty()).toString();
                latestStatus.setLatitude(latitude);

                String longitude = result.getProperty(DatastorePropertyNames.Status_longitude.getProperty()).toString();
                latestStatus.setLongitude(longitude);

                Text imageText = (Text) result.getProperty(DatastorePropertyNames.Status_image.getProperty());
                String ImageData = imageText.getValue();
                if(ImageData.length() > 0)
                    latestStatus.setStatusPhoto(ImageData);

                Date publishDate = (Date) result.getProperty(DatastorePropertyNames.Status_time.getProperty());
                latestStatus.setPublishDate(publishDate);


            }
        return latestStatus;
        }

    @ApiMethod(name = "fetchFriendStatus", path = "fetchFriendStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList <StatusData> fetchFriendStatus(@Named("personMail") String personMail) throws EntityNotFoundException{
        FriendsEndpoint friendsEndpoint = new FriendsEndpoint();

        ArrayList <Search> friendList = friendsEndpoint.fetchFriendList(personMail, "1");

        ArrayList <StatusData> statusList = new ArrayList<>();
        for(int i = 0 ;i < friendList.size() ; i++){

            String friendMail = friendList.get(i).getData();
            StatusData s = showLatestStatus(friendMail);

            statusList.add(s);
        }

        return statusList;
    }
}

