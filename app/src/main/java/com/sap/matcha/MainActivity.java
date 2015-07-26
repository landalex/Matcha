package com.sap.matcha;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    public static final String SHARED_PREFERENCES = "Matcha_Preferences";
    private static final String FIRST_RUN = "first_run";
    private static final String PREFERENCE_USER_NAME = "user_name";
    private static final String PREFERENCE_USER_EMAIL = "user_email";
    private static final String PREFERENCE_USER_DEPT = "user_department";
    private static final String PREFERENCE_USER_ROLE = "user_role";
    private static final String PREFERENCE_USER_AMA = "user_ama";
    private static final String PREFERENCE_USER_LOCATION = "user_location";


    private static int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int minute = Calendar.getInstance().get(Calendar.MINUTE);
    private MySQLiteHelper dbHelper = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dbHelper.getWritableDatabase().execSQL("DELETE FROM employee WHERE Name=\"Alex Land\"");
//        dbHelper.onCreate(dbHelper.getWritableDatabase(), MySQLiteHelper.REQUEST_TABLE_CREATE);
//        dbHelper.onCreate(dbHelper.getWritableDatabase(), MySQLiteHelper.EMPLOYEE_TABLE_CREATE);


        onFirstRun();

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String email = settings.getString(PREFERENCE_USER_EMAIL, "");
                String name = settings.getString(PREFERENCE_USER_NAME, "");
                TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String phone = tMgr.getLine1Number();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                long time = calendar.getTimeInMillis();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ArrayList<String> results = getRecentResults(db, time);
                if (results != null) {
                    matchFound(results);
                }
                else {
                    dbHelper.insertRequest(db, time, name, email, phone);
                }
                MainActivity.hour = hourOfDay;
                MainActivity.minute = minute;
            }
        };
        final TimePickerDialog dialog = new TimePickerDialog(this, listener, hour, minute, false);

        final ImageButton matchButton = (ImageButton)findViewById(R.id.match_me);
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                matchButton.setImageDrawable(getDrawable(R.drawable.matcha_me_icon_pressed));
                updateDatabase();
                dialog.show();
            }
        });
    }

    private ArrayList<String> getRecentResults(SQLiteDatabase db, long time) {
        ArrayList<String> results = dbHelper.readRequest(db, time);
//
//        Long timeDiff = time - Long.parseLong(results.get(1));
//        if (Math.abs(timeDiff) < 300000) {
//            return results;
//        }
//        else {
//            return null;
//        }
        return results;
    }

    private void matchFound(final ArrayList<String> results) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.main_activity_decline_message));
        builder.setPositiveButton(getString(R.string.main_activity_decline_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog noDialog = builder.create();

        builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.main_activity_decline, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noDialog.show();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.main_activity_accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putStringArrayListExtra("request", results);
                startActivity(intent);
            }
        });
        builder.setMessage(R.string.main_activity_match_found_body);
        builder.setTitle(R.string.main_activity_match_found);
        builder.create().show();
    }

    private void updateDatabase() {
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String email = settings.getString(PREFERENCE_USER_EMAIL, "");
        String name = settings.getString(PREFERENCE_USER_NAME, "");
        String dept = settings.getString(PREFERENCE_USER_DEPT, "");
        String ama = settings.getString(PREFERENCE_USER_AMA, "");
        String location = settings.getString(PREFERENCE_USER_LOCATION, "");

        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String phone = tMgr.getLine1Number();

        dbHelper.insertEmployee(db, email, name, dept, ama, phone, location);
        dbHelper.readEmployee(db);
    }

    private void onFirstRun() {
        SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES,  MODE_PRIVATE);
        if (settings.getBoolean(FIRST_RUN, true)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FIRST_RUN, false);
            editor.commit();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.onCreate(db, MySQLiteHelper.TABLE_EMPLOYEE);
            dbHelper.onCreate(db, MySQLiteHelper.TABLE_REQUEST);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
