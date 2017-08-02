package com.example.crespo.vehicleexpenses.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Menu;
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import Fragments.VehicleListFragment;


/**
 * Klasa aktivnosti VehicleActivity za koju je vezan fragment - prikaz liste vozila
 */

public class VehicleActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_vehicle, frameLayout);
        toolbar.setTitle(R.string.vozila);
        toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.primary6, this.getTheme()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark6));
        }


        if (savedInstanceState == null) {

            VehicleListFragment vehicleListFragment = new VehicleListFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragmentVehicle, vehicleListFragment);
            transaction.commit();


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
