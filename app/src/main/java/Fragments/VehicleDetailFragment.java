package Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.Activity.UpdateVehicleActivity;
import com.example.crespo.vehicleexpenses.Activity.VehicleActivity;
import com.example.crespo.vehicleexpenses.R;

import Models.VehicleModel;

/**
 * Klasa fragmenta koji prikazuje detalje o vozilu
 */
public class VehicleDetailFragment extends Fragment {

    private String detail_poz;
    private int menu_icon_id;
    private TextView tv_DetailVehicleName;
    private TextView tv_DetailProducer;
    private TextView tv_DetailRegistracija;
    private TextView tv_DetailSasija;
    private TextView tv_DetailSpremnik;
    private TextView tv_Biljeske;
    private TextView tv_godina;
    private TextView tv_type;


    private FragmentManager fragmentManager;
    private VehicleModel vehicleModel;
    private Boolean zastavica_update;


    public VehicleDetailFragment() {
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
        return inflater.inflate(R.layout.fragment_vehicle_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle_lista");

        if (bundle != null) {

            detail_poz = bundle.getString("pozicija");
            vehicleModel = bundle.getParcelable("ObjToDisplay");
            zastavica_update = bundle.getBoolean("zastavica_update");

        }


        fragmentManager = getActivity().getSupportFragmentManager();


        tv_DetailVehicleName = (TextView) getView().findViewById(R.id.idDetailVehicleName);
        tv_DetailProducer = (TextView) getView().findViewById(R.id.DetailProducer_tv);
        tv_DetailRegistracija = (TextView) getView().findViewById(R.id.DetailRegistracija);
        tv_DetailSasija = (TextView) getView().findViewById(R.id.DetailSasija_tv);
        tv_DetailSpremnik = (TextView) getView().findViewById(R.id.DetailSpremnik_tv);
        tv_Biljeske = (TextView) getView().findViewById(R.id.DetailVehicleBiljeske_tv);
        tv_godina = (TextView) getView().findViewById(R.id.DetailGodina);
        tv_type = (TextView) getView().findViewById(R.id.DetailType_tv);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                    startActivity(new Intent(getActivity(), VehicleActivity.class));
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


    /**
     * Metoda za pokretanje UpdateVehicleActivity activityja, uz postavljanje pripadajućih Bundlea
     */
    private void startUpdateActivity() {

        Intent intent = new Intent(getActivity(), UpdateVehicleActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("ObjektVozila", vehicleModel);
        b.putInt("idButtona", menu_icon_id);
        b.putString("pozicija", detail_poz);
        intent.putExtras(b);
        startActivity(intent);

    }


    /**
     * metoda za pokretanje AlertDialoga
     */
    private void startAlertDialog() {


        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(vehicleModel.getId()));
        bundle_id.putString("Naziv_Klase", "VehicleDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(fragmentManager, "Alert_Delete");
    }

    /**
     * metoda za postavljanje prikaza
     */
    private void postaviPrikaz() {


        tv_DetailVehicleName.setText(vehicleModel.getNazivVozila());
        tv_DetailProducer.setText(vehicleModel.getNazivProizvodaca());
        tv_DetailRegistracija.setText(vehicleModel.getRegistracija());
        tv_DetailSasija.setText(vehicleModel.getBrojSasije());
        tv_DetailSpremnik.setText(vehicleModel.getKapacitetSpremnika1().toString());
        tv_Biljeske.setText(vehicleModel.getBiljeske());
        tv_godina.setText(vehicleModel.getGodina().toString());
        tv_type.setText(vehicleModel.getTip());


    }


}
