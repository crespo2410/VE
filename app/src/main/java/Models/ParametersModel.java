package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ParametersModel {

    @SerializedName("datum_od")
    @Expose
    String dateOd;

    @SerializedName("datum_do")
    @Expose
    String dateDo;

    @SerializedName("vozilo_id")
    @Expose
    int vozilo_id;


    @SerializedName("id_servisa")
    @Expose
    int id_servisi;

    public String getDateOd() {
        return dateOd;
    }

    public void setDateOd(String dateOd) {
        this.dateOd = dateOd;
    }

    public String getDateDo() {
        return dateDo;
    }

    public void setDateDo(String dateDo) {
        this.dateDo = dateDo;
    }

    public int getVozilo_id() {
        return vozilo_id;
    }

    public void setVozilo_id(int vozilo_id) {
        this.vozilo_id = vozilo_id;
    }

    public int getId_servisi() {
        return id_servisi;
    }

    public void setId_servisi(int id_servisi) {
        this.id_servisi = id_servisi;
    }
}
