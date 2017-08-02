package helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Pomocna klasa za dohvat id_vozila, zadnje_kilometrazi iz SharedPreference datoteke
 */
public class VehicleAndDistanceManager {

    private static String TAG = VehicleAndDistanceManager.class.getSimpleName();


    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "VoziloAndDistance";

    private static final String VEHICLE_ID = "VoziloID";
    private static final String LAST_DISTANCE = "LastDistance";
    private static final String LAST_DATE = "LastDate";
    private static final String VEHICLE_NAME = "Vehicle_name";
    private static final String VEHICLE_MANUFACTURER = "Vehicle_Manufacturer";


    public VehicleAndDistanceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLastDistance(String lastDistance) {

        editor.putString(LAST_DISTANCE, lastDistance);
        editor.commit();
    }

    public void setLastDate(long lastDate) {

        editor.putLong(LAST_DATE, lastDate);
        editor.commit();
    }

    public void setVehicleId(int vehicleId){
        editor.putInt(VEHICLE_ID,vehicleId);
        editor.commit();

    }


    public void setVehicleName(String name, String manufacturer) {

        editor.putString(VEHICLE_NAME, name);
        editor.putString(VEHICLE_MANUFACTURER, manufacturer);
        editor.commit();
    }



    public String getLastDistance(){
        return pref.getString(LAST_DISTANCE, "0");
    }

    public String getVehicleName(){
        return pref.getString(VEHICLE_NAME, "0");
    }

    public String getManufactrer(){
        return pref.getString(VEHICLE_MANUFACTURER, "0");
    }


    public long getLastDate(){
        return pref.getLong(LAST_DATE,0);
    }

    public int getVehicleId(){
        return pref.getInt(VEHICLE_ID,1);
    }

}
