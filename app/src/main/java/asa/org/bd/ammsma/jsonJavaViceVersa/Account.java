
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("P_MemberId")
    @Expose
    private Integer memberId;
    @SerializedName("P_ProgramId")
    @Expose
    private Integer programId;
    @SerializedName("P_ProgramTypeId")
    @Expose
    private Integer programTypeId;
    @SerializedName("P_Duration")
    @Expose
    private Integer duration;
    @SerializedName("P_InstallmentType")
    @Expose
    private Integer installmentType;
    @SerializedName("IsSupplementary")
    @Expose
    private Boolean isSupplementary;
    @SerializedName("Cycle")
    @Expose
    private Integer cycle;
    @SerializedName("OpeningDate")
    @Expose
    private Integer openingDate;
    @SerializedName("MemberSex")
    @Expose
    private Integer memberSex;
    @SerializedName("DisbursedAmount")
    @Expose
    private Float disbursedAmount;
    @SerializedName("ServiceChargeAmount")
    @Expose
    private Float serviceChargeAmount;
    @SerializedName("MinimumDeposit")
    @Expose
    private Float minimumDeposit;
    @SerializedName("MeetingDayOfWeek")
    @Expose
    private Integer meetingDayOfWeek;
    @SerializedName("MeetingDayOfMonth")
    @Expose
    private Integer meetingDayOfMonth;
    @SerializedName("OpeningDateValue")
    @Expose
    private String openingDateValue;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ProgramName")
    @Expose
    private String programName;

    @SerializedName("GracePeriod")
    @Expose
    private Integer gracePeriod;

    @SerializedName("P_SchemeId")
    @Expose
    private Integer schemeId;

    @SerializedName("FirstInstallmentDate")
    @Expose
    private String firstInstallmentDate;
    @SerializedName("Status")
    @Expose
    private Integer status;


    @SerializedName("P_FundId")
    @Expose
    private Integer fundId;

    @SerializedName("DisbursedAmountWithSC")
    @Expose
    private Float disbursedAmountWithSC;

    @SerializedName("LoanInsuranceAmount")
    @Expose
    private Float loanInsuranceAmount;


    @SerializedName("ReceiveDate")
    @Expose
    private Integer receiveDate;
    @SerializedName("ReceiveAmount")
    @Expose
    private Float receiveAmount;
    /////////////



    private int programOfficerId;

    private int flag ;



    public Integer getMemberId() {
        return memberId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public Integer getProgramTypeId() {
        return programTypeId;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getInstallmentType() {
        return installmentType;
    }

    public Boolean getSupplementary() {
        return isSupplementary;
    }

    public Integer getCycle() {
        return cycle;
    }

    public Integer getMemberSex() {
        return memberSex;
    }

    public Float getDisbursedAmount() {
        return disbursedAmount;
    }

    public Float getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public Float getMinimumDeposit() {
        return minimumDeposit;
    }

    public Integer getMeetingDayOfWeek() {
        return meetingDayOfWeek;
    }

    public Integer getMeetingDayOfMonth() {
        return meetingDayOfMonth;
    }

    public String getOpeningDateValue() {
        return openingDateValue;
    }

    public Integer getId() {
        return id;
    }

    public String getProgramName() {
        return programName;
    }

    public Integer getOpeningDate() {
        return openingDate;
    }

    public Integer getReceiveDate() {
        return receiveDate;
    }

    public Float getReceiveAmount() {
        return receiveAmount;
    }
    ////////////////////////////////////////


    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public void setProgramTypeId(Integer programTypeId) {
        this.programTypeId = programTypeId;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setInstallmentType(Integer installmentType) {
        this.installmentType = installmentType;
    }

    public void setSupplementary(Boolean supplementary) {
        isSupplementary = supplementary;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public void setMemberSex(Integer memberSex) {
        this.memberSex = memberSex;
    }

    public void setDisbursedAmount(Float disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public void setServiceChargeAmount(Float serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public void setMinimumDeposit(Float minimumDeposit) {
        this.minimumDeposit = minimumDeposit;
    }

    public void setMeetingDayOfWeek(Integer meetingDayOfWeek) {
        this.meetingDayOfWeek = meetingDayOfWeek;
    }

    public void setMeetingDayOfMonth(Integer meetingDayOfMonth) {
        this.meetingDayOfMonth = meetingDayOfMonth;
    }

    public void setOpeningDateValue(String openingDateValue) {
        this.openingDateValue = openingDateValue;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public void setOpeningDate(Integer openingDate) {
        this.openingDate = openingDate;
    }

    /////////////////////////////////////////////////////////////////////

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Integer getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(Integer schemeId) {
        this.schemeId = schemeId;
    }

    public String getFirstInstallmentDate() {
        return firstInstallmentDate;
    }

    public void setFirstInstallmentDate(String firstInstallmentDate) {
        this.firstInstallmentDate = firstInstallmentDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Float getDisbursedAmountWithSC() {
        return disbursedAmountWithSC;
    }

    public void setDisbursedAmountWithSC(Float disbursedAmountWithSC) {
        this.disbursedAmountWithSC = disbursedAmountWithSC;
    }

    public Float getLoanInsuranceAmount() {
        return loanInsuranceAmount;
    }

    public void setLoanInsuranceAmount(Float loanInsuranceAmount) {
        this.loanInsuranceAmount = loanInsuranceAmount;
    }


    public int getProgramOfficerId() {
        return programOfficerId;
    }

    public void setProgramOfficerId(int programOfficerId) {
        this.programOfficerId = programOfficerId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
