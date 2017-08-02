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

import Models.ExpensesOtherExpensesModel;
import Models.Ok;
import Models.OtherExpensesModel;
import Retrofit.api.client.RestClient;
import helper.GetSettingValue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Klasa po funkcionalnosti i načinu rada jednaka HistoryAdapter klasi u kojoj je objašnjeno
 */
public class OtherExpensesUpdateAdapter extends RecyclerView.Adapter<OtherExpensesUpdateAdapter.OtherExpensesUpdateHolder> {

    private Context context;
    private OtherExpensesModel otherExpensesModel;
    private ArrayList<ExpensesOtherExpensesModel> expenses_list;
    private int pritisnutaTipka;

    private static final String BRISANJE = "er_delete_Other";


    public OtherExpensesUpdateAdapter(Context context, OtherExpensesModel otherExpensesModel, ArrayList<ExpensesOtherExpensesModel> lista, int pritisnutaTipka) {

        this.context = context;
        this.otherExpensesModel = otherExpensesModel;
        this.pritisnutaTipka = pritisnutaTipka;
        this.expenses_list = lista;

    }


    @Override
    public OtherExpensesUpdateHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_servisi_update, null);
        return new OtherExpensesUpdateHolder(v);


    }

    @Override
    public void onBindViewHolder(OtherExpensesUpdateHolder holder, final int position) {


        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsDetail(context);


        holder.tv_tipServisa.setText(otherExpensesModel.getTros().get(position).getNazivOstalogTroska());
        holder.tv_vrijednost.setText(otherExpensesModel.getTros().get(position).getIznos() + settingValue.getCurrency_format());
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //provjeravamo da li vec postoji unos unutar liste
                boolean unutar_liste = false;
                for (int i = 0; i < expenses_list.size(); i++) {

                    if ((otherExpensesModel.getTros().get(position).getIdTot() == expenses_list.get(i).getIdTot()) && (expenses_list.get(i).getIdTot() != null))
                        unutar_liste = true;
                }


                //ako smo došli sa pritiskom tipke za dodavanje novog troška i nema vec takvog unosa u listi (bazi), izbrisati samo iz RecyclerViewa
                if ((pritisnutaTipka == R.id.buttonAddOtherExpenses) || (unutar_liste == false)) {

                    otherExpensesModel.getTros().remove(position);
                    notifyDataSetChanged();

                }

                //u suprotnom izbrisati baš i iz same baze
                else {


                    Call<Ok> deleteExpenseCall = RestClient.getInstance().getApiService().deleteTroskoviostale(Integer.valueOf(otherExpensesModel.getTros().get(position).getIdTot()));

                    deleteExpenseCall.enqueue(new Callback<Ok>() {
                        @Override
                        public void onResponse(Call<Ok> call, Response<Ok> response) {

                            if (response.isSuccessful()) {
                                otherExpensesModel.getTros().remove(position);
                                notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onFailure(Call<Ok> call, Throwable t) {
                            Log.d(BRISANJE,t.getMessage());
                            Toast.makeText(context,context.getString(R.string.poruka_greske_dohvata), Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            }
        });


    }


    @Override
    public int getItemCount() {
        return otherExpensesModel.getTros().size();
    }

    public class OtherExpensesUpdateHolder extends RecyclerView.ViewHolder {

        public TextView tv_tipServisa;
        public TextView tv_vrijednost;
        public ImageView deleteImage;

        public OtherExpensesUpdateHolder(View itemView) {
            super(itemView);


            tv_tipServisa = (TextView) itemView.findViewById(R.id.tip_servis_tv_update);
            tv_vrijednost = (TextView) itemView.findViewById(R.id.vrijednost_tv_update);
            deleteImage = (ImageView) itemView.findViewById(R.id.brisanjeRecycler);

        }
    }
}
