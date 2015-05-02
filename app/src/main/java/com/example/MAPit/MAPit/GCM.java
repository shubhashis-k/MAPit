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


public class GCM extends ActionBarActivity {
    public String regID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        final Context c = this;
        Button b = (Button) findViewById(R.id.checkbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = ((EditText) findViewById(R.id.mailText)).getText().toString();
                Check(mail, c);
            }
        });

    }

    public void Check(String mail, Context c){
        final String CheckMail = mail;
        final Context mainContext = c;

        Data d = new Data();
        d.setUsermail(mail);
        d.setCommand(Commands.GCM_getRegID.getCommand());

        new GCMRegIDCheckerEndpointCommunicator(){
            @Override
            protected void onPostExecute(GCMEndpointReturnData result){

                super.onPostExecute(result);
                if(result.getRegID() != null)
                {
                    Toast.makeText(mainContext, "Already Registered",Toast.LENGTH_LONG).show();
                }
                else
                {
                    GenerateAndRegister(CheckMail);
                }
            }
        }.execute(d);
    }

    public void GenerateAndRegister(String mail){
        regID = null;

        final Data d = new Data();
        d.setUsermail(mail);
        d.setCommand(Commands.GCM_setRegID.getCommand());
        final Context c = this;


            new GcmRegistrationAsyncTask() {
                @Override
                protected void onPostExecute(String msg) {
                    regID = msg;
                    Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
                    d.setExtra(msg);
                    new GCMRegIDCheckerEndpointCommunicator().execute(d);
                }
            }.execute(this);

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
