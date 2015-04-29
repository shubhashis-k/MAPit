package com.example.MAPit.MAPit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by shubhashis on 2/25/2015.
 */
public class LocationFinder extends AsyncTask <LocationFinderData, Void, LocationFinderData> {
    private Context context;
    @Override
    protected LocationFinderData doInBackground(LocationFinderData... params) {
        LocationFinderData lfd = new LocationFinderData();
        lfd = params[0];
        context = lfd.getContext();
        Double Lat = lfd.getLatitude();
        Double Long = lfd.getLongitude();

        String loc="";
        LatitudeToLocation ll = new LatitudeToLocation(context);
        try {
            loc=ll.GetLocation(Lat,Long);
        } catch (IOException e) {
            Toast.makeText(context, "Internet Connection Error.", Toast.LENGTH_SHORT).show();
        }

        LocationFinderData returnData = new LocationFinderData();
        returnData.setLocation(loc);
        returnData.setIndex(lfd.getIndex());

        return returnData;
    }
}
