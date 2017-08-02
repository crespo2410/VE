package com.example.crespo.vehicleexpenses.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.Locale;

import Models.RegisterResponseModel;
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SessionManager;
import helper.SharedpreferencesKeys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword2;
    private Button signUpButton;

    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etUsername = (EditText) findViewById(R.id.editTextUsername);
        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        etPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        signUpButton = (Button) findViewById(R.id.buttonReg);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());




        //pritisak tipke signUpButton
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password1 = etPassword.getText().toString().trim();
                String password2 = etPassword2.getText().toString().trim();


                if (password1.equals(password2)) {
                    if (!name.isEmpty() && !email.isEmpty() && !password1.isEmpty() && !password2.isEmpty()) {
                        registerUser(name, email, password1);

                    } else {

                        Toast.makeText(RegisterActivity.this, R.string.prazna_polja_err, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(RegisterActivity.this, R.string.loz_err, Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    /**
     * Metoda unutar koje se dešava registracija korisnika
     * @param name
     * @param email
     * @param password
     */
    private void registerUser(final String name, final String email, final String password) {


        pDialog.setMessage(getString(R.string.reg_korisnika));
        showDialog();

        final Call<RegisterResponseModel> registerUserCall = RestClient.getInstance().getApiService().userRegistration(name, email, password);
        registerUserCall.enqueue(new Callback<RegisterResponseModel>() {
            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {

                if (response.isSuccessful()) {
                    hideDialog();
                    RegisterResponseModel registerModel = response.body();
                    boolean error = registerModel.getError();

                    if (!error) {
                        Toast.makeText(RegisterActivity.this, R.string.korisnik_stvoren, Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));

                    } else {
                        String err_msg = registerModel.getErrorMsg();
                        Toast.makeText(RegisterActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                hideDialog();
                Toast.makeText(RegisterActivity.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                Log.d("Reg", t.getMessage());

            }
        });


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
