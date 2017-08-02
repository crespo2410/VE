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

import com.example.crespo.vehicleexpenses.Activity.LogInActivity;
import com.example.crespo.vehicleexpenses.R;

import helper.SessionManager;

/**
 * Dialog fragment klasa za prikaz dijaloga da nismo prijavljeni korisnik (kod prijevoda)
 */
public class AlertNotLogInFragment extends DialogFragment {


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
        builder.setMessage(getString(R.string.no_logIn))
                .setPositiveButton(getString(R.string.prijavi_se), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //odgovor Da, završi aktivnost i pošalji na LogInActivity aktivnost
                        getActivity().finish();
                        startActivity(new Intent(getContext(), LogInActivity.class));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.odustani, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //zatvori dijalog
                        dialog.dismiss();

                    }
                });


        AlertDialog dialog = builder.create();
        return dialog;
    }
}
