package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

//This is my first activity
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        TextView signin = (TextView) findViewById(R.id.signin);
        TextView signup = (TextView) findViewById(R.id.signup);

        //must return true otherwise it will take many backbutton press to come back to the previous activity
        signup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent signupintent = new Intent(MainActivity.this,HomeMapActivity.class);
                startActivity(signupintent);
                return false;

            }
        });
        signin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent signinintent = new Intent(MainActivity.this,SignIn.class);
                startActivity(signinintent);
                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        //this must have to give to quit application
      moveTaskToBack(true);
    }
}
