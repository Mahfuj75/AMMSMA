
package asa.org.bd.ammsma.jsonJavaViceVersa.main;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import asa.org.bd.ammsma.extra.NewMember;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountBalanceListByProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountDetailsListByProgramOffice;
import asa.org.bd.ammsma.jsonJavaViceVersa.Account;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountBalance;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountDetails;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountListByProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.Branch;
import asa.org.bd.ammsma.jsonJavaViceVersa.Calendar;
import asa.org.bd.ammsma.jsonJavaViceVersa.Duration;
import asa.org.bd.ammsma.jsonJavaViceVersa.Fund;
import asa.org.bd.ammsma.jsonJavaViceVersa.GracePeriod;
import asa.org.bd.ammsma.jsonJavaViceVersa.Group;
import asa.org.bd.ammsma.jsonJavaViceVersa.InstallmentAmount;
import asa.org.bd.ammsma.jsonJavaViceVersa.InstallmentCount;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanGroupDuration;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanGroup;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanGroupInstallment;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanTransaction;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;
import asa.org.bd.ammsma.jsonJavaViceVersa.OtherFee;
import asa.org.bd.ammsma.jsonJavaViceVersa.Program;
import asa.org.bd.ammsma.jsonJavaViceVersa.ProgramGroupType;
import asa.org.bd.ammsma.jsonJavaViceVersa.ProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.Schedule;
import asa.org.bd.ammsma.jsonJavaViceVersa.ScheduleListByProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.Scheme;
import asa.org.bd.ammsma.jsonJavaViceVersa.ServiceCharge;
import asa.org.bd.ammsma.jsonJavaViceVersa.User;

public class JsonJavaViceVersaImport {


    @SerializedName("Branch")
    @Expose
    private Branch branch;
    @SerializedName("P_InstallmentAmountList")
    @Expose
    private List<InstallmentAmount> installmentAmount = null;
    @SerializedName("P_InstallmentCountList")
    @Expose
    private List<InstallmentCount> installmentCount = new ArrayList<>();
    @SerializedName("P_LoanGroupList")
    @Expose
    private List<LoanGroup> loanGroup = new ArrayList<>();
    @SerializedName("P_ProgramGroupTypeList")
    @Expose
    private List<ProgramGroupType> programGroupType = new ArrayList<>();
    @SerializedName("P_ServiceChargeList")
    @Expose
    private List<ServiceCharge> serviceCharge = new ArrayList<>();
    @SerializedName("P_OtherFeeList")
    @Expose
    private List<OtherFee> otherFee = new ArrayList<>();
    @SerializedName("P_DurationList")
    @Expose
    private List<Duration> duration = new ArrayList<>();
    @SerializedName("P_GracePeriodList")
    @Expose
    private List<GracePeriod> gracePeriod = new ArrayList<>();
    @SerializedName("CalendarList")
    @Expose
    private List<Calendar> calendarList = new ArrayList<>();
    @SerializedName("UserList")
    @Expose
    private List<User> userList = new ArrayList<>();
    @SerializedName("ProgramOfficerList")
    @Expose
    private List<ProgramOfficer> programOfficerList = new ArrayList<>();
    @SerializedName("GroupLists")
    @Expose
    private List<Group> groups = new ArrayList<>();
    @SerializedName("Version")
    @Expose
    private String version;
    @SerializedName("LoanGroupDurationList")
    @Expose
    private List<LoanGroupDuration> loanGroupDurationList = new ArrayList<>();
    @SerializedName("LoanGroupInstallmentList")
    @Expose
    private List<LoanGroupInstallment> loanGroupInstallmentList = new ArrayList<>();
    @SerializedName("P_AccountLists")
    @Expose
    private List<Account> accounts = new ArrayList<>();
    @SerializedName("P_ScheduleLists")
    @Expose
    private Object pScheduleLists;
    @SerializedName("MemberLists")
    @Expose
    private Object memberLists;
    @SerializedName("P_AccountBalanceLists")
    @Expose
    private List<AccountBalance> accountBalances = new ArrayList<>();
    @SerializedName("ProgramLists")
    @Expose
    private List<Program> programs = new ArrayList<>();
    @SerializedName("P_LoanTransactionList")
    @Expose
    private List<LoanTransaction> loanTransaction = new ArrayList<>();
    @SerializedName("P_SchViewList")
    @Expose
    private List<Schedule> schedule = new ArrayList<>();
    @SerializedName("P_AcDetList")
    @Expose
    private List<AccountDetails> accountDetails = new ArrayList<>();
    @SerializedName("P_MemberViewList")
    @Expose
    private List<Member> members = new ArrayList<>();


    @SerializedName("P_FundList")
    @Expose
    private List<Fund> funds = new ArrayList<>();

    @SerializedName("P_SchemeList")
    @Expose
    private List<Scheme> schemes = new ArrayList<>();



    @SerializedName("P_AcDetListByProgramOffice")
    @Expose
    private List<AccountDetailsListByProgramOffice> accountDetailsListByProgramOffice = new ArrayList<>();

    @SerializedName("P_AccountListByProgramOfficer")
    @Expose
    private List<AccountListByProgramOfficer> accountListByProgramOfficer = new ArrayList<>();


    @SerializedName("P_SchViewListByProgramOfficer")
    @Expose
    private List<ScheduleListByProgramOfficer> scheduleListByProgramOfficer = new ArrayList<>();


    @SerializedName("P_AccountBalanceListByProgramOfficer")
    @Expose
    private List<AccountBalanceListByProgramOfficer> accountBalanceListByProgramOfficer = null;

    @SerializedName("MemberIdsWithoutLoan")
    @Expose
    private List<Integer> memberIdsWithoutLoan = null;

    /////////////////////////////////////////////////////////////////////////////
    private List<NewMember> newMembers = new ArrayList<>();


    public Branch getBranch() {
        return branch;
    }

    public List<InstallmentAmount> getInstallmentAmount() {
        return installmentAmount;
    }

    public List<InstallmentCount> getInstallmentCount() {
        return installmentCount;
    }

    public List<LoanGroup> getLoanGroup() {
        return loanGroup;
    }

    public List<ProgramGroupType> getProgramGroupType() {
        return programGroupType;
    }

    public List<ServiceCharge> getServiceCharge() {
        return serviceCharge;
    }

    public List<OtherFee> getOtherFee() {
        return otherFee;
    }

    public List<Duration> getDuration() {
        return duration;
    }

    public List<GracePeriod> getGracePeriod() {
        return gracePeriod;
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<ProgramOfficer> getProgramOfficerList() {
        return programOfficerList;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String getVersion() {
        return version;
    }

    public List<LoanGroupDuration> getLoanGroupDurationList() {
        return loanGroupDurationList;
    }

    public List<LoanGroupInstallment> getLoanGroupInstallmentList() {
        return loanGroupInstallmentList;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<AccountBalance> getAccountBalances() {
        return accountBalances;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public List<LoanTransaction> getLoanTransaction() {
        return loanTransaction;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public List<AccountDetails> getAccountDetails() {
        return accountDetails;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Fund> getFunds() {
        return funds;
    }

    public List<Scheme> getSchemes() {
        return schemes;
    }

    ////////////////////////////////////////////////////////////////////////////

    public List<NewMember> getNewMembers() {
        return newMembers;
    }



/////////////////////////////////////////////////////////////////////////////////


    public List<AccountDetailsListByProgramOffice> getAccountDetailsListByProgramOffice() {
        return accountDetailsListByProgramOffice;
    }


    public List<AccountListByProgramOfficer> getAccountListByProgramOfficer() {
        return accountListByProgramOfficer;
    }


    public List<ScheduleListByProgramOfficer> getScheduleListByProgramOfficer() {
        return scheduleListByProgramOfficer;
    }

    public List<AccountBalanceListByProgramOfficer> getAccountBalanceListByProgramOfficer() {
        return accountBalanceListByProgramOfficer;
    }

    public List<Integer> getMemberIdsWithoutLoan() {
        return memberIdsWithoutLoan;
    }
}
