package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.Activity.Chart.LineChartService;
import com.example.crespo.vehicleexpenses.Activity.Chart.PieChartService;
import com.example.crespo.vehicleexpenses.R;

import java.text.DecimalFormat;

import Models.ParametersModel;
import Models.ServiceStatistikaModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa fragmenta zadužena za stvari kod treceg tab prikaza ViewPagera
 */

public class Tab3Servisi extends Fragment {

    private static final String GRESKA = "ErrStat";
    public ServiceStatistikaModel serviceStatistikaModel;
    private String datumOd, datumDo;
    private TextView broj_usluga, ukupni_troskovi, dnevni_troskovi, km_troskovi;
    private String polje_naziva[];
    private Spinner spinnerGrafikoni;
    private TextView km_milja;


    public Tab3Servisi() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        polje_naziva = new String[]{getString(R.string.odaberite_grafikon), getString(R.string.grafikon_kretanja_troskova), getString(R.string.grafikon_udjela_troskova)};
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //dohvat datuma između kojih se radi statistika
        Bundle bundle = getArguments();
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");

        return inflater.inflate(R.layout.fragment_tab3_servisi, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        broj_usluga = (TextView) getActivity().findViewById(R.id.brojkreso);
        ukupni_troskovi = (TextView) getActivity().findViewById(R.id.textViewUkupnoS);
        dnevni_troskovi = (TextView) getActivity().findViewById(R.id.textViewDnevnoS);
        km_troskovi = (TextView) getActivity().findViewById(R.id.textViewkmS);
        spinnerGrafikoni = (Spinner) getActivity().findViewById(R.id.spinner_grafikon_s);
        km_milja = (TextView) getActivity().findViewById(R.id.textViewKm_Milja);


        //postavljanje adaptera za spinner u kojem se odabire tip grafikona
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, polje_naziva);
        spinnerGrafikoni.setAdapter(adapter2);
        spinnerGrafikoni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String ime = adapterView.getItemAtPosition(i).toString();

                switch (ime) {

                    case "Odaberite grafikon":
                        break;
                    case "Grafikon kretanja troškova":
                        Toast.makeText(getActivity(), "Grafikon kretanja troskova", Toast.LENGTH_SHORT).show();
                        //inace pokreni novu aktivnost s grafikonom

                        Bundle bundle_datumi2 = new Bundle();
                        bundle_datumi2.putString("DatumOd", datumOd);
                        bundle_datumi2.putString("DatumDo", datumDo);

                        Intent intent = new Intent(getActivity(), LineChartService.class);
                        intent.putExtra("Datumi", bundle_datumi2);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent);
                        break;


                    case "Grafikon udjela troškova":


                        Bundle bundle_datumi = new Bundle();
                        bundle_datumi.putString("DatumOd", datumOd);
                        bundle_datumi.putString("DatumDo", datumDo);

                        Intent intent2 = new Intent(getActivity(), PieChartService.class);
                        intent2.putExtra("Datumi", bundle_datumi);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent2);


                        break;
                    default:

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //dohvat vozila id iz SharedPreferences
        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
        getStatistikaServisa(datumOd, datumDo,  manager.getVehicleId());


    }



    /**
     * Metoda za dohvat informacija vezanih uz servise vozila
     *
     * @param date1     - datum od kojeg se gleda
     * @param date2     - datum do kojeg se gleda
     * @param vozilo_id - id vozila
     */
    private void getStatistikaServisa(String date1, String date2, int vozilo_id) {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
        ParametersModel s = new ParametersModel();
        s.setDateOd(date1);
        s.setDateDo(date2);
        s.setVozilo_id(manager.getVehicleId());

        final Call<ServiceStatistikaModel> getStatServisiCall = RestClient.getInstance().getApiService().getServiceStatistic(s);

        getStatServisiCall.enqueue(new Callback<ServiceStatistikaModel>() {
            @Override
            public void onResponse(Call<ServiceStatistikaModel> call, Response<ServiceStatistikaModel> response) {

                if (response.isSuccessful()) {


                    GetSettingValue settingValue = new GetSettingValue();
                    settingValue.readSettingsDetail(getContext());

                    km_milja.setText(getString(R.string.po_, settingValue.getUnit_kmOrMile()));

                    serviceStatistikaModel = response.body();

                    DecimalFormat precision = new DecimalFormat("0.000");

                    broj_usluga.setText(serviceStatistikaModel.getBrojServisa() + " " + getString(R.string.servisa_u) + " " + serviceStatistikaModel.getBrojDana() + getString(R.string._dana));
                    ukupni_troskovi.setText(precision.format(serviceStatistikaModel.getUkupniTrosak()) + settingValue.getCurrency_format());
                    dnevni_troskovi.setText(precision.format(serviceStatistikaModel.getDnevniTroskovi()) + settingValue.getCurrency_format());
                    km_troskovi.setText(precision.format(serviceStatistikaModel.getTroskoviPoKm()) + settingValue.getCurrency_format());

                }
            }

            @Override
            public void onFailure(Call<ServiceStatistikaModel> call, Throwable t) {
                Log.d(GRESKA, t.getMessage().toString());
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });


    }




}
