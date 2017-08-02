package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OstaliTroskoviNaziviModel {

    @SerializedName("naziv_ostalog_troska")
    @Expose
    private String nazivOstalogTroska;

    public String getNazivOstalogTroska() {
        return nazivOstalogTroska;
    }

    public void setNazivOstalogTroska(String nazivOstalogTroska) {
        this.nazivOstalogTroska = nazivOstalogTroska;
    }

}