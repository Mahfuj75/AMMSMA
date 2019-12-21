package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahfu on 12/16/2017.
 */

public class Scheme {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("P_SchemeCategoriesId")
    @Expose
    private Integer schemeCategoriesId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchemeCategoriesId() {
        return schemeCategoriesId;
    }

    public void setSchemeCategoriesId(Integer schemeCategoriesId) {
        this.schemeCategoriesId = schemeCategoriesId;
    }
}
