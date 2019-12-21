package asa.org.bd.ammsma.extra;

/**
 * Created by Mahfujur Rahman Khan on 02/12/2018.
 */

public class ProgramNameChange {

    private int id;
    private String shortName;
    private String changedName;
    private String validName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getChangedName() {
        return changedName;
    }

    public void setChangedName(String changedName) {
        this.changedName = changedName;
    }

    public String getValidName() {
        return validName;
    }

    public void setValidName(String validName) {
        this.validName = validName;
    }
}
