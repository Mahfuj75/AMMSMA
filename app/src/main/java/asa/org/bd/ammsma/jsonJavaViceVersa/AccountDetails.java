
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDetails {

    @SerializedName("AcId")
    @Expose
    private Integer accountId;
    @SerializedName("Dt")
    @Expose
    private Integer loanTransactionDate;
    @SerializedName("Amt")
    @Expose
    private Float amount;
    @SerializedName("Ty")
    @Expose
    private Integer type;
    @SerializedName("Pro")
    @Expose
    private Integer process;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public Integer getAccountId() {
        return accountId;
    }

    public Integer getLoanTransactionDate() {
        return loanTransactionDate;
    }

    public Float getAmount() {
        return amount;
    }

    public Integer getType() {
        return type;
    }

    public Integer getProcess() {
        return process;
    }

    public Integer getId() {
        return id;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setLoanTransactionDate(Integer loanTransactionDate) {
        this.loanTransactionDate = loanTransactionDate;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
