
package Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ok {

    @SerializedName("Obavijest")
    @Expose
    private String obavijest;

    public String getObavijest() {
        return obavijest;
    }

    public void setObavijest(String obavijest) {
        this.obavijest = obavijest;
    }

}