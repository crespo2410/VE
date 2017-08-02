package Fragments;


import android.app.Activity;
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
 * Fragment koji je isti kao i NoviNazivOstalogTroska
 */
public class NoviNazivOstalogTroskaFragment extends Fragment {

    private Toolbar toolbar;
    private EditText naziv;
    private EditText cijena;

    private ArrayList<String> dohvaceniNaziviOstalihTroskova;

    RecyclerValueInterfaceOther recyclerValueInterfaceOther;



    public NoviNazivOstalogTroskaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle dohvatiListe = getArguments();
        dohvaceniNaziviOstalihTroskova  = dohvatiListe.getStringArrayList("listaNazivaServisa");




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




        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_naziv_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark3));
        }



        naziv = (EditText) getActivity().findViewById(R.id.novo_ime_usluge2);
        cijena = (EditText) getActivity().findViewById(R.id.nova_cijena_usluge2);




    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);

        menu.getItem(0).setIcon(R.drawable.ic_action_done);





    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            getActivity().getSupportFragmentManager().popBackStack();

        } else if(item.getItemId() == R.id.menu_icon1) {

          unesiNoviNazivOstalogTroska();

        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.novi_trosak_title);


        return super.onOptionsItemSelected(item);


    }

    private void unesiNoviNazivOstalogTroska() {
        boolean provjera = false;
        for (int i = 0; i < dohvaceniNaziviOstalihTroskova.size(); i++) {
            if (naziv.getText().toString().equalsIgnoreCase(dohvaceniNaziviOstalihTroskova.get(i).toString())) {

                provjera = true;
                break;
            }
        }


        if (provjera == false) {

            final ExpensesOtherExpensesModel t = new ExpensesOtherExpensesModel();
            t.setNazivOstalogTroska(naziv.getText().toString());
            t.setIznos(cijena.getText().toString());


            OstaliTroskoviNaziviModel ostaliTroskoviNaziviModel = new OstaliTroskoviNaziviModel();
            ostaliTroskoviNaziviModel.setNazivOstalogTroska(naziv.getText().toString());

            Call<Ok> noviNazivOstalogTroskaCall = RestClient.getInstance().getApiService().addNazivOstalogTroska(ostaliTroskoviNaziviModel);
            noviNazivOstalogTroskaCall.enqueue(new Callback<Ok>() {
                @Override
                public void onResponse(Call<Ok> call, Response<Ok> response) {
                    if (response.isSuccessful()) {
                        recyclerValueInterfaceOther.setValueforRecycler(t);
                        getActivity().getSupportFragmentManager().popBackStack();

                    }
                }

                @Override
                public void onFailure(Call<Ok> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

                }
            });


        }else{

            Toast.makeText(getActivity(),R.string.poruka_pogreske_unosa_u_bazu, Toast.LENGTH_SHORT).show();
            naziv.setText("");
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            recyclerValueInterfaceOther = (NoviNazivOstalogTroskaFragment.RecyclerValueInterfaceOther) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement recyclerValueInterface");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        recyclerValueInterfaceOther = null;

    }

    public interface RecyclerValueInterfaceOther{
        public void setValueforRecycler(ExpensesOtherExpensesModel expensesOtherExpensesModel);
    }




}
