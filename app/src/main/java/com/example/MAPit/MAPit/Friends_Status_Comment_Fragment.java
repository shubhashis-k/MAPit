package com.example.MAPit.MAPit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Volley.adapter.CommentListAdapter;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Comment_Item;
import com.example.MAPit.Volley.data.StatusListItem;
import com.mapit.backend.informationApi.model.Information;
import com.mapit.backend.statusApi.model.StatusData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SETU on 1/22/2015.
 */
public class Friends_Status_Comment_Fragment extends Fragment {

    private ListView listView;
    private CommentListAdapter listAdapter;
    private List<Comment_Item> commentItems;
    ImageView profilePic, feedPic;
    TextView name, location, status, url;
    Bundle bundle;
    public String command;
    private ArrayList<StatusData> dataReceived;

    String locname;
    Double lat, lng;
    StatusData data;

    //added this for adding fragment menu
    public Friends_Status_Comment_Fragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frnd_single_status, null, false);

        profilePic = (ImageView) v.findViewById(R.id.group_Pic_single_status);
        feedPic = (ImageView) v.findViewById(R.id.feedImage_single_status);
        name = (TextView) v.findViewById(R.id.group_name_single_status);
        location = (TextView) v.findViewById(R.id.timestamp_single_status);
        status = (TextView) v.findViewById(R.id.txtStatusMsg_single_status);
        url = (TextView) v.findViewById(R.id.txtUrl_single_status);

        bundle = getArguments();
        command = bundle.getString(Commands.Fragment_Caller.getCommand());
        if (command.equals(Commands.Called_From_Location.getCommand())) {
            ArrayList<Information> data = (ArrayList<Information>) bundle.getSerializable(PropertyNames.Marker_Position.getProperty());
            Information markerInfo = data.get(0);
            name.setText(markerInfo.getInfoName());
            status.setText(markerInfo.getInfoDescription());
            if (markerInfo.getInformationPic() != null) {
                profilePic.setImageBitmap(ImageConverter.stringToimageConverter(markerInfo.getInformationPic()));
            }
        }

        if (command.equals(Commands.Called_From_Status.getCommand())) {
            dataReceived = (ArrayList<StatusData>) bundle.getSerializable(Commands.Arraylist_Values.getCommand());
            data = dataReceived.get(0);

            name.setText(data.getPersonName());
            status.setText(data.getStatus());
            lat = Double.parseDouble(data.getLatitude());
            lng = Double.parseDouble(data.getLongitude());

            location.setText(data.getLocation());

            if (data.getProfilePic() != null) {
                profilePic.setImageBitmap(ImageConverter.stringToimageConverter(data.getProfilePic()));
            } else {
                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));

            }
            if (data.getStatusPhoto() != null) {
                feedPic.setImageBitmap(ImageConverter.stringToimageConverter(data.getStatusPhoto()));
            } else {

            }

        }


        listView = (ListView) v.findViewById(R.id.comment_single_status);
        commentItems = new ArrayList<Comment_Item>();
        listAdapter = new CommentListAdapter(getActivity(), commentItems);
        listView.setAdapter(listAdapter);


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //must clear menu here to get fragment own menu option
        menu.clear();
        if (command.equals(Commands.Called_From_Location.getCommand())) {

        } else if (command.equals(Commands.Called_From_Status.getCommand())) {
            inflater.inflate(R.menu.menu_add_comment, menu);
            menu.findItem(R.id.go_to_frnd_location).setTitle("Delete Status");
        } else {
            inflater.inflate(R.menu.menu_add_comment, menu);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_frnd_location:
                Fragment fragment = null;
                if (command.equals(Commands.Called_From_Status.getCommand())) {

                    Data d = new Data();
                    d.setStringKey(data.getStatusKey());
                    d.setCommand(Commands.Status_Remove.getCommand());

                    StatusData s = new StatusData();

                   new StatusEndpointCommunicator(){
                       @Override
                       protected void onPostExecute(ArrayList<StatusData> result) {
                           super.onPostExecute(result);
                       }
                   }.execute(new Pair<Data, StatusData>(d,s));

                    fragment = new StatusFragment();
                    Bundle myWallData = new Bundle();
                    myWallData.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_MyWall.getCommand());
                    myWallData.putString(PropertyNames.Userinfo_Mail.getProperty(), getmail());
                    fragment.setArguments(myWallData);

                } else {
                    fragment = new Friend_Location_Fragment();
                    Bundle data = new Bundle();
                    data.putDouble("latitude", lat);
                    data.putDouble("longitude", lng);
                    fragment.setArguments(data);
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getmail(){
        Bundle mailBundle = ((SlidingDrawerActivity)getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }

    /*private void addcommentdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Your Comment");
        final EditText input = new EditText(getActivity());
        input.setId(0);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();
                // Toast.makeText(getActivity(),value,Toast.LENGTH_SHORT).show();

                Comment_Item item = new Comment_Item();
                item.setUser_Name("Neerob Basak");

                // Image might be null sometimes
                item.setUser_comment(value);
                item.setUser_Imge("http://api.androidhive.info/feed/img/time.png");
                item.setComment_TimeStamp("1403375851930");
                commentItems.add(item);
                listAdapter.notifyDataSetChanged();
                return;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }*/

}
