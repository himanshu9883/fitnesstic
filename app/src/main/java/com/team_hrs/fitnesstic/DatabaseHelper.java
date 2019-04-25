package com.team_hrs.fitnesstic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Workout_Activity.db";
    public static final String TABLE_NAME = "workout_history";

    public static final String col_2 = "STEPS";
    public static final String col_3 = "DISTANCE";
    public static final String col_4 ="DATE";

    public DatabaseHelper(Context context) {  //Constructor of DatabaseHelper
        super(context, DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Table Created
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+"("+col_2+" TEXT, "+col_3+" TEXT,"+col_4+" TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME); //Table is Created
        onCreate(sqLiteDatabase);
    }

    public boolean insertData( String steps, String distance, String date){       //Inserting Data in your Table
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_2,steps);
        contentValues.put(col_3,distance);
        contentValues.put(col_4,date);
        long result = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){ //Getting All the Data from the Databse and showing the Details
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }

    public void deleteAll(){ //For Deleting the Data from the Table
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,null,null);
        sqLiteDatabase.execSQL("delete from "+ TABLE_NAME);
    }

}
