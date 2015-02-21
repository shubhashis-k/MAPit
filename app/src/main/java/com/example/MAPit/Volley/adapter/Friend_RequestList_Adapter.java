package com.example.MAPit.Volley.adapter;

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
import com.example.MAPit.MAPit.FriendsEndpointCommunicator;
import com.example.MAPit.MAPit.ImageConverter;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.MAPit.SlidingDrawerActivity;
import com.example.MAPit.Volley.data.Friend_Request_ListItem;
import com.mapit.backend.friendsApi.model.Friends;
import com.mapit.backend.friendsApi.model.Search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SETU on 1/23/2015.
 */
public class Friend_RequestList_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Friend_Request_ListItem> frndlistItems;

    public Friend_RequestList_Adapter(Activity activity, List<Friend_Request_ListItem> frndlistItems) {
        this.activity = activity;
        this.frndlistItems = frndlistItems;
    }

    @Override
    public int getCount() {
        return frndlistItems.size();
    }

    @Override
    public Object getItem(int location) {
        return frndlistItems.get(location);
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
            convertView = inflater.inflate(R.layout.frnd_request_list_item, null);


        TextView name = (TextView) convertView.findViewById(R.id.frnd_name);
        TextView location = (TextView) convertView.findViewById(R.id.frnd_location);
        ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilePic);
        final Button acceptRequest = (Button) convertView.findViewById(R.id.bt_add_frnd);
        final Button rejectRequest = (Button) convertView.findViewById(R.id.bt_delete_frnd);

        final Friend_Request_ListItem item = frndlistItems.get(position);

        final String command = item.getButton_type();

        name.setText(item.getUser_Name());
        if(item.getUser_Imge()!=null){
            profilePic.setImageBitmap(ImageConverter.stringToimageConverter(item.getUser_Imge()));
        }else{
            profilePic.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_profile));
        }
        location.setText(item.getUser_location());

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(command.equals(Commands.Friends_Request.getCommand())){
                    Data info = new Data();
                    info.setCommand(Commands.Friends_Make.getCommand());

                    Friends f = new Friends();
                    f.setMail1(getmail());
                    f.setMail2(item.getUsermail());

                    new FriendsEndpointCommunicator(){
                        @Override
                        protected void onPostExecute(FriendsEndpointReturnData result){

                            super.onPostExecute(result);
                        }
                    }.execute(new Pair<Data, Friends>(info, f));
                }
                frndlistItems.remove(position);
                notifyDataSetChanged();
            }
        });
        rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public String getmail(){
        Bundle mailBundle = ((SlidingDrawerActivity)activity).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }


}

