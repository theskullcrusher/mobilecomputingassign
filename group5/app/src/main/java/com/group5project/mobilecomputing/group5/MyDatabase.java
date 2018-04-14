package com.group5project.mobilecomputing.group5;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;

import java.sql.Timestamp;

/*https://stackoverflow.com/questions/14373863/how-to-store-sqlite-database-directly-on-sdcard*/

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DbName = "Group5db.db";
    public static final String DbPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_ASSIGNMENT3/";
    public static final String TAG = MyDatabase.class.getCanonicalName();
    public String str1="Activity string, ";
    private Context mContext;

    MyDatabase(Context context) {
        super(context, DbPath+DbName, null, 1);

        Log.d(TAG, "Creating Database");

        this.mContext = context;
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


    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase()
    {
        File dbFile = new File(DbPath + DbName);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DbName);
        String outFileName = DbPath + DbName;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    public void data() {
        Log.d(TAG, "In Db method");
        String tableName = "Activity_Database";

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
//            this.getReadableDatabase();
//            this.close();
//            for (int i = 1; i <= 50; i++) {
//                for (int j = 1; j <= 3; j++) {
//                    if (j == 1) {
//                        str1 = str1 + "AccelX" + Integer.toString(i) + " " + "float, ";
//                    } else if (j == 2) {
//                        str1 = str1 + "AccelY" + Integer.toString(i) + " " + "float, ";
//                    } else if (j == 3) {
//                        if (i == 50)
//                            str1 = str1 + "AccelZ" + Integer.toString(i) + " " + "float ";
//                        else
//                            str1 = str1 + "AccelZ" + Integer.toString(i) + " " + "float, ";
//
//                    }
//                }
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "Default database copied to sdcard");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }

//            Log.d(TAG, "Creating table");
//            db1.execSQL("create table if not exists " + tableName + "(" + str1 + ");");
//            Log.d(TAG, "table created");
        }
        else
            Log.e(TAG, "Database exists!!");
    }
    /*https://stackoverflow.com/questions/42619923/how-to-insert-multiple-rows-into-sqlite-android*/

    public void AddData(float values[], String str) {
        Log.d(TAG, "In insert row method");
        String tableName2 = "Activity_Database";
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues tableContents = new ContentValues();
            tableContents.put("Activity", str);
            for (int i = 1; i <= 50; i++) {
                for (int j = 1; j <= 3; j++) {
                    if (j == 1)
                        tableContents.put("AccelX" + Integer.toString(i), values[3 * (i - 1) + j - 1]);
                    else if (j == 2)
                        tableContents.put("AccelY" + Integer.toString(i), values[3 * (i - 1) + j - 1]);
                    else if (j == 3)
                        tableContents.put("AccelZ" + Integer.toString(i), values[3 * (i - 1) + j - 1]);


                }
            }

            db1.insert(tableName2, null, tableContents);
        int id = android.os.Process.myTid();
        Log.d(TAG, "in myDatabase " + Integer.toString(id));


    }

    //https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
    public ArrayList<DbReturnObject> readData(String Table_Name) {
        int i = 0;
        SQLiteDatabase db2 = this.getReadableDatabase();
        ArrayList<DbReturnObject> db_list = new ArrayList<DbReturnObject>();
        try {
            Cursor cursor = db2.rawQuery("SELECT * FROM " + Table_Name, null);
            int count = cursor.getCount();
            Log.e(TAG, "Count is:"+count);
            cursor.moveToFirst();
                do {
                    DbReturnObject dbReturnObject;
                    String label = cursor.getString(0);
                    ArrayList<XYZvalues> xyz_list = new ArrayList<XYZvalues>();
                    for(int j=0;j<150;j+=3){
                        XYZvalues xyz_value = new XYZvalues();
                        xyz_value.x_value = cursor.getFloat(j+1);
                        xyz_value.y_value = cursor.getFloat(j+2);
                        xyz_value.z_value = cursor.getFloat(j+3);
                        xyz_list.add(xyz_value);
                    }
                    dbReturnObject = new DbReturnObject(label, xyz_list);
                    db_list.add(dbReturnObject);
                    Log.d(TAG, "Reading data item:"+String.valueOf(i+1));
                    cursor.moveToNext();
                    i++;
                } while (i < count);
            db2.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
            Log.d(TAG, "Exception caught");
        }
        return db_list;
    }
}

