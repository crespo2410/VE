package Models;

import android.os.Parcel;
import android.os.Parcelable;




public class TocenjeParcelable implements Parcelable {

    private String id;
    private String voziloId;
    private String tocenjeDatum;
    private String tocenjeVrijeme;
    private String kmTrenutno;
    private String nazivGoriva;
    private String cijena;
    private String uzetoLitara;
    private String ukupniTrosak;
    private String benzinskaNaziv;
    private String razlogNaziv;
    private String biljeske;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoziloId() {
        return voziloId;
    }

    public void setVoziloId(String voziloId) {
        this.voziloId = voziloId;
    }

    public String getTocenjeDatum() {
        return tocenjeDatum;
    }

    public void setTocenjeDatum(String tocenjeDatum) {
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




    public static final Parcelable.Creator<TocenjeParcelable> CREATOR = new Creator<TocenjeParcelable>() {
        @Override
        public TocenjeParcelable createFromParcel(Parcel parcel) {

            TocenjeParcelable tocenjeParcelable = new TocenjeParcelable();
            tocenjeParcelable.setId(parcel.readString());
            tocenjeParcelable.setVoziloId(parcel.readString());
            tocenjeParcelable.setKmTrenutno(parcel.readString());
            tocenjeParcelable.setTocenjeDatum(parcel.readString());
            tocenjeParcelable.setTocenjeVrijeme(parcel.readString());
            tocenjeParcelable.setUkupniTrosak(parcel.readString());
            tocenjeParcelable.setUzetoLitara(parcel.readString());
            tocenjeParcelable.setCijena(parcel.readString());
            tocenjeParcelable.setRazlogNaziv(parcel.readString());
            tocenjeParcelable.setBenzinskaNaziv(parcel.readString());
            tocenjeParcelable.setNazivGoriva(parcel.readString());
            tocenjeParcelable.setBiljeske(parcel.readString());

            return tocenjeParcelable;
        }

        @Override
        public TocenjeParcelable[] newArray(int i) {
            return new TocenjeParcelable[i];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(voziloId);
        parcel.writeString(tocenjeDatum);
        parcel.writeString(tocenjeVrijeme);
        parcel.writeString(kmTrenutno);
        parcel.writeString(nazivGoriva);
        parcel.writeString(cijena);
        parcel.writeString(uzetoLitara);
        parcel.writeString(ukupniTrosak);
        parcel.writeString(benzinskaNaziv);
        parcel.writeString(razlogNaziv);
        parcel.writeString(biljeske);


    }
}
