package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OstaliTroskoviStatistikaModel implements Parcelable
{

    @SerializedName("broj_ostalih_troskova")
    @Expose
    private String brojOstalihTroskova;
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
    public final static Parcelable.Creator<OstaliTroskoviStatistikaModel> CREATOR = new Creator<OstaliTroskoviStatistikaModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OstaliTroskoviStatistikaModel createFromParcel(Parcel in) {
            OstaliTroskoviStatistikaModel instance = new OstaliTroskoviStatistikaModel();
            instance.brojOstalihTroskova = ((String) in.readValue((String.class.getClassLoader())));
            instance.brojDana = ((String) in.readValue((String.class.getClassLoader())));
            instance.dnevniTroskovi = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.troskoviPoKm = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.ukupniTrosak = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public OstaliTroskoviStatistikaModel[] newArray(int size) {
            return (new OstaliTroskoviStatistikaModel[size]);
        }

    }
            ;

    public String getBrojOstalihTroskova() {
        return brojOstalihTroskova;
    }

    public void setBrojOstalihTroskova(String brojOstalihTroskova) {
        this.brojOstalihTroskova = brojOstalihTroskova;
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
        dest.writeValue(brojOstalihTroskova);
        dest.writeValue(brojDana);
        dest.writeValue(dnevniTroskovi);
        dest.writeValue(troskoviPoKm);
        dest.writeValue(ukupniTrosak);
    }

    public int describeContents() {
        return 0;
    }

}