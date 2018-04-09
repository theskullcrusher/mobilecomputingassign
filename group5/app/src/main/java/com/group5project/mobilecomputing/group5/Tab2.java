package com.group5project.mobilecomputing.group5;

/**
 * Created by srinija on 3/31/18.
 */

import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import umich.cse.yctung.androidlibsvm.LibSVM;


import com.group5project.mobilecomputing.group5.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tab2 extends Fragment {

    private static final String TAG = Tab2.class.getCanonicalName();
    String activity = "Activity";
    private static int i = 0;
    String activityName;
    MyDatabase Db1;
    Activity currentActivity;
    ArrayList<DbReturnObject> db_list;
    String tableName = "Activity_Database";
    private Button run;
    private Button stop;
    private EditText inputPara;
    private String inputstr = "";
    LibSVM svm;
    public static final String appFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_ASSIGNMENT3/";
    public static final String data_file = "training_data";

    // Adopted from https://stackoverflow.com/questions/35481924/write-a-string-to-a-file
    public void writeDataToFile(String data){
        final File file = new File(appFolderPath, data_file);
        try
        {
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }


    }


    // Using the SVM library for android made from libsvm that professor suggested at https://github.com/yctung/AndroidLibSVM
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);
        currentActivity = getActivity();
        run = (Button) rootView.findViewById(R.id.run1);
        stop = (Button) rootView.findViewById(R.id.stop1);
        inputPara = (EditText) rootView.findViewById(R.id.et1);
        final HashMap<Integer,String> hmap = new HashMap<Integer,String>();
        final HashMap<String,Integer> reverse_hmap = new HashMap<String,Integer>();
        Toast.makeText(getActivity().getApplicationContext(), "No Input data found. Please record data in Tab 1 to perform classification on...", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                run.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        run.setClickable(false);
                        inputstr = inputPara.getText().toString().trim();
                        Db1 = new MyDatabase(currentActivity);
                        db_list = Db1.readData(tableName);
                        if (db_list.isEmpty()) {
                            Toast.makeText(getActivity().getApplicationContext(), "No Input data found. Please record data in Tab 1 to perform classification on...", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "Please wait 30sec for training set creation..." +
                                    "Creating training dataset \"training_data\"", Toast.LENGTH_LONG).show();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "Comes to else part");
                            //Use inputstr parameters or default values if nothing is given to perform multiclass svm
                            String data = "";
                            ArrayList<String> class_labels = new ArrayList<String>();
                            for(DbReturnObject each_obj: db_list){
                                String label = each_obj.label;
                                if(!class_labels.contains(label))
                                    class_labels.add(label);
                            }
                            for(int k=0; k < class_labels.size(); k++){
                                hmap.put(k, class_labels.get(k));
                                reverse_hmap.put(class_labels.get(k), k);
                            }
                            for(DbReturnObject each_obj: db_list){
                                String label = each_obj.label;
                                int label1 = reverse_hmap.get(label);
                                data += String.valueOf(label1) + " ";
                                int j = 0;
                                for(XYZvalues each_xyz: each_obj.xyz_list){
                                    data += String.valueOf(j++) + ":" + String.valueOf(each_xyz.x_value) + " ";
                                    data += String.valueOf(j++) + ":" + String.valueOf(each_xyz.y_value) + " ";
                                    data += String.valueOf(j++) + ":" + String.valueOf(each_xyz.z_value) + " ";
                                }
                                data += "\n";
                            }
                            writeDataToFile(data);
                            Log.d(TAG, "Writes to file");
                            Toast.makeText(getActivity().getApplicationContext(), "Successfully created training dataset \"training_data\" in the same sdcard directory as db file", Toast.LENGTH_LONG).show();
                        }
                        run.setClickable(true);
                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

            }
        }).start();

        return rootView;
    }
}
