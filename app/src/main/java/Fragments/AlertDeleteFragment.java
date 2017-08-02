package Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.OtherExpensesActivity;
import com.example.crespo.vehicleexpenses.Activity.RefuelingExpensesActivity;
import com.example.crespo.vehicleexpenses.Activity.ServiceExpencesActivity;
import com.example.crespo.vehicleexpenses.Activity.VehicleActivity;
import com.example.crespo.vehicleexpenses.R;

import Models.Ok;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * DialogFragment klasa u kojoj se dešava brisanje elemenata
 */
public class AlertDeleteFragment extends DialogFragment {

    public static final String ID = "iD";
    public static final String NAZIV_KLASE = "Naziv_Klase";
    public static final String TOCENJE_DETAIL_CLASS = "TocenjeDetailClass";
    public static final String SERVISI_DETAIL_CLASS = "ServisiDetailClass";
    public static final String OSTALI_TROSKOVI_DETAIL_CLASS = "OstaliTroskoviDetailClass";
    public static final String VOZILA_DETAIL_CLASS = "VehicleDetailClass";
    public static final String BRISANJE = "del_";
    private int id;
    private String naziv_klase;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //iz bundle-a tj. drugog fragmenta dohvaćamo id i naziv_klase
        Bundle bundle = getArguments();
        id = bundle.getInt(ID);
        naziv_klase = bundle.getString(NAZIV_KLASE);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.AlertBrisanje)
                .setPositiveButton(R.string.Pozitivno, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //pokrecemo brisanje
                        brisanje();

                        //s obzirom na naziv klase pokreće se željena aktivnost
                        switch (naziv_klase) {

                            case TOCENJE_DETAIL_CLASS:
                                startActivity(new Intent(activity, RefuelingExpensesActivity.class));
                                break;
                            case SERVISI_DETAIL_CLASS:
                                startActivity(new Intent(activity, ServiceExpencesActivity.class));
                                break;


                            case OSTALI_TROSKOVI_DETAIL_CLASS:
                                startActivity(new Intent(activity, OtherExpensesActivity.class));
                                break;

                            case VOZILA_DETAIL_CLASS:
                                startActivity(new Intent(activity, VehicleActivity.class));
                                break;


                            default:
                                break;


                        }

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


    /**
     * metoda brisanja, elementi se brišu po id-u i korisnik dobiva poruku
     */
    private void brisanje() {


        switch (naziv_klase) {

            case TOCENJE_DETAIL_CLASS:

                //htttp poziv prema poslužitelju na kojem je baza preko API-a
                Call<Ok> deleteRepos = RestClient.getInstance().getApiService().deleteTocenje(id);

                //druga dretva
                deleteRepos.enqueue(new Callback<Ok>() {
                    @Override
                    public void onResponse(Call<Ok> call, Response<Ok> response) {

                        //ako je sve proslo u redu ispisi poruku
                        if (response.isSuccessful())
                            Toast.makeText(activity, R.string.sadrzaj_obrisan_poruka, Toast.LENGTH_SHORT).show();


                    }

                    //desila se neka greska
                    @Override
                    public void onFailure(Call<Ok> call, Throwable t) {

                        Log.d(BRISANJE + TOCENJE_DETAIL_CLASS, t.getMessage().toString());
                        Toast.makeText(activity, R.string.poruka_greske, Toast.LENGTH_SHORT).show();

                    }
                });

                break;


            case SERVISI_DETAIL_CLASS:

                Call<Ok> serviceDelete = RestClient.getInstance().getApiService().deleteServisi(id);
                serviceDelete.enqueue(new Callback<Ok>() {
                    @Override
                    public void onResponse(Call<Ok> call, Response<Ok> response) {
                        if (response.isSuccessful())
                            Toast.makeText(activity, R.string.sadrzaj_obrisan_poruka, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Ok> call, Throwable t) {

                        Log.d(BRISANJE + SERVISI_DETAIL_CLASS, t.getMessage().toString());
                        Toast.makeText(activity, R.string.poruka_greske, Toast.LENGTH_SHORT).show();


                    }
                });

                break;


            case OSTALI_TROSKOVI_DETAIL_CLASS:

                Call<Ok> ostaliTroskoviDelete = RestClient.getInstance().getApiService().deleteOstaleTroskak(id);
                ostaliTroskoviDelete.enqueue(new Callback<Ok>() {
                    @Override
                    public void onResponse(Call<Ok> call, Response<Ok> response) {
                        if (response.isSuccessful())
                            Toast.makeText(activity, R.string.sadrzaj_obrisan_poruka, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Ok> call, Throwable t) {
                        Log.d(BRISANJE, t.getMessage().toString());
                        Toast.makeText(activity, R.string.poruka_greske, Toast.LENGTH_SHORT).show();


                    }
                });

            case VOZILA_DETAIL_CLASS:
                Call<Ok> vehicleDelete = RestClient.getInstance().getApiService().deleteVehicle(id);
                vehicleDelete.enqueue(new Callback<Ok>() {
                    @Override
                    public void onResponse(Call<Ok> call, Response<Ok> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(activity, R.string.sadrzaj_obrisan_poruka, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ok> call, Throwable t) {

                        Log.d(BRISANJE + VOZILA_DETAIL_CLASS, t.getMessage().toString());
                        Toast.makeText(activity, R.string.poruka_greske, Toast.LENGTH_SHORT).show();
                    }
                });


            default:
                break;

        }


    }


}
