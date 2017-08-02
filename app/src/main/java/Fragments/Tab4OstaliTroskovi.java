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

import com.example.crespo.vehicleexpenses.Activity.Chart.LineCharOtherExp;
import com.example.crespo.vehicleexpenses.Activity.Chart.PieCharOtherExp;
import com.example.crespo.vehicleexpenses.R;

import java.text.DecimalFormat;

import Models.OstaliTroskoviStatistikaModel;
import Models.ParametersModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Klasa fragmenta zadužena za stvari kod treceg tab prikaza ViewPagera
 */
public class Tab4OstaliTroskovi extends Fragment {


    private static final String GRESKA = "ErrStat";
    private String datumOd, datumDo;
    private TextView broj_usluga, ukupni_troskovi, dnevni_troskovi, km_troskovi;
    private String polje_naziva[];
    private Spinner spinnerGrafikoni;

    private OstaliTroskoviStatistikaModel ostaliTroskoviStatistikaModel;
    private TextView km_milja;

    public Tab4OstaliTroskovi() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        polje_naziva = new String[]{getString(R.string.odaberite_grafikon), getString(R.string.grafikon_udjela_troskova), getString(R.string.grafikon_kretanja_troskova)};
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //vozilo_id = ReadVoziloId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //dohvat datuma između kojih se radi statistika
        Bundle bundle = getArguments();
        datumOd = bundle.getString("DatumOd");
        datumDo = bundle.getString("DatumDo");

        return inflater.inflate(R.layout.fragment_tab4_ostali_troskovi, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        broj_usluga = (TextView) getActivity().findViewById(R.id.brojOt);
        ukupni_troskovi = (TextView) getActivity().findViewById(R.id.textViewUkupnoOt);
        dnevni_troskovi = (TextView) getActivity().findViewById(R.id.textViewDnevnoOt);
        km_troskovi = (TextView) getActivity().findViewById(R.id.textViewkmOt);
        spinnerGrafikoni = (Spinner) getActivity().findViewById(R.id.spinner_grafikon_ot);
        km_milja = (TextView) getActivity().findViewById(R.id.textViewKm_Milja2);


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

                        //inace pokreni novu aktivnost s grafikonom

                        Bundle bundle_datumi2 = new Bundle();
                        bundle_datumi2.putString("DatumOd", datumOd);
                        bundle_datumi2.putString("DatumDo", datumDo);

                        Intent intent = new Intent(getActivity(), LineCharOtherExp.class);
                        intent.putExtra("Datumi", bundle_datumi2);

                        spinnerGrafikoni.setSelection(0);

                        startActivity(intent);


                        break;
                    case "Grafikon udjela troškova":

                        Bundle bundle_datumi = new Bundle();
                        bundle_datumi.putString("DatumOd", datumOd);
                        bundle_datumi.putString("DatumDo", datumDo);

                        Intent intent2 = new Intent(getActivity(), PieCharOtherExp.class);
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
        getStatistikaOstalihTroskova(datumOd, datumDo, manager.getVehicleId());
    }



    /**
     * Metoda za dohvat informacija vezanih uz ostale troskove vozila
     *
     * @param date1     - datum od kojeg se gleda
     * @param date2     - datum do kojeg se gleda
     * @param vozilo_id - id vozila
     */
    private void getStatistikaOstalihTroskova(String date1, String date2, int vozilo_id) {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());
        ParametersModel s = new ParametersModel();
        s.setDateOd(date1);
        s.setDateDo(date2);
        s.setVozilo_id(manager.getVehicleId());


        final Call<OstaliTroskoviStatistikaModel> getStatOstaliTroskoviCall = RestClient.getInstance().getApiService().getOtherStatistic(s);
        getStatOstaliTroskoviCall.enqueue(new Callback<OstaliTroskoviStatistikaModel>() {
            @Override
            public void onResponse(Call<OstaliTroskoviStatistikaModel> call, Response<OstaliTroskoviStatistikaModel> response) {

                if (response.isSuccessful()) {


                    GetSettingValue settingValue = new GetSettingValue();
                    settingValue.readSettingsDetail(getContext());




                    ostaliTroskoviStatistikaModel = response.body();

                    DecimalFormat precision = new DecimalFormat("0.000");

                    broj_usluga.setText(ostaliTroskoviStatistikaModel.getBrojOstalihTroskova() + " " + getString(R.string.ostalih_troskova_u) + ostaliTroskoviStatistikaModel.getBrojDana() + getString(R.string._dana));
                    ukupni_troskovi.setText(precision.format(ostaliTroskoviStatistikaModel.getUkupniTrosak()) + settingValue.getCurrency_format());
                    dnevni_troskovi.setText(precision.format(ostaliTroskoviStatistikaModel.getDnevniTroskovi()) + settingValue.getCurrency_format());
                    km_troskovi.setText(precision.format(ostaliTroskoviStatistikaModel.getTroskoviPoKm()) + settingValue.getCurrency_format());

                }

            }

            @Override
            public void onFailure(Call<OstaliTroskoviStatistikaModel> call, Throwable t) {

                Log.d(GRESKA, t.getMessage().toString());
                Toast.makeText(getActivity(), R.string.poruka_greske_dohvata, Toast.LENGTH_SHORT).show();


            }
        });


    }



}