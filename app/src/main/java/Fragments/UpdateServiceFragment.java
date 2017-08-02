package Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.ServiceExpencesActivity;
import com.example.crespo.vehicleexpenses.Activity.ServiceExpensesDetail;
import com.example.crespo.vehicleexpenses.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Adapters.ServicesUpdateAdapter;
import Interfaces.ProvjeraCallBack;
import Models.DistanceValidationModel;
import Models.ObrtniciModel;
import Models.Ok;
import Models.ServiceExpensesModel;
import Models.ServiceModel;
import Models.ServiceNameModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateServiceFragment extends Fragment{

    public static TextView textViewDate;
    public static TextView textViewTime;

    public int zapa_poz = 0;
    public Toolbar toolbar;
    public ImageView icon_spinner_obrtnik;
    public ImageView icon_spinner_naziv_servisa;
    public ImageView icon_spinner_novi_naziv;
    public Spinner spinner_obrtnik;
    public Spinner spinner_naziv_servisa;
    public int pritisakTipke;
    public Menu menuReference;
    public String poz, zadnja_km_info;
    private Date zadnji_datum;
    private boolean potvrda;


    ServiceModel serviceModel;
    EditText unoskmEd, biljeskeed, cijena_servisaed;
    FragmentManager fr;
    ArrayList<ObrtniciModel> obrtniciModels;
    ArrayList<String> obrtniciModels2;
    ArrayList<ServiceNameModel> serviceNameModels;
    ArrayList<String> servisiNaziviModels2;
    ArrayList<ServiceExpensesModel> listaTroskova = new ArrayList<>();
    ServiceExpensesModel expensesModel = new ServiceExpensesModel();
    String datum_post;
    String vrijeme_post;
    String stanje_km_post;
    String naziv_obrtnika_post;
    String naziv_servisa_post;
    String biljeske;
    int vozilo_id;
    String cijena_post;
    private TextView zadnja_kilometraza;
    private RecyclerView recyclerView;
    private ServicesUpdateAdapter servisiDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;

    private String unit_kmOrMile;
    private static String date_format;
    private String time_format;
    private String currency_format;
    private static String formatted_date;
    private static String vrijeme;
    private Date time;
    private static Date forma_datuma;
    private Date forma_vremena;
    private static Date promjena_vrijeme;
    private static String getFormat_vremena2;


    public UpdateServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.d("Zavrsno","OnCreateServise");

        Intent intent = getActivity().getIntent();
        Bundle data = intent.getExtras();
        pritisakTipke = data.getInt("idButtona");
        poz = data.getString("pozicija");
        serviceModel = intent.getParcelableExtra("ObjektServisa");


        if (serviceModel == null) {
            serviceModel = new ServiceModel();
        }


        listaTroskova = serviceModel.getTros();



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fragmentManager = getActivity().getSupportFragmentManager();


        obrtniciModels2 = new ArrayList<String>();
        servisiNaziviModels2 = new ArrayList<String>();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_update_service, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.primary4));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark4));
        }


        zadnja_kilometraza = (TextView) getActivity().findViewById(R.id.zadnja_kilometraza_ser_update);
        unoskmEd = (EditText) getActivity().findViewById(R.id.unos_km_ser_update);
        biljeskeed = (EditText) getActivity().findViewById(R.id.biljeskeServis_dodavanje);
        cijena_servisaed = (EditText) getActivity().findViewById(R.id.cijenaServisEd);

        fr = getActivity().getSupportFragmentManager();


        icon_spinner_obrtnik = (ImageView) getActivity().findViewById(R.id.icon_spinner_obrtnik);
        icon_spinner_naziv_servisa = (ImageView) getActivity().findViewById(R.id.icon_spinner_tip_servisa);
        icon_spinner_novi_naziv = (ImageView) getActivity().findViewById(R.id.icon_spinner_tip_servisa2);


        textViewDate = (TextView) getActivity().findViewById(R.id.date_ser_update);
        textViewTime = (TextView) getActivity().findViewById(R.id.time_ser_update);

        spinner_obrtnik = (Spinner) getActivity().findViewById(R.id.spinner_obrtnik);
        spinner_naziv_servisa = (Spinner) getActivity().findViewById(R.id.spinner_tip_servisa);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        menuReference = menu;


        if (pritisakTipke == R.id.menu_icon1) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);
            menuReference.getItem(1).setIcon(R.drawable.ic_action_delete).setVisible(true);

        } else if (pritisakTipke == R.id.buttonAddService) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (pritisakTipke == R.id.buttonAddService) {

                Intent intent = new Intent(getActivity(), ServiceExpencesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else if (pritisakTipke == R.id.menu_icon1) {

                Bundle b2 = new Bundle();
                b2.putString("pozicija", serviceModel.getId());
                b2.putParcelable("ObjToDisplay", serviceModel);


                Intent intent = new Intent(getActivity(), ServiceExpensesDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("bundle_lista", b2);
                startActivity(intent);


            }


        } else if (item.getItemId() == R.id.menu_icon1) {
            if (pritisakTipke == R.id.buttonAddService) {
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

        outState.putParcelable("SpemljeniObjektServis", serviceModel);
        outState.putString("spinner_Naziv_Ser", naziv_servisa_post);
        outState.putString("spinner_Naziv_Obr", naziv_obrtnika_post);
        outState.putStringArrayList("obrtniciModels2",obrtniciModels2);
        outState.putStringArrayList("servisiNazModels2",servisiNaziviModels2);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            serviceModel = savedInstanceState.getParcelable("SpemljeniObjektServis");
            naziv_servisa_post = savedInstanceState.getString("spinner_Naziv_Ser");
            naziv_obrtnika_post = savedInstanceState.getString("spinner_Naziv_Obr");
            obrtniciModels2 = savedInstanceState.getStringArrayList("obrtniciModels2");
            servisiNaziviModels2 = savedInstanceState.getStringArrayList("servisiNazModels2");
        }


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsUpdate(getContext());

        SharedPreferences sharedPref = getContext().getSharedPreferences("DistanceDate", Context.MODE_PRIVATE);
        zadnja_km_info = sharedPref.getString("ZADNJA_KM", String.valueOf(0));
        zadnji_datum = new Date(sharedPref.getLong("ZADNJI_DAT",0));

        zadnja_kilometraza.setText(getString(R.string.zadnja_) + " " + zadnja_km_info + settingValue.getUnit_kmOrMile());


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerAddUpdateService);
        servisiDetailAdapter = new ServicesUpdateAdapter(getContext(), serviceModel, listaTroskova, pritisakTipke);
        recyclerView.setAdapter(servisiDetailAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        postaviSpinnere(savedInstanceState);

        if (pritisakTipke != 0) {

            if (pritisakTipke == R.id.menu_icon1) {

                postaviVrijednostiUpdatea();

            } else if (pritisakTipke == R.id.buttonAddService) {

                postaviDanasnjiDateTime();

            }


        }


    }


    private boolean provjeraUnosa(ServiceModel dohvaceniModel) {

        boolean provjera = false;

        if (dohvaceniModel.getTros().size() > 0 && dohvaceniModel.getKmTrenutno().isEmpty() == false) {

            provjera = true;
        }


        return provjera;
    }


    private ServiceModel dajModelzaUnos() {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        stanje_km_post = unoskmEd.getText().toString();
        biljeske = biljeskeed.getText().toString();
        vrijeme_post = textViewTime.getText().toString();
        cijena_post = cijena_servisaed.getText().toString();
        vozilo_id = manager.getVehicleId();

        if(pritisakTipke == R.id.menu_icon1)forma_datuma = serviceModel.getDatumServis();



        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readDateFromSettings(getContext(),forma_datuma,vrijeme_post);




        Date date = new Date();
        ServiceModel m = new ServiceModel();
        try {

            //napraviti konverziju za sve moguce formate, tj. funkciju
            switch (settingValue.getDate_format()) {

                case "1":

                    date = new SimpleDateFormat("dd/MM/yyyy").parse(settingValue.getFormatted_date());
                    break;
                case "2":

                    date = new SimpleDateFormat("dd-MM-yyyy").parse(settingValue.getFormatted_date());
                    break;
                case "3":
                    date = new SimpleDateFormat("yyyy/MM/dd").parse(settingValue.getFormatted_date());
                    break;
                case "4":
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(settingValue.getFormatted_date());
                    break;
                case "5":
                    date = new SimpleDateFormat("dd/M/yyyy").parse(settingValue.getFormatted_date());
                    break;
                default:



            }



        } catch (ParseException e) {
            e.printStackTrace();
        }


        m.setDatumServis(date);
        m.setVrijemeServis(vrijeme_post);
        m.setKmTrenutno(stanje_km_post);
        m.setNazivObrtnika(naziv_obrtnika_post);
        m.setBiljeske(biljeske);
        m.setVoziloId(vozilo_id);
        if (pritisakTipke == R.id.menu_icon1) m.setId(serviceModel.getId());
        m.setTros(serviceModel.getTros());



        if(!stanje_km_post.isEmpty() && !stanje_km_post.equals("")) {
            if ((Double.valueOf(zadnja_km_info) < Double.valueOf(stanje_km_post)) && zadnji_datum.before(date)) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("DistanceDate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ZADNJA_KM", stanje_km_post);
                editor.putLong("ZADNJI_DAT", date.getTime());
                editor.commit();

            }
        }


        return m;
    }


    public void postaviDanasnjiDateTime() {


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        String time = timeformat.format(c.getTime());

        forma_datuma = c.getTime();
        forma_vremena = c.getTime();


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readDateFromSettings(getContext(),forma_datuma,time);


        textViewDate.setText(settingValue.getFormatted_date());
        textViewTime.setText(settingValue.getVrijeme());


    }


    public void postaviVrijednostiUpdatea() {


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readDateFromSettings(getContext(),serviceModel.getDatumServis(), serviceModel.getVrijemeServis());

        textViewDate.setText(settingValue.getFormatted_date());
        textViewTime.setText(settingValue.getVrijeme());
        unoskmEd.setText(serviceModel.getKmTrenutno());
        biljeskeed.setText(serviceModel.getBiljeske());


    }


    public void postaviSpinnere(Bundle savedInstanceState) {




            icon_spinner_obrtnik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundleListe = new Bundle();
                    bundleListe.putStringArrayList("listaObrtnika", obrtniciModels2);


                    NoviObrtnikFragment noviObrtnikFragment = new NoviObrtnikFragment();
                    noviObrtnikFragment.setArguments(bundleListe);
                    fr.beginTransaction().replace(R.id.lin_ley_updateServise, noviObrtnikFragment)
                            .addToBackStack("NoviObrtnik").commit();


                }
            });


            icon_spinner_naziv_servisa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ServiceExpensesModel t = new ServiceExpensesModel();
                    t.setIznos(cijena_servisaed.getText().toString());
                    t.setNazivServisa(naziv_servisa_post);


                    serviceModel.getTros().add(t);
                    cijena_servisaed.setText("");
                    recyclerView.getAdapter().notifyItemInserted(serviceModel.getTros().size() - 1);

                }
            });


            icon_spinner_novi_naziv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundleListe = new Bundle();
                    bundleListe.putStringArrayList("listaNazivaServisa", servisiNaziviModels2);


                    NoviNazivServisa novi = new NoviNazivServisa();
                    novi.setArguments(bundleListe);
                    fr.beginTransaction().replace(R.id.lin_ley_updateServise, novi).addToBackStack("NoviNazivServisa").commit();


                }
            });


            textViewDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogFragment newFragment = new UpdateServiceFragment.DatePickerFragment();
                    newFragment.show(getActivity().getSupportFragmentManager(), "datePickerService");


                }
            });


            textViewTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogFragment newTimeFragment = new UpdateServiceFragment.TimePickerFragment();
                    newTimeFragment.show(getActivity().getSupportFragmentManager(), "timepickerService");


                }
            });

        if (savedInstanceState == null) {
            final Call<ArrayList<ObrtniciModel>> obrtnici_lista_repos = RestClient.getInstance().getApiService().getObrtnici();
            Call<ArrayList<ServiceNameModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNazivServisa();


            obrtnici_lista_repos.enqueue(new Callback<ArrayList<ObrtniciModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ObrtniciModel>> call, Response<ArrayList<ObrtniciModel>> response) {

                    if (response.isSuccessful()) {

                        obrtniciModels = response.body();

                        for (int i = 0; i < obrtniciModels.size(); i++) {

                            obrtniciModels2.add(obrtniciModels.get(i).getNazivObrtnika());

                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, obrtniciModels2);
                        spinner_obrtnik.setAdapter(adapter);


                        if (pritisakTipke == R.id.menu_icon1) {

                            spinner_obrtnik.setSelection(obrtniciModels2.indexOf(serviceModel.getNazivObrtnika()));

                        } else if (pritisakTipke == R.id.buttonAddService) {

                            spinner_obrtnik.setSelection(zapa_poz);

                        }

                        spinner_obrtnik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                zapa_poz = i;
                                naziv_obrtnika_post = adapterView.getItemAtPosition(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }


                }

                @Override
                public void onFailure(Call<ArrayList<ObrtniciModel>> call, Throwable t) {

                    Toast.makeText(getContext(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR_Spinner_Obrtnik", t.getMessage().toString());

                }
            });


            nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<ServiceNameModel>>() {
                @Override
                public void onResponse(Call<ArrayList<ServiceNameModel>> call, Response<ArrayList<ServiceNameModel>> response) {

                    if (response.isSuccessful()) {

                        serviceNameModels = response.body();


                        for (int i = 0; i < serviceNameModels.size(); i++) {

                            servisiNaziviModels2.add(serviceNameModels.get(i).getNazivServisa());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, servisiNaziviModels2);
                        spinner_naziv_servisa.setAdapter(adapter);


                        if (pritisakTipke == R.id.menu_icon1) {

                            spinner_naziv_servisa.setSelection(servisiNaziviModels2.indexOf(serviceModel.getTros().get(0).getNazivServisa()));

                        } else if (pritisakTipke == R.id.buttonAddService) {

                            spinner_naziv_servisa.setSelection(0);

                        }


                        spinner_naziv_servisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                naziv_servisa_post = adapterView.getItemAtPosition(i).toString();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ServiceNameModel>> call, Throwable t) {


                    Toast.makeText(getContext(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR_Spinner_Naziv_Ser", t.getMessage().toString());

                }
            });

        }else{



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, obrtniciModels2);
            spinner_obrtnik.setAdapter(adapter);


            if (pritisakTipke == R.id.menu_icon1) {

                spinner_obrtnik.setSelection(obrtniciModels2.indexOf(serviceModel.getNazivObrtnika()));

            } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                spinner_obrtnik.setSelection(zapa_poz);

            }

            spinner_obrtnik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    zapa_poz = i;
                    naziv_obrtnika_post = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, servisiNaziviModels2);
            spinner_naziv_servisa.setAdapter(adapter2);


            if (pritisakTipke == R.id.menu_icon1) {

                spinner_naziv_servisa.setSelection(servisiNaziviModels2.indexOf(serviceModel.getTros().get(0).getNazivServisa()));

            } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                spinner_naziv_servisa.setSelection(0);

            }


            spinner_naziv_servisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    naziv_servisa_post = adapterView.getItemAtPosition(i).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



        }
    }


    private void update() {

        final ServiceModel model = dajModelzaUnos();


        if (provjeraUnosa(model) == true) {

            provjeraUnosaUdaljenosti(Double.valueOf(model.getKmTrenutno()), model.getDatumServis(), model.getVoziloId(), new ProvjeraCallBack() {
                @Override
                public void onSuccess(boolean value) {

                    potvrda = value;


                    if(potvrda == true){

                        Call<Ok> updateServisirepos = RestClient.getInstance().getApiService().updateServisi(model, Integer.valueOf(serviceModel.getId()));


                        updateServisirepos.enqueue(new Callback<Ok>() {
                            @Override
                            public void onResponse(Call<Ok> call, Response<Ok> response) {

                                if (response.isSuccessful())
                                    Toast.makeText(getActivity(), R.string.servis_azuriran, Toast.LENGTH_SHORT).show();

                                Bundle b2 = new Bundle();
                                b2.putString("pozicija", model.getId());
                                b2.putBoolean("zastavica_update", true);
                                b2.putParcelable("ObjToDisplay", model);


                                Intent in = new Intent(getActivity(), ServiceExpensesDetail.class);
                                in.putExtra("bundle_lista", b2);
                                startActivity(in);

                            }

                            @Override
                            public void onFailure(Call<Ok> call, Throwable t) {

                                Log.d("greska_update_Service", t.getMessage());
                                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }else {

                        Toast.makeText(getContext(), getString(R.string.km_unos_err), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError() {
                    potvrda = false;
                }
            });


        } else Toast.makeText(getActivity(), R.string.prazno_oprez, Toast.LENGTH_SHORT).show();

    }


    private void unesi() {

        final ServiceModel model = dajModelzaUnos();


        if (provjeraUnosa(dajModelzaUnos()) == true) {

            provjeraUnosaUdaljenosti(Double.valueOf(model.getKmTrenutno()), model.getDatumServis(), model.getVoziloId(), new ProvjeraCallBack() {
                @Override
                public void onSuccess(boolean value) {

                    potvrda = value;


                    if(potvrda == true){

                        Call<Ok> unosServicaRepos = RestClient.getInstance().getApiService().addServisi(model);
                        unosServicaRepos.enqueue(new Callback<Ok>() {
                            @Override
                            public void onResponse(Call<Ok> call, Response<Ok> response) {

                                if (response.isSuccessful())
                                    Toast.makeText(getActivity(), R.string.dodan_servis, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), ServiceExpencesActivity.class));
                            }

                            @Override
                            public void onFailure(Call<Ok> call, Throwable t) {

                                Log.d("greska_unosServisa", t.getMessage().toString());
                                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{


                        Toast.makeText(getContext(), getString(R.string.km_unos_err), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onError() {
                    potvrda = false;
                }
            });

        } else Toast.makeText(getActivity(), R.string.prazno_oprez, Toast.LENGTH_SHORT).show();

    }

    private void startAlertDialog() {

        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(serviceModel.getId()));
        bundle_id.putString("Naziv_Klase", "ServisiDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(getActivity().getSupportFragmentManager(), "Alert_Delete");
    }




    private void  provjeraUnosaUdaljenosti(Double km_unosa, Date datum_unosa,int vozilo_id, final ProvjeraCallBack provjeraCallBack){


        if(km_unosa.equals(""))   provjeraCallBack.onSuccess(false);
        else if(km_unosa > Double.valueOf(zadnja_km_info)){
            provjeraCallBack.onSuccess(true);
        }else if(km_unosa<Double.valueOf(zadnja_km_info) && datum_unosa.after(zadnji_datum)){
            provjeraCallBack.onSuccess(false);
        } else if(km_unosa<Double.valueOf(zadnja_km_info) && datum_unosa.before(zadnji_datum)){


            Call<DistanceValidationModel> validationModelCall = RestClient.getInstance().getApiService().distanceValidation(vozilo_id, km_unosa, datum_unosa);
            validationModelCall.enqueue(new Callback<DistanceValidationModel>() {
                @Override
                public void onResponse(Call<DistanceValidationModel> call, Response<DistanceValidationModel> response) {

                    if (response.isSuccessful()) {

                        if (response.body().getPoruka()){
                            if(provjeraCallBack != null){
                                provjeraCallBack.onSuccess(true);
                            }

                        }else{

                            if(provjeraCallBack != null){
                                provjeraCallBack.onSuccess(false);
                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<DistanceValidationModel> call, Throwable t) {
                    Toast.makeText(getContext(), getString(R.string.poruka_greske_dohvata), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            provjeraCallBack.onSuccess(true);
        }





    }





    public void setTroskoveServisa(ServiceExpensesModel s) {
        expensesModel = s;
        serviceModel.getTros().add(expensesModel);
        cijena_servisaed.setText("");


        recyclerView.getAdapter().notifyItemInserted(serviceModel.getTros().size() - 1);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.invalidate();


    }




    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, day);
            forma_datuma = calendar.getTime();


            GetSettingValue settingValue = new GetSettingValue();
            settingValue.readDateOnlyFromSettings(getContext(),forma_datuma);

            textViewDate.setText(settingValue.getFormatted_date());
            getDialog().dismiss();
        }
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute)  {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            promjena_vrijeme = calendar.getTime();


            GetSettingValue settingValue = new GetSettingValue();
            settingValue.readTimeOnlyFromSettings(getContext(),promjena_vrijeme);

            textViewTime.setText(settingValue.getVrijeme());

            getDialog().dismiss();
        }
    }


}
