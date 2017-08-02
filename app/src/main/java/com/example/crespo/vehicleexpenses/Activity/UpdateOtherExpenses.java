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
import android.view.WindowManager;

import com.example.crespo.vehicleexpenses.R;

import java.util.Locale;

import Fragments.NoviNazivOstalogTroskaFragment;
import Fragments.UpdateOtherExpensesFragment;
import Models.ExpensesOtherExpensesModel;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;


/**
 * Klasa aktivnosti UpdateOtherExpenses za koju je vezan fragment - prikaz ažuriranja/dodavanja ostalih troškova
 */

public class UpdateOtherExpenses extends AppCompatActivity implements NoviNazivOstalogTroskaFragment.RecyclerValueInterfaceOther {


    public Toolbar toolbar;
    public FragmentManager fragmentManager;
    UpdateOtherExpensesFragment updateOtherExpensesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_other_expenses);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.ostali_troskovi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getSupportFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }


        if (savedInstanceState == null) {

            updateOtherExpensesFragment = new UpdateOtherExpensesFragment();
            fragmentManager.beginTransaction().add(R.id.lin_ley_updateOtherExpenses, updateOtherExpensesFragment, "ccc").commit();
        } else {

            updateOtherExpensesFragment = (UpdateOtherExpensesFragment) fragmentManager.getFragment(savedInstanceState, "ccc");
        }


    }

    @Override
    public void setValueforRecycler(ExpensesOtherExpensesModel expensesOtherExpensesModel) {

        UpdateOtherExpensesFragment articleFrag = (UpdateOtherExpensesFragment)
                getSupportFragmentManager().findFragmentByTag("ccc");
        articleFrag.setTroskoveOstalihTroskova(expensesOtherExpensesModel);

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
