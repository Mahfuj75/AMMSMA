package asa.org.bd.ammsma.extra;

import java.util.ArrayList;

import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;


public class DynamicFieldForListViewObject {

    private ArrayList<AccountForDailyTransaction> loanAndCbsList;
    private ArrayList<AccountForDailyTransaction> loanList;
    private ArrayList<AccountForDailyTransaction> savingsList;
    private ArrayList<AccountForDailyTransaction> accountWithoutLong;
    private ArrayList<AccountForDailyTransaction> accountOnlyLong;
    private ArrayList<AccountForDailyTransaction> accountWithdrawal;
    /*private ArrayList<MemberDetailsInfo> memberAccountDetails;*/


    public ArrayList<AccountForDailyTransaction> getLoanAndCbsList() {
        return loanAndCbsList;
    }

    public void setLoanAndCbsList(ArrayList<AccountForDailyTransaction> loanAndCbsList) {
        this.loanAndCbsList = loanAndCbsList;
    }

    public ArrayList<AccountForDailyTransaction> getSavingsList() {
        return savingsList;
    }

    public void setSavingsList(ArrayList<AccountForDailyTransaction> savingsList) {
        this.savingsList = savingsList;
    }

    public ArrayList<AccountForDailyTransaction> getAccountWithoutLong() {
        return accountWithoutLong;
    }

    public void setAccountWithoutLong(ArrayList<AccountForDailyTransaction> accountWithoutLong) {
        this.accountWithoutLong = accountWithoutLong;
    }

    public ArrayList<AccountForDailyTransaction> getAccountOnlyLong() {
        return accountOnlyLong;
    }

    public void setAccountOnlyLong(ArrayList<AccountForDailyTransaction> accountOnlyLong) {
        this.accountOnlyLong = accountOnlyLong;
    }

    public ArrayList<AccountForDailyTransaction> getAccountWithdrawal() {
        return accountWithdrawal;
    }

    public void setAccountWithdrawal(ArrayList<AccountForDailyTransaction> accountWithdrawal) {
        this.accountWithdrawal = accountWithdrawal;
    }

    public ArrayList<AccountForDailyTransaction> getLoanList() {
        return loanList;
    }

    public void setLoanList(ArrayList<AccountForDailyTransaction> loanList) {
        this.loanList = loanList;
    }

   /* public ArrayList<MemberDetailsInfo> getMemberAccountDetails() {
        return memberAccountDetails;
    }

    public void setMemberAccountDetails(ArrayList<MemberDetailsInfo> memberAccountDetails) {
        this.memberAccountDetails = memberAccountDetails;
    }*/
}
