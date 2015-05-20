package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;

import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.example.MAPit.Volley.adapter.SearchListAdapter;

import com.example.MAPit.Volley.data.SearchListItem;
import com.example.MAPit.Volley.data.StatusListItem;
import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.friendsApi.model.Search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Friend_Search_Fragment extends Fragment {
    String usermail;
    private EditText searchBox;
    private ListView listview;
    SearchListItem item;
    private SearchListAdapter searchListAdapter;
    private List<SearchListItem> listItems;
    String loc = null;


    //added this for adding fragment menu
    public Friend_Search_Fragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_list_adapter_layout, null, false);

        searchBox = (EditText) v.findViewById(R.id.searchBox);
        listview = (ListView) v.findViewById(R.id.listview);
        listItems = new ArrayList<SearchListItem>();
        searchListAdapter = new SearchListAdapter(getActivity(), listItems);
        listview.setAdapter(searchListAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new Friend_Tracking();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        showFriends();
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
                if (pattern.length() != 0)
                    searchUser(pattern);
                else
                    showFriends();
            }
        });

        return v;
    }

    public String getmail() {
        Bundle mailBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }

    public void showFriends() {
        Data info = new Data();
        info.setContext(getActivity());
        info.setCommand(Commands.Friends_fetch.getCommand());
        info.setUsermail(getmail());

        Friends f = new Friends();

        new FriendsEndpointCommunicator() {
            @Override
            protected void onPostExecute(FriendsEndpointReturnData result){

                super.onPostExecute(result);


                try {
                    ArrayList<Search> res = result.getDataList();
                    PopulateFriends(res);
                }
                catch(Exception e){

                }
            }
        }.execute(new Pair<Data, Friends>(info, f));
    }

    public void PopulateFriends(ArrayList<Search> a) {
        listItems.clear();
        searchListAdapter.notifyDataSetChanged();

        for (int i = 0; i < a.size(); i++) {
            Search s = a.get(i);

            item = new SearchListItem();
            item.setName(s.getData());
            item.setLocation(s.getLocation());
            item.setKey(s.getKey());
            item.setButton(Commands.Button_removeFriend.getCommand());
            item.setExtra(s.getExtra());
            if (s.getPicData() != null) {
                item.setImage(s.getPicData());
            }
            listItems.add(item);
        }

        // notify data changes to list adapter
        searchListAdapter.notifyDataSetChanged();

    }

    public void searchUser(String pattern) {
        Search searchProperty = new Search();
        searchProperty.setData(pattern);

        Data info = new Data();
        info.setContext(getActivity());
        info.setCommand(Commands.Friends_fetch_notfriends.getCommand());
        info.setUsermail(getmail());
        info.setExtra(pattern);

        Friends f = new Friends();

        new FriendsEndpointCommunicator() {
            @Override
            protected void onPostExecute(FriendsEndpointReturnData result){

                super.onPostExecute(result);


                try {
                    ArrayList<Search> res = result.getDataList();
                    PopulateNotFriends(res);
                }
                catch(Exception e){

                }
            }
        }.execute(new Pair<Data, Friends>(info, f));
    }

    public void PopulateNotFriends(ArrayList<Search> a) {
        listItems.clear();
        searchListAdapter.notifyDataSetChanged();

        for (int i = 0; i < a.size(); i++) {
            Search s = a.get(i);

            item = new SearchListItem();
            item.setName(s.getData());
            item.setLocation(s.getLocation());
            item.setButton(Commands.Button_addFriend.getCommand());
            if (s.getPicData() != null) {
                item.setImage(s.getPicData());
            }
            item.setKey(s.getKey());
            item.setExtra(s.getExtra());
            listItems.add(item);
        }

        // notify data changes to list adapter
        searchListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


}