package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "gcmregistrationApi",
        version = "v1",
        resource = "gcmregistration",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class GCMregistrationEndpoint {

    @ApiMethod(name = "insertGCMregistration", path = "insertGCMregistrationPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void insertGCMregistration(GCMregistration regData) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity e = new Entity(DatastoreKindNames.GCMdata.getKind());
        e.setProperty(DatastorePropertyNames.Userinfo_Mail.getProperty(), regData.getMail());
        e.setProperty(DatastorePropertyNames.GCMdata_id.getProperty(), regData.getRegID());
        datastore.put(e);
    }

    @ApiMethod(name = "getGCMregistration", path = "getGCMregistrationPath", httpMethod = ApiMethod.HttpMethod.POST)
    public GCMregistration getGCMregistration(@Named("mail") String mail) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query.Filter gcmRegMailFilter = new Query.FilterPredicate(DatastorePropertyNames.Userinfo_Mail.getProperty(), Query.FilterOperator.EQUAL, mail);
        Query gcmRegQuery = new Query(DatastoreKindNames.GCMdata.getKind()).setFilter(gcmRegMailFilter);

        PreparedQuery queryResult = datastore.prepare(gcmRegQuery);
        String registrationID = null;
        for (Entity result : queryResult.asIterable()) {
            registrationID = result.getProperty(DatastorePropertyNames.GCMdata_id.getProperty()).toString();
        }

        GCMregistration gcmResult = new GCMregistration();
        gcmResult.setMail(mail);
        gcmResult.setRegID(registrationID);

        return gcmResult;
    }
}