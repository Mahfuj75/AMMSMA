
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("P_AcId")
    @Expose
    private Integer accountId;
    @SerializedName("BInsAmt")
    @Expose
    private Float baseInstallmentAmount;
    @SerializedName("InsAmt")
    @Expose
    private Float installmentAmount;
    @SerializedName("SchDate")
    @Expose
    private Integer scheduledDate;
    @SerializedName("NDate")
    @Expose
    private Integer nextDate;
    @SerializedName("Schd")
    @Expose
    private Boolean scheduled;
    @SerializedName("PaidAmt")
    @Expose
    private Float paidAmount;
    @SerializedName("AdvAmt")
    @Expose
    private Float advanceAmount;
    @SerializedName("OverAmt")
    @Expose
    private Float overDueAmount;
    @SerializedName("OutAmt")
    @Expose
    private Float outstandingAmount;
    @SerializedName("PriOutAmt")
    @Expose
    private Float principalOutstanding;
    @SerializedName("MINum")
    @Expose
    private Integer maxInstallmentNumber;
    @SerializedName("Id")
    @Expose
    private Integer id;



    public Integer getAccountId() {
        return accountId;
    }

    public Float getBaseInstallmentAmount() {
        return baseInstallmentAmount;
    }

    public Float getInstallmentAmount() {
        return installmentAmount;
    }

    public Integer getScheduledDate() {
        return scheduledDate;
    }

    public Integer getNextDate() {
        return nextDate;
    }

    public Boolean getScheduled() {
        return scheduled;
    }

    public Float getPaidAmount() {
        return paidAmount;
    }

    public Float getAdvanceAmount() {
        return advanceAmount;
    }

    public Float getOverDueAmount() {
        return overDueAmount;
    }

    public Float getOutstandingAmount() {
        return outstandingAmount;
    }

    public Float getPrincipalOutstanding() {
        return principalOutstanding;
    }

    public Integer getMaxInstallmentNumber() {
        return maxInstallmentNumber;
    }

    public Integer getId() {
        return id;
    }


    public void setBaseInstallmentAmount(Float baseInstallmentAmount) {
        this.baseInstallmentAmount = baseInstallmentAmount;
    }

    public void setOverDueAmount(Float overDueAmount) {
        this.overDueAmount = overDueAmount;

    }

    public void setMaxInstallmentNumber(Integer maxInstallmentNumber) {
        this.maxInstallmentNumber = maxInstallmentNumber;
    }

    public void setInstallmentAmount(Float installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public void setAdvanceAmount(Float advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setScheduledDate(Integer scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public void setNextDate(Integer nextDate) {
        this.nextDate = nextDate;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public void setPaidAmount(Float paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setOutstandingAmount(Float outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public void setPrincipalOutstanding(Float principalOutstanding) {
        this.principalOutstanding = principalOutstanding;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
