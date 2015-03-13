package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.model.GmapV2Direction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
public class Friend_Location_Fragment extends Fragment implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public Friend_Location_Fragment(){setHasOptionsMenu(true);}
    //GeoPoint point1,point2;
    GmapV2Direction route;
    MapFragment fragment;
    GoogleMap directionMap;
    MarkerOptions markerOptions;
    LatLng fromPosition, toPosition;
    Document document;
    Location location;
    GoogleApiClient mgClient;
    float dis=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.route_direction_frnd_location, null, false);
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

        markerOptions = new MarkerOptions();
        Bundle data = getArguments();
        Double lat = data.getDouble("latitude");
        Double lng = data.getDouble("longitude");

        Bundle mydata = ((SlidingDrawerActivity)getActivity()).getEmail();
        Double myLat = Double.parseDouble(mydata.getString(PropertyNames.Userinfo_latitude.getProperty()));
        Double  myLng = Double.parseDouble(mydata.getString(PropertyNames.Userinfo_longitude.getProperty()));
        fromPosition = new LatLng(lat, lng);
        toPosition = new LatLng(myLat, myLng);

        directionMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 15));

        mgClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mgClient.connect();

        //calling the getroutetask for knowing the route
        GetRouteTask getRouteTask = new GetRouteTask();
        getRouteTask.execute();



        return v;
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            location = LocationServices.FusedLocationApi.getLastLocation(mgClient);
            toPosition = new LatLng(location.getLatitude(), location.getLongitude());
            directionMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 15));
        }catch (Exception e){
            Toast.makeText(getActivity(),"GPS not enabled",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

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


    //This class Get Route on the map
    private class GetRouteTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;
        String response = "";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading Route...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            document = route.getDocument(fromPosition, toPosition, GmapV2Direction.MODE_DRIVING);
            response = "Success";
            dis = checkForArea(fromPosition,toPosition);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            // directionMap.clear();
            if (response.equalsIgnoreCase("Success")) {
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
                //markerOptions.draggable(true);
                directionMap.addMarker(markerOptions);
            }
            dialog.dismiss();
            Toast.makeText(getActivity(),"Distance:"+String.valueOf(dis)+ " Km.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.route_frnd_location_map);
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (directionMap != null)
            directionMap = null;
        if (mgClient.isConnected()) {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mgClient, (com.google.android.gms.location.LocationListener) this);
            mgClient.disconnect();

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                directionMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                directionMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                directionMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                directionMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                directionMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private float checkForArea( LatLng fromPosition, LatLng toPosition) {
        Location locationA = new Location("point A");
        locationA.setLatitude(fromPosition.latitude);
        locationA.setLongitude(fromPosition.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(toPosition.latitude);
        locationB.setLongitude(toPosition.longitude);
        int distance = (int) locationA.distanceTo(locationB);
        return distance / 1000;

    }
}
