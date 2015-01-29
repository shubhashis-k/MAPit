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
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;
import java.util.logging.Logger;

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
    public void addStatus(Status status) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity e = new Entity(status.getKind());

        if(status.getKind().equals(DatastoreKindNames.StatusInGroup.getKind())){
            e.setProperty(DatastorePropertyNames.Status_groupKey.getProperty(), status.getGroupKey());
        }

        e.setProperty(DatastorePropertyNames.Status_personMail.getProperty(), status.getPersonMail());
        e.setProperty(DatastorePropertyNames.Status_latitude.getProperty(), status.getLatitude());
        e.setProperty(DatastorePropertyNames.Status_longitude.getProperty(), status.getLongitude());
        e.setProperty(DatastorePropertyNames.Status_text.getProperty(), status.getStatus());
        e.setProperty(DatastorePropertyNames.Status_time.getProperty(), status.getPublishDate());
        datastore.put(e);

    }


    @ApiMethod(name = "removeStatus", path = "removeStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void removeStatus(@Named("statusKey") String statusKey) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key k = KeyFactory.stringToKey(statusKey);
        datastore.delete(k);

    }

    @ApiMethod(name = "showStatus", path = "showStatusPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList <Status> showStatus(Status status) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query statusQuery = new Query(status.getKind());

        if(status.getKind().equals(DatastoreKindNames.StatusInGroup.getKind())) {
            statusQuery.addProjection(new PropertyProjection(DatastorePropertyNames.Status_personMail.getProperty(), String.class));
            statusQuery.addProjection(new PropertyProjection(DatastorePropertyNames.Status_text.getProperty(), String.class));
        }
        else if(status.getKind().equals(DatastoreKindNames.StatusbyIndividual.getKind())){
            statusQuery.addProjection(new PropertyProjection(DatastorePropertyNames.Status_text.getProperty(), String.class));
            Filter personMailFilter = new FilterPredicate(DatastorePropertyNames.Status_personMail.getProperty(), FilterOperator.EQUAL, status.getPersonMail());
            statusQuery.setFilter(personMailFilter);
        }


        PreparedQuery queryResult = datastore.prepare(statusQuery);
        ArrayList<Status> statusList = new ArrayList<>();

        if(status.getKind().equals(DatastoreKindNames.StatusInGroup.getKind())) {
            for (Entity result : queryResult.asIterable()) {
                Status s = new Status();
                Key k = result.getKey();
                s.setStatusKey(k);

                String personMail = result.getProperty(DatastorePropertyNames.Status_personMail.getProperty()).toString();
                s.setPersonMail(personMail);

                String personStatus = result.getProperty(DatastorePropertyNames.Status_text.getProperty()).toString();
                s.setStatus(personStatus);

                statusList.add(s);
            }
        }
        else if(status.getKind().equals(DatastoreKindNames.StatusbyIndividual.getKind())){
            for (Entity result : queryResult.asIterable()) {
                Status s = new Status();
                Key k = result.getKey();
                s.setStatusKey(k);

                s.setStatus(result.getProperty(DatastorePropertyNames.Status_text.getProperty()).toString());
                statusList.add(s);
            }
        }


        return statusList;

    }

}