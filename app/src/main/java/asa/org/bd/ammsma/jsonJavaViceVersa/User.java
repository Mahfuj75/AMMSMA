
package asa.org.bd.ammsma.jsonJavaViceVersa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Login")
    @Expose
    private String login;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("ProgramOfficerId")
    @Expose
    private Integer programOfficerId;
    @SerializedName("Id")
    @Expose
    private Integer id;


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Integer getProgramOfficerId() {
        return programOfficerId;
    }
}
