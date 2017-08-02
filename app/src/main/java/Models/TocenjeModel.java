package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TocenjeModel implements Parcelable {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("vozilo_id")
    private int voziloId;

    @Expose
    @SerializedName("tocenje_datum")
    private Date tocenjeDatum;

    @Expose
    @SerializedName("tocenje_vrijeme")
    private String tocenjeVrijeme;

    @Expose
    @SerializedName("km_trenutno")
    private String kmTrenutno;

    @Expose
    @SerializedName("cijena")
    private String cijena;

    @Expose
    @SerializedName("ukupni_trosak")
    private String ukupniTrosak;

    @Expose
    @SerializedName("uzeto_litara")
    private String uzetoLitara;

    @Expose
    @SerializedName("naziv")
    private String nazivGoriva;

    @Expose
    @SerializedName("naziv_benzinske")
    private String benzinskaNaziv;

    @Expose
    @SerializedName("naziv_razloga")
    private String razlogNaziv;

    @Expose
    @SerializedName("biljeske")
    private String biljeske;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVoziloId() {
        return voziloId;
    }

    public void setVoziloId(int voziloId) {
        this.voziloId = voziloId;
    }

    public Date getTocenjeDatum() {
        return tocenjeDatum;
    }

    public void setTocenjeDatum(Date tocenjeDatum) {
        this.tocenjeDatum = tocenjeDatum;
    }

    public String getTocenjeVrijeme() {
        return tocenjeVrijeme;
    }

    public void setTocenjeVrijeme(String tocenjeVrijeme) {
        this.tocenjeVrijeme = tocenjeVrijeme;
    }

    public String getKmTrenutno() {
        return kmTrenutno;
    }

    public void setKmTrenutno(String kmTrenutno) {
        this.kmTrenutno = kmTrenutno;
    }

    public String getNazivGoriva() {
        return nazivGoriva;
    }

    public void setNazivGoriva(String nazivGoriva) {
        this.nazivGoriva = nazivGoriva;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }

    public String getUzetoLitara() {
        return uzetoLitara;
    }

    public void setUzetoLitara(String uzetoLitara) {
        this.uzetoLitara = uzetoLitara;
    }

    public String getUkupniTrosak() {
        return ukupniTrosak;
    }

    public void setUkupniTrosak(String ukupniTrosak) {
        this.ukupniTrosak = ukupniTrosak;
    }

    public String getBenzinskaNaziv() {
        return benzinskaNaziv;
    }

    public void setBenzinskaNaziv(String benzinskaNaziv) {
        this.benzinskaNaziv = benzinskaNaziv;
    }

    public String getRazlogNaziv() {
        return razlogNaziv;
    }

    public void setRazlogNaziv(String razlogNaziv) {
        this.razlogNaziv = razlogNaziv;
    }

    public String getBiljeske() {
        return biljeske;
    }

    public void setBiljeske(String biljeske) {
        this.biljeske = biljeske;
    }



    public static final Parcelable.Creator<TocenjeModel> CREATOR = new Creator<TocenjeModel>() {
        @Override
        public TocenjeModel createFromParcel(Parcel parcel) {

            TocenjeModel tocenjeParcelable = new TocenjeModel();

            tocenjeParcelable.setId(parcel.readString());
            tocenjeParcelable.setVoziloId(parcel.readInt());
            tocenjeParcelable.setTocenjeDatum((Date) parcel.readValue(Date.class.getClassLoader()));
            tocenjeParcelable.setTocenjeVrijeme(parcel.readString());
            tocenjeParcelable.setKmTrenutno(parcel.readString());
            tocenjeParcelable.setCijena(parcel.readString());
            tocenjeParcelable.setUkupniTrosak(parcel.readString());
            tocenjeParcelable.setUzetoLitara(parcel.readString());
            tocenjeParcelable.setNazivGoriva(parcel.readString());
            tocenjeParcelable.setBenzinskaNaziv(parcel.readString());
            tocenjeParcelable.setRazlogNaziv(parcel.readString());
            tocenjeParcelable.setBiljeske(parcel.readString());

            return tocenjeParcelable;
        }

        @Override
        public TocenjeModel[] newArray(int i) {
            return new TocenjeModel[i];
        }
    };













    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeInt(voziloId);
        parcel.writeValue(tocenjeDatum);
        parcel.writeString(tocenjeVrijeme);
        parcel.writeString(kmTrenutno);
        parcel.writeString(cijena);
        parcel.writeString(ukupniTrosak);
        parcel.writeString(uzetoLitara);
        parcel.writeString(nazivGoriva);
        parcel.writeString(benzinskaNaziv);
        parcel.writeString(razlogNaziv);
        parcel.writeString(biljeske);


    }
}
