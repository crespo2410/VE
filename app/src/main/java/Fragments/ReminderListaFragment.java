package Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.Activity.UpdateReminder;
import com.example.crespo.vehicleexpenses.R;

import Adapters.ReminderAdapter;
import helper.DatabaseHelper;
import helper.VehicleAndDistanceManager;

/**
 * Fragment klasa - prikaz liste Podsjetnika
 */
public class ReminderListaFragment extends Fragment {


    private DatabaseHelper database;
    private TextView empty;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReminderAdapter adapter;
    private FloatingActionButton button;
    private Cursor cursor;


    //deleteReciver koji prima "DELETE" signal, te na kraju updatea UI
    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("REFRESH")) {
                emptyCheck();
                adapter.notifyDataSetChanged();
            }
        }
    };


    public ReminderListaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder_lista, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        VehicleAndDistanceManager manager = new VehicleAndDistanceManager(getContext());

        database = new DatabaseHelper(getActivity());
        cursor = database.getAllItems(manager.getVehicleId());

        //broadcastManager koje je zadužen za AlarmService
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter filter = new IntentFilter("REFRESH");
        broadcastManager.registerReceiver(deleteReceiver, filter);


        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewReminder);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new ReminderAdapter(getActivity(), cursor, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

        empty = (TextView) getActivity().findViewById(R.id.empty);
        emptyCheck();

        adapter.notifyDataSetChanged();


        button = (FloatingActionButton) getActivity().findViewById(R.id.buttonAddReminder);
        button.setImageResource(R.drawable.ic_note_add_white_48dp);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), UpdateReminder.class));
            }
        });


    }

    /**
     * Metoda za provjeru da li je lista dohvačenih podataka prazna kako bi se znalo
     * da li treba ispisati poruku u TextView da nema podataka
     */
    private void emptyCheck() {
        if (database.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!cursor.isClosed()) {

            cursor.close();
        }

        database.close();
    }

}
