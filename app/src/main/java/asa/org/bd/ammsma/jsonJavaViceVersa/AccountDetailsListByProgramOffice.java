
package asa.org.bd.ammsma.jsonJavaViceVersa;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountDetailsListByProgramOffice {

    @SerializedName("AccountDetailsList")
    @Expose
    private List<AccountDetails> accountDetailsList = null;
    @SerializedName("ProgramOfficerId")
    @Expose
    private Integer programOfficerId;

    public List<AccountDetails> getAccountDetailsList() {
        return accountDetailsList;
    }

    public void setAccountDetailsList(List<AccountDetails> accountDetailsList) {
        this.accountDetailsList = accountDetailsList;
    }

    public Integer getProgramOfficerId() {
        return programOfficerId;
    }

    public void setProgramOfficerId(Integer programOfficerId) {
        this.programOfficerId = programOfficerId;
    }

}
