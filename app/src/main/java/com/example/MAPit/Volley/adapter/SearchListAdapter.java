package com.example.MAPit.Volley.adapter;

/**
 * Created by SETU on 1/24/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.FriendsEndpointReturnData;
import com.example.MAPit.Data_and_Return_Data.GroupsEndpointReturnData;
import com.example.MAPit.MAPit.FriendsEndpointCommunicator;
import com.example.MAPit.MAPit.GroupsEndpointCommunicator;
import com.example.MAPit.MAPit.ImageConverter;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.MAPit.SlidingDrawerActivity;
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
        ImageView profilePic = (ImageView) convertView.findViewById(R.id.myfrnd_profilePic);

        SearchListItem item = listItems.get(position);


        name.setText(item.getName());
        location.setText(item.getLocation());
        if(item.getImage()!=null){
            profilePic.setImageBitmap(ImageConverter.stringToimageConverter(item.getImage()));
        }else{
            profilePic.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_profile));
        }

        final String buttonText = item.getButton();
        final String RequestUsermail = item.getExtra();
        final String stringKey = item.getKey();
        button.setText(buttonText);


        //button add listener is needed to fill
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buttonText.equals(Commands.Button_addFriend.getCommand())) {
                    requestORremoveFriend(RequestUsermail, Commands.Friends_Request.getCommand());
                    //here i have to to increment the counter of Friend Request menu in slidingDrawer
                }
                else if(buttonText.equals(Commands.Button_removeFriend.getCommand()))
                    requestORremoveFriend(RequestUsermail, Commands.Friends_Remove.getCommand());
                else if(buttonText.equals(Commands.Group_Remove.getCommand()))
                    removeGroup(stringKey);
                else if(buttonText.equals(Commands.Group_Join_Group.getCommand()))
                    JoinGroup(stringKey, getmail());
                else if(buttonText.equals(Commands.Leave_Group.getCommand()))
                    LeaveGroup(stringKey, getmail());

                listItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    public void LeaveGroup(String stringKey, String mail){
        Groups g = new Groups();
        Data d = new Data();
        d.setCommand(Commands.Leave_Group.getCommand());
        d.setStringKey(stringKey);
        d.setUsermail(mail);

        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result){

                super.onPostExecute(result);

                String response = result.getResponseMessages();
            }
        }.execute(new Pair<Data, Groups>(d, g));
    }

    public void JoinGroup(String stringKey, String mail){
        Groups g = new Groups();
        Data d = new Data();
        d.setCommand(Commands.Request_Group.getCommand());
        d.setStringKey(stringKey);
        d.setUsermail(mail);

        new GroupsEndpointCommunicator(){
            @Override
            protected void onPostExecute(GroupsEndpointReturnData result){

                super.onPostExecute(result);

                String response = result.getResponseMessages();
            }
        }.execute(new Pair<Data, Groups>(d, g));
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

    public void requestORremoveFriend(final String requestUserMail, final String command){


                Friends friendData = new Friends();
                friendData.setMail1(requestUserMail);
                friendData.setMail2(getmail());

                Data d = new Data();
                d.setCommand(command);

                finalizeRequest(d, friendData);

    }

    public void finalizeRequest(Data d, Friends f){
        new FriendsEndpointCommunicator(){
            @Override
            protected void onPostExecute(FriendsEndpointReturnData returnData){

                super.onPostExecute(returnData);


            }
        }.execute(new Pair<Data, Friends>(d, f));

    }

    public String getmail(){
        Bundle mailBundle = ((SlidingDrawerActivity)activity).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }
}


