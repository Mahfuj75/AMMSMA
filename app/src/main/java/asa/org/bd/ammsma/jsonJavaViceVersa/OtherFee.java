
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherFee {

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
    @SerializedName("ShortName")
    @Expose
    private String shortName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Amount")
    @Expose
    private Float amount;
    @SerializedName("TransactionType")
    @Expose
    private Integer transactionType;
    @SerializedName("IsMandatory")
    @Expose
    private Boolean isMandatory;
    @SerializedName("WhileDisbursing")
    @Expose
    private Boolean whileDisbursing;
    @SerializedName("IsFixed")
    @Expose
    private Boolean isFixed;
    @SerializedName("IsExemptable")
    @Expose
    private Boolean isExemptable;
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

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public Float getAmount() {
        return amount;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public Boolean getMandatory() {
        return isMandatory;
    }

    public Boolean getWhileDisbursing() {
        return whileDisbursing;
    }

    public Boolean getFixed() {
        return isFixed;
    }

    public Boolean getExemptable() {
        return isExemptable;
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
}
