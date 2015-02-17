package com.example.MAPit.MAPit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MAPit.Commands_and_Properties.PropertyNames;
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;

/**
 * Created by SETU on 12/27/2014.
 */
public class SignIn extends Activity implements SignIn_Endpoint_Communicator.manipulate_Signin{

    private String retrieved_pass;
    private String input_pass;
    private String input_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        Button signin = (Button) findViewById(R.id.userlogin);
        //activity for userlogin
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getInformation();
                getPassfromDatstore();

            }
        });
    }

    public void getInformation()
    {
        input_mail = ((EditText) findViewById(R.id.signin_email)).getText().toString();
        input_pass = ((EditText) findViewById(R.id.signin_password)).getText().toString();
    }

    public void getPassfromDatstore()
    {
        UserinfoModel userdata = new UserinfoModel();
        userdata.setMail(input_mail);

        new SignIn_Endpoint_Communicator().execute(new Pair<Context, UserinfoModel>(this, userdata));
    }

    @Override
    public void setResponseMessage(UserinfoModel logininfo) {
        retrieved_pass = logininfo.getPassword();

        if(retrieved_pass.equals(input_pass))
        {
            Intent signin_intent = new Intent(SignIn.this,SlidingDrawerActivity.class);
            signin_intent.putExtra(PropertyNames.Userinfo_Mail.getProperty(), input_mail);
            signin_intent.putExtra(PropertyNames.Userinfo_Username.getProperty(), logininfo.getName());

            String imageData = logininfo.getImagedata();
            if(imageData != null)
                signin_intent.putExtra(PropertyNames.Userinfo_Profilepic.getProperty(), logininfo.getImagedata());

            signin_intent.putExtra(PropertyNames.Userinfo_longitude.getProperty(), logininfo.getLongitude());
            signin_intent.putExtra(PropertyNames.Userinfo_latitude.getProperty(), logininfo.getLatitude());
            startActivity(signin_intent);
        }
        else
        {
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show();
        }


    }
}

