
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstallmentAmount {

    @SerializedName("P_ProgramId")
    @Expose
    private Integer programId;
    @SerializedName("P_Duration")
    @Expose
    private Integer duration;
    @SerializedName("P_InstallmentType")
    @Expose
    private Integer installmentType;
    @SerializedName("GracePeriod")
    @Expose
    private Integer gracePeriod;
    @SerializedName("Sex")
    @Expose
    private Integer sex;
    @SerializedName("CalculationMode")
    @Expose
    private Integer calculationMode;
    @SerializedName("BaseAmount")
    @Expose
    private Float baseAmount;
    @SerializedName("AmountPerBase")
    @Expose
    private Float amountPerBase;
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

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getCalculationMode() {
        return calculationMode;
    }

    public Float getBaseAmount() {
        return baseAmount;
    }

    public Float getAmountPerBase() {
        return amountPerBase;
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

    public void setCalculationMode(Integer calculationMode) {
        this.calculationMode = calculationMode;
    }

    public void setBaseAmount(Float baseAmount) {
        this.baseAmount = baseAmount;
    }

    public void setAmountPerBase(Float amountPerBase) {
        this.amountPerBase = amountPerBase;
    }
}
