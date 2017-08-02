package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

import Interfaces.CardItemClickListener;
import Models.HistoryModel;
import helper.GetSettingValue;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private Context context;
    private ArrayList<HistoryModel> historyModelsList;
    private CardItemClickListener clickListener;


    /**
     * Konstruktor preko kojega inicijaliziramo Context i primamo listu_modela podataka
     * @param context - Context objekt
     * @param lista_models - lista objekata koji sadrže podatke o povijesti
     */
    public HistoryAdapter(Context context, ArrayList<HistoryModel> lista_models) {

        this.context = context;
        this.historyModelsList = lista_models;

    }


    /**
     * Stvaranje view elementa nasljdeđivanje i HistoryHolder objekta
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

          /* Ako je desi da nema podataka u bazi, postavi TextView koji se prikazati poruku o tome, u suprotnom
        naslijedi normalni layout */
        if(historyModelsList.isEmpty()){

            View emptyView = parent.findViewById(R.id.emptyHistoryList);
            return new HistoryHolder(emptyView,viewType);

        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_history, null);
            return new HistoryHolder(v, viewType);
        }
    }


    /**
     * "Ljepljenje referenciranih elemenata iz ViewHoldera podacima i to s obzirom na poziciju"
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {


        //procitaj stvari iz settings datoteke
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettings(context,historyModelsList.get(position).getDatum(),historyModelsList.get(position).getVrijeme());

        //postavi podatke
        holder.datum_tv.setText(context.getString(R.string.datum_vrijeme_history) + settingValue.getFormatted_date() + ", " + settingValue.getVrijeme());
        holder.naslov_tv.setText(historyModelsList.get(position).getNaziv());
        holder.kilometri_tv.setText(context.getString(R.string.km) + historyModelsList.get(position).getKmTrenutno() + " " + settingValue.getUnit_kmOrMile());
        holder.cijena_tv.setText(context.getString(R.string.cijena) + historyModelsList.get(position).getIznos() + " " + settingValue.getCurrency_format());


    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }


    @Override
    public int getItemCount() {
        return historyModelsList.size();
    }


    /**
     * Metoda za inicijalizaciju clickListenera potrebnog za reagiranje na
     * pritisak pojedinog elementa RecyclerViewa
     *
     * @param clickListener
     */
    public void setClickListenerforRecycler(CardItemClickListener clickListener) {

        this.clickListener = clickListener;

    }


    /**
     * Holder klasa koja drži elemente definirane kroz custom_row_history datoteku
     */
    class HistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TimelineView mTimelineView;
        private TextView cijena_tv;
        private TextView datum_tv;
        private TextView naslov_tv;
        private TextView kilometri_tv;


        HistoryHolder(View itemView, int viewType) {
            super(itemView);

            mTimelineView = (TimelineView) itemView.findViewById(R.id.history_timeline);
            cijena_tv = (TextView) itemView.findViewById(R.id.timeline_cijena);
            datum_tv = (TextView) itemView.findViewById(R.id.timeline_date_time);
            naslov_tv = (TextView) itemView.findViewById(R.id.timeline_titile);
            kilometri_tv = (TextView) itemView.findViewById(R.id.timeline_km);
            mTimelineView.initLine(viewType);

            //postavljanje onClickListenera
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            // vezano za mogućnost odabira pojedinog elementa liste kada se klikne na njega
            if (clickListener != null) {

                clickListener.itemClicked(view, getAdapterPosition());
            }

        }
    }


}
