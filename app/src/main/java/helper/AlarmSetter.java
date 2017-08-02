package helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.Calendar;


/**
 * Kada se napravi reboot uređaja potrebno je ponovno postavniti sve alarme
 */
public class AlarmSetter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(context);

        DatabaseHelper database = new DatabaseHelper(context);
        Cursor cursor = database.getAllItems(manager.getVehicleId());
        try {
            while (cursor.moveToNext()) {

                long time = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TIME));

                if (time > Calendar.getInstance().getTimeInMillis()) {
                    Intent service = new Intent(context, AlarmService.class);
                    service.setAction(AlarmService.CREATE);
                    service.putExtra("id", cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DB_COLUMN_ID)));
                    context.startService(service);
                }
            }
        } finally {
            cursor.close();
        }

    }

}

