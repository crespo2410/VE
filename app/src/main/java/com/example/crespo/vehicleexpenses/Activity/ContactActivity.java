package com.example.crespo.vehicleexpenses.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

/**
 * Klasa aktivnosti Kontaktirajte nas
 */
public class ContactActivity extends HomeActivity {

    private EditText editTextName;
    private EditText editTextSubject;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_contact, frameLayout);
        toolbar.setTitle(R.string.kontaktirati);


        //inicijalizacija polja za slanje emaila
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(0).setIcon(R.drawable.ic_send_white_24dp);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_icon1:
                sendEmail();
            default:
                return super.onOptionsItemSelected(item);


        }

    }


    //metoda za slanje maila
    private void sendEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String recipient = getString(R.string.moja_mail_adresa);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        emailIntent.putExtra(Intent.EXTRA_TITLE, editTextName.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editTextSubject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, (editTextMessage.getText().toString() + "\n" + editTextName.getText().toString()));

        try {
            startActivity(Intent.createChooser(emailIntent, "Pošalji mail..."));
            cleanEditText();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, R.string.nema_klijenta, Toast.LENGTH_SHORT).show();
        }

    }


    private void cleanEditText(){

        editTextName.setText("");
        editTextSubject.setText("");
        editTextMessage.setText("");
    }

}
