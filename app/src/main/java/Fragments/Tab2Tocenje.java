package Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.crespo.vehicleexpenses.Activity.Chart.Line2ChartGas;
import com.example.crespo.vehicleexpenses.Activity.Chart.LineChartGas;
import com.example.crespo.vehicleexpenses.Activity.Chart.Pie2ChartGas;
import com.example.crespo.vehicleexpenses.Activity.Chart.Pie4ChartGas;
import com.example.crespo.vehicleexpenses.R;

import java.text.DecimalFormat;

import Models.ParametersModel;
import Models.TocenjeStatistikaModel;
import Models.VehicleProducerModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa fragmenta zadužena za stvari kod drugog tab prikaza ViewPagera
 */
public class Tab2Tocenje extends Fragment {

    private static final String GRESKA = "ErrStat";
    SharedPreferences preferences;
    private String datumOd, datumDo;
    private TextView broj_usluga, ukupni_troskovi, dnevni_troskovi, km_troskovi, uzetoLitara, pros_po_km, pros_100;
    private String polje_naziva[];
    private Spinner spinnerGrafikoni;
    private TocenjeStatistikaModel tocenjeStatistikaModel;

    private TextView litara_tv, potrosnja_tv, potrosnja100_tv, po_km;


    public Tab2Tocenje() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        polje_naziva = new String[]{getString(R.string.odaberite_grafikon), getString(R.string.grafikon_kretanja_troskova), getString(R.string.Grafikon_kretanja_cijen_goriva), getString(R.string.grafikon_udio_benzinske), getString(R.string.grafikon_udjela_razloga)};
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

        return inflater.inflate(R.layout.fragment_tab2_tocenje, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        broj_usluga = (TextView) getActivity().findViewById(R.id.brojTocenjaS);
        ukupni_troskovi = (TextView) getActivity().findViewById(R.id.textViewUkupnoT);
        dnevni_troskovi = (TextView) getActivity().findViewById(R.id.textViewDnevnoT);
        km_troskovi = (TextView) getActivity().findViewById(R.id.textViewkmT);
        uzetoLitara = (TextView) getActivity().findViewById(R.id.textViewUkupnoLit);
        pros_po_km = (TextView) getActivity().findViewById(R.id.textViewProskm);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        potrosnja_tv = (TextView) getActivity().findViewById(R.id.textViewTocenjePotrosnja);
        litara_tv = (TextView) getActivity().findViewById(R.id.textViewTocenjeLitara);
        potrosnja100_tv = (TextView) getActivity().findViewById(R.id.textViewTocenjePotrosnja100);
        po_km = (TextView) getActivity().findViewById(R.id.textViewTocenjekm);


        pros_100 = (TextView) getActivity().findViewById(R.id.textViewPros100km);
        spinnerGrafikoni = (Spinner) getActivity().findViewById(R.id.spinner_grafikon_novi);


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

                        Bundle bundle_datumi2 = new Bundle();
                        bundle_datumi2.putString("DatumOd", datumOd);
                        bundle_datumi2.putString("DatumDo", datumDo);

                        Intent intent = new Intent(getActivity(), LineChartGas.class);
                        intent.putExtra("Datumi", bundle_datumi2);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent);

                        break;


                    case "Grafikon kretanja cijene goriva":


                        Bundle bundle_datumi3 = new Bundle();
                        bundle_datumi3.putString("DatumOd", datumOd);
                        bundle_datumi3.putString("DatumDo", datumDo);


                        Intent intent3 = new Intent(getActivity(), Line2ChartGas.class);
                        intent3.putExtra("Datumi", bundle_datumi3);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent3);


                        break;

                    case "Grafikon udjela benzinske":


                        Bundle bundle_datumi4 = new Bundle();
                        bundle_datumi4.putString("DatumOd", datumOd);
                        bundle_datumi4.putString("DatumDo", datumDo);

                        Intent intent4 = new Intent(getActivity(), Pie2ChartGas.class);
                        intent4.putExtra("Datumi", bundle_datumi4);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent4);


                        break;

                    case "Grafikon udjela razloga":


                        Bundle bundle_datumi5 = new Bundle();
                        bundle_datumi5.putString("DatumOd", datumOd);
                        bundle_datumi5.putString("DatumDo", datumDo);


                        Intent intent5 = new Intent(getActivity(), Pie4ChartGas.class);
                        intent5.putExtra("Datumi", bundle_datumi5);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent5);


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
        getStatistikaTocenja(datumOd, datumDo, manager.getVehicleId());

    }





    /**
     * Metoda za dohvat informacija vezanih uz točenja na benzinskim postajama
     *
     * @param date1     - datum od kojeg se gleda
     * @param date2     - datum do kojeg se gleda
     * @param vozilo_id - id vozila
     */
    private void getStatistikaTocenja(String date1, String date2, int vozilo_id) {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        ParametersModel s = new ParametersModel();
        s.setDateOd(date1);
        s.setDateDo(date2);
        s.setVozilo_id(manager.getVehicleId());

        Call<TocenjeStatistikaModel> getStatRepos = RestClient.getInstance().getApiService().getTocenjeStatistic(s);
        getStatRepos.enqueue(new Callback<TocenjeStatistikaModel>() {
            @Override
            public void onResponse(Call<TocenjeStatistikaModel> call, Response<TocenjeStatistikaModel> response) {
                if (response.isSuccessful()) {

                    tocenjeStatistikaModel = response.body();

                    DecimalFormat precision = new DecimalFormat("0.000");

                    GetSettingValue settingValue = new GetSettingValue();
                    settingValue.readSettingsUpdate(getContext());

                   // po_km.setText(getString(R.string.po_, unit_kmOrMile));
                    potrosnja_tv.setText(getString(R.string.potrosnja) + settingValue.getUnit_kmOrMile());
                    potrosnja100_tv.setText(getString(R.string.potrosnja_100) + settingValue.getUnit_kmOrMile());
                    String naziv_pogona = tocenjeStatistikaModel.getNazivGoriva();
                    if (naziv_pogona.equals("Elektricni")) {

                        broj_usluga.setText(tocenjeStatistikaModel.getBrojTocenja() + " " + getString(R.string.punjenja_u) + " " + tocenjeStatistikaModel.getBrojDana() + getString(R.string._dana));
                        ukupni_troskovi.setText(precision.format(tocenjeStatistikaModel.getUkupniTrosak()) + settingValue.getCurrency_format());
                        dnevni_troskovi.setText(precision.format(tocenjeStatistikaModel.getDnevniTroskovi()) + settingValue.getCurrency_format());
                        km_troskovi.setText(precision.format(tocenjeStatistikaModel.getTroskoviPoKm()) + settingValue.getCurrency_format());
                        uzetoLitara.setText(tocenjeStatistikaModel.getUzetoLitara().toString() + " " + "kwh");
                        pros_100.setText(precision.format(tocenjeStatistikaModel.getProsPo100km()).toString() + "kwh" + "/100" + settingValue.getUnit_kmOrMile());
                        pros_po_km.setText(precision.format(tocenjeStatistikaModel.getProsPoKm().toString()) + "kwh" + " /" + settingValue.getUnit_kmOrMile());


                    } else if (naziv_pogona.equals("Plin")) {

                        broj_usluga.setText(tocenjeStatistikaModel.getBrojTocenja() + " " + getString(R.string.tocenja_u) + tocenjeStatistikaModel.getBrojDana() + getString(R.string._dana));
                        ukupni_troskovi.setText(precision.format(tocenjeStatistikaModel.getUkupniTrosak()) + settingValue.getCurrency_format());
                        dnevni_troskovi.setText(precision.format(tocenjeStatistikaModel.getDnevniTroskovi()) + settingValue.getCurrency_format());
                        km_troskovi.setText(precision.format(tocenjeStatistikaModel.getTroskoviPoKm()) + settingValue.getCurrency_format());
                        uzetoLitara.setText(tocenjeStatistikaModel.getUzetoLitara().toString() + " " + "m3");
                        pros_100.setText(tocenjeStatistikaModel.getProsPo100km().toString() + "m3" + "/100" + settingValue.getUnit_kmOrMile());
                        pros_po_km.setText(tocenjeStatistikaModel.getProsPoKm().toString() + "m3" + " /" + settingValue.getUnit_kmOrMile());

                    } else {

                        broj_usluga.setText(tocenjeStatistikaModel.getBrojTocenja() + " " + getString(R.string.tocenja_u) + tocenjeStatistikaModel.getBrojDana() + getString(R.string._dana));
                        ukupni_troskovi.setText(precision.format(tocenjeStatistikaModel.getUkupniTrosak()) + settingValue.getCurrency_format());
                        dnevni_troskovi.setText(precision.format(tocenjeStatistikaModel.getDnevniTroskovi()) + settingValue.getCurrency_format());
                        km_troskovi.setText(precision.format(tocenjeStatistikaModel.getTroskoviPoKm()) + settingValue.getCurrency_format());
                        uzetoLitara.setText(tocenjeStatistikaModel.getUzetoLitara().toString() + " " + settingValue.getUnit_litraOrGalon());
                        pros_100.setText(precision.format(tocenjeStatistikaModel.getProsPo100km()).toString() + " " + settingValue.getUnit_litraOrGalon() + "/100 " + settingValue.getUnit_kmOrMile());
                        pros_po_km.setText(precision.format(tocenjeStatistikaModel.getProsPoKm()).toString() + " " + settingValue.getUnit_litraOrGalon() + " /" + settingValue.getUnit_kmOrMile());
                    }
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
