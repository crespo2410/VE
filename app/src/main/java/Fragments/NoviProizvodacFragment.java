package Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Models.Ok;
import Models.VehicleProducerModel;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment koji je zadužen za Unos novog naziva proizvođaca vozila. Radi na istom principu kao i prethodni
 * fragmenti koji su bili zaduženi za unos novih imena (npr. Benziska - NovaBenzinskaFragment, NazivServisa, NoviNazivOstalogTroska - pogledaj njih)
 */
public class NoviProizvodacFragment extends Fragment {


    private static final String GRESKA = "errNoviProizvodac";
    private Toolbar toolbar;
    private EditText naziv;
    private ArrayList<String> dohvaceniProizvodaci;

    public NoviProizvodacFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle dohvatiListe = getArguments();
        dohvaceniProizvodaci = dohvatiListe.getStringArrayList("listaProizvodaca");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_novi_proizvodac, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_proizvodac_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }

        naziv = (EditText) getActivity().findViewById(R.id.novi_proizvodac);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        menu.getItem(0).setIcon(R.drawable.ic_action_done);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            getActivity().getSupportFragmentManager().popBackStack();

        } else if (item.getItemId() == R.id.menu_icon1) {

            unesi_novog_proizvodaca();
        }


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_servis);

        return super.onOptionsItemSelected(item);


    }


    private void unesi_novog_proizvodaca() {

        boolean provjera = false;

        for (int i = 0; i < dohvaceniProizvodaci.size(); i++) {

            if (naziv.getText().toString().equalsIgnoreCase(dohvaceniProizvodaci.get(i).toString())) {

                provjera = true;
            }
        }


        if (provjera == false) {

            String dohvaceno_ime = naziv.getText().toString();

            VehicleProducerModel vehicleProducerModel = new VehicleProducerModel();
            vehicleProducerModel.setNazivProizvodaca(dohvaceno_ime);


            Call<Ok> noviProizvodacInserCall = RestClient.getInstance().getApiService().insertVehicleProducer(vehicleProducerModel);
            noviProizvodacInserCall.enqueue(new Callback<Ok>() {
                @Override
                public void onResponse(Call<Ok> call, Response<Ok> response) {

                    if (response.isSuccessful()) {

                        getActivity().getSupportFragmentManager().popBackStack();

                    }

                }

                @Override
                public void onFailure(Call<Ok> call, Throwable t) {
                    Log.d(GRESKA, t.getMessage());
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            Toast.makeText(getActivity(), R.string.poruka_pogreske_unosa_u_bazu, Toast.LENGTH_SHORT).show();
            naziv.setText("");
        }

    }


}
