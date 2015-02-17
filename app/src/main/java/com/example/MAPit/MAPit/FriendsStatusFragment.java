package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.MAPit.Volley.adapter.StatusListAdapter;
import com.example.MAPit.Volley.data.StatusListItem;
import com.mapit.backend.statusApi.model.StatusData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SETU on 1/20/2015.
 */
public class FriendsStatusFragment extends Fragment{

    public FriendsStatusFragment(){
        setHasOptionsMenu(true);
    }

    private ListView listView;
    private StatusListAdapter statuslistAdapter;
    private List<StatusListItem> statusListItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.friend_status,null,false);
        listView =(ListView)v.findViewById(R.id.list_frnd_status);
        statusListItems = new ArrayList<StatusListItem>();
        statuslistAdapter = new StatusListAdapter(getActivity(), statusListItems);
        listView.setAdapter(statuslistAdapter);
        //listener for each listitem of friend status
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Fragment fragment = new Friends_Status_Comment_Fragment();
              FragmentManager fragmentManager = getFragmentManager();
              fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
            }
        });

        populateFriendsLatestStatus();

        return v;
    }

    public void populateFriendsLatestStatus(){
        Bundle data = getArguments();
        ArrayList <StatusData> result = (ArrayList <StatusData>) data.getSerializable("data");

        statusListItems.clear();
        statuslistAdapter.notifyDataSetChanged();

        for (int i = 0; i < result.size(); i++) {
            StatusData statusData = result.get(i);

            StatusListItem item = new StatusListItem();
            item.setName(statusData.getPersonName());
            item.setStatus(statusData.getStatus());
            statusListItems.add(item);
        }

        statuslistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home_fragment,menu);
        menu.findItem(R.id.switch_view_to_list).setTitle("Switch Back to Map");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.switch_view_to_list:

                Fragment fragment = new Marker_MapView();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container,fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
