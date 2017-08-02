package Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceModel implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @Expose
    @SerializedName("vozilo_id")
    private int voziloId;

    @SerializedName("datum_servis")
    @Expose
    private Date datumServis;
    @SerializedName("vrijeme_servis")
    @Expose
    private String vrijemeServis;
    @SerializedName("km_trenutno")
    @Expose
    private String kmTrenutno;
    @SerializedName("biljeske")
    @Expose
    private String biljeske;
    @SerializedName("naziv_obrtnika")
    @Expose
    private String nazivObrtnika;
    @SerializedName("tros")
    @Expose
    public ArrayList<ServiceExpensesModel> tros = new ArrayList<>();


    public ServiceModel(){

    }

    public ServiceModel(Parcel parcel){

        id = parcel.readString();
        voziloId = parcel.readInt();
        datumServis = (Date) parcel.readValue(Date.class.getClassLoader());
        vrijemeServis = parcel.readString();
        kmTrenutno = parcel.readString();
        biljeske = parcel.readString();
        nazivObrtnika = parcel.readString();
        tros = parcel.createTypedArrayList(ServiceExpensesModel.CREATOR);

    }


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

    public Date getDatumServis() {
        return datumServis;
    }

    public void setDatumServis(Date datumServis) {
        this.datumServis = datumServis;
    }

    public String getVrijemeServis() {
        return vrijemeServis;
    }

    public void setVrijemeServis(String vrijemeServis) {
        this.vrijemeServis = vrijemeServis;
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

    public ArrayList<ServiceExpensesModel> getTros() {
        return tros;
    }

    public ArrayList<ServiceExpensesModel> setTros(ArrayList<ServiceExpensesModel> tros) {
       return this.tros = tros;
    }






    public static final Parcelable.Creator<ServiceModel> CREATOR = new Creator<ServiceModel>() {
        @Override
        public ServiceModel createFromParcel(Parcel parcel) {

            return new ServiceModel(parcel);
        }

        @Override
        public ServiceModel[] newArray(int i) {

            return new ServiceModel[0];
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
        parcel.writeValue(datumServis);
        parcel.writeString(vrijemeServis);
        parcel.writeString(kmTrenutno);
        parcel.writeString(biljeske);
        parcel.writeString(nazivObrtnika);
        parcel.writeTypedList(tros);



    }
}
