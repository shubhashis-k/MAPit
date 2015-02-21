package com.example.MAPit.MAPit;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapit.backend.userinfoModelApi.model.ResponseMessages;
import com.mapit.backend.userinfoModelApi.model.UserinfoModel;

/**
 * Created by SETU on 12/26/2014.
 */
public class SignUp extends Activity implements SignUp_Endpoint_Communicator.manipulate_Signup{
    private String signup_name, signup_email, signup_password,signup_phonenumber;
    private EditText infoCollector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Button signupButton = (Button) findViewById(R.id.signup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformation();
                if(signup_name.equals("") || signup_email.equals("") || signup_password.equals("") || signup_phonenumber.equals("")){

                    Toast.makeText(getApplication(),"Enter all the Info",Toast.LENGTH_LONG).show();
                }
                else {
                    registerInformation();
                }
            }
        });
    }

    public void getInformation()
    {
        infoCollector = (EditText) findViewById(R.id.signup_name);
        signup_name = infoCollector.getText().toString();

        infoCollector = (EditText) findViewById(R.id.signup_email);
        signup_email = infoCollector.getText().toString();

        infoCollector = (EditText) findViewById(R.id.signup_password);
        signup_password = infoCollector.getText().toString();

        infoCollector = (EditText) findViewById(R.id.signup_phone);
        signup_phonenumber = infoCollector.getText().toString();
    }

    public void registerInformation()
    {
        Bundle data = getIntent().getBundleExtra("From HomeMapActivity");
        String lat =String.valueOf(data.getDouble("latitude")) ;
        String lng = String.valueOf(data.getDouble("longitude"));
        UserinfoModel userinformation = new UserinfoModel();
        userinformation.setName(signup_name);
        userinformation.setMail(signup_email);
        userinformation.setMobilephone(signup_phonenumber);
        userinformation.setPassword(signup_password);
        userinformation.setLatitude(lat);
        userinformation.setLongitude(lng);

        new SignUp_Endpoint_Communicator().execute(new Pair<Context, UserinfoModel>(this, userinformation));
    }


    @Override
    public void setResponseMessage(ResponseMessages response) {
        String res = response.getMessage();

        if(res.equals("OK"))
        {
            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SignUp.this,MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Duplicate Email!", Toast.LENGTH_LONG).show();
        }

    }
}

