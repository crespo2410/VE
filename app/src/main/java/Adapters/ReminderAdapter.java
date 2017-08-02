package Adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.Activity.UpdateReminder;
import com.example.crespo.vehicleexpenses.R;

import java.text.SimpleDateFormat;

import helper.AlarmService;
import helper.DatabaseHelper;


/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    public View.OnClickListener mListener = new reminderClickListener();
    private Context mContext;
    private Cursor mCursor;
    private DatabaseHelper database;
    private RecyclerView mRecyclerView;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm, MMM d ''yy");


    public ReminderAdapter(Context context, Cursor cursor, RecyclerView recyclerView) {

        mContext = context;
        mCursor = cursor;
        mRecyclerView = recyclerView;
        database = new DatabaseHelper(mContext);
    }


    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

            /* Ako je desi da nema podataka u bazi, postavi TextView koji se prikazati poruku o tome, u suprotnom
        naslijedi normalni layout */
        if (database.isEmpty()) {
            View emptyView = parent.findViewById(R.id.empty);
            return new ViewHolder(emptyView);
        } else {

            View reminderView = inflater.inflate(R.layout.custom_row_reminder, parent, false);
            reminderView.setOnClickListener(mListener);

            return new ViewHolder(reminderView);
        }
    }


    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder viewHolder, int id) {
        mCursor.moveToPosition(id);

        viewHolder.title.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TITLE)));
        viewHolder.date.setText(timeFormat.format(mCursor.getLong(mCursor.getColumnIndex(DatabaseHelper.DB_COLUMN_TIME))));
        viewHolder.content.setText(mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DB_COLUMN_CONTENT)));
    }


    public int getItemCount() {
        int broj = mCursor.getCount();
        return broj;
    }


    //stvaranje dijaloga za brisanje željenog alarma
    private AlertDialog deleteDialog(int id, final int position) {
        final int deleteId = id;
        final Cursor cursor = database.getItem(id);
        cursor.moveToFirst();

        return new AlertDialog.Builder(mContext)
                .setTitle(R.string.potvrda)
                .setMessage(R.string.delete_question)

                .setPositiveButton(R.string.potvrdno, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int i) {

                        //izbriši view s pozicije
                        mRecyclerView.removeViewAt(position);


                        //startanje servisa kako bi se izbrisano/otkazao alarm
                        Intent delete = new Intent(mContext, AlarmService.class);
                        delete.putExtra("id", deleteId);
                        delete.putExtra("deletedFromMain", true);
                        delete.setAction(AlarmService.DELETE);
                        mContext.startService(delete);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton(R.string.negativno, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                })
                .create();

    }


    //normalni kratki clickListener
    class reminderClickListener implements View.OnClickListener {
        public void onClick(View view) {
            int position = mRecyclerView.getChildAdapterPosition(view);
            mCursor.moveToPosition(position);
            Intent intent;
            intent = new Intent(mContext, UpdateReminder.class);
            intent.putExtra("ID", mCursor.getInt(mCursor.getColumnIndex(DatabaseHelper.DB_COLUMN_ID)));
            mContext.startActivity(intent);
        }
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView content;
        ImageView icon;

        ViewHolder(View view) {

            super(view);
            title = (TextView) view.findViewById(R.id.tv_NaslovObavijesti);
            date = (TextView) view.findViewById(R.id.tv_DatumObavijesti);
            content = (TextView) view.findViewById(R.id.tv_BiljeskeObavijesti);
            icon = (ImageView) view.findViewById(R.id.iconObavijest);

        }
    }

}


