package com.example.crespo.vehicleexpenses.Activity.Chart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Models.LineGasModel;
import Models.ParametersModel;
import Models.PieOtherModel;
import Models.PieServisiModel;
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LineChartOpcenito extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String datumOd, datumDo;
    private Toolbar toolbar;
    private LineChart linechart;
    private ArrayList<PieServisiModel> lineServisiModels;
    private ArrayList<LineGasModel> lineGasModels;
    private ArrayList<PieOtherModel> lineOtherModels;
    private ArrayList<Entry> entries = new ArrayList<Entry>();
    private DateFormat dateFormat;
    private String unit_kmOrMile, date_format, time_format, currency_format, formatted_date, vrijeme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_opcenito);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.grafikon_kretanja_troskova);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }


        Bundle bundle = getIntent().getBundleExtra("Datumi");
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");


        readSettings();

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);
        ParametersModel parametersModel = new ParametersModel();
        parametersModel.setVozilo_id(manager.getVehicleId());
        parametersModel.setId_servisi(1);
        parametersModel.setDateOd(datumOd);
        parametersModel.setDateDo(datumDo);


        linechart = (LineChart) findViewById(R.id.chartLineOpcenito);


        Call<ArrayList<PieServisiModel>> getDataLineRepos = RestClient.getInstance().getApiService().getDataForLineServisi(parametersModel);
        getDataLineRepos.enqueue(new Callback<ArrayList<PieServisiModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieServisiModel>> call, Response<ArrayList<PieServisiModel>> response) {

                if (response.isSuccessful()) {

                    lineServisiModels = response.body();

                    if(!lineServisiModels.isEmpty()) {

                        for (int i = 0; i < lineServisiModels.size(); i++) {


                            entries.add(new Entry(lineServisiModels.get(i).getDatumServis().getTime(), lineServisiModels.get(i).getIznos().floatValue()));


                        }


                        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.kretanje_troskova));
                        LineData lineData = new LineData(dataSet);

                        XAxis xAxis = linechart.getXAxis();
                        xAxis.setLabelRotationAngle(-45);
                        xAxis.setGranularity(1f);
                        xAxis.disableGridDashedLine();
                        xAxis.setValueFormatter(new LineChartOpcenito.DateValueFormatter());

                        linechart.setData(lineData);
                        Description des = new Description();
                        des.setText("");
                        linechart.setDescription(des);
                        MyMarkerView myMarkerView = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
                        linechart.setMarkerView(myMarkerView);


                        linechart.invalidate();
                        linechart.notifyDataSetChanged();

                    }

                    getToc();

                }

            }

            @Override
            public void onFailure(Call<ArrayList<PieServisiModel>> call, Throwable t) {

                Log.d("ErrLineServis", t.getMessage().toString());
                Toast.makeText(LineChartOpcenito.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals("datum_list")) {
            date_format = sharedPreferences.getString("datum_list", "1");


            switch (date_format) {

                case "1":
                    formatted_date = "dd/MM/yyyy";
                    break;
                case "2":
                    formatted_date = "dd-MM-yyyy";
                    break;
                case "3":
                    formatted_date = "yyyy/MM/dd";
                    break;
                case "4":
                    formatted_date = "yyyy-MM-dd";
                    break;
                case "5":
                    formatted_date = "dd/M/yyyy";
                    break;
                default:


            }
        }

        if (s.equals("valuta_list")) {

            currency_format = sharedPreferences.getString("valuta_list", "4");


            switch (currency_format) {

                case "1":
                    currency_format = " €";
                    break;
                case "2":
                    currency_format = " $";
                    break;
                case "3":
                    currency_format = " £";
                    break;

                case "4":
                    currency_format = " HRK";
                    break;

                default:


            }


        }


    }

    private ParametersModel getModel() {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);
        ParametersModel s = new ParametersModel();
        s.setDateOd(datumOd);
        s.setDateDo(datumDo);
        s.setId_servisi(1);
        s.setVozilo_id(manager.getVehicleId());

        return s;
    }

    private void getToc() {

        Call<ArrayList<LineGasModel>> getDataRepos = RestClient.getInstance().getApiService().getDataForLineGas(getModel());
        getDataRepos.enqueue(new Callback<ArrayList<LineGasModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LineGasModel>> call, Response<ArrayList<LineGasModel>> response) {
                if (response.isSuccessful()) {

                    lineGasModels = response.body();


                    if(!lineGasModels.isEmpty()) {

                        for (int i = 0; i < lineGasModels.size(); i++) {


                            entries.add(new Entry(lineGasModels.get(i).getDatum().getTime(), lineGasModels.get(i).getIznos().floatValue()));


                        }


                        LineDataSet dataSet = new LineDataSet(entries, "Kretanje točenja");
                        LineData lineData = new LineData(dataSet);

                        XAxis xAxis = linechart.getXAxis();
                        xAxis.setLabelRotationAngle(-45);
                        xAxis.setGranularity(1f);
                        xAxis.disableGridDashedLine();
                        xAxis.setValueFormatter(new LineChartOpcenito.DateValueFormatter());

                        linechart.setData(lineData);
                        Description des = new Description();
                        des.setText("");
                        linechart.setDescription(des);
                        MyMarkerView myMarkerView = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
                        linechart.setMarkerView(myMarkerView);


                        linechart.invalidate();
                        linechart.notifyDataSetChanged();
                    }
                    getOstalo();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<LineGasModel>> call, Throwable t) {


                Log.d("ErrLineServis", t.getMessage().toString());
                Toast.makeText(LineChartOpcenito.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getOstalo() {


        Call<ArrayList<PieOtherModel>> getDataLineRepos = RestClient.getInstance().getApiService().getDataForLineOther(getModel());
        getDataLineRepos.enqueue(new Callback<ArrayList<PieOtherModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieOtherModel>> call, Response<ArrayList<PieOtherModel>> response) {

                if (response.isSuccessful()) {


                    lineOtherModels = response.body();

                    if (!lineOtherModels.isEmpty()) {

                        for (int i = 0; i < lineOtherModels.size(); i++) {


                            entries.add(new Entry(lineOtherModels.get(i).getDatumTroska().getTime(), lineOtherModels.get(i).getIznos().floatValue()));


                        }


                        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.kretanje_troskova));
                        LineData lineData = new LineData(dataSet);

                        XAxis xAxis = linechart.getXAxis();
                        xAxis.setLabelRotationAngle(-45);
                        xAxis.setGranularity(1f);
                        xAxis.disableGridDashedLine();
                        xAxis.setValueFormatter(new LineChartOpcenito.DateValueFormatter());

                        linechart.setData(lineData);
                        Description des = new Description();
                        des.setText("");
                        linechart.setDescription(des);
                        MyMarkerView myMarkerView = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
                        linechart.setMarkerView(myMarkerView);


                        linechart.invalidate();
                        linechart.notifyDataSetChanged();


                    }else {

                        linechart.invalidate();
                        linechart.setNoDataText(getString(R.string.grafikon_prazan));
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<PieOtherModel>> call, Throwable t) {


                Log.d("ErrLineServis", t.getMessage().toString());
                Toast.makeText(LineChartOpcenito.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void readSettings() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date_format = preferences.getString("datum_list", "1");
        currency_format = preferences.getString("valuta_list", "1");


        switch (currency_format) {

            case "1":
                currency_format = " €";
                break;
            case "2":
                currency_format = " $";
                break;
            case "3":
                currency_format = " £";
                break;

            case "4":
                currency_format = " HRK";
                break;

            default:


        }


        switch (date_format) {

            case "1":
                formatted_date = "dd/MM/yyyy";
                break;
            case "2":
                formatted_date = "dd-MM-yyyy";
                break;
            case "3":
                formatted_date = "yyyy/MM/dd";
                break;
            case "4":
                formatted_date = "yyyy-MM-dd";
                break;
            case "5":
                formatted_date = "dd/M/yyyy";
                break;
            default:


        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    private class DateValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // Simple version. You should use a DateFormatter to specify how you want to textually represent your date.

            dateFormat = new SimpleDateFormat(formatted_date);
            Date date = new Date(new Float(value).longValue());

            return dateFormat.format(date);
        }
    }



    @Override
    protected void attachBaseContext(Context newBase) {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String translate_value = sharedPreferences.getString(SharedpreferencesKeys.PRIJEVOD, "1");
        String translate_value2;

        switch (translate_value) {
            case "1":
                translate_value2 = "hr";
                break;
            case "2":
                translate_value2 = "en";

                break;
            default:
                translate_value2 = "hr";
        }


        Locale locale = new Locale(translate_value2);
        super.attachBaseContext(ConfigurationWrapper.wrapLocale(newBase, locale));
    }
}
