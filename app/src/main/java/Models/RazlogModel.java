package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RazlogModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("naziv_razloga")
    @Expose
    private String nazivRazloga;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazivRazloga() {
        return nazivRazloga;
    }

    public void setNazivRazloga(String nazivRazloga) {
        this.nazivRazloga = nazivRazloga;
    }

}

