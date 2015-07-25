package com.sap.matcha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public static final String COLUMN_TIME = "Timestamp";

    public static final String DATABASE_NAME = "DB";
    public static final int DATABASE_VERSION = 1;

    public static int DATABASE_SIZE = 0;
    public static int DATABASE_NEXT_RECORD = DATABASE_SIZE++;

    // EventsDataSource creation sql statement
    private static final String EMPLOYEE_DATABASE_CREATE = "CREATE TABLE "
            + TABLE_EMPLOYEE + "("
            + COLUMN_EMAIL + " TINYTEXT,"
            + COLUMN_NAME + " TINYTEXT,"
            + COLUMN_DEPT + " TINYTEXT,"
            + COLUMN_AMA + " TEXT,"
            + COLUMN_PHONE + " VARCHAR(15),"
            + COLUMN_LOCATION + " TINYTEXT,"
            + "PRIMARY KEY(" + COLUMN_EMAIL + ")"
            + ");";

    private static final String REQUEST_DATABASE_CREATE = "CREATE TABLE "
            + TABLE_REQUEST + "("
            + COLUMN_TIME + " "
            + COLUMN_NAME + " TINYTEXT,"
            + COLUMN_EMAIL + " TINYTEXT,"

    /**
     * Constructor
     * @param context
     */
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
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
        onCreate(db);
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

}
