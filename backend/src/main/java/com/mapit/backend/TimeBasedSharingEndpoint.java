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
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "timeBasedSharingApi",
        version = "v1",
        resource = "timeBasedSharing",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class TimeBasedSharingEndpoint {

    @ApiMethod(name = "getTimeBasedSharing", path = "getTimeBasedSharingpath", httpMethod = ApiMethod.HttpMethod.POST)
    public TimeBasedSharing getTimeBasedSharing(TimeBasedSharing timeBasedSharing) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter mailFilter = new Query.FilterPredicate(DatastorePropertyNames.timeSharing_mail.getProperty(), Query.FilterOperator.EQUAL, timeBasedSharing.getMail());
        Query.Filter startTimeFilter = new Query.FilterPredicate(DatastorePropertyNames.timeSharing_startTime.getProperty(), Query.FilterOperator.EQUAL, timeBasedSharing.getStartTime());
        Query.Filter endTimeFilter = new Query.FilterPredicate(DatastorePropertyNames.timeSharing_endTime.getProperty(), Query.FilterOperator.EQUAL, timeBasedSharing.getEndTime());

        Query.CompositeFilter startendFilter = Query.CompositeFilterOperator.and(mailFilter,startTimeFilter,endTimeFilter);
        Query timeQuery = new Query(DatastoreKindNames.timeBasedSharing.getKind()).setFilter(startendFilter);

        PreparedQuery queryResult = datastore.prepare(timeQuery);

        TimeBasedSharing timeInfo = new TimeBasedSharing();

        for (Entity result : queryResult.asIterable()) {
            timeInfo.setKey(KeyFactory.keyToString(result.getKey()));

            timeInfo.setMail(timeBasedSharing.getMail());
            timeInfo.setStartTime((String) result.getProperty(DatastorePropertyNames.timeSharing_startTime.getProperty()));
            timeInfo.setEndTime((String) result.getProperty(DatastorePropertyNames.timeSharing_endTime.getProperty()));
            timeInfo.setCategory((String) result.getProperty(DatastorePropertyNames.timeSharing_category.getProperty()));

        }

        return timeInfo;
    }

    @ApiMethod(name = "getAllTimeBasedSharing", path = "getAllTimeBasedSharingpath", httpMethod = ApiMethod.HttpMethod.GET)
    public ArrayList<TimeBasedSharing> getAllTimeBasedSharing(@Named("Mail") String mail) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter mailFilter = new Query.FilterPredicate(DatastorePropertyNames.timeSharing_mail.getProperty(), Query.FilterOperator.EQUAL, mail);

        Query timeQuery = new Query(DatastoreKindNames.timeBasedSharing.getKind()).setFilter(mailFilter);

        PreparedQuery queryResult = datastore.prepare(timeQuery);


        ArrayList <TimeBasedSharing> timeInfo = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {

            TimeBasedSharing tb = new TimeBasedSharing();

            tb.setKey(KeyFactory.keyToString(result.getKey()));

            tb.setMail(mail);
            tb.setStartTime((String) result.getProperty(DatastorePropertyNames.timeSharing_startTime.getProperty()));
            tb.setEndTime((String) result.getProperty(DatastorePropertyNames.timeSharing_endTime.getProperty()));
            tb.setCategory((String) result.getProperty(DatastorePropertyNames.timeSharing_category.getProperty()));

            timeInfo.add(tb);

        }

        return timeInfo;
    }


    @ApiMethod(name = "getCategoryFromTimeBasedSharing", path = "getCategoryFromTimeBasedSharingpath", httpMethod = ApiMethod.HttpMethod.POST)
    public TimeBasedSharing getCategoryFromTimeBasedSharing(TimeBasedSharing timeBasedSharing) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter mailFilter = new Query.FilterPredicate(DatastorePropertyNames.timeSharing_mail.getProperty(), Query.FilterOperator.EQUAL, timeBasedSharing.getMail());

        Query timeQuery = new Query(DatastoreKindNames.timeBasedSharing.getKind()).setFilter(mailFilter);

        PreparedQuery queryResult = datastore.prepare(timeQuery);

        ArrayList<TimeBasedSharing> AllTimeInfo = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            TimeBasedSharing timeInfo = new TimeBasedSharing();

            timeInfo.setKey(KeyFactory.keyToString(result.getKey()));

            timeInfo.setMail(timeBasedSharing.getMail());
            timeInfo.setStartTime((String) result.getProperty(DatastorePropertyNames.timeSharing_startTime.getProperty()));
            timeInfo.setEndTime((String) result.getProperty(DatastorePropertyNames.timeSharing_endTime.getProperty()));
            timeInfo.setCategory((String) result.getProperty(DatastorePropertyNames.timeSharing_category.getProperty()));

            AllTimeInfo.add(timeInfo);

        }

        TimeBasedSharing result = new TimeBasedSharing();

        for (int i = 0; i < AllTimeInfo.size(); i++){
            TimeBasedSharing t = AllTimeInfo.get(i);

            Double requestedTime = Double.parseDouble(timeBasedSharing.getStartTime());
            Double startTime = Double.parseDouble(t.getStartTime());
            Double endTime = Double.parseDouble(t.getEndTime());
            if(requestedTime >= startTime && requestedTime <= endTime)
            {
                result = t;
                break;
            }
        }

        return result;
    }


    @ApiMethod(name = "insertTimeBasedSharing", path = "insertTimeBasedSharingPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void insertTimeBasedSharing(TimeBasedSharing timeBasedSharing) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        TimeBasedSharing ts = getTimeBasedSharing(timeBasedSharing);

        if(ts.getMail() != null) {
            Key key = KeyFactory.stringToKey(ts.getKey());
            datastore.delete(key);

            Entity e = new Entity(DatastoreKindNames.timeBasedSharing.getKind());
            e.setProperty(DatastorePropertyNames.timeSharing_mail.getProperty(), timeBasedSharing.getMail());
            e.setProperty(DatastorePropertyNames.timeSharing_startTime.getProperty(), timeBasedSharing.getStartTime());
            e.setProperty(DatastorePropertyNames.timeSharing_endTime.getProperty(), timeBasedSharing.getEndTime());
            e.setProperty(DatastorePropertyNames.timeSharing_category.getProperty(), timeBasedSharing.getCategory());

            datastore.put(e);
        }
        else
        {
            Entity e = new Entity(DatastoreKindNames.timeBasedSharing.getKind());
            e.setProperty(DatastorePropertyNames.timeSharing_mail.getProperty(), timeBasedSharing.getMail());
            e.setProperty(DatastorePropertyNames.timeSharing_startTime.getProperty(), timeBasedSharing.getStartTime());
            e.setProperty(DatastorePropertyNames.timeSharing_endTime.getProperty(), timeBasedSharing.getEndTime());
            e.setProperty(DatastorePropertyNames.timeSharing_category.getProperty(), timeBasedSharing.getCategory());
            datastore.put(e);
        }

    }
}