package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.example.MAPit.Volley.adapter.Friend_RequestList_Adapter;
import com.example.MAPit.Volley.adapter.SearchListAdapter;
import com.example.MAPit.Volley.data.Friend_Request_ListItem;
import com.example.MAPit.Volley.data.SearchListItem;
import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.friendsApi.model.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SETU on 2/21/2015.
 */
public class Friend_Request_Fragment extends Fragment {

    private ListView listview;

    private Friend_RequestList_Adapter listAdapter;
    private List<Friend_Request_ListItem> listItems;


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

        return v;
    }

    public String getmail(){
        Bundle mailBundle = ((SlidingDrawerActivity)getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }

}