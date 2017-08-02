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
import Models.PieOtherModel;
import Models.PieServisiModel;
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PieChartOpcenito extends AppCompatActivity {

    private PieChart pieChart;
    private String datumOd, datumDo;
    private ArrayList<PieEntry> entries = new ArrayList<>();
    private ArrayList<PieServisiModel> pieServisiModelList;
    private ArrayList<PieGasModel> pieGasModels;
    private ArrayList<PieOtherModel> pieOtherModels;
    private Toolbar toolbar;
    private Double bundle_ukupno = 0., bundle_pojedinacno_s = 0., bundle_pojedinacno_t = 0., bundle_pojedinacno_ot = 0.;
    private float uk1 = 0,uk2 = 0,uk3 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_opcenito);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.grafikon_udjela_opcenito);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);


        Bundle bundle = getIntent().getBundleExtra("Datumi");
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");
        bundle_ukupno = bundle.getDouble("Ukupno");
        bundle_pojedinacno_t = bundle.getDouble("UkupnoTocenja");
        bundle_pojedinacno_s = bundle.getDouble("UkupnoServisi");
        bundle_pojedinacno_ot = bundle.getDouble("UkupnoOstali");

        pieChart = (PieChart) findViewById(R.id.chartPieOpcenito);

        setData();

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




    private void setData() {

        Call<ArrayList<PieServisiModel>> getDataPieRepos = RestClient.getInstance().getApiService().getDataForPieServisi(getModel());
        getDataPieRepos.enqueue(new Callback<ArrayList<PieServisiModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieServisiModel>> call, Response<ArrayList<PieServisiModel>> response) {

                if (response.isSuccessful()) {

                    pieServisiModelList = response.body();

                    if (!pieServisiModelList.isEmpty()) {

                        for (int j = 0; j < pieServisiModelList.size(); j++)
                            uk1 += pieServisiModelList.get(j).getIznos().floatValue();

                    }

                    getToc();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PieServisiModel>> call, Throwable t) {

                Log.d("ErrPieServis", t.getMessage().toString());
                Toast.makeText(PieChartOpcenito.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });


    }



    private void getToc() {


        Call<ArrayList<PieGasModel>> getDataRepos = RestClient.getInstance().getApiService().getDataForPieGas(getModel());
        getDataRepos.enqueue(new Callback<ArrayList<PieGasModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieGasModel>> call, Response<ArrayList<PieGasModel>> response) {
                if (response.isSuccessful()) {

                    pieGasModels = response.body();

                    if(!pieGasModels.isEmpty()) {


                        for (int j = 0; j < pieGasModels.size(); j++)
                            uk2 += pieGasModels.get(j).getIznos().floatValue();


                    }

                    getOstalo();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PieGasModel>> call, Throwable t) {
                Log.d("ErrPie2Gas", t.getMessage().toString());
                Toast.makeText(PieChartOpcenito.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void getOstalo() {


        Call<ArrayList<PieOtherModel>> getDataPieRepos = RestClient.getInstance().getApiService().getDataForPieOther(getModel());
        getDataPieRepos.enqueue(new Callback<ArrayList<PieOtherModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PieOtherModel>> call, Response<ArrayList<PieOtherModel>> response) {


                if (response.isSuccessful()) {

                    pieOtherModels = response.body();


                    if(!pieOtherModels.isEmpty()) {

                        for (int j = 0; j < pieOtherModels.size(); j++)
                            uk3 += pieOtherModels.get(j).getIznos().floatValue();

                        float ukupno = uk1+uk2+uk3;
                        entries.add(new PieEntry(((uk1 / ukupno) * 100),getString(R.string.servisi)));
                        entries.add(new PieEntry(((uk2 / ukupno) * 100),getString(R.string.tocenje_goriva)));
                        entries.add(new PieEntry(((uk3 / ukupno) * 100),getString(R.string.ostali_troskovi)));

                        PieDataSet dataSet = new PieDataSet(entries,getString(R.string.troskovi));
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
            public void onFailure(Call<ArrayList<PieOtherModel>> call, Throwable t) {

                Log.d("ErrPieServis", t.getMessage().toString());
                Toast.makeText(PieChartOpcenito.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

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
