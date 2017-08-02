package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HistoryModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("naziv")
    @Expose
    private String naziv;
    @SerializedName("km_trenutno")
    @Expose
    private String kmTrenutno;
    @SerializedName("datum")
    @Expose
    private Date formatted_date;
    @SerializedName("vrijeme")
    @Expose
    private String vrijeme;
    @SerializedName("iznos")
    @Expose
    private String iznos;
    public final static Parcelable.Creator<HistoryModel> CREATOR = new Creator<HistoryModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HistoryModel createFromParcel(Parcel in) {
            HistoryModel instance = new HistoryModel();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.naziv = ((String) in.readValue((String.class.getClassLoader())));
            instance.kmTrenutno = ((String) in.readValue((String.class.getClassLoader())));
            instance.formatted_date = ((Date) in.readValue((Date.class.getClassLoader())));
            instance.vrijeme = ((String) in.readValue((String.class.getClassLoader())));
            instance.iznos = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public HistoryModel[] newArray(int size) {
            return (new HistoryModel[size]);
        }

    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKmTrenutno() {
        return kmTrenutno;
    }

    public void setKmTrenutno(String kmTrenutno) {
        this.kmTrenutno = kmTrenutno;
    }

    public Date getDatum() {
        return formatted_date;
    }

    public void setDatum(Date formatted_date) {
        this.formatted_date = formatted_date;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getIznos() {
        return iznos;
    }

    public void setIznos(String iznos) {
        this.iznos = iznos;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(naziv);
        dest.writeValue(kmTrenutno);
        dest.writeValue(formatted_date);
        dest.writeValue(vrijeme);
        dest.writeValue(iznos);
    }

    public int describeContents() {
        return 0;
    }

}