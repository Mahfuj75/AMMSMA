
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import asa.org.bd.ammsma.extra.MemberExtra;

public class Member {


    @SerializedName("P_GrpId")
    @Expose
    private Integer groupId;
    @SerializedName("P_ProId")
    @Expose
    private Integer programId;
    @SerializedName("PbNum")
    @Expose
    private Integer passbookNumber;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("FName")
    @Expose
    private String fatherName;
    @SerializedName("IsHus")
    @Expose
    private Boolean isHusband;
    @SerializedName("DOBirth")
    @Expose
    private String deathOfBirth;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("NIdNum")
    @Expose
    private String nIdNum;
    @SerializedName("AdmDate")
    @Expose
    private String admissionDate;
    @SerializedName("Sex")
    @Expose
    private Integer sex;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ReceiveDate")
    @Expose
    private Integer receiveDate;
    @SerializedName("ReceiveType")
    @Expose
    private Integer receiveType;
    @SerializedName("BirthRNum")
    @Expose
    private String BirthCertificateNumber;

    private String memberOldOrNew;

    private int admissionDateInteger;

    private MemberExtra memberExtra;
    private int updatePhone;
    private int updateNid;





    public Integer getGroupId() {
        return groupId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public Integer getPassbookNumber() {
        return passbookNumber;
    }

    public String getName() {
        return name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public Boolean getHusband() {
        return isHusband;
    }

    public String getDeathOfBirth() {
        return deathOfBirth;
    }

    public Integer getStatus() {
        return status;
    }

    public String getnIdNum() {
        return nIdNum;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public Integer getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getId() {
        return id;
    }




    public void setPassbookNumber(Integer passbookNumber) {
        this.passbookNumber = passbookNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }




    public void setId(Integer id) {
        this.id = id;
    }

    public void setMemberOldOrNew(String memberOldOrNew) {
        this.memberOldOrNew = memberOldOrNew;
    }

    public String getMemberOldOrNew() {
        return memberOldOrNew;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public void setHusband(Boolean husband) {
        isHusband = husband;
    }

    public void setDeathOfBirth(String deathOfBirth) {
        this.deathOfBirth = deathOfBirth;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setnIdNum(String nIdNum) {
        this.nIdNum = nIdNum;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAdmissionDateInteger() {
        return admissionDateInteger;
    }

    public void setAdmissionDateInteger(int admissionDateInteger) {
        this.admissionDateInteger = admissionDateInteger;
    }


    public MemberExtra getMemberExtra() {
        return memberExtra;
    }

    public void setMemberExtra(MemberExtra memberExtra) {
        this.memberExtra = memberExtra;
    }

    public Integer getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Integer receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Integer getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Integer receiveType) {
        this.receiveType = receiveType;
    }

    public int getUpdatePhone() {
        return updatePhone;
    }

    public void setUpdatePhone(int updatePhone) {
        this.updatePhone = updatePhone;
    }

    public int getUpdateNid() {
        return updateNid;
    }

    public void setUpdateNid(int updateNid) {
        this.updateNid = updateNid;
    }

    public String getBirthCertificateNumber() {
        return BirthCertificateNumber;
    }

    public void setBirthCertificateNumber(String birthCertificateNumber) {
        BirthCertificateNumber = birthCertificateNumber;
    }
}
