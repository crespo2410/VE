package Fragments;


import android.content.Context;
import android.content.Intent;
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

import com.example.crespo.vehicleexpenses.Activity.Chart.LineChartOpcenito;
import com.example.crespo.vehicleexpenses.Activity.Chart.PieChartOpcenito;
import com.example.crespo.vehicleexpenses.R;

import java.text.DecimalFormat;

import Models.OstaliTroskoviStatistikaModel;
import Models.ParametersModel;
import Models.ServiceStatistikaModel;
import Models.TocenjeStatistikaModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa fragmenta zadužena za stvari kod prvog tab prikaza ViewPagera
 */
public class Tab1Opcenito extends Fragment {

    private static final String GRESKA = "ErrStat";
    public ServiceStatistikaModel serviceStatistikaModel;
    private String datumOd, datumDo;
    private TextView broj_usluga, ukupni_troskovi, dnevni_troskovi, km_troskovi, km_milja;
    private String polje_naziva[];
    private Spinner spinnerGrafikoni;
    private OstaliTroskoviStatistikaModel ostaliTroskoviStatistikaModel;
    private Double bundle_ukupno = 0., bundle_pojedinacno_s = 0., bundle_pojedinacno_t = 0., bundle_pojedinacno_ot = 0.;
    private TocenjeStatistikaModel tocenjeStatistikaModel;



    public Tab1Opcenito() {
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
        // Inflate the layout for this fragment

        //dohvat datuma između kojih se radi statistika
        Bundle bundle = getArguments();
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");


        View v = inflater.inflate(R.layout.fragment_tab1_opcenito, container, false);
        return v;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        broj_usluga = (TextView) getActivity().findViewById(R.id.brojUkupnihUsluga);
        ukupni_troskovi = (TextView) getActivity().findViewById(R.id.textViewUkupnoOpcenito);
        dnevni_troskovi = (TextView) getActivity().findViewById(R.id.textViewDnevnoOpcenito);
        km_troskovi = (TextView) getActivity().findViewById(R.id.textViewkmOpcenito);
        km_milja = (TextView) getActivity().findViewById(R.id.textViewKm_Milja);
        spinnerGrafikoni = (Spinner) getActivity().findViewById(R.id.spinner_grafikon_opcenito);


        //postavljanje adaptera za spinner u kojem se odabire tip grafikona
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, polje_naziva);
        spinnerGrafikoni.setAdapter(adapter2);

        spinnerGrafikoni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String ime = adapterView.getItemAtPosition(i).toString();


        //s obzirom na odabir grafikona u spinneru, pokretanje aktivnosti
                switch (ime) {

                    case "Odaberite grafikon":
                        break;
                    case "Grafikon kretanja troškova":

                        Bundle bundle_datumi2 = new Bundle();
                        bundle_datumi2.putString("DatumOd", datumOd);
                        bundle_datumi2.putString("DatumDo", datumDo);


                        Intent intent = new Intent(getActivity(), LineChartOpcenito.class);
                        intent.putExtra("Datumi", bundle_datumi2);

                        spinnerGrafikoni.setSelection(0);
                        startActivity(intent);
                        break;

                    case "Grafikon udjela troškova":

                        Bundle bundle_datumi = new Bundle();
                        bundle_datumi.putString("DatumOd", datumOd);
                        bundle_datumi.putString("DatumDo", datumDo);
                        bundle_datumi.putDouble("Ukupno", bundle_ukupno);
                        bundle_datumi.putDouble("UkupnoTocenja", bundle_pojedinacno_t);
                        bundle_datumi.putDouble("UkupnoServisi", bundle_pojedinacno_s);
                        bundle_datumi.putDouble("UkupnoOstali", bundle_pojedinacno_ot);

                        Intent intent2 = new Intent(getActivity(), PieChartOpcenito.class);
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


        getStatistikaOpcenito();

    }



    /**
     * Metoda za stvaranje modela za potrebne parametre
     *
     * @return - vrača objekt ParametersModel
     */
    private ParametersModel getModel() {
        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        ParametersModel s = new ParametersModel();
        s.setDateOd(datumOd);
        s.setDateDo(datumDo);
        s.setVozilo_id(manager.getVehicleId());

        return s;
    }


    /**
     * Funkcija za dohvat svih podataka od baze sa servera
     */
    private void getStatistikaOpcenito() {


        final Call<ServiceStatistikaModel> getStatServisiCall = RestClient.getInstance().getApiService().getServiceStatistic(getModel());

        getStatServisiCall.enqueue(new Callback<ServiceStatistikaModel>() {
            @Override
            public void onResponse(Call<ServiceStatistikaModel> call, Response<ServiceStatistikaModel> response) {

                if (response.isSuccessful()) {

                    serviceStatistikaModel = response.body();
                    getOstaliData();

                }
            }

            @Override
            public void onFailure(Call<ServiceStatistikaModel> call, Throwable t) {
                Log.d(GRESKA, t.getMessage());
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


            }
        });


    }


    /**
     * Metoda za dohvat ostalij podataka, prvo ostalih troškova, a zatim poziv za tocenja
     */
    private void getOstaliData() {

        final Call<OstaliTroskoviStatistikaModel> getStatOstaliTroskoviCall = RestClient.getInstance().getApiService().getOtherStatistic(getModel());
        getStatOstaliTroskoviCall.enqueue(new Callback<OstaliTroskoviStatistikaModel>() {
            @Override
            public void onResponse(Call<OstaliTroskoviStatistikaModel> call, Response<OstaliTroskoviStatistikaModel> response) {

                if (response.isSuccessful()) {

                    ostaliTroskoviStatistikaModel = response.body();
                    getTocenjeData();
                }

            }

            @Override
            public void onFailure(Call<OstaliTroskoviStatistikaModel> call, Throwable t) {

                Log.d(GRESKA, t.getMessage().toString());
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


            }
        });


    }


    /**
     * Metoda za dohvat informacija vezanih uz točenja na benzinskim postajama
     */
    private void getTocenjeData() {

        Call<TocenjeStatistikaModel> getStatTocenjeCall = RestClient.getInstance().getApiService().getTocenjeStatistic(getModel());
        getStatTocenjeCall.enqueue(new Callback<TocenjeStatistikaModel>() {
            @Override
            public void onResponse(Call<TocenjeStatistikaModel> call, Response<TocenjeStatistikaModel> response) {
                if (response.isSuccessful()) {

                    tocenjeStatistikaModel = response.body();

                    DecimalFormat precision = new DecimalFormat("0.000");

                    GetSettingValue settingValue = new GetSettingValue();
                    settingValue.readSettingsDetail(getContext());



                    int broj_usluga_uk = Integer.valueOf(tocenjeStatistikaModel.getBrojTocenja()) + Integer.valueOf(ostaliTroskoviStatistikaModel.getBrojOstalihTroskova()) + Integer.valueOf(serviceStatistikaModel.getBrojServisa());
                    Double ukupni_tro = tocenjeStatistikaModel.getUkupniTrosak() + ostaliTroskoviStatistikaModel.getUkupniTrosak() + serviceStatistikaModel.getUkupniTrosak();
                    Double ukupni_tro_dne = tocenjeStatistikaModel.getDnevniTroskovi() + ostaliTroskoviStatistikaModel.getDnevniTroskovi() + serviceStatistikaModel.getDnevniTroskovi();
                    Double ukupni_tro_km = tocenjeStatistikaModel.getTroskoviPoKm() + ostaliTroskoviStatistikaModel.getTroskoviPoKm() + serviceStatistikaModel.getTroskoviPoKm();


                    broj_usluga.setText(broj_usluga_uk + " " + getString(R.string._usluga_u) + " " + tocenjeStatistikaModel.getBrojDana() + getString(R.string._dana));
                    ukupni_troskovi.setText(precision.format(ukupni_tro) + settingValue.getCurrency_format());
                    dnevni_troskovi.setText(precision.format(ukupni_tro_dne) + settingValue.getCurrency_format());
                    km_troskovi.setText(precision.format(ukupni_tro_km) + settingValue.getCurrency_format());

                    bundle_ukupno = ukupni_tro;
                    bundle_pojedinacno_t = tocenjeStatistikaModel.getUkupniTrosak();
                    bundle_pojedinacno_s = serviceStatistikaModel.getUkupniTrosak();
                    bundle_pojedinacno_ot = ostaliTroskoviStatistikaModel.getUkupniTrosak();

                }
            }

            @Override
            public void onFailure(Call<TocenjeStatistikaModel> call, Throwable t) {

                Log.d(GRESKA, t.getMessage().toString());
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();

            }
        });

    }




}

