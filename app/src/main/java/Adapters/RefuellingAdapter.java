package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Interfaces.CardItemClickListener;
import Models.TocenjeModel;
import helper.GetSettingValue;



/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */
public class RefuellingAdapter extends RecyclerView.Adapter<RefuellingAdapter.RefuellingHolder> {


    private Context context;
    private ArrayList<Models.TocenjeModel> lista_tocenja;
    private CardItemClickListener clickListener;


    public RefuellingAdapter(Context context, ArrayList<TocenjeModel> lista_tocenja){

        this.context = context;
        this.lista_tocenja = lista_tocenja;
    }




    @Override
    public RefuellingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /* Ako je desi da nema podataka u bazi, postavi TextView koji se prikazati poruku o tome, u suprotnom
        naslijedi normalni layout */
        if (lista_tocenja.isEmpty()) {
            View emptyView = parent.findViewById(R.id.emptyTocenjeList);
            return new RefuellingHolder(emptyView);
        }else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_tocenja, null);
            return new RefuellingHolder(v);
        }
    }



    /**
     * Metoda za inicijalizaciju clickListenera potrebnog za reagiranje na
     * pritisak pojedinog elementa RecyclerViewa
     *
     * @param clickListener
     */
    public void setClickListenerforRecycler(CardItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(final RefuellingHolder holder, int position) {
        //dobivanje vrijednosti postavki
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettings(context,lista_tocenja.get(position).getTocenjeDatum());

        holder.tv_Ime_benzinske.setText(lista_tocenja.get(position).getBenzinskaNaziv().toString());
        holder.tv_cijena_po_litri.setText(lista_tocenja.get(position).getCijena().toString() + settingValue.getCurrency_format());
        holder.tv_datum.setText(settingValue.getFormatted_date());
        holder.tv_kilometraza.setText(lista_tocenja.get(position).getKmTrenutno().toString() + settingValue.getUnit_kmOrMile());
        holder.tv_naziv_goriva.setText(lista_tocenja.get(position).getNazivGoriva().toString());
        holder.tv_ukupni_trosak.setText(lista_tocenja.get(position).getUkupniTrosak().toString() + settingValue.getCurrency_format());
        holder.tv_uzeto_litara.setText(lista_tocenja.get(position).getUzetoLitara().toString() + settingValue.getUnit_litraOrGalon());

    }


    @Override
    public int getItemCount() {
        return lista_tocenja.size();
    }




     class RefuellingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         TextView tv_Ime_benzinske;
         TextView tv_naziv_goriva;
         TextView tv_cijena_po_litri;
         TextView tv_km_po_l;
         TextView tv_kilometraza;
         TextView tv_ukupni_trosak;
         TextView tv_uzeto_litara;
         TextView tv_datum;



         RefuellingHolder(View itemView) {
            super(itemView);

            tv_Ime_benzinske = (TextView) itemView.findViewById(R.id.tv_Ime_benzinske);
            tv_naziv_goriva = (TextView) itemView.findViewById(R.id.tv_naziv_goriva);
            tv_cijena_po_litri = (TextView) itemView.findViewById(R.id.tv_cijena_po_litri);
            tv_km_po_l = (TextView) itemView.findViewById(R.id.tv_km_po_l);
            tv_kilometraza = (TextView) itemView.findViewById(R.id.tv_kilometraza);
            tv_ukupni_trosak = (TextView) itemView.findViewById(R.id.tv_ukupni_trosak);
            tv_uzeto_litara = (TextView) itemView.findViewById(R.id.tv_uzeto_litara);
            tv_datum = (TextView) itemView.findViewById(R.id.tv_datum);



            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if(clickListener != null){

                clickListener.itemClicked(view, getAdapterPosition());
            }

        }


    }






}
