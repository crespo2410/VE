package Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Adapters.HistoryAdapter;
import Interfaces.CardItemClickListener;
import Models.HistoryModel;
import Models.ParametersModel;
import Retrofit.api.client.RestClient;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private ArrayList<HistoryModel> historyModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView empty;
    private RetainedFragment mRetainedFragment;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Registracija OnSharedPreferenceChangeListenera
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

        // pokušaj korištenja reTainedFragmenta
        FragmentManager fm = getFragmentManager();
        mRetainedFragment = (RetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        // create the fragment and data the first time
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new RetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
            mRetainedFragment.setData(historyModels);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerHistory);
        empty = (TextView) view.findViewById(R.id.emptyHistoryList);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            historyModels = savedInstanceState.getParcelableArrayList("ListaPovijestBundle");
        }
        getHistoryData(savedInstanceState);

    }


    /**
     * Stvaranje modela koji sadrži parametre za slanje
     *
     * @return - vraća model koji se predaje prilikom poziva prema serveru
     */
    ParametersModel getParametersModel() {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        ParametersModel parametersModel = new ParametersModel();
        parametersModel.setVozilo_id(manager.getVehicleId());
        return parametersModel;

    }


    /**
     * metoda koja provjerava ukoliko nema zapisa da se postavi poruka da nema zapisa te
     * da se može dodati novi
     */
    private void emptyCheck() {
        if (historyModels.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Metoda unutar koje se dešava upit prema bazi na Serveru kako bi dobili podatke o povijesti radnji unutar aplikacije
     * @param savedInstanceState
     */
    private void getHistoryData(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            final Call<ArrayList<HistoryModel>> getHisData = RestClient.getInstance().getApiService().getHistory(getParametersModel());
            getHisData.enqueue(new Callback<ArrayList<HistoryModel>>() {
                @Override
                public void onResponse(Call<ArrayList<HistoryModel>> call, Response<ArrayList<HistoryModel>> response) {

                    // ako je response uspješan
                    if (response.isSuccessful()) {

                        //Postavi adapter
                        historyModels = response.body();
                        historyAdapter = new HistoryAdapter(getContext(), historyModels);
                        historyAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                            @Override
                            public void itemClicked(View view, int position) {

                            }
                        });


                        recyclerView.setAdapter(historyAdapter);
                        emptyCheck();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<HistoryModel>> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                }
            });

            historyAdapter = new HistoryAdapter(getContext(), historyModels);
            recyclerView.setAdapter(historyAdapter);
            emptyCheck();
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);


        }else{

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        //Odregistriraj listener koji je registriran u onCreate
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ListaPovijestBundle",  historyModels);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        //osviježi pogled
        recyclerView.getAdapter().notifyDataSetChanged();
    }


}
