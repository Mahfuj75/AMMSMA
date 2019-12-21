
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProgramOfficer {

    @SerializedName("Code")
    @Expose
    private Integer code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("StartingDate")
    @Expose
    private String startingDate;
    @SerializedName("EndingDate")
    @Expose
    private String endingDate;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }


    public void setCode(Integer code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
