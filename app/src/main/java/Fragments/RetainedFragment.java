package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import Models.HistoryModel;

/**
 * RetainedFragment koji je korišten za probu, a koristi se kod Promjene konfiguracije
 * da se ne bi moralo sve postavljati ponovno - SAMO PROBA - nije korišten nigdje funkcionalno
 */

public class RetainedFragment extends Fragment {

    private ArrayList<HistoryModel> historyModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(ArrayList<HistoryModel> data) {
        this.historyModels = data;
    }

    public ArrayList<HistoryModel>  getData() {
        return historyModels;
    }

}
