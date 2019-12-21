
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanGroupInstallment {

    @SerializedName("P_GroupTypeId")
    @Expose
    private Integer groupTypeId;
    @SerializedName("P_LoanGroupProgramId")
    @Expose
    private Integer loanGroupProgramId;
    @SerializedName("P_ProgramId")
    @Expose
    private Integer programId;
    @SerializedName("P_Duration")
    @Expose
    private Integer duration;

    @SerializedName("P_InstallmentType")
    @Expose
    private Integer installmentType;
    @SerializedName("StartingDate")
    @Expose
    private String startingDate;
    @SerializedName("EndingDate")
    @Expose
    private String endingDate;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
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

    public Integer getDuration() {
        return duration;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Integer getId() {
        return id;
    }

    public Integer getInstallmentType() {
        return installmentType;
    }
}
