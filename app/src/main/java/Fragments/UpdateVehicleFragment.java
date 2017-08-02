package Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.VehicleActivity;
import com.example.crespo.vehicleexpenses.Activity.VehicleDetailActivity;
import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Models.GorivoModel;
import Models.Ok;
import Models.VehicleModel;
import Models.VehicleProducerModel;
import Models.VehicleTypeModel;
import Retrofit.api.client.RestClient;
import helper.SessionManager;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateVehicleFragment extends Fragment {

    public Toolbar toolbar;
    public int zapa_poz = 0;

    public ImageView icon_spinner_proizvodac;
    public ImageView icon_spinner_gorivo_vozilo;


    public Spinner spinner_tip_vozila;
    public Spinner spinner_naziv_proizvodac;
    public Spinner spinner_gorivo_vozila;


    public int pritisakTipke;
    public Menu menuReference;
    public String poz;
    public String naziv_vrste_vozila;
    public String naziv_vrste_goriva;
    public String naziv_vrste_proizvodaca;
    VehicleModel vehicleModel;
    ArrayList<GorivoModel> gorivoModels;
    ArrayList<String> gorivoModels2;
    ArrayList<VehicleProducerModel> naziviProizvodaca;
    ArrayList<String> naziviProizvodaca2;
    ArrayList<VehicleTypeModel> vrstaVozila;
    ArrayList<String> vrstaVozila2;
    private EditText tip_marke_ed, naziv_vozila_ed, reg_vozila_ed, sas_vozila_ed, god_vozila_ed, spremnik_vozila_ed, biljeske_vozila_ed;
    private FragmentManager fr;


    public UpdateVehicleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Intent intent = getActivity().getIntent();
        Bundle data = intent.getExtras();
        pritisakTipke = data.getInt("idButtona");
        poz = data.getString("pozicija");
        vehicleModel = intent.getParcelableExtra("ObjektVozila");


        if (vehicleModel == null) {
            vehicleModel = new VehicleModel();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fr = getActivity().getSupportFragmentManager();
        gorivoModels2 = new ArrayList<String>();
        naziviProizvodaca2 = new ArrayList<String>();
        vrstaVozila2 = new ArrayList<String>();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_update_vehicle, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary4));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark4));
        }


        tip_marke_ed = (EditText) getActivity().findViewById(R.id.tip_marke_ed);
        naziv_vozila_ed = (EditText) getActivity().findViewById(R.id.naziv_vozila_ed);
        reg_vozila_ed = (EditText) getActivity().findViewById(R.id.reg_vozila_ed);
        sas_vozila_ed = (EditText) getActivity().findViewById(R.id.sas_vozila_ed);
        god_vozila_ed = (EditText) getActivity().findViewById(R.id.god_vozila_ed);
        spremnik_vozila_ed = (EditText) getActivity().findViewById(R.id.spremnik_vozila_ed);
        biljeske_vozila_ed = (EditText) getActivity().findViewById(R.id.biljeske_vozila_ed);


        icon_spinner_proizvodac = (ImageView) getActivity().findViewById(R.id.icon_spinner_proizvodac);
        icon_spinner_gorivo_vozilo = (ImageView) getActivity().findViewById(R.id.icon_spinner_gorivo_vozila);


        spinner_tip_vozila = (Spinner) getActivity().findViewById(R.id.spinner_tip_vozila);
        spinner_naziv_proizvodac = (Spinner) getActivity().findViewById(R.id.spinner_proizvodac);
        spinner_gorivo_vozila = (Spinner) getActivity().findViewById(R.id.spinner_gorivo_vozila);


    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        menuReference = menu;


        if (pritisakTipke == R.id.menu_icon1) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);
            menuReference.getItem(1).setIcon(R.drawable.ic_action_delete).setVisible(true);

        } else if (pritisakTipke == R.id.buttonAddVehicle) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (pritisakTipke == R.id.buttonAddVehicle) {

                Intent intent = new Intent(getActivity(), VehicleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else if (pritisakTipke == R.id.menu_icon1) {

                Bundle b2 = new Bundle();
                b2.putString("pozicija", vehicleModel.getId().toString());
                b2.putParcelable("ObjToDisplay", vehicleModel);


                Intent intent = new Intent(getActivity(), VehicleDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("bundle_lista", b2);
                startActivity(intent);


            }


        } else if (item.getItemId() == R.id.menu_icon1) {
            if (pritisakTipke == R.id.buttonAddVehicle) {
                unesi();
            } else if (pritisakTipke == R.id.menu_icon1) {
                update();

            }
        } else if (item.getItemId() == R.id.menu_icon2) {
            startAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("SpemljeniObjekt", vehicleModel);
        outState.putString("spinner_Naziv_gor_v", naziv_vrste_goriva);
        outState.putString("spinner_Naziv_vr_v", naziv_vrste_vozila);
        outState.putString("spinner_Naziv_vr_p", naziv_vrste_proizvodaca);
        outState.putStringArrayList("naz_proi",naziviProizvodaca2);
        outState.putStringArrayList("vrsta_voz",vrstaVozila2);
        outState.putStringArrayList("nazivi_gorivo_v",gorivoModels2);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            vehicleModel = savedInstanceState.getParcelable("SpemljeniObjekt");
            naziv_vrste_goriva = savedInstanceState.getString("spinner_Naziv_gor_v");
            naziv_vrste_vozila = savedInstanceState.getString("spinner_Naziv_vr_v");
            naziv_vrste_proizvodaca = savedInstanceState.getString("spinner_Naziv_vr_p");
            naziviProizvodaca2 = savedInstanceState.getStringArrayList("naz_proi");
            vrstaVozila2 = savedInstanceState.getStringArrayList("vrsta_voz");
            gorivoModels2 = savedInstanceState.getStringArrayList("nazivi_gorivo_v");

        }

        postaviSpinnere(savedInstanceState);

        if (pritisakTipke != 0) {

            if (pritisakTipke == R.id.menu_icon1) {

                postaviVrijednostiUpdatea();

            } else if (pritisakTipke == R.id.buttonAddVehicle) {


            }


        }


    }


    private VehicleModel dajModelzaUnos() {
        VehicleModel v = new VehicleModel();

        v.setId(vehicleModel.getId());
        v.setBiljeske(biljeske_vozila_ed.getText().toString());
        v.setBrojSasije(sas_vozila_ed.getText().toString());
        if(!god_vozila_ed.getText().toString().isEmpty()) v.setGodina(Integer.parseInt(god_vozila_ed.getText().toString()));
        v.setHibrid("0");
        v.setKapacitetSpremnika1(spremnik_vozila_ed.getText().toString());
        v.setKapacitetSpremnika2(null);
        v.setNaziv(naziv_vrste_goriva);
        v.setNazivProizvodaca(naziv_vrste_proizvodaca);
        v.setNazivVozila(naziv_vozila_ed.getText().toString());
        v.setNazivVrste(naziv_vrste_vozila);
        v.setRegistracija(reg_vozila_ed.getText().toString());
        v.setTip(tip_marke_ed.getText().toString());


        return v;
    }


    public void postaviVrijednostiUpdatea() {


        tip_marke_ed.setText(vehicleModel.getTip());
        naziv_vozila_ed.setText(vehicleModel.getNazivVozila());
        reg_vozila_ed.setText(vehicleModel.getRegistracija());
        sas_vozila_ed.setText(vehicleModel.getBrojSasije());
        god_vozila_ed.setText(vehicleModel.getGodina().toString());
        spremnik_vozila_ed.setText(vehicleModel.getKapacitetSpremnika1().toString());
        biljeske_vozila_ed.setText(vehicleModel.getBiljeske());


    }


    public void postaviSpinnere(Bundle savedInstanceState) {


        icon_spinner_gorivo_vozilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaNazivaGoriva", gorivoModels2);


                NovoGorivoFragment novi = new NovoGorivoFragment();
                novi.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_updateVehicle, novi).addToBackStack("NoviNazivGoriva").commit();


            }
        });


        icon_spinner_proizvodac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaProizvodaca", naziviProizvodaca2);


                NoviProizvodacFragment novi = new NoviProizvodacFragment();
                novi.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_updateVehicle, novi).addToBackStack("NoviNazivProizvodaca").commit();


            }
        });

        if(savedInstanceState == null) {
            postaviSpinnerVrstaVozila();
            postaviSpinnerProizvodaca();
            postaviSpinnerGorivo();
        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vrstaVozila2);
            spinner_tip_vozila.setAdapter(adapter);

            if (pritisakTipke == R.id.menu_icon1) {

                spinner_tip_vozila.setSelection(vrstaVozila2.indexOf(vehicleModel.getNazivVrste()));

            } else if (pritisakTipke == R.id.buttonAddVehicle) {

                spinner_tip_vozila.setSelection(zapa_poz);

            }

            spinner_tip_vozila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    zapa_poz = i;
                    naziv_vrste_vozila = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, naziviProizvodaca2);
            spinner_naziv_proizvodac.setAdapter(adapter2);

            if (pritisakTipke == R.id.menu_icon1) {

                spinner_naziv_proizvodac.setSelection(vrstaVozila2.indexOf(vehicleModel.getNazivProizvodaca()));

            } else if (pritisakTipke == R.id.buttonAddVehicle) {

                spinner_naziv_proizvodac.setSelection(zapa_poz);

            }

            spinner_naziv_proizvodac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    zapa_poz = i;
                    naziv_vrste_proizvodaca = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });





            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gorivoModels2);
            spinner_gorivo_vozila.setAdapter(adapter3);

            if (pritisakTipke == R.id.menu_icon1) {

                spinner_gorivo_vozila.setSelection(gorivoModels2.indexOf(vehicleModel.getNaziv()));

            } else if (pritisakTipke == R.id.buttonAddVehicle) {

                spinner_gorivo_vozila.setSelection(zapa_poz);

            }

            spinner_gorivo_vozila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    zapa_poz = i;
                    naziv_vrste_goriva = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        }

    }


    public void postaviSpinnerVrstaVozila() {

        Call<ArrayList<VehicleTypeModel>> getVrstaVozila = RestClient.getInstance().getApiService().getVehicleType();
        getVrstaVozila.enqueue(new Callback<ArrayList<VehicleTypeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<VehicleTypeModel>> call, Response<ArrayList<VehicleTypeModel>> response) {

                if (response.isSuccessful()) {

                    vrstaVozila = response.body();

                    for (int i = 0; i < vrstaVozila.size(); i++) {

                        vrstaVozila2.add(vrstaVozila.get(i).getNazivVrste());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vrstaVozila2);
                    spinner_tip_vozila.setAdapter(adapter);

                    if (pritisakTipke == R.id.menu_icon1) {

                        spinner_tip_vozila.setSelection(vrstaVozila2.indexOf(vehicleModel.getNazivVrste()));

                    } else if (pritisakTipke == R.id.buttonAddVehicle) {

                        spinner_tip_vozila.setSelection(zapa_poz);

                    }

                    spinner_tip_vozila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            zapa_poz = i;
                            naziv_vrste_vozila = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ArrayList<VehicleTypeModel>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void postaviSpinnerProizvodaca() {

        Call<ArrayList<VehicleProducerModel>> getVrstaVozila = RestClient.getInstance().getApiService().getVehicleProducers();
        getVrstaVozila.enqueue(new Callback<ArrayList<VehicleProducerModel>>() {
            @Override
            public void onResponse(Call<ArrayList<VehicleProducerModel>> call, Response<ArrayList<VehicleProducerModel>> response) {

                if (response.isSuccessful()) {

                    naziviProizvodaca = response.body();

                    for (int i = 0; i < naziviProizvodaca.size(); i++) {

                        naziviProizvodaca2.add(naziviProizvodaca.get(i).getNazivProizvodaca());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, naziviProizvodaca2);
                    spinner_naziv_proizvodac.setAdapter(adapter);

                    if (pritisakTipke == R.id.menu_icon1) {

                        spinner_naziv_proizvodac.setSelection(vrstaVozila2.indexOf(vehicleModel.getNazivProizvodaca()));

                    } else if (pritisakTipke == R.id.buttonAddVehicle) {

                        spinner_naziv_proizvodac.setSelection(zapa_poz);

                    }

                    spinner_naziv_proizvodac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            zapa_poz = i;
                            naziv_vrste_proizvodaca = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ArrayList<VehicleProducerModel>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void postaviSpinnerGorivo() {

        Call<ArrayList<GorivoModel>> getVrstaGoriva = RestClient.getInstance().getApiService().getGorivo();
        getVrstaGoriva.enqueue(new Callback<ArrayList<GorivoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GorivoModel>> call, Response<ArrayList<GorivoModel>> response) {

                if (response.isSuccessful()) {

                    gorivoModels = response.body();

                    for (int i = 0; i < gorivoModels.size(); i++) {

                        gorivoModels2.add(gorivoModels.get(i).getNaziv());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gorivoModels2);
                    spinner_gorivo_vozila.setAdapter(adapter);

                    if (pritisakTipke == R.id.menu_icon1) {

                        spinner_gorivo_vozila.setSelection(gorivoModels2.indexOf(vehicleModel.getNaziv()));

                    } else if (pritisakTipke == R.id.buttonAddVehicle) {

                        spinner_gorivo_vozila.setSelection(zapa_poz);

                    }

                    spinner_gorivo_vozila.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            zapa_poz = i;
                            naziv_vrste_goriva = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ArrayList<GorivoModel>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
            }
        });


    }



    private boolean provjeraUnosa() {
        if(!naziv_vozila_ed.getText().toString().isEmpty() && !spremnik_vozila_ed.getText().toString().isEmpty() && !god_vozila_ed.getText().toString().isEmpty())
        return true;
        else return false;
    }


    private void unesi() {


        if (provjeraUnosa() == true) {

            VehicleModel model = dajModelzaUnos();

            Call<Ok> unosVozilaRepos = RestClient.getInstance().getApiService().insertVehicle(model);
            unosVozilaRepos.enqueue(new Callback<Ok>() {
                @Override
                public void onResponse(Call<Ok> call, Response<Ok> response) {

                    if (response.isSuccessful())
                        Toast.makeText(getActivity(), R.string.vozilo_dodano, Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(getActivity(), VehicleActivity.class));

                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.setFirstTime(false);
                }

                @Override
                public void onFailure(Call<Ok> call, Throwable t) {

                    Log.d("greska_unosServisa", t.getMessage().toString());
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                }
            });


        } else Toast.makeText(getActivity(), R.string.prazni_elementi, Toast.LENGTH_SHORT).show();

    }




    private void startAlertDialog() {

        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(vehicleModel.getId()));
        bundle_id.putString("Naziv_Klase", "VehicleDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(getActivity().getSupportFragmentManager(), "Alert_Delete");
    }






    private void update() {


        if (provjeraUnosa() == true) {

           final VehicleModel model = dajModelzaUnos();

            Call<Ok> updateVehiclerepos = RestClient.getInstance().getApiService().updateVehicle(model, vehicleModel.getId());


            updateVehiclerepos.enqueue(new Callback<Ok>() {
                @Override
                public void onResponse(Call<Ok> call, Response<Ok> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), R.string.vozilo_azurirano, Toast.LENGTH_SHORT).show();
                        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
                        manager.setVehicleName(model.getNazivVozila(),model.getNazivProizvodaca());

                        Bundle b2 = new Bundle();
                        b2.putString("pozicija", model.getId().toString());
                        b2.putBoolean("zastavica_update", true);
                        b2.putParcelable("ObjToDisplay", dajModelzaUnos());


                        Intent in = new Intent(getActivity(), VehicleDetailActivity.class);
                        in.putExtra("bundle_lista", b2);
                        startActivity(in);
                    }
                }

                @Override
                public void onFailure(Call<Ok> call, Throwable t) {

                    Log.d("greska_update_Service", t.getMessage());
                    Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

                }
            });
        } else Toast.makeText(getActivity(), R.string.prazni_elementi, Toast.LENGTH_SHORT).show();

    }


}
