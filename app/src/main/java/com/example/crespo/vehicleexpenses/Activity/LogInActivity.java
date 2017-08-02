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

import Models.LoginResponseModel;
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SessionManager;
import helper.SharedpreferencesKeys;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa aktivnosti za prijavu
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etEmail;
    private EditText etPassword;
    private Button signUpButton;
    private Button logInButton;
    private ProgressDialog pDialog;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        etEmail = (EditText) findViewById(R.id.etEmailLogin);
        etPassword = (EditText) findViewById(R.id.etPasswordLogin);
        signUpButton = (Button) findViewById(R.id.buttonSignup);
        logInButton = (Button) findViewById(R.id.buttonLogIn);

        signUpButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        //Ako je korisnik ulogiran i ako mu je prvi puta dobaci ga do aktivnosti dodavanja vozila
        if (session.isLoggedIn()) {
            if(session.isFirstTime()){
                Intent intent = new Intent(this, UpdateVehicleActivity.class);
                intent.putExtra("idButtona", R.id.buttonAddVehicle);
                startActivity(intent);


              // u suprotnom na aktivost Statistike
            }else {
                Intent intent = new Intent(LogInActivity.this, StatisticActivity.class);
                startActivity(intent);
                finish();
            }
        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonLogIn:

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                //jednostavna provjera pravilnog unosa
                if (!email.isEmpty() && !password.isEmpty()) {
                    checkLogin(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.logInerror_msg, Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.buttonSignup:
                finish();
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            default:


        }

    }


    /**
     * Metoda za prijavu i provjeru unesenih podataka kroz poziv na bazu
     * @param email - uneseni email
     * @param password - uneseni password
     */
    private void checkLogin(final String email, final String password) {

        pDialog.setMessage(getString(R.string.provjera_login));
        showDialog();

        Call<LoginResponseModel> logInResponseCall = RestClient.getInstance().getApiService().userLogIn(email, password);
        logInResponseCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.isSuccessful()) {
                    hideDialog();

                    LoginResponseModel loginModel = response.body();
                    boolean error = loginModel.getError();

                    if (!error) {
                        session.setLogin(true);
                        finish();
                        startActivity(new Intent(LogInActivity.this, StatisticActivity.class));


                    } else {

                        Toast.makeText(LogInActivity.this, R.string.kor_podaci_error, Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

                hideDialog();
                Log.d("Log", t.getMessage());
                Toast.makeText(LogInActivity.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
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
