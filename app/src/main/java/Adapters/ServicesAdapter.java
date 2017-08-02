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
import Models.ServiceModel;
import helper.GetSettingValue;


/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServisiHolder> {

    private Context context;
    private List<ServiceModel> lista_servisa;
    private CardItemClickListener clickListener;



    public ServicesAdapter(Context context, List<ServiceModel> lista_servisa) {

        this.context = context;
        this.lista_servisa = lista_servisa;
    }


    @Override
    public ServisiHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /* Ako je desi da nema podataka u bazi, postavi TextView koji se prikazati poruku o tome, u suprotnom
        naslijedi normalni layout */
        if (lista_servisa.isEmpty()) {
            View emptyView = parent.findViewById(R.id.emptyServiseList);
            return new ServicesAdapter.ServisiHolder(emptyView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_servisi, null);
            return new ServisiHolder(v);

        }

    }


    @Override
    public void onBindViewHolder(ServisiHolder holder, int position) {

        //dobivanje vrijednosti postavki
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettings(context,lista_servisa.get(position).getDatumServis(),lista_servisa.get(position).getVrijemeServis());

        holder.tv_naziv_servisa.setText(lista_servisa.get(position).getTros().get(0).getNazivServisa());
        holder.tv_datum_servisa.setText(settingValue.getFormatted_date());
        holder.tv_kilometraza_servis.setText(lista_servisa.get(position).getKmTrenutno() + " "+ settingValue.getUnit_kmOrMile());

        Double ukupno = 0.0;

        for (int i = 0; i < lista_servisa.get(position).getTros().size(); i++) {
            ukupno += Double.valueOf(lista_servisa.get(position).getTros().get(i).getIznos());
        }

        holder.tv_ukupni_trosak_servis.setText(String.valueOf(ukupno) + " "+ settingValue.getCurrency_format());

    }


    @Override
    public int getItemCount() {
        return lista_servisa.size();
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





    class ServisiHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_naziv_servisa;
        TextView tv_datum_servisa;
        TextView tv_kilometraza_servis;
        TextView tv_ukupni_trosak_servis;


        ServisiHolder(View itemView) {
            super(itemView);


            tv_naziv_servisa = (TextView) itemView.findViewById(R.id.tv_Naziv_servisa);
            tv_datum_servisa = (TextView) itemView.findViewById(R.id.tv_Datum_servisa);
            tv_kilometraza_servis = (TextView) itemView.findViewById(R.id.tv_kilometraza_servis);
            tv_ukupni_trosak_servis = (TextView) itemView.findViewById(R.id.tv_ukupni_trosak_servis);

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
