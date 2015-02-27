package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.GroupsEndpointReturnData;
import com.example.MAPit.Volley.adapter.SearchListAdapter;
import com.example.MAPit.Volley.data.SearchListItem;
import com.example.MAPit.Volley.data.StatusListItem;
import com.mapit.backend.groupApi.model.Groups;
import com.mapit.backend.groupApi.model.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SETU on 1/24/2015.
 */
public class Groups_Fragment extends Fragment {
    String usermail;
    private EditText searchBox;
    private ListView listview;
    private ArrayList <Search> res;
    private SearchListAdapter searchListAdapter;
    private List<SearchListItem> listItems;
    private boolean ShowingMyGroups = false;

    public Groups_Fragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_list_adapter_layout, null, false);
        res = new ArrayList<>();
        searchBox = (EditText) v.findViewById(R.id.searchBox);
        listview = (ListView) v.findViewById(R.id.listview);
        listItems = new ArrayList<SearchListItem>();
        searchListAdapter = new SearchListAdapter(getActivity(), listItems);
        listview.setAdapter(searchListAdapter);
        listview.setItemsCanFocus(true);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Search s = res.get(position);

                if(s.getExtra1().equals(PropertyNames.Group_Private.getProperty()) && ShowingMyGroups == false)
                {
                    Toast.makeText(getActivity(), "Sorry, The Group is Private!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Bundle data = new Bundle();
                    data.putString(PropertyNames.Status_groupKey.getProperty(), s.getKey());
                    data.putString(Commands.Fragment_Caller.getCommand(), Commands.Called_From_Group.getCommand());
                    data.putBoolean(PropertyNames.Group_logged.getProperty(), ShowingMyGroups);

                    Fragment fragment = new StatusFragment();
                    fragment.setArguments(data);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });

        showMyGroups();
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pattern = searchBox.getText().toString().toLowerCase(Locale.getDefault());

                if(pattern.length() != 0)
                    searchGroups(pattern);
                else
                    showMyGroups();

            }
        });

        return v;
    }

    public String getmail(){
        Bundle mailBundle = ((SlidingDrawerActivity)getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }


    public void showMyGroups(){
        ShowingMyGroups = true;
        Data info = new Data();
        info.setContext(getActivity());
        info.setCommand(Commands.Group_fetch_myGroups.getCommand());
        info.setUsermail(getmail());

        Groups g = new Groups();

        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result) throws  NullPointerException{

                super.onPostExecute(result);

                res = result.getDataList();
                PopulateMyGroups(res);

            }
        }.execute(new Pair<Data, Groups>(info, g));
    }

    public void PopulateMyGroups(ArrayList<Search> a){
        listItems.clear();
        searchListAdapter.notifyDataSetChanged();

        for (int i = 0; i < a.size(); i++) {
            Search s = a.get(i);

            SearchListItem item = new SearchListItem();
            item.setName(s.getData());
            //item.setLocation("Khulna");
            Double lat = Double.parseDouble(s.getLatitude());
            Double lng = Double.parseDouble(s.getLongitude());

            LocationFinderData lfd = new LocationFinderData();

            lfd.setIndex(i);
            lfd.setLatitude(lat);
            lfd.setLongitude(lng);
            lfd.setContext(getActivity());

            new LocationFinder(){
                @Override
                protected void onPostExecute(LocationFinderData result) throws IndexOutOfBoundsException{
                    super.onPostExecute(result);
                    try {
                        SearchListItem fetchItem = listItems.get(result.getIndex());
                        fetchItem.setLocation(result.getLocation());

                        listItems.set(result.getIndex(), fetchItem);

                        searchListAdapter.notifyDataSetChanged();
                    }
                    catch(Exception io){

                    }
                }
            }.execute(lfd);

            item.setKey(s.getKey());
            item.setButton(Commands.Group_Remove.getCommand());
            item.setExtra(getmail());
            listItems.add(item);
        }

        // notify data changes to list adapter
        searchListAdapter.notifyDataSetChanged();

    }

    public void searchGroups(String pattern){
        ShowingMyGroups = false;
        Search searchProperty = new Search();
        searchProperty.setData(pattern);

        Data info = new Data();
        info.setContext(getActivity());
        info.setCommand(Commands.Group_fetch_GroupsnotMine.getCommand());
        info.setUsermail(getmail());
        info.setExtra(pattern);

        Groups g = new Groups();

        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result){

                super.onPostExecute(result);

                res = result.getDataList();
                PopulateSearchGroup(res);

            }
        }.execute(new Pair<Data, Groups>(info, g));
    }

    public void PopulateSearchGroup(ArrayList<Search> a){
        listItems.clear();
        searchListAdapter.notifyDataSetChanged();

        for (int i = 0; i < a.size(); i++) {
            Search s = a.get(i);

            SearchListItem item = new SearchListItem();
            item.setName(s.getData());

            Double lat = Double.parseDouble(s.getLatitude());
            Double lng = Double.parseDouble(s.getLongitude());

            LocationFinderData lfd = new LocationFinderData();

            lfd.setIndex(i);
            lfd.setLatitude(lat);
            lfd.setLongitude(lng);
            lfd.setContext(getActivity());

            new LocationFinder(){
                @Override
                protected void onPostExecute(LocationFinderData result) {
                    super.onPostExecute(result);
                    SearchListItem fetchItem = listItems.get(result.getIndex());
                    fetchItem.setLocation(result.getLocation());

                    listItems.set(result.getIndex(), fetchItem);

                    searchListAdapter.notifyDataSetChanged();
                }
            }.execute(lfd);

            item.setButton(Commands.Group_Join_Group.getCommand());
            item.setKey(s.getKey());
            item.setExtra(getmail());
            listItems.add(item);
        }

        // notify data changes to list adapter
        searchListAdapter.notifyDataSetChanged();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_group_adding, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager;
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.create_group:
                fragment = new OnlyGoogleMap();
                Bundle d = new Bundle();
                d.putString(Commands.SearchAndADD.getCommand(),Commands.Group_Create.getCommand());
                fragment.setArguments(d);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
            case R.id.see_in_map:

                fragment = new Marker_MapView();
                Bundle data = new Bundle();
                data.putString(Commands.ForMarkerView.getCommand(),Commands.Called_From_Group.getCommand());
                data.putSerializable(Commands.Arraylist_Values.getCommand(), res);
                fragment.setArguments(data);
                FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                transaction1.replace(R.id.frame_container,fragment);
                transaction1.addToBackStack(null);
                transaction1.commit();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
