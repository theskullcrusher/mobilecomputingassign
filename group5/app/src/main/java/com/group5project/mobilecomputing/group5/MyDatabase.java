package com.group5project.mobilecomputing.group5;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Environment;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.util.ArrayList;
        import java.util.List;


        import java.sql.Timestamp;

/*https://stackoverflow.com/questions/14373863/how-to-store-sqlite-database-directly-on-sdcard*/

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DbName = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/Data/CSE535_Assignment2/Group5db.db";
    public static final String TAG = MyDatabase.class.getCanonicalName();

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

    public void data(String str1, String str2, String str3, String str4) {
        Log.d(TAG, "In Db method");
        String tableName = str1 + "_" + str2 + "_" + str3 + "_" + str4;
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("create table if not exists " + tableName + " ("
                + "timestamp long, "
                + "x_values float, "
                + "y_values float, "
                + "z_values float); ");
    }
    /*https://stackoverflow.com/questions/42619923/how-to-insert-multiple-rows-into-sqlite-android*/

    public void AddData(long time, float x, float y, float z, String str1, String str2, String str3, String str4) {
        Log.d(TAG, "In insert row method");
        String tableName2 = str1 + "_" + str2 + "_" + str3 + "_" + str4;
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues tableContents = new ContentValues();
        tableContents.put("timestamp", time);
        tableContents.put("x_values", x);
        tableContents.put("y_values", y);
        tableContents.put("z_values", z);
        //to insert a row
        db1.insert(tableName2, null, tableContents);
        db1.close();

    }

    /*public void ClearData(){
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.execSQL("DELETE FROM "+ "NAME_ID_SEX_AGE");
    }*/
    public List<XYZvalues> readData(String Table_Name) {
        List<XYZvalues> values;
        values = new ArrayList<XYZvalues>();
        int i = 0;
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery("SELECT * FROM " + Table_Name + " ORDER BY TIMESTAMP DESC", null);
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
            } while (i <= 10);
        }
        db2.close();
        return values;
    }
}

