package com.sap.matcha;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PreferenceFragment() {
                    @Override
                    public void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        addPreferencesFromResource(R.xml.settings_fragment);
                    }

                    @Override
                    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                        View view = super.onCreateView(inflater, container, savedInstanceState);
                        return view;
                    }
                }).commit();

    }

    @Override
    public void onDestroy() {
        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        SharedPreferences settings = getSharedPreferences(MainActivity.SHARED_PREFERENCES,  MODE_PRIVATE);
        String email = settings.getString(getString(R.string.user_email), "");
        String name = settings.getString(getString(R.string.user_name), "");
        String dept = settings.getString(getString(R.string.user_department), "");
        String ama = settings.getString(getString(R.string.user_ama), "");
        String location = settings.getString(getString(R.string.user_location), "");

        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String phone = tMgr.getLine1Number();

        dbHelper.insertEmployee(db, email, name, dept, ama, phone, location);
    }

}
