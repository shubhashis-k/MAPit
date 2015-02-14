package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;

/**
 * Created by SETU on 2/13/2015.
 */
public class OnlyGoogleMap extends Fragment implements View.OnClickListener {
    public static View v;
    private GoogleMap map;
    EditText et;
    MapFragment mapFrag;

    public View onCreateView(  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        try {
            v = inflater.inflate(R.layout.home_map_activity, null, false);
        } catch (InflateException e) {
           Toast.makeText(getActivity(),"Map can't be loaded",Toast.LENGTH_LONG).show();
        }
        //View v = inflater.inflate(R.layout.home_map_activity, container, false);

        mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFrag.getMap();
        //enabling my current location
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setTrafficEnabled(true);


        // Zoom in, animating the camera.
        LatLng ll = new LatLng (53.558,9.927);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
        et = (EditText) v.findViewById(R.id.editText1);
        //added the go button listener
         Button go = (Button) v.findViewById(R.id.go);
        go.setOnClickListener(this);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Double lat = latLng.latitude;
                Double lng = latLng.longitude;
                Bundle ll = new Bundle();
                ll.putDouble("latitude",lat);
                ll.putDouble("longitude",lng);
                Fragment fragment = new Create_New_Group_Fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
                fragmentManager.beginTransaction().addToBackStack(null);
                fragment.setArguments(ll);
            }
        });

        return v;
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

    public void onDestroyView()
    {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if(map!=null)
            map=null;
    }
}