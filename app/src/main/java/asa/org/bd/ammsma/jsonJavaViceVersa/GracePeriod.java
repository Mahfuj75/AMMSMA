
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GracePeriod {

    @SerializedName("P_ProgramId")
    @Expose
    private Integer programId;
    @SerializedName("P_Duration")
    @Expose
    private Integer duration;
    @SerializedName("P_InstallmentType")
    @Expose
    private Integer installmentType;
    @SerializedName("Sex")
    @Expose
    private Integer sex;
    @SerializedName("GracePeriod")
    @Expose
    private Integer gracePeriod;
    @SerializedName("StartingDate")
    @Expose
    private String startingDate;
    @SerializedName("EndingDate")
    @Expose
    private String endingDate;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public Integer getProgramId() {
        return programId;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getInstallmentType() {
        return installmentType;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public Integer getId() {
        return id;
    }
}
