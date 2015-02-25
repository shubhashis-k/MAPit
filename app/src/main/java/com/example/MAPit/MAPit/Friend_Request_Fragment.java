package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.example.MAPit.Data_and_Return_Data.GroupsEndpointReturnData;
import com.example.MAPit.Volley.adapter.Friend_RequestList_Adapter;
import com.example.MAPit.Volley.data.Friend_Request_ListItem;
import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.friendsApi.model.Search;
import com.mapit.backend.groupApi.model.Groups;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SETU on 2/21/2015.
 */
public class Friend_Request_Fragment extends Fragment {
    Bundle data;
    private ListView listview;

    private Friend_RequestList_Adapter listAdapter;
    private List<Friend_Request_ListItem> listItems;
    Friend_Request_ListItem item;

    //added this for adding fragment menu
    public Friend_Request_Fragment() {
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_status, null, false);
        listview = (ListView) v.findViewById(R.id.list_frnd_status);
        listItems = new ArrayList<Friend_Request_ListItem>();
        listAdapter = new Friend_RequestList_Adapter(getActivity(), listItems);
        listview.setAdapter(listAdapter);

        data = getArguments();
        String command = data.getString(Commands.Notification_job.getCommand());

        if (command.equals(Commands.Friends_Request.getCommand()))
            populatePendingFriendRequest();
        else if(command.equals(Commands.Group_Join_Group.getCommand()))
            populateGroupJoinRequest();
        return v;
    }

    public void populateGroupJoinRequest(){
        Data info = new Data();
        info.setCommand(Commands.Group_fetch_notification.getCommand());
        info.setUsermail(getmail());

        Groups g = new Groups();

        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result){

                super.onPostExecute(result);

                ArrayList <com.mapit.backend.groupApi.model.Search> res = result.getDataList();
                try {
                    PopulatePendingGroupList(res, Commands.Group_Join_Group.getCommand());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.execute(new Pair<Data, Groups>(info, g));
    }
    public void populatePendingFriendRequest(){
        String personMail = getmail();

        Data info = new Data();
        info.setCommand(Commands.Friends_fetch_Pending.getCommand());
        info.setUsermail(getmail());

        Friends f = new Friends();

        new FriendsEndpointCommunicator(){
            @Override
            protected void onPostExecute(FriendsEndpointReturnData result){

                super.onPostExecute(result);

                ArrayList <Search> res = result.getDataList();
                try {
                    PopulatePendingFriendList(res, Commands.Friends_Request.getCommand());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.execute(new Pair<Data, Friends>(info, f));
    }

    public void PopulatePendingGroupList(ArrayList<com.mapit.backend.groupApi.model.Search> result, String request_Type) throws IOException{
        listItems.clear();
        listAdapter.notifyDataSetChanged();

        for (int i = 0; i < result.size(); i++) {
            com.mapit.backend.groupApi.model.Search s = result.get(i);

            item = new Friend_Request_ListItem();
            item.setUser_Name(s.getData());
            item.setButton_type(request_Type);

            item.setUsermail(s.getExtra());
            item.setStringKey(s.getKey());
            item.setUser_location(s.getExtra1());

            if (s.getPicData() != null) {
                item.setUser_Imge(s.getPicData());
            }

            listItems.add(item);
        }

        // notify data changes to list adapter
        listAdapter.notifyDataSetChanged();
    }



    public void PopulatePendingFriendList(ArrayList<Search> result, String request_Type) throws IOException{
        listItems.clear();
        listAdapter.notifyDataSetChanged();

        for (int i = 0; i < result.size(); i++) {
            Search s = result.get(i);

            item = new Friend_Request_ListItem();
            item.setUser_Name(s.getData());
            item.setButton_type(request_Type);
            Double lat = Double.parseDouble(s.getLatitude());
            Double lng = Double.parseDouble(s.getLongitude());
            Double[] dd = new Double[]{lat,lng};
            new LocationFinder().execute(dd);
            item.setUsermail(s.getExtra());
            if (s.getPicData() != null) {
                item.setUser_Imge(s.getPicData());
            }

            listItems.add(item);
        }

        // notify data changes to list adapter
        listAdapter.notifyDataSetChanged();
    }


    public String getmail(){
        Bundle mailBundle = ((SlidingDrawerActivity)getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
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
            item.setUser_location(s);
            listAdapter.notifyDataSetChanged();

        }
    }

}