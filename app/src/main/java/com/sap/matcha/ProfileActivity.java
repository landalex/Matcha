package com.sap.matcha;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;


public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);

        ArrayList<String> meetingInfo = getIntent().getExtras().getStringArrayList("request");
        ArrayList<String> employeeInfo = dbHelper.readEmployee(dbHelper.getWritableDatabase());

        TextView profileName = (TextView)findViewById(R.id.profileName);
        profileName.setText(employeeInfo.get(1));
        TextView profileDept = (TextView)findViewById(R.id.profileDept);
        profileDept.setText(employeeInfo.get(2));
        TextView profileAMA = (TextView)findViewById(R.id.profileAMA);
        profileAMA.setText(employeeInfo.get(3));
        TextView profileDescription = (TextView)findViewById(R.id.profileMatchInfo);
        profileDescription.setText(getString(R.string.profile_activity_match_info));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
