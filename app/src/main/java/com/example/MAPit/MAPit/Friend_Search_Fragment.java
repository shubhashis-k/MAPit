package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.os.Bundle;
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
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.adapter.CommentListAdapter;
import com.example.MAPit.Volley.adapter.Friend_SearchList_Adapter;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Comment_Item;
import com.example.MAPit.Volley.data.Friend_Search_ListItem;

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
    private ListView listView;
    private Friend_SearchList_Adapter listAdapter;
    private List<Friend_Search_ListItem> frndlistItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

    //added this for adding fragment menu
    public Friend_Search_Fragment() {
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_search, null, false);

        search_frnd = (EditText) v.findViewById(R.id.frnd_search_et);
        listView = (ListView) v.findViewById(R.id.frnd_search_lv);
        frndlistItems = new ArrayList<Friend_Search_ListItem>();
        listAdapter = new Friend_SearchList_Adapter(getActivity(), frndlistItems);
        listView.setAdapter(listAdapter);
        //listener for each listitem of friend status
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

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
                if(text.equalsIgnoreCase("")){
                    frndlistItems.clear();
                    listAdapter.notifyDataSetChanged();
                }
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
                data = new String(entry.data, "UTF-8");
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

            if (filter.equalsIgnoreCase("empty")) {
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    Friend_Search_ListItem item = new Friend_Search_ListItem();
                    item.setUser_Name(feedObj.getString("name"));
                    item.setUser_location("Khulna");
                    item.setUser_Imge(feedObj.getString("profilePic"));

                    frndlistItems.add(item);
                }
            } else {
                //this checks whether a i go back to empty edittext
                  if(filter.equalsIgnoreCase("")){
                      frndlistItems.clear();
                      listAdapter.notifyDataSetChanged();
                  }
                else {
                      frndlistItems.clear();
                      for (int i = 0; i < feedArray.length(); i++) {
                          JSONObject feedObj = (JSONObject) feedArray.get(i);
                          filter = filter.toLowerCase(Locale.getDefault());
                          String checkname = feedObj.get("name").toString();
                          if (checkname.toLowerCase(Locale.getDefault()).contains(filter)) {
                              Friend_Search_ListItem item = new Friend_Search_ListItem();
                              item.setUser_Name(feedObj.getString("name"));
                              item.setUser_location("Khulna");
                              item.setUser_Imge(feedObj.getString("profilePic"));
                              frndlistItems.add(item);
                          }
                      }
                  }

            }
            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}