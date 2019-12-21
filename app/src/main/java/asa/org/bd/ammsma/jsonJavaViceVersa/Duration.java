
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Duration {

    @SerializedName("Duration")
    @Expose
    private Integer duration;

    public Integer getDuration() {
        return duration;
    }

}
