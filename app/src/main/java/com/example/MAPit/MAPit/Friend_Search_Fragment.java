package com.example.MAPit.MAPit;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.adapter.CommentListAdapter;
import com.example.MAPit.Volley.adapter.Friend_SearchList_Adapter;
import com.example.MAPit.Volley.adapter.MyFriendListAdapter;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Comment_Item;
import com.example.MAPit.Volley.data.FeedItem;
import com.example.MAPit.Volley.data.Friend_Search_ListItem;
import com.example.MAPit.Volley.data.MyFriendsItem;
import com.mapit.backend.searchQueriesApi.model.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Friend_Search_Fragment extends Fragment {
    String data;
    private EditText search_frnd;
    private ListView listView, myfrndlistView;
    private Friend_SearchList_Adapter listAdapter;
    private MyFriendListAdapter myFriendListAdapter;
    private List<Friend_Search_ListItem> frndlistItems;
    private List<MyFriendsItem> myfrndlistItems;


    //added this for adding fragment menu
    public Friend_Search_Fragment() {
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_search, null, false);

        search_frnd = (EditText) v.findViewById(R.id.frnd_search_et);

        myfrndlistView = (ListView) v.findViewById(R.id.my_frnd_lv);

        myfrndlistItems = new ArrayList<MyFriendsItem>();

        myFriendListAdapter = new MyFriendListAdapter(getActivity(), myfrndlistItems);

        myfrndlistView.setAdapter(myFriendListAdapter);

        search_frnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_frnd.getText().toString().toLowerCase(Locale.getDefault());

                searchUser(text);
                //checkForCache(text);


            }
        });
        return v;
    }
    public void searchUser(String pattern){
        Search searchProperty = new Search();
        searchProperty.setData(pattern);

        Data info = new Data();
        info.setContext(getActivity());
        //info.setCommand();
        //info.setUsermail();
        info.setCommand(Commands.Search_users.getCommand());

        new SearchEndpointCommunicator(){
            @Override
            protected void onPostExecute(ArrayList <Search> result){

                super.onPostExecute(result);

                ArrayList <Search> res = result;
                Populate(res);

            }
        }.execute(new Pair<Data, Search>(info, searchProperty));
    }


    public void Populate(ArrayList <Search> a){
        myfrndlistItems.clear();
        myFriendListAdapter.notifyDataSetChanged();

        for (int i = 0; i < a.size(); i++) {
            Search s = a.get(i);

            MyFriendsItem item = new MyFriendsItem();
            item.setUser_Name(s.getData());
            item.setUser_location("Khulna");

            myfrndlistItems.add(item);
        }

        // notify data changes to list adapter
        myFriendListAdapter.notifyDataSetChanged();

    }

}