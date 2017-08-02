package Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.crespo.vehicleexpenses.Activity.RegisterActivity;
import com.example.crespo.vehicleexpenses.R;


/**
 * Klasa za izgradnju DialogFragmenta koji nudi dijalog s mogucnoscu polaska prema izradi korisnickog racuna
 */
public class CreateAccountFragment extends DialogFragment {

    private Activity activity;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        this.context = context;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.zabrana_pristupa))
                .setPositiveButton(getString(R.string.racun_izrada), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //ako da odvedi korisnika na registraciju - RegisterActivity aktivnost
                        startActivity(new Intent(getContext(), RegisterActivity.class));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.odustani), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //ako ne zatvori dijalog
                        dialog.dismiss();

                    }
                });

        AlertDialog dialog = builder.create();
        return dialog;


    }
}