package com.example.MAPit.MAPit;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by SETU on 2/17/2015.
 */
public class LatitudeToLocation {
    public Context context;
    public LatitudeToLocation(Context c){
        context=c;
    }
    public String GetLocation(Double latitude,Double longitude) throws IOException {
        String loc="";
        Geocoder gcd = new Geocoder(context,Locale.getDefault());

         List<Address> addressList = gcd.getFromLocation(latitude,longitude,1);
         if (addressList.size() > 0) {
             loc = addressList.get(0).getLocality();
             if(loc == null){
                 loc=addressList.get(0).getAdminArea();
                 if(loc == null){
                     loc=addressList.get(0).getCountryName();
                 }
             }

         }

        return loc;
    }

}
