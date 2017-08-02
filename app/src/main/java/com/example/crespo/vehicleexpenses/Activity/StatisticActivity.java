package com.example.crespo.vehicleexpenses.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.Calendar;
import java.util.Locale;

import Fragments.Tab1Opcenito;
import Fragments.Tab2Tocenje;
import Fragments.Tab3Servisi;
import Fragments.Tab4OstaliTroskovi;
import Models.DatesModel;
import Models.ParametersModel;
import Models.ServiceStatistikaModel;
import Retrofit.api.client.RestClient;
import helper.VehicleAndDistanceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticActivity extends HomeActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static String datumDo;
    public static ServiceStatistikaModel serviceStatistikaModel;
    private static ViewPager mViewPager;
    private static TextView datumOdtv;
    private static TextView datumDotv;
    private static String datumOd;
    private static Bundle bundle;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    private DatesModel datesModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_statistic, frameLayout);
        toolbar.setTitle(R.string.izvije_a_statistika);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.primary_dark4));

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        bundle = new Bundle();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNovi);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.izvije_a_statistika);
        toolbar.setBackgroundColor(getResources().getColor(R.color.primary4));


        datumOdtv = (TextView) findViewById(R.id.date_od_statistika);
        datumDotv = (TextView) findViewById(R.id.date_do_statistika);





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark4));
        }

        mViewPager = (ViewPager) findViewById(R.id.container);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        // Set up the ViewPager with the sections adapter.
        tabLayout = (TabLayout) findViewById(R.id.tabs);


        getMaxMinDatesAndStartActivity();


        datumOdtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DialogFragment newDateOdFragment = new DatePickerFragmentOd();
                newDateOdFragment.show(getSupportFragmentManager(), "DatepickerOd");

            }
        });


        datumDotv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newDateDoFragment = new DatePickerFragmentDo();
                newDateDoFragment.show(getSupportFragmentManager(), "DatepickerDo");

            }
        });



    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private void getMaxMinDatesAndStartActivity() {

        ParametersModel s = new ParametersModel();
        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(this);
        s.setVozilo_id(manager.getVehicleId());


        Call<DatesModel> getDatesService = RestClient.getInstance().getApiService().getMaxMinDate(s);
        getDatesService.enqueue(new Callback<DatesModel>() {
            @Override
            public void onResponse(Call<DatesModel> call, Response<DatesModel> response) {
                if (response.isSuccessful()) {

                    datesModel = response.body();


                    datumOd = datesModel.getMin();
                    datumOdtv.setText(datumOd);
                    datumOd = "'" + datumOd + "'";
                    bundle.putString("DatumOd", datumOd);


                    datumDo = datesModel.getMax();
                    datumDotv.setText(datumDo);
                    datumDo = "'" + datumDo + "'";
                    bundle.putString("DatumDo", datumDo);

                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    tabLayout.setupWithViewPager(mViewPager);

                }
            }

            @Override
            public void onFailure(Call<DatesModel> call, Throwable t) {
                Log.d("MaxMinDate", t.getMessage());
                Toast.makeText(StatisticActivity.this, "Greska s dohvatom podataka", Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }

    public static class DatePickerFragmentOd extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {


            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            String s = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", calendar);
            String s2 = String.format(Locale.getDefault(), "'%1$tY-%1$tm-%1$td'", calendar);

            datumOdtv.setText(s);

            datumOd = datumOdtv.getText().toString();
            bundle.putString("DatumOd", s2);
            mViewPager.getAdapter().notifyDataSetChanged();
            getDialog().dismiss();
        }
    }

    public static class DatePickerFragmentDo extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            String s = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", calendar);
            String s2 = String.format(Locale.getDefault(), "'%1$tY-%1$tm-%1$td'", calendar);


            datumDotv.setText(s);
            datumDo = datumDotv.getText().toString();
            bundle.putString("DatumDo", s2);
            mViewPager.getAdapter().notifyDataSetChanged();
            getDialog().dismiss();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Tab1Opcenito tab1 = new Tab1Opcenito();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    Tab2Tocenje tab2 = new Tab2Tocenje();
                    tab2.setArguments(bundle);
                    return tab2;
                case 2:
                    Tab3Servisi tab3 = new Tab3Servisi();
                    tab3.setArguments(bundle);

                    return tab3;
                case 3:
                    Tab4OstaliTroskovi tab4 = new Tab4OstaliTroskovi();
                    tab4.setArguments(bundle);
                    return tab4;
                default:
                    return null;
            }


        }

        @Override
        public int getCount() {

            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.opcenito_tab);
                case 1:
                    return getString(R.string.tocenje_goriva);
                case 2:
                    return getString(R.string.servisi);
                case 3:
                    return getString(R.string.ostali_troskovi);

            }
            return null;
        }


        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

    }


}

