package com.example.crespo.vehicleexpenses.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import Fragments.OtherExpensesListaFragment;


/**
 * Klasa aktivnosti OtherExpensesActivity za koju je vezan fragment - prikaz liste ostalih troškova
 */

public class OtherExpensesActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_other_expenses, frameLayout);
        toolbar.setTitle(R.string.ostali_troskovi);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary_dark5));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }


        if (savedInstanceState == null) {

            OtherExpensesListaFragment servisiListaFragment = new OtherExpensesListaFragment();
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragmentOtherExpenses, servisiListaFragment);
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
