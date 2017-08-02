package com.example.crespo.vehicleexpenses.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;

import java.util.Locale;

import Fragments.AlertLogoutFragment;
import Fragments.AlertNotLogInFragment;
import Fragments.CreateAccountFragment;
import helper.ConfigurationWrapper;
import helper.SessionManager;
import helper.SharedpreferencesKeys;
import helper.VehicleAndDistanceManager;

/**
 * Klasa NavigationView aktivnosti
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final int MY_PERMISSION_REWQUEST_CALL_PHONE = 5;
    public Toolbar toolbar;
    protected LinearLayout frameLayout;
    protected NavigationView navigationView;
    protected FragmentManager fragmentManager;
    DrawerLayout drawer;
    private SessionManager sessionManager;
    private TextView tvCarName;
    private TextView tvCarType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.povijest);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary6));
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        frameLayout = (LinearLayout) findViewById(R.id.fragment_container);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //referenciranje na zaglavlje NavigationViewa
        View headerLayout = navigationView.getHeaderView(0);
        tvCarName = (TextView) headerLayout.findViewById(R.id.tvCarName);
        tvCarType = (TextView) headerLayout.findViewById(R.id.tvCarType);

        //dohvačanje imena i proizvodaca trenutno aktivnog vozila i postavljanje u TextView elemente
        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);
        tvCarName.setText(manager.getVehicleName());
        tvCarType.setText(manager.getManufactrer());

        fragmentManager = getSupportFragmentManager();


    }


    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Tu se rješava pitanje koji je od elemenata navigacije pritisnut tako se pokreće
        //odgovarajuća aktivnost

        int id = item.getItemId();


        if (id == R.id.nav_history) {

            startActivity(new Intent(this, HistoryActivity.class));


        } else if (id == R.id.nav_stat) {

            Intent in = new Intent(HomeActivity.this, StatisticActivity.class);
            startActivity(in);


        } else if (id == R.id.nav_gas) {

            startActivity(new Intent(this, RefuelingExpensesActivity.class));


        } else if (id == R.id.nav_vehicle) {

            startActivity(new Intent(this, VehicleActivity.class));


        } else if (id == R.id.nav_ser) {

            startActivity(new Intent(this, ServiceExpencesActivity.class));


        } else if (id == R.id.nav_other) {

            startActivity(new Intent(this, OtherExpensesActivity.class));

        } else if (id == R.id.nav_reminder) {

            startActivity(new Intent(this, ReminderActivity.class));


        } else if (id == R.id.nav_settings) {

            startActivity(new Intent(this, SettingsActivity.class));


        } else if (id == R.id.nav_translate) {

            //provjerava se da li je korisnik prijavljen kako bi mu se omogućilo ili ne pokretanje aktivnosti prijevoda
            sessionManager = new SessionManager(getApplicationContext());

            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(HomeActivity.this, TranslateActivity.class));
            } else {

                CreateAccountFragment createAccountFragment = new CreateAccountFragment();
                createAccountFragment.show(getSupportFragmentManager(), "CreateAccountFrgment");

            }


        } else if (id == R.id.nav_contact) {

            startActivity(new Intent(this, ContactActivity.class));


        } else if (id == R.id.nav_about) {


            startActivity(new Intent(this, AboutActivity.class));

        } else if (id == R.id.nav_help) {


            startActivity(new Intent(this, HelpActivity.class));

        } else if (id == R.id.nav_logout) {


            //provjerava se da li je korisnik prijavljen, kako bi mu se prikazo pravilni dijalog
            sessionManager = new SessionManager(getApplicationContext());
            if (sessionManager.isLoggedIn()) {
                AlertLogoutFragment alertLogoutFragment = new AlertLogoutFragment();
                alertLogoutFragment.show(getSupportFragmentManager(), "AlertLogoutFragmet");
            } else {

                AlertNotLogInFragment alertNotLogInFragment = new AlertNotLogInFragment();
                alertNotLogInFragment.show(getSupportFragmentManager(), "AlertNotLogIn");

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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





