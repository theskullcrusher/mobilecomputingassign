package com.group5project.mobilecomputing.group5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    //more than one press exits the app and finishes the activity
    int backButtonCount = 0;

    private LineGraphSeries<DataPoint> series1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        double x,y;
        x = 0;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series1 = new LineGraphSeries<DataPoint>();

        int numPoints = 500;

        for(int i=0;i<numPoints;i++){
            x = x + 0.1;
            y = Math.sin(x);
            series1.appendData(new DataPoint(x,y), true, 100);
        }
        graph.addSeries(series1);
    }

    /**
     * Back button listener.
     * https://stackoverflow.com/questions/2354336/android-pressing-back-button-should-exit-the-app
     */


    public void nullifyBackPress() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        backButtonCount--;
    }

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
            try {
                nullifyBackPress();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
