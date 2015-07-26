package com.sap.matcha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by AlexLand on 15-07-25.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EMPLOYEE = "employee";
    public static final String COLUMN_EMAIL = "SAPemail";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DEPT = "Department";
    public static final String COLUMN_AMA = "AskMeAbout";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_LOCATION = "Location";

    public static final String TABLE_REQUEST = "request";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIME = "Timestamp";

    public static final String DATABASE_NAME = "DB";
    public static final int DATABASE_VERSION = 1;
    private static final String INSERT = "INSERT INTO ";
    private static final String VALUES = " VALUES ";
    private static final String REQUEST_COLUMNS = "("
            + COLUMN_TIME + ", "
            + COLUMN_NAME + ", "
            + COLUMN_EMAIL + ", "
            + COLUMN_PHONE + ")";

    public static int DATABASE_SIZE = 0;
    public static int DATABASE_NEXT_RECORD = DATABASE_SIZE++;

    // creation sql statement
    public static final String EMPLOYEE_TABLE_CREATE = "CREATE TABLE "
            + TABLE_EMPLOYEE + "("
            + COLUMN_EMAIL + " VARCHAR(255),"
            + COLUMN_NAME + " TINYTEXT,"
            + COLUMN_DEPT + " TINYTEXT,"
            + COLUMN_AMA + " TEXT,"
            + COLUMN_PHONE + " VARCHAR(15),"
            + COLUMN_LOCATION + " TINYTEXT,"
            + "PRIMARY KEY(" + COLUMN_EMAIL + ")"
            + ");";

    public static final String REQUEST_TABLE_CREATE = "CREATE TABLE "
            + TABLE_REQUEST + "("
            + COLUMN_ID + " INTEGER,"
            + COLUMN_TIME + " BIGINT,"
            + COLUMN_NAME + " TINYTEXT,"
            + COLUMN_EMAIL + " TINYTEXT,"
            + COLUMN_PHONE + " VARCHAR(15),"
            + "PRIMARY KEY(" + COLUMN_ID + ")"
            + ");";

    /**
     * Constructor
     * @param context
     */
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
//        database.execSQL(EMPLOYEE_TABLE_CREATE);
        ServerHelper.serverRequest(EMPLOYEE_TABLE_CREATE);
    }

    public void onCreate(SQLiteDatabase database, String query) {
        ServerHelper.serverRequest(query);
//        database.execSQL(DATABASE_CREATE);
    }


    /**
     * Deletes all existing data from the table and re-creates the table
     * @param db the database to re-create
     * @param oldVersion the current version of the database
     * @param newVersion the version of the database to move to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db, TABLE_EMPLOYEE);

        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUEST);
        onCreate(db, TABLE_REQUEST);
    }

    /**
     * Closes connections to database and deletes it. This should be called if
     * changes are made to the database structure
     * @param db the database to close
     * @param context
     */
    public static void deleteDatabase(SQLiteDatabase db, Context context){
        db.close();
        context.deleteDatabase(DATABASE_NAME);
    }

    public void insertRequest(SQLiteDatabase db, long time, String name, String email, String phone) {
        String statement = INSERT + TABLE_REQUEST + REQUEST_COLUMNS + VALUES + "("
                + time + ","
                + "\"" + name + "\","
                + "\"" + email + "\","
                + "\"" + phone + "\""
                + ");";
        db.execSQL(statement);
//        ServerHelper.serverRequest(statement);
        Log.i("SQLiteHelper", statement);
    }
    public void insertEmployee(SQLiteDatabase db, String email, String name, String dept, String ama, String phone, String location) {
        if (readEmployee(db).equals("")) {
            String statement = INSERT + TABLE_EMPLOYEE + VALUES + "("
                    + "\"" + email + "\","
                    + "\"" + name + "\","
                    + "\"" + dept + "\","
                    + "\"" + ama + "\","
                    + "\"" + phone + "\","
                    + "\"" + location + "\""
                    + ");";
            db.execSQL(statement);
//            ServerHelper.serverRequest(statement);
            Log.i("SQLiteHelper", "Inserted");
        }
        else {
            Log.i("SQLiteHelper", "Not inserted");
        }
    }

    public ArrayList<String> readEmployee(SQLiteDatabase db) {
        ArrayList<String> employee = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_EMPLOYEE, null);
        if (c.moveToFirst()) {
            employee.add(c.getString(0));
            employee.add(c.getString(1));
            employee.add(c.getString(2));
            employee.add(c.getString(3));
            employee.add(c.getString(4));
            employee.add(c.getString(5));

            Log.i("Query", employee.get(0) + employee.get(1) + employee.get(2) + employee.get(3)
                    + employee.get(4) + employee.get(5));
        }
        c.close();
        return employee;
    }

    public ArrayList<String> readRequest(SQLiteDatabase db, long queryTime){
        ArrayList<String> returnList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_REQUEST + " WHERE " + COLUMN_TIME + " = " + queryTime, null);
        if (c.moveToFirst()){
            returnList.add(c.getString(0));
            returnList.add(c.getString(1));
            returnList.add(c.getString(2));
            returnList.add(c.getString(3));
            returnList.add(c.getString(4));

        }
        return returnList;
    }

}
