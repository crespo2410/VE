package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LineGasModel implements Parcelable
{

    @SerializedName("cijena")
    @Expose
    private Double cijena;
    @SerializedName("iznos")
    @Expose
    private Double iznos;
    @SerializedName("datum")
    @Expose
    private Date formatted_date;
    @SerializedName("dan_u_godini")
    @Expose
    private String danUGodini;
    public final static Parcelable.Creator<LineGasModel> CREATOR = new Creator<LineGasModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LineGasModel createFromParcel(Parcel in) {
            LineGasModel instance = new LineGasModel();
            instance.cijena = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.iznos = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.formatted_date = ((Date) in.readValue((Date.class.getClassLoader())));
            instance.danUGodini = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public LineGasModel[] newArray(int size) {
            return (new LineGasModel[size]);
        }

    }
            ;

    public Double getCijena() {
        return cijena;
    }

    public void setCijena(Double cijena) {
        this.cijena = cijena;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }

    public Date getDatum() {
        return formatted_date;
    }

    public void setDatum(Date formatted_date) {
        this.formatted_date = formatted_date;
    }

    public String getDanUGodini() {
        return danUGodini;
    }

    public void setDanUGodini(String danUGodini) {
        this.danUGodini = danUGodini;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cijena);
        dest.writeValue(iznos);
        dest.writeValue(formatted_date);
        dest.writeValue(danUGodini);
    }

    public int describeContents() {
        return 0;
    }

}