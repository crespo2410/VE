package com.example.crespo.vehicleexpenses.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;

import java.util.Locale;

import helper.ConfigurationWrapper;
import helper.SessionManager;
import helper.SharedpreferencesKeys;

/**
 * Klasa aktivnosti - početnog zaslona s mogucnoscu prijave, registracije ili nastavka kao gost
 */
public class StartUpActivity extends AppCompatActivity implements View.OnClickListener {


    private Button signUpButton;
    private Button logInButton;
    private TextView exploreAsGuest;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        signUpButton = (Button) findViewById(R.id.buttonSignUp);
        logInButton = (Button) findViewById(R.id.buttonLogin);
        exploreAsGuest = (TextView) findViewById(R.id.textViewGuest);

        signUpButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        exploreAsGuest.setOnClickListener(this);



         sessionManager = new SessionManager(this);


    }


    @Override
    public void onClick(View view) {


        //Prvi ekran kod ulaska u aplikaciju, u ovisnosti o odabranoj tipki pozovi aktivnost
        switch (view.getId()) {

            case R.id.buttonSignUp:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.buttonLogin:
                startActivity(new Intent(this, LogInActivity.class));
                break;

            case R.id.textViewGuest:
                if(sessionManager.isFirstTime()) {

                    Intent intent = new Intent(this, UpdateVehicleActivity.class);
                    intent.putExtra("idButtona", R.id.buttonAddVehicle);
                    startActivity(intent);

                }else startActivity(new Intent(this, StatisticActivity.class));
                break;


            default:

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
