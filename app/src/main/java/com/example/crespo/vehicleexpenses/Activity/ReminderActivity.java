package com.example.crespo.vehicleexpenses.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import Fragments.ReminderListaFragment;

/**
 * Klasa aktivnosti ReminderActivity za koju je vezan fragment - prikaz liste podsjetnika
 */

public class ReminderActivity extends HomeActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_reminder, frameLayout);
        toolbar.setTitle(R.string.podsjetnici);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary2));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark2));
        }


        if (savedInstanceState == null) {

            ReminderListaFragment reminderListaFragment = new ReminderListaFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragmentReminder, reminderListaFragment).addToBackStack("ReminderListaFragment");
            transaction.commit();


        }


    }

    @Override
    public void onBackStackChanged() {
        for (int entry = 0; entry < getSupportFragmentManager().getBackStackEntryCount(); entry++) {
            Log.d("Stack", "Found fragment: " + getSupportFragmentManager().getBackStackEntryAt(entry).getName());
        }
    }
}
