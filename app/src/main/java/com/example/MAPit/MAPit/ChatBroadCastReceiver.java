package com.example.MAPit.MAPit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by SETU on 5/4/2015.
 */
public class ChatBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("chat", "I am in ChatBroadCastReceiver");
        String msg = intent.getStringExtra("msg");
        Log.i("chat", msg);


    }
}
