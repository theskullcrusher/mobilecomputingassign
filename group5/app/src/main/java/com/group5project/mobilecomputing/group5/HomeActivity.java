package com.group5project.mobilecomputing.group5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    //more than one press exits the app and finishes the activity
    int backButtonCount = 0;

    private LineGraphSeries<DataPoint> series1;
    private boolean run_flag = false;
    private static final Random RANDOM = new Random();
    private double lastX = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button run = (Button) findViewById(R.id.run);
        Button stop = (Button) findViewById(R.id.stop);

        double x,y;
        x = 0.0;
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series1 = new LineGraphSeries<DataPoint>();
        graph.addSeries(series1);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0.0);
        viewport.setMaxY(1.0);
        viewport.setMinX(0.0);
        viewport.setMaxX(100.0);
        viewport.setScalable(true);
        viewport.setScrollable(true);

//        int numPoints = 15000;
//
//        for(int i=0;i<numPoints;i++){
//            x = x + 0.1;
//            y = Math.sin(x);
//            series1.appendData(new DataPoint(x,y), true, 1000);
//        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<1000;i++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateGraph();
                        }
                    });
                    //Sleep for short time to show updates on screen
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * Write a method with a flag to keep adding data to graph with run is called.
     * Turn flag to false to stop adding any data
     */
    public void updateGraph(){
        double lastY = Math.sin(lastX);
        series1.appendData(new DataPoint(lastX, lastY), true, 1000);
        lastX+=0.1;
    }



//    public void nullifyBackPress() throws InterruptedException {
//        TimeUnit.SECONDS.sleep(5);
//        backButtonCount--;
//    }


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
