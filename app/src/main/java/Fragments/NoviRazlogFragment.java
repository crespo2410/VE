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
import Models.RazlogModel;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment koji je zadužen za Unos novog naziva razloga. Radi na istom principu kao i prethodni
 * fragmenti koji su bili zaduženi za unos novih imena (npr. Benziska - NovaBenzinskaFragment, NazivServisa, NoviNazivOstalogTroska - pogledaj njih)
 */
public class NoviRazlogFragment extends Fragment {

    private static final String GRESKA = "errNoviRazl";
    private Toolbar toolbar;
    private EditText naziv_ed;

    private ArrayList<String> dohvaceniNaziviRazloga;

    private String naziv_klase;

    public NoviRazlogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle dohvatiListe = getArguments();
        dohvaceniNaziviRazloga = dohvatiListe.getStringArrayList("listaNazivaRazloga");
        naziv_klase = dohvatiListe.getString("Dolazak");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_razlog_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }

        naziv_ed = (EditText) getActivity().findViewById(R.id.naziv_razlog_ed);


        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_novi_razlog, container, false);
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

            unesi_novi_razlog();
        }


        if(naziv_klase.equals("KlasaRefuelling")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.tocenje_goriva);
        }else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_ostali_trosak_title);
        }

        return super.onOptionsItemSelected(item);

    }


    private void unesi_novi_razlog() {


        boolean provjera = false;
        for (int i = 0; i < dohvaceniNaziviRazloga.size(); i++) {
            if (naziv_ed.getText().toString().equalsIgnoreCase(dohvaceniNaziviRazloga.get(i).toString())) {

                provjera = true;
                break;
            }
        }


        if (provjera == false) {
            String dohvaceno_ime = naziv_ed.getText().toString();
            RazlogModel razlog = new RazlogModel();
            razlog.setNazivRazloga(dohvaceno_ime);

            Call<Ok> noviRazlogInsertCall = RestClient.getInstance().getApiService().addRazlog(razlog);
            noviRazlogInsertCall.enqueue(new Callback<Ok>() {
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
            naziv_ed.setText("");
        }


        if(naziv_klase.equals("KlasaRefuelling")) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.tocenje_goriva);
        }else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_ostali_trosak_title);
        }
    }
}
