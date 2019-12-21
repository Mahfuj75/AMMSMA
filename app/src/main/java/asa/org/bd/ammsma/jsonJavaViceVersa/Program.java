
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Program {

    @SerializedName("P_ProgramTypeId")
    @Expose
    private Integer programTypeId;
    @SerializedName("ShortName")
    @Expose
    private String shortName;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("IsPrimary")
    @Expose
    private Boolean isPrimary;
    @SerializedName("IsLongTerm")
    @Expose
    private Boolean isLongTerm;
    @SerializedName("IsCollectionSheet")
    @Expose
    private Boolean isCollectionSheet;
    @SerializedName("StartingDate")
    @Expose
    private String startingDate;
    @SerializedName("EndingDate")
    @Expose
    private String endingDate;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public Integer getProgramTypeId() {
        return programTypeId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public Boolean getLongTerm() {
        return isLongTerm;
    }

    public Boolean getCollectionSheet() {
        return isCollectionSheet;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
