package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapit.backend.locationServiceApi.model.LocationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import android.os.Handler;
import android.widget.Toast;

/**
 * Created by SETU on 5/20/2015.
 */
public class Friend_Tracking extends Fragment {

    Bundle data;
    String mail;
    MapFragment mapFrag;
    TextView online, lastseen, tv_last_seen;
    GoogleMap map;
    Double lat, lng;
    RelativeLayout rel;
    Runnable trackerRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.tracking_friend, null, false);

        online = (TextView) v.findViewById(R.id.tvOnline);
        lastseen = (TextView) v.findViewById(R.id.tvLastSeen);
        tv_last_seen = (TextView) v.findViewById(R.id.textView2);
        rel = (RelativeLayout) v.findViewById(R.id.reltrack);

        mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.tracking_map);
        map = mapFrag.getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setTrafficEnabled(true);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        rel.startAnimation(animation);


        data = getArguments();
        mail = data.getString("mail");

       /* while (true) {
            checkForPosition(mail);
            return v;
        }*/
       /*while(true) {
           new Thread(new Runnable() {
               @Override
               public void run() {

                   checkForPosition(mail);
                   Log.i("tracking","i am in thread");

               }



           }).start();*/
        final Handler trackerHandler;
        trackerHandler = new Handler();
        trackerHandler.postDelayed(trackerRunnable, 1000);

        trackerRunnable = new Runnable() {
            @Override
            public void run() {
                checkForPosition(mail);
                trackerHandler.postDelayed(trackerRunnable, 1000);
            }
        };

        return v;

    }


    private void checkForPosition(final String mail) {
        if (map != null) {
            map.clear();
        }
        Log.i("tracking", "i am in while loop");
        String usermail = mail;
        LocationService ls = new LocationService();
        final BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.red_marker);
        Data d = new Data();
        d.setUsermail(usermail);
        d.setCommand(Commands.locService_getInfo.getCommand());

        try {
            new locServiceEndpointCommunicator() {
                @Override
                protected void onPostExecute(LocationService fetchedData) {
                    String state = "0";
                    String last_time = "Unknown";
                    try {
                        state = fetchedData.getStatus();
                        DateConverter dc = new DateConverter();
                        ArrayList<String> formatted = dc.MobileFriendly(fetchedData.getDate());
                        last_time = formatted.get(0) + " " + formatted.get(1);
                        lat = Double.parseDouble(fetchedData.getLatitude());
                        lng = Double.parseDouble(fetchedData.getLongitude());
                        if (state.equals("1")) {
                            tv_last_seen.setVisibility(View.GONE);
                            lastseen.setVisibility(View.GONE);
                            online.setText("Online");
                            online.setTextColor(Color.GREEN);
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng));
                            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
                            marker.icon(icon);
                            if (map == null) {
                                Log.i("tracking", "map is null");
                                map = mapFrag.getMap();
                            }
                            map.addMarker(marker);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                        } else {
                            online.setText("Offline");
                            online.setTextColor(Color.WHITE);
                            lastseen.setTextColor(Color.WHITE);
                            lastseen.setText(last_time);

                            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng));
                            //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
                            marker.icon(icon);
                            if (map == null) {
                                Log.i("tracking", "map is null");
                                map = mapFrag.getMap();
                            }
                            map.addMarker(marker);


                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));

                        }

                    } catch (Exception e) {
                        online.setText("Offline");
                        online.setTextColor(Color.WHITE);
                        tv_last_seen.setVisibility(View.GONE);
                        lastseen.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Internet Connection Problem", Toast.LENGTH_LONG).show();
                    }
                }
            }


                    .execute(new Pair<Data, LocationService>(d, ls));


        }catch (Exception e){
            online.setText("Offline");
            online.setTextColor(Color.WHITE);
            tv_last_seen.setVisibility(View.GONE);
            lastseen.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Internet Connection Problem", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        Log.i("ss", "i am here");
        checkForPosition(mail);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.tracking_map));
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null)
            map = null;
    }

}
