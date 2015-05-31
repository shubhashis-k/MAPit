package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.GCMEndpointReturnData;
import com.example.MAPit.adapter.ChatWindowAdapter;
import com.example.MAPit.model.ChatInfo;
import com.google.api.client.util.DateTime;
import com.mapit.backend.chatSessionApi.model.ChatSession;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by SETU on 5/3/2015.
 */
public class ChatFragment extends Fragment {

    String incoming_msg;
    //ChatBroadCastReceiver mReceiver;
    private EditText et_chat;
    Bundle mailData;
    String caller_mail;
    private ListView chatListview;
    private ChatWindowAdapter chatWindowAdapter;
    private List<ChatSession> PreviousChatSession;
    private List<ChatInfo> chatListItems;
    ImageView chat_send;

    public ChatFragment() {
          setHasOptionsMenu(true);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("chat", "I am in BroadCastReceiver");
            incoming_msg = intent.getStringExtra("msg");
            //Log.i("chat",msg);
            String sender_mail = incoming_msg.substring(0, incoming_msg.indexOf(" "));
            String message = incoming_msg.substring(incoming_msg.indexOf(" ") + 1);
            if (sender_mail.equals(caller_mail)) {
                Log.i("chat", "Inside updateChatWindow");
                updateChatWindow(message);
                startNotificationSound();
            }

            //here if sender mail not equals caller mail then start a notification
            // or i can have a variable in the slidingdraweractivity and check for chatfragment on


        }
    };

    private void startNotificationSound() {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
            r.play();
        } catch (Exception e) {
            try {
                Toast.makeText(getActivity(), "Something with Internet.Leaving..", Toast.LENGTH_SHORT).show();
            }catch (Exception el){

            }
        }

    }

    private void updateChatWindow(String message) {

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setChat_text(message);
        chatInfo.setDirection("left");

        DateConverter dc = new DateConverter();
        String stringDate = dc.DateToString(new Date());
        ArrayList<String> formatted = dc.MobileFriendly(stringDate);

        chatInfo.setChat_date(formatted);

        chatListItems.add(chatInfo);
        chatWindowAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // mReceiver = new ChatBroadCastReceiver();
        //getActivity().registerReceiver(new ChatBroadCastReceiver(),new IntentFilter("chatupdater"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter("chatupdater"));
        Log.i("chat", "I am in onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_window, null, false);
        et_chat = (EditText) v.findViewById(R.id.et_chat);
        chat_send = (ImageView) v.findViewById(R.id.bt_chat_send);
        chatListview = (ListView) v.findViewById(R.id.chat_listView);
        chatListItems = new ArrayList<ChatInfo>();
        chatWindowAdapter = new ChatWindowAdapter(getActivity(), chatListItems);
        chatListview.setAdapter(chatWindowAdapter);
        mailData = getArguments();
        caller_mail = mailData.getString(PropertyNames.Userinfo_Mail.getProperty());

        retrieveChats();

        chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setChat_text(et_chat.getText().toString());
                chatInfo.setDirection("right");

                DateConverter dc = new DateConverter();
                String stringDate = dc.DateToString(new Date());
                ArrayList<String> formatted = dc.MobileFriendly(stringDate);

                chatInfo.setChat_date(formatted);

                chatListItems.add(chatInfo);
                chatWindowAdapter.notifyDataSetChanged();

                fetchID(caller_mail);

            }
        });

        return v;
    }


    public void fetchID(String mail) {
        Data d = new Data();
        d.setUsermail(mail);
        d.setCommand(Commands.GCM_getRegID.getCommand());

        new GCMRegIDCheckerEndpointCommunicator() {
            @Override
            protected void onPostExecute(GCMEndpointReturnData result) {

                super.onPostExecute(result);

                insertMsg(result.getRegID());
            }
        }.execute(d);
    }

    public void insertMsg(String DestinationID) {

        Data d = new Data();
        d.setCommand(Commands.ChatSession_insert.getCommand());
        d.setExtra(DestinationID);

        String sessionName = null;
        if (caller_mail.compareTo(getMymail()) < 0) {
            sessionName = caller_mail + getMymail();
        } else //greater
        {
            sessionName = getMymail() + caller_mail;
        }

        d.setStringKey(sessionName);
        d.setExtramsg(getMymail() + " " + et_chat.getText().toString());
        d.setUsername(getMyName());

        Date now = new Date();
        DateConverter dc = new DateConverter();
        String stringDate = dc.DateToString(now);

        d.setDateInfo(stringDate);
        try {
            new ChatSessionEndpointCommunicator().execute(d);
        }catch (Exception e){
            //Toast.makeText(getActivity(),"Problem with Internet",Toast.LENGTH_SHORT).show();
        }
        et_chat.setText("");
    }


    public String getMymail() {
        Bundle mailBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String mail = mailBundle.getString(PropertyNames.Userinfo_Mail.getProperty());
        return mail;
    }

    public String getMyName() {
        Bundle nameBundle = ((SlidingDrawerActivity) getActivity()).getEmail();
        String name = nameBundle.getString(PropertyNames.Userinfo_Username.getProperty());
        return name;
    }

    private void retrieveChats() {
        String sessionName = null;
        if (caller_mail.compareTo(getMymail()) < 0) {
            sessionName = caller_mail + getMymail();
        } else //greater
        {
            sessionName = getMymail() + caller_mail;
        }

        Data d = new Data();
        d.setUsername(getMyName());
        d.setCommand(Commands.ChatSession_fetch.getCommand());
        d.setStringKey(sessionName);

        new ChatSessionEndpointCommunicator() {
            @Override
            protected void onPostExecute(List<ChatSession> result) {

                super.onPostExecute(result);
                PreviousChatSession = result;
                try {
                    populateChats(PreviousChatSession);
                }catch (Exception e){
                   // Toast.makeText(getActivity(),"Internet Problem",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(d);
    }

    private void populateChats(List<ChatSession> previousChatSession) {
        for (int i = 0; i < previousChatSession.size(); i++) {
            ChatSession c = previousChatSession.get(i);
            ChatInfo chatInfo = new ChatInfo();
            String incoming_msg = c.getMsg();
            String message = incoming_msg.substring(incoming_msg.indexOf(" ") + 1);
            chatInfo.setChat_text(message);


            try {

                DateConverter dc = new DateConverter();
                ArrayList<String> formatted = dc.MobileFriendly(c.getDate());
                chatInfo.setChat_date(formatted);
            } catch (Exception e) {
                //Toast.makeText(getActivity(),"Internet Problem",Toast.LENGTH_SHORT).show();
            }

            if (c.getNameofPerson().equals(getMyName())) {
                chatInfo.setDirection("right");
            } else {
                chatInfo.setDirection("left");
            }
            chatListItems.add(chatInfo);
        }
        chatWindowAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
