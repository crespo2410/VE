package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleModel implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("naziv_vozila")
    @Expose
    private String nazivVozila;
    @SerializedName("naziv_vrste")
    @Expose
    private String nazivVrste;
    @SerializedName("naziv_podvrste")
    @Expose
    private String nazivPodvrste;
    @SerializedName("kapacitet_spremnika1")
    @Expose
    private String kapacitetSpremnika1;
    @SerializedName("kapacitet_spremnika2")
    @Expose
    private String kapacitetSpremnika2;
    @SerializedName("hibrid")
    @Expose
    private String hibrid;
    @SerializedName("registracija")
    @Expose
    private String registracija;
    @SerializedName("broj_sasije")
    @Expose
    private String brojSasije;
    @SerializedName("biljeske")
    @Expose
    private String biljeske;
    @SerializedName("naziv_proizvodaca")
    @Expose
    private String nazivProizvodaca;
    @SerializedName("naziv")
    @Expose
    private String naziv;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("godina")
    @Expose
    private Integer godina;
    public final static Parcelable.Creator<VehicleModel> CREATOR = new Creator<VehicleModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VehicleModel createFromParcel(Parcel in) {
            VehicleModel instance = new VehicleModel();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.nazivVozila = ((String) in.readValue((String.class.getClassLoader())));
            instance.nazivVrste = ((String) in.readValue((String.class.getClassLoader())));
            instance.nazivPodvrste = ((String) in.readValue((String.class.getClassLoader())));
            instance.kapacitetSpremnika1 = ((String) in.readValue((String.class.getClassLoader())));
            instance.kapacitetSpremnika2 = ((String) in.readValue((String.class.getClassLoader())));
            instance.hibrid = ((String) in.readValue((String.class.getClassLoader())));
            instance.registracija = ((String) in.readValue((String.class.getClassLoader())));
            instance.brojSasije = ((String) in.readValue((String.class.getClassLoader())));
            instance.biljeske = ((String) in.readValue((String.class.getClassLoader())));
            instance.nazivProizvodaca = ((String) in.readValue((String.class.getClassLoader())));
            instance.naziv = ((String) in.readValue((String.class.getClassLoader())));
            instance.tip = ((String) in.readValue((String.class.getClassLoader())));
            instance.godina = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public VehicleModel[] newArray(int size) {
            return (new VehicleModel[size]);
        }

    }
            ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazivVozila() {
        return nazivVozila;
    }

    public void setNazivVozila(String nazivVozila) {
        this.nazivVozila = nazivVozila;
    }

    public String getNazivVrste() {
        return nazivVrste;
    }

    public void setNazivVrste(String nazivVrste) {
        this.nazivVrste = nazivVrste;
    }

    public String getNazivPodvrste() {
        return nazivPodvrste;
    }

    public void setNazivPodvrste(String nazivPodvrste) {
        this.nazivPodvrste = nazivPodvrste;
    }

    public String getKapacitetSpremnika1() {
        return kapacitetSpremnika1;
    }

    public void setKapacitetSpremnika1(String kapacitetSpremnika1) {
        this.kapacitetSpremnika1 = kapacitetSpremnika1;
    }

    public String getKapacitetSpremnika2() {
        return kapacitetSpremnika2;
    }

    public void setKapacitetSpremnika2(String kapacitetSpremnika2) {
        this.kapacitetSpremnika2 = kapacitetSpremnika2;
    }

    public String getHibrid() {
        return hibrid;
    }

    public void setHibrid(String hibrid) {
        this.hibrid = hibrid;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

    public String getBrojSasije() {
        return brojSasije;
    }

    public void setBrojSasije(String brojSasije) {
        this.brojSasije = brojSasije;
    }

    public String getBiljeske() {
        return biljeske;
    }

    public void setBiljeske(String biljeske) {
        this.biljeske = biljeske;
    }

    public String getNazivProizvodaca() {
        return nazivProizvodaca;
    }

    public void setNazivProizvodaca(String nazivProizvodaca) {
        this.nazivProizvodaca = nazivProizvodaca;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getGodina() {
        return godina;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(nazivVozila);
        dest.writeValue(nazivVrste);
        dest.writeValue(nazivPodvrste);
        dest.writeValue(kapacitetSpremnika1);
        dest.writeValue(kapacitetSpremnika2);
        dest.writeValue(hibrid);
        dest.writeValue(registracija);
        dest.writeValue(brojSasije);
        dest.writeValue(biljeske);
        dest.writeValue(nazivProizvodaca);
        dest.writeValue(naziv);
        dest.writeValue(tip);
        dest.writeValue(godina);
    }

    public int describeContents() {
        return 0;
    }

}