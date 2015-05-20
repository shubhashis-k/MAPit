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
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "locationServiceApi",
        version = "v1",
        resource = "locationService",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class locationServiceEndpoint {

    @ApiMethod(name = "getlocationService", path = "getlocationServicePath")
    public locationService getlocationService(@Named("mail") String mail) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter mailFilter = new Query.FilterPredicate(DatastorePropertyNames.locService_mail.getProperty(), Query.FilterOperator.EQUAL, mail);

        Query GroupnameQuery = new Query(DatastoreKindNames.locService.getKind()).setFilter(mailFilter);
        PreparedQuery queryResult = datastore.prepare(GroupnameQuery);

        locationService locInfo = new locationService();

        for (Entity result : queryResult.asIterable()) {
            locInfo.setKey(KeyFactory.keyToString(result.getKey()));

            locInfo.setMail(mail);
            locInfo.setLatitude((String)result.getProperty(DatastorePropertyNames.locService_lat.getProperty()));
            locInfo.setLongitude((String)result.getProperty(DatastorePropertyNames.locService_long.getProperty()));
            locInfo.setStatus((String)result.getProperty(DatastorePropertyNames.locService_status.getProperty()));


            Date date = (Date)result.getProperty(DatastorePropertyNames.locService_date.getProperty());

            DateConverter dc = new DateConverter();
            locInfo.setDate(dc.DateToString(date));
        }

        return locInfo;
    }

    /**
     * This inserts a new <code>locationService</code> object.
     *
     * @param locationService The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertlocationService", path = "insertlocationServicePath")
    public void insertlocationService(locationService locationService) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        String mail = locationService.getMail();

        locationService fetchedLoc = getlocationService(mail);

        if(fetchedLoc.getMail() == null){
            Entity e = new Entity(DatastoreKindNames.locService.getKind());
            e.setProperty(DatastorePropertyNames.locService_mail.getProperty(), mail);
            e.setProperty(DatastorePropertyNames.locService_lat.getProperty(), locationService.getLatitude());
            e.setProperty(DatastorePropertyNames.locService_long.getProperty(), locationService.getLongitude());
            e.setProperty(DatastorePropertyNames.locService_status.getProperty(), locationService.getStatus());

            String stringDate = locationService.getDate();

            DateConverter dc = new DateConverter();
            Date date = dc.StringToDate(stringDate);
            e.setProperty(DatastorePropertyNames.locService_date.getProperty(), date);

            datastore.put(e);
        }
        else
        {
            String key = fetchedLoc.getKey();
            Key k = KeyFactory.stringToKey(key);

            datastore.delete(k);

            Entity e = new Entity(DatastoreKindNames.locService.getKind());
            e.setProperty(DatastorePropertyNames.locService_mail.getProperty(), mail);
            e.setProperty(DatastorePropertyNames.locService_lat.getProperty(), locationService.getLatitude());
            e.setProperty(DatastorePropertyNames.locService_long.getProperty(), locationService.getLongitude());
            e.setProperty(DatastorePropertyNames.locService_status.getProperty(), locationService.getStatus());

            String stringDate = locationService.getDate();

            DateConverter dc = new DateConverter();
            Date date = dc.StringToDate(stringDate);
            e.setProperty(DatastorePropertyNames.locService_date.getProperty(), date);

            datastore.put(e);
        }

    }
}