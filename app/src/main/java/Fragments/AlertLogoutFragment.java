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

import com.example.crespo.vehicleexpenses.Activity.StartUpActivity;
import com.example.crespo.vehicleexpenses.R;

import helper.SessionManager;


public class AlertLogoutFragment extends DialogFragment {

    private Activity activity;
    private Context context;
    private SessionManager sessionManager;

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
        builder.setMessage(R.string.odjava_pitanje)
                .setPositiveButton(getString(R.string.Pozitivno), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        sessionManager = new SessionManager(getContext());
                        sessionManager.setLogin(false);
                        getActivity().finish();
                        startActivity(new Intent(getContext(), StartUpActivity.class));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.Negativno, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.dismiss();

                    }
                });


        AlertDialog dialog = builder.create();
        return dialog;
    }
}
