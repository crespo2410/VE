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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Locale;

import Models.ParametersModel;
import Models.PieGasModel;
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  Klasa zadužena za stvaranje tortnog grafikona
 */
public class Pie2ChartGas extends AppCompatActivity {


    private PieChart pieChart;
    private String datumOd, datumDo;
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private ArrayList<PieGasModel> pieGasModels;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie2_chart_gas);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.grafikon_udio_benzinske);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }


        Bundle bundle = getIntent().getBundleExtra("Datumi");
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");


        pieChart = (PieChart) findViewById(R.id.chartPie2Gas);

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);
        ParametersModel parametersModel = new ParametersModel();
        parametersModel.setVozilo_id(manager.getVehicleId());
        parametersModel.setId_servisi(1);
        parametersModel.setDateOd(datumOd);
        parametersModel.setDateDo(datumDo);



        //Dohvacanje potrebnih podataka za grafikon
        Call<ArrayList<PieGasModel>> getDataRepos = RestClient.getInstance().getApiService().getDataForPieGas(parametersModel);
        getDataRepos.enqueue(new Callback<ArrayList<PieGasModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieGasModel>> call, Response<ArrayList<PieGasModel>> response) {
                if (response.isSuccessful()) {

                    pieGasModels = response.body();

                    if(!pieGasModels.isEmpty()) {

                        float ukupno = 0;
                        for (int j = 0; j < pieGasModels.size(); j++)
                            ukupno += pieGasModels.get(j).getIznos().floatValue();


                        for (int i = 0; i < pieGasModels.size(); i++)
                            entries.add(new PieEntry(((pieGasModels.get(i).getIznos().floatValue() / ukupno) * 100), pieGasModels.get(i).getNaziv()));


                        //Editiranje (stvaranje, dodavanje podataka, uređivanje) tortnog grafikona
                        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.naziv_benzinske));
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        PieData data = new PieData(dataSet);
                        data.setValueFormatter(new PercentFormatter());
                        data.setValueTextSize(12f);


                        Legend legend = pieChart.getLegend();
                        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);


                        pieChart.setData(data);
                        Description des = new Description();
                        des.setText("");
                        pieChart.setDescription(des);
                        pieChart.notifyDataSetChanged();
                        pieChart.invalidate();

                    }else {


                        pieChart.invalidate();
                        pieChart.setNoDataText(getString(R.string.grafikon_prazan));
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PieGasModel>> call, Throwable t) {
                Log.d("ErrPie2Gas", t.getMessage().toString());
                Toast.makeText(Pie2ChartGas.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return super.onOptionsItemSelected(item);
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
