package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceStatistikaModel implements Parcelable
{

    @SerializedName("broj_servisa")
    @Expose
    private String brojServisa;
    @SerializedName("broj_dana")
    @Expose
    private String brojDana;
    @SerializedName("dnevni_troskovi")
    @Expose
    private Double dnevniTroskovi;
    @SerializedName("troskovi_po_km")
    @Expose
    private Double troskoviPoKm;
    @SerializedName("ukupni_trosak")
    @Expose
    private Double ukupniTrosak;
    public final static Parcelable.Creator<ServiceStatistikaModel> CREATOR = new Creator<ServiceStatistikaModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ServiceStatistikaModel createFromParcel(Parcel in) {
            ServiceStatistikaModel instance = new ServiceStatistikaModel();
            instance.brojServisa = ((String) in.readValue((String.class.getClassLoader())));
            instance.brojDana = ((String) in.readValue((String.class.getClassLoader())));
            instance.dnevniTroskovi = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.troskoviPoKm = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.ukupniTrosak = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public ServiceStatistikaModel[] newArray(int size) {
            return (new ServiceStatistikaModel[size]);
        }

    }
            ;

    public String getBrojServisa() {
        return brojServisa;
    }

    public void setBrojServisa(String brojServisa) {
        this.brojServisa = brojServisa;
    }

    public String getBrojDana() {
        return brojDana;
    }

    public void setBrojDana(String brojDana) {
        this.brojDana = brojDana;
    }

    public Double getDnevniTroskovi() {
        return dnevniTroskovi;
    }

    public void setDnevniTroskovi(Double dnevniTroskovi) {
        this.dnevniTroskovi = dnevniTroskovi;
    }

    public Double getTroskoviPoKm() {
        return troskoviPoKm;
    }

    public void setTroskoviPoKm(Double troskoviPoKm) {
        this.troskoviPoKm = troskoviPoKm;
    }

    public Double getUkupniTrosak() {
        return ukupniTrosak;
    }

    public void setUkupniTrosak(Double ukupniTrosak) {
        this.ukupniTrosak = ukupniTrosak;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(brojServisa);
        dest.writeValue(brojDana);
        dest.writeValue(dnevniTroskovi);
        dest.writeValue(troskoviPoKm);
        dest.writeValue(ukupniTrosak);
    }

    public int describeContents() {
        return 0;
    }

}