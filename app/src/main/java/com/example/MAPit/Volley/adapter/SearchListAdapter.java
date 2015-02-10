package com.example.MAPit.Volley.adapter;

/**
 * Created by SETU on 1/24/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.example.MAPit.Data_and_Return_Data.GroupsEndpointReturnData;
import com.example.MAPit.MAPit.FriendsEndpointCommunicator;
import com.example.MAPit.MAPit.GroupsEndpointCommunicator;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.data.SearchListItem;
import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.infoCollectorApi.model.InfoCollector;
import com.mapit.backend.infoCollectorApi.model.UserinfoModel;
import com.example.MAPit.MAPit.infoCollectorEndpointCommunicator;
import com.mapit.backend.groupApi.model.Groups;
import java.util.List;

public class SearchListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SearchListItem> listItems;
    public SearchListAdapter(Activity activity, List<SearchListItem> listItems) {
        this.activity = activity;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int location) {
        return listItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_list_items, null);


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        Button button = (Button) convertView.findViewById(R.id.command_button);


        SearchListItem item = listItems.get(position);


        name.setText(item.getName());
        location.setText(item.getLocation());

        final String buttonText = item.getButton();
        final String usermail = item.getExtra();
        final String stringKey = item.getKey();
        button.setText(buttonText);


        //button add listener is needed to fill
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buttonText.equals(Commands.Button_addFriend.getCommand()))
                    requestORremoveFriend(stringKey, usermail, Commands.Friends_Request.getCommand());
                else if(buttonText.equals(Commands.Button_removeFriend.getCommand()))
                    requestORremoveFriend(stringKey, usermail, Commands.Friends_Remove.getCommand());
                else if(buttonText.equals(Commands.Group_Remove.getCommand()))
                    removeGroup(stringKey);


                listItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void removeGroup(String stringKey){
        Groups g = new Groups();
        Data d = new Data();
        d.setCommand(Commands.Group_Remove.getCommand());
        d.setStringKey(stringKey);
        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result){

                super.onPostExecute(result);

                String response = result.getResponseMessages();
            }
        }.execute(new Pair<Data, Groups>(d, g));
    }

    public void requestORremoveFriend(String stringKey, final String requestMail, final String command){

        new infoCollectorEndpointCommunicator(){
            @Override
            protected void onPostExecute(InfoCollector result){

                super.onPostExecute(result);

                UserinfoModel userdata = result.getUserdata();
                String mail = userdata.getMail();

                Friends friendData = new Friends();
                friendData.setMail1(requestMail);
                friendData.setMail2(mail);

                Data d = new Data();
                d.setCommand(command);

                finalizeRequest(d, friendData);
            }
        }.execute(new String(stringKey));

    }

    public void finalizeRequest(Data d, Friends f){
        new FriendsEndpointCommunicator(){
            @Override
            protected void onPostExecute(FriendsEndpointReturnData returnData){

                super.onPostExecute(returnData);


            }
        }.execute(new Pair<Data, Friends>(d, f));

    }

}


