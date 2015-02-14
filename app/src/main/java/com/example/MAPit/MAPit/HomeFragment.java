package com.example.MAPit.MAPit;

//some test comment


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {
    // ...
    String[] latitude = {
            "53.558", "22.8427707", "53.551"
    };
    String[] longitude = {
            "9.927", "89.5980763", "9.993"
    };
    private GoogleMap map;
    EditText et;
    MapFragment mapFrag;
    public static View v;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null){
                parent.removeView(v);
                }
        }
        try {
            v = inflater.inflate(R.layout.home_map_activity, container, false);
        } catch (InflateException e) {
            Toast.makeText(getActivity(),"Map can't be loaded",Toast.LENGTH_LONG).show();
        }
        //View v = inflater.inflate(R.layout.home_map_activity, null, false);

        mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFrag.getMap();
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
                    String status = "Hi how are you everyone? Have a nice day ahead";
                    View v = getActivity().getLayoutInflater().inflate(R.layout.map_info_listview, null);
                    TextView tvFrndname = (TextView) v.findViewById(R.id.tv_frnd_name);
                    TextView tvFrndStatus = (TextView) v.findViewById(R.id.tv_frnd_status);
                    tvFrndname.setText("Neerob Basak"); // Later it will be name of friend
                    //checking for length of status
                    if (status.length() > 10) {
                        String substatus = status.substring(0, 20);
                        substatus += "...";
                        tvFrndStatus.setText(substatus);
                    }
                    return v;
                }
            });
        }
        /*Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
                .title("Hamburg"));
        Marker kiel = map.addMarker(new MarkerOptions()
                .position(KIEL)
                .title("Kiel")
                .snippet("Kiel is cool")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)));

        //*/
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < 3; i++) {
            String a = latitude[i];
            String b = longitude[i];
            double lati = Double.parseDouble(a);
            double longLat = Double.parseDouble(b);
            builder.include(new LatLng(lati, longLat));
            map.addMarker(new MarkerOptions().position(new LatLng(lati, longLat)).title(a).snippet(b));
        }
        LatLngBounds bounds = builder.build();
        drawLine();


        // Move the camera instantly to hamburg with a zoom of 15.
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        // Zoom in, animating the camera.
        LatLng ll = new LatLng(53.558, 9.927);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        et = (EditText) v.findViewById(R.id.editText1);
        //added the go button listener
        Button go = (Button) v.findViewById(R.id.go);
        go.setOnClickListener(this);


        //onclick listener on marker of friends location

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FriendsStatusFragment();
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            }
        });

        return v;


    }

    private void drawLine() {

        PolygonOptions options = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);
        for (int i = 0; i < 3; i++) {
            String a = latitude[i];
            String b = longitude[i];
            double lati = Double.parseDouble(a);
            double longLat = Double.parseDouble(b);
            LatLng ll = new LatLng(lati, longLat);
            options.add(ll);
        }
        map.addPolygon(options);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
                try {
                    geoLocate(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        map.moveCamera(update);
    }

    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);


        String location = et.getText().toString();

        Geocoder gc = new Geocoder(getActivity());
        List<Address> list = gc.getFromLocationName(location, 1);
        Address add = list.get(0);
        String locality = add.getLocality();
        Toast.makeText(getActivity(), locality, Toast.LENGTH_LONG).show();

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        gotoLocation(lat, lng, 15);

    }
    // to hide keyboard must use getActivity() and Context

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
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
}