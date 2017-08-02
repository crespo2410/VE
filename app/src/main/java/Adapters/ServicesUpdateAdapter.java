package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crespo.vehicleexpenses.R;

import java.util.ArrayList;

import Models.Ok;
import Models.ServiceExpensesModel;
import Models.ServiceModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */
public class ServicesUpdateAdapter extends RecyclerView.Adapter<ServicesUpdateAdapter.ServisUpdateHolder> {


    private Context context;
    private ServiceModel serviceModel;
    private ArrayList<ServiceExpensesModel> service_expenses_list;
    private int pritisnutaTipka;


    private static final String BRISANJE = "er_delete_ServiceUpdate";


    public ServicesUpdateAdapter(Context context, ServiceModel serviceModel, ArrayList<ServiceExpensesModel> service_expenses_list, int pritisnutaTipka) {
        this.context = context;
        this.serviceModel = serviceModel;
        this.pritisnutaTipka = pritisnutaTipka;
        this.service_expenses_list = service_expenses_list;
    }


    @Override
    public ServisUpdateHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_servisi_update, null);
        return new ServisUpdateHolder(v);

    }


    @Override
    public void onBindViewHolder(ServisUpdateHolder holder, final int position) {

        //dobivanje vrijednosti postavki
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsDetail(context);

        holder.tv_tipServisa.setText(serviceModel.getTros().get(position).getNazivServisa());
        holder.tv_vrijednost.setText(serviceModel.getTros().get(position).getIznos() + settingValue.getCurrency_format());
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //provjeravamo da li vec postoji unos unutar liste
                boolean unutar_liste = false;
                for (int i = 0; i < service_expenses_list.size(); i++) {
                    if ((serviceModel.getTros().get(position).getIdT() == service_expenses_list.get(i).getIdT()) && (service_expenses_list.get(i).getIdT() != null))
                        unutar_liste = true;
                }

                //ako smo došli sa pritiskom tipke za dodavanje novog troška i nema vec takvog unosa u listi (bazi), izbrisati samo iz RecyclerViewa
                if ((pritisnutaTipka == R.id.buttonAddService) || (unutar_liste == false)) {

                    serviceModel.getTros().remove(position);
                    notifyDataSetChanged();
                }


                //u suprotnom izbrisati baš i iz same baze
                else {


                    Toast.makeText(context, serviceModel.getTros().get(position).getIdT(), Toast.LENGTH_SHORT).show();

                    Call<Ok> deleteTrosakRepos = RestClient.getInstance().getApiService().deleteTroskovi(Integer.valueOf(serviceModel.getTros().get(position).getIdT()));

                    deleteTrosakRepos.enqueue(new Callback<Ok>() {
                        @Override
                        public void onResponse(Call<Ok> call, Response<Ok> response) {
                            serviceModel.getTros().remove(position);
                            notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<Ok> call, Throwable t) {
                            Log.d(BRISANJE,t.getMessage());
                            Toast.makeText(context, context.getString(R.string.poruka_greske_dohvata), Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return serviceModel.getTros().size();
    }





    class ServisUpdateHolder extends RecyclerView.ViewHolder {

        TextView tv_tipServisa;
        TextView tv_vrijednost;
        ImageView deleteImage;


        ServisUpdateHolder(View itemView) {
            super(itemView);

            tv_tipServisa = (TextView) itemView.findViewById(R.id.tip_servis_tv_update);
            tv_vrijednost = (TextView) itemView.findViewById(R.id.vrijednost_tv_update);
            deleteImage = (ImageView) itemView.findViewById(R.id.brisanjeRecycler);

        }


    }
}
