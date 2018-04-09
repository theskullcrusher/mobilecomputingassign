package com.group5project.mobilecomputing.group5;

/**
 * Created by srinija on 3/31/18.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import umich.cse.yctung.androidlibsvm.LibSVM;


import com.group5project.mobilecomputing.group5.R;

import java.util.ArrayList;
import java.util.List;

public class Tab2 extends Fragment{

    private static final String TAG = Tab2.class.getCanonicalName() ;
    private TextView countText1;
    private TextView countText2;
    private TextView countText3;
    String activity = "Activity";
    private static int i=0;
    String activityName;
    MyDatabase Db1;
    ArrayList<DbReturnObject> db_list;
    String tableName = "Activity_Database";

    // Using the SVM library for android made from libsvm that professor suggested at https://github.com/yctung/AndroidLibSVM
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);

        Db1 = new MyDatabase(getActivity());
        db_list = Db1.readData(tableName);
        if(db_list.isEmpty()) {
            Toast.makeText(getActivity(), "No Input data found. Please record data to perform classification on in tab1...", Toast.LENGTH_LONG).show();
        }
        return rootView;
    }
}
