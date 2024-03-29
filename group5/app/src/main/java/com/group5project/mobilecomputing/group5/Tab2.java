package com.group5project.mobilecomputing.group5;

/**
 * Created by srinija on 3/31/18.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import static com.group5project.mobilecomputing.group5.svm_train.*;
import static com.group5project.mobilecomputing.group5.svm_predict.*;
import java.util.concurrent.TimeUnit;


public class Tab2 extends Fragment{

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
    private Button bt1, tes, trs;
    private EditText inputPara;
    private TextView tv;
    private String inputstr = "";
    public static final String appFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_ASSIGNMENT3/";
    public static final String data_file = "training_data";
    public static final String training_set = appFolderPath + "training_set";
    public static final String testing_set = appFolderPath + "testing_set";
    private String accuracy = "";
    svm_train callbackregister;
    svm_predict predict;

    // Adopted from https://stackoverflow.com/questions/35481924/write-a-string-to-a-file
    public void writeDataToFile(String data) {
        final File file = new File(appFolderPath, data_file);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
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
        stop.setVisibility(View.GONE);
        tv = (TextView) rootView.findViewById(R.id.tv5);
        tv.setVisibility(View.GONE);
        bt1 = (Button) rootView.findViewById(R.id.bt1);
        trs = (Button) rootView.findViewById(R.id.trs);
        tes = (Button) rootView.findViewById(R.id.tes);
        inputPara = (EditText) rootView.findViewById(R.id.et1);
        final HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        final HashMap<String, Integer> reverse_hmap = new HashMap<String, Integer>();
        callbackregister = new svm_train();
        callbackregister.registerCallback((GetAccuracy) getActivity());

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt1.setClickable(false);
                run.setClickable(false);
                tv.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Db1 = new MyDatabase(currentActivity);
                        db_list = Db1.readData(tableName);
                        if (db_list.isEmpty()) {
                            Log.e(TAG, "Empty readdata. This should not happen!");
//                            Toast.makeText(currentActivity.getApplicationContext(), "No Input data found. Please record data in Tab 1 to perform classification on...", Toast.LENGTH_LONG).show();
                        } else {
//                            Toast.makeText(currentActivity.getApplicationContext(), "Please wait 30sec for training set creation..." +
//                                    "Creating training dataset \"training_data\"", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Comes to else part: "+inputstr);
                            //Use inputstr parameters or default values if nothing is given to perform multiclass svm
                            String data = "";
                            ArrayList<String> class_labels = new ArrayList<String>();
                            for (DbReturnObject each_obj : db_list) {
                                String label = each_obj.label;
                                if (!class_labels.contains(label))
                                    class_labels.add(label);
                            }
                            for (int k = 0; k < class_labels.size(); k++) {
                                hmap.put(k, class_labels.get(k));
                                reverse_hmap.put(class_labels.get(k), k);
                            }
                            for (DbReturnObject each_obj : db_list) {
                                String label = each_obj.label;
                                int label1 = reverse_hmap.get(label);
                                data += String.valueOf(label1) + " ";
                                int j = 0;
                                for (XYZvalues each_xyz : each_obj.xyz_list) {
                                    data += String.valueOf(j++) + ":" + String.valueOf(each_xyz.x_value) + " ";
                                    data += String.valueOf(j++) + ":" + String.valueOf(each_xyz.y_value) + " ";
                                    data += String.valueOf(j++) + ":" + String.valueOf(each_xyz.z_value) + " ";
                                }
                                data += "\n";
                            }
                            writeDataToFile(data);
                            Log.d(TAG, "Writes to file");
//                            Toast.makeText(currentActivity.getApplicationContext(), "Successfully created training dataset \"training_data\" in the same sdcard directory as db file", Toast.LENGTH_LONG).show();
                            currentActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    tv.setVisibility(View.VISIBLE);
                                    bt1.setClickable(true);
                                    run.setClickable(true);
                                }
                            });
                        }
                    }
                }).start();

            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt1.setClickable(false);
                run.setClickable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        inputstr = inputPara.getText().toString().trim();

                        if(inputstr.equals(""))
                            inputstr = "-s 1 -g 0.04 -v 5 -t 0 ";
                        else
                            inputstr = inputstr.trim() + " ";
                        String path = appFolderPath + data_file;
                        Log.d(TAG, "Program Input SVM: "+path);
                        String temp = inputstr + path + " ";
                        String[] array = temp.split(" ");
                        try {
                            callbackregister.run(array);
                            TimeUnit.SECONDS.sleep(1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        accuracy = ((HomeActivity)currentActivity).getAccuracyString();
//                        System.out.println("accuracy="+accuracy);
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                bt1.setClickable(true);
                                run.setClickable(true);
                                String tmp = tv.getText().toString();
                                tmp += "\nDefault input used: -s 1 -g 0.04 -v 5 -t 0 ";
                                tmp += "\nSuccessfully run SVM with 5 fold cross-validation accuracy of "+ accuracy + "...";
                                tv.setText(tmp);
                            }
                        });
                    }
                }).start();

            }
        });

        trs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trs.setClickable(false);
                tes.setClickable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String str = "-s 1 -g 0.04 -t 0 ";
                        Log.d(TAG, "Training Input to SVM: "+training_set);
                        String temp = str + training_set + " " + training_set + ".model ";
                        String[] array = temp.split(" ");
                        try {
                            callbackregister.run(array);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                trs.setClickable(true);
                                tes.setClickable(true);
                                Toast.makeText(currentActivity.getApplicationContext(), "Finished Training data on 80% samples", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();

            }
        });

        tes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trs.setClickable(false);
                tes.setClickable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Training Input to SVM: "+testing_set);
                        String temp = "" + testing_set+ " " + training_set + ".model " + appFolderPath + "output";
                        String[] array = temp.split(" ");
                        try {
                            predict.main(array);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                trs.setClickable(true);
                                tes.setClickable(true);
                                Toast.makeText(currentActivity.getApplicationContext(), "Finished Testing data on remaining 20% samples. Please look into \"output\" file in sdcard assign3 folder..", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).start();

            }
        });

        return rootView;
    }
}
