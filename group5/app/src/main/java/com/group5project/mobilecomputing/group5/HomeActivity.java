package com.group5project.mobilecomputing.group5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    int backButtonCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * Back button listener.
     * https://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
     */

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            finish();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this, "Please press the back button again to exit the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

}
