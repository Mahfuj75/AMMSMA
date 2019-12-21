
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanTransaction {

    @SerializedName("P_AccountId")
    @Expose
    private Integer accountId;
    @SerializedName("Date")
    @Expose
    private Integer date;
    @SerializedName("Debit")
    @Expose
    private Float debit;
    @SerializedName("Status")
    @Expose
    private Boolean Status;



    public Integer getAccountId() {
        return accountId;
    }

    public Integer getDate() {
        return date;
    }

    public Float getDebit() {
        return debit;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public void setDebit(Float debit) {
        this.debit = debit;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }


}
