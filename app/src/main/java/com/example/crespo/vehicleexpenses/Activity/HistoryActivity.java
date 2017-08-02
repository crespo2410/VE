package com.example.crespo.vehicleexpenses.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import Fragments.HistoryFragment;


/**
 * Klasa aktivnosti HistoryActivity zadužena za povijest
 */
public class HistoryActivity extends HomeActivity {


    String translate_value, translate_value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_history, frameLayout);
        toolbar.setTitle(R.string.povijest);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary6));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark6));
        }


        //Pokretanje fragmenta koji je vezan za ovu aktivnost
        if (savedInstanceState == null) {

            HistoryFragment historyFragment = new HistoryFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentHistory, historyFragment);
            transaction.commit();

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }




}
