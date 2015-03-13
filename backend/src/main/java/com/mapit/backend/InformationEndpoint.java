package com.mapit.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.mapit.backend.Properties_and_Values.DatastorePropertyNames;
import com.mapit.backend.Properties_and_Values.Values;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.sound.midi.MidiDevice;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "informationApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.mapit.com",
                ownerName = "backend.mapit.com",
                packagePath = ""
        )
)
public class InformationEndpoint {

    @ApiMethod(name = "setInformation", path = "setInformationPath", httpMethod = ApiMethod.HttpMethod.POST)
    public void setInformation(Information data) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity infoKind = new Entity(data.getKindName());

        infoKind.setProperty(DatastorePropertyNames.Information_name.getProperty(), data.getInfoName());
        infoKind.setProperty(DatastorePropertyNames.Information_description.getProperty(), data.getInfoDescription());
        infoKind.setProperty(DatastorePropertyNames.Information_latitude.getProperty(), data.getLatitude());
        infoKind.setProperty(DatastorePropertyNames.Information_longitude.getProperty(), data.getLongitude());
        infoKind.setProperty(DatastorePropertyNames.Information_location.getProperty(), data.getLocation());

        if(data.getInformationPic()!=null) {
            Text infoPhoto = new Text(data.getInformationPic());
            infoKind.setUnindexedProperty(DatastorePropertyNames.Information_infoPic.getProperty(), infoPhoto);
        }
        else{
            Text infoPhoto = new Text("");
            infoKind.setUnindexedProperty(DatastorePropertyNames.Information_infoPic.getProperty(), infoPhoto);
        }

        if(data.getDetailInfopic()!=null) {
            Text infoPhoto = new Text(data.getDetailInfopic());
            infoKind.setUnindexedProperty(DatastorePropertyNames.Information_detailpic.getProperty(), infoPhoto);
        }
        else{
            Text infoPhoto = new Text("");
            infoKind.setUnindexedProperty(DatastorePropertyNames.Information_detailpic.getProperty(), infoPhoto);
        }

        datastore.put(infoKind);
    }


    @ApiMethod(name = "getInformation", path = "getInformationPath", httpMethod = ApiMethod.HttpMethod.POST)
    public ArrayList <Information> getInformation(@Named("Category") String Category) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        ArrayList <String> Categories = new ArrayList<>();

        if(Category.equals(DatastorePropertyNames.Information_All.getProperty()))
        {
            Categories.add(Values.Information_Food.getValue());
            Categories.add(Values.Information_Education.getValue());
            Categories.add(Values.Information_Accomodation.getValue());
            Categories.add(Values.Information_Market.getValue());
            Categories.add(Values.Information_Religion.getValue());
            Categories.add(Values.Information_Transport.getValue());
            Categories.add(Values.Information_tourism.getValue());
        }
        else
        {
            Categories.add(Category);
        }

        ArrayList<Information> informationList = new ArrayList<>();

        for(int i = 0 ; i < Categories.size() ; i++) {
            Query statusQuery = new Query(Categories.get(i));

            PreparedQuery queryResult = datastore.prepare(statusQuery);

            for (Entity result : queryResult.asIterable()) {
                Information info = new Information();

                info.setInfoName(result.getProperty(DatastorePropertyNames.Information_name.getProperty()).toString());
                info.setInfoDescription(result.getProperty(DatastorePropertyNames.Information_description.getProperty()).toString());
                info.setLatitude(result.getProperty(DatastorePropertyNames.Information_latitude.getProperty()).toString());
                info.setLongitude(result.getProperty(DatastorePropertyNames.Information_longitude.getProperty()).toString());

                Text imageText = (Text) result.getProperty(DatastorePropertyNames.Information_infoPic.getProperty());
                String ImageData = imageText.getValue();
                if (ImageData.length() > 0)
                    info.setInformationPic(ImageData);


                Text detailImageText = (Text) result.getProperty(DatastorePropertyNames.Information_detailpic.getProperty());
                String detailImageTextData = detailImageText.getValue();
                if (detailImageTextData.length() > 0)
                    info.setDetailInfopic(detailImageTextData);


                informationList.add(info);
            }

        }

        return informationList;
    }
}