package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PodvrstaModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("naziv_podvrste")
    @Expose
    private String nazivPodvrste;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazivPodvrste() {
        return nazivPodvrste;
    }

    public void setNazivPodvrste(String nazivPodvrste) {
        this.nazivPodvrste = nazivPodvrste;
    }

}