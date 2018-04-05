package com.group5project.mobilecomputing.group5;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteException;
        import android.os.Environment;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.io.IOException;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.ArrayList;
        import java.util.List;


        import java.sql.Timestamp;

/*https://stackoverflow.com/questions/14373863/how-to-store-sqlite-database-directly-on-sdcard*/

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DbName = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_Assignment2/Group5db.db";
    public static final String TAG = MyDatabase.class.getCanonicalName();
    public String str1="Activity string,";

    MyDatabase(Context context) {
        super(context, DbName, null, 1);
        Log.d(TAG, "Creating Database");
    }

    MyDatabase(Context context, String DbName2) {
        super(context, DbName2, null, 1);
        Log.d(TAG, "Opening Downloaded Database");

    }

    @Override
    public void onConfigure(SQLiteDatabase dB) {
        super.onConfigure(dB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public void data() {
        Log.d(TAG, "In Db method");
        String tableName = "Activity Database";
        SQLiteDatabase db1 = this.getWritableDatabase();
        for(int i=1; i<=50; i++) {
            for (int j = 1; j <= 3; j++) {
                if(j==1)
                    str1 = str1 + "AccelX" + Integer.toString(i) + " " + "float,";
                else if (j==2)
                    str1 = str1 + "AccelY" + Integer.toString(i) + " " + "float,";
                else if (j==3)
                    str1 = str1 + "AccelZ" + Integer.toString(i) + " " + "float,";


            }
        }
        db1.execSQL("create table if not exists " + tableName + str1);
    }
    /*https://stackoverflow.com/questions/42619923/how-to-insert-multiple-rows-into-sqlite-android*/

    public void AddData(float values[], String str) {
        Log.d(TAG, "In insert row method");
        String tableName2 = str1;
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues tableContents = new ContentValues();
        tableContents.put("Activity", str);
        for(int i=1; i<=50; i++) {
            for (int j = 1; j <= 3; j++) {
                if(j==1)
                    tableContents.put("AccelX"+ Integer.toString(i), values[3*(i-1)+j-1]);
                else if (j==2)
                    tableContents.put("AccelY"+ Integer.toString(i), values[3*(i-1)+j-1]);
                else if (j==3)
                    tableContents.put("AccelZ"+ Integer.toString(i), values[3*(i-1)+j-1]);


            }
        }

        db1.insert(tableName2, null, tableContents);
        db1.close();

    }

    //https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
    public List<XYZvalues> readData(String Table_Name) {
        List<XYZvalues> values;
        values = new ArrayList<XYZvalues>();
        int i = 0;
        SQLiteDatabase db2 = this.getReadableDatabase();
        try {
            Cursor cursor = db2.rawQuery("SELECT * FROM " + Table_Name + " ORDER BY TIMESTAMP DESC", null);
            String countQuery = "SELECT  * FROM " + Table_Name;
            Cursor cursor1 = db2.rawQuery(countQuery, null);
            int count = cursor1.getCount();
            cursor1.close();

            if (cursor.moveToFirst()) {
                do {
                    XYZvalues xyz_value = new XYZvalues();
                    xyz_value.x_value = cursor.getFloat(1);
                    xyz_value.y_value = cursor.getFloat(2);
                    xyz_value.z_value = cursor.getFloat(3);
                    values.add(xyz_value);
                    Log.d(TAG, "appending to the array list");
                    cursor.moveToNext();
                    i++;
                } while (i < Math.min(count, 10));
            }
            db2.close();

        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception caught");
        }
        return values;
    }
}

