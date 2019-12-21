package asa.org.bd.ammsma.extra;


import java.util.ArrayList;
import java.util.List;

import asa.org.bd.ammsma.jsonJavaViceVersa.Member;


public class MemberListInfo {

    private List<String> membersName = new ArrayList<>();
    private List<String> membersSex = new ArrayList<>();
    private List<Boolean> membersRealizedInfo = new ArrayList<>();
    private List<Boolean> membersPaidOrNot = new ArrayList<>();
    private List<Member> membersInfo = new ArrayList<>();
    private List<Boolean> membersTermOverDue = new ArrayList<>();
    private List<Boolean> memberNewOrOld = new ArrayList<>();
    private List<Boolean> memberHasLoanOrNot = new ArrayList<>();


    public MemberListInfo() {
    }

    public List<String> getMembersName() {
        return membersName;
    }

    public void setMembersName(List<String> membersName) {
        this.membersName = membersName;
    }

    public List<String> getMembersSex() {
        return membersSex;
    }

    public void setMembersSex(List<String> membersSex) {
        this.membersSex = membersSex;
    }

    public List<Boolean> getMembersRealizedInfo() {
        return membersRealizedInfo;
    }

    public void setMembersRealizedInfo(List<Boolean> membersRealizedInfo) {
        this.membersRealizedInfo = membersRealizedInfo;
    }

    public List<Boolean> getMembersPaidOrNot() {
        return membersPaidOrNot;
    }

    public void setMembersPaidOrNot(List<Boolean> membersPaidOrNot) {
        this.membersPaidOrNot = membersPaidOrNot;
    }

    public List<Member> getMembersInfo() {
        return membersInfo;
    }


    public List<Boolean> getMembersTermOverDue() {
        return membersTermOverDue;
    }

    public void setMembersTermOverDue(List<Boolean> membersTermOverDue) {
        this.membersTermOverDue = membersTermOverDue;
    }

    public void setMembersInfo(List<Member> membersInfo) {
        this.membersInfo = membersInfo;
    }

    public void updatePaidOrNotData(int position, boolean paidOrNot) {
        membersPaidOrNot.set(position, paidOrNot);
    }

    public List<Boolean> getMemberNewOrOld() {
        return memberNewOrOld;
    }

    public void setMemberNewOrOld(List<Boolean> memberNewOrOld) {
        this.memberNewOrOld = memberNewOrOld;
    }

    public List<Boolean> getMemberHasLoanOrNot() {
        return memberHasLoanOrNot;
    }

    public void setMemberHasLoanOrNot(List<Boolean> memberHasLoanOrNot) {
        this.memberHasLoanOrNot = memberHasLoanOrNot;
    }
}
