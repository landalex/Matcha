package com.sap.matcha;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    private static final String SHARED_PREFERENCES = "Matcha_Preferences";
    private static final String FIRST_RUN = "first_run";
    private static int hour = Calendar.getInstance().get(Calendar.HOUR);
    private static int minute = Calendar.getInstance().get(Calendar.MINUTE);
    private MySQLiteHelper dbHelper = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onFirstRun();

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                MainActivity.hour = hourOfDay;
                MainActivity.minute = minute;
                Toast.makeText(getApplicationContext(), "" + hour + ":" + minute, Toast.LENGTH_LONG).show();
            }
        };
        final TimePickerDialog dialog = new TimePickerDialog(this, listener, hour, minute, false);

        Button matchButton = (Button)findViewById(R.id.match_me);
        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
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
