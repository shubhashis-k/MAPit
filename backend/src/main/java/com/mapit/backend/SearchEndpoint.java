package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.mapit.backend.Properties_and_Values.DatastoreKindNames;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;

import java.util.ArrayList;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "searchQueriesApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class SearchEndpoint {


    @ApiMethod(name = "getUserData", path = "getUserDataPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getUserData() {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query usernameQuery = new Query(DatastoreKindNames.Userinfo.getKind());
        usernameQuery.addProjection(new PropertyProjection(DatastorePropertyNames.Userinfo_Username.getProperty(), String.class));

        PreparedQuery queryResult = datastore.prepare(usernameQuery);


        ArrayList <Search> searchList = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            String data = result.getProperty(DatastorePropertyNames.Userinfo_Username.getProperty()).toString();
            Key key = result.getKey();

            Search s = new Search();

            s.setData(data);
            s.setKey(key);

            searchList.add(s);
        }
        return searchList;
    }

    @ApiMethod(name = "getGroupData", path = "getGroupDataPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getGroupData() {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query GroupnameQuery = new Query(DatastoreKindNames.Groups.getKind());
        GroupnameQuery.addProjection(new PropertyProjection(DatastorePropertyNames.Groups_groupname.getProperty(), String.class));

        PreparedQuery queryResult = datastore.prepare(GroupnameQuery);


        ArrayList <Search> searchList = new ArrayList<>();

        for (Entity result : queryResult.asIterable()) {
            String data = result.getProperty(DatastorePropertyNames.Groups_groupname.getProperty()).toString();
            Key key = result.getKey();

            Search s = new Search();

            s.setData(data);
            s.setKey(key);

            searchList.add(s);
        }
        return searchList;
    }


    @ApiMethod(name = "getResult", path = "getResultPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList<Search> getResult(@Named("KindName") String kindName, @Named("SearchKey") String SearchKey){
        ArrayList<Search> fullList = new ArrayList<>();

        if(kindName.equals(DatastoreKindNames.Userinfo.getKind()))
        {
            fullList = getUserData();
        }
        else if(kindName.equals(DatastoreKindNames.Groups.getKind()))
        {
            fullList = getGroupData();
        }

        ArrayList <Search> filteredList = new ArrayList();
        KMP ApplyKMP = new KMP();
        filteredList = ApplyKMP.FilterField(fullList, SearchKey);

        return filteredList;
    }
}