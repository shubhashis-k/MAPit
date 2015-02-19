package com.example.MAPit.MAPit;

//some test comment


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.google.android.gms.maps.CameraUpdate;
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


public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    private Context context;
    private GoogleMap map;
    EditText et;
    MapFragment mapFrag;
    Bundle info_data;
    private ArrayList <StatusData> passThisData;

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
        View v = inflater.inflate(R.layout.home_map_activity, null, false);

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
                    View v = getActivity().getLayoutInflater().inflate(R.layout.map_info_listview, null);
                    TextView tvFrndname = (TextView) v.findViewById(R.id.tv_frnd_name);
                    TextView tvFrndStatus = (TextView) v.findViewById(R.id.tv_frnd_status);
                    String status = marker.getTitle();
                    String actual_status = status.substring(0, status.indexOf('/'));
                    String email = status.substring(status.lastIndexOf('/') + 1);
                    tvFrndname.setText(actual_status);
                    tvFrndStatus.setText(marker.getSnippet());
                    info_data = new Bundle();
                    info_data.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Info.getCommand());
                    info_data.putString(PropertyNames.Userinfo_Mail.getProperty(), email);
                    return v;
                }
            });
        }

        et = (EditText) v.findViewById(R.id.editText1);
        Button go = (Button) v.findViewById(R.id.go);
        go.setOnClickListener(this);


        //onclick listener on marker of friends location

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Fragment fragment = new StatusFragment();
                fragment.setArguments(info_data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        fetchFriendStatus();

        return v;
    }

    public void fetchFriendStatus(){
        Data d = new Data();
        d.setCommand(Commands.Status_fetchFriendsStatus.getCommand());
        d.setUsermail(getmail());

        StatusData statusData = new StatusData();

        context = this.getActivity();

        new StatusEndpointCommunicator(){
            @Override
            protected void onPostExecute(ArrayList <StatusData> result){

                super.onPostExecute(result);
                passThisData = result;
                drawMarkerAndLine(result);

            }
        }.execute(new Pair<Data, StatusData>(d, statusData));
    }

    private void drawMarkerAndLine(ArrayList<StatusData> result) {

        if(result.size() != 0) {
            PolygonOptions options = new PolygonOptions()
                    .fillColor(0x330000FF)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(3);
            for (int i = 0; i < result.size(); i++) {
                String status = result.get(i).getStatus();
                String name = result.get(i).getPersonName();
                String email = result.get(i).getPersonMail();
                name += "/" + email;
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


    }

    public String getmail() {
        Bundle mailBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.switch_view_to_list:
                Bundle data = new Bundle();
                data.putSerializable(Commands.Arraylist_Values.getCommand(), passThisData);
                data.putString(Commands.Fragment_Caller.getCommand(),Commands.Called_From_Home.getCommand());
                Fragment fragment = new StatusFragment();
                fragment.setArguments(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}