package com.example.MAPit.Volley.adapter;

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

import java.util.List;

/**
 * Created by SETU on 1/23/2015.
 */
public class Friend_SearchList_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Friend_Search_ListItem> frndlistItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public Friend_SearchList_Adapter(Activity activity, List<Friend_Search_ListItem> frndlistItems) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.frnd_search_list_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.frnd_name);
        TextView location = (TextView) convertView.findViewById(R.id.frnd_location);
        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
        final Button addfrnd = (Button) convertView.findViewById(R.id.bt_add_frnd);

        Friend_Search_ListItem item = frndlistItems.get(position);

        name.setText(item.getUser_Name());
        profilePic.setImageUrl(item.getUser_Imge(), imageLoader);
        location.setText(item.getUser_location());

        //button add listener is needed to fill
        addfrnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addfrnd.setText("Request Sent");
                addfrnd.setEnabled(false);
            }
        });

        return convertView;
    }

}

