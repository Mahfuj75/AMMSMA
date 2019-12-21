
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountBalance {

    @SerializedName("P_AccountId")
    @Expose
    private Integer accountId;
    @SerializedName("Balance")
    @Expose
    private Float balance;
    @SerializedName("Debit")
    @Expose
    private Float debit;
    @SerializedName("Credit")
    @Expose
    private Float credit;
    @SerializedName("Date")
    @Expose
    private Integer date;
    @SerializedName("ProgramType")
    @Expose
    private Integer programType;

    private int flag;

    private String createdDayTime;


    public Integer getAccountId() {
        return accountId;
    }

    public Float getBalance() {
        return balance;
    }

    public Float getDebit() {
        return debit;
    }

    public Float getCredit() {
        return credit;
    }


    public Integer getProgramType() {
        return programType;
    }

    public Integer getDate() {
        return date;
    }

    //////////////////////////////////////////////////////////


    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public void setDebit(Float debit) {
        this.debit = debit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public void setProgramType(Integer programType) {
        this.programType = programType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getCreatedDayTime() {
        return createdDayTime;
    }

    public void setCreatedDayTime(String createdDayTime) {
        this.createdDayTime = createdDayTime;
    }
}
