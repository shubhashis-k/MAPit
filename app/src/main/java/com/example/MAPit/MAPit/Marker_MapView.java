package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.mapit.backend.statusApi.model.StatusData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SETU on 2/17/2015.
 */
public class Marker_MapView extends Fragment {

    public Marker_MapView() {
        setHasOptionsMenu(true);
    }

    private Context context;
    private GoogleMap map;
    MapFragment mapFrag;
    ArrayList <StatusData> result;
    Bundle data;


    // public static View v;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /*if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null){
               parent.removeView(v);
                }
        }
        try {
             v = inflater.inflate(R.layout.home_map_activity, null, false);
        } catch (InflateException e) {

        }*/
        View v = inflater.inflate(R.layout.route_direction_frnd_location, null, false);

        mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.route_frnd_location_map);
        map = mapFrag.getMap();

        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setTrafficEnabled(true);
        //added the custom info adapter
        if (map != null) {
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {

                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    // creating my own info for latest frnd status
                    View v = getActivity().getLayoutInflater().inflate(R.layout.map_info_listview, null);
                    TextView tvFrndname = (TextView) v.findViewById(R.id.tv_frnd_name);
                    TextView tvFrndStatus = (TextView) v.findViewById(R.id.tv_frnd_status);
                    String status = marker.getTitle();
                    String actual_status = status.substring(0, status.indexOf('/'));
                    String email = status.substring(status.lastIndexOf('/') + 1);
                    String pos = status.substring(status.lastIndexOf('&')+1);
                    int position = Integer.parseInt(pos);
                    StatusData st = result.get(position);
                    data = new Bundle();
                    ArrayList <StatusData> passData = new ArrayList<StatusData>();
                    passData.add(st);
                    data.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Status.getCommand());
                    data.putSerializable(Commands.Arraylist_Values.getCommand(), passData);
                    tvFrndname.setText(actual_status);
                    tvFrndStatus.setText(marker.getSnippet());
                    return v;
                }
            });
        }


        //onclick listener on marker of friends location

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Fragment fragment = new Friends_Status_Comment_Fragment();
                fragment.setArguments(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //getting the bundle value

        Bundle data = getArguments();
         result = (ArrayList <StatusData>) data.getSerializable(Commands.Arraylist_Values.getCommand());

        drawMarkerAndLine(result);
        return v;
    }



    private void drawMarkerAndLine(ArrayList<StatusData> result) {
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);
        for (int i = 0; i < result.size(); i++) {
            String status = result.get(i).getStatus();
            String name = result.get(i).getPersonName();
            String email = result.get(i).getPersonMail();
            name += "/" + email;
            name +="&" + String.valueOf(i);
            Double lat = Double.parseDouble(result.get(i).getLatitude());
            Double lng = Double.parseDouble(result.get(i).getLongitude());
            if (status.length() > 20) {
                status = status.substring(0, 20);
                status += "...";
            }
            LatLng ll = new LatLng(lat, lng);
            options.add(ll);
            map.addMarker(new MarkerOptions().position(ll).title(name).snippet(status));
            if (i == 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
            }
        }
        map.addPolygon(options);


    }

    public String getmail() {
        Bundle mailBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }


    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.route_frnd_location_map));
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

    }

}
