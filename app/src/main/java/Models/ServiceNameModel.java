package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceNameModel {

    @SerializedName("naziv_servisa")
    @Expose
    private String nazivServisa;

    public String getNazivServisa() {
        return nazivServisa;
    }

    public void setNazivServisa(String nazivServisa) {
        this.nazivServisa = nazivServisa;
    }

}