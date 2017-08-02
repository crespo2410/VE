package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GorivoModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("naziv")
    @Expose
    private String naziv;
    @SerializedName("naziv_podvrste")
    @Expose
    private String naziv_podvrste;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getNazivPodvrstaGoriva() {
        return naziv_podvrste;
    }

    public void setNazivPodvrstaGoriva(String nazivPodvrstaGoriva) {
        this.naziv_podvrste = nazivPodvrstaGoriva;
    }

}