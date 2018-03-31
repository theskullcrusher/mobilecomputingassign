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

import com.group5project.mobilecomputing.group5.R;

public class Tab2 extends Fragment{

    //SVM

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);
        return rootView;
    }
}
