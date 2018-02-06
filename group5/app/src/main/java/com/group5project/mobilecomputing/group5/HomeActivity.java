package com.group5project.mobilecomputing.group5;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    //more than one press of back button exits the app and finishes the activity
    int backButtonCount = 0;

    private LineGraphSeries<DataPoint> series1;
    GraphView graph;
    private DataPoint[] data;
    private boolean run_flag = false;
    private boolean clear_data = false;
    private static final Random RANDOM = new Random();
    private int SPIKE_PERIOD = 20;
    private double SPIKE_AMPLITUDE = 40.0;
    private double lastX = 0.0;
    private Button run;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        run = (Button) findViewById(R.id.run);
        stop = (Button) findViewById(R.id.stop);

        double x,y;
        x = 0.0;
        graph = (GraphView) findViewById(R.id.graph);
        series1 = new LineGraphSeries<DataPoint>();
        graph.addSeries(series1);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0.0);
        viewport.setMaxY(50.0);
        viewport.setMinX(0.0);
        viewport.setMaxX(100.0);
        viewport.setScalable(true);
        viewport.setScrollable(true);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run_flag = true;
                clear_data = false;
                //adds the series to the graph whenever run is pressed; shows the updated graph on clicking run after clear
                graph.addSeries(series1);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run_flag = false;
                clear_data = true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //keeps updating and drawing the graph when run button is pressed
                    while (run_flag) {
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
                    /*
                     *clears the graph when stop button is pressed
                     * https://stackoverflow.com/questions/15913017/graphview-and-resetdata
                     */
                    if(clear_data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                graph.removeAllSeries();
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
            }
        }).start();
    }

    /**
     * Write a method with a flag to keep adding data to graph with run is called.
     * Turn flag to false to stop adding any data
     */
    public void updateGraph(){
        /**
         * To simulate ECG type of graph
         * https://stackoverflow.com/questions/23167879/java-how-to-simulate-an-ecg-electro-cardiogram
         */
        double lastY = RANDOM.nextDouble() * 5d;
        if (lastX % SPIKE_PERIOD == 0) {
            lastY = SPIKE_AMPLITUDE;
        }
        series1.appendData(new DataPoint(lastX, lastY), true, 1000);
        lastX += 2;
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
