package Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.crespo.vehicleexpenses.Activity.OtherExpensesActivity;
import com.example.crespo.vehicleexpenses.Activity.OtherExpensesDetail;
import com.example.crespo.vehicleexpenses.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Adapters.OtherExpensesUpdateAdapter;
import Interfaces.ProvjeraCallBack;
import Models.DistanceValidationModel;
import Models.ExpensesOtherExpensesModel;
import Models.ObrtniciModel;
import Models.Ok;
import Models.OstaliTroskoviNaziviModel;
import Models.OtherExpensesModel;
import Models.RazlogModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa Fragmenta koje obrađuje informacije vezane za Ažuriranje unosa ostalih troškova vozila te i za sam unos novih. Na isti način
 * rade i klase UpdateOtherExpensesFragment,UpdateRefuellingFragment,UpdateReminderFragment,UpdateServiseFragment te
 * UpdateVehicleFragment, stoga za osnovne detalje pogledati ovu klasu
 */
public class UpdateOtherExpensesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static TextView textViewDate;
    public static TextView textViewTime;

    public int zapa_poz = 0;
    public Toolbar toolbar;
    public ImageView icon_spinner_obrtnik;
    public ImageView icon_spinner_naziv_servisa;
    public ImageView icon_spinner_novi_naziv;
    public ImageView icon_spinner_novi_razlog;
    public Spinner spinner_obrtnik;
    public Spinner spinner_naziv_servisa;
    public Spinner spinner_razlog;
    public int pritisakTipke;
    public Menu menuReference;
    public String poz, zadnja_km_info;
    private Date zadnji_datum;
    private boolean potvrda;
    OtherExpensesModel otherExpensesModel = new OtherExpensesModel();
    EditText unoskmEd, biljeskeed, cijena_servisaed;
    FragmentManager fr;
    ArrayList<ObrtniciModel> obrtniciModels;
    ArrayList<String> obrtniciModels2;
    ArrayList<OstaliTroskoviNaziviModel> ostaliTroskoviNaziviModels;
    ArrayList<String> ostaliTroskoviNaziviModels2;
    ArrayList<RazlogModel> razlogModels;
    ArrayList<String> razlogModels2;
    ArrayList<ExpensesOtherExpensesModel> listaTroskova = new ArrayList<>();
    ExpensesOtherExpensesModel vvv = new ExpensesOtherExpensesModel();
    String  vrijeme_post, stanje_km_post, naziv_obrtnika_post, naziv_servisa_post, biljeske, vozilo_id, cijena_post, naziv_razloga_post;
    private TextView zadnja_kilometraza;
    private static Date forma_datuma, promjena_vrijeme,forma_vremena;
    private RecyclerView recyclerView;
    private OtherExpensesUpdateAdapter otherExpensesUpdateAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentManager fragmentManager;
    private String unit_kmOrMile, date_format, time_format, currency_format, formatted_date, vrijeme;
    private Date time;


    public UpdateOtherExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        //dohvačanje vrijednosti iz Bundlea kako bi znali kako odreagirati, da li
        //prazno sučelje (ako se stvara novi), odnosno popuniti ako se ažurira
        Intent intent = getActivity().getIntent();
        Bundle data = intent.getExtras();
        pritisakTipke = data.getInt("idButtona");
        poz = data.getString("pozicija");
        otherExpensesModel = intent.getParcelableExtra("ObjektOtherExpenses");

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

        //Ako stvaramo novi nismo dohvatili ništa (null) pa samo stvorimo novu instancu objekta
        if (otherExpensesModel == null) {
            otherExpensesModel = new OtherExpensesModel();
        }

        //dohvačanje troškova nastalih tjekom jednog unosa
        listaTroskova = otherExpensesModel.getTros();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        fragmentManager = getActivity().getSupportFragmentManager();
        obrtniciModels2 = new ArrayList<String>();
        ostaliTroskoviNaziviModels2 = new ArrayList<String>();
        razlogModels2 = new ArrayList<>();

        return inflater.inflate(R.layout.add_update_other_expense, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary5));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.primary_dark5));
        }


        zadnja_kilometraza = (TextView) getActivity().findViewById(R.id.zadnja_kilometraza_other_update);
        unoskmEd = (EditText) getActivity().findViewById(R.id.unos_km_other_update);
        biljeskeed = (EditText) getActivity().findViewById(R.id.biljeskeServis_dodavanje2);
        cijena_servisaed = (EditText) getActivity().findViewById(R.id.cijenaOstalogTroskaEd);

        fr = getActivity().getSupportFragmentManager();


        icon_spinner_obrtnik = (ImageView) getActivity().findViewById(R.id.icon_spinner_obrtnik2);
        icon_spinner_naziv_servisa = (ImageView) getActivity().findViewById(R.id.icon_spinner_tip_ostalog_troska);
        icon_spinner_novi_naziv = (ImageView) getActivity().findViewById(R.id.icon_spinnertip_ostalog_troska2);
        icon_spinner_novi_razlog = (ImageView) getActivity().findViewById(R.id.icon_spinner_razlog2);


        textViewDate = (TextView) getActivity().findViewById(R.id.date_other_update);
        textViewTime = (TextView) getActivity().findViewById(R.id.time_other_update);

        spinner_obrtnik = (Spinner) getActivity().findViewById(R.id.spinner_obrtnik2);
        spinner_naziv_servisa = (Spinner) getActivity().findViewById(R.id.spinner_tip_ostalog_troska);
        spinner_razlog = (Spinner) getActivity().findViewById(R.id.spinner_razlog2);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        menuReference = menu;

        //Ako smo došli ažurirati postavi dvije ikone
        if (pritisakTipke == R.id.menu_icon1) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);
            menuReference.getItem(1).setIcon(R.drawable.ic_action_delete).setVisible(true);

            //Ako unosimo nove vrijenosti, tada samo postavi ikonu za potvrdu unosa
        } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            //Ako idemo natrag preko Home buttona i došli smo dodati novi zapis
            if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                Intent intent = new Intent(getActivity(), OtherExpensesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                //Ako idemo natrag preko Home buttona i došli smo ažurirati zapis
            } else if (pritisakTipke == R.id.menu_icon1) {

                Bundle b2 = new Bundle();
                b2.putString("pozicija", otherExpensesModel.getIdOt());
                b2.putParcelable("ObjToDisplay", otherExpensesModel);


                Intent intent = new Intent(getActivity(), OtherExpensesDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("bundle_lista", b2);
                startActivity(intent);


            }

            //slučaj kada potvrđujemo svoju radnju
        } else if (item.getItemId() == R.id.menu_icon1) {
            //potvrdi unos
            if (pritisakTipke == R.id.buttonAddOtherExpenses) {
                unesi();

                //potvrdi ažuriranje
            } else if (pritisakTipke == R.id.menu_icon1) {
                update();

            }
            //ako je odabrano brisanje
        } else if (item.getItemId() == R.id.menu_icon2) {
            startAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("SpemljeniObjekt", otherExpensesModel);
        outState.putString("spinner_Naziv_Ser", naziv_servisa_post);
        outState.putString("spinner_Naziv_Obr", naziv_obrtnika_post);
        outState.putString("spinner_Naziv_Raz", naziv_razloga_post);
        outState.putStringArrayList("obrtniciModels2",obrtniciModels2);
        outState.putStringArrayList("ostaliTroskoviNazivi2",ostaliTroskoviNaziviModels2);
        outState.putStringArrayList("naziviRazlog2",razlogModels2);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            otherExpensesModel = savedInstanceState.getParcelable("SpemljeniObjekt");
            naziv_servisa_post = savedInstanceState.getString("spinner_Naziv_Ser");
            naziv_obrtnika_post = savedInstanceState.getString("spinner_Naziv_Obr");
            naziv_razloga_post = savedInstanceState.getString("spinner_Naziv_Raz");
            obrtniciModels2 = savedInstanceState.getStringArrayList("obrtniciModels2");
            ostaliTroskoviNaziviModels2 = savedInstanceState.getStringArrayList("ostaliTroskoviNazivi2");
            razlogModels2 = savedInstanceState.getStringArrayList("naziviRazlog2");
        }



        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsDetail(getContext());



        SharedPreferences sharedPref = getContext().getSharedPreferences("DistanceDate", Context.MODE_PRIVATE);
        zadnja_km_info = sharedPref.getString("ZADNJA_KM", String.valueOf(0));
        zadnji_datum = new Date(sharedPref.getLong("ZADNJI_DAT",0));


        zadnja_kilometraza.setText(getString(R.string.zadnja_) + " " + zadnja_km_info + settingValue.getUnit_kmOrMile());

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerAddUpdateOstaliTrosak);
        otherExpensesUpdateAdapter = new OtherExpensesUpdateAdapter(getContext(), otherExpensesModel, listaTroskova, pritisakTipke);
        recyclerView.setAdapter(otherExpensesUpdateAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        postaviSpinnere(savedInstanceState);

        if (pritisakTipke != 0) {

            if (pritisakTipke == R.id.menu_icon1) {

                postaviVrijednostiUpdatea();

            } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                postaviDanasnjiDateTime();

            }


        }


    }


    /**
     * Provjera da li je sve što treba biti uneseno
     *
     * @param otherExpensesModel
     * @return
     */
    private boolean provjeraUnosa(OtherExpensesModel otherExpensesModel) {

        boolean provjera = false;

        if (otherExpensesModel.getTros().size() > 0 && otherExpensesModel.getKmTrenutno().isEmpty() == false) {
            provjera = true;
        }
        return provjera;
    }


    /**
     * Metoda unutar koje se stvara model za unos, odnosno  model sa novim ažuriranim podacima
     *
     * @return
     */
    private OtherExpensesModel dajModelzaUnos() {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        stanje_km_post = unoskmEd.getText().toString();
        biljeske = biljeskeed.getText().toString();
        vrijeme_post = textViewTime.getText().toString();
        cijena_post = cijena_servisaed.getText().toString();
        vozilo_id = String.valueOf(manager.getVehicleId());

        if(pritisakTipke == R.id.menu_icon1)forma_datuma = otherExpensesModel.getDatumTroska();


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readDateFromSettings(getContext(),forma_datuma,vrijeme_post);



        Date date = new Date();
        OtherExpensesModel o = new OtherExpensesModel();
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


        o.setDatumTroska(date);
        o.setVrijemeTroska(vrijeme_post);
        o.setKmTrenutno(stanje_km_post);
        o.setNazivObrtnika(naziv_obrtnika_post);
        o.setBiljeske(biljeske);
        o.setVoziloId(vozilo_id);
        if (pritisakTipke == R.id.menu_icon1)  o.setIdOt(otherExpensesModel.getIdOt());
        o.setTros(otherExpensesModel.getTros());
        o.setNazivRazloga(naziv_razloga_post);



        if(!stanje_km_post.isEmpty() && !stanje_km_post.equals("")) {
            if ((Double.valueOf(zadnja_km_info) < Double.valueOf(stanje_km_post)) && zadnji_datum.before(date)) {

                SharedPreferences sharedPref = getContext().getSharedPreferences("DistanceDate", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("ZADNJA_KM", stanje_km_post);
                editor.putLong("ZADNJI_DAT", date.getTime());
                editor.commit();

            }
        }

        return o;
    }


    /**
     * Metoda za postavljanje trenutnog datuma i vremena u za to predviđene TextViewe
     */
    public void postaviDanasnjiDateTime() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");

        String time = timeformat.format(c.getTime());


        forma_datuma = c.getTime();
        forma_vremena = c.getTime();

        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readDateFromSettings(getContext(),forma_datuma,time);

        String date = dateformat.format(c.getTime());
        textViewDate.setText(settingValue.getFormatted_date());
        textViewTime.setText(settingValue.getVrijeme());
    }


    /**
     * Metoda za postavljanje vrijednosti u UI, ako smo došli ažurirati
     */
    public void postaviVrijednostiUpdatea() {


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readDateFromSettings(getContext(),otherExpensesModel.getDatumTroska(),otherExpensesModel.getVrijemeTroska());

        textViewDate.setText(settingValue.getFormatted_date());
        textViewTime.setText(settingValue.getVrijeme());
        unoskmEd.setText(otherExpensesModel.getKmTrenutno());
        biljeskeed.setText(otherExpensesModel.getBiljeske());


    }


    /**
     * Metoda unutar koje se postavljaju svi spinneri
     */
    public void postaviSpinnere(Bundle onSaveInstanceState) {


        icon_spinner_obrtnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaObrtnika", obrtniciModels2);


                NoviObrtnikFragment noviObrtnikFragment = new NoviObrtnikFragment();
                noviObrtnikFragment.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_updateOtherExpenses, noviObrtnikFragment)
                        .addToBackStack("NoviObrtnik").commit();


            }
        });


        icon_spinner_naziv_servisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ExpensesOtherExpensesModel t = new ExpensesOtherExpensesModel();
                t.setIznos(cijena_servisaed.getText().toString());
                t.setNazivOstalogTroska(naziv_servisa_post);


                otherExpensesModel.getTros().add(t);
                cijena_servisaed.setText("");
                //        spinner_obrtnik.setSelection(0);

                recyclerView.getAdapter().notifyItemInserted(otherExpensesModel.getTros().size() - 1);

            }
        });


        //Pokretanje fragmenta novog naziva Servisa
        icon_spinner_novi_naziv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaNazivaServisa", ostaliTroskoviNaziviModels2);


                NoviNazivOstalogTroskaFragment novi = new NoviNazivOstalogTroskaFragment();
                novi.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_updateOtherExpenses, novi).addToBackStack("NoviNazivServisa").commit();


            }
        });

        //Pokretanje fragmenta novog naziva Razloga
        icon_spinner_novi_razlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundleListe = new Bundle();
                bundleListe.putStringArrayList("listaNazivaRazloga", razlogModels2);
                bundleListe.putString("Dolazak","KlasaOtherExp");

                NoviRazlogFragment noviRazlogFragment = new NoviRazlogFragment();
                noviRazlogFragment.setArguments(bundleListe);
                fr.beginTransaction().replace(R.id.lin_ley_updateOtherExpenses, noviRazlogFragment).addToBackStack("NoviRazlog").commit();

            }
        });


        //pokretanje DatePickera
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = new UpdateOtherExpensesFragment.DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePickerOtherExpenses");


            }
        });


        //pokretanje TimePickera
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newTimeFragment = new UpdateOtherExpensesFragment.TimePickerFragment();
                newTimeFragment.show(getActivity().getSupportFragmentManager(), "timepickerOtherExpenses");


            }
        });



        if(onSaveInstanceState == null) {
            final Call<ArrayList<ObrtniciModel>> obrtnici_lista_Call = RestClient.getInstance().getApiService().getObrtnici();
            Call<ArrayList<OstaliTroskoviNaziviModel>> nazivi_servisa_lista_Call = RestClient.getInstance().getApiService().getNaziveTroskova();
            Call<ArrayList<RazlogModel>> naziviRazloga_Call = RestClient.getInstance().getApiService().getRazlog();

            //radnja u pozadini (enqueue) dakle ne u istoj dretvi
            obrtnici_lista_Call.enqueue(new Callback<ArrayList<ObrtniciModel>>() {
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

                            spinner_obrtnik.setSelection(obrtniciModels2.indexOf(otherExpensesModel.getNazivObrtnika()));

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


                    }


                }

                @Override
                public void onFailure(Call<ArrayList<ObrtniciModel>> call, Throwable t) {

                    Toast.makeText(getContext(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR_Spinner_Obrtnik", t.getMessage().toString());

                }
            });


            nazivi_servisa_lista_Call.enqueue(new Callback<ArrayList<OstaliTroskoviNaziviModel>>() {
                @Override
                public void onResponse(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Response<ArrayList<OstaliTroskoviNaziviModel>> response) {

                    if (response.isSuccessful()) {

                        ostaliTroskoviNaziviModels = response.body();


                        for (int i = 0; i < ostaliTroskoviNaziviModels.size(); i++) {

                            ostaliTroskoviNaziviModels2.add(ostaliTroskoviNaziviModels.get(i).getNazivOstalogTroska());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ostaliTroskoviNaziviModels2);
                        spinner_naziv_servisa.setAdapter(adapter);


                        if (pritisakTipke == R.id.menu_icon1) {

                            spinner_naziv_servisa.setSelection(ostaliTroskoviNaziviModels2.indexOf(otherExpensesModel.getTros().get(0).getNazivOstalogTroska()));

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

                @Override
                public void onFailure(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Throwable t) {


                    Toast.makeText(getContext(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR_Spinner_Naziv_Ser", t.getMessage().toString());

                }
            });


            naziviRazloga_Call.enqueue(new Callback<ArrayList<RazlogModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RazlogModel>> call, Response<ArrayList<RazlogModel>> response) {


                    if (response.isSuccessful()) {

                        razlogModels = response.body();


                        for (int i = 0; i < razlogModels.size(); i++) {

                            razlogModels2.add(razlogModels.get(i).getNazivRazloga());


                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, razlogModels2);
                        spinner_razlog.setAdapter(adapter);


                        if (pritisakTipke == R.id.menu_icon1) {

                            spinner_razlog.setSelection(razlogModels2.indexOf(otherExpensesModel.getNazivRazloga()));

                        } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                            spinner_razlog.setSelection(0);

                        }


                        spinner_razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                naziv_razloga_post = adapterView.getItemAtPosition(i).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }


                }

                @Override
                public void onFailure(Call<ArrayList<RazlogModel>> call, Throwable t) {

                    Toast.makeText(getContext(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                    Log.d("ERROR_Spinner_Razlog", t.getMessage().toString());
                }
            });

        }else{


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, obrtniciModels2);
            spinner_obrtnik.setAdapter(adapter);


            if (pritisakTipke == R.id.menu_icon1) {

                spinner_obrtnik.setSelection(obrtniciModels2.indexOf(otherExpensesModel.getNazivObrtnika()));

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




            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ostaliTroskoviNaziviModels2);
            spinner_naziv_servisa.setAdapter(adapter2);


            if (pritisakTipke == R.id.menu_icon1) {

                spinner_naziv_servisa.setSelection(ostaliTroskoviNaziviModels2.indexOf(otherExpensesModel.getTros().get(0).getNazivOstalogTroska()));

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




            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, razlogModels2);
            spinner_razlog.setAdapter(adapter3);


            if (pritisakTipke == R.id.menu_icon1) {

                spinner_razlog.setSelection(razlogModels2.indexOf(otherExpensesModel.getNazivRazloga()));

            } else if (pritisakTipke == R.id.buttonAddOtherExpenses) {

                spinner_razlog.setSelection(0);

            }


            spinner_razlog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    naziv_razloga_post = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });






        }
    }


    /**
     * Metoda za ažuriranje odabranog ostalog troška vozila
     */
    private void update() {


        final OtherExpensesModel model = dajModelzaUnos();

        if (provjeraUnosa(model) == true) {


            provjeraUnosaUdaljenosti(Double.valueOf(model.getKmTrenutno()), model.getDatumTroska(), Integer.parseInt(model.getVoziloId()), new ProvjeraCallBack() {
                @Override
                public void onSuccess(boolean value) {

                    potvrda = value;


                    if(potvrda == true) {

                        Call<Ok> updateOstaliTroskovirepos = RestClient.getInstance().getApiService().updateOstaliTroskovi(model, Integer.valueOf(otherExpensesModel.getIdOt()));


                        updateOstaliTroskovirepos.enqueue(new Callback<Ok>() {
                            @Override
                            public void onResponse(Call<Ok> call, Response<Ok> response) {

                                if (response.isSuccessful())
                                    Toast.makeText(getActivity(), R.string.zapis_azuriran, Toast.LENGTH_SHORT).show();

                                Bundle b2 = new Bundle();
                                b2.putString("pozicija", model.getIdOt());
                                b2.putBoolean("zastavica_update", true);
                                b2.putParcelable("ObjToDisplay",model);


                                Intent in = new Intent(getActivity(), OtherExpensesDetail.class);
                                in.putExtra("bundle_lista", b2);
                                startActivity(in);

                            }

                            @Override
                            public void onFailure(Call<Ok> call, Throwable t) {

                                Log.d("greska_update_Service", t.getMessage());
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
        } else
            Toast.makeText(getActivity(), getString(R.string.prazno_oprez), Toast.LENGTH_SHORT).show();

    }


    /**
     * Metoda za unos novog ostalog troska
     */
    private void unesi() {


        final OtherExpensesModel model = dajModelzaUnos();

        //Ako je sve uneseno kako treba
        if (provjeraUnosa(model) == true) {


            provjeraUnosaUdaljenosti(Double.valueOf(model.getKmTrenutno()), model.getDatumTroska(), Integer.parseInt(model.getVoziloId()), new ProvjeraCallBack() {
                @Override
                public void onSuccess(boolean value) {

                    potvrda = value;


                    if(potvrda == true) {


                        Call<Ok> unosOstalogTroskaRepos = RestClient.getInstance().getApiService().addOstaliTroskovi(model);
                        unosOstalogTroskaRepos.enqueue(new Callback<Ok>() {
                            @Override
                            public void onResponse(Call<Ok> call, Response<Ok> response) {

                                if (response.isSuccessful())
                                    Toast.makeText(getActivity(), R.string.dodan_zapis_ostali, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), OtherExpensesActivity.class));
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


        } else
            Toast.makeText(getActivity(), getString(R.string.prazno_oprez), Toast.LENGTH_SHORT).show();

    }





    private void  provjeraUnosaUdaljenosti(Double km_unosa, Date datum_unosa,int vozilo_id, final ProvjeraCallBack provjeraCallBack){


        if(km_unosa.equals(""))  provjeraCallBack.onSuccess(false);
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


    /**
     * Pokretanje Alert Dialoga
     */
    private void startAlertDialog() {

        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(otherExpensesModel.getIdOt()));
        bundle_id.putString("Naziv_Klase", "OstaliTroskoviDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(getActivity().getSupportFragmentManager(), "Alert_Delete");
    }



    /**
     * Postavljanje RecyclerViewa za Ostale troškove
     *
     * @param s
     */
    public void setTroskoveOstalihTroskova(ExpensesOtherExpensesModel s) {
        vvv = s;

        otherExpensesModel.getTros().add(vvv);
        cijena_servisaed.setText("");

        recyclerView.getAdapter().notifyItemInserted(otherExpensesModel.getTros().size() - 1);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.invalidate();


    }


    /**
     * Kada se promjene postavke, osvježi UI
     *
     * @param sharedPreferences
     * @param s
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);

    }


    //DatePicker za odabir Datuma
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

            //postavi dohvačeno

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


    //TimePicker za odabir vremena
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

            //postavi dohvačeno


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            promjena_vrijeme = calendar.getTime();

            //pročitaj postavke
            GetSettingValue settingValue = new GetSettingValue();
            settingValue.readTimeOnlyFromSettings(getContext(),promjena_vrijeme);
            textViewTime.setText(settingValue.getVrijeme());

            getDialog().dismiss();
        }
    }


}
