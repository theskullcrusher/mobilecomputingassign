package com.group5project.mobilecomputing.group5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj on 4/8/2018.
 */

public class DbReturnObject {
    public String label = "";
    ArrayList<XYZvalues> xyz_list = null;
    public DbReturnObject(String label, ArrayList<XYZvalues> xyz_list){
        this.label = label;
        this.xyz_list = xyz_list;
    }
}
