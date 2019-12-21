
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceCharge {

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
    @SerializedName("ServiceCharge")
    @Expose
    private Float serviceCharge;
    @SerializedName("StartingDate")
    @Expose
    private String startingDate;
    @SerializedName("EndingDate")
    @Expose
    private String endingDate;
    @SerializedName("DecliningServiceCharge")
    @Expose
    private Float decliningServiceCharge;
    @SerializedName("P_FundId")
    @Expose
    private Integer fundId;
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

    public Float getServiceCharge() {
        return serviceCharge;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public Float getDecliningServiceCharge() {
        return decliningServiceCharge;
    }

    public Integer getFundId() {
        return fundId;
    }

    public Integer getId() {
        return id;
    }
}
