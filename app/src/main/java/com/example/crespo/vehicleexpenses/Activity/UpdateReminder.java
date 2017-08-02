package com.example.crespo.vehicleexpenses.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import java.util.Locale;

import Fragments.UpdateReminderFragment;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;

/**
 * Klasa aktivnosti UpdateReminder za koju je vezan fragment - prikaz ažuriranja/dodavanja podsjetnika
 */

public class UpdateReminder extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    public Toolbar toolbar;
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reminder);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary2));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.stvori_uredi_podsjetnik);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark2));
        }


        if (savedInstanceState == null) {

            UpdateReminderFragment updateRefuelingFragment = new UpdateReminderFragment();
            fragmentManager.beginTransaction().add(R.id.lin_ley_updateReminder, updateRefuelingFragment).addToBackStack("UpdateReminderFragmnet").commit();
        }


    }


    @Override
    public void onBackStackChanged() {
        for (int entry = 0; entry < getSupportFragmentManager().getBackStackEntryCount(); entry++) {
            Log.d("Stack", "Found fragment : " + getSupportFragmentManager().getBackStackEntryAt(entry).getName());
        }
    }




    @Override
    protected void attachBaseContext(Context newBase) {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String translate_value = sharedPreferences.getString(SharedpreferencesKeys.PRIJEVOD, "1");
        String translate_value2;

        switch (translate_value) {
            case "1":
                translate_value2 = "hr";
                break;
            case "2":
                translate_value2 = "en";

                break;
            default:
                translate_value2 = "hr";
        }


        Locale locale = new Locale(translate_value2);
        super.attachBaseContext(ConfigurationWrapper.wrapLocale(newBase, locale));
    }
}

