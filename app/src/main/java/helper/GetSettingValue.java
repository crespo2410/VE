package helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pomocna klasa preko koje citam postavke
 */

public class GetSettingValue {


    private String unit_kmOrMile;
    private String date_format;
    private String time_format;
    private String currency_format;

    public String getUnit_litraOrGalon() {
        return unit_litraOrGalon;
    }

    public void setUnit_litraOrGalon(String unit_litraOrGalon) {
        this.unit_litraOrGalon = unit_litraOrGalon;
    }

    private String unit_litraOrGalon;

    private String formatted_date;
    private String vrijeme;
    private Date time;

    public String getUnit_kmOrMile() {
        return unit_kmOrMile;
    }

    public void setUnit_kmOrMile(String unit_kmOrMile) {
        this.unit_kmOrMile = unit_kmOrMile;
    }

    public String getDate_format() {
        return date_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    public String getTime_format() {
        return time_format;
    }

    public void setTime_format(String time_format) {
        this.time_format = time_format;
    }

    public String getCurrency_format() {
        return currency_format;
    }

    public void setCurrency_format(String currency_format) {
        this.currency_format = currency_format;
    }

    public String getFormatted_date() {
        return formatted_date;
    }

    public void setFormatted_date(String formatted_date) {
        this.formatted_date = formatted_date;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public void readSettings(Context context, Date date, String vrijeme_object) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        unit_kmOrMile = preferences.getString(SharedpreferencesKeys.DISTANCE_UNIT_LIST, "1");
        date_format = preferences.getString(SharedpreferencesKeys.DATE_LIST, "1");
        time_format = preferences.getString(SharedpreferencesKeys.TIME_LIST, "1");
        currency_format = preferences.getString(SharedpreferencesKeys.CURRENCY_LIST, "1");


        switch (unit_kmOrMile) {
            case "1":
                unit_kmOrMile = "km";
                break;
            case "2":
                unit_kmOrMile = "Mi";
                break;
            default:

        }


        switch (currency_format) {

            case "1":
                currency_format = " €";
                break;
            case "2":
                currency_format = " $";
                break;
            case "3":
                currency_format = " £";
                break;

            case "4":
                currency_format = " HRK";
                break;

            default:


        }


        switch (date_format) {

            case "1":
                formatted_date = String.format(Locale.getDefault(), "%1$td/%1$tm/%1$tY", date);
                break;
            case "2":
                formatted_date = String.format(Locale.getDefault(), "%1$td-%1$tm-%1$tY", date);
                break;
            case "3":
                formatted_date = String.format(Locale.getDefault(), "%1$tY/%1$tm/%1$td", date);
                break;
            case "4":
                formatted_date = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", date);
                break;
            case "5":
                formatted_date = String.format(Locale.getDefault(), "%1$td %1$tb %1$tY", date);
                break;
            default:


        }


        switch (time_format) {

            case "1":

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                try {

                    time = timeFormat.parse(vrijeme_object);
                    vrijeme = timeFormat.format(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case "2":

                SimpleDateFormat timeFormat2 = new SimpleDateFormat("h:mm:ss");
                try {


                    time = timeFormat2.parse(vrijeme_object);
                    vrijeme = timeFormat2.format(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;
            default:


        }


    }





    public void readSettings(Context context, Date date) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        unit_kmOrMile = preferences.getString(SharedpreferencesKeys.DISTANCE_UNIT_LIST, "1");
        date_format = preferences.getString(SharedpreferencesKeys.DATE_LIST, "1");
        time_format = preferences.getString(SharedpreferencesKeys.TIME_LIST, "1");
        currency_format = preferences.getString(SharedpreferencesKeys.CURRENCY_LIST, "1");
        unit_litraOrGalon = preferences.getString(SharedpreferencesKeys.VOLUME_LIST,"1");


        switch (unit_kmOrMile) {
            case "1":
                unit_kmOrMile = "km";
                break;
            case "2":
                unit_kmOrMile = "Mi";
                break;
            default:

        }


        switch (currency_format) {

            case "1":
                currency_format = " €";
                break;
            case "2":
                currency_format = " $";
                break;
            case "3":
                currency_format = " £";
                break;

            case "4":
                currency_format = " HRK";
                break;

            default:


        }


        switch (date_format) {

            case "1":
                formatted_date = String.format(Locale.getDefault(), "%1$td/%1$tm/%1$tY", date);
                break;
            case "2":
                formatted_date = String.format(Locale.getDefault(), "%1$td-%1$tm-%1$tY", date);
                break;
            case "3":
                formatted_date = String.format(Locale.getDefault(), "%1$tY/%1$tm/%1$td", date);
                break;
            case "4":
                formatted_date = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", date);
                break;
            case "5":
                formatted_date = String.format(Locale.getDefault(), "%1$td %1$tb %1$tY", date);
                break;
            default:


        }

        switch (unit_litraOrGalon){

            case "1":
                unit_litraOrGalon = " l";
                break;
            case "2":
                unit_litraOrGalon = " gal US";
                break;
            case "3":
                unit_litraOrGalon = " gal Imp";
                break;
            case "4":
                unit_litraOrGalon = " kwh";
                break;

            case "5":
                unit_litraOrGalon = " m3";
                break;
            default:



        }
    }



    public void readSettingsToc(Context context, Date date, String vrijeme_object) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        unit_kmOrMile = preferences.getString(SharedpreferencesKeys.DISTANCE_UNIT_LIST, "1");
        date_format = preferences.getString(SharedpreferencesKeys.DATE_LIST, "1");
        time_format = preferences.getString(SharedpreferencesKeys.TIME_LIST, "1");
        currency_format = preferences.getString(SharedpreferencesKeys.CURRENCY_LIST, "1");
        unit_litraOrGalon = preferences.getString(SharedpreferencesKeys.VOLUME_LIST,"1");


        switch (unit_kmOrMile) {
            case "1":
                unit_kmOrMile = "km";
                break;
            case "2":
                unit_kmOrMile = "Mi";
                break;
            default:

        }


        switch (currency_format) {

            case "1":
                currency_format = " €";
                break;
            case "2":
                currency_format = " $";
                break;
            case "3":
                currency_format = " £";
                break;

            case "4":
                currency_format = " HRK";
                break;

            default:


        }


        switch (date_format) {

            case "1":
                formatted_date = String.format(Locale.getDefault(), "%1$td/%1$tm/%1$tY", date);
                break;
            case "2":
                formatted_date = String.format(Locale.getDefault(), "%1$td-%1$tm-%1$tY", date);
                break;
            case "3":
                formatted_date = String.format(Locale.getDefault(), "%1$tY/%1$tm/%1$td", date);
                break;
            case "4":
                formatted_date = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", date);
                break;
            case "5":
                formatted_date = String.format(Locale.getDefault(), "%1$td %1$tb %1$tY", date);
                break;
            default:


        }

        switch (unit_litraOrGalon){

            case "1":
                unit_litraOrGalon = " l";
                break;
            case "2":
                unit_litraOrGalon = " gal US";
                break;
            case "3":
                unit_litraOrGalon = " gal Imp";
                break;
            case "4":
                unit_litraOrGalon = " kwh";
                break;

            case "5":
                unit_litraOrGalon = " m3";
                break;
            default:



        }







        switch (time_format) {

            case "1":

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                try {

                    time = timeFormat.parse(vrijeme_object);
                    vrijeme = timeFormat.format(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case "2":

                SimpleDateFormat timeFormat2 = new SimpleDateFormat("h:mm:ss");
                try {


                    time = timeFormat2.parse(vrijeme_object);
                    vrijeme = timeFormat2.format(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;
            default:


        }




    }



    public void readDateOnlyFromSettings(Context context,Date date){


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        date_format = preferences.getString(SharedpreferencesKeys.DATE_LIST, "1");



        switch (date_format) {

            case "1":
                formatted_date = String.format(Locale.getDefault(), "%1$td/%1$tm/%1$tY", date);
                break;
            case "2":
                formatted_date = String.format(Locale.getDefault(), "%1$td-%1$tm-%1$tY", date);
                break;
            case "3":
                formatted_date = String.format(Locale.getDefault(), "%1$tY/%1$tm/%1$td", date);
                break;
            case "4":
                formatted_date = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", date);
                break;
            case "5":
                formatted_date = String.format(Locale.getDefault(), "%1$td %1$tb %1$tY", date);
                break;
            default:


        }


    }



    public void readTimeOnlyFromSettings(Context context,Date time){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        time_format = preferences.getString(SharedpreferencesKeys.TIME_LIST, "1");


        switch (time_format) {

            case "1":

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                    vrijeme = timeFormat.format(time);

                break;

            case "2":

                SimpleDateFormat timeFormat2 = new SimpleDateFormat("hh:mm:ss");

                    vrijeme = timeFormat2.format(time);


                break;
            default:


        }


    }

    public void readDateFromSettings(Context context,Date date,String time_new ) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        date_format = preferences.getString(SharedpreferencesKeys.DATE_LIST, "1");
        time_format = preferences.getString(SharedpreferencesKeys.TIME_LIST, "1");


        switch (date_format) {

            case "1":
                formatted_date = String.format(Locale.getDefault(), "%1$td/%1$tm/%1$tY", date);
                break;
            case "2":
                formatted_date = String.format(Locale.getDefault(), "%1$td-%1$tm-%1$tY", date);
                break;
            case "3":
                formatted_date = String.format(Locale.getDefault(), "%1$tY/%1$tm/%1$td", date);
                break;
            case "4":
                formatted_date = String.format(Locale.getDefault(), "%1$tY-%1$tm-%1$td", date);
                break;
            case "5":
                formatted_date = String.format(Locale.getDefault(), "%1$td %1$tb %1$tY", date);
                break;
            default:


        }


        switch (time_format) {

            case "1":

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                try {

                    time = timeFormat.parse(time_new);
                    vrijeme = timeFormat.format(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

            case "2":

                SimpleDateFormat timeFormat2 = new SimpleDateFormat("hh:mm:ss");
                try {


                    time = timeFormat2.parse(time_new);
                    vrijeme = timeFormat2.format(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;
            default:


        }


    }




    public void readSettingsUpdate(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        unit_kmOrMile = preferences.getString(SharedpreferencesKeys.DISTANCE_UNIT_LIST, "1");
        currency_format = preferences.getString(SharedpreferencesKeys.CURRENCY_LIST, "1");
        unit_litraOrGalon = preferences.getString(SharedpreferencesKeys.VOLUME_LIST, "1");


        switch (unit_litraOrGalon) {

            case "1":
                unit_litraOrGalon = "l";
                break;
            case "2":
                unit_litraOrGalon = "gal US";
                break;
            case "3":
                unit_litraOrGalon = "gal Imp";
                break;
            case "4":
                unit_litraOrGalon = "kwh";
                break;

            case "5":
                unit_litraOrGalon = "m3";
                break;
            default:


        }


        switch (unit_kmOrMile) {
            case "1":
                unit_kmOrMile = "km";
                break;
            case "2":
                unit_kmOrMile = "Mi";
                break;
            default:

        }


        switch (currency_format) {

            case "1":
                currency_format = " €";
                break;
            case "2":
                currency_format = " $";
                break;
            case "3":
                currency_format = " £";
                break;

            case "4":
                currency_format = " HRK";
                break;

            default:


        }


    }





    public void readSettingsDetail(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        unit_kmOrMile = preferences.getString(SharedpreferencesKeys.DISTANCE_UNIT_LIST, "1");
        currency_format = preferences.getString(SharedpreferencesKeys.CURRENCY_LIST, "1");


        switch (unit_kmOrMile) {
            case "1":
                unit_kmOrMile = " km";
                break;
            case "2":
                unit_kmOrMile = " Mi";
                break;
            default:

        }


        switch (currency_format) {

            case "1":
                currency_format = " €";
                break;
            case "2":
                currency_format = " $";
                break;
            case "3":
                currency_format = " £";
                break;

            case "4":
                currency_format = " HRK";
                break;

            default:


        }

    }













}
