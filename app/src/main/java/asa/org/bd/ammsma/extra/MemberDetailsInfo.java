package asa.org.bd.ammsma.extra;


public class MemberDetailsInfo {
    private int programId;
    private String programName;
    private String disburseOrSavingOpeningDate;
    private String disburseAmountWithServiceCharge;
    private String disbursePrincipal;
    private String outstandingAmountOrBalance;
    private String overdueAmount;
    private String installmentAmountOrMinimumDeposit;
    private String remainingInstallmentNumber;
    private String cycle;
    private int accountId;
    private boolean isSupplementary;
    private String advanceAmount;
    private String overdueAmountActual;
    private ProgramNameChange programNameChange;
    private float serviceChargeInterest;
    private int missingLtsPremium;


    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getDisburseOrSavingOpeningDate() {
        return disburseOrSavingOpeningDate;
    }

    public void setDisburseOrSavingOpeningDate(String disburseOrSavingOpeningDate) {
        this.disburseOrSavingOpeningDate = disburseOrSavingOpeningDate;
    }

    public String getDisburseAmountWithServiceCharge() {
        return disburseAmountWithServiceCharge;
    }

    public void setDisburseAmountWithServiceCharge(String disburseAmountWithServiceCharge) {
        this.disburseAmountWithServiceCharge = disburseAmountWithServiceCharge;
    }

    public String getDisbursePrincipal() {
        return disbursePrincipal;
    }

    public void setDisbursePrincipal(String disbursePrincipal) {
        this.disbursePrincipal = disbursePrincipal;
    }

    public String getOutstandingAmountOrBalance() {
        return outstandingAmountOrBalance;
    }

    public void setOutstandingAmountOrBalance(String outstandingAmountOrBalance) {
        this.outstandingAmountOrBalance = outstandingAmountOrBalance;
    }

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getInstallmentAmountOrMinimumDeposit() {
        return installmentAmountOrMinimumDeposit;
    }

    public void setInstallmentAmountOrMinimumDeposit(String installmentAmountOrMinimumDeposit) {
        this.installmentAmountOrMinimumDeposit = installmentAmountOrMinimumDeposit;
    }

    public String getRemainingInstallmentNumber() {
        return remainingInstallmentNumber;
    }

    public void setRemainingInstallmentNumber(String remainingInstallmentNumber) {
        this.remainingInstallmentNumber = remainingInstallmentNumber;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public boolean isSupplementary() {
        return isSupplementary;
    }

    public void setSupplementary(boolean supplementary) {
        isSupplementary = supplementary;
    }

    public String getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(String advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public String getOverdueAmountActual() {
        return overdueAmountActual;
    }

    public void setOverdueAmountActual(String overdueAmountActual) {
        this.overdueAmountActual = overdueAmountActual;
    }

    public ProgramNameChange getProgramNameChange() {
        return programNameChange;
    }

    public void setProgramNameChange(ProgramNameChange programNameChange) {
        this.programNameChange = programNameChange;
    }

    public float getServiceChargeInterest() {
        return serviceChargeInterest;
    }

    public void setServiceChargeInterest(float serviceChargeInterest) {
        this.serviceChargeInterest = serviceChargeInterest;
    }

    public int getMissingLtsPremium() {
        return missingLtsPremium;
    }

    public void setMissingLtsPremium(int missingLtsPremium) {
        this.missingLtsPremium = missingLtsPremium;
    }
}
