package Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.ReminderActivity;
import com.example.crespo.vehicleexpenses.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.OstaliTroskoviNaziviModel;
import Models.ServiceNameModel;
import Retrofit.api.client.RestClient;
import helper.AlarmService;
import helper.DatabaseHelper;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateReminderFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {


    public String contentString, titleString, typeString;
    public List<Map<String, String>> mapList;
    public long timeInMilliseconds, datefromDB;
    private SimpleAdapter adapter;
    private DatabaseHelper database;
    private EditText content;
    private String time, date;
    private int id, repeatMode;
    private Map<String, String> item1, item2, item3;
    private DateFormat df, df1;
    private Calendar alertTime;
    private String[] repeatModes;
    private Spinner tipPodsjetnika, tipPodsjetnika2;
    private ImageView icon_spinner, icon_tip_podsjetnika1, icon_tip_podsjetnika2;
    private ListView reminderSettings;
    private ArrayList<ServiceNameModel> serviceNameModels;
    private ArrayList<String> servisiNaziviModels2;
    private Activity activity;
    private Menu menuReference;
    private int vozilo_id;
    private ArrayList<OstaliTroskoviNaziviModel> troskoviNaziviModels;
    private ArrayList<String> troksoviNaziviModels2;
    private ArrayList<String> odabir_tipa = new ArrayList<>();
    private Toolbar toolbar;
    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DELETED")) {
                terminateActivity2();
            }
        }
    };


    public UpdateReminderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter("DELETED");
        broadcastManager.registerReceiver(deleteReceiver, filter);

        mapList = new ArrayList<Map<String, String>>();
        item1 = new HashMap<String, String>();
        item2 = new HashMap<String, String>();
        item3 = new HashMap<String, String>();

        database = new DatabaseHelper(getActivity());


        repeatModes = new String[]{getString(R.string.repeat_none), getString(R.string.repeat_hourly), getString(R.string.repeat_daily), getString(R.string.repeat_weekly), getString(R.string.repeat_mjesecno), getString(R.string.repeat_yearly)};
        repeatMode = 0;

        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("ID", 0);
        alertTime = Calendar.getInstance();


        df = new SimpleDateFormat("hh:mm aa");
        df1 = new SimpleDateFormat("dd/MM/yy");


        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
        vozilo_id = manager.getVehicleId();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        servisiNaziviModels2 = new ArrayList<>();
        troksoviNaziviModels2 = new ArrayList<>();
        serviceNameModels = new ArrayList<>();
        troskoviNaziviModels = new ArrayList<>();


        return inflater.inflate(R.layout.add_update_reminder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tipPodsjetnika = (Spinner) getView().findViewById(R.id.spinner_tip_podsjetnika);
        tipPodsjetnika2 = (Spinner) getView().findViewById(R.id.spinner_tip_podsjetnika2);
        content = (EditText) getView().findViewById(R.id.alertContent);
        reminderSettings = (ListView) getView().findViewById(R.id.reminderSettings);
        icon_tip_podsjetnika1 = (ImageView) getView().findViewById(R.id.icon_tip_podsjetnika);
        icon_tip_podsjetnika2 = (ImageView) getView().findViewById(R.id.icon_tip_podsjetnika2);
        icon_spinner = (ImageView) getView().findViewById(R.id.icon_spinner_novo);


        if (odabir_tipa.size() == 0) {

            odabir_tipa.add(getString(R.string.s));
            odabir_tipa.add(getString(R.string.o_t));

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.primary2));
        toolbar.setTitle(R.string.stvori_uredi_podsjetnik);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.primary_dark2));
        }


        postaviSpinnere();

        item1.put("title", getString(R.string.time_reminder));
        item1.put("subtext", time);
        item2.put("title", getString(R.string.date_reminder));
        item2.put("subtext", date);
        item3.put("title", getString(R.string.repeat_remaider));
        item3.put("subtext", repeatModes[repeatMode]);


        mapList.add(item1);
        mapList.add(item2);
        mapList.add(item3);
        adapter = new SimpleAdapter(getActivity(), mapList, android.R.layout.simple_list_item_2,
                new String[]{"title", "subtext"}, new int[]{android.R.id.text1, android.R.id.text2});

        reminderSettings.setAdapter(adapter);

        reminderSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    TimePickerDialog timePicker = timePicker();
                    timePicker.show();
                } else if (i == 1) {
                    DatePickerDialog datePicker = datePicker();
                    datePicker.show();
                } else {
                    repeatDialog().show();
                }

            }
        });


    }

    private int ReadVoziloId() {

        SharedPreferences sharedPref = getActivity().getSharedPreferences("SharedVoziloId", Context.MODE_PRIVATE);
        int vozilo_id = sharedPref.getInt("vozilo_id", 0);
        return vozilo_id;
    }

    public void postaviSpinnere() {

        //treba prvo pogledati da li je insert ili update
        //- ako je insert onda se gleda koji je odabir na prvom spinneru te se s obzirom na to skida
        //- ako je update onda se iz baze pogleda koji je tip zapisa i prema tome se postavi prvi odnsono drugi spinner

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, odabir_tipa);
        tipPodsjetnika.setAdapter(adapter2);


        tipPodsjetnika.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (adapterView.getItemAtPosition(i).toString().equals("Servisi")) {


                    Call<ArrayList<ServiceNameModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNazivServisa();
                    nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<ServiceNameModel>>() {

                        @Override
                        public void onResponse(Call<ArrayList<ServiceNameModel>> call, Response<ArrayList<ServiceNameModel>> response) {

                            if (response.isSuccessful()) {

                                serviceNameModels = response.body();


                                if (servisiNaziviModels2.size() == 0) {

                                    for (int i = 0; i < serviceNameModels.size(); i++) {

                                        servisiNaziviModels2.add(serviceNameModels.get(i).getNazivServisa());

                                    }

                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, servisiNaziviModels2);
                                tipPodsjetnika2.setAdapter(adapter);


                                tipPodsjetnika2.setSelection(servisiNaziviModels2.indexOf(titleString));


                                tipPodsjetnika2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        titleString = adapterView.getItemAtPosition(i).toString();
                                        //  Toast.makeText(getActivity(),titleString, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<ServiceNameModel>> call, Throwable t) {


                            Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                            Log.d("ERROR_Spinner_Naziv_Ser", t.getMessage().toString());

                        }
                    });


                    icon_spinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Bundle bundleListe = new Bundle();
                            bundleListe.putStringArrayList("listaNazivaServisa", servisiNaziviModels2);


                            NoviNazivServisa2 novi = new NoviNazivServisa2();
                            novi.setArguments(bundleListe);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.lin_ley_updateReminder, novi);
                            transaction.addToBackStack("NoviNazivServisa2");
                            transaction.commit();


                        }
                    });


                } else if (adapterView.getItemAtPosition(i).toString().equals("Ostali Troskovi")) {


                    Call<ArrayList<OstaliTroskoviNaziviModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNaziveTroskova();
                    nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<OstaliTroskoviNaziviModel>>() {
                        @Override
                        public void onResponse(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Response<ArrayList<OstaliTroskoviNaziviModel>> response) {
                            if (response.isSuccessful()) {


                                troskoviNaziviModels = response.body();


                                if (troksoviNaziviModels2.size() == 0) {
                                    for (int i = 0; i < troskoviNaziviModels.size(); i++) {

                                        troksoviNaziviModels2.add(troskoviNaziviModels.get(i).getNazivOstalogTroska());

                                    }

                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, troksoviNaziviModels2);
                                tipPodsjetnika2.setAdapter(adapter);


                                tipPodsjetnika2.setSelection(troksoviNaziviModels2.indexOf(titleString));


                                tipPodsjetnika2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        titleString = adapterView.getItemAtPosition(i).toString();
                                        // Toast.makeText(getActivity(),titleString, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Throwable t) {

                            Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                            Log.d("ERROR_Spinner_Naziv_Ser", t.getMessage().toString());


                        }
                    });


                    icon_spinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Bundle bundleListe = new Bundle();
                            bundleListe.putStringArrayList("listaNazivaServisa", troksoviNaziviModels2);


                            NoviNazivOstalogTroska novi = new NoviNazivOstalogTroska();
                            novi.setArguments(bundleListe);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.lin_ley_updateReminder, novi).addToBackStack("NoviNazivOstalogTroska").commit();


                        }
                    });


                }else {


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (id > 0) {

            Cursor cursor = database.getItem(id);
            cursor.moveToFirst();
            typeString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TYPE));
            contentString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_CONTENT));
            titleString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TITLE));
            timeInMilliseconds = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TIME));
            datefromDB = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TIME));
            repeatMode = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_FREQUENCY));


            content.setText(contentString);

            if (typeString.equalsIgnoreCase("alertServis")) {

                tipPodsjetnika.setSelection(0);

                Call<ArrayList<ServiceNameModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNazivServisa();
                nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<ServiceNameModel>>() {

                    @Override
                    public void onResponse(Call<ArrayList<ServiceNameModel>> call, Response<ArrayList<ServiceNameModel>> response) {

                        if (response.isSuccessful()) {

                            serviceNameModels = response.body();


                            for (int i = 0; i < serviceNameModels.size(); i++) {

                                servisiNaziviModels2.add(serviceNameModels.get(i).getNazivServisa());

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, servisiNaziviModels2);
                            tipPodsjetnika2.setAdapter(adapter);


                            tipPodsjetnika2.setSelection(servisiNaziviModels2.indexOf(titleString));


                            tipPodsjetnika2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    titleString = adapterView.getItemAtPosition(i).toString();
                                    //   Toast.makeText(getActivity(),titleString, Toast.LENGTH_SHORT).show();
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


            }
            if (typeString.equalsIgnoreCase("alertTrosak")) {



                tipPodsjetnika.setSelection(1);
                Call<ArrayList<OstaliTroskoviNaziviModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNaziveTroskova();
                nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<OstaliTroskoviNaziviModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Response<ArrayList<OstaliTroskoviNaziviModel>> response) {
                        if (response.isSuccessful()) {


                            troskoviNaziviModels = response.body();

                            for (int i = 0; i < troskoviNaziviModels.size(); i++) {

                                troksoviNaziviModels2.add(troskoviNaziviModels.get(i).getNazivOstalogTroska());

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, troksoviNaziviModels2);
                            tipPodsjetnika2.setAdapter(adapter);


                            tipPodsjetnika2.setSelection(servisiNaziviModels2.indexOf(titleString));


                            tipPodsjetnika2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    titleString = adapterView.getItemAtPosition(i).toString();
                                    //   Toast.makeText(getActivity(),titleString, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Throwable t) {

                        Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR_Spinner_Naziv_Ser", t.getMessage().toString());


                    }
                });

            }


            alertTime.setTimeInMillis(timeInMilliseconds);
            DateFormat df = new SimpleDateFormat("hh:mm aa");
            DateFormat df1 = new SimpleDateFormat("dd/MM/yy");

            time = df.format(alertTime.getTime());
            date = df1.format(alertTime.getTime());

            if (!cursor.isClosed()) {

                cursor.close();

            }


        } else {


            if (tipPodsjetnika.getSelectedItemPosition() == 0) {


                Call<ArrayList<ServiceNameModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNazivServisa();
                nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<ServiceNameModel>>() {

                    @Override
                    public void onResponse(Call<ArrayList<ServiceNameModel>> call, Response<ArrayList<ServiceNameModel>> response) {

                        if (response.isSuccessful()) {

                            serviceNameModels = response.body();


                            for (int i = 0; i < serviceNameModels.size(); i++) {

                                servisiNaziviModels2.add(serviceNameModels.get(i).getNazivServisa());

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, servisiNaziviModels2);
                            tipPodsjetnika2.setAdapter(adapter);


                            tipPodsjetnika2.setSelection(servisiNaziviModels2.indexOf(titleString));


                            tipPodsjetnika2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    titleString = adapterView.getItemAtPosition(i).toString();

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


            }
            if (tipPodsjetnika.getSelectedItemPosition() == 1) {

                tipPodsjetnika.setSelection(1);
                Call<ArrayList<OstaliTroskoviNaziviModel>> nazivi_servisa_lista_repos = RestClient.getInstance().getApiService().getNaziveTroskova();
                nazivi_servisa_lista_repos.enqueue(new Callback<ArrayList<OstaliTroskoviNaziviModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<OstaliTroskoviNaziviModel>> call, Response<ArrayList<OstaliTroskoviNaziviModel>> response) {
                        if (response.isSuccessful()) {


                            troskoviNaziviModels = response.body();

                            for (int i = 0; i < troskoviNaziviModels.size(); i++) {

                                troksoviNaziviModels2.add(troskoviNaziviModels.get(i).getNazivOstalogTroska());

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, troksoviNaziviModels2);
                            tipPodsjetnika2.setAdapter(adapter);


                            tipPodsjetnika2.setSelection(servisiNaziviModels2.indexOf(titleString));


                            tipPodsjetnika2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    titleString = adapterView.getItemAtPosition(i).toString();

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


            }


            Calendar current = Calendar.getInstance();
            time = df.format(current.getTime());
            date = df1.format(current.getTime());
            alertTime.setTimeInMillis(current.getTimeInMillis());


        }

    }

    private void saveAlert() {

        contentString = content.getText().toString();


        if (!(alertTime.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())) {
            saveDialog(id, titleString, contentString, alertTime.getTimeInMillis()).show();

        } else {
            saveDialog(id, titleString, contentString, Calendar.getInstance().getTimeInMillis()).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        menuReference = menu;


        if (id > 0) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);
            menuReference.getItem(1).setIcon(R.drawable.ic_action_delete).setVisible(true);

        } else if (id <= 0) {

            menuReference.getItem(0).setIcon(R.drawable.ic_action_done);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {


            terminateActivity2();


        }
        if (item.getItemId() == R.id.menu_icon1) {


            saveAlert();

        }

        if (item.getItemId() == R.id.menu_icon2) {
            deleteDialog(id).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog saveDialog(int id, String title, String content, long time) {
        final int saveId = id;
        final long saveTime = time;
        final String saveMessage = content;
        final String saveTitle = title;


        return new AlertDialog.Builder(getActivity())

                .setTitle(R.string.potvrdi)
                .setMessage(R.string.da_li_zelite_spremiti)

                .setPositiveButton(R.string.potvrda, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int i) {

                        if (saveId > 0) {
                            Intent cancelPrevious = new Intent(getActivity(),
                                    AlarmService.class);
                            cancelPrevious.putExtra("id", saveId);
                            cancelPrevious.setAction(AlarmService.CANCEL);
                            getActivity().startService(cancelPrevious);
                            database.updateAlert(saveId, saveTitle, saveMessage, saveTime, repeatMode, vozilo_id);
                            createAlarm(saveId);

                        } else {
                            createAlarm((int) database.insertAlert(saveTitle, saveMessage, saveTime, repeatMode, vozilo_id));
                        }
                        terminateActivity2();
                        dialog.dismiss();
                    }

                })


                .setNegativeButton(R.string.negativno, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {

                        database.close();
                        dialog.dismiss();

                    }
                })
                .create();
    }

    private AlertDialog deleteDialog(int id) {

        final int deleteId = id;
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.potvrda)
                .setMessage(getString(R.string.delete_question))
                .setPositiveButton(R.string.potvrda, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {

                        if (deleteId > 0) {
                            // delete the alarm
                            Intent delete = new Intent(getActivity(), AlarmService.class);
                            delete.putExtra("id", deleteId);
                            delete.setAction(AlarmService.DELETE);
                            getActivity().startService(delete);
                        } else {
                            terminateActivity2();
                        }
                    }

                })
                .setNegativeButton(R.string.negativno, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();

    }

    // creates an alarm
    private void createAlarm(int id) {
        Intent alarm = new Intent(getActivity(), AlarmService.class);
        alarm.putExtra("id", id);
        alarm.setAction(AlarmService.CREATE);
        getActivity().startService(alarm);
        database.close();
    }

    // go back to main activity
    private void terminateActivity() {
        NavUtils.navigateUpFromSameTask(getActivity());
    }

    private void terminateActivity2() {
        Intent intent = new Intent(getActivity(), ReminderActivity.class);
        startActivity(intent);
    }

    private TimePickerDialog timePicker() {
        return new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        alertTime.set(Calendar.HOUR_OF_DAY, hour);
                        alertTime.set(Calendar.MINUTE, minute);
                        alertTime.set(Calendar.SECOND, 0);
                        time = df.format(alertTime.getTime());
                        item1.put("subtext", time);
                        adapter.notifyDataSetChanged();
                    }
                }, alertTime.get(Calendar.HOUR_OF_DAY), alertTime.get(Calendar.MINUTE), false);
    }

    // date picker
    private DatePickerDialog datePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        alertTime.set(Calendar.YEAR, year);
                        alertTime.set(Calendar.MONTH, month);
                        alertTime.set(Calendar.DAY_OF_MONTH, day);
                        date = df1.format(alertTime.getTime());
                        item2.put("subtext", date);
                        adapter.notifyDataSetChanged();
                    }
                }, alertTime.get(Calendar.YEAR), alertTime.get(Calendar.MONTH), alertTime.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return datePicker;
    }

    private AlertDialog repeatDialog() {
        final int prevRepeat = repeatMode;
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.repeat_remaider))
                .setSingleChoiceItems(repeatModes, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        repeatMode = i;
                    }
                })
                .setPositiveButton(getString(R.string.potvrda), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // set label to selected repeat mode
                        item3.put("subtext", repeatModes[repeatMode]);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.odustani), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        repeatMode = prevRepeat;
                    }
                })
                .create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();



    }

    @Override
    public void onBackStackChanged() {
        for (int entry = 0; entry < getActivity().getSupportFragmentManager().getBackStackEntryCount(); entry++) {
            Log.d("Stack", "Found fragment: " + getActivity().getSupportFragmentManager().getBackStackEntryAt(entry).getName());
        }
    }
}

