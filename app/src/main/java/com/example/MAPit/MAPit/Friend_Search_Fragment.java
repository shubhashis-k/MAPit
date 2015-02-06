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
import com.example.MAPit.Data_and_Return_Data.Data;

import com.example.MAPit.Volley.adapter.SearchListAdapter;

import com.example.MAPit.Volley.data.SearchListItem;
import com.mapit.backend.searchQueriesApi.model.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Friend_Search_Fragment extends Fragment {
    String data;
    private EditText searchBox;
    private ListView listview;

    private SearchListAdapter searchListAdapter;
    private List<SearchListItem> listItems;


    //added this for adding fragment menu
    public Friend_Search_Fragment() {
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_list_adapter_layout, null, false);

        searchBox = (EditText) v.findViewById(R.id.searchBox);
        listview = (ListView) v.findViewById(R.id.listview);
        listItems = new ArrayList<SearchListItem>();
        searchListAdapter = new SearchListAdapter(getActivity(), listItems);
        listview.setAdapter(searchListAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchBox.getText().toString().toLowerCase(Locale.getDefault());

                searchUser(text);

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
        listItems.clear();
        searchListAdapter.notifyDataSetChanged();

        for (int i = 0; i < a.size(); i++) {
            Search s = a.get(i);

            SearchListItem item = new SearchListItem();
            item.setName(s.getData());
            item.setLocation("Khulna");

            listItems.add(item);
        }

        // notify data changes to list adapter
        searchListAdapter.notifyDataSetChanged();

    }

}