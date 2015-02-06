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
import com.example.MAPit.Volley.data.SearchListItem;

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

        //button add listener is needed to fill
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Integer index = (Integer) v.getTag();
                listItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}


