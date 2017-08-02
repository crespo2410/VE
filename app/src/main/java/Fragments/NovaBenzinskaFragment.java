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

import Models.BenzinskaModel;
import Models.Ok;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NovaBenzinskaFragment extends Fragment {

    private static final String GRESKA = "errNovaBenzska";
    private Toolbar toolbar;
    private EditText naziv;
    private ArrayList<String> dohvaceniNaziviBenzinskihPostaja;


    public NovaBenzinskaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Postavljanje za omogućavanje optionsMenua
        setHasOptionsMenu(true);

        //dohvacanje liste koja sadrži nazive benzinskih postaja (OtherExpenses) preko bundlea
        Bundle dohvatiListe = getArguments();
        dohvaceniNaziviBenzinskihPostaja = dohvatiListe.getStringArrayList("listaNazivaBenzinskih");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.nova_benzinska_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //postavljanje appBara u istu boju koplatibilnu toolbaru, a što je tek podržano nakon LOLLIPOP verzije Androida
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }


        naziv = (EditText) getActivity().findViewById(R.id.nova_benz);


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nova_benzinska, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        menu.getItem(0).setIcon(R.drawable.ic_action_done);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //ako je odabran home button
        if (item.getItemId() == android.R.id.home) {

            getActivity().getSupportFragmentManager().popBackStack();

            //odnosno ako je odabrana icona strelice koja označava potvrdu unosa nove benzinske
        } else if (item.getItemId() == R.id.menu_icon1) {

            unesi_novu_benzinsku();
        }


        //Pravilno postavljanje naslova toolbara
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.tocenje_goriva);

        return super.onOptionsItemSelected(item);


    }


    /**
     * Metoda za unos novog naziva benzinske postaje
     */
    private void unesi_novu_benzinsku() {

        //ako smo nešto unesli za naziv
        if (!naziv.getText().toString().isEmpty()) {

            //provjeriti da li se već nalazi u bazi (provjera kroz listu u koju su spremljeni podaci iz baze)
            boolean provjera = false;
            for (int i = 0; i < dohvaceniNaziviBenzinskihPostaja.size(); i++) {
                if (naziv.getText().toString().equalsIgnoreCase(dohvaceniNaziviBenzinskihPostaja.get(i).toString())) {

                    provjera = true;
                    break;
                }
            }


            if (provjera == false) {


                String dohvaceno_ime = naziv.getText().toString();
                BenzinskaModel benz = new BenzinskaModel();
                benz.setNazivBenzinske(dohvaceno_ime);

                Call<Ok> benzinska_insertCall = RestClient.getInstance().getApiService().addBenzinska(benz);
                benzinska_insertCall.enqueue(new Callback<Ok>() {
                    @Override
                    public void onResponse(Call<Ok> call, Response<Ok> response) {
                        if (response.isSuccessful()) {
                            //kada je unos završen vrati se nazad u prijašnje prikazan fragment (sljedeći s backstacka)
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
        Toast.makeText(getActivity(), R.string.poruka_upozorenja_praznog_unosa, Toast.LENGTH_SHORT).show();
    }


}
