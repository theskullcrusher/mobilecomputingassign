package com.group5project.mobilecomputing.group5;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import android.util.Log;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HomeActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,SensorEventListener {

    //more than one press of back button exits the app and finishes the activity
    int backButtonCount = 0;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 8503;
    private LineGraphSeries<DataPoint> series1, series2, series3,series4,series5,series6;
    GraphView graph;
    private DataPoint[] data;
    private boolean run_flag = false;
    private boolean clear_data = false;
    private static final Random RANDOM = new Random();
    private int SPIKE_PERIOD = 20;
    private double SPIKE_AMPLITUDE = 40.0;
    private double lastX = 0.0;
    private double lastXd = 0.0;
    private Button run;
    private Button stop;
    private Button save;
    private Button upload;
    private boolean uploadClicked = false;
    private Button download;
    private RadioButton maleButton;
    private RadioButton femaleButton;
    private EditText pid, age, pname;
    private RadioGroup rg;
    private Handler handler = new Handler();
    private boolean Download_Flag;
    private List<XYZvalues> values;
    private String str1,str2,str3,str4;
    private String Table_Name;
    private boolean DownloadFinish = false;
    private boolean saveClicked = false;

    //SQLiteDatabase Db;
    Timestamp time;
    TimerTask timertask;
    Timer timer;
    MyDatabase Db1;
    MyDatabase Db2;
    private boolean DataBaseFlag;

    //All sensor related code is added with reference to this tutorial https://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    public static final String TAG1= HomeActivity.class.getCanonicalName();
    public static final String DbName= Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_Assignment2/Group5db.db";
    public static final String DbName2  = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/Android/Data/CSE535_ASSIGNMENT2_DOWN/Group5db.db";
    public static final int TOAST = 3;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    //permissions code from stackoverflow
    private void storagePermissionCheck() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
        upload = (Button)findViewById(R.id.upload);
        download = (Button)findViewById(R.id.download);

        pid = (EditText) findViewById(R.id.editText1);
        age = (EditText) findViewById(R.id.editText2);
        pname = (EditText) findViewById(R.id.editText3);
        rg = (RadioGroup) findViewById(R.id.rg1);
        maleButton = (RadioButton)findViewById(R.id.radioButton1);
        femaleButton = (RadioButton)findViewById(R.id.radioButton2);


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
        series4 = new LineGraphSeries<DataPoint>();
        series5 = new LineGraphSeries<DataPoint>();
        series6 = new LineGraphSeries<DataPoint>();
        series4.setTitle("X Axis");
        series4.setColor(Color.RED);
        series5.setTitle("Y Axis");
        series5.setColor(Color.GREEN);
        series6.setTitle("Z Axis");
        series6.setColor(Color.BLUE);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0.0);
        viewport.setMaxY(20.0);
        viewport.setMinX(0.0);
        viewport.setMaxX(10.0);
        viewport.setScalable(true);
        viewport.setScrollable(true);

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run_flag = true;
                clear_data = false;
                //to remove to downloaded part graph if there and show the original graph
                //adds the series to the graph whenever run is pressed; shows the updated graph on clicking run after clear
                    graph.removeAllSeries();
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
                saveClicked = true;
                Download_Flag = false;
                boolean flag = validatePatientInput(pid, age, pname, rg);
                str1 = pname.getText().toString().replaceAll("[^a-zA-Z0-9]","");
                str2 = pid.getText().toString().replaceAll("[^a-zA-Z0-9]","");
                str3 = age.getText().toString().replaceAll("[^a-zA-Z0-9]","");
                str4 = (maleButton.isChecked()?"Male":"Female");
                if(flag == true) {
                    Db1 = new MyDatabase(getApplicationContext()); // creating database
                    Db1.data(str1,str2,str3,str4); // create database table
                    DataBaseFlag = true;
                    Toast.makeText(getApplicationContext(),"Successfully started recording.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter all patient data and hit save to start recording values",Toast.LENGTH_LONG).show();
                }

            }
        });

        /*
         * All upload and download related code is added with reference to these examples:
         * https://www.codepuppet.com/2013/03/26/android-uploading-a-file-to-a-php-server/
         * https://gist.github.com/Kieranties/2225346
         */
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveClicked) {
                    /*only if some data has been saved, it allows upload*/
                    boolean flag = validatePatientInput(pid, age, pname, rg);
                    if (flag == true) {
                        uploadClicked = true;
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    /*sends HTTP requests and gets responses from a server identified a URL*/
                                    HttpClient hc = new DefaultHttpClient();
                                    /*requests the server to accept the enclosed entity to be part of it*/
                                    HttpPost hp = new HttpPost("http://impact.asu.edu/CSE535Spring18Folder/UploadToServer.php");
                                    File f = new File(DbName);
                                    FileBody fb = new FileBody(f);
                                    MultipartEntity mp = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                                    mp.addPart("uploaded_file", fb);
                                    hp.setEntity(mp);
                                    HttpResponse hr = hc.execute(hp);
                                    HttpEntity he = hr.getEntity();

                                    if (hr != null) {
                                        Log.d(TAG1, "Response received");
                                        Log.d(TAG1, hr.getStatusLine().getReasonPhrase() + " " + hr.getStatusLine().getStatusCode());
                                        String messageText = EntityUtils.toString(he);
                                        Log.d(TAG1, messageText);
                                        handler.sendMessage(handler.obtainMessage(TOAST, "Response: " + messageText));
                                    } else {
                                        handler.sendMessage(handler.obtainMessage(TOAST, "Response not received"));
                                        Log.d(TAG1, "Response not received");
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d(TAG1, "Exception caught");
                                }
                            }
                        }).start();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please enter patient data first", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please save the data first", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_LONG).show();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadClicked){
                    /*only if upload is done, it allows download*/
                    boolean flag = validatePatientInput(pid, age, pname, rg);
                    if(flag == true) {
                        Download_Flag = true;
                        new Thread(new Runnable() {
                        public void run() {
                            try {
                                HttpClient hc = new DefaultHttpClient();
                                HttpGet hg = new HttpGet("http://impact.asu.edu/CSE535Spring18Folder/Group5db.db");
                                HttpResponse hr = hc.execute(hg);
                                HttpEntity he = hr.getEntity();

                                if (he != null) {
                                    File downloadPath = new File(Environment.getExternalStorageDirectory() + "/Android/Data/CSE535_ASSIGNMENT2_DOWN/");
                                    if (!downloadPath.exists())
                                        downloadPath.mkdirs();
                                    FileOutputStream fos = new FileOutputStream(new File(downloadPath, "Group5db.db"));
                                    he.writeTo(fos);
                                    fos.close();
                                    handler.sendMessage(handler.obtainMessage(TOAST, "Downloaded successfully!"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(TAG1, "Exception caught");
                            }
                            Db2 = new MyDatabase(getApplicationContext(),DbName2);
                            Table_Name = str1+"_"+str2+"_"+str3+"_"+str4;
                            values = Db2.readData(Table_Name);
                            Log.d(TAG1,"Reading data from downloaded database");
                            DownloadFinish = true;
                            graph.removeAllSeries();
                            graph.addSeries(series4);
                            graph.addSeries(series5);
                            graph.addSeries(series6);
                            /*only after downloading the database, the last 10 records are plotted*/
                            if(DownloadFinish) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Successfully Downloaded Database",Toast.LENGTH_LONG).show();
                                        updateDownloadGraph(values);
                                        run_flag = false;
                                        DownloadFinish = false;
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
                else {
                        Toast.makeText(getApplicationContext(), "Please enter patient data first", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Upload first", Toast.LENGTH_LONG).show();

                }
            }
        });

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }
    //https://examples.javacodegeeks.com/android/core/activity/android-timertask-example/

    public boolean validatePatientInput(EditText pid, EditText age, EditText pname, RadioGroup rg){
        /*checks if all the patient data is correctly entered in the required data format*/
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
                     * clears the graph when stop button is pressed
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

    /* This function takes the updated accelerometer values and displays on the graph */
    public void updateGraph(){
        series1.appendData(new DataPoint(lastX, last_x), true, 1000);
        series2.appendData(new DataPoint(lastX, last_y), true, 1000);
        series3.appendData(new DataPoint(lastX, last_z), true, 1000);
        lastX += 1;
    }

    /* This function takes the values for the last ten records of downloaded database and displays on the graph */
    public void updateDownloadGraph(List<XYZvalues> values1){
        for(int i = 0; i < values1.size();i++) {
            series4.appendData(new DataPoint(lastXd, values.get(i).x_value), true, 1000);
            series5.appendData(new DataPoint(lastXd, values.get(i).y_value), true, 1000);
            series6.appendData(new DataPoint(lastXd, values.get(i).z_value), true, 1000);
            lastXd += 1;
        }
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

            /*get the data for every 1 second*/
            if ((curTime - lastUpdate) > 995) {
                if (DataBaseFlag == true)
                Db1.AddData(curTime,x,y,z,str1,str2,str3,str4);
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
