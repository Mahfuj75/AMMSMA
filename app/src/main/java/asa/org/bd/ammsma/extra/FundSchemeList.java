package asa.org.bd.ammsma.extra;

import java.util.List;


public class FundSchemeList {
    private List<String> fundListName;
    private List<Integer> fundIdList;
    private List<String> schemeListName;
    private List<Integer> schemeIdList;


    public List<String> getFundListName() {
        return fundListName;
    }

    public void setFundListName(List<String> fundListName) {
        this.fundListName = fundListName;
    }

    public List<String> getSchemeListName() {
        return schemeListName;
    }

    public void setSchemeListName(List<String> schemeListName) {
        this.schemeListName = schemeListName;
    }

    public List<Integer> getFundIdList() {
        return fundIdList;
    }

    public void setFundIdList(List<Integer> fundIdList) {
        this.fundIdList = fundIdList;
    }

    public List<Integer> getSchemeIdList() {
        return schemeIdList;
    }

    public void setSchemeIdList(List<Integer> schemeIdList) {
        this.schemeIdList = schemeIdList;
    }
}
