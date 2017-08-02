package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;

import java.util.List;

import Interfaces.CardItemClickListener;
import Models.OtherExpensesModel;
import helper.GetSettingValue;

/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */
public class OtherExpensesAdapter extends RecyclerView.Adapter<OtherExpensesAdapter.OtherExpensesHolder> {


    private Context context;
    private List<OtherExpensesModel> otherExpensesModelsList;
    private CardItemClickListener clickListener;


    public OtherExpensesAdapter(Context context, List<OtherExpensesModel> otherExpensesModelsList) {

        this.context = context;
        this.otherExpensesModelsList = otherExpensesModelsList;

    }


    @Override
    public OtherExpensesHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /* Ako je desi da nema podataka u bazi, postavi TextView koji se prikazati poruku o tome, u suprotnom
        naslijedi normalni layout */

        if (otherExpensesModelsList.isEmpty()) {
            View emptyView = parent.findViewById(R.id.emptyServiseList);
            return new OtherExpensesHolder(emptyView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_ostali_troskovi, null);
            return new OtherExpensesHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(OtherExpensesHolder holder, int position) {

        //dobivanje vrijednosti postavki
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettings(context,otherExpensesModelsList.get(position).getDatumTroska(),otherExpensesModelsList.get(position).getVrijemeTroska());

        holder.tv_naziv_ostalog_troska.setText(otherExpensesModelsList.get(position).getTros().get(0).getNazivOstalogTroska());
        holder.tv_datum_ostalog_troska.setText(settingValue.getFormatted_date());
        holder.tv_kilometraza_ostalog_troska.setText(otherExpensesModelsList.get(position).getKmTrenutno() + " " + settingValue.getUnit_kmOrMile());


        Double ukupno = 0.0;

        for (int i = 0; i < otherExpensesModelsList.get(position).getTros().size(); i++) {

            ukupno += Double.valueOf(otherExpensesModelsList.get(position).getTros().get(i).getIznos());

        }


        holder.tv_ukupni_trosak_ostalog_troska.setText(String.valueOf(ukupno) + " "+settingValue.getCurrency_format());


    }

    @Override
    public int getItemCount() {
        return otherExpensesModelsList.size();
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



     class OtherExpensesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_naziv_ostalog_troska;
        public TextView tv_datum_ostalog_troska;
        public TextView tv_kilometraza_ostalog_troska;
        public TextView tv_ukupni_trosak_ostalog_troska;



         OtherExpensesHolder(View itemView) {
            super(itemView);


            tv_naziv_ostalog_troska = (TextView) itemView.findViewById(R.id.tv_Naziv_ostalog_troska);
            tv_datum_ostalog_troska = (TextView) itemView.findViewById(R.id.tv_Datum_ostalog_troska);
            tv_kilometraza_ostalog_troska = (TextView) itemView.findViewById(R.id.tv_kilometraza_ostalog_troska);
            tv_ukupni_trosak_ostalog_troska = (TextView) itemView.findViewById(R.id.tv_ukupni_trosak_ostali_trosak);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (clickListener != null) {

                clickListener.itemClicked(view, getAdapterPosition());
            }

        }
    }

}
