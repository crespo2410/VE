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

import com.example.crespo.vehicleexpenses.Activity.RefuelingExpensesActivity;
import com.example.crespo.vehicleexpenses.Activity.RefuelingExpensesDetail;
import com.example.crespo.vehicleexpenses.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Interfaces.ProvjeraCallBack;
import Models.BenzinskaModel;
import Models.DistanceValidationModel;
import Models.GorivoModel;
import Models.Ok;
import Models.PodvrstaModel;
import Models.RazlogModel;
import Models.TocenjeModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa Fragmenta koje obrađuje informacije vezane za Ažuriranje unosa  troškova točenja vozila te i za sam unos novih.
 */
public class UpdateRefuelingFragment extends Fragment {


    public static TextView textViewDate;
    public static TextView textViewTime;
    public Toolbar toolbar;
    public ImageView icon_spinner_razlog;
    public ImageView icon_spinner_gorivo;
    public ImageView icon_spinner_ben;
    public Spinner spinner_gorivo;
    public Spinner spinner_benzinska;
    public Spinner spinner_razlog;
    public int pritisakTipke;
    public Menu menuReference;
    public String poz, zadnja_km_info;
    TocenjeModel tocenjeModel;
    EditText unoskmEd, ukupnoEd, litaraEd, cijenaEd, biljeskeed;
    FragmentManager fr;
    ArrayList<BenzinskaModel> benzinskaModels;
    ArrayList<String> benzinskaModels2;
    ArrayList<GorivoModel> gorivoModels;
    ArrayList<String> gorivoModels2;
    ArrayList<RazlogModel> razlogModels;
    ArrayList<String> razlogModels2;
    ArrayList<PodvrstaModel> podvrstaModels;
    ArrayList<String> podvrstaModels2;
    String datum_post, vrijeme_post, stanje_km_post, cijena_l_post, ukupni_trosak_post, litara_post, naziv_benzina_post;
    String benzinska_naziv_post, razlog_naziv, biljeske;
    int vozilo_id;
    private TextView zadnja_kilometraza;
    private String unit_kmOrMile, date_format, time_format, currency_format, formatted_date, vrijeme, jed_litra_galon;
    private Date time;
    private static Date forma_datuma;
    private Date forma_vremena;
    private Date zadnji_datum;
    boolean potvrda;
    private GetSettingValue settingValue;

    private static Date promjena_datum;
    private static Date promjena_vrijeme;


    public UpdateRefuelingFragment() {
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

        Intent intent = getActivity().getIntent();
        Bundle data = intent.getExtras();
        pritisakTipke = data.getInt("idButtona");
        poz = data.getString("pozicija");
        tocenjeModel = intent.getParcelableExtra("ObjektTocenja");

        benzinskaModels2 = new ArrayList<>();
        gorivoModels2 = new ArrayList<>();
        razlogModels2 = new ArrayList<>();
        podvrstaModels2 = new ArrayList<>();


        return inflater.inflate(R.layout.add_update_gas, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.primary2));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary_dark2));
        }


        zadnja_kilometraza = (TextView) getActivity().findViewById(R.id.zadnja_kilometraza);
        unoskmEd = (EditText) getActivity().findViewById(R.id.unos_km);
        ukupnoEd = (EditText) getActivity().findViewById(R.id.c);
        litaraEd = (EditText) getActivity().findViewById(R.id.d);
        cijenaEd = (EditText) getActivity().findViewById(R.id.b);
        biljeskeed = (EditText) getActivity().findViewById(R.id.biljeske_dodavanje);
        fr = getActivity().getSupportFragmentManager();
        icon_spinner_ben = (ImageView) getActivity().findViewById(R.id.icon_spinner_ben);
        icon_spinner_gorivo = (ImageView) getActivity().findViewById(R.id.icon_spinner_gorivo);
        icon_spinner_razlog = (ImageView) getActivity().findViewById(R.id.icon_spinner_razlog);
        textViewDate = (TextView) getActivity().findViewById(R.id.date);
        textViewTime = (TextView) getActivity().findViewById(R.id.time);
        spinner_gorivo = (Spinner) getActivity().findViewById(R.id.spinner_gorivo);
        spinner_benzinska = (Spinner) getActivity().findViewById(R.id.spinner_benzinska);
        spinner_razlog = (Spinner) getActivity().findViewById(R.id.spinner_razlog);




        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        menuReference = menu;


        if (pritisakTipke == R.id.menu_icon1) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);
            menuReference.getItem(1).setIcon(R.drawable.ic_action_delete).setVisible(true);

        } else if (pritisakTipke == R.id.buttonAddGas) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (pritisakTipke == R.id.buttonAddGas) {

                Intent intent = new Intent(getActivity(), RefuelingExpensesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            } else if (pritisakTipke == R.id.menu_icon1) {

                Bundle b2 = new Bundle();
                b2.putString("pozicija", tocenjeModel.getId());
                b2.putParcelable("ObjToDisplay", tocenjeModel);


                Intent intent = new Intent(getActivity(), RefuelingExpensesDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("bundle_lista", b2);
                startActivity(intent);


            }


        } else if (item.getItemId() == R.id.menu_icon1) {
            if (pritisakTipke == R.id.buttonAddGas) {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            tocenjeModel = savedInstanceState.getParcelable("SpemljeniObjekt_t");
            benzinska_naziv_post = savedInstanceState.getString("spinner_Naziv_ben");
            naziv_benzina_post = savedInstanceState.getString("spinner_Naziv_gor");
            razlog_naziv = savedInstanceState.getString("spinner_Naziv_Raz_t");
            benzinskaModels2 = savedInstanceState.getStringArrayList("naziviBenz2_t");
            gorivoModels2 = savedInstanceState.getStringArrayList("naziviGor2_t");
        }


        settingValue = new GetSettingValue();
        settingValue.readSettingsUpdate(getContext());



        SharedPreferences sharedPref = getContext().getSharedPreferences("DistanceDate", Context.MODE_PRIVATE);
        zadnja_km_info = sharedPref.getString("ZADNJA_KM", String.valueOf(0));
        zadnji_datum = new Date(sharedPref.getLong("ZADNJI_DAT",0));


        zadnja_kilometraza.setText(getString(R.string.zadnja_) + " " + zadnja_km_info + settingValue.getUnit_kmOrMile());


        postaviSpinnere(savedInstanceState);


        if (pritisakTipke != 0) {

            if (pritisakTipke == R.id.menu_icon1) {

                postaviVrijednostiUpdatea();

            } else if (pritisakTipke == R.id.buttonAddGas) {

                postaviDanasnjiDateTime();

            }


        }

        super.onActivityCreated(savedInstanceState);
    }


    private TocenjeModel dajModelzaUnos() {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        stanje_km_post = unoskmEd.getText().toString();
        ukupni_trosak_post = ukupnoEd.getText().toString();
        litara_post = litaraEd.getText().toString();
        cijena_l_post = cijenaEd.getText().toString();
        biljeske = biljeskeed.getText().toString();
        vrijeme_post = textViewTime.getText().toString();
        vozilo_id = manager.getVehicleId();


        if(pritisakTipke == R.id.menu_icon1)forma_datuma = tocenjeModel.getTocenjeDatum();


        settingValue.readDateFromSettings(getContext(),forma_datuma,vrijeme_post);


        TocenjeModel t = new TocenjeModel();
        Date date = new Date();
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


            t.setTocenjeDatum(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }

        t.setTocenjeVrijeme(vrijeme_post);
        t.setKmTrenutno(stanje_km_post);
        t.setUkupniTrosak(ukupni_trosak_post);
        t.setUzetoLitara(litara_post);
        t.setCijena(cijena_l_post);
        t.setBiljeske(biljeske);
        t.setBenzinskaNaziv(benzinska_naziv_post);
        t.setNazivGoriva(naziv_benzina_post);
        t.setRazlogNaziv(razlog_naziv);
        t.setVoziloId(vozilo_id);

        if (pritisakTipke == R.id.menu_icon1) t.setId(tocenjeModel.getId());


        if(!stanje_km_post.isEmpty() && !stanje_km_post.equals("")) {
            if ((Double.valueOf(zadnja_km_info) < Double.valueOf(stanje_km_post)) && date.after(zadnji_datum)) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("DistanceDate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ZADNJA_KM", stanje_km_post);
                editor.putLong("ZADNJI_DAT", date.getTime());
                editor.commit();

            }
        }

        return t;
    }


    public void postaviDanasnjiDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        String time = timeformat.format(c.getTime());

        forma_datuma = c.getTime();
        forma_vremena = c.getTime();


        settingValue.readDateFromSettings(getContext(),forma_datuma, time);


        textViewDate.setText(settingValue.getFormatted_date());
        textViewTime.setText(settingValue.getVrijeme());


    }


    public void postaviVrijednostiUpdatea() {

        settingValue.readDateFromSettings(getContext(),tocenjeModel.getTocenjeDatum(), tocenjeModel.getTocenjeVrijeme());


        textViewDate.setText(formatted_date);
        textViewTime.setText(tocenjeModel.getTocenjeVrijeme());
        unoskmEd.setText(tocenjeModel.getKmTrenutno());
        litaraEd.setText(tocenjeModel.getUzetoLitara());
        cijenaEd.setText(tocenjeModel.getCijena());
        ukupnoEd.setText(tocenjeModel.getUkupniTrosak());
        biljeskeed.setText(tocenjeModel.getBiljeske());


    }


    public void postaviSpinnere(Bundle onSaveInstanceState) {


        icon_spinner_ben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaNazivaBenzinskih", benzinskaModels2);

                NovaBenzinskaFragment novaben = new NovaBenzinskaFragment();
                novaben.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_update, novaben).addToBackStack("NovaBen").commit();


            }
        });


        icon_spinner_gorivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaNazivaGoriva", gorivoModels2);

                NovoGorivoFragment novogorivo = new NovoGorivoFragment();
                novogorivo.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_update, novogorivo).addToBackStack("Novogorivo").commit();


            }
        });


        icon_spinner_razlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaNazivaRazloga", razlogModels2);
                bundleListe.putString("Dolazak","KlasaRefuelling");


                NoviRazlogFragment novirazlog = new NoviRazlogFragment();
                novirazlog.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_update, novirazlog).addToBackStack("Novirazlog").commit();


            }
        });


        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");


            }
        });


        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newTimeFragment = new TimePickerFragment();
                newTimeFragment.show(getActivity().getSupportFragmentManager(), "timepicker");


            }
        });



        if(onSaveInstanceState == null) {

            Call<ArrayList<BenzinskaModel>> benzinska_call = RestClient.getInstance().getApiService().getBenzinske();
            Call<ArrayList<GorivoModel>> gorivo_Call = RestClient.getInstance().getApiService().getGorivo();
            Call<ArrayList<PodvrstaModel>> podvrsta_Call = RestClient.getInstance().getApiService().getPodvrsta();
            Call<ArrayList<RazlogModel>> razlog_Call = RestClient.getInstance().getApiService().getRazlog();


            benzinska_call.enqueue(new Callback<ArrayList<BenzinskaModel>>() {
                @Override
                public void onResponse(Call<ArrayList<BenzinskaModel>> call, Response<ArrayList<BenzinskaModel>> response) {

                    benzinskaModels = response.body();

                    for (int i = 0; i < benzinskaModels.size(); i++) {

                        benzinskaModels2.add(benzinskaModels.get(i).getNazivBenzinske());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, benzinskaModels2);
                    spinner_benzinska.setAdapter(adapter);

                    if (pritisakTipke == R.id.menu_icon1) {

                        spinner_benzinska.setSelection(benzinskaModels2.indexOf(tocenjeModel.getBenzinskaNaziv()));

                    } else if (pritisakTipke == R.id.buttonAddGas) {

                        spinner_benzinska.setSelection(0);

                    }

                    spinner_benzinska.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            benzinska_naziv_post = adapterView.getItemAtPosition(i).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }

                @Override
                public void onFailure(Call<ArrayList<BenzinskaModel>> call, Throwable t) {


                    Log.d("ERROR_VELIKI", t.getMessage().toString());

                }
            });


            gorivo_Call.enqueue(new Callback<ArrayList<GorivoModel>>() {
                @Override
                public void onResponse(Call<ArrayList<GorivoModel>> call, Response<ArrayList<GorivoModel>> response) {


                    gorivoModels = response.body();

                    for (int i = 0; i < gorivoModels.size(); i++) {

                        gorivoModels2.add(gorivoModels.get(i).getNaziv());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gorivoModels2);
                    spinner_gorivo.setAdapter(adapter);

                    if (pritisakTipke == R.id.menu_icon1) {

                        spinner_gorivo.setSelection(gorivoModels2.indexOf(tocenjeModel.getNazivGoriva()));


                    } else if (pritisakTipke == R.id.buttonAddGas) {

                        spinner_gorivo.setSelection(0);

                    }

                    spinner_gorivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            naziv_benzina_post = adapterView.getItemAtPosition(i).toString();
                            if (naziv_benzina_post.equals("Elektricni")) {

                                litaraEd.setHint(getString(R.string.Uzeto_kwh));

                            } else if (naziv_benzina_post.equals("Plin")) {

                                litaraEd.setHint(getString(R.string.uzeto__m3));

                            } else {

                                litaraEd.setHint(getString(R.string.tocenje_uzeto_litara_hint));

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }

                @Override
                public void onFailure(Call<ArrayList<GorivoModel>> call, Throwable t) {

                    Log.d("ERROR_VELIKI", t.getMessage().toString());
                }
            });


            razlog_Call.enqueue(new Callback<ArrayList<RazlogModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RazlogModel>> call, Response<ArrayList<RazlogModel>> response) {


                    razlogModels = response.body();

                    for (int i = 0; i < razlogModels.size(); i++) {

                        razlogModels2.add(razlogModels.get(i).getNazivRazloga());

                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, razlogModels2);
                    spinner_razlog.setAdapter(adapter);


                    if (pritisakTipke == R.id.menu_icon1) {


                        spinner_razlog.setSelection(razlogModels2.indexOf(tocenjeModel.getRazlogNaziv()));

                    } else if (pritisakTipke == R.id.buttonAddGas) {


                        spinner_razlog.setSelection(0);

                    }

                    spinner_razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            razlog_naziv = adapterView.getItemAtPosition(i).toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }

                @Override
                public void onFailure(Call<ArrayList<RazlogModel>> call, Throwable t) {

                    Log.d("ERROR_VELIKI", t.getMessage().toString());
                }
            });

        }else{

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, benzinskaModels2);
            spinner_benzinska.setAdapter(adapter);

            if (pritisakTipke == R.id.menu_icon1) {

                spinner_benzinska.setSelection(benzinskaModels2.indexOf(tocenjeModel.getBenzinskaNaziv()));

            } else if (pritisakTipke == R.id.buttonAddGas) {

                spinner_benzinska.setSelection(0);

            }

            spinner_benzinska.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    benzinska_naziv_post = adapterView.getItemAtPosition(i).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gorivoModels2);
            spinner_gorivo.setAdapter(adapter2);

            if (pritisakTipke == R.id.menu_icon1) {

                spinner_gorivo.setSelection(gorivoModels2.indexOf(tocenjeModel.getNazivGoriva()));


            } else if (pritisakTipke == R.id.buttonAddGas) {

                spinner_gorivo.setSelection(0);

            }

            spinner_gorivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    naziv_benzina_post = adapterView.getItemAtPosition(i).toString();
                    if (naziv_benzina_post.equals("Elektricni")) {

                        litaraEd.setHint(getString(R.string.Uzeto_kwh));

                    } else if (naziv_benzina_post.equals("Plin")) {

                        litaraEd.setHint(getString(R.string.uzeto__m3));

                    } else {

                        litaraEd.setHint(getString(R.string.tocenje_uzeto_litara_hint));

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, razlogModels2);
            spinner_razlog.setAdapter(adapter3);


            if (pritisakTipke == R.id.menu_icon1) {

                spinner_razlog.setSelection(razlogModels2.indexOf(tocenjeModel.getRazlogNaziv()));

            } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                spinner_razlog.setSelection(0);

            }


            spinner_razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    razlog_naziv = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        }
    }


    private boolean provjeraUnosa(TocenjeModel dohvaceniModel) {
        boolean provjera = true;

        if (dohvaceniModel.getCijena().isEmpty() || dohvaceniModel.getKmTrenutno().isEmpty() || dohvaceniModel.getUkupniTrosak().isEmpty() || dohvaceniModel.getUzetoLitara().isEmpty()) {
            provjera = false;
        }
        return provjera;
    }





    private void update() {


        final TocenjeModel model = dajModelzaUnos();



        if (provjeraUnosa(model) == true) {



            provjeraUnosaUdaljenosti(Double.valueOf(model.getKmTrenutno()), model.getTocenjeDatum(), model.getVoziloId(), new ProvjeraCallBack() {
                @Override
                public void onSuccess(boolean value) {

                    potvrda = value;

                    if(potvrda == true)
                    {

                        Call<Ok> updateRepos = RestClient.getInstance().getApiService().updateTocenje(model, Integer.valueOf(tocenjeModel.getId()));

                        updateRepos.enqueue(new Callback<Ok>() {
                            @Override
                            public void onResponse(Call<Ok> call, Response<Ok> response) {

                                String ok = response.body().getObavijest();

                                if (response.isSuccessful())
                                    Toast.makeText(getActivity(), R.string.tocenje_azurirano, Toast.LENGTH_SHORT).show();

                                Bundle b2 = new Bundle();
                                b2.putString("pozicija",model.getId());
                                b2.putBoolean("zastavica_update", true);
                                b2.putParcelable("ObjToDisplay", model);


                                Intent in = new Intent(getActivity(), RefuelingExpensesDetail.class);
                                in.putExtra("bundle_lista", b2);
                                startActivity(in);


                            }

                            @Override
                            public void onFailure(Call<Ok> call, Throwable t) {
                                Log.d("greska_update", t.getMessage());
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


        }else{
            Toast.makeText(getContext(), getString(R.string.err_empty_toc), Toast.LENGTH_SHORT).show();
        }
    }


    private void unesi() {


        final TocenjeModel model = dajModelzaUnos();

        if (provjeraUnosa(model) == true) {


            provjeraUnosaUdaljenosti(Double.valueOf(model.getKmTrenutno()), model.getTocenjeDatum(), model.getVoziloId(), new ProvjeraCallBack() {
                @Override
                public void onSuccess(boolean value) {
                    potvrda = value;

                    if(potvrda == true){

                        Call<Ok> unosRepos = RestClient.getInstance().getApiService().addTocenje(model);
                        unosRepos.enqueue(new Callback<Ok>() {
                            @Override
                            public void onResponse(Call<Ok> call, Response<Ok> response) {

                                String ok = response.body().getObavijest();

                                if (response.isSuccessful())
                                    Toast.makeText(getActivity(), R.string.dodano_tocenje, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), RefuelingExpensesActivity.class));

                            }

                            @Override
                            public void onFailure(Call<Ok> call, Throwable t) {

                                Log.d("greska_unos", t.getMessage().toString());
                                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


                            }
                        });

                    } else{
                        Toast.makeText(getContext(),  getString(R.string.km_unos_err), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError() {
                    potvrda = false;
                }
            });
            } else {
            Toast.makeText(getContext(), getString(R.string.err_empty_toc), Toast.LENGTH_SHORT).show();


            }

    }




    private void startAlertDialog() {

        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(tocenjeModel.getId()));
        bundle_id.putString("Naziv_Klase", "TocenjeDetailClass");


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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("SpemljeniObjekt_t", tocenjeModel);
        outState.putString("spinner_Naziv_ben", benzinska_naziv_post);
        outState.putString("spinner_Naziv_gor", naziv_benzina_post);
        outState.putString("spinner_Naziv_Raz_t", razlog_naziv);
        outState.putStringArrayList("naziviRazlog2_t",razlogModels2);
        outState.putStringArrayList("naziviBenz2_t",benzinskaModels2);
        outState.putStringArrayList("naziviGor2_t",gorivoModels2);
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

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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

