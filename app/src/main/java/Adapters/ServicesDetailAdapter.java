package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.crespo.vehicleexpenses.R;

import Models.ServiceModel;
import helper.GetSettingValue;

public class ServicesDetailAdapter extends RecyclerView.Adapter<ServicesDetailAdapter.ServicesDetailHolder> {


    private Context context;
    private ServiceModel serviceModel;

;


    public ServicesDetailAdapter(Context context, ServiceModel serviceModel) {

        this.context = context;
        this.serviceModel = serviceModel;

    }


    @Override
    public ServicesDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_servisi_detail, null);
        return new ServicesDetailHolder(v);
    }


    @Override
    public void onBindViewHolder(ServicesDetailHolder holder, int position) {

        //dobivanje vrijednosti postavki
        GetSettingValue settingValue = new GetSettingValue();
        settingValue.readSettingsDetail(context);

        holder.tv_tipServisa.setText(serviceModel.getTros().get(position).getNazivServisa());
        holder.tv_vrijednost.setText(serviceModel.getTros().get(position).getIznos() + settingValue.getCurrency_format());

    }

    @Override
    public int getItemCount() {
        return serviceModel.getTros().size();
    }





    class ServicesDetailHolder extends RecyclerView.ViewHolder {

        TextView tv_tipServisa;
        TextView tv_vrijednost;


        ServicesDetailHolder(View itemView) {
            super(itemView);

            tv_tipServisa = (TextView) itemView.findViewById(R.id.tip_servis_tv);
            tv_vrijednost = (TextView) itemView.findViewById(R.id.vrijednost_tv);
        }


    }
}
