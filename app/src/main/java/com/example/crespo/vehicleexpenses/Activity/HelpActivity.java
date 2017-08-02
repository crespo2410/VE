package com.example.crespo.vehicleexpenses.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import helper.SharedpreferencesKeys;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
/**
 * Klasa aktivnosti HelpActivity - zvanje i pronalazak ben. postaja i servisa na karti
 */

public class HelpActivity extends HomeActivity {


    private Button zovi;
    private Button karta;
    private String broj_za_zvanje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_help, frameLayout);


        toolbar.setTitle(R.string.pomoc);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary6));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark6));
        }


        zovi = (Button) findViewById(R.id.zoviButton);
        karta = (Button) findViewById(R.id.zoviKarte);


        //Button za zvanje pomoći
        zovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callHelp();

            }
        });


        //Button za pokretanje Google Maps
        karta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HelpActivity.this, MapsActivity.class));

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {



        Intent callForHelpIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + readSettings()));

        switch (requestCode) {
            case MY_PERMISSION_REWQUEST_CALL_PHONE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callForHelpIntent);

                } else {


                    Toast.makeText(this, R.string.toast_permision, Toast.LENGTH_SHORT).show();

                    return;
                }
            }
        }

    }


    private String readSettings() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        broj_za_zvanje = preferences.getString(SharedpreferencesKeys.NUMBBER,"1987");

        return broj_za_zvanje;
    }




    //metoda za zvanje
    public void callHelp() {


        //Provjera da li postoji permission (dozvola)
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)) != PERMISSION_GRANTED) {

            //Dodatno pojasnjenje zasto
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                //u  ovom slucaju je ocito za poziv pa nije potrebno

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REWQUEST_CALL_PHONE);
            }


        } else {
            //permision postoji
            //Stavljen samo bezveze broj - PROMIJENI NA PRAVI
            Intent callForHelpIntent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + readSettings()));
            startActivity(callForHelpIntent2);

        }


    }


}
