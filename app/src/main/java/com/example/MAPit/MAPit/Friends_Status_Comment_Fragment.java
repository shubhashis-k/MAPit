package com.example.MAPit.MAPit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.MAPit.Volley.adapter.CommentListAdapter;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Comment_Item;
import com.example.MAPit.Volley.data.StatusListItem;
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

        if (command.equals(Commands.Called_From_Status.getCommand())) {
            dataReceived = (ArrayList<StatusData>) bundle.getSerializable(Commands.Arraylist_Values.getCommand());
            data = dataReceived.get(0);

            name.setText(data.getPersonName());
            status.setText(data.getStatus());
            lat = Double.parseDouble(data.getLatitude());
            lng = Double.parseDouble(data.getLongitude());

            LocationFinderData lfd = new LocationFinderData();

            lfd.setIndex(0);
            lfd.setLatitude(lat);
            lfd.setLongitude(lng);
            lfd.setContext(getActivity());

            new com.example.MAPit.MAPit.LocationFinder(){
                @Override
                protected void onPostExecute(LocationFinderData result) {
                    super.onPostExecute(result);
                    Comment_Item fetchItem = commentItems.get(result.getIndex());
                    fetchItem.setComment_TimeStamp(result.getLocation());

                    commentItems.set(result.getIndex(), fetchItem);

                    listAdapter.notifyDataSetChanged();
                }
            }.execute(lfd);

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

        if (command.equals(Commands.Called_From_Status.getCommand())) {


        }


        listView = (ListView) v.findViewById(R.id.comment_single_status);
        commentItems = new ArrayList<Comment_Item>();
        listAdapter = new CommentListAdapter(getActivity(), commentItems);
        listView.setAdapter(listAdapter);
        //listener for each listitem of friend status
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // We first check for cached request

        return v;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //must clear menu here to get fragment own menu option
        menu.clear();
        inflater.inflate(R.menu.menu_add_comment, menu);
        //super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_comment_single_status:
                addcommentdialog();
                return true;
            case R.id.go_to_frnd_location:
                Fragment fragment = new Friend_Location_Fragment();
                Bundle data = new Bundle();
                data.putDouble("latitude", lat);
                data.putDouble("longitude", lng);
                fragment.setArguments(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addcommentdialog() {
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
    }

    private class LocationFinder extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {

            String loc="";
            LatitudeToLocation ll = new LatitudeToLocation(getActivity());
            try {
                loc=ll.GetLocation(params[0],params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return loc;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            location.setText(s);

        }
    }

}
