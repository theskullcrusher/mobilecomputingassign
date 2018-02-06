package com.group5project.mobilecomputing.group5;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.srinija.group5.R;

public class MainActivity extends AppCompatActivity {
    private static int SPLASHSCREENTIMEOUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                //displays the splash screen for 4000ms before switching to HomeActivity
                finish();
            }
        },SPLASHSCREENTIMEOUT);
    }
}
