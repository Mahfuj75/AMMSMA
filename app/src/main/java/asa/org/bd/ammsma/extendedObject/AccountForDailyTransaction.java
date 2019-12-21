package asa.org.bd.ammsma.extendedObject;

import asa.org.bd.ammsma.extra.ProgramNameChange;
import asa.org.bd.ammsma.jsonJavaViceVersa.Account;


public class AccountForDailyTransaction extends Account {

    private double balance;
    private int installmentNumber;
    private int accountId;
    private float debit;
    private float credit;
    private float loanTransactionAmount;
    private boolean exemptedOrNot;
    private float baseInstallmentAmount;
    private int accountStatus;
    private int overdueAmount;
    private int flag;
    private String lastAccountDetailsDate;
    private int paymentNumber;
    private int advanceAmount;
    private int realizedToday;
    private float overDueAmountActual;

    private float installmentAmount;
    private int realizedPrevious;
    private float termOverDue;
    private ProgramNameChange programNameChange;
    private boolean isScheduled;
    private int missingLtsCount;
    private boolean withdrawPermission;


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public int getAccountId() {
        return accountId;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(int installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public float getDebit() {
        return debit;
    }

    public void setDebit(float debit) {
        this.debit = debit;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public float getLoanTransactionAmount() {
        return loanTransactionAmount;
    }

    public void setLoanTransactionAmount(float loanTransactionAmount) {
        this.loanTransactionAmount = loanTransactionAmount;
    }

    public boolean isExemptedOrNot() {
        return exemptedOrNot;
    }

    public void setExemptedOrNot(boolean exemptedOrNot) {
        this.exemptedOrNot = exemptedOrNot;
    }

    public float getBaseInstallmentAmount() {
        return baseInstallmentAmount;
    }

    public void setBaseInstallmentAmount(float installmentAmount) {
        this.baseInstallmentAmount = installmentAmount;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }


    public int getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(int overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getLastAccountDetailsDate() {
        return lastAccountDetailsDate;
    }

    public void setLastAccountDetailsDate(String lastAccountDetailsDate) {
        this.lastAccountDetailsDate = lastAccountDetailsDate;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public int getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(int advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public float getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(float installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public int getRealizedToday() {
        return realizedToday;
    }

    public void setRealizedToday(int realizedToday) {
        this.realizedToday = realizedToday;
    }

    public float getOverDueAmountActual() {
        return overDueAmountActual;
    }

    public void setOverDueAmountActual(float overDueAmountActual) {
        this.overDueAmountActual = overDueAmountActual;
    }

    public int getRealizedPrevious() {
        return realizedPrevious;
    }

    public void setRealizedPrevious(int realizedPrevious) {
        this.realizedPrevious = realizedPrevious;
    }


    public float getTermOverDue() {
        return termOverDue;
    }

    public void setTermOverDue(float termOverDue) {
        this.termOverDue = termOverDue;
    }

    public ProgramNameChange getProgramNameChange() {
        return programNameChange;
    }

    public void setProgramNameChange(ProgramNameChange programNameChange) {
        this.programNameChange = programNameChange;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }

    public int getMissingLtsCount() {
        return missingLtsCount;
    }

    public void setMissingLtsCount(int missingLtsCount) {
        this.missingLtsCount = missingLtsCount;
    }

    public boolean isWithdrawPermission() {
        return withdrawPermission;
    }

    public void setWithdrawPermission(boolean withdrawPermission) {
        this.withdrawPermission = withdrawPermission;
    }
}
