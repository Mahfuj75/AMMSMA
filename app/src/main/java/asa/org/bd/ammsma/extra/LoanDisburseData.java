package asa.org.bd.ammsma.extra;

import java.util.List;


public class LoanDisburseData {
    private List<String> nameList;
    private List<Integer> idList;


    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> programsName) {
        this.nameList = programsName;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }
}
