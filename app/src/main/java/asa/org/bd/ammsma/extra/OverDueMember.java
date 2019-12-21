package asa.org.bd.ammsma.extra;

public class OverDueMember {
    private String memberNameFull;
    private int memberId;
    private String oldOrNewMember;
    private String memberName;
    private int sex;
    private int passbookNumber;
    private double overDueAmount;
    private int groupId;
    private String groupName;
    private int accountId;
    private String accountOpeningDate;
    private double disbursedAmount;
    private double outstandingAmount;
    private String programName;
    private boolean isSupplementary;

    public OverDueMember() {
    }

    public String getMemberNameFull() {
        return memberNameFull;
    }

    public void setMemberNameFull(String memberNameFull) {
        this.memberNameFull = memberNameFull;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getOldOrNewMember() {
        return oldOrNewMember;
    }

    public void setOldOrNewMember(String oldOrNewMember) {
        this.oldOrNewMember = oldOrNewMember;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getPassbookNumber() {
        return passbookNumber;
    }

    public void setPassbookNumber(int passbookNumber) {
        this.passbookNumber = passbookNumber;
    }

    public double getOverDueAmount() {
        return overDueAmount;
    }

    public void setOverDueAmount(double overDueAmount) {
        this.overDueAmount = overDueAmount;
    }

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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public void setAccountOpeningDate(String accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    public double getDisbursedAmount() {
        return disbursedAmount;
    }

    public void setDisbursedAmount(double disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    public double getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(double outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public boolean isSupplementary() {
        return isSupplementary;
    }

    public void setSupplementary(boolean supplementary) {
        isSupplementary = supplementary;
    }
}
