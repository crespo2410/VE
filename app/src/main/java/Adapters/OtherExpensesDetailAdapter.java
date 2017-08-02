package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;

import Models.OtherExpensesModel;
import helper.GetSettingValue;


public class OtherExpensesDetailAdapter extends RecyclerView.Adapter<OtherExpensesDetailAdapter.OtherExpensesDetailHolder> {

    private Context context;
    private OtherExpensesModel otherExpensesModel;



    public OtherExpensesDetailAdapter(Context context, OtherExpensesModel otherExpensesModel) {

        this.context = context;
        this.otherExpensesModel = otherExpensesModel;


    }


    @Override
    public OtherExpensesDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_servisi_detail, null);
        return new OtherExpensesDetailHolder(v);

    }

    @Override
    public void onBindViewHolder(OtherExpensesDetailHolder holder, int position) {

        //dobivanje vrijednosti postavki
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsDetail(context);


        holder.tv_tipServisa.setText(otherExpensesModel.getTros().get(position).getNazivOstalogTroska());
        holder.tv_vrijednost.setText(otherExpensesModel.getTros().get(position).getIznos() + settingValue.getCurrency_format());


    }


    @Override
    public int getItemCount() {
        return otherExpensesModel.getTros().size();
    }


    class OtherExpensesDetailHolder extends RecyclerView.ViewHolder {

        TextView tv_tipServisa;
        TextView tv_vrijednost;


        OtherExpensesDetailHolder(View itemView) {
            super(itemView);

            tv_tipServisa = (TextView) itemView.findViewById(R.id.tip_servis_tv);
            tv_vrijednost = (TextView) itemView.findViewById(R.id.vrijednost_tv);
        }
    }
}
