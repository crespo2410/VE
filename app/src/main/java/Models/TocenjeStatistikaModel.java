package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TocenjeStatistikaModel implements Parcelable
{

    @SerializedName("broj_tocenja")
    @Expose
    private String brojTocenja;
    @SerializedName("broj_dana")
    @Expose
    private Integer brojDana;
    @SerializedName("dnevni_troskovi")
    @Expose
    private Double dnevniTroskovi;
    @SerializedName("troskovi_po_km")
    @Expose
    private Double troskoviPoKm;
    @SerializedName("ukupni_trosak")
    @Expose
    private Double ukupniTrosak;
    @SerializedName("naziv_goriva")
    @Expose
    private String nazivGoriva;
    @SerializedName("uzeto_litara")
    @Expose
    private Double uzetoLitara;
    @SerializedName("pros_po_100km")
    @Expose
    private Double prosPo100km;
    @SerializedName("pros_po_km")
    @Expose
    private Double prosPoKm;
    public final static Parcelable.Creator<TocenjeStatistikaModel> CREATOR = new Creator<TocenjeStatistikaModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TocenjeStatistikaModel createFromParcel(Parcel in) {
            TocenjeStatistikaModel instance = new TocenjeStatistikaModel();
            instance.brojTocenja = ((String) in.readValue((String.class.getClassLoader())));
            instance.brojDana = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.dnevniTroskovi = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.troskoviPoKm = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.ukupniTrosak = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.nazivGoriva = ((String) in.readValue((String.class.getClassLoader())));
            instance.uzetoLitara = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.prosPo100km = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.prosPoKm = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public TocenjeStatistikaModel[] newArray(int size) {
            return (new TocenjeStatistikaModel[size]);
        }

    }
            ;

    public String getBrojTocenja() {
        return brojTocenja;
    }

    public void setBrojTocenja(String brojTocenja) {
        this.brojTocenja = brojTocenja;
    }

    public Integer getBrojDana() {
        return brojDana;
    }

    public void setBrojDana(Integer brojDana) {
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

    public String getNazivGoriva() {
        return nazivGoriva;
    }

    public void setNazivGoriva(String nazivGoriva) {
        this.nazivGoriva = nazivGoriva;
    }

    public Double getUzetoLitara() {
        return uzetoLitara;
    }

    public void setUzetoLitara(Double uzetoLitara) {
        this.uzetoLitara = uzetoLitara;
    }

    public Double getProsPo100km() {
        return prosPo100km;
    }

    public void setProsPo100km(Double prosPo100km) {
        this.prosPo100km = prosPo100km;
    }

    public Double getProsPoKm() {
        return prosPoKm;
    }

    public void setProsPoKm(Double prosPoKm) {
        this.prosPoKm = prosPoKm;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(brojTocenja);
        dest.writeValue(brojDana);
        dest.writeValue(dnevniTroskovi);
        dest.writeValue(troskoviPoKm);
        dest.writeValue(ukupniTrosak);
        dest.writeValue(nazivGoriva);
        dest.writeValue(uzetoLitara);
        dest.writeValue(prosPo100km);
        dest.writeValue(prosPoKm);
    }

    public int describeContents() {
        return 0;
    }

}