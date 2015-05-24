package com.example.MAPit.MAPit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
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


/**
 * Created by SETU on 5/2/2015.
 */
public class LocBuzzService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Double passed_lat,passed_lng,current_lat,current_lng;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static final String TAG = "HelloService";
    private boolean isRunning = false;
    public Vibrator vibrator;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(LocBuzzService.this)
                .addOnConnectionFailedListener(LocBuzzService.this)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle b=intent.getExtras();
                     passed_lat = b.getDouble("lat");
                     passed_lng = b.getDouble("lng");
                    Log.i(TAG,"Passed Data:" + String.valueOf(passed_lat)+","+String.valueOf(passed_lng));
                    Thread.sleep(5000);
                    while(true){
                         Log.i(TAG,"I am in while loop");

                         boolean ok=checkForLocation(passed_lat,passed_lng,current_lat,current_lng);
                         if(ok==true){
                             Log.i(TAG,"vibrator");
                             vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                             vibrator.vibrate(10000);
                             break;
                         }
                        Thread.sleep(60000);
                    }

                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));

                }

                stopSelf();
            }
        }).start();


        return Service.START_STICKY;
    }

    private boolean checkForLocation(Double passed_lat, Double passed_lng, Double current_lat, Double current_lng) {

        Log.i(TAG,"I am in checkforLoc");
        Location locationA = new Location("point A");
        locationA.setLatitude(passed_lat);
        locationA.setLongitude(passed_lng);
        Location locationB = new Location("point B");
        locationB.setLatitude(current_lat);
        locationB.setLongitude(current_lng);
        int distance = (int) locationA.distanceTo(locationB);
        if (distance / 1000 <= .30)
            return true;
        else
            return false;

    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i(TAG, "Service onDestroy");
        isRunning = false;
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {






        //replace this portion anywhere to update location of user.

        //String usermail = "USER MAIL GOES HERE";

        //information needed to be saved via Locationservice Class
        /*LocationService ls = new LocationService();
        ls.setStatus("1"); //one or zero whichever you prefer;
        ls.setLongitude("12.1123123");
        ls.setLatitude("13.222");
        ls.setMail(usermail);

        Data d = new Data();
        d.setUsermail(usermail);
        d.setCommand(Commands.locService_setInfo.getCommand());
        new locServiceEndpointCommunicator().execute(new Pair<Data,LocationService> (d,ls));
        */








        /*
        //replace this portion anywhere to get the location of any specific user.

        String usermail = "USER MAIL GOES HERE";
        LocationService ls = new LocationService();


        Data d = new Data();
        d.setUsermail(usermail);
        d.setCommand(Commands.locService_getInfo.getCommand());

        new locServiceEndpointCommunicator(){
            @Override
            protected void onPostExecute(LocationService fetchedData){

                //these three information you get
                fetchedData.getLatitude();
                fetchedData.getLongitude();
                fetchedData.getStatus();

            }
        }
        .execute(new Pair<Data,LocationService> (d,ls));

         */



        Log.i(TAG, "Onconnected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(120000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest,new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        current_lat = location.getLatitude();
                        current_lng = location.getLongitude();

                       //for testing purpose
                       /* String usermail = "USER MAIL GOES HERE";
                        LocationService ls = new LocationService();
                        ls.setStatus("1"); //one or zero whichever you prefer;
                        ls.setLongitude(String.valueOf(current_lng));
                        ls.setLatitude(String.valueOf(current_lng));
                        ls.setMail(usermail);

                        Data d = new Data();
                        d.setUsermail(usermail);
                        d.setCommand(Commands.locService_setInfo.getCommand());
                        new locServiceEndpointCommunicator().execute(new Pair<Data,LocationService> (d,ls));*/


                        Log.i(TAG,"Current Data:" + String.valueOf(current_lat)+","+String.valueOf(current_lng));
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
        Double lat1 = location.getLatitude();
        Double lng1 = location.getLongitude();
        Log.i(TAG,String.valueOf(lat1)+ ",  " + String.valueOf(lng1));

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
