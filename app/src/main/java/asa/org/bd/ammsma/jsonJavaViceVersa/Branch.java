
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Branch {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("DistrictId")
    @Expose
    private Integer districtId;

    @SerializedName("Id")
    @Expose
    private Integer id;

    @SerializedName("BranchType")
    @Expose
    private Integer branchType;

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBranchType() {
        return branchType;
    }
    //////////////////////////////////////////////

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
