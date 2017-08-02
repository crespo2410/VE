package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.Activity.StatisticActivity;
import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Interfaces.CardItemClickListener;
import Models.VehicleModel;
import helper.VehicleAndDistanceManager;


/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleHolder> {



    private Context context;
    private ArrayList<VehicleModel> lista_vozila;
    private CardItemClickListener clickListener;
    private  VehicleAndDistanceManager vehicleAndDistanceManager;


    public VehicleAdapter(Context context,ArrayList<VehicleModel> lista_vozila){

        this.context = context;
        this.lista_vozila = lista_vozila;
        vehicleAndDistanceManager = new VehicleAndDistanceManager(context);
    }


    @Override
    public VehicleHolder onCreateViewHolder(ViewGroup parent, int viewType) {


          /* Ako je desi da nema podataka u bazi, postavi TextView koji se prikazati poruku o tome, u suprotnom
        naslijedi normalni layout */
        if (lista_vozila.isEmpty()) {
            View emptyView = parent.findViewById(R.id.emptyVehicleList);
            return new VehicleHolder(emptyView);
        }else{

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_vehicle,null);
            return new VehicleHolder(v);
        }

    }




    @Override
    public void onBindViewHolder(final VehicleHolder holder, final int position) {


        holder.vehicleName.setText(String.valueOf(lista_vozila.get(position).getNazivVozila()));
        int voz_id = vehicleAndDistanceManager.getVehicleId();
        if(lista_vozila.get(position).getId().toString().equals(String.valueOf(voz_id))) holder.switchActiveVehicle.setChecked(true);
        else holder.switchActiveVehicle.setChecked(false);


        holder.switchActiveVehicle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bool) {

                  //u ovisnosti o postavljenom switchu, vozilo se postavlja kao trenutno aktivno
                    if(bool){
                        vehicleAndDistanceManager.setVehicleId(lista_vozila.get(position).getId());
                        vehicleAndDistanceManager.setVehicleName(lista_vozila.get(position).getNazivVozila(),lista_vozila.get(position).getNazivProizvodaca());
                        context.startActivity(new Intent(context, StatisticActivity.class));
                    }else{
                        //Ne koristi se
                    }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista_vozila.size();
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


     class VehicleHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         ImageView vehicleImage;
         TextView vehicleName;
         Switch switchActiveVehicle;


         VehicleHolder(View itemView) {
            super(itemView);

            vehicleImage = (ImageView) itemView.findViewById(R.id.vehicleImage);
            vehicleName = (TextView) itemView.findViewById(R.id.vehicleName);
            switchActiveVehicle = (Switch) itemView.findViewById(R.id.switchActiveVehicle);

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
