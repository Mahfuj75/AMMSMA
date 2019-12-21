
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Calendar {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("IsWeeklyHoliday")
    @Expose
    private Boolean isWeeklyHoliday;
    @SerializedName("IsSpecialHoliday")
    @Expose
    private Boolean isSpecialHoliday;
    @SerializedName("Id")
    @Expose
    private Integer id;



    public String getDate() {
        return date;
    }

    public Boolean getWeeklyHoliday() {
        return isWeeklyHoliday;
    }

    public Boolean getSpecialHoliday() {
        return isSpecialHoliday;
    }

    public Integer getId() {
        return id;
    }
}
