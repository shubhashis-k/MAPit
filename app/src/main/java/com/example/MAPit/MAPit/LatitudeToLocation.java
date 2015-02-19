package com.example.MAPit.MAPit;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by SETU on 2/17/2015.
 */
public class LatitudeToLocation extends SlidingDrawerActivity{

   public String GetLocation(String latitude,String longitude) throws IOException {
        String loc="";
        Double lat=Double.parseDouble(latitude);
        Double lng=Double.parseDouble(longitude);
        Geocoder gcd = new Geocoder(this,Locale.getDefault());

         List<Address> addressList = gcd.getFromLocation(22.819511,89.549999,1);
         if (addressList.size() > 0)
          loc = addressList.get(0).getLocality();

        return loc;
    }

}