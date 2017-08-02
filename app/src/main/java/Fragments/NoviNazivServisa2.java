package Fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Models.Ok;
import Models.ServiceExpensesModel;
import Models.ServiceNameModel;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Fragment koji je zadužen za Unos novog naziva servisa. Radi na istom principu kao i prethodni
 * fragmenti koji su bili zaduženi za unos novih imena (npr. Benziska - NovaBenzinskaFragment, NazivServisa, NoviNazivOstalogTroska - pogledaj njih)
 */

public class NoviNazivServisa2 extends Fragment {

    private Toolbar toolbar;
    private EditText naziv;
    private EditText cijena;
    private ImageView imageView;


    private ArrayList<String> dohvaceniNaziviServisa;


    public NoviNazivServisa2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle dohvatiListe = getArguments();
        dohvaceniNaziviServisa = dohvatiListe.getStringArrayList("listaNazivaServisa");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_novi_naziv_servisa, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_naziv_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }


        naziv = (EditText) getActivity().findViewById(R.id.novo_ime_usluge);
        cijena = (EditText) getActivity().findViewById(R.id.nova_cijena_usluge);
        imageView = (ImageView) getActivity().findViewById(R.id.slika_dodaj_razlog5);
        imageView.setVisibility(View.INVISIBLE);
        cijena.setVisibility(View.INVISIBLE);


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

            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.podsjetnik_title);
            toolbar.setBackgroundColor(getResources().getColor(R.color.primary2));

            getFragmentManager().popBackStack();

        } else if (item.getItemId() == R.id.menu_icon1) {

            unesiNoviNazivServisa();
        }


        return super.onOptionsItemSelected(item);


    }

    private void unesiNoviNazivServisa() {

        if (!naziv.getText().toString().isEmpty()) {

            boolean provjera = false;
            for (int i = 0; i < dohvaceniNaziviServisa.size(); i++) {
                if (naziv.getText().toString().equalsIgnoreCase(dohvaceniNaziviServisa.get(i).toString())) {

                    provjera = true;
                    break;
                }
            }


            if (provjera == false) {

                final ServiceExpensesModel t = new ServiceExpensesModel();
                t.setNazivServisa(naziv.getText().toString());
                t.setIznos(cijena.getText().toString());


                ServiceNameModel serviceNameModel = new ServiceNameModel();
                serviceNameModel.setNazivServisa(naziv.getText().toString());

                Call<Ok> noviServisInsertCall = RestClient.getInstance().getApiService().addNazivServisa(serviceNameModel);
                noviServisInsertCall.enqueue(new Callback<Ok>() {
                    @Override
                    public void onResponse(Call<Ok> call, Response<Ok> response) {
                        if (response.isSuccessful()) {

                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.podsjetnik_title);
                            toolbar.setBackgroundColor(getResources().getColor(R.color.primary2));

                            getActivity().getSupportFragmentManager().popBackStack();

                        }
                    }

                    @Override
                    public void onFailure(Call<Ok> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


                    }
                });


            } else {

                Toast.makeText(getActivity(), R.string.poruka_pogreske_unosa_u_bazu, Toast.LENGTH_SHORT).show();
                naziv.setText("");
            }

        } else
            Toast.makeText(getActivity(), R.string.poruka_upozorenja_praznog_unosa, Toast.LENGTH_SHORT).show();
    }


}
