
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProgramGroupType {

    @SerializedName("P_GroupTypeId")
    @Expose
    private Integer groupTypeId;
    @SerializedName("P_ProgramId")
    @Expose
    private Integer programId;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public Integer getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(Integer groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
