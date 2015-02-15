package com.example.MAPit.MAPit;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.MAPit.Volley.adapter.CommentListAdapter;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Comment_Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private String URL_FEED="http://api.androidhive.info/feed/feed.json";

    //added this for adding fragment menu
    public Friends_Status_Comment_Fragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.frnd_single_status,null,false);

        listView =(ListView)v.findViewById(R.id.comment_single_status);
        commentItems = new ArrayList<Comment_Item>();
        listAdapter = new CommentListAdapter(getActivity(),commentItems);
        listView.setAdapter(listAdapter);
        //listener for each listitem of friend status
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
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
                        parseJsonFeed(response);
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
        //this must to be included to get the menu of fragment working
        //setHasOptionsMenu(true);
        return v;
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Comment_Item item = new Comment_Item();
                item.setUser_Name(feedObj.getString("name"));

                // Image might be null sometimes
                item.setUser_comment(feedObj.getString("status"));
                item.setUser_Imge(feedObj.getString("profilePic"));
                item.setComment_TimeStamp(feedObj.getString("timeStamp"));

                commentItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //must clear menu here to get fragment own menu option
        menu.clear();
        inflater.inflate(R.menu.menu_add_comment,menu);
        //super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_comment_single_status:
                addcommentdialog();
                return true;
            case R.id.go_to_frnd_location:
                Fragment fragment = new Friend_Location_Fragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container,fragment);
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

        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
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
}
