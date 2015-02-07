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
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "infoCollectorApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class infoCollectorEndpoint {


    @ApiMethod(name = "getinfo", path = "getinfoPath", httpMethod = ApiMethod.HttpMethod.POST)
    public infoCollector getinfo(@Named("StringKey") String StringKey) throws EntityNotFoundException{
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Key k = KeyFactory.stringToKey(StringKey);

        Entity data = datastore.get(k);
        String kind = data.getKind();

        infoCollector info = new infoCollector();

        if(kind.equals(DatastoreKindNames.Userinfo.getKind())){
            UserinfoModel userdata = new UserinfoModel();
            userdata.setMail(data.getProperty(DatastorePropertyNames.Userinfo_Mail.getProperty()).toString());

            info.setUserdata(userdata);
        }

        return info;
    }

}