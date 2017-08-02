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
import Retrofit.api.client.RestClient;
import helper.ConfigurationWrapper;
import helper.SharedpreferencesKeys;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineChartGas extends AppCompatActivity {


    private String datumOd, datumDo;
    private Toolbar toolbar;
    private LineChart linechart;
    private ArrayList<LineGasModel> lineGasModels;
    private ArrayList<Entry> entries = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_gas);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary5));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.grafikon_kretanja_tocenja);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark5));
        }


        Bundle bundle = getIntent().getBundleExtra("Datumi");
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);
        ParametersModel parametersModel = new ParametersModel();
        parametersModel.setVozilo_id(manager.getVehicleId());
        parametersModel.setId_servisi(1);
        parametersModel.setDateOd(datumOd);
        parametersModel.setDateDo(datumDo);


        linechart = (LineChart) findViewById(R.id.chartLineGas);


        Call<ArrayList<LineGasModel>> getDataRepos = RestClient.getInstance().getApiService().getDataForLineGas(parametersModel);
        getDataRepos.enqueue(new Callback<ArrayList<LineGasModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LineGasModel>> call, Response<ArrayList<LineGasModel>> response) {
                if (response.isSuccessful()) {

                    lineGasModels = response.body();


                    if (!lineGasModels.isEmpty()) {
                        for (int i = 0; i < lineGasModels.size(); i++) {


                            entries.add(new Entry(lineGasModels.get(i).getDatum().getTime(), lineGasModels.get(i).getIznos().floatValue()));


                        }


                        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.kretanje_tocenja));
                        LineData lineData = new LineData(dataSet);

                        XAxis xAxis = linechart.getXAxis();
                        xAxis.setLabelRotationAngle(-45);
                        xAxis.setGranularity(1f);
                        xAxis.disableGridDashedLine();
                        xAxis.setValueFormatter(new LineChartGas.DateValueFormatter());

                        linechart.setData(lineData);
                        Description des = new Description();
                        des.setText("");
                        linechart.setDescription(des);
                        MyMarkerView myMarkerView = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
                        linechart.setMarkerView(myMarkerView);


                        linechart.invalidate();
                        linechart.notifyDataSetChanged();


                    }else{

                        linechart.invalidate();
                        linechart.setNoDataText(getString(R.string.grafikon_prazan));
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<LineGasModel>> call, Throwable t) {


                Log.d("ErrLineServis", t.getMessage().toString());
                Toast.makeText(LineChartGas.this, R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            onBackPressed();


        return super.onOptionsItemSelected(item);
    }

    private class DateValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // Simple version. You should use a DateFormatter to specify how you want to textually represent your date.
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
