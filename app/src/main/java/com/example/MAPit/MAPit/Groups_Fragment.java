package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.MAPit.Volley.adapter.Group_Search_List_Adapter;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Group_Item;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SETU on 1/24/2015.
 */
public class Groups_Fragment extends Fragment {
    private EditText search_group;
    private ListView groupListView;
    private Group_Search_List_Adapter groupListAdapter;
    private List<Group_Item> grouplistItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    GoogleMap map;
    public Groups_Fragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.groups_search_layout, null, false);
        search_group = (EditText) v.findViewById(R.id.group_search_et);
        groupListView = (ListView) v.findViewById(R.id.grouplist_lv);
        grouplistItems = new ArrayList<Group_Item>();
        groupListAdapter = new Group_Search_List_Adapter(getActivity(), grouplistItems);
        groupListView.setAdapter(groupListAdapter);

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"working",Toast.LENGTH_LONG).show();
            }
        });

        //checkForCache();

        search_group.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_group.getText().toString().toLowerCase(Locale.getDefault());
                checkForCache(text);
            }
        });
        return v;
    }

    private void checkForCache(final String filter) {
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data), filter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response, filter);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void parseJsonFeed(JSONObject response, String filter) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
            if (filter.equalsIgnoreCase("")) {
                grouplistItems.clear();

                groupListAdapter.notifyDataSetChanged();

            } else {
                grouplistItems.clear();
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    filter = filter.toLowerCase(Locale.getDefault());
                    String checkname = feedObj.get("name").toString();
                    if (checkname.toLowerCase(Locale.getDefault()).contains(filter)) {
                        Group_Item item = new Group_Item();
                        item.setGroup_Name(feedObj.getString("name"));
                        item.setGroup_location("Khulna");
                        item.setGroup_Image(feedObj.getString("profilePic"));
                        grouplistItems.add(item);
                    }
                }
            }

            // notify data changes to list adapater

            groupListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_group_adding,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager;
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.create_group:
                fragmentManager = getFragmentManager();
                fragment = new Create_New_Group_Fragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
                fragmentManager.beginTransaction().addToBackStack(null);
                return true;
            case R.id.my_groups:
                 fragmentManager = getFragmentManager();
                 fragment = new MyOwnGroupsFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
                fragmentManager.beginTransaction().addToBackStack(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.group_map);
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if(map!=null)
            map=null;
    }
}
