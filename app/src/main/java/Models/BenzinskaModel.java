package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BenzinskaModel {

    @SerializedName("naziv_benzinske")
    @Expose
    private String nazivBenzinske;

    public String getNazivBenzinske() {
        return nazivBenzinske;
    }

    public void setNazivBenzinske(String nazivBenzinske) {
        this.nazivBenzinske = nazivBenzinske;
    }

}