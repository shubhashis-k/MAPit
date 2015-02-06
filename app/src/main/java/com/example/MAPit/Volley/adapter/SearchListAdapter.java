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

import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.data.Item;

import java.util.List;

public class SearchListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> myfrndlistItems;

    public SearchListAdapter(Activity activity, List<Item> myfrndlistItems) {
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


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        Button deletefrnd = (Button) convertView.findViewById(R.id.command_button);

        Item item = myfrndlistItems.get(position);
        name.setText(item.getName());
        location.setText(item.getLocation());

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


