package Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.Activity.ServiceExpencesActivity;
import com.example.crespo.vehicleexpenses.Activity.UpdateServiceExpense;
import com.example.crespo.vehicleexpenses.R;

import Adapters.ServicesDetailAdapter;
import Models.ServiceModel;
import helper.GetSettingValue;

/**
 * Fragment identičan OtherExpensesDetailFragment -  pogledaj detalje u njemu, samo je ovaj
 * namjenjen za detalje servisa
 */
public class ServisiDetailFragment extends Fragment {

    private String detail_poz;
    private int menu_icon_id;
    private TextView tv_DetailDate;
    private TextView tv_DetailTime;
    private TextView tv_DetailKm;
    private TextView tv_DetailTrosak;
    private TextView tv_NazivObrtnika;
    private TextView tv_Biljeske;
    private TextView prijedeno;

    private FragmentManager fragmentManager;
    private ServiceModel serviceModelPrikaz;
    private Boolean zastavica_update;

    private RecyclerView recyclerView;
    private ServicesDetailAdapter servicesDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private GetSettingValue getSettingValue;


    public ServisiDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_servisi_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle_lista");

        if (bundle != null) {

            detail_poz = bundle.getString("pozicija");
            serviceModelPrikaz = bundle.getParcelable("ObjToDisplay");
            zastavica_update = bundle.getBoolean("zastavica_update");

        }


        fragmentManager = getActivity().getSupportFragmentManager();

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerDetailServisi);
        servicesDetailAdapter = new ServicesDetailAdapter(getContext(), serviceModelPrikaz);
        recyclerView.setAdapter(servicesDetailAdapter);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        tv_DetailDate = (TextView) getView().findViewById(R.id.idDetailDate);
        tv_DetailTime = (TextView) getView().findViewById(R.id.idDetailTime);
        tv_DetailKm = (TextView) getView().findViewById(R.id.DetailKm_tv);
        tv_DetailTrosak = (TextView) getView().findViewById(R.id.DetailTrosak_tv);
        tv_NazivObrtnika = (TextView) getView().findViewById(R.id.DetailNazivObrtika_tv);
        tv_Biljeske = (TextView) getView().findViewById(R.id.DetailBiljeske_tv);
        prijedeno = (TextView) getView().findViewById(R.id.stanje_prijedeno);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getSettingValue = new GetSettingValue();
        postaviPrikaz();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(0).setIcon(R.drawable.ic_action_edit);
        menu.getItem(1).setIcon(R.drawable.ic_action_delete).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (zastavica_update == true) {
                    startActivity(new Intent(getActivity(), ServiceExpencesActivity.class));
                } else NavUtils.navigateUpFromSameTask(getActivity());
                break;
            case R.id.menu_icon1: {
                menu_icon_id = R.id.menu_icon1;
                startUpdateActivity();
                break;
            }
            case R.id.menu_icon2: {
                menu_icon_id = R.id.menu_icon2;
                startAlertDialog();
                break;
            }

        }


        return super.onOptionsItemSelected(item);
    }


    private void startUpdateActivity() {

        Intent intent = new Intent(getActivity(), UpdateServiceExpense.class);
        Bundle b = new Bundle();
        b.putParcelable("ObjektServisa", serviceModelPrikaz);
        b.putInt("idButtona", menu_icon_id);
        b.putString("pozicija", detail_poz);
        intent.putExtras(b);
        startActivity(intent);

    }


    private void startAlertDialog() {


        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(serviceModelPrikaz.getId()));
        bundle_id.putString("Naziv_Klase", "ServisiDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(fragmentManager, "Alert_Delete");
    }



    private void postaviPrikaz() {


        getSettingValue.readSettings(getContext(),serviceModelPrikaz.getDatumServis(),serviceModelPrikaz.getVrijemeServis());

        Double ukupno = 0.0;

        for (int i = 0; i < serviceModelPrikaz.getTros().size(); i++) {

            ukupno += Double.valueOf(serviceModelPrikaz.getTros().get(i).getIznos());

        }

        tv_DetailDate.setText(getSettingValue.getFormatted_date());
        tv_DetailTime.setText(" / " + getSettingValue.getVrijeme());
        tv_DetailKm.setText(serviceModelPrikaz.getKmTrenutno() + getSettingValue.getUnit_kmOrMile());
        tv_DetailTrosak.setText(String.valueOf(ukupno) + getSettingValue.getCurrency_format());
        tv_NazivObrtnika.setText(serviceModelPrikaz.getNazivObrtnika());
        tv_Biljeske.setText(serviceModelPrikaz.getBiljeske());


    }


}
