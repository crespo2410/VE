package Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherExpensesModel implements Parcelable{

    @SerializedName("id_ot")
    @Expose
    private String idOt;
    @SerializedName("vozilo_id")
    @Expose
    private String voziloId;
    @SerializedName("datum_troska")
    @Expose
    private Date datumTroska;
    @SerializedName("vrijeme_troska")
    @Expose
    private String vrijemeTroska;
    @SerializedName("km_trenutno")
    @Expose
    private String kmTrenutno;
    @SerializedName("biljeske")
    @Expose
    private String biljeske;
    @SerializedName("naziv_obrtnika")
    @Expose
    private String nazivObrtnika;
    @SerializedName("naziv_razloga")
    @Expose
    private String nazivRazloga;
    @SerializedName("tros")
    @Expose
    private ArrayList<ExpensesOtherExpensesModel> tros = new ArrayList<>();



    public String getIdOt() {
        return idOt;
    }

    public void setIdOt(String idOt) {
        this.idOt = idOt;
    }

    public String getVoziloId() {
        return voziloId;
    }

    public void setVoziloId(String voziloId) {
        this.voziloId = voziloId;
    }

    public Date getDatumTroska() {
        return datumTroska;
    }

    public void setDatumTroska(Date datumTroska) {
        this.datumTroska = datumTroska;
    }

    public String getVrijemeTroska() {
        return vrijemeTroska;
    }

    public void setVrijemeTroska(String vrijemeTroska) {
        this.vrijemeTroska = vrijemeTroska;
    }

    public String getKmTrenutno() {
        return kmTrenutno;
    }

    public void setKmTrenutno(String kmTrenutno) {
        this.kmTrenutno = kmTrenutno;
    }

    public String getBiljeske() {
        return biljeske;
    }

    public void setBiljeske(String biljeske) {
        this.biljeske = biljeske;
    }

    public String getNazivObrtnika() {
        return nazivObrtnika;
    }

    public void setNazivObrtnika(String nazivObrtnika) {
        this.nazivObrtnika = nazivObrtnika;
    }

    public String getNazivRazloga() {
        return nazivRazloga;
    }

    public void setNazivRazloga(String nazivRazloga) {
        this.nazivRazloga = nazivRazloga;
    }

    public ArrayList<ExpensesOtherExpensesModel> getTros() {
        return tros;
    }

    public void setTros(ArrayList<ExpensesOtherExpensesModel> tros) {
        this.tros = tros;
    }



    public OtherExpensesModel(){

    }

    public OtherExpensesModel(Parcel parcel){

        idOt = parcel.readString();
        voziloId = parcel.readString();
        datumTroska = (Date) parcel.readValue(Date.class.getClassLoader());
        vrijemeTroska = parcel.readString();
        kmTrenutno = parcel.readString();
        biljeske = parcel.readString();
        nazivObrtnika = parcel.readString();
        nazivRazloga = parcel.readString();
        tros = parcel.createTypedArrayList(ExpensesOtherExpensesModel.CREATOR);



    }



    public static final Parcelable.Creator<OtherExpensesModel> CREATOR = new Creator<OtherExpensesModel>() {
        @Override
        public OtherExpensesModel createFromParcel(Parcel parcel) {

            return new OtherExpensesModel(parcel);
        }

        @Override
        public OtherExpensesModel[] newArray(int i) {

            return new OtherExpensesModel[0];
        }
    };











    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(idOt);
        parcel.writeString(voziloId);
        parcel.writeValue(datumTroska);
        parcel.writeString(vrijemeTroska);
        parcel.writeString(kmTrenutno);
        parcel.writeString(biljeske);
        parcel.writeString(nazivObrtnika);
        parcel.writeString(nazivRazloga);
        parcel.writeTypedList(tros);



    }
}