package Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.crespo.vehicleexpenses.Activity.OtherExpensesActivity;
import com.example.crespo.vehicleexpenses.Activity.UpdateOtherExpenses;
import com.example.crespo.vehicleexpenses.R;

import java.util.Date;

import Adapters.OtherExpensesDetailAdapter;
import Models.OtherExpensesModel;
import helper.GetSettingValue;

/**
 * Fragment klasa - prikaz detalja Ostalih troskova
 */

public class OtherExpensesDetailFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

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
    private OtherExpensesModel otherExpensesModel;
    private Boolean zastavica_update;

    private RecyclerView recyclerView;
    private OtherExpensesDetailAdapter otherExpensesDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public OtherExpensesDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_expenses_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Dohvaćanje bundlea iz OtherExpensesListaFragmet kako pi otkrili poziciju
        //elementa liste koji je kliknut,sam objekt te zastavicu_update koja nam pomaze
        //kod vraćanja prilikom pritiska tipke Home buttona

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle_lista");

        if (bundle != null) {

            detail_poz = bundle.getString("pozicija");
            otherExpensesModel = bundle.getParcelable("ObjToDisplay");
            zastavica_update = bundle.getBoolean("zastavica_update");

        }


        //postavljanje adaptera i layoutManagera za RecyclerView
        fragmentManager = getActivity().getSupportFragmentManager();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerDetailOstaliTroskovi);
        otherExpensesDetailAdapter = new OtherExpensesDetailAdapter(getContext(), otherExpensesModel);
        recyclerView.setAdapter(otherExpensesDetailAdapter);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        //referenciranje na bitne UI elemente
        tv_DetailDate = (TextView) getView().findViewById(R.id.idDetailOtherExpDate);
        tv_DetailTime = (TextView) getView().findViewById(R.id.idDetailOtherExpTime);
        tv_DetailKm = (TextView) getView().findViewById(R.id.DetailOtherExpKm_tv);
        tv_DetailTrosak = (TextView) getView().findViewById(R.id.DetailOtherExpTrosak_tv);
        tv_NazivObrtnika = (TextView) getView().findViewById(R.id.DetailOtherExpNazivObrtika_tv);
        tv_Biljeske = (TextView) getView().findViewById(R.id.DetailOtheExpBiljeske_tv);
        prijedeno = (TextView) getView().findViewById(R.id.stanje_prijedeno);


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
                    startActivity(new Intent(getActivity(), OtherExpensesActivity.class));
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
     * Metoda za pokretanje UpdateOtherExpenses activityja, uz postavljanje pripadajućih Bundlea
     */
    private void startUpdateActivity() {

        //pokreni aktivnost updatea, ali i pošalji podatke koji moraju biti prikazani
        Intent intent = new Intent(getActivity(), UpdateOtherExpenses.class);
        Bundle b = new Bundle();
        b.putParcelable("ObjektOtherExpenses", otherExpensesModel);
        b.putInt("idButtona", menu_icon_id);
        b.putString("pozicija", detail_poz);
        intent.putExtras(b);
        startActivity(intent);

    }


    /**
     * metoda za pokretanje AlertDialoga
     */
    private void startAlertDialog() {

        //slanje id-a i naziva klase kako bi se u AlertDialogFragment klasi znalo što treba obrisati
        Bundle bundle_id = new Bundle();
        bundle_id.putInt("iD", Integer.valueOf(otherExpensesModel.getIdOt()));
        bundle_id.putString("Naziv_Klase", "OstaliTroskoviDetailClass");


        AlertDeleteFragment alertDeleteFragment = new AlertDeleteFragment();
        alertDeleteFragment.setArguments(bundle_id);
        alertDeleteFragment.show(fragmentManager, "Alert_Delete");
    }


    /**
     * metoda za postavljanje prikaza Detalja, iz dohvacenog objekta otherExpensesModel i dohvata postavki
     */
    private void postaviPrikaz() {


        //dohvati željene postavke (km, datuma i slično)
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettings(getContext(),otherExpensesModel.getDatumTroska(),otherExpensesModel.getVrijemeTroska());

        //računanje ukupnog iznosa
        Double ukupno = 0.0;

        for (int i = 0; i < otherExpensesModel.getTros().size(); i++) {

            ukupno += Double.valueOf(otherExpensesModel.getTros().get(i).getIznos());

        }


        //postavljanje UI elemenata s konkretnim vrijednostima
        tv_DetailDate.setText(settingValue.getFormatted_date());
        tv_DetailTime.setText(" / " + settingValue.getVrijeme());
        tv_DetailKm.setText(otherExpensesModel.getKmTrenutno() + settingValue.getUnit_kmOrMile());
        tv_DetailTrosak.setText(String.valueOf(ukupno) + settingValue.getCurrency_format());
        tv_NazivObrtnika.setText(otherExpensesModel.getNazivObrtnika());
        tv_Biljeske.setText(otherExpensesModel.getBiljeske());


    }




    /**
     * Overridana metoda koja se poziva prikikom promjenu unutar Settingsa aplikacije koju su spremljeni
     * kroz SharedPreferences
     *
     * @param sharedPreferences
     * @param s
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        //dohvati postveke
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettings(getContext(),otherExpensesModel.getDatumTroska(),otherExpensesModel.getVrijemeTroska());
        //osvježi UI
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
