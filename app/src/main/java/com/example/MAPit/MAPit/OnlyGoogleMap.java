package com.example.MAPit.MAPit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.CheckedOutputStream;

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
            populateInfoOfLocation();
        }



        // Here need my current location
        LatLng ll = new LatLng(53.558, 9.927);
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

                    locImage = (ImageView) v.findViewById(R.id.add_new_location_pic);
                    EditText loc_name = (EditText) dialog.findViewById(R.id.et_new_location_name);
                    EditText loc_desc = (EditText) dialog.findViewById(R.id.et_new_location_desc);
                    Button add = (Button) dialog.findViewById(R.id.bt_add_location);
                    Button chooseImg = (Button) dialog.findViewById(R.id.choose_location_pic);
                    String name = loc_name.getText().toString();
                    String desc = loc_desc.getText().toString();

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
                            //I have to add the location to datastore and also have to check that every field present
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();


                } else if (command.equals(Commands.Group_Create.getCommand())) {
                    Bundle ll = new Bundle();
                    ll.putDouble("latitude", lat);
                    ll.putDouble("longitude", lng);
                    Fragment fragment = new Create_New_Group_Fragment();
                    fragment.setArguments(ll);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if(command.equals(Commands.Status_add.getCommand())){
                    Bundle d = new Bundle();
                    String groupKey = data.getString(Commands.Group_Key.getCommand());
                    d.putString(Commands.Group_Key.getCommand(),groupKey);
                    d.putDouble("latitude", lat);
                    d.putDouble("longitude", lng);
                    d.putString(Commands.Status_add.getCommand(),Commands.Called_From_Group.getCommand());
                    Fragment fragment = new AddStatus();
                    fragment.setArguments(d);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }


            }
        });

        return v;
    }

    //here the info of all the markers of locations will be shown
    private void populateInfoOfLocation() {


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
                EditText rad = (EditText) dialog.findViewById(R.id.et_search_radius);
                String search_radius = rad.getText().toString();

                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //I have to search for the location and show it in the map with all the marker
                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}