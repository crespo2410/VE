package com.example.crespo.vehicleexpenses.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import Fragments.ServisiListaFragment;


/**
 * Klasa aktivnosti ServiceExpencesActivity za koju je vezan fragment - prikaz liste troškova servisa
 */
public class ServiceExpencesActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_service_expences, frameLayout);
        toolbar.setTitle(R.string.servisi);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary4));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark4));
        }


        if (savedInstanceState == null) {

            ServisiListaFragment servisiListaFragment = new ServisiListaFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragmentService, servisiListaFragment);
            transaction.commit();


        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setIcon(R.drawable.ic_action_downlod);

        return super.onCreateOptionsMenu(menu);
    }


}
