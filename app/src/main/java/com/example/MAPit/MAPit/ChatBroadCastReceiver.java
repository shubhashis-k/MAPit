package com.example.MAPit.MAPit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by SETU on 5/4/2015.
 */
public class ChatBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("chat","I am in ChatBroadCastReceiver");
        String msg = intent.getStringExtra("msg");
        Log.i("chat",msg);

        //Intent data = new Intent("chatupdater");
        //Intent data = new Intent("chatupdater");
        //data.putExtra("key","data");
        //data.putInt("fragmentno",1); // Pass the unique id of fragment we talked abt earlier
        //context.sendBroadcast(intent);


    }
}
