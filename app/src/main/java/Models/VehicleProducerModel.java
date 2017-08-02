package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleProducerModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("naziv_proizvodaca")
    @Expose
    private String nazivProizvodaca;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazivProizvodaca() {
        return nazivProizvodaca;
    }

    public void setNazivProizvodaca(String nazivProizvodaca) {
        this.nazivProizvodaca = nazivProizvodaca;
    }

}