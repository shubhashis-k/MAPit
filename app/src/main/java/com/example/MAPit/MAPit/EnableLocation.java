package com.example.MAPit.MAPit;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapit.backend.locationServiceApi.model.LocationService;

import java.util.Date;


/**
 * Created by SETU on 5/2/2015.
 */
public class EnableLocation extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Double current_lat,current_lng;
    String user_mail;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static final String TAG = "Tracking";


    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(EnableLocation.this)
                .addOnConnectionFailedListener(EnableLocation.this)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
        Bundle b=intent.getExtras();
        user_mail = b.getString("usermail");

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i(TAG, "Service onDestroy");

        LocationService ls = new LocationService();
        ls.setStatus("0");
        ls.setLongitude(String.valueOf(current_lng));
        ls.setLatitude(String.valueOf(current_lat));
        ls.setMail(user_mail);

        Date now = new Date();
        DateConverter dc = new DateConverter();
        String stringDate = dc.DateToString(now);

        ls.setDate(stringDate);

        Data d = new Data();
        d.setUsermail(user_mail);
        d.setCommand(Commands.locService_setInfo.getCommand());
        try {
            new locServiceEndpointCommunicator().execute(new Pair<Data, LocationService>(d, ls));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
        }

        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i(TAG, "Onconnected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(60000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest,new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(final Location location) {

                        Data d = new Data();
                        d.setUsermail(user_mail);
                        d.setCommand(Commands.locService_getInfo.getCommand());
                        LocationService dummy = new LocationService();
                        try {
                            new locServiceEndpointCommunicator(){
                                @Override
                                protected void onPostExecute(LocationService ls) {
                                    super.onPostExecute(ls);

                                    current_lat = location.getLatitude();
                                    current_lng = location.getLongitude();

                                    //for testing purpose
                                    ls.setStatus("1");
                                    ls.setLongitude(String.valueOf(current_lng));
                                    ls.setLatitude(String.valueOf(current_lat));
                                    ls.setMail(user_mail);

                                    Date now = new Date();
                                    DateConverter dc = new DateConverter();
                                    String stringDate = dc.DateToString(now);

                                    ls.setDate(stringDate);

                                    Data d = new Data();
                                    d.setUsermail(user_mail);
                                    d.setCommand(Commands.locService_setInfo.getCommand());
                                    try {
                                        new locServiceEndpointCommunicator().execute(new Pair<Data, LocationService>(d, ls));
                                    }catch (Exception e){
                                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }.execute(new Pair<Data, LocationService>(d, dummy));
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "LocationChanged");

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}