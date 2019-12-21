package asa.org.bd.ammsma.extra;


public class GroupNameForSpinnerObject {
    private int groupId;
    private String groupName;
    private String dayName;
    private boolean realizedOrNot;
    private int totalMember;
    private boolean isMeetingDay;
    private int defaultProgramId;
    private int groupTypeId;
    private boolean badDebtOrNot;

    private String groupFullName;

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

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public boolean isRealizedOrNot() {
        return realizedOrNot;
    }

    public void setRealizedOrNot(boolean realizedOrNot) {
        this.realizedOrNot = realizedOrNot;
    }

    public int getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(int totalMember) {
        this.totalMember = totalMember;
    }

    public boolean isMeetingDay() {
        return isMeetingDay;
    }

    public void setMeetingDay(boolean isMeetingDay) {
        this.isMeetingDay = isMeetingDay;
    }

    public int getDefaultProgramId() {
        return defaultProgramId;
    }

    public void setDefaultProgramId(int defaultProgramId) {
        this.defaultProgramId = defaultProgramId;
    }

    public int getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(int groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public String getGroupFullName() {
        return groupFullName;
    }

    public void setGroupFullName(String groupFullName) {
        this.groupFullName = groupFullName;
    }

    public boolean isBadDebtOrNot() {
        return badDebtOrNot;
    }

    public void setBadDebtOrNot(boolean badDebtOrNot) {
        this.badDebtOrNot = badDebtOrNot;
    }
}
