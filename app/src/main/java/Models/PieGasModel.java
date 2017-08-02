package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PieGasModel implements Parcelable
{

    @SerializedName("naziv")
    @Expose
    private String naziv;
    @SerializedName("iznos")
    @Expose
    private Double iznos;
    public final static Parcelable.Creator<PieGasModel> CREATOR = new Creator<PieGasModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PieGasModel createFromParcel(Parcel in) {
            PieGasModel instance = new PieGasModel();
            instance.naziv = ((String) in.readValue((String.class.getClassLoader())));
            instance.iznos = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public PieGasModel[] newArray(int size) {
            return (new PieGasModel[size]);
        }

    }
            ;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(naziv);
        dest.writeValue(iznos);
    }

    public int describeContents() {
        return 0;
    }

}