package com.example.crespo.vehicleexpenses.Activity;


import android.os.Bundle;

import com.example.crespo.vehicleexpenses.R;

/**
 * Klasa aktivnosti O nama
 */
public class AboutActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_about_us, frameLayout);
        //postavljanje naslova Toolbara
        toolbar.setTitle(getString(R.string.o_nama));
    }

}
