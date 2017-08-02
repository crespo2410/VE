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

import Models.ExpensesOtherExpensesModel;
import Models.Ok;
import Models.OstaliTroskoviNaziviModel;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Fragment koji je isti kao i NoviNazivOstalogTroskaFragment, samo je malo izmjenjen zbog potrebe
 * izrade Podsjetnika
 */
public class NoviNazivOstalogTroska extends Fragment {

    private Toolbar toolbar;
    private EditText naziv;
    private EditText cijena;
    private ImageView imageView;
    private ArrayList<String> dohvaceniNaziviOstalihTroskova;


    public NoviNazivOstalogTroska() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //dohvacanje liste koja sadrži nazive ostalihTroskova (OtherExpenses) preko bundlea
        Bundle dohvatiListe = getArguments();
        dohvaceniNaziviOstalihTroskova = dohvatiListe.getStringArrayList("listaNazivaServisa");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_novi_naziv_ostalog_troska, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_naziv_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //postavljanje appBara u istu boju koplatibilnu toolbaru, a što je tek podržano nakon LOLLIPOPA
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }


        naziv = (EditText) getActivity().findViewById(R.id.novo_ime_usluge2);
        cijena = (EditText) getActivity().findViewById(R.id.nova_cijena_usluge2);
        cijena.setVisibility(View.INVISIBLE);
        imageView = (ImageView) getActivity().findViewById(R.id.slika_dodaj_razlog5);
        imageView.setVisibility(View.INVISIBLE);


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

            getActivity().getSupportFragmentManager().popBackStack();

            //ako se odabire potvrda unosa novog naziva benzinske
        } else if (item.getItemId() == R.id.menu_icon1) {

            unesiNoviNazivOstalogTroska();

        }
        return super.onOptionsItemSelected(item);


    }

    /**
     * Metoda za unos novog naziva Ostalog troška
     */
    private void unesiNoviNazivOstalogTroska() {


        //ako smo nešto unesli za naziv
        if (!naziv.getText().toString().isEmpty()) {

            //provjeriti da li se već nalazi u bazi (provjera kroz listu u koju su spremljeni podaci iz baze)
            boolean provjera = false;
            for (int i = 0; i < dohvaceniNaziviOstalihTroskova.size(); i++) {
                if (naziv.getText().toString().equalsIgnoreCase(dohvaceniNaziviOstalihTroskova.get(i).toString())) {

                    provjera = true;
                    break;
                }
            }


            //ako nema tog naziva u bazi, slobodno unesi novu
            if (provjera == false) {

                final ExpensesOtherExpensesModel t = new ExpensesOtherExpensesModel();
                t.setNazivOstalogTroska(naziv.getText().toString());
                t.setIznos(cijena.getText().toString());


                OstaliTroskoviNaziviModel ostaliTroskoviNaziviModel = new OstaliTroskoviNaziviModel();
                ostaliTroskoviNaziviModel.setNazivOstalogTroska(naziv.getText().toString());

                Call<Ok> noviServisInsertCall = RestClient.getInstance().getApiService().addNazivOstalogTroska(ostaliTroskoviNaziviModel);
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

        } else {
            Toast.makeText(getActivity(), R.string.poruka_upozorenja_praznog_unosa, Toast.LENGTH_SHORT).show();
        }
    }


}
