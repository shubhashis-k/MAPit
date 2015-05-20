package com.example.MAPit.MAPit;

//some test comment


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;
import com.mapit.backend.userinfoModelApi.model.UserinfoModelCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {
        setHasOptionsMenu(true);
    }

    boolean togstate;
    ToggleButton toggleButton;
    String email;
    private Button chat, call, see_info;
    private Context context;
    private GoogleMap map;
    EditText et;
    MapFragment mapFrag;
    Bundle info_data;
    private ArrayList<StatusData> passThisData;
    final CharSequence[] items = {"Give Information", "Create Group", "Buzz"};

    // public static View v;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("Tracking","I am in onCreateView");

        View v = inflater.inflate(R.layout.home_map_activity, null, false);
        toggleButton = (ToggleButton) v.findViewById(R.id.enPosition);

        checkPreferenceForTogState();

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableMyPosition();
                    togstate=true;
                    Toast.makeText(getActivity(), "Sharing Location.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(getActivity(), EnableLocation.class);
                    getActivity().stopService(i);
                    togstate=false;
                    Toast.makeText(getActivity(), "Location Sharing Stopped.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mapFrag = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
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
                    email = status.substring(status.lastIndexOf('/') + 1);
                    tvFrndname.setText(actual_status);
                    tvFrndStatus.setText(marker.getSnippet());
                    info_data = new Bundle();
                    info_data.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Info.getCommand());
                    info_data.putString(PropertyNames.Userinfo_Mail.getProperty(), email);

                    return v;
                }
            });
        }

        et = (EditText) v.findViewById(R.id.etsearch);
        ImageView go = (ImageView) v.findViewById(R.id.go);
        go.setOnClickListener(this);

        //onclick listener on map

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final String lat = String.valueOf(latLng.latitude);
                final String lng = String.valueOf(latLng.longitude);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick Up Your Choice")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragment;
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                switch (which) {
                                    case 0:
                                        fragment = new AddStatus();
                                        Bundle dataToAddStatus = new Bundle();
                                        dataToAddStatus.putString(Commands.Status_Job.getCommand(), Commands.Status_Job_Type_Individual.getCommand());
                                        dataToAddStatus.putString(PropertyNames.Status_latitude.getProperty(), lat);
                                        dataToAddStatus.putString(PropertyNames.Status_longitude.getProperty(), lng);
                                        fragment.setArguments(dataToAddStatus);
                                        transaction.replace(R.id.frame_container, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        break;
                                    case 1:
                                        fragment = new Create_New_Group_Fragment();
                                        Bundle createGroup = new Bundle();
                                        createGroup.putDouble(PropertyNames.Status_latitude.getProperty(), Double.parseDouble(lat));
                                        createGroup.putDouble(PropertyNames.Status_longitude.getProperty(), Double.parseDouble(lng));
                                        fragment.setArguments(createGroup);
                                        transaction.replace(R.id.frame_container, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();
                                        break;
                                    case 2:
                                        Intent buzz = new Intent(getActivity(), LocBuzzService.class);
                                        Bundle b = new Bundle();
                                        b.putDouble("lat", latLng.latitude);
                                        b.putDouble("lng", latLng.longitude);
                                        buzz.putExtras(b);
                                        getActivity().startService(buzz);

                                        break;
                                    default:
                                        break;

                                }

                            }
                        });

                builder.show();

            }
        });


        //onclick listener on marker of friends location

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.call_chat_dialog);
                dialog.setTitle("   Choose Any Option");

                see_info = (Button) dialog.findViewById(R.id.b_see_information);
                chat = (Button) dialog.findViewById(R.id.b_chat);
                call = (Button) dialog.findViewById(R.id.b_call);

                see_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Fragment fragment = new StatusFragment();
                        fragment.setArguments(info_data);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchCall();
                    }
                });

                chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Fragment fragment = new ChatFragment();
                        fragment.setArguments(info_data);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });

                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                dialog.show();

            }
        });

        fetchFriendStatus();

        return v;
    }



    private void enableMyPosition() {
        Log.i("Tracking","I am in enableMyPostion");
        Intent enPos = new Intent(getActivity(), EnableLocation.class);
        Bundle b = new Bundle();
        b.putString("usermail", getmail());
        enPos.putExtras(b);
        getActivity().startService(enPos);
    }


    private void fetchCall() {
        final UserinfoModel userdata = new UserinfoModel();
        userdata.setMail(email);

        new SignIn_Endpoint_Communicator() {
            @Override
            protected void onPostExecute(UserinfoModelCollection result) {
                try {
                    ArrayList<UserinfoModel> result_list = (ArrayList<UserinfoModel>) result.getItems();
                    UserinfoModel userininfo = new UserinfoModel();

                    if (result_list.size() > 0)
                        userininfo = result_list.get(0);

                    String phoneNumber = userininfo.getMobilephone();
                    makeCall(phoneNumber);
                } catch (Exception e) {

                }
            }
        }.execute(new Pair<Context, UserinfoModel>(this.getActivity(), userdata));
        // PhoneCallListener phoneCallListener = new PhoneCal
    }

    private void makeCall(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }


    public void fetchFriendStatus() {
        Data d = new Data();
        d.setCommand(Commands.Status_fetchFriendsStatus.getCommand());
        d.setUsermail(getmail());

        StatusData statusData = new StatusData();

        context = this.getActivity();

        new StatusEndpointCommunicator() {
            @Override
            protected void onPostExecute(ArrayList<StatusData> result) {

                super.onPostExecute(result);
                passThisData = result;
                try {
                    drawMarkerAndLine(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute(new Pair<Data, StatusData>(d, statusData));
    }

    private void drawMarkerAndLine(ArrayList<StatusData> result) throws Exception {

        if (result.size() != 0) {
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
        try {
            Address add = list.get(0);
            String locality = add.getLocality();
            Toast.makeText(getActivity(), locality, Toast.LENGTH_LONG).show();
            double lat = add.getLatitude();
            double lng = add.getLongitude();

            gotoLocation(lat, lng, 15);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Give valid Location Name", Toast.LENGTH_LONG).show();
        }


    }
    // to hide keyboard must use getActivity() and Context

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        Log.i("Tracking","i am in onDestroy");
        super.onDestroyView();
        saveToggleState();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        try {
            ft.commit();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Closing MapIit", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToggleState() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("togstate",togstate);
        editor.commit();
    }

    private void checkPreferenceForTogState() {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        togstate = sharedPref.getBoolean("togstate",true);
        toggleButton.setChecked(togstate);

    }

    @Override
    public void onPause() {
        super.onPause();
        saveToggleState();
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

        switch (item.getItemId()) {
            case R.id.switch_view_to_list:
                Bundle data = new Bundle();
                data.putSerializable(Commands.Arraylist_Values.getCommand(), passThisData);
                data.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Home.getCommand());
                Fragment fragment = new StatusFragment();
                fragment.setArguments(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}