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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Models.GorivoModel;
import Models.Ok;
import Models.PodvrstaModel;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment koji je zadužen za Unos novog naziva goriva tj. pokretača vozila. Radi na istom principu kao i prethodni
 * fragmenti koji su bili zaduženi za unos novih imena (npr. Benziska - NovaBenzinskaFragment, NazivServisa, NoviNazivOstalogTroska - pogledaj njih)
 * <p>
 * Ovaj fragment ima i dodatan spinner po čemu se razlikuje od ostalih navedenih
 */
public class NovoGorivoFragment extends Fragment {


    private static final String GRESKA = "errNovoGorivo";
    private Toolbar toolbar;
    private EditText ed_naziv;
    private Spinner podvrsta_spinner;
    private String spinner_naziv;
    private ArrayList<PodvrstaModel> lista;
    private ArrayList<String> lista2 = new ArrayList<>();
    private ArrayList<String> dohvaceniNaziviGoriva;


    public NovoGorivoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle dohvatiListe = getArguments();
        dohvaceniNaziviGoriva = dohvatiListe.getStringArrayList("listaNazivaGoriva");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novo_gorivo);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }

        ed_naziv = (EditText) getActivity().findViewById(R.id.ed_dodaj_razlog);
        podvrsta_spinner = (Spinner) getActivity().findViewById(R.id.spinner_dodaj_povrstu);

        postavi_spinner();


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_novo_gorivo, container, false);
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

            dodaj_novo_gorivo();
        }


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.tocenje_goriva);
        return super.onOptionsItemSelected(item);
    }


    private void dodaj_novo_gorivo() {


        boolean provjera = false;
        for (int i = 0; i < dohvaceniNaziviGoriva.size(); i++) {
            if (ed_naziv.getText().toString().equalsIgnoreCase(dohvaceniNaziviGoriva.get(i).toString())) {

                provjera = true;
                break;
            }
        }


        if (provjera == false) {


            String naziv = ed_naziv.getText().toString();

            GorivoModel gorivo = new GorivoModel();
            gorivo.setNaziv(naziv);
            gorivo.setNazivPodvrstaGoriva(spinner_naziv);


            Call<Ok> gorivo_repos = RestClient.getInstance().getApiService().addGorivo(gorivo);
            gorivo_repos.enqueue(new Callback<Ok>() {
                @Override
                public void onResponse(Call<Ok> call, Response<Ok> response) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }

                @Override
                public void onFailure(Call<Ok> call, Throwable t) {
                    Log.d(GRESKA, t.getMessage());
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            Toast.makeText(getActivity(), R.string.poruka_pogreske_unosa_u_bazu, Toast.LENGTH_SHORT).show();
            ed_naziv.setText("");
        }

    }


    /**
     * Metoda za postavljanje spinnera koji sadrži vrste oblika goriva tj. pokretača vozila (tekucine, plin, struja)
     */
    public void postavi_spinner() {

        Call<ArrayList<PodvrstaModel>> podvrsta_repos = RestClient.getInstance().getApiService().getPodvrsta();
        podvrsta_repos.enqueue(new Callback<ArrayList<PodvrstaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PodvrstaModel>> call, Response<ArrayList<PodvrstaModel>> response) {

                lista = response.body();

                //stvaranje nove liste koja sadrži isključivo Nazive(String) objekte koju pak predajemo
                //za spinner adapter
                for (int i = 0; i < lista.size(); i++) {

                    lista2.add(lista.get(i).getNazivPodvrste());

                }


                //Postavljanje adaptera za spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lista2);
                podvrsta_spinner.setAdapter(adapter);


                //ItemSelectedListener da se zna koji je element odabran
                podvrsta_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        spinner_naziv = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<PodvrstaModel>> call, Throwable t) {

                Toast.makeText(getContext(), getString(R.string.poruka_greske_dohvata), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
