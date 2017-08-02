package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObrtniciModel {

    @SerializedName("naziv_obrtnika")
    @Expose
    private String nazivObrtnika;

    public String getNazivObrtnika() {
        return nazivObrtnika;
    }

    public void setNazivObrtnika(String nazivObrtnika) {
        this.nazivObrtnika = nazivObrtnika;
    }

}