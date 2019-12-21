package asa.org.bd.ammsma.extra;

/**
 * Created by Mahfuj75 on 7/11/2017.
 */

public class RealizedGroupData {
    private int groupId;
    private String groupName;
    private String meetingDay;
    private float loanCollection;
    private float savingsDeposit;
    private float savingsDepositWithoutLts;
    private float ltsDeposit;
    private float cbsDeposit;
    private float savingsWithdrawal;
    private float cbsWithdrawal;
    private float badDebtCollection;
    private float exemptionTotal;
    private float totalCollection;
    private float totalWithdrawal;
    private float netCollection;
    private float loanRealizable;


    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public float getLoanCollection() {
        return loanCollection;
    }

    public void setLoanCollection(float loanCollection) {
        this.loanCollection = loanCollection;
    }

    public float getSavingsDeposit() {
        return savingsDeposit;
    }

    public void setSavingsDeposit(float savingsDeposit) {
        this.savingsDeposit = savingsDeposit;
    }

    public float getSavingsDepositWithoutLts() {
        return savingsDepositWithoutLts;
    }

    public void setSavingsDepositWithoutLts(float savingsDepositWithoutLts) {
        this.savingsDepositWithoutLts = savingsDepositWithoutLts;
    }

    public float getLtsDeposit() {
        return ltsDeposit;
    }

    public void setLtsDeposit(float ltsDeposit) {
        this.ltsDeposit = ltsDeposit;
    }

    public float getCbsDeposit() {
        return cbsDeposit;
    }

    public void setCbsDeposit(float cbsDeposit) {
        this.cbsDeposit = cbsDeposit;
    }

    public float getSavingsWithdrawal() {
        return savingsWithdrawal;
    }

    public void setSavingsWithdrawal(float savingsWithdrawal) {
        this.savingsWithdrawal = savingsWithdrawal;
    }

    public float getCbsWithdrawal() {
        return cbsWithdrawal;
    }

    public void setCbsWithdrawal(float cbsWithdrawal) {
        this.cbsWithdrawal = cbsWithdrawal;
    }

    public float getBadDebtCollection() {
        return badDebtCollection;
    }

    public void setBadDebtCollection(float badDebtCollection) {
        this.badDebtCollection = badDebtCollection;
    }

    public float getExemptionTotal() {
        return exemptionTotal;
    }

    public void setExemptionTotal(float exemptionTotal) {
        this.exemptionTotal = exemptionTotal;
    }

    public float getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(float totalCollection) {
        this.totalCollection = totalCollection;
    }

    public float getNetCollection() {
        return netCollection;
    }

    public void setNetCollection(float netCollection) {
        this.netCollection = netCollection;
    }

    public String getMeetingDay() {
        return meetingDay;
    }

    public void setMeetingDay(String meetingDay) {
        this.meetingDay = meetingDay;
    }

    public float getTotalWithdrawal() {
        return totalWithdrawal;
    }

    public void setTotalWithdrawal(float totalWithdrawal) {
        this.totalWithdrawal = totalWithdrawal;
    }

    public float getLoanRealizable() {
        return loanRealizable;
    }

    public void setLoanRealizable(float loanRealizable) {
        this.loanRealizable = loanRealizable;
    }
}
