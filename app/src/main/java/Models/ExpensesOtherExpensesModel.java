package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpensesOtherExpensesModel implements Parcelable {

    @SerializedName("id_tot")
    @Expose
    private String idTot;
    @SerializedName("id_ostali_trosak")
    @Expose
    private String idOstaliTrosak;
    @SerializedName("naziv_ostalog_troska")
    @Expose
    private String nazivOstalogTroska;
    @SerializedName("iznos")
    @Expose
    private String iznos;




    public static final Parcelable.Creator<ExpensesOtherExpensesModel> CREATOR = new Creator<ExpensesOtherExpensesModel>() {
        @Override
        public ExpensesOtherExpensesModel createFromParcel(Parcel in) {
            ExpensesOtherExpensesModel s = new ExpensesOtherExpensesModel();
            s.setIdTot(in.readString());
            s.setIdOstaliTrosak(in.readString());
            s.setNazivOstalogTroska(in.readString());
            s.setIznos(in.readString());

            return s;

        }

        @Override
        public ExpensesOtherExpensesModel[] newArray(int size) {
            return new ExpensesOtherExpensesModel[size];
        }
    };




    public String getIdTot() {
        return idTot;
    }

    public void setIdTot(String idTot) {
        this.idTot = idTot;
    }

    public String getIdOstaliTrosak() {
        return idOstaliTrosak;
    }

    public void setIdOstaliTrosak(String idOstaliTrosak) {
        this.idOstaliTrosak = idOstaliTrosak;
    }

    public String getNazivOstalogTroska() {
        return nazivOstalogTroska;
    }

    public void setNazivOstalogTroska(String nazivOstalogTroska) {
        this.nazivOstalogTroska = nazivOstalogTroska;
    }

    public String getIznos() {
        return iznos;
    }

    public void setIznos(String iznos) {
        this.iznos = iznos;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {


        parcel.writeString(idTot);
        parcel.writeString(idOstaliTrosak);
        parcel.writeString(nazivOstalogTroska);
        parcel.writeString(iznos);

    }
}
