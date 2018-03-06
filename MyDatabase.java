package com.group5project.mobilecomputing.group5;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Environment;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.sql.Timestamp;

/*https://stackoverflow.com/questions/14373863/how-to-store-sqlite-database-directly-on-sdcard*/

public class MyDatabase extends SQLiteOpenHelper {
    public static final String DbName=Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_Assignment2/Group5db";
    public static final String TAG= MyDatabase.class.getCanonicalName();

    MyDatabase(Context context){
        super(context,DbName , null, 1);
        Log.d(TAG,"Creating Database");
    }

    @Override
    public void onConfigure(SQLiteDatabase dB){
        super.onConfigure(dB);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
    public void data(){
        Log.d(TAG,"In Db method");
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("create table if not exists " + "NAME_ID_SEX_AGE" + " ("
                + "timestamp long, "
                + "x_values float, "
                + "y_values float, "
                + "z_values float); ");
    }
    /*https://stackoverflow.com/questions/42619923/how-to-insert-multiple-rows-into-sqlite-android*/

    public void AddData(long time, float x, float y, float z){
        Log.d(TAG,"In insert row method");
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues tableContents = new ContentValues();
        tableContents.put("timestamp", time);
        tableContents.put("x_values",x);
        tableContents.put("y_values",y);
        tableContents.put("z_values",z);
        //to insert a row
        db1.insert("NAME_ID_SEX_AGE",null,tableContents);

    }
    public void ClearData(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("DELETE FROM "+ "NAME_ID_SEX_AGE");
    }
}
