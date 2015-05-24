package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapit.backend.locationServiceApi.model.LocationService;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tracking_friend, null, false);

        online = (TextView) v.findViewById(R.id.tvOnline);
        lastseen = (TextView) v.findViewById(R.id.tvLastSeen);
        tv_last_seen = (TextView) v.findViewById(R.id.textView2);
        rel=(RelativeLayout) v.findViewById(R.id.reltrack);

        mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.tracking_map);
        map = mapFrag.getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setTrafficEnabled(true);

        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_right);
        rel.startAnimation(animation);


        data = getArguments();
        mail = data.getString("mail");



        while (true) {

            checkForPosition(mail);
            return v;

        }
    }

    private void checkForPosition(String mail) {

        String usermail = mail;
        LocationService ls = new LocationService();

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
                        last_time = fetchedData.getDate();
                        lat = Double.parseDouble(fetchedData.getLatitude());
                        lng = Double.parseDouble(fetchedData.getLongitude());
                        if (state.equals("1")) {
                            tv_last_seen.setVisibility(View.GONE);
                            lastseen.setVisibility(View.GONE);
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng));
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
                            map.addMarker(marker);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
                        } else {
                            online.setText("Offline");
                            online.setTextColor(Color.WHITE);
                            lastseen.setTextColor(Color.WHITE);
                            lastseen.setText(last_time);
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng));
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));
                            map.addMarker(marker);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));

                        }
                    } catch (Exception e) {
                        online.setText("Offline");
                        online.setTextColor(Color.WHITE);
                        tv_last_seen.setVisibility(View.GONE);
                        lastseen.setVisibility(View.GONE);

                    }


                }
            }
                    .execute(new Pair<Data, LocationService>(d, ls));
        } catch (Exception e) {
            online.setText("Offline");
            tv_last_seen.setVisibility(View.GONE);
            lastseen.setVisibility(View.GONE);
        }

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
