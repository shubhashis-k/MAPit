package com.example.MAPit.MAPit;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by SETU on 2/17/2015.
 */
public class LatitudeToLocation extends SlidingDrawerActivity {

    public String GetLocation(Double latitude,Double longitude) throws IOException {
        String loc="";
        Geocoder gcd = new Geocoder(this,Locale.getDefault());

         List<Address> addressList = gcd.getFromLocation(latitude,longitude,1);
         if (addressList.size() > 0)
          loc = addressList.get(0).getLocality();

        return loc;
    }

}
