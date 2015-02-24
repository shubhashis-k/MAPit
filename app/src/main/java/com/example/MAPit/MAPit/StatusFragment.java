package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.DatastoreKindNames;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Volley.adapter.StatusListAdapter;
import com.example.MAPit.Volley.data.StatusListItem;
import com.mapit.backend.statusApi.model.StatusData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SETU on 1/20/2015.
 */
public class StatusFragment extends Fragment {

    public StatusFragment() {
        setHasOptionsMenu(true);
    }

    private ListView listView;
    private StatusListAdapter statuslistAdapter;
    private List<StatusListItem> statusListItems;
    public String command;
    public ArrayList<StatusData> passThisData;
    public ArrayList <String> loc;
    public Bundle bundle;
    public Bundle data;
    StatusListItem item;
    static int counter=0;
    static int size=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_status, null, false);
        listView = (ListView) v.findViewById(R.id.list_frnd_status);
        loc = new ArrayList<>();
        statusListItems = new ArrayList<StatusListItem>();
        statuslistAdapter = new StatusListAdapter(getActivity(), statusListItems);
        listView.setAdapter(statuslistAdapter);
        data = getArguments();
        command = data.getString(Commands.Fragment_Caller.getCommand());

        if (command.equals(Commands.Called_From_Home.getCommand()))
            populateFriendsLatestStatus();
        else if (command.equals(Commands.Called_From_Info.getCommand()))
            populatePersonStatus();
        //listener for each listitem of friend status
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new Friends_Status_Comment_Fragment();
                bundle = new Bundle();
                StatusData st = passThisData.get(position);
                ArrayList<StatusData> passData = new ArrayList<StatusData>();
                passData.add(st);
                bundle.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Status.getCommand());
                bundle.putSerializable(Commands.Arraylist_Values.getCommand(), passData);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return v;
    }

    public void populatePersonStatus() {
        data = getArguments();
        String personMail = data.getString(PropertyNames.Userinfo_Mail.getProperty());

        Data d = new Data();
        d.setCommand(Commands.Status_showIndividualStatus.getCommand());


        StatusData s = new StatusData();
        s.setKind(DatastoreKindNames.StatusbyIndividual.getKind());
        s.setPersonMail(personMail);


        new StatusEndpointCommunicator() {
            @Override
            protected void onPostExecute(ArrayList<StatusData> result) {

                super.onPostExecute(result);
                passThisData = result;
                try {
                    populate(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute(new Pair<Data, StatusData>(d, s));
    }


    public void populateFriendsLatestStatus() {
        Bundle data = getArguments();
        ArrayList<StatusData> result = (ArrayList<StatusData>) data.getSerializable(Commands.Arraylist_Values.getCommand());
        passThisData = result;
        try {
            populate(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populate(ArrayList<StatusData> result) throws IOException {
        statusListItems.clear();
        statuslistAdapter.notifyDataSetChanged();
        size=result.size();
        for (int i = 0; i < result.size(); i++) {
            StatusData statusData = result.get(i);

            item = new StatusListItem();
            item.setName(statusData.getPersonName());
            item.setStatus(statusData.getStatus());

            Double lat = Double.parseDouble(statusData.getLatitude());
            Double lng = Double.parseDouble(statusData.getLongitude());

            LocationFinderData lfd = new LocationFinderData();

            lfd.setIndex(i);
            lfd.setLatitude(lat);
            lfd.setLongitude(lng);
            lfd.setContext(getActivity());

            new LocationFinder(){
                @Override
                protected void onPostExecute(LocationFinderData result) {
                    super.onPostExecute(result);
                    StatusListItem fetchItem = statusListItems.get(result.getIndex());
                    fetchItem.setLocation(result.getLocation());

                    statusListItems.set(result.getIndex(), fetchItem);

                    statuslistAdapter.notifyDataSetChanged();
                }
            }.execute(lfd);


            if (statusData.getStatusPhoto() != null) {
                item.setImge(statusData.getStatusPhoto());
            }
            if (statusData.getProfilePic() != null) {
                item.setProfilePic(statusData.getProfilePic());
            } else {

            }

            statusListItems.add(item);
        }

        statuslistAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home_fragment, menu);
        menu.findItem(R.id.switch_view_to_list).setTitle("Switch Back to Map");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.switch_view_to_list:
                if (command.equals(Commands.Called_From_Home.getCommand())) {
                    Fragment fragment = new HomeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (command.equals(Commands.Called_From_Info.getCommand())) {
                    bundle = new Bundle();
                    bundle.putSerializable(Commands.Arraylist_Values.getCommand(), passThisData);
                    Fragment fragment = new Marker_MapView();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
