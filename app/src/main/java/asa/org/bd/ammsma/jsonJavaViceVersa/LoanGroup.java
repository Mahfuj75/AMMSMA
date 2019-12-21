
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanGroup {

    @SerializedName("P_GroupTypeId")
    @Expose
    private Integer groupTypeId;
    @SerializedName("P_LoanGroupProgramId")
    @Expose
    private Integer loanGroupProgramId;
    @SerializedName("P_ProgramId")
    @Expose
    private Integer programId;
    @SerializedName("P_DefaultInstallmentType")
    @Expose
    private Integer defaultInstallmentType;
    @SerializedName("P_DefaultDuration")
    @Expose
    private Integer defaultDuration;
    @SerializedName("DefaultSex")
    @Expose
    private Integer defaultSex;
    @SerializedName("IsSupplementary")
    @Expose
    private Boolean isSupplementary;
    @SerializedName("Id")
    @Expose
    private Integer id;




    public Integer getGroupTypeId() {
        return groupTypeId;
    }

    public Integer getLoanGroupProgramId() {
        return loanGroupProgramId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public Integer getDefaultInstallmentType() {
        return defaultInstallmentType;
    }

    public Integer getDefaultDuration() {
        return defaultDuration;
    }

    public Integer getDefaultSex() {
        return defaultSex;
    }

    public Boolean getSupplementary() {
        return isSupplementary;
    }

    public Integer getId() {
        return id;
    }
}
