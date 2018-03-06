package com.group5project.mobilecomputing.group5;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.util.Timer;
import java.util.TimerTask;
import android.database.sqlite.SQLiteOpenHelper;


public class HomeActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,SensorEventListener {

    //more than one press of back button exits the app and finishes the activity
    int backButtonCount = 0;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 8503;
    private LineGraphSeries<DataPoint> series1, series2, series3;
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
    private Button save;
    private EditText pid, age, pname;
    private RadioGroup rg;
    //SQLiteDatabase Db;
    Timestamp time;
    TimerTask timertask;
    Timer timer;
    MyDatabase Db1;
    private boolean DataBaseFlag;

    //All sensor related code is added with reference to this tutorial https://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private void storagePermissionCheck() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else
                    HomeActivity.this.finish();

                return;
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
        storagePermissionCheck();

        run = (Button) findViewById(R.id.run);
        stop = (Button) findViewById(R.id.stop);
        save = (Button) findViewById(R.id.save);

        pid = (EditText) findViewById(R.id.editText1);
        age = (EditText) findViewById(R.id.editText2);
        pname = (EditText) findViewById(R.id.editText3);
        rg = (RadioGroup) findViewById(R.id.rg1);

        double x,y;
        x = 0.0;
        graph = (GraphView) findViewById(R.id.graph);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Accelerometer X-Y-Z axes");
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        series1 = new LineGraphSeries<DataPoint>();
        series2 = new LineGraphSeries<DataPoint>();
        series3 = new LineGraphSeries<DataPoint>();
        series1.setTitle("X Axis");
        series1.setColor(Color.RED);
        series2.setTitle("Y Axis");
        series2.setColor(Color.GREEN);
        series3.setTitle("Z Axis");
        series3.setColor(Color.BLUE);
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
                graph.addSeries(series2);
                graph.addSeries(series3);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run_flag = false;
                clear_data = true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = validatePatientInput(pid, age, pname, rg);
                if(flag == true) {
                    Db1 = new MyDatabase(getApplicationContext()); // creating database
                    Db1.data(); // create database table
                    Db1.ClearData(); // clear previous data, remove if not required
                    DataBaseFlag = true;
                    Toast.makeText(getApplicationContext(),"Successfully started recording. Displaying graph",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter all patient data and hit save to start recording values",Toast.LENGTH_LONG).show();
                }

            }
        });


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

    }
    //https://examples.javacodegeeks.com/android/core/activity/android-timertask-example/

    public boolean validatePatientInput(EditText pid, EditText age, EditText pname, RadioGroup rg){
        final String str1 = pid.getText().toString().trim();
        final String str2 = age.getText().toString().trim();
        final String str3 = pname.getText().toString().trim();
        RadioButton rb = ((RadioButton)findViewById(rg.getCheckedRadioButtonId()));

        if(str1.equals("") || str2.equals("") || str3.equals("") || rb == null){
            return false;
        }
        else{
            return true;
        }
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
                            Thread.sleep(998);
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

        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Write a method with a flag to keep adding data to graph with run is called.
     * Turn flag to false to stop adding any data
     */
/*    public void updateGraph(){
        *//**
     * To simulate ECG type of graph
     * https://stackoverflow.com/questions/23167879/java-how-to-simulate-an-ecg-electro-cardiogram
     *//*
        double lastY = RANDOM.nextDouble() * 5d;
        if (lastX % SPIKE_PERIOD == 0) {
            lastY = SPIKE_AMPLITUDE;
        }
        series1.appendData(new DataPoint(lastX, lastY), true, 1000);
        lastX += 2;
    }*/

    public void updateGraph(){
        /**
         * This function takes the updated accelerometer values and displays on the graph
         *
         */
        series1.appendData(new DataPoint(lastX, last_x), true, 1000);
        series2.appendData(new DataPoint(lastX, last_y), true, 1000);
        series3.appendData(new DataPoint(lastX, last_z), true, 1000);
        lastX += 1;
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            boolean flag = validatePatientInput(pid, age, pname, rg);
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 995 && DataBaseFlag == true) {
                Db1.AddData(curTime,x,y,z);
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

}
