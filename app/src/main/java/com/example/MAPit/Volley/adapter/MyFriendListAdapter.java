package com.example.MAPit.Volley.adapter;

/**
 * Created by SETU on 1/24/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.app.AppController;
import com.example.MAPit.Volley.data.Friend_Search_ListItem;
import com.example.MAPit.Volley.data.MyFriendsItem;

import java.util.List;

public class MyFriendListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MyFriendsItem> myfrndlistItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MyFriendListAdapter(Activity activity, List<MyFriendsItem> myfrndlistItems) {
        this.activity = activity;
        this.myfrndlistItems = myfrndlistItems;
    }

    @Override
    public int getCount() {
        return myfrndlistItems.size();
    }

    @Override
    public Object getItem(int location) {
        return myfrndlistItems.get(location);
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
            convertView = inflater.inflate(R.layout.myfriend_list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.my_frnd_name);
        TextView location = (TextView) convertView.findViewById(R.id.my_frnd_location);
        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.myfrnd_profilePic);
        Button deletefrnd = (Button) convertView.findViewById(R.id.bt_delete_frnd);

        MyFriendsItem item = myfrndlistItems.get(position);

        name.setText(item.getUser_Name());
        profilePic.setImageUrl(item.getUser_Imge(), imageLoader);
        location.setText(item.getUser_location());

        //button add listener is needed to fill
        deletefrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Integer index = (Integer) v.getTag();
                myfrndlistItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}


