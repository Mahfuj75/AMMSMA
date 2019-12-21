
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("P_ProgramOfficerId")
    @Expose
    private Integer programOfficerId;
    @SerializedName("P_GroupTypeId")
    @Expose
    private Integer groupTypeId;
    @SerializedName("P_DefaultProgramId")
    @Expose
    private Integer defaultProgramId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Village")
    @Expose
    private String village;
    @SerializedName("MeetingDay")
    @Expose
    private Integer meetingDay;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("MinimumSavingsDeposit")
    @Expose
    private Float minimumSavingsDeposit;
    @SerializedName("MinimumSecurityDeposit")
    @Expose
    private Float minimumSecurityDeposit;
    @SerializedName("GroupLeaderName")
    @Expose
    private String groupLeaderName;
    @SerializedName("GroupLeaderAddress")
    @Expose
    private String groupLeaderAddress;
    @SerializedName("GroupLeaderPhone")
    @Expose
    private String groupLeaderPhone;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public Integer getProgramOfficerId() {
        return programOfficerId;
    }

    public Integer getGroupTypeId() {
        return groupTypeId;
    }

    public Integer getDefaultProgramId() {
        return defaultProgramId;
    }

    public String getName() {
        return name;
    }

    public String getVillage() {
        return village;
    }

    public Integer getMeetingDay() {
        return meetingDay;
    }

    public Integer getStatus() {
        return status;
    }

    public Float getMinimumSavingsDeposit() {
        return minimumSavingsDeposit;
    }

    public Float getMinimumSecurityDeposit() {
        return minimumSecurityDeposit;
    }

    public String getGroupLeaderName() {
        return groupLeaderName;
    }

    public String getGroupLeaderAddress() {
        return groupLeaderAddress;
    }

    public String getGroupLeaderPhone() {
        return groupLeaderPhone;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setDefaultProgramId(Integer defaultProgramId) {
        this.defaultProgramId = defaultProgramId;
    }
}
