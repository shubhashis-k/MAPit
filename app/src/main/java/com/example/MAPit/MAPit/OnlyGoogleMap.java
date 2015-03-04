package com.example.MAPit.MAPit;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.GroupsEndpointReturnData;
import com.example.MAPit.model.GmapV2Direction;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapit.backend.groupApi.model.Groups;
import com.mapit.backend.groupApi.model.Search;
import com.mapit.backend.informationApi.model.Information;

import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by SETU on 2/13/2015.
 */
public class OnlyGoogleMap extends Fragment implements View.OnClickListener {
    public static View v;
    private GoogleMap map;
    private final int SELECT_PHOTO = 1;
    EditText et;
    MapFragment mapFrag;
    Bundle data;
    String command, op_name;
    ImageView locImage;
    private String stringLocImage;
    ArrayList<Information> markerInfo;
    LatLng fromPosition;
    ArrayList<LatLng> routeData;
    GmapV2Direction route;
    Document document;
    Boolean Filter = false;
    Bundle info_data;
    ArrayList<Information> singleMarkerInfo;

    public OnlyGoogleMap() {
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        try {
            v = inflater.inflate(R.layout.home_map_activity, null, false);
        } catch (InflateException e) {
            Toast.makeText(getActivity(), "Map can't be loaded", Toast.LENGTH_LONG).show();
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

        data = getArguments();

        command = data.getString(Commands.SearchAndADD.getCommand());

        if (command.equals(Commands.ShowInMap.getCommand())) {
            populateInfoOfLocation("All", -1);
        } else if (command.equals(Commands.All_Group_Show.getCommand())) {
            PopulateAllGroups();
        }
        routeData = new ArrayList<LatLng>();
        route = new GmapV2Direction();

        // Here need my current location
        Bundle mydata = ((SlidingDrawerActivity) getActivity()).getEmail();
        Double myLat = Double.parseDouble(mydata.getString(PropertyNames.Userinfo_latitude.getProperty()));
        Double myLng = Double.parseDouble(mydata.getString(PropertyNames.Userinfo_longitude.getProperty()));

        fromPosition = new LatLng(myLat, myLng);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 15));


        et = (EditText) v.findViewById(R.id.editText1);
        //added the go button listener
        Button go = (Button) v.findViewById(R.id.go);
        go.setOnClickListener(this);

        singleMarkerInfo = new ArrayList<Information>();
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
                String position = status.substring(status.lastIndexOf('/') + 1);
                tvFrndname.setText(actual_status);
                tvFrndStatus.setText(marker.getSnippet());
                Information singleMarker = markerInfo.get(Integer.parseInt(position));
                singleMarkerInfo.add(singleMarker);
                info_data = new Bundle();
                info_data.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Location.getCommand());
                info_data.putSerializable(PropertyNames.Marker_Position.getProperty(), singleMarkerInfo);
                return v;
            }
        });


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (command.equals(Commands.All_Group_Show.getCommand())) {
                    //nothing to do
                } else {
                    Fragment fragment = new Friends_Status_Comment_Fragment();
                    fragment.setArguments(info_data);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                final Double lat = latLng.latitude;
                final Double lng = latLng.longitude;

                if (command.equals(Commands.ShowInMap.getCommand())) {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.add_location);
                    dialog.setTitle("Add Location on Map");
                    final Spinner sp = (Spinner) dialog.findViewById(R.id.sp_choose_type);
                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.location_category, android.R.layout.simple_list_item_checked);
                    adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    sp.setAdapter(adapter2);
                    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            op_name = sp.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    locImage = (ImageView) dialog.findViewById(R.id.add_new_location_pic);
                    final EditText loc_name = (EditText) dialog.findViewById(R.id.et_new_location_name);
                    final EditText loc_desc = (EditText) dialog.findViewById(R.id.et_new_location_desc);
                    Button add = (Button) dialog.findViewById(R.id.bt_add_location);
                    Button chooseImg = (Button) dialog.findViewById(R.id.choose_location_pic);

                    chooseImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent imagepicking = new Intent(Intent.ACTION_PICK);
                            imagepicking.setType("image/*");
                            startActivityForResult(imagepicking, SELECT_PHOTO);
                        }
                    });

                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = loc_name.getText().toString();
                            final String desc = loc_desc.getText().toString();
                            Data d = new Data();
                            d.setCommand(Commands.Information_set.getCommand());

                            Information i = new Information();
                            i.setKindName(op_name);
                            i.setInfoName(name);
                            i.setInfoDescription(desc);
                            i.setLatitude(String.valueOf(lat));
                            i.setLongitude(String.valueOf(lng));

                            if (stringLocImage != null)
                                i.setInformationPic(stringLocImage);

                            new InformationEndpointCommunicator() {
                                @Override
                                protected void onPostExecute(ArrayList<Information> result) {

                                    super.onPostExecute(result);


                                }
                            }.execute(new Pair<Data, Information>(d, i));

                            dialog.dismiss();
                            populateInfoOfLocation("All", -1);
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();


                } else if (command.equals(Commands.Group_Create.getCommand())) {
                    Bundle ll = new Bundle();
                    ll.putDouble(PropertyNames.Status_latitude.getProperty(), lat);
                    ll.putDouble(PropertyNames.Status_longitude.getProperty(), lng);
                    Fragment fragment = new Create_New_Group_Fragment();
                    fragment.setArguments(ll);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }     else if(command.equals(Commands.Status_add.getCommand())){
                    String groupKey = data.getString(Commands.Group_Key.getCommand());
                    Boolean logged = data.getBoolean(PropertyNames.Group_logged.getProperty());
                    if(logged) {
                        Bundle d = new Bundle();
                        d.putString(PropertyNames.Status_groupKey.getProperty(), groupKey);
                        d.putString(PropertyNames.Status_latitude.getProperty(), String.valueOf(lat));
                        d.putString(PropertyNames.Status_longitude.getProperty(), String.valueOf(lng));
                        d.putString(Commands.Status_Job.getCommand(), Commands.Status_Job_Type_Group.getCommand());
                        Fragment fragment = new AddStatus();
                        fragment.setArguments(d);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Sorry, You haven't joined this group yet.", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

        return v;
    }

    public void PopulateAllGroups(){
        Data info = new Data();
        info.setContext(getActivity());
        info.setCommand(Commands.Groups_fetch_all.getCommand());
        info.setUsermail(getmail());

        Groups g = new Groups();

        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result){

                super.onPostExecute(result);
                try {
                    ArrayList <Search> AllGroups = result.getDataList();
                    int x = 0;

                }
                catch (Exception e){

                }
            }
        }.execute(new Pair<Data, Groups>(info, g));
    }

    public String getmail() {
        Bundle mailBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }
    //here the info of all the markers of locations will be shown
    private void populateInfoOfLocation(String cat, final int radius) {
        Data d = new Data();
        d.setExtra(cat);
        d.setCommand(Commands.Information_get.getCommand());
        Information i = new Information();
        new InformationEndpointCommunicator() {
            @Override
            protected void onPostExecute(ArrayList<Information> result) {

                super.onPostExecute(result);

                try {
                    markerInfo = result;

                } catch (Exception e) {

                }
                drawMarkerAndLine(radius);
            }
        }.execute(new Pair<Data, Information>(d, i));
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
        if (map != null)
            map = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                try {
                    final Uri imageUri = imageReturnedIntent.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    locImage.setImageBitmap(selectedImage);

                    stringLocImage = ImageConverter.imageToStringConverter(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (command.equals(Commands.ShowInMap.getCommand())) {
            inflater.inflate(R.menu.menu_addnew_status, menu);
            menu.findItem(R.id.add_new_status).setTitle("Find Location");
        }
        if (command.equals(Commands.Group_Create.getCommand())) {

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_status:
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.filter_location);
                dialog.setTitle("Filter Your Search");
                final Spinner sp = (Spinner) dialog.findViewById(R.id.sp_choose_type);
                ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.location_category, android.R.layout.simple_list_item_checked);
                adapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                sp.setAdapter(adapter2);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        op_name = sp.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button search = (Button) dialog.findViewById(R.id.bt_find_location);
                final EditText rad = (EditText) dialog.findViewById(R.id.et_search_radius);
                // String search_radius = rad.getText().toString();

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String search_radius = rad.getText().toString();
                        if (search_radius.equals("")) {

                            Toast.makeText(getActivity(), "Give Radius", Toast.LENGTH_LONG).show();

                        } else {
                            int radius = Integer.parseInt(search_radius);
                            populateInfoOfLocation(op_name, radius);
                            dialog.dismiss();
                            Filter = true;
                        }
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void drawMarkerAndLine(int rad) {
        if (map == null) {
            map = mapFrag.getMap();
        } else {
            map.clear();
        }

        if (markerInfo.size() != 0) {
            for (int i = 0; i < markerInfo.size(); i++) {

                Double lat = Double.parseDouble(markerInfo.get(i).getLatitude());
                Double lng = Double.parseDouble(markerInfo.get(i).getLongitude());
                String status = markerInfo.get(i).getInfoDescription();
                String name = markerInfo.get(i).getInfoName();
                name += "/" + String.valueOf(i);
                LatLng ll = new LatLng(lat, lng);
                if (status.length() > 20) {
                    status = status.substring(0, 20);
                    status += "...";
                }

                if (rad != -1) {
                    boolean ret = checkForArea(rad,fromPosition,ll);
                    if (ret) {
                        routeData.add(ll);
                        map.addMarker(new MarkerOptions().position(ll).title(name).snippet(status));
                    }
                } else {
                    map.addMarker(new MarkerOptions().position(ll).title(name).snippet(status));
                }


                //String email = markerInfo.get(i).getPers();
                // name += "/" + email;

            }

            if (rad != -1) {
                for (int i = 0; i < routeData.size(); i++) {

                    LatLng toPosition = new LatLng(routeData.get(i).latitude, routeData.get(i).longitude);
                    new GetRouteTask() {
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                        }
                    }.execute((LatLng) toPosition);
                }


                map.addMarker(new MarkerOptions().position(fromPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker)));
            }
        }
    }

    private boolean checkForArea(int rad,LatLng fromPosition,LatLng toPosition) {
        Location locationA = new Location("point A");
        locationA.setLatitude(fromPosition.latitude);
        locationA.setLongitude(fromPosition.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(toPosition.latitude);
        locationB.setLongitude(toPosition.longitude);
        int  distance = (int) locationA.distanceTo(locationB) ;
        if(distance/1000 <= rad)
        return true;
        else
            return false;
    }


    private class GetRouteTask extends AsyncTask<LatLng, Void, String> {

        String response = "";

        @Override
        protected String doInBackground(LatLng... params) {
            document = route.getDocument(fromPosition, params[0], GmapV2Direction.MODE_WALKING);
            response = "Success";
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            // directionMap.clear();

            if (response.equalsIgnoreCase("Success")) {
                Log.v("Map", "got here");
                ArrayList<LatLng> directionPoint = route.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.RED);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                // Adding route on the map
                map.addPolyline(rectLine);
            }

        }
    }
}