
package asa.org.bd.ammsma.jsonJavaViceVersa;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountBalanceListByProgramOfficer {

    @SerializedName("AccountBalanceList")
    @Expose
    private List<AccountBalance> accountBalanceList = new ArrayList<>();
    @SerializedName("ProgramOfficerId")
    @Expose
    private Integer programOfficerId;
    @SerializedName("ProgramOfficerName")
    @Expose
    private String programOfficerName;
    @SerializedName("ProgramOfficerCode")
    @Expose
    private String programOfficerCode;

    public List<AccountBalance> getAccountBalanceList() {
        return accountBalanceList;
    }

    public void setAccountBalanceList(List<AccountBalance> accountBalanceList) {
        this.accountBalanceList = accountBalanceList;
    }

    public Integer getProgramOfficerId() {
        return programOfficerId;
    }

    public void setProgramOfficerId(Integer programOfficerId) {
        this.programOfficerId = programOfficerId;
    }

    public String getProgramOfficerName() {
        return programOfficerName;
    }

    public void setProgramOfficerName(String programOfficerName) {
        this.programOfficerName = programOfficerName;
    }

    public String getProgramOfficerCode() {
        return programOfficerCode;
    }

    public void setProgramOfficerCode(String programOfficerCode) {
        this.programOfficerCode = programOfficerCode;
    }

}
