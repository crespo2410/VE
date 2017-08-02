
package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceExpensesModel implements Parcelable {

    @SerializedName("id_t")
    @Expose
    private String idT;
    @SerializedName("id_servisi")
    @Expose
    private String idServisi;
    @SerializedName("naziv_servisa")
    @Expose
    private String nazivServisa;
    @SerializedName("iznos")
    @Expose
    private String iznos;


    public static final Parcelable.Creator<ServiceExpensesModel> CREATOR = new Creator<ServiceExpensesModel>() {
        @Override
        public ServiceExpensesModel createFromParcel(Parcel in) {
            ServiceExpensesModel s = new ServiceExpensesModel();
            s.setIdT(in.readString());
            s.setIdServisi(in.readString());
            s.setNazivServisa(in.readString());
            s.setIznos(in.readString());

            return s;

        }

        @Override
        public ServiceExpensesModel[] newArray(int size) {
            return new ServiceExpensesModel[size];
        }
    };

    public String getIdT() {
        return idT;
    }

    public void setIdT(String idT) {
        this.idT = idT;
    }

    public String getIdServisi() {
        return idServisi;
    }

    public void setIdServisi(String idServisi) {
        this.idServisi = idServisi;
    }

    public String getNazivServisa() {
        return nazivServisa;
    }

    public void setNazivServisa(String nazivServisa) {
        this.nazivServisa = nazivServisa;
    }

    public String getIznos() {
        return iznos;
    }

    public void setIznos(String iznos) {
        this.iznos = iznos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(idT);
        parcel.writeString(idServisi);
        parcel.writeString(nazivServisa);
        parcel.writeString(iznos);

    }
}