package com.example.MAPit.MAPit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

//This is my first activity
public class MainActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        TextView signin = (TextView) findViewById(R.id.signin);
        TextView signup = (TextView) findViewById(R.id.signup);
        TextView mapit = (TextView) findViewById(R.id.st_mapit);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/freestylefont.ttf");
        mapit.setTypeface(typeFace);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.slidein_top);
        mapit.startAnimation(animation);
        Animation ani1= AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
        Animation ani2=AnimationUtils.loadAnimation(this,R.anim.slide_in_right);
        signin.startAnimation(ani2);
        signup.startAnimation(ani1);

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
