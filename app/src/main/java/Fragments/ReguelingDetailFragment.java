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

import com.example.crespo.vehicleexpenses.Activity.RefuelingExpensesActivity;
import com.example.crespo.vehicleexpenses.Activity.UpdateRefuelingExpense;
import com.example.crespo.vehicleexpenses.R;

import java.util.Date;

import Models.TocenjeModel;
import helper.GetSettingValue;

/**
 * Fragment klasa - prikaz detalja Tocenja goriva
 */
public class ReguelingDetailFragment extends Fragment {


    private String detail_poz;
    private int menu_icon_id;
    private TextView tv_ukupni_trosak_detail;
    private TextView tv_kilometraza_detail;
    private TextView tv_Ime_benzinske_detail;
    private TextView tv_datum_detail;
    private TextView tv_naziv_goriva_detail;
    private TextView tv_cijena_po_litri_detail;
    private TextView tv_ukupni_trosak_detail2;
    private TextView tv_uzeto_litara_detail;
    private FragmentManager fragmentManager;
    private TocenjeModel tocenjeModelPrikaz;
    private Boolean zastavica_update;


    public ReguelingDetailFragment() {
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

        return inflater.inflate(R.layout.fragment_tocenje_detail, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fragmentManager = getActivity().getSupportFragmentManager();


        tv_ukupni_trosak_detail = (TextView) getView().findViewById(R.id.tv_ukupni_trosak_detail);
        tv_kilometraza_detail = (TextView) getView().findViewById(R.id.tv_kilometraza_detail);
        tv_Ime_benzinske_detail = (TextView) getView().findViewById(R.id.tv_Ime_benzinske_detail);
        tv_datum_detail = (TextView) getView().findViewById(R.id.tv_datum_detail);
        tv_naziv_goriva_detail = (TextView) getView().findViewById(R.id.tv_naziv_goriva_detail);
        tv_cijena_po_litri_detail = (TextView) getView().findViewById(R.id.tv_cijena_po_litri_detail);
        tv_ukupni_trosak_detail2 = (TextView) getView().findViewById(R.id.tv_ukupni_trosak_detail2);
        tv_uzeto_litara_detail = (TextView) getView().findViewById(R.id.tv_uzeto_litara_detail);


        //Dohvaćanje bundlea iz  TocenjeListaFragmet kako bi otkrili poziciju
        //elementa liste koji je kliknut,sam objekt te zastavicu_update koja nam pomaze
        //kod vraćanja prilikom pritiska tipke Home buttona

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle_lista");

        if (bundle != null) {

            detail_poz = bundle.getString("pozicija");
            tocenjeModelPrikaz = bundle.getParcelable("ObjToDisplay");
            if(tocenjeModelPrikaz != null)
            zastavica_update = bundle.getBoolean("zastavica_update");

        }

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
                    startActivity(new Intent(getActivity(), RefuelingExpensesActivity.class));
                } else NavUtils.navigateUpFromSameTask(getActivity());
                break;
            //menu_icon1 predstavlja ikonu čijim pritiskom dolazimo na Update dio
            case R.id.menu_icon1: {
                menu_icon_id = R.id.menu_icon1;
                startUpdateActivity();
                break;
            }
            //menu_icon2 predstavlja ikonu čijim pritiskom dobimo AlertDialog vezan za brisanje
            case R.id.menu_icon2: {
                menu_icon_id = R.id.menu_icon2;
                startAlertDialog();
                break;
            }

        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * Metoda za pokretanje UpdateRefuelingExpenses activityja, uz postavljanje pripadajućih Bundlea
     */
    private void startUpdateActivity() {

        Intent intent = new Intent(getActivity(), UpdateRefuelingExpense.class);
        Bundle b = new Bundle();
        b.putParcelable("ObjektTocenja", tocenjeModelPrikaz);
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
        bundle_id.putInt("iD", Integer.valueOf(tocenjeModelPrikaz.getId()));
        bundle_id.putString("Naziv_Klase", "TocenjeDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(fragmentManager, "Alert_Delete");
    }


    /**
     * metoda za postavljanje prikaza
     */
    private void postaviPrikaz() {

        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsToc(getContext(),tocenjeModelPrikaz.getTocenjeDatum(),tocenjeModelPrikaz.getTocenjeVrijeme());

        tv_ukupni_trosak_detail.setText(tocenjeModelPrikaz.getUkupniTrosak().toString() + settingValue.getCurrency_format());
        tv_kilometraza_detail.setText(tocenjeModelPrikaz.getKmTrenutno().toString() + settingValue.getUnit_kmOrMile());
        tv_Ime_benzinske_detail.setText(tocenjeModelPrikaz.getBenzinskaNaziv().toString());
        tv_datum_detail.setText(settingValue.getFormatted_date() + "-" + tocenjeModelPrikaz.getTocenjeVrijeme().toString());
        tv_naziv_goriva_detail.setText(tocenjeModelPrikaz.getNazivGoriva().toString());
        tv_cijena_po_litri_detail.setText(tocenjeModelPrikaz.getCijena().toString() + settingValue.getCurrency_format());
        tv_ukupni_trosak_detail2.setText(tocenjeModelPrikaz.getUkupniTrosak().toString() + settingValue.getCurrency_format());
        tv_uzeto_litara_detail.setText(tocenjeModelPrikaz.getUzetoLitara().toString() + settingValue.getUnit_litraOrGalon());


    }





}
