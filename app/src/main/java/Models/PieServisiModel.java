package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PieServisiModel implements Parcelable
{

    @SerializedName("naziv_servisa")
    @Expose
    private String nazivServisa;
    @SerializedName("iznos")
    @Expose
    private Double iznos;
    @SerializedName("datum_servis")
    @Expose
    private Date datumServis;
    @SerializedName("dan_u_godini")
    @Expose
    private int danUGodini;
    public final static Parcelable.Creator<PieServisiModel> CREATOR = new Creator<PieServisiModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PieServisiModel createFromParcel(Parcel in) {
            PieServisiModel instance = new PieServisiModel();
            instance.nazivServisa = ((String) in.readValue((String.class.getClassLoader())));
            instance.iznos = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.datumServis = ((Date) in.readValue((Date.class.getClassLoader())));
            instance.danUGodini = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public PieServisiModel[] newArray(int size) {
            return (new PieServisiModel[size]);
        }

    }
            ;

    public String getNazivServisa() {
        return nazivServisa;
    }

    public void setNazivServisa(String nazivServisa) {
        this.nazivServisa = nazivServisa;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }

    public Date getDatumServis() {
        return datumServis;
    }

    public void setDatumServis(Date datumServis) {
        this.datumServis = datumServis;
    }

    public int getDanUGodini() {
        return danUGodini;
    }

    public void setDanUGodini(int danUGodini) {
        this.danUGodini = danUGodini;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(nazivServisa);
        dest.writeValue(iznos);
        dest.writeValue(datumServis);
        dest.writeValue(danUGodini);
    }

    public int describeContents() {
        return 0;
    }

}
