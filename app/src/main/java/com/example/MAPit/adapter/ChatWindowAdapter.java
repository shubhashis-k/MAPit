package com.example.MAPit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.MAPit.MAPit.ImageConverter;
import com.example.MAPit.MAPit.R;
import com.example.MAPit.Volley.data.StatusListItem;
import com.example.MAPit.model.ChatInfo;

import java.util.List;

/**
 * Created by SETU on 5/3/2015.
 */
public class ChatWindowAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ChatInfo> chatListItems;
    LinearLayout wrapper;

    public ChatWindowAdapter(Activity activity, List<ChatInfo> chatListItems) {
        this.activity = activity;
        this.chatListItems = chatListItems;
    }

    @Override
    public int getCount() {
        return chatListItems.size();
    }

    @Override
    public Object getItem(int location) {
        return chatListItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //if (inflater == null)
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //if (convertView == null)
        convertView = inflater.inflate(R.layout.single_chat_row, null);

        wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
        TextView chat_text = (TextView) convertView.findViewById(R.id.chat_text);
        TextView chat_time = (TextView) convertView.findViewById(R.id.chat_time);

        ChatInfo chatInfo = chatListItems.get(position);
        chat_text.setText(chatInfo.getChat_text());
        chat_time.setText(chatInfo.getChat_time());
        if (chatInfo.getDirection().equals("left")){
            chat_text.setBackgroundResource(R.drawable.bubble_green);
            wrapper.setGravity(Gravity.LEFT);
        }else{
            chat_text.setBackgroundResource(R.drawable.bubble_yellow);
            wrapper.setGravity(Gravity.RIGHT);
        }

        return convertView;
    }
}
