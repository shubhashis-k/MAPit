package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.MAPit.model.GmapV2Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;


/**
 * Created by SETU on 1/23/2015.
 */
public class Friend_Location_Fragment extends Fragment {

    //GeoPoint point1,point2;
    GmapV2Direction route;
    MapFragment fragment;
    GoogleMap directionMap;
    MarkerOptions markerOptions;
    LatLng fromPosition,toPosition;
    Document document;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.route_direction_frnd_location,null,false);
        route = new GmapV2Direction();
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.route_frnd_location_map);
        directionMap = fragment.getMap();
        // Enabling MyLocation in Google Map
        directionMap.setMyLocationEnabled(true);
        directionMap.getUiSettings().setZoomControlsEnabled(true);
        directionMap.getUiSettings().setCompassEnabled(true);
        directionMap.getUiSettings().setMyLocationButtonEnabled(true);
        directionMap.getUiSettings().setAllGesturesEnabled(true);
        directionMap.setTrafficEnabled(true);
        //directionMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        markerOptions = new MarkerOptions();
        fromPosition = new LatLng(11.663837, 78.147297);
        toPosition = new LatLng(11.723512, 78.466287);
        directionMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 15));
        //calling the getroutetask for knowing the route
        GetRouteTask getRouteTask = new GetRouteTask();
        getRouteTask.execute();

        return v;
    }


    //This class Get Route on the map
    private class GetRouteTask extends AsyncTask<String,Void,String>{
        private ProgressDialog dialog;
        String response ="";
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading Route...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            document = route.getDocument(fromPosition,toPosition,GmapV2Direction.MODE_DRIVING);
            response = "Success";
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
           // directionMap.clear();
            if(response.equalsIgnoreCase("Success")){
                ArrayList<LatLng> directionPoint = route.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.RED);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                // Adding route on the map
                directionMap.addPolyline(rectLine);
                markerOptions.position(toPosition);
                markerOptions.position(fromPosition);
                markerOptions.draggable(true);
                directionMap.addMarker(markerOptions);
            }
            dialog.dismiss();
        }

    }
}
