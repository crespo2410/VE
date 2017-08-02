package com.example.crespo.vehicleexpenses.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RadioButton;

import com.example.crespo.vehicleexpenses.R;

import helper.SharedpreferencesKeys;

/**
 * Klasa aktivnosti za prijevod koja sadrži dva RadioButotna za odabir jezika
 */

public class TranslateActivity extends HomeActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_translate, frameLayout);
        toolbar.setTitle(R.string.pref_title_prijevod);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPurple));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
    }


    public void onRadioButtonClicked(View view) {

        //ovisno o odabiru sprema se vrijednost u SharedPreferences datoteku postavki, kroz
        //promjene u postavkama, promjene se prenose u nadjačanu metodu koja se poziva kod mjenjanja postavki - gdje se osvježava UI
        boolean checked = ((RadioButton) view).isChecked();

        String broj = "1";


        switch (view.getId()) {
            case R.id.jezik_hr:
                if (checked) {

                    broj = "1";

                    editor.putString(SharedpreferencesKeys.PRIJEVOD, broj);
                    editor.commit();

                    recreate();
                    break;
                }
            case R.id.jezik_en:
                if (checked) {

                    broj = "2";

                    editor.putString(SharedpreferencesKeys.PRIJEVOD, broj);
                    editor.commit();

                    recreate();
                    break;
                }
            default:
                break;
        }


    }

}
