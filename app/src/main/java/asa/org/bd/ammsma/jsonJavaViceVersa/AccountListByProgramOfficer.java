
package asa.org.bd.ammsma.jsonJavaViceVersa;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountListByProgramOfficer {

    @SerializedName("AccountList")
    @Expose
    private List<Account> accountList = null;
    @SerializedName("ProgramOfficerId")
    @Expose
    private Integer programOfficerId;
    @SerializedName("ProgramOfficerName")
    @Expose
    private String programOfficerName;
    @SerializedName("ProgramOfficerCode")
    @Expose
    private String programOfficerCode;

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
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
