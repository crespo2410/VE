package Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.UpdateVehicleActivity;
import com.example.crespo.vehicleexpenses.Activity.VehicleDetailActivity;
import com.example.crespo.vehicleexpenses.R;

import java.io.File;
import java.util.ArrayList;

import Adapters.VehicleAdapter;
import Interfaces.CardItemClickListener;
import Models.VehicleModel;
import Retrofit.api.client.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa fragmenta koji prikazuje listu vozila
 */
public class VehicleListFragment extends Fragment {

    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private RecyclerView recyclerView;
    private VehicleAdapter vehicleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<VehicleModel> lista_vozila;
    private FloatingActionButton button;
    private TextView empty;
    private File pdfFile;


    public VehicleListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_list, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            lista_vozila = savedInstanceState.getParcelableArrayList("ListaVozilaBundle");
        }

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerViewVehicle);
        button = (FloatingActionButton) getView().findViewById(R.id.buttonAddVehicle);
        button.setImageResource(R.drawable.ic_note_add_white_48dp);
        empty = (TextView) getActivity().findViewById(R.id.emptyVehicleList);


        postaviListu(savedInstanceState);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateVehicleActivity.class);
                intent.putExtra("idButtona", button.getId());
                startActivity(intent);

            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_icon1:

            default:

                return super.onOptionsItemSelected(item);


        }

    }





    private void postaviListu(Bundle savedInstanceState){


        if(savedInstanceState == null) {
            Call<ArrayList<VehicleModel>> getVehiclesDataCall = RestClient.getInstance().getApiService().getVehicle();

            getVehiclesDataCall.enqueue(new Callback<ArrayList<VehicleModel>>() {
                @Override
                public void onResponse(Call<ArrayList<VehicleModel>> call, Response<ArrayList<VehicleModel>> response) {


                    if (response.isSuccessful()) {


                        lista_vozila = response.body();
                        vehicleAdapter = new VehicleAdapter(getContext(), lista_vozila);
                        vehicleAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                            @Override
                            public void itemClicked(View view, int position) {


                                VehicleModel vehicleModel = lista_vozila.get(position);

                                Bundle bundle = new Bundle();
                                bundle.putString("pozicija", String.valueOf(lista_vozila.get(position).getId()));
                                bundle.putParcelable("ObjToDisplay", vehicleModel);

                                Intent intent = new Intent(getActivity(), VehicleDetailActivity.class);
                                intent.putExtra("bundle_lista", bundle);
                                startActivity(intent);


                            }
                        });


                        recyclerView.setAdapter(vehicleAdapter);
                        emptyCheck();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<VehicleModel>> call, Throwable t) {


                    Log.d("ErrRetro_VozilaLista", t.getMessage());
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


                }
            });


            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        }else{

            vehicleAdapter = new VehicleAdapter(getContext(), lista_vozila);
            vehicleAdapter.setClickListenerforRecycler(new CardItemClickListener() {
                @Override
                public void itemClicked(View view, int position) {


                    VehicleModel vehicleModel = lista_vozila.get(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("pozicija", String.valueOf(lista_vozila.get(position).getId()));
                    bundle.putParcelable("ObjToDisplay", vehicleModel);

                    Intent intent = new Intent(getActivity(), VehicleDetailActivity.class);
                    intent.putExtra("bundle_lista", bundle);
                    startActivity(intent);


                }
            });


            recyclerView.setAdapter(vehicleAdapter);
            emptyCheck();

            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);

        }

    }

    private void emptyCheck() {
        if (lista_vozila.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ListaVozilaBundle",  lista_vozila);

    }


}
