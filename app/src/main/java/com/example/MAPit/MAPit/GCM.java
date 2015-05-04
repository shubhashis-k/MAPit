package com.example.MAPit.MAPit;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.Commands;
import com.example.MAPit.Data_and_Return_Data.Data;
import com.example.MAPit.Data_and_Return_Data.GCMEndpointReturnData;
import com.google.common.collect.Iterables;
import com.mapit.backend.chatSessionApi.model.ChatSession;

import java.util.List;


public class GCM extends ActionBarActivity {
    public String regID, DestinationID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        final Context c = this;
        Button registerButton = (Button) findViewById(R.id.checkbutton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = ((EditText) findViewById(R.id.mailText)).getText().toString();
                //Check(mail, c);
            }
        });

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = ((EditText) findViewById(R.id.mailField)).getText().toString();

                fetchID(mail);
            }
        });
    }
    public void Send(Data d){

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

    public void insertMsg(String DestinationID){
        String mail = ((EditText) findViewById(R.id.mailField)).getText().toString();
        String msg = ((EditText)findViewById(R.id.msgField)).getText().toString();

        Data d = new Data();
        d.setCommand(Commands.ChatSession_insert.getCommand());
        d.setExtra(DestinationID);
        d.setStringKey(mail + "monty");
        d.setExtramsg(msg);
        d.setUsername(mail);

        new ChatSessionEndpointCommunicator().execute(d);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gcm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
