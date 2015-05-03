package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.example.MAPit.adapter.ChatWindowAdapter;
import com.example.MAPit.model.ChatInfo;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by SETU on 5/3/2015.
 */
public class ChatFragment extends Fragment {

    private ListView chatListview;
    private ChatWindowAdapter chatWindowAdapter;
    private List<ChatInfo> chatListItems;
    Button chat_send;
    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_window, null, false);
        chat_send = (Button) v.findViewById(R.id.bt_chat_send);
        chatListview = (ListView)v.findViewById(R.id.chat_listView);
        chatListItems = new ArrayList<ChatInfo>();
        chatWindowAdapter = new ChatWindowAdapter(getActivity(),chatListItems);
        chatListview.setAdapter(chatWindowAdapter);

        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatInfo chatInfo= new ChatInfo();
                chatInfo.setChat_text("how are u");
                chatInfo.setChat_time("Jan 27 8:27 pm");
                chatInfo.setDirection("left");
                chatListItems.add(chatInfo);
                chatWindowAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

}
