package com.example.MAPit.MAPit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by SETU on 12/27/2014.
 */
public class SignIn extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        Button signin = (Button) findViewById(R.id.userlogin);
        //activity for userlogin
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin_intent = new Intent(SignIn.this,SlidingDrawerActivity.class);
                startActivity(signin_intent);
            }
        });
    }
}

