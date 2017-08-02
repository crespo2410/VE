package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistanceValidationModel {

    @SerializedName("poruka")
    @Expose
    private Boolean poruka;

    public Boolean getPoruka() {
        return poruka;
    }

    public void setPoruka(Boolean poruka) {
        this.poruka = poruka;
    }

}