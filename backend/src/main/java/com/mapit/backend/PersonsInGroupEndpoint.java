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


    @ApiMethod(name = "showPersonsInGroup", path = "showPersonsInGroupPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<PersonsInGroup> showPersonsInGroup(PersonsInGroup personsInGroup) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query PersonsInGroupQuery = new Query(DatastoreKindNames.PersonsInGroup.getKind());
        PersonsInGroupQuery.addProjection(new PropertyProjection(DatastorePropertyNames.PersonsInGroup_personMail.getProperty(), String.class));

        Filter keyFilter = new FilterPredicate(DatastorePropertyNames.PersonsInGroup_groupKey.getProperty(), FilterOperator.EQUAL, personsInGroup.getGroupKey());
        PersonsInGroupQuery.setFilter(keyFilter);

        PreparedQuery queryResult = datastore.prepare(PersonsInGroupQuery);


        ArrayList<PersonsInGroup> personList = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            String data = result.getProperty(DatastorePropertyNames.PersonsInGroup_personMail.getProperty()).toString();

            PersonsInGroup p = new PersonsInGroup();
            p.setPersonMail(data);

            personList.add(p);
        }
        return personList;
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
}