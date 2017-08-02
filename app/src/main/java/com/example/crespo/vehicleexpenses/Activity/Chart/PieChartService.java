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
import Models.PieServisiModel;
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PieChartService extends AppCompatActivity {

    private PieChart pieChart;
    private String datumOd, datumDo;
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private ArrayList<PieServisiModel> pieServisiModelList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_service);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.grafikon_udjela_troskova);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }


        Bundle bundle = getIntent().getBundleExtra("Datumi");
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");


        pieChart = (PieChart) findViewById(R.id.chartPieService);

        ParametersModel parametersModel = new ParametersModel();
        parametersModel.setVozilo_id(manager.getVehicleId());
        parametersModel.setId_servisi(1);
        parametersModel.setDateOd(datumOd);
        parametersModel.setDateDo(datumDo);


        Call<ArrayList<PieServisiModel>> getDataPieRepos = RestClient.getInstance().getApiService().getDataForPieServisi(parametersModel);
        getDataPieRepos.enqueue(new Callback<ArrayList<PieServisiModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieServisiModel>> call, Response<ArrayList<PieServisiModel>> response) {

                if (response.isSuccessful()) {

                    pieServisiModelList = response.body();

                    if (!pieServisiModelList.isEmpty()) {
                        float ukupno = 0;
                        for (int j = 0; j < pieServisiModelList.size(); j++)
                            ukupno += pieServisiModelList.get(j).getIznos().floatValue();


                        for (int i = 0; i < pieServisiModelList.size(); i++)
                            entries.add(new PieEntry(((pieServisiModelList.get(i).getIznos().floatValue() / ukupno) * 100), pieServisiModelList.get(i).getNazivServisa()));

                        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.naziv_servisa));
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



                    }else{

                        pieChart.invalidate();
                        pieChart.setNoDataText(getString(R.string.grafikon_prazan));

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PieServisiModel>> call, Throwable t) {

                Log.d("ErrPieServis", t.getMessage().toString());
                Toast.makeText(PieChartService.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return super.onOptionsItemSelected(item);
    }


}
