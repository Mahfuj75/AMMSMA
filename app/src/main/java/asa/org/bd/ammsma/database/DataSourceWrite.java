package asa.org.bd.ammsma.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.extra.NewMember;
import asa.org.bd.ammsma.jsonJavaViceVersa.Account;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountBalance;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountDetails;
import asa.org.bd.ammsma.jsonJavaViceVersa.Branch;
import asa.org.bd.ammsma.jsonJavaViceVersa.Calendar;
import asa.org.bd.ammsma.jsonJavaViceVersa.Duration;
import asa.org.bd.ammsma.jsonJavaViceVersa.Fund;
import asa.org.bd.ammsma.jsonJavaViceVersa.GracePeriod;
import asa.org.bd.ammsma.jsonJavaViceVersa.Group;
import asa.org.bd.ammsma.jsonJavaViceVersa.InstallmentAmount;
import asa.org.bd.ammsma.jsonJavaViceVersa.InstallmentCount;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanGroup;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanGroupDuration;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanGroupInstallment;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanTransaction;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;
import asa.org.bd.ammsma.jsonJavaViceVersa.OtherFee;
import asa.org.bd.ammsma.jsonJavaViceVersa.Program;
import asa.org.bd.ammsma.jsonJavaViceVersa.ProgramGroupType;
import asa.org.bd.ammsma.jsonJavaViceVersa.ProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.Schedule;
import asa.org.bd.ammsma.jsonJavaViceVersa.Scheme;
import asa.org.bd.ammsma.jsonJavaViceVersa.ServiceCharge;
import asa.org.bd.ammsma.jsonJavaViceVersa.User;
import asa.org.bd.ammsma.jsonJavaViceVersa.main.JsonJavaViceVersaImport;

public class DataSourceWrite {

    private DatabaseHelper helper;
    private SQLiteDatabase databaseWrite;
    private SQLiteDatabase databaseRead;
    private DataSourceOperationsCommon dataSourceOperationsCommon;
    private DataSourceRead dataSourceRead;
    private int openingDay;

    public DataSourceWrite(Context context) {
        helper = new DatabaseHelper(context);
        dataSourceOperationsCommon = new DataSourceOperationsCommon(context);
        dataSourceRead = new DataSourceRead(context);
        openingDay = new DataSourceOperationsCommon(context).getWorkingDay();


    }

    private void openWritableDatabase() {
        databaseWrite = helper.getWritableDatabase();
    }

    private void openReadableDatabase() {
        databaseRead = helper.getWritableDatabase();
    }

    private void close() {
        helper.close();
    }

    public int truncateAllTables() {
        this.openWritableDatabase();
        int flag = 0;

        try {


            databaseWrite.delete("P_Transaction", null, null);
            databaseWrite.delete("P_LoanRecord", null, null);
            databaseWrite.delete("P_TransactionHistory", null, null);


            databaseWrite.delete("P_Account", null, null);
            databaseWrite.delete("P_AccountBalance", null, null);
            databaseWrite.delete("P_AccountDetails", null, null);
            databaseWrite.delete("Branch", null, null);
            databaseWrite.delete("Calender", null, null);
            databaseWrite.delete("P_duration", null, null);
            databaseWrite.delete("P_GracePeriod", null, null);
            databaseWrite.delete("P_Group", null, null);
            databaseWrite.delete("P_InstallmentAmount", null, null);
            databaseWrite.delete("P_InstallmentCount", null, null);
            databaseWrite.delete("P_LoanGroup", null, null);
            databaseWrite.delete("P_LoanGroupDuration", null, null);
            databaseWrite.delete("P_LoanGroupInstallment", null, null);
            databaseWrite.delete("P_LoanTransaction", null, null);
            databaseWrite.delete("P_MemberView", null, null);
            databaseWrite.delete("P_OtherFee", null, null);
            databaseWrite.delete("P_Program", null, null);
            databaseWrite.delete("P_ProgramGroupType", null, null);
            databaseWrite.delete("P_ProgramOfficer", null, null);
            databaseWrite.delete("P_Schedule", null, null);
            databaseWrite.delete("P_ServiceCharge", null, null);
            databaseWrite.delete("User", null, null);


            databaseWrite.delete("TempRealizedGroup", null, null);
            databaseWrite.delete("P_MemberNew", null, null);


            databaseWrite.delete("P_Fund", null, null);
            databaseWrite.delete("P_Scheme", null, null);
            flag = 1;

        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        this.close();

        return flag;
    }

    public int dataInsert(JsonJavaViceVersaImport jsonToJava, int position) {


        this.openWritableDatabase();

        databaseWrite.beginTransaction();
        try {

            ContentValues contentValues = new ContentValues();

            for (Calendar calendar : jsonToJava.getCalendarList()) {
                String finalDate;
                String[] strArr = calendar.getDate().split("T");
                finalDate = strArr[0];
                String[] dateTrick = finalDate.split("-");
                finalDate = dateTrick[2] + "/" + dateTrick[1] + "/" + dateTrick[0];
                SimpleDateFormat dateFromDatabase = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                dateFromDatabase.setTimeZone(TimeZone.getTimeZone("GMT+6"));
                Date dfd = dateFromDatabase.parse(finalDate);
                SimpleDateFormat convertedDate = new SimpleDateFormat("EE", Locale.getDefault());
                convertedDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
                String day = convertedDate.format(dfd);

                int dayId;

                switch (day.toUpperCase()) {
                    case "SUN":
                        dayId = 0;
                        break;
                    case "MON":
                        dayId = 1;
                        break;
                    case "TUE":
                        dayId = 2;
                        break;
                    case "WED":
                        dayId = 3;
                        break;
                    case "THU":
                        dayId = 4;
                        break;
                    case "FRI":
                        dayId = 5;
                        break;
                    case "SAT":
                        dayId = 6;
                        break;
                    default:
                        dayId = 7;
                        break;
                }

                int longDate = (int) new DateAndDataConversion().dateToLong(finalDate);
                contentValues.put("Id", calendar.getId());
                contentValues.put("Date", longDate);
                contentValues.put("RealDate", finalDate);
                contentValues.put("Day", Integer.parseInt(dateTrick[2]));
                contentValues.put("Month", Integer.parseInt(dateTrick[1]));
                contentValues.put("Year", Integer.parseInt(dateTrick[0]));
                contentValues.put("DayShortName", day);
                contentValues.put("DayId", dayId);
                contentValues.put("IsWeeklyHoliday", calendar.getWeeklyHoliday());
                contentValues.put("IsSpecialHoliday", calendar.getSpecialHoliday());
                contentValues.put("OpenORClose", "Open");

                try {
                    databaseWrite.insert("Calender", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }


            contentValues = new ContentValues();


            for (Account account : jsonToJava.getAccountListByProgramOfficer().get(position).getAccountList()) {

                contentValues.put("P_MemberId", account.getMemberId());
                contentValues.put("P_ProgramId", account.getProgramId());
                contentValues.put("P_ProgramTypeId", account.getProgramTypeId());
                contentValues.put("P_Duration", account.getDuration());
                contentValues.put("P_InstallmentType", account.getInstallmentType());
                contentValues.put("IsSupplementary", account.getSupplementary());
                contentValues.put("Cycle", account.getCycle());
                contentValues.put("GracePeriod", account.getGracePeriod());
                contentValues.put("MemberSex", account.getMemberSex());
                contentValues.put("OpeningDate", account.getOpeningDate());
                contentValues.put("DisbursedAmount", account.getDisbursedAmount());
                contentValues.put("ServiceChargeAmount", account.getServiceChargeAmount());
                contentValues.put("MinimumDeposit", account.getMinimumDeposit());
                contentValues.put("MeetingDayOfWeek", account.getMeetingDayOfWeek());
                contentValues.put("MeetingDayOfMonth", account.getMeetingDayOfMonth());
                contentValues.put("Account_ID", account.getId());
                contentValues.put("ProgramName", account.getProgramName());
                contentValues.put("Flag", 0);
                contentValues.put("NewLoan", 0);
                contentValues.put("NewAccount", 0);
                contentValues.put("ReceiveDate", account.getReceiveDate());
                contentValues.put("ReceiveAmount", account.getReceiveAmount());

                try {
                    databaseWrite.insert("P_Account", null, contentValues);

                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }


            openReadableDatabase();

            int firstDay = 0;
            try {
                Cursor cursor = databaseRead.rawQuery(
                        "SELECT Date from Calender ORDER BY Calender_Id ASC", null);
                cursor.moveToFirst();


                if (cursor.getCount() == 0) {
                    firstDay = 1;
                } else {
                    firstDay = cursor.getInt(cursor.getColumnIndex("Date"));
                }
                cursor.close();
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }


            contentValues = new ContentValues();


            for (AccountBalance accountBalance : jsonToJava.getAccountBalanceListByProgramOfficer().get(position).getAccountBalanceList()) {
                contentValues.put("P_AccountId", accountBalance.getAccountId());
                contentValues.put("ProgramType", accountBalance.getProgramType());
                contentValues.put("Date", accountBalance.getDate());
                contentValues.put("Flag", 0);

                int programType = accountBalance.getProgramType();
                if (programType == 1 || programType == 8) {


                    if (firstDay == accountBalance.getDate()) {
                        contentValues.put("Debit", (accountBalance.getBalance()));
                    } else {
                        contentValues.put("Debit", (accountBalance.getBalance() - accountBalance.getCredit()));
                    }
                    contentValues.put("Credit", 0);
                    contentValues.put("Type", 0);


                    if (accountBalance.getCredit() > 0) {
                        ContentValues newContentValue = new ContentValues();

                        newContentValue.put("P_AccountId", accountBalance.getAccountId());
                        newContentValue.put("Date", accountBalance.getDate());
                        newContentValue.put("Debit", 0);
                        newContentValue.put("Credit", accountBalance.getCredit());
                        newContentValue.put("ProgramType", accountBalance.getProgramType());
                        newContentValue.put("Flag", 0);

                        if (programType == 1) {
                            newContentValue.put("Type", 4);
                        } else {
                            newContentValue.put("Type", 256);
                        }


                        try {
                            databaseWrite.insert("P_AccountBalance", null, newContentValue);
                        } catch (SQLException e) {
                            Log.i("SQL Error", e.toString());
                        }
                    }

                } else if (programType == 2 || programType == 4) {
                    Cursor checkReceivedAccount = databaseRead.rawQuery("SELECT CASE WHEN ReceiveDate IS NOT NULL AND ReceiveDate = " + firstDay + " THEN ReceiveAmount ELSE 0 END AS ReceiveAmount FROM P_Account WHERE Account_ID = " + accountBalance.getAccountId(), null);
                    double receivedAmount = 0;

                    checkReceivedAccount.moveToFirst();
                    if (checkReceivedAccount.getCount() > 0) {
                        receivedAmount = checkReceivedAccount.getFloat(checkReceivedAccount.getColumnIndex("ReceiveAmount"));
                    }


                    contentValues.put("Debit", 0);
                    if (receivedAmount > 0 && accountBalance.getBalance() == 0) {
                        contentValues.put("Credit", accountBalance.getBalance() + receivedAmount);
                    } else if (receivedAmount > 0 && accountBalance.getBalance() > 0) {
                        contentValues.put("Credit", receivedAmount);
                    } else {
                        contentValues.put("Credit", accountBalance.getBalance());
                    }
                    contentValues.put("Type", 0);

                    if (((accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() == 0)
                            || (accountBalance.getCredit() > 0 && accountBalance.getBalance() > 0 && receivedAmount == 0)
                            || (accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() > 0))
                            && accountBalance.getDebit() > 0) {
                        ContentValues newContentValueCredit = new ContentValues();
                        newContentValueCredit.put("P_AccountId", accountBalance.getAccountId());
                        newContentValueCredit.put("Date", accountBalance.getDate());
                        newContentValueCredit.put("Debit", 0);
                        if (accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() == 0) {
                            newContentValueCredit.put("Credit", accountBalance.getCredit() - receivedAmount);
                        } else if (accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() > 0) {
                            newContentValueCredit.put("Credit", accountBalance.getCredit() - receivedAmount);
                        } else if (receivedAmount == 0) {
                            newContentValueCredit.put("Credit", accountBalance.getCredit());
                        }

                        newContentValueCredit.put("ProgramType", programType);
                        newContentValueCredit.put("Flag", 0);


                        ContentValues newContentValueDebit = new ContentValues();
                        newContentValueDebit.put("P_AccountId", accountBalance.getAccountId());
                        newContentValueDebit.put("Date", accountBalance.getDate());
                        newContentValueDebit.put("Debit", accountBalance.getDebit());
                        newContentValueDebit.put("Credit", 0);
                        newContentValueDebit.put("ProgramType", programType);
                        newContentValueDebit.put("Flag", 0);


                        if (programType == 2) {
                            newContentValueCredit.put("Type", 1024);
                            newContentValueDebit.put("Type", 16386);
                        } else {
                            newContentValueCredit.put("Type", 131072);
                            newContentValueDebit.put("Type", 48576);
                        }
                        try {
                            databaseWrite.insert("P_AccountBalance", null, newContentValueCredit);
                            databaseWrite.insert("P_AccountBalance", null, newContentValueDebit);
                        } catch (SQLException e) {
                            Log.i("SQL Error", e.toString());
                        }
                    } else if ((accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() == 0)
                            || (accountBalance.getCredit() > 0 && accountBalance.getBalance() > 0 && receivedAmount == 0)
                            || (accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() > 0)) {
                        ContentValues newContentValueCredit = new ContentValues();
                        newContentValueCredit.put("P_AccountId", accountBalance.getAccountId());
                        newContentValueCredit.put("Date", accountBalance.getDate());
                        newContentValueCredit.put("Debit", 0);
                        if (accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() == 0) {
                            newContentValueCredit.put("Credit", accountBalance.getCredit() - receivedAmount);
                        } else if (accountBalance.getCredit() - receivedAmount > 0 && accountBalance.getBalance() > 0) {
                            newContentValueCredit.put("Credit", accountBalance.getCredit() - receivedAmount);
                        } else if (receivedAmount == 0) {
                            newContentValueCredit.put("Credit", accountBalance.getCredit());
                        }

                        newContentValueCredit.put("ProgramType", programType);
                        newContentValueCredit.put("Flag", 0);

                        if (programType == 2) {
                            newContentValueCredit.put("Type", 1024);
                        } else {
                            newContentValueCredit.put("Type", 131072);
                        }
                        try {
                            databaseWrite.insert("P_AccountBalance", null, newContentValueCredit);
                        } catch (SQLException e) {
                            Log.i("SQL Error", e.toString());
                        }
                    } else if (accountBalance.getDebit() > 0) {
                        ContentValues newContentValueDebit = new ContentValues();
                        newContentValueDebit.put("P_AccountId", accountBalance.getAccountId());
                        newContentValueDebit.put("Date", accountBalance.getDate());
                        newContentValueDebit.put("Debit", accountBalance.getDebit());
                        newContentValueDebit.put("Credit", 0);
                        newContentValueDebit.put("ProgramType", programType);
                        newContentValueDebit.put("Flag", 0);


                        if (programType == 2) {
                            newContentValueDebit.put("Type", 16386);
                        } else {
                            newContentValueDebit.put("Type", 48576);
                        }
                        try {
                            databaseWrite.insert("P_AccountBalance", null, newContentValueDebit);
                        } catch (SQLException e) {
                            Log.i("SQL Error", e.toString());
                        }
                    }

                    checkReceivedAccount.close();
                }

                try {
                    databaseWrite.insert("P_AccountBalance", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }

            }


            this.openReadableDatabase();
            Cursor cursor = databaseRead.rawQuery("SELECT P_Account.Account_ID AS AccountId , P_Account.OpeningDate AS OpeningDate , P_Account.P_ProgramTypeId AS ProgramTypeId , CASE WHEN   P_AccountBalance.P_AccountId IS NULL THEN 0 ELSE 1 END AS  Exist  FROM P_Account LEFT JOIN P_AccountBalance ON P_Account.Account_ID  =   P_AccountBalance.P_AccountId GROUP BY P_Account.Account_ID ORDER BY Exist", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getInt(cursor.getColumnIndex("Exist")) == 0) {
                    ContentValues newContentValueCredit = new ContentValues();
                    newContentValueCredit.put("P_AccountId", cursor.getInt(cursor.getColumnIndex("AccountId")));
                    newContentValueCredit.put("Date", cursor.getInt(cursor.getColumnIndex("OpeningDate")));
                    newContentValueCredit.put("Debit", 0);
                    newContentValueCredit.put("Credit", 0);
                    newContentValueCredit.put("ProgramType", cursor.getInt(cursor.getColumnIndex("ProgramTypeId")));
                    newContentValueCredit.put("Flag", 3);
                    newContentValueCredit.put("Type", 0);


                    try {
                        databaseWrite.insert("P_AccountBalance", null, newContentValueCredit);
                    } catch (SQLException e) {
                        Log.i("SQL Error", e.toString());
                    }
                } else {
                    break;
                }

                cursor.moveToNext();
            }
            cursor.close();


            contentValues = new ContentValues();

            for (AccountDetails accountDetails : jsonToJava.getAccountDetailsListByProgramOffice().get(position).getAccountDetailsList()) {
                contentValues.put("P_AccountId", accountDetails.getAccountId());
                contentValues.put("P_LoanTransactionDate", accountDetails.getLoanTransactionDate());
                contentValues.put("Type", accountDetails.getType());
                contentValues.put("Amount", accountDetails.getAmount());
                contentValues.put("Process", accountDetails.getProcess());
                contentValues.put("NewlyCreated", false);
                try {
                    databaseWrite.insert("P_AccountDetails", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }


            contentValues = new ContentValues();

            contentValues.clear();

            Branch branch = jsonToJava.getBranch();

            contentValues.put("Name", branch.getName());
            contentValues.put("Mobile", branch.getMobileNumber());
            contentValues.put("DistrictId", branch.getDistrictId());
            contentValues.put("BranchID", branch.getId());
            contentValues.put("BranchType",branch.getBranchType());
            String newVersionStr = jsonToJava.getVersion().replace(".", "");

            int version = Integer.parseInt(newVersionStr);
            String[] arr = String.valueOf(version).split("");
            String finalVersion = arr[1] + "." + arr[2] + "." + arr[3];

            contentValues.put("AMMSVersion", finalVersion);
            try {
                databaseWrite.insert("Branch", null, contentValues);
            } catch (SQLException e) {
                Log.i("SQL Error", e.toString());
            }


            contentValues = new ContentValues();


            for (Duration duration : jsonToJava.getDuration()) {
                contentValues.put("Duration", duration.getDuration());
                try {
                    databaseWrite.insert("P_Duration", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }


            contentValues = new ContentValues();


            for (GracePeriod gracePeriod : jsonToJava.getGracePeriod()) {
                contentValues.put("Id", gracePeriod.getId());
                contentValues.put("P_ProgramId", gracePeriod.getProgramId());
                contentValues.put("P_Duration", gracePeriod.getDuration());
                contentValues.put("P_InstallmentType", gracePeriod.getInstallmentType());
                contentValues.put("Sex", gracePeriod.getSex());
                contentValues.put("GracePeriod", gracePeriod.getGracePeriod());
                contentValues.put("StartingDate", new DateAndDataConversion().dateFormationLongType(gracePeriod.getStartingDate()));
                contentValues.put("EndingDate", new DateAndDataConversion().dateFormationLongType(gracePeriod.getEndingDate()));
                try {
                    databaseWrite.insert("P_GracePeriod", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (Group group : jsonToJava.getGroups()) {
                contentValues.put("P_ProgramOfficerId", group.getProgramOfficerId());
                contentValues.put("P_GroupTypeId", group.getGroupTypeId());
                contentValues.put("P_DefaultProgramId", group.getDefaultProgramId());
                contentValues.put("Name", group.getName());
                contentValues.put("Village", group.getVillage());
                contentValues.put("MeetingDay", group.getMeetingDay());
                contentValues.put("Status", group.getStatus());
                contentValues.put("MinimumSavingsDeposit", group.getMinimumSavingsDeposit());
                contentValues.put("MinimumSecurityDeposit", group.getMinimumSecurityDeposit());
                contentValues.put("GroupLeaderName", group.getGroupLeaderName());
                contentValues.put("GroupLeaderAddress", group.getGroupLeaderAddress());
                contentValues.put("GroupLeaderPhone", group.getGroupLeaderPhone());
                contentValues.put("FullName", group.getFullName());
                contentValues.put("ID", group.getId());
                contentValues.put("Flag", 0);
                try {
                    databaseWrite.insert("P_Group", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (InstallmentAmount installmentAmount : jsonToJava.getInstallmentAmount()) {
                contentValues.put("Id", installmentAmount.getId());
                contentValues.put("P_ProgramId", installmentAmount.getProgramId());
                contentValues.put("P_Duration", installmentAmount.getDuration());
                contentValues.put("P_InstallmentType", installmentAmount.getInstallmentType());
                contentValues.put("GracePeriod", installmentAmount.getGracePeriod());
                contentValues.put("Sex", installmentAmount.getSex());
                contentValues.put("CalculationMode", installmentAmount.getCalculationMode());
                contentValues.put("BaseAmount", installmentAmount.getBaseAmount());
                contentValues.put("AmountPerBase", installmentAmount.getAmountPerBase());
                contentValues.put("StartingDate", new DateAndDataConversion().dateFormationLongType(installmentAmount.getStartingDate()));
                contentValues.put("EndingDate", new DateAndDataConversion().dateFormationLongType(installmentAmount.getEndingDate()));
                try {
                    databaseWrite.insert("P_InstallmentAmount", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (InstallmentCount installmentCount : jsonToJava.getInstallmentCount()) {
                contentValues.put("Id", installmentCount.getId());
                contentValues.put("P_ProgramId", installmentCount.getProgramId());
                contentValues.put("P_Duration", installmentCount.getDuration());
                contentValues.put("P_InstallmentType", installmentCount.getInstallmentType());
                contentValues.put("GracePeriod", installmentCount.getGracePeriod());
                contentValues.put("Sex", installmentCount.getSex());
                contentValues.put("InstallmentCount", installmentCount.getInstallmentCount());
                contentValues.put("StartingDate", new DateAndDataConversion().dateFormationLongType(installmentCount.getStartingDate()));
                contentValues.put("EndingDate", new DateAndDataConversion().dateFormationLongType(installmentCount.getEndingDate()));
                try {
                    databaseWrite.insert("P_InstallmentCount", null, contentValues);

                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (LoanGroup loanGroup : jsonToJava.getLoanGroup()) {
                contentValues.put("Id", loanGroup.getId());
                contentValues.put("P_GroupTypeId", loanGroup.getGroupTypeId());
                contentValues.put("P_LoanGroupProgramId", loanGroup.getLoanGroupProgramId());
                contentValues.put("P_ProgramId", loanGroup.getProgramId());
                contentValues.put("P_DefaultInstallmentType", loanGroup.getDefaultInstallmentType());
                contentValues.put("P_DefaultDuration", loanGroup.getDefaultDuration());
                contentValues.put("DefaultSex", loanGroup.getDefaultSex());
                contentValues.put("IsSupplementary", loanGroup.getSupplementary());

                try {
                    databaseWrite.insert("P_LoanGroup", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (LoanGroupDuration loanGroupDuration : jsonToJava.getLoanGroupDurationList()) {
                contentValues.put("Id", loanGroupDuration.getId());
                contentValues.put("P_GroupTypeId", loanGroupDuration.getGroupTypeId());
                contentValues.put("P_LoanGroupProgramId", loanGroupDuration.getLoanGroupProgramId());
                contentValues.put("P_ProgramId", loanGroupDuration.getProgramId());
                contentValues.put("P_Duration", loanGroupDuration.getDuration());
                contentValues.put("StartingDate", loanGroupDuration.getStartingDate());
                contentValues.put("EndingDate", loanGroupDuration.getEndingDate());
                contentValues.put("StartingDateDuration", new DateAndDataConversion().dateFormationLongType(loanGroupDuration.getStartingDate()));
                contentValues.put("EndingDateDuration", new DateAndDataConversion().dateFormationLongType(loanGroupDuration.getEndingDate()));
                contentValues.put("SortOrder", loanGroupDuration.getSortOrder());
                try {
                    databaseWrite.insert("P_LoanGroupDuration", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (LoanGroupInstallment loanGroupInstallment : jsonToJava.getLoanGroupInstallmentList()) {
                contentValues.put("Id", loanGroupInstallment.getId());
                contentValues.put("P_GroupTypeId", loanGroupInstallment.getGroupTypeId());
                contentValues.put("P_LoanGroupProgramId", loanGroupInstallment.getLoanGroupProgramId());
                contentValues.put("P_ProgramId", loanGroupInstallment.getProgramId());
                contentValues.put("P_Duration", loanGroupInstallment.getDuration());
                contentValues.put("P_InstallmentType", loanGroupInstallment.getInstallmentType());
                contentValues.put("StartingDate", loanGroupInstallment.getStartingDate());
                contentValues.put("EndingDate", loanGroupInstallment.getEndingDate());
                contentValues.put("StartingDateDuration", new DateAndDataConversion()
                        .dateFormationLongType(loanGroupInstallment.getStartingDate()));

                contentValues.put("EndingDateDuration", new DateAndDataConversion()
                        .dateFormationLongType(loanGroupInstallment.getEndingDate()));

                contentValues.put("SortOrder", loanGroupInstallment.getSortOrder());
                try {
                    databaseWrite.insert("P_LoanGroupInstallment", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (LoanTransaction loanTransaction : jsonToJava.getLoanTransaction()) {
                contentValues.put("P_AccountId", loanTransaction.getAccountId());
                contentValues.put("Date", loanTransaction.getDate());
                contentValues.put("Debit", loanTransaction.getDebit());
                contentValues.put("Status", false);
                contentValues.put("Flag", 0);
                try {
                    databaseWrite.insert("P_LoanTransaction", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (Member member : jsonToJava.getMembers()) {
                contentValues.put("Id", member.getId());
                contentValues.put("P_GroupId", member.getGroupId());
                contentValues.put("P_ProgramId", member.getProgramId());
                contentValues.put("PassbookNumber", member.getPassbookNumber());
                contentValues.put("Name", member.getName());
                contentValues.put("FatherOrHusbandName", member.getFatherName());
                contentValues.put("IsHusband", member.getHusband());
                contentValues.put("DateOfBirth", new DateAndDataConversion().deformationStringToString(member.getDeathOfBirth()));
                contentValues.put("Status", member.getStatus());
                contentValues.put("AdmissionDate", new DateAndDataConversion().dateFormationLongType(member.getAdmissionDate()));
                contentValues.put("NationalIdNumber", member.getnIdNum());
                contentValues.put("Sex", member.getSex());
                contentValues.put("Phone", member.getPhone());
                contentValues.put("BirthCertificateNumber",member.getBirthCertificateNumber());
                contentValues.put("NewStatus", "Old");

                contentValues.put("ReceiveDate", member.getReceiveDate());
                contentValues.put("ReceiveType", member.getReceiveType());
                contentValues.put("UpdateNid", 0);
                contentValues.put("UpdatePhone", 0);
                contentValues.put("IsWithoutLoan", 0);

                if(jsonToJava.getMemberIdsWithoutLoan().contains(member.getId())){
                    contentValues.put("IsWithoutLoan", 1);
                }

                try {
                    databaseWrite.insert("P_MemberView", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (OtherFee otherFee : jsonToJava.getOtherFee()) {
                contentValues.put("Id", otherFee.getId());
                contentValues.put("P_ProgramId", otherFee.getProgramId());
                contentValues.put("P_Duration", otherFee.getDuration());
                contentValues.put("P_InstallmentType", otherFee.getInstallmentType());
                contentValues.put("Sex", otherFee.getSex());
                contentValues.put("ShortName", otherFee.getShortName());
                contentValues.put("Name", otherFee.getName());
                contentValues.put("Amount", otherFee.getAmount());
                contentValues.put("TransactionType", otherFee.getTransactionType());
                contentValues.put("IsMandatory", otherFee.getMandatory());
                contentValues.put("WhileDisbursing", otherFee.getWhileDisbursing());
                contentValues.put("IsFixed", otherFee.getFixed());
                contentValues.put("IsExemptable", otherFee.getExemptable());
                contentValues.put("StartingDate", otherFee.getStartingDate());
                contentValues.put("EndingDate", otherFee.getEndingDate());
                contentValues.put("SortOrder", otherFee.getSortOrder());
                try {
                    databaseWrite.insert("P_OtherFee", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (Program program : jsonToJava.getPrograms()) {
                contentValues.put("P_ProgramTypeId", program.getProgramTypeId());
                contentValues.put("ShortName", program.getShortName());
                contentValues.put("Name", program.getName());
                contentValues.put("Description", program.getDescription());
                contentValues.put("IsPrimary", program.getPrimary());
                contentValues.put("IsLongTerm", program.getLongTerm());
                contentValues.put("IsCollectionSheet", program.getCollectionSheet());
                contentValues.put("StartingDate", new DateAndDataConversion().dateFormationLongType(program.getStartingDate()));
                contentValues.put("EndingDate", new DateAndDataConversion().dateFormationLongType(program.getEndingDate()));
                contentValues.put("SortOrder", program.getSortOrder());
                contentValues.put("Program_ID", program.getId());
                try {
                    databaseWrite.insert("P_Program", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }

            }

            contentValues = new ContentValues();


            for (ProgramGroupType programGroupType : jsonToJava.getProgramGroupType()) {
                contentValues.put("P_GroupTypeId", programGroupType.getGroupTypeId());
                contentValues.put("P_ProgramId", programGroupType.getProgramId());
                try {
                    databaseWrite.insert("P_ProgramGroupType", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (ProgramOfficer programOfficer : jsonToJava.getProgramOfficerList()) {
                contentValues.put("Id", programOfficer.getId());
                contentValues.put("Code", programOfficer.getCode());
                contentValues.put("Name", programOfficer.getName());
                contentValues.put("Designation", programOfficer.getDesignation());
                contentValues.put("StartingDate", programOfficer.getStartingDate());
                contentValues.put("EndingDate", programOfficer.getEndingDate());
                if (jsonToJava.getProgramOfficerList().indexOf(programOfficer) == position && programOfficer.getStatus() == 1) {
                    contentValues.put("Status", programOfficer.getStatus());
                } else {
                    contentValues.put("Status", 2);
                }

                try {
                    databaseWrite.insert("P_ProgramOfficer", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (Schedule schedule : jsonToJava.getScheduleListByProgramOfficer().get(position).getScheduleList()) {
                contentValues.put("P_AccountId", schedule.getAccountId());
                contentValues.put("BaseInstallmentAmount", schedule.getBaseInstallmentAmount());
                contentValues.put("InstallmentAmount", schedule.getInstallmentAmount());
                contentValues.put("ScheduledDate ", schedule.getScheduledDate());
                contentValues.put("Scheduled", schedule.getScheduled());
                contentValues.put("NextDate", schedule.getNextDate());
                contentValues.put("PaidAmount", schedule.getPaidAmount());
                contentValues.put("AdvanceAmount ", schedule.getAdvanceAmount());
                contentValues.put("OverdueAmount", schedule.getOverDueAmount());
                contentValues.put("OutstandingAmount ", schedule.getOutstandingAmount());
                contentValues.put("PrincipalOutstanding ", schedule.getPrincipalOutstanding());
                contentValues.put("MaxInstallmentNumber", schedule.getMaxInstallmentNumber());
                contentValues.put("ScheduleID", schedule.getId());
                try {
                    databaseWrite.insert("P_Schedule", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (ServiceCharge serviceCharge : jsonToJava.getServiceCharge()) {
                contentValues.put("P_ProgramId", serviceCharge.getProgramId());
                contentValues.put("P_Duration", serviceCharge.getDuration());
                contentValues.put("P_InstallmentType", serviceCharge.getInstallmentType());
                contentValues.put("Sex", serviceCharge.getSex());
                contentValues.put("ServiceCharge", serviceCharge.getServiceCharge());
                contentValues.put("StartingDate", new DateAndDataConversion().deformationStringToString(serviceCharge.getStartingDate()));
                contentValues.put("EndingDate", new DateAndDataConversion().deformationStringToString(serviceCharge.getEndingDate()));
                contentValues.put("StartingDateInteger", new DateAndDataConversion().dateFormationLongType(serviceCharge.getStartingDate()));
                contentValues.put("EndingDateInteger", new DateAndDataConversion().dateFormationLongType(serviceCharge.getEndingDate()));
                contentValues.put("DecliningServiceCharge", serviceCharge.getDecliningServiceCharge());
                contentValues.put("P_FundId", serviceCharge.getFundId());
                try {
                    databaseWrite.insert("P_ServiceCharge", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();

            for (Fund fund : jsonToJava.getFunds()) {
                contentValues.put("Id", fund.getId());
                contentValues.put("Name", fund.getName());


                try {
                    databaseWrite.insert("P_Fund", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();

            for (Scheme scheme : jsonToJava.getSchemes()) {
                contentValues.put("Id", scheme.getId());
                contentValues.put("Name", scheme.getName());
                contentValues.put("P_SchemeCategoriesId", scheme.getSchemeCategoriesId());


                try {
                    databaseWrite.insert("P_Scheme", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            contentValues = new ContentValues();


            for (User user : jsonToJava.getUserList()) {
                contentValues.put("Login", user.getLogin());
                contentValues.put("Password", user.getPassword());
                contentValues.put("Name", user.getName());
                if (jsonToJava.getUserList().indexOf(user) == position && user.getActive()) {
                    contentValues.put("IsActive", user.getActive());
                } else {
                    contentValues.put("IsActive", false);
                }

                contentValues.put("ProgramOfficerId", user.getProgramOfficerId());
                try {
                    databaseWrite.insert("User", null, contentValues);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

            this.openReadableDatabase();
            Cursor cursorScheduleUpdate = databaseRead.rawQuery("SELECT P_Schedule.Id AS Id ,  P_Schedule.AdvanceAmount AS AdvanceAmount , P_Schedule.PaidAmount AS PaidAmount , P_Schedule.OverdueAmount AS OverdueAmount , P_AccountBalance.Credit AS Credit FROM P_AccountBalance INNER JOIN P_Schedule ON P_AccountBalance.P_AccountId = P_Schedule.P_AccountId AND P_AccountBalance.Credit >0  AND P_AccountBalance.Date = " + openingDay + " AND P_Schedule.ScheduledDate <= " + openingDay + "", null);
            cursorScheduleUpdate.moveToFirst();
            while (!cursorScheduleUpdate.isAfterLast()) {

                float advanceAmount = cursorScheduleUpdate.getFloat(cursorScheduleUpdate.getColumnIndex("AdvanceAmount"));
                float paidAmount = cursorScheduleUpdate.getFloat(cursorScheduleUpdate.getColumnIndex("PaidAmount"));
                float overDueAmount = cursorScheduleUpdate.getFloat(cursorScheduleUpdate.getColumnIndex("OverdueAmount"));
                float credit = cursorScheduleUpdate.getFloat(cursorScheduleUpdate.getColumnIndex("Credit"));

                int scheduleId = cursorScheduleUpdate.getInt(cursorScheduleUpdate.getColumnIndex("Id"));


                paidAmount -= credit;
                if (paidAmount < 0)
                    paidAmount = 0;

                if (advanceAmount > 0) {
                    credit -= advanceAmount;
                    advanceAmount = 0;
                    if (credit < 0)
                        credit = 0;
                }

                overDueAmount += credit;
                if (overDueAmount < 0) {
                    overDueAmount = 0;
                }
                ContentValues newContentValueUpdateSchedule = new ContentValues();
                newContentValueUpdateSchedule.put("AdvanceAmount", advanceAmount);
                newContentValueUpdateSchedule.put("PaidAmount", paidAmount);
                newContentValueUpdateSchedule.put("OverdueAmount", overDueAmount);


                try {
                    databaseWrite.update("P_Schedule", newContentValueUpdateSchedule, "Id = " + scheduleId + "", null);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }

                cursorScheduleUpdate.moveToNext();
            }
            cursorScheduleUpdate.close();


            databaseWrite.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DatabaseInsertError", e.getMessage());
            return 2;
        } finally {
            databaseWrite.endTransaction();
        }


        return 1;
    }

    public void insertTempData(int groupId, String groupName) {
        ContentValues contentValues = new ContentValues();
        this.openWritableDatabase();
        int date = dataSourceOperationsCommon.getWorkingDay();
        contentValues.put("GroupId", groupId);
        contentValues.put("GroupName", groupName);
        contentValues.put("WorkingDay", date);
        try {
            databaseWrite.insert("TempRealizedGroup", null, contentValues);
            Log.i("Temps", contentValues.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isTableExists() {
        boolean result = false;
        this.openReadableDatabase();
        Cursor cursor = databaseRead.rawQuery("Select * from P_Group", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                result = true;
            }
            cursor.close();
        }

        return result;
    }

    public void insertOrUpdateAccountBalanceCredit(AccountForDailyTransaction accountForDailyTransaction, float creditAmount, String createDateTime, int programTypeId, int programOfficerId) {
        int workingDay = dataSourceOperationsCommon.getWorkingDay();
        this.openReadableDatabase();
        this.openWritableDatabase();

        int accountBalanceId;
        int type = 0;

        if (programTypeId == 1) {
            type = 4;
        } else if (programTypeId == 2) {
            type = 1024;
        } else if (programTypeId == 4) {
            type = 131072;
        } else if (programTypeId == 8) {
            type = 256;
        }


        String query = "SELECT * FROM P_AccountBalance WHERE P_AccountId = "
                + accountForDailyTransaction.getAccountId()
                + " AND Date = "
                + workingDay
                + " And Debit = 0 AND  Type = " + type + " AND Type <> 0";

        try {

            Cursor cursor = databaseRead.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                accountBalanceId = cursor.getInt(cursor.getColumnIndex("Id"));

                if (accountBalanceId != 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Credit", creditAmount);
                    contentValues.put("CreateDateTime", createDateTime);


                    if (cursor.getInt(cursor.getColumnIndex("ProgramType")) == 1 || cursor.getInt(cursor.getColumnIndex("ProgramType")) == 8) {
                        contentValues.put("Status", "collection");
                    } else {
                        contentValues.put("Status", "deposit");

                    }


                    if (cursor.getInt(cursor.getColumnIndex("Flag")) == 0) {
                        contentValues.put("Flag", 1);
                    } else if (creditAmount > 0 && cursor.getInt(cursor.getColumnIndex("Flag")) == 3) {
                        contentValues.put("Flag", 2);
                    } else if (creditAmount <= 0 && cursor.getInt(cursor.getColumnIndex("Flag")) == 2) {
                        contentValues.put("Flag", 3);
                    } else {
                        contentValues.put("Flag", cursor.getInt(cursor.getColumnIndex("Flag")));
                    }


                    contentValues.put("ProgramOfficerID", programOfficerId);
                    try {
                        databaseWrite.update("P_AccountBalance", contentValues, "Id = '"
                                + accountBalanceId + "'", null);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                int programType = accountForDailyTransaction.getProgramTypeId();

                ContentValues values = new ContentValues();
                values.put("P_AccountId", accountForDailyTransaction.getAccountId());
                values.put("ProgramType", programType);
                values.put("Date", workingDay);
                values.put("Credit", creditAmount);
                values.put("Debit", 0);
                values.put("CreateDateTime", createDateTime);
                values.put("ProgramOfficerID", programOfficerId);


                if (creditAmount > 0) {
                    values.put("Flag", 2);
                } else if (creditAmount <= 0) {
                    values.put("Flag", 3);
                } else {
                    values.put("Flag", 3);
                }

                if (programType == 1) {
                    values.put("Type", 4);
                } else if (programType == 8) {
                    values.put("Type", 256);
                } else if (programType == 2) {
                    values.put("Type", 1024);
                } else if (programType == 4) {
                    values.put("Type", 131072);
                }
                if (programType == 1 || programType == 8) {
                    values.put("Status", "collection");
                } else {
                    values.put("Status", "deposit");

                }
                databaseWrite.insert("P_AccountBalance", null, values);


            }
            cursor.close();

            if (accountForDailyTransaction.getProgramId() == 204) {

                String queryStr = "SELECT * FROM P_AccountDetails WHERE P_AccountId = "
                        + accountForDailyTransaction.getAccountId() + "  AND P_LoanTransactionDate = "
                        + workingDay + " AND NewlyCreated = 1 ORDER BY P_LoanTransactionDate DESC";


                Cursor cursorAD = databaseRead.rawQuery(queryStr, null);
                cursorAD.moveToFirst();

                if (cursorAD.getCount() == 0 && creditAmount > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("P_AccountId", accountForDailyTransaction.getAccountId());
                    contentValues.put("P_LoanTransactionDate", workingDay);
                    contentValues.put("Type", 1024);
                    contentValues.put("Amount", creditAmount);
                    contentValues.put("Process", 1);
                    contentValues.put("NewlyCreated", true);
                    try {
                        databaseWrite.insert("P_AccountDetails", null, contentValues);
                    } catch (SQLException e) {
                        Log.i("SQL Error", e.toString());
                    }

                } else if (cursorAD.getCount() > 0) {
                    if (creditAmount > 0) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("P_AccountId", accountForDailyTransaction.getAccountId());
                        contentValues.put("P_LoanTransactionDate", workingDay);
                        contentValues.put("Type", 1024);
                        contentValues.put("Amount", creditAmount);
                        contentValues.put("Process", 1);
                        contentValues.put("NewlyCreated", true);
                        try {
                            databaseWrite.update("P_AccountDetails", contentValues, "ID = " + cursorAD.getInt(cursorAD.getColumnIndex("ID")), null);
                        } catch (SQLException e) {
                            Log.i("SQL Error", e.toString());
                        }
                    } else {
                        databaseWrite.delete("P_AccountDetails", "ID = " + cursorAD.getInt(cursorAD.getColumnIndex("ID")), null);
                    }

                }
                cursorAD.close();

            }
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


    }

    public void insertOrUpdateAccountBalanceDebit(AccountForDailyTransaction accountForDailyTransaction, float debitAmount, String createDateTime, int programTypeId, int programOfficerId) {
        int workingDay = dataSourceOperationsCommon.getWorkingDay();
        this.openReadableDatabase();
        this.openWritableDatabase();

        int accountBalanceId;

        int type = 0;

        if (programTypeId == 1) {
            type = 4;
        } else if (programTypeId == 2) {
            type = 16386;
        } else if (programTypeId == 4) {
            type = 48576;
        }

        String query = "SELECT * FROM P_AccountBalance WHERE P_AccountId = "
                + accountForDailyTransaction.getAccountId()
                + " AND Date = "
                + workingDay
                + " And Credit = 0  AND Type = " + type + " AND Type <> 0 ";
        String queryTotal = "SELECT SUM(Credit) AS SumCredit FROM P_AccountBalance WHERE P_AccountId = "
                + accountForDailyTransaction.getAccountId()
                + "";


        try {

            Cursor cursorTotal = databaseRead.rawQuery(queryTotal, null);
            cursorTotal.moveToFirst();


            if (debitAmount > cursorTotal.getInt(cursorTotal.getColumnIndex("SumCredit"))) {
                debitAmount = cursorTotal.getInt(cursorTotal.getColumnIndex("SumCredit"));

            }

            Cursor cursor = databaseRead.rawQuery(query, null);
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                accountBalanceId = cursor.getInt(cursor.getColumnIndex("Id"));

                if (accountBalanceId != 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Debit", debitAmount);
                    contentValues.put("CreateDateTime", createDateTime);
                    contentValues.put("Date", dataSourceOperationsCommon.getWorkingDay());
                    contentValues.put("ProgramOfficerID", programOfficerId);

                    if (cursor.getInt(cursor.getColumnIndex("ProgramType")) != 1 || cursor.getInt(cursor.getColumnIndex("ProgramType")) != 8) {
                        contentValues.put("Status", "withdrawal");
                    }

                    if (cursor.getInt(cursor.getColumnIndex("Flag")) == 0) {
                        contentValues.put("Flag", 1);
                    } else if (debitAmount > 0 && cursor.getInt(cursor.getColumnIndex("Flag")) == 3) {
                        contentValues.put("Flag", 2);
                    } else if (debitAmount <= 0 && cursor.getInt(cursor.getColumnIndex("Flag")) == 2) {
                        contentValues.put("Flag", 3);
                    } else {
                        contentValues.put("Flag", cursor.getInt(cursor.getColumnIndex("Flag")));
                    }

                    try {
                        databaseWrite.update("P_AccountBalance", contentValues, "Id = '"
                                + accountBalanceId + "'", null);

                        if (programTypeId == 4) {
                            String queryLast = "SELECT SUM(Credit) AS SumCredit , SUM(Debit) AS SumDebit  FROM P_AccountBalance WHERE P_AccountId = "
                                    + accountForDailyTransaction.getAccountId()
                                    + "";

                            Cursor cursorLast = databaseRead.rawQuery(queryLast, null);
                            cursorLast.moveToFirst();
                            if (cursorLast.getInt(cursorLast.getColumnIndex("SumCredit")) - cursorLast.getInt(cursorLast.getColumnIndex("SumDebit")) > 4000) {
                                String queryFinal = "SELECT * FROM P_AccountBalance WHERE P_AccountId = "
                                        + accountForDailyTransaction.getAccountId()
                                        + " AND Date = "
                                        + workingDay
                                        + "  AND Type = 0 ";
                                Cursor cursorFinal = databaseRead.rawQuery(queryFinal, null);
                                cursorFinal.moveToFirst();

                                int amount = 4000 - cursorFinal.getInt(cursorFinal.getColumnIndex("Credit"));
                                if (amount >= 0) {
                                    insertOrUpdateAccountBalanceCredit(accountForDailyTransaction, amount, createDateTime, programTypeId, programOfficerId);
                                }

                                cursorFinal.close();
                            }

                            cursorLast.close();

                        }


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            } else {
                int programType = accountForDailyTransaction.getProgramTypeId();
                ContentValues values = new ContentValues();
                values.put("P_AccountId", accountForDailyTransaction.getAccountId());
                values.put("ProgramType", programType);
                values.put("Date", dataSourceOperationsCommon.getWorkingDay());
                values.put("Credit", 0);
                values.put("Debit", debitAmount);
                values.put("CreateDateTime", createDateTime);
                values.put("ProgramOfficerID", programOfficerId);


                if (debitAmount > 0) {
                    values.put("Flag", 2);
                } else if (debitAmount <= 0) {
                    values.put("Flag", 3);
                } else {
                    values.put("Flag", 3);
                }

                if (programType != 1 || programOfficerId != 8) {
                    values.put("Status", "withdrawal");
                }


                if (programType == 1) {
                    values.put("Type", 4);
                } else if (programType == 2) {
                    values.put("Type", 16386);
                } else if (programType == 4) {
                    values.put("Type", 48576);
                }

                databaseWrite.insert("P_AccountBalance", null, values);

                if (programTypeId == 4) {
                    String queryLast = "SELECT SUM(Credit) AS SumCredit , SUM(Debit) AS SumDebit  FROM P_AccountBalance WHERE P_AccountId = "
                            + accountForDailyTransaction.getAccountId()
                            + "";

                    Cursor cursorLast = databaseRead.rawQuery(queryLast, null);
                    cursorLast.moveToFirst();
                    if (cursorLast.getInt(cursorLast.getColumnIndex("SumCredit")) - cursorLast.getInt(cursorLast.getColumnIndex("SumDebit")) > 4000) {
                        String queryFinal = "SELECT * FROM P_AccountBalance WHERE P_AccountId = "
                                + accountForDailyTransaction.getAccountId()
                                + " AND Date = "
                                + workingDay
                                + "  AND Type = 0 ";
                        Cursor cursorFinal = databaseRead.rawQuery(queryFinal, null);
                        cursorFinal.moveToFirst();


                        int amount = 4000 - cursorFinal.getInt(cursorFinal.getColumnIndex("Credit"));
                        if (amount >= 0) {
                            insertOrUpdateAccountBalanceCredit(accountForDailyTransaction, amount, createDateTime, programTypeId, programOfficerId);
                        }

                        cursorFinal.close();
                    }

                    cursorLast.close();
                }

            }


            cursor.close();
            cursorTotal.close();

            if (accountForDailyTransaction.getProgramId() != 204 && accountForDailyTransaction.getProgramId() > 200 && accountForDailyTransaction.getProgramId() < 300) {

                String queryStr = "SELECT * FROM P_AccountDetails WHERE P_AccountId = "
                        + accountForDailyTransaction.getAccountId() + "  AND P_LoanTransactionDate = "
                        + workingDay + " AND NewlyCreated = 1 ORDER BY P_LoanTransactionDate DESC";


                Cursor cursorAD = databaseRead.rawQuery(queryStr, null);
                cursorAD.moveToFirst();

                if (cursorAD.getCount() == 0 && debitAmount > 0) {
                    ContentValues contentValues = new ContentValues();

                    if (accountForDailyTransaction.getProgramTypeId() == 1) {
                        contentValues.put("Type", 4);
                    } else if (accountForDailyTransaction.getProgramTypeId() == 2) {
                        contentValues.put("Type", 16386);
                    } else if (accountForDailyTransaction.getProgramTypeId() == 4) {
                        contentValues.put("Type", 48576);
                    }

                    contentValues.put("P_AccountId", accountForDailyTransaction.getAccountId());
                    contentValues.put("P_LoanTransactionDate", workingDay);
                    //contentValues.put("Type", 1024);
                    contentValues.put("Amount", debitAmount);
                    contentValues.put("Process", 1);
                    contentValues.put("NewlyCreated", true);
                    try {
                        databaseWrite.insert("P_AccountDetails", null, contentValues);
                    } catch (SQLException e) {
                        Log.i("SQL Error", e.toString());
                    }

                } else if (cursorAD.getCount() > 0) {
                    if (debitAmount > 0) {
                        ContentValues contentValues = new ContentValues();


                        if (accountForDailyTransaction.getProgramTypeId() == 1) {
                            contentValues.put("Type", 4);
                        } else if (accountForDailyTransaction.getProgramTypeId() == 2) {
                            contentValues.put("Type", 16386);
                        } else if (accountForDailyTransaction.getProgramTypeId() == 4) {
                            contentValues.put("Type", 48576);
                        }

                        contentValues.put("P_AccountId", accountForDailyTransaction.getAccountId());
                        contentValues.put("P_LoanTransactionDate", workingDay);
                        //contentValues.put("Type", 1024);
                        contentValues.put("Amount", debitAmount);
                        contentValues.put("Process", 1);
                        contentValues.put("NewlyCreated", true);
                        try {
                            databaseWrite.update("P_AccountDetails", contentValues, "ID = " + cursorAD.getInt(cursorAD.getColumnIndex("ID")), null);
                        } catch (SQLException e) {
                            Log.i("SQL Error", e.toString());
                        }
                    } else {
                        databaseWrite.delete("P_AccountDetails", "ID = " + cursorAD.getInt(cursorAD.getColumnIndex("ID")), null);
                    }

                }
                cursorAD.close();

            }
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


    }

    public void updateExemptionData(int accountId, boolean status) {
        this.openWritableDatabase();
        ContentValues updateExemptionData = new ContentValues();
        int currentDay = dataSourceOperationsCommon.getWorkingDay();
        try {
            updateExemptionData.put("Status", status);
            updateExemptionData.put("Flag", 1);

            databaseWrite.update("P_LoanTransaction", updateExemptionData, "P_AccountId" + "=" + accountId + " AND Date = '" + currentDay + "'", null);

        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
    }

    public void insertNewAccountWithoutLoan(Account account, int programOfficerId) {
        this.openWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("P_MemberId", account.getMemberId());
        contentValues.put("P_ProgramId", account.getProgramId());
        contentValues.put("P_ProgramTypeId", account.getProgramTypeId());
        contentValues.put("P_Duration", account.getDuration());
        contentValues.put("P_InstallmentType", account.getInstallmentType());
        contentValues.put("IsSupplementary", account.getSupplementary());
        contentValues.put("MemberSex", account.getMemberSex());
        contentValues.put("OpeningDate", account.getOpeningDate());
        contentValues.put("DisbursedAmount", account.getDisbursedAmount());
        contentValues.put("ServiceChargeAmount", account.getServiceChargeAmount());
        contentValues.put("MinimumDeposit", account.getMinimumDeposit());
        contentValues.put("MeetingDayOfWeek", account.getMeetingDayOfWeek());
        contentValues.put("MeetingDayOfMonth", account.getMeetingDayOfMonth());
        contentValues.put("Status", 1);
        contentValues.put("Account_ID", account.getId());
        contentValues.put("ProgramName", account.getProgramName());
        contentValues.put("Flag", 1);
        contentValues.put("NewLoan", 0);
        contentValues.put("NewAccount", 1);
        contentValues.put("ProgramOfficerID", programOfficerId);


        try {
            databaseWrite.insert("P_Account", null, contentValues);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }
    }

    private void updateNewAccountWithoutLoan(Account account, int programOfficerId) {
        this.openWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("P_ProgramId", account.getProgramId());
        contentValues.put("P_ProgramTypeId", account.getProgramTypeId());
        contentValues.put("P_Duration", account.getDuration());
        contentValues.put("P_InstallmentType", account.getInstallmentType());
        contentValues.put("IsSupplementary", account.getSupplementary());
        contentValues.put("MemberSex", account.getMemberSex());
        contentValues.put("OpeningDate", account.getOpeningDate());
        contentValues.put("DisbursedAmount", account.getDisbursedAmount());
        contentValues.put("ServiceChargeAmount", account.getServiceChargeAmount());
        contentValues.put("MinimumDeposit", account.getMinimumDeposit());
        contentValues.put("MeetingDayOfWeek", account.getMeetingDayOfWeek());
        contentValues.put("MeetingDayOfMonth", account.getMeetingDayOfMonth());
        contentValues.put("Status", 1);
        contentValues.put("ProgramName", account.getProgramName());
        contentValues.put("Flag", 1);
        contentValues.put("NewLoan", 0);
        contentValues.put("NewAccount", 1);
        contentValues.put("ProgramOfficerID", programOfficerId);


        try {
            databaseWrite.update("P_Account", contentValues, "P_MemberId = " + account.getMemberId() + " AND Account_ID = " + account.getId(), null);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }
    }

    public boolean insertLoanAccount(Account account) {
        this.openWritableDatabase();
        boolean successful = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("P_MemberId", account.getMemberId());
        contentValues.put("P_ProgramId", account.getProgramId());
        contentValues.put("P_ProgramTypeId", account.getProgramTypeId());
        contentValues.put("P_Duration", account.getDuration());
        contentValues.put("P_InstallmentType", account.getInstallmentType());
        contentValues.put("IsSupplementary", account.getSupplementary());
        contentValues.put("MemberSex", account.getMemberSex());
        contentValues.put("OpeningDate", account.getOpeningDate());
        contentValues.put("DisbursedAmount", account.getDisbursedAmount());
        contentValues.put("ServiceChargeAmount", account.getServiceChargeAmount());
        contentValues.put("MinimumDeposit", account.getMinimumDeposit());
        contentValues.put("MeetingDayOfWeek", account.getMeetingDayOfWeek());
        contentValues.put("MeetingDayOfMonth", account.getMeetingDayOfMonth());
        contentValues.put("FirstInstallmentDate", account.getFirstInstallmentDate());
        contentValues.put("Status", 1);
        contentValues.put("GracePeriod", account.getGracePeriod());
        contentValues.put("P_SchemeId", account.getSchemeId());
        contentValues.put("P_FundId", account.getFundId());
        contentValues.put("DisbursedAmountWithSC", account.getDisbursedAmountWithSC());
        contentValues.put("Account_ID", account.getId());
        contentValues.put("ProgramName", account.getProgramName());
        contentValues.put("LoanInsurance", account.getLoanInsuranceAmount());
        contentValues.put("ProgramOfficerID", account.getProgramOfficerId());
        contentValues.put("Flag", 1);
        contentValues.put("NewLoan", 1);
        contentValues.put("NewAccount", 1);

        try {

            databaseWrite.insert("P_Account", null, contentValues);
            successful = true;
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }
        return successful;

    }

    public void insertNewAccountBalance(AccountBalance accountBalance, int programOfficerId) {
        this.openWritableDatabase();

        java.util.Calendar c = java.util.Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+6"));

        String currentDateTime = df.format(c.getTime());

        ContentValues contentValues = new ContentValues();


        contentValues.put("P_AccountId", accountBalance.getAccountId());
        contentValues.put("ProgramType", accountBalance.getProgramType());
        contentValues.put("Date", accountBalance.getDate());
        contentValues.put("CreateDateTime", currentDateTime);
        contentValues.put("ProgramOfficerID", programOfficerId);
        contentValues.put("StatusLoan", "New");

        int programType = accountBalance.getProgramType();
        if (programType == 1 || programType == 8) {
            contentValues.put("Debit", (accountBalance.getBalance() - accountBalance.getCredit()));
            contentValues.put("Flag", 3);
            contentValues.put("Credit", 0);
            contentValues.put("Type", 0);

            if (accountBalance.getCredit() > 0) {
                ContentValues newContentValue = new ContentValues();

                newContentValue.put("P_AccountId", accountBalance.getAccountId());
                newContentValue.put("Date", accountBalance.getDate());
                newContentValue.put("Debit", 0);
                newContentValue.put("Credit", accountBalance.getCredit());
                newContentValue.put("ProgramType", accountBalance.getProgramType());
                newContentValue.put("Type", 4);
                newContentValue.put("Flag", 2);
                newContentValue.put("CreateDateTime", currentDateTime);
                newContentValue.put("ProgramOfficerID", programOfficerId);
                newContentValue.put("Status", "collection");


                try {
                    databaseWrite.insert("P_AccountBalance", null, newContentValue);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }

        } else if (programType == 2 || programType == 4) {

            contentValues.put("Debit", 0);
            contentValues.put("Credit", accountBalance.getBalance());
            contentValues.put("Type", 0);
            contentValues.put("Flag", 3);
            contentValues.put("ProgramOfficerID", programOfficerId);


            if (accountBalance.getCredit() > 0 && accountBalance.getDebit() > 0) {
                ContentValues newContentValueCredit = new ContentValues();
                newContentValueCredit.put("P_AccountId", accountBalance.getAccountId());
                newContentValueCredit.put("Date", accountBalance.getDate());
                newContentValueCredit.put("Debit", 0);
                newContentValueCredit.put("Credit", accountBalance.getCredit());
                newContentValueCredit.put("ProgramType", programType);
                newContentValueCredit.put("Flag", 2);
                newContentValueCredit.put("CreateDateTime", currentDateTime);
                newContentValueCredit.put("ProgramOfficerID", programOfficerId);
                newContentValueCredit.put("Status", "deposit");


                ContentValues newContentValueDebit = new ContentValues();
                newContentValueDebit.put("P_AccountId", accountBalance.getAccountId());
                newContentValueDebit.put("Date", accountBalance.getDate());
                newContentValueDebit.put("Debit", accountBalance.getDebit());
                newContentValueDebit.put("Credit", 0);
                newContentValueDebit.put("Flag", 2);
                newContentValueDebit.put("CreateDateTime", currentDateTime);
                newContentValueDebit.put("ProgramOfficerID", programOfficerId);
                newContentValueDebit.put("Status", "withdrawal");


                if (programType == 2) {
                    newContentValueCredit.put("Type", 1024);
                    newContentValueDebit.put("Type", 16386);
                } else {
                    newContentValueCredit.put("Type", 131072);
                    newContentValueDebit.put("Type", 48576);
                }
                try {
                    databaseWrite.insert("P_AccountBalance", null, newContentValueCredit);
                    databaseWrite.insert("P_AccountBalance", null, newContentValueDebit);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            } else if (accountBalance.getCredit() > 0) {
                ContentValues newContentValueCredit = new ContentValues();
                newContentValueCredit.put("P_AccountId", accountBalance.getAccountId());
                newContentValueCredit.put("Date", accountBalance.getDate());
                newContentValueCredit.put("Debit", 0);
                newContentValueCredit.put("Credit", accountBalance.getCredit());
                newContentValueCredit.put("ProgramType", programType);
                newContentValueCredit.put("Flag", 2);
                newContentValueCredit.put("CreateDateTime", currentDateTime);
                newContentValueCredit.put("ProgramOfficerID", programOfficerId);
                newContentValueCredit.put("Status", "deposit");

                if (programType == 2) {
                    newContentValueCredit.put("Type", 1024);
                } else {
                    newContentValueCredit.put("Type", 131072);
                }
                try {
                    databaseWrite.insert("P_AccountBalance", null, newContentValueCredit);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            } else if (accountBalance.getDebit() > 0) {
                ContentValues newContentValueDebit = new ContentValues();
                newContentValueDebit.put("P_AccountId", accountBalance.getAccountId());
                newContentValueDebit.put("Date", accountBalance.getDate());
                newContentValueDebit.put("Debit", accountBalance.getDebit());
                newContentValueDebit.put("Credit", 0);
                newContentValueDebit.put("ProgramType", programType);
                newContentValueDebit.put("Flag", 2);
                newContentValueDebit.put("CreateDateTime", currentDateTime);
                newContentValueDebit.put("ProgramOfficerID", programOfficerId);
                newContentValueDebit.put("Status", "withdrawal");


                if (programType == 2) {
                    newContentValueDebit.put("Type", 16386);
                } else {
                    newContentValueDebit.put("Type", 48576);
                }
                try {
                    databaseWrite.insert("P_AccountBalance", null, newContentValueDebit);
                } catch (SQLException e) {
                    Log.i("SQL Error", e.toString());
                }
            }
        }

        try {
            databaseWrite.insert("P_AccountBalance", null, contentValues);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }
    }

    private int currentDateSelection() {
        String currentDate = dataSourceOperationsCommon.getFirstRealDate();

        SimpleDateFormat dateFromDatabase = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFromDatabase.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date dfd = null;
        int date;
        try {
            dfd = dateFromDatabase.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat convertedDate = new SimpleDateFormat("dd", Locale.getDefault());
        convertedDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try {
            date = Integer.parseInt(convertedDate.format(dfd));
            if (date < 0) {
                date = 1;
            }
        } catch (Exception e) {
            date = 1;
        }

        return date;
    }

    public boolean insertNewMember(NewMember member, int programOfficerId) {

        this.openWritableDatabase();
        ContentValues contentValuesOld = new ContentValues();

        ContentValues contentValuesNew = new ContentValues();


        int memberId = dataSourceRead.getGroupNextMemberId();

        contentValuesOld.put("Id", memberId);
        contentValuesOld.put("P_GroupId", member.getGroupId());
        contentValuesOld.put("P_ProgramId", member.getProgramId());
        contentValuesOld.put("PassbookNumber", member.getPassbookNumber());
        contentValuesOld.put("Name", member.getMemberNickName());

        if (member.isHusband()) {
            contentValuesOld.put("FatherOrHusbandName", member.getSpouseNickName());
        } else {
            contentValuesOld.put("FatherOrHusbandName", member.getFatherNickName());
        }

        contentValuesOld.put("IsHusband", member.isHusband());
        contentValuesOld.put("DateOfBirth", member.getDateOfBirth());
        contentValuesOld.put("Status", member.getStatus());
        contentValuesOld.put("AdmissionDate", new DateAndDataConversion().dateToLong(member.getAdmissionDate()));
        contentValuesOld.put("NationalIdNumber", member.getNationalID());
        contentValuesOld.put("Sex", member.getSex());
        contentValuesOld.put("Phone", member.getPresentPhone());
        contentValuesOld.put("NewStatus", "New");
        contentValuesOld.put("ReceiveDate", 0);
        contentValuesOld.put("ReceiveType", -1);
        contentValuesOld.put("BirthCertificateNumber",member.getBirthCertificateNumber());


        contentValuesNew.put("Id", memberId);
        contentValuesNew.put("P_GroupId", member.getGroupId());
        contentValuesNew.put("P_ProgramId", member.getProgramId());
        contentValuesNew.put("PassbookNumber", member.getPassbookNumber());
        contentValuesNew.put("Name", member.getMemberNickName());
        if (member.isHusband()) {
            contentValuesNew.put("FatherOrHusbandName", member.getSpouseNickName());
        } else {
            contentValuesNew.put("FatherOrHusbandName", member.getFatherNickName());
        }

        contentValuesNew.put("IsHusband", member.isHusband());
        contentValuesNew.put("DateOfBirth", member.getDateOfBirth());
        contentValuesNew.put("Status", member.getStatus());
        contentValuesNew.put("AdmissionDate", new DateAndDataConversion().dateToLong(member.getAdmissionDate()));
        contentValuesNew.put("AdmissionDateString", member.getAdmissionDate());
        contentValuesNew.put("NationalIdNumber", member.getNationalID());
        contentValuesNew.put("Sex", member.getSex());
        contentValuesNew.put("Phone", member.getPermanentPhone());
        contentValuesNew.put("NewStatus", "New");


        contentValuesNew.put("PresentPermanentSame", member.isPresentPermanentSame());


        contentValuesNew.put("MemberNickName", member.getMemberNickName());
        contentValuesNew.put("MemberFullName", member.getMemberFullName());
        contentValuesNew.put("FatherNickName", member.getFatherNickName());
        contentValuesNew.put("FatherFullName", member.getFatherFullName());
        contentValuesNew.put("MotherName", member.getMotherName());
        contentValuesNew.put("EducationInfo", member.getEducationInfo());
        contentValuesNew.put("ProfessionInfo", member.getProfessionInfo());
        contentValuesNew.put("MemberAge", member.getMemberAge());
        contentValuesNew.put("Nationality", member.getNationality());
        contentValuesNew.put("ReligionInfo", member.getReligionInfo());
        contentValuesNew.put("Ethnicity", member.getEthnicity());
        contentValuesNew.put("MaritalStatus", member.getMaritalStatus());
        contentValuesNew.put("SpouseFullName", member.getSpouseFullName());
        contentValuesNew.put("SpouseNickName", member.getSpouseNickName());
        contentValuesNew.put("GuardianName", member.getGuardianName());
        contentValuesNew.put("GuardianRelation", member.getGuardianRelation());
        contentValuesNew.put("BirthCertificateNumber", member.getBirthCertificateNumber());
        contentValuesNew.put("ResidenceType", member.getResidenceType());
        contentValuesNew.put("LandLordName", member.getLandLordName());
        contentValuesNew.put("PermanentDistrictId", member.getPermanentDistrictId());
        contentValuesNew.put("PermanentUpazila", member.getPermanentUpazila());
        contentValuesNew.put("PermanentUnion", member.getPermanentUnion());
        contentValuesNew.put("PermanentPostOffice", member.getPermanentPostOffice());
        contentValuesNew.put("PermanentVillage", member.getPermanentVillage());
        contentValuesNew.put("PermanentRoad", member.getPermanentRoad());
        contentValuesNew.put("PermanentHouse", member.getPermanentHouse());
        contentValuesNew.put("PermanentFixedProperty", member.getPermanentFixedProperty());
        contentValuesNew.put("PermanentIntroducerName", member.getPermanentIntroducerName());
        contentValuesNew.put("PermanentIntroducerDesignation", member.getPermanentIntroducerDesignation());
        contentValuesNew.put("PresentDistrictId", member.getPresentDistrictId());
        contentValuesNew.put("PresentUpazila", member.getPresentUpazila());
        contentValuesNew.put("PresentUnion", member.getPermanentUnion());
        contentValuesNew.put("PresentPostOffice", member.getPresentPostOffice());
        contentValuesNew.put("PresentVillage", member.getPresentVillage());
        contentValuesNew.put("PresentRoad", member.getPresentRoad());
        contentValuesNew.put("PresentHouse", member.getPresentHouse());
        contentValuesNew.put("PresentPhone", member.getPresentPhone());
        contentValuesNew.put("SavingsDeposit", member.getSavingDeposit());
        contentValuesNew.put("SecurityDeposit", member.getCbsDeposit());
        contentValuesNew.put("ReceiveDate", 0);
        contentValuesNew.put("ReceiveType", -1);


        try {
            databaseWrite.insert("P_MemberView", null, contentValuesOld);
            databaseWrite.insert("P_MemberNew", null, contentValuesNew);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }

        Account accountSaving = new Account();

        if (member.getProgramId() > 140 && member.getProgramId() < 145) {
            accountSaving.setProgramId(241);
            accountSaving.setProgramName("MSME SAVINGS");

        } else if (member.getProgramId() == 129) {
            accountSaving.setProgramId(229);
            accountSaving.setProgramName("PRIMARY SAVINGS");
        } else if (member.getProgramId() == 130) {
            accountSaving.setProgramId(230);
            accountSaving.setProgramName("SPECIAL SAVINGS");
        } else if (member.getProgramId() == 131) {
            accountSaving.setProgramId(231);
            accountSaving.setProgramName("SANITATION ENTREPRENEUR SAVINGS");
        } else if (member.getProgramId() == 137) {
            accountSaving.setProgramId(237);
            accountSaving.setProgramName("SMAP SAVINGS");
        } else if (member.getProgramId() == 138) {
            accountSaving.setProgramId(238);
            accountSaving.setProgramName("CROSSBREED MILKING COW SAVINGS");
        } else {
            accountSaving.setProgramId(229);
            accountSaving.setProgramName("PRIMARY SAVINGS");
        }


        accountSaving.setMemberId(memberId);
        accountSaving.setProgramTypeId(2);
        accountSaving.setDuration(0);
        accountSaving.setInstallmentType(member.getSavingInstallmentType());
        accountSaving.setSupplementary(false);
        accountSaving.setCycle(0);
        accountSaving.setMemberSex(member.getSex());
        accountSaving.setOpeningDate(openingDay);
        accountSaving.setDisbursedAmount((float) 0);
        accountSaving.setServiceChargeAmount((float) 0);
        accountSaving.setMinimumDeposit(member.getSavingDeposit());
        accountSaving.setMeetingDayOfWeek(member.getMeetingDayOfweek());
        accountSaving.setMeetingDayOfMonth(currentDateSelection());
        int accountIdSaving = dataSourceRead.getNextAccount();
        accountSaving.setId(accountIdSaving);
        insertNewAccountWithoutLoan(accountSaving, programOfficerId);
        AccountBalance accountBalancePrimarySavings = new AccountBalance();
        accountBalancePrimarySavings.setAccountId(accountIdSaving);
        accountBalancePrimarySavings.setDate(openingDay);
        accountBalancePrimarySavings.setBalance((float) 0);
        accountBalancePrimarySavings.setDebit((float) 0);
        accountBalancePrimarySavings.setProgramType(2);
        accountBalancePrimarySavings.setCredit((float) 0);
        insertNewAccountBalance(accountBalancePrimarySavings, programOfficerId);

        if (member.getCbsDeposit() > 0) {
            Account accountCBS = new Account();


            if (member.getProgramId() > 140 && member.getProgramId() < 145) {
                accountCBS.setProgramId(341);
                accountCBS.setProgramName("MSME CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 129) {
                accountCBS.setProgramId(329);
                accountCBS.setProgramName("PRIMARY CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 130) {
                accountCBS.setProgramId(330);
                accountCBS.setProgramName("SPECIAL CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 131) {
                accountCBS.setProgramId(331);
                accountCBS.setProgramName("SANITATION ENTREPRENEUR CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 137) {
                accountCBS.setProgramId(337);
                accountCBS.setProgramName("SMAP CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 138) {
                accountCBS.setProgramId(338);
                accountCBS.setProgramName("CROSSBREED MILKING COW CAPITAL BUILDUP SAVINGS");
            } else {
                accountCBS.setProgramId(329);
                accountCBS.setProgramName("PRIMARY CAPITAL BUILDUP SAVINGS");
            }


            accountCBS.setMemberId(memberId);
            accountCBS.setProgramTypeId(4);
            accountCBS.setDuration(8);
            accountCBS.setInstallmentType(member.getCbsInstallmentType());
            accountCBS.setSupplementary(false);
            accountCBS.setCycle(0);
            accountCBS.setMemberSex(member.getSex());
            accountCBS.setOpeningDate(openingDay);
            accountCBS.setDisbursedAmount((float) 0);
            accountCBS.setServiceChargeAmount((float) 0);
            accountCBS.setMinimumDeposit(member.getCbsDeposit());
            accountCBS.setMeetingDayOfWeek(member.getMeetingDayOfweek());
            accountCBS.setMeetingDayOfMonth(currentDateSelection());
            int accountIdCbs = dataSourceRead.getNextAccount();
            accountCBS.setId(accountIdCbs);

            insertNewAccountWithoutLoan(accountCBS, programOfficerId);

            AccountBalance accountBalanceCbs = new AccountBalance();

            accountBalanceCbs.setAccountId(accountIdCbs);
            accountBalanceCbs.setDate(openingDay);
            accountBalanceCbs.setBalance((float) 0);
            accountBalanceCbs.setDebit((float) 0);
            accountBalanceCbs.setProgramType(4);

            accountBalanceCbs.setCredit((float) 0);

            insertNewAccountBalance(accountBalanceCbs, programOfficerId);
        }

        return true;
    }

    public boolean updateNewMember(NewMember member, int programOfficerId, int memberId) {

        this.openWritableDatabase();
        this.openReadableDatabase();
        ContentValues contentValuesOld = new ContentValues();

        ContentValues contentValuesNew = new ContentValues();


        contentValuesOld.put("P_GroupId", member.getGroupId());
        contentValuesOld.put("P_ProgramId", member.getProgramId());
        contentValuesOld.put("PassbookNumber", member.getPassbookNumber());
        contentValuesOld.put("Name", member.getMemberNickName());

        if (member.isHusband()) {
            contentValuesOld.put("FatherOrHusbandName", member.getSpouseNickName());
        } else {
            contentValuesOld.put("FatherOrHusbandName", member.getFatherNickName());
        }

        contentValuesOld.put("IsHusband", member.isHusband());
        contentValuesOld.put("DateOfBirth", member.getDateOfBirth());
        contentValuesOld.put("Status", member.getStatus());
        contentValuesOld.put("AdmissionDate", new DateAndDataConversion().dateToLong(member.getAdmissionDate()));
        contentValuesOld.put("NationalIdNumber", member.getNationalID());
        contentValuesOld.put("Sex", member.getSex());
        contentValuesOld.put("Phone", member.getPresentPhone());
        contentValuesOld.put("NewStatus", "New");
        contentValuesOld.put("ReceiveDate", 0);
        contentValuesOld.put("ReceiveType", -1);
        contentValuesNew.put("P_GroupId", member.getGroupId());
        contentValuesNew.put("P_ProgramId", member.getProgramId());
        contentValuesNew.put("PassbookNumber", member.getPassbookNumber());
        contentValuesNew.put("Name", member.getMemberNickName());
        if (member.isHusband()) {
            contentValuesNew.put("FatherOrHusbandName", member.getSpouseNickName());
        } else {
            contentValuesNew.put("FatherOrHusbandName", member.getFatherNickName());
        }

        contentValuesNew.put("IsHusband", member.isHusband());
        contentValuesNew.put("DateOfBirth", member.getDateOfBirth());
        contentValuesNew.put("Status", member.getStatus());
        contentValuesNew.put("AdmissionDate", new DateAndDataConversion().dateToLong(member.getAdmissionDate()));
        contentValuesNew.put("AdmissionDateString", member.getAdmissionDate());
        contentValuesNew.put("NationalIdNumber", member.getNationalID());
        contentValuesNew.put("Sex", member.getSex());
        contentValuesNew.put("Phone", member.getPermanentPhone());
        contentValuesNew.put("NewStatus", "New");
        contentValuesNew.put("PresentPermanentSame", member.isPresentPermanentSame());
        contentValuesNew.put("MemberNickName", member.getMemberNickName());
        contentValuesNew.put("MemberFullName", member.getMemberFullName());
        contentValuesNew.put("FatherNickName", member.getFatherNickName());
        contentValuesNew.put("FatherFullName", member.getFatherFullName());
        contentValuesNew.put("MotherName", member.getMotherName());
        contentValuesNew.put("EducationInfo", member.getEducationInfo());
        contentValuesNew.put("ProfessionInfo", member.getProfessionInfo());
        contentValuesNew.put("MemberAge", member.getMemberAge());
        contentValuesNew.put("Nationality", member.getNationality());
        contentValuesNew.put("ReligionInfo", member.getReligionInfo());
        contentValuesNew.put("Ethnicity", member.getEthnicity());
        contentValuesNew.put("MaritalStatus", member.getMaritalStatus());
        contentValuesNew.put("SpouseFullName", member.getSpouseFullName());
        contentValuesNew.put("SpouseNickName", member.getSpouseNickName());
        contentValuesNew.put("GuardianName", member.getGuardianName());
        contentValuesNew.put("GuardianRelation", member.getGuardianRelation());
        contentValuesNew.put("BirthCertificateNumber", member.getBirthCertificateNumber());
        contentValuesNew.put("ResidenceType", member.getResidenceType());
        contentValuesNew.put("LandLordName", member.getLandLordName());
        contentValuesNew.put("PermanentDistrictId", member.getPermanentDistrictId());
        contentValuesNew.put("PermanentUpazila", member.getPermanentUpazila());
        contentValuesNew.put("PermanentUnion", member.getPermanentUnion());
        contentValuesNew.put("PermanentPostOffice", member.getPermanentPostOffice());
        contentValuesNew.put("PermanentVillage", member.getPermanentVillage());
        contentValuesNew.put("PermanentRoad", member.getPermanentRoad());
        contentValuesNew.put("PermanentHouse", member.getPermanentHouse());
        contentValuesNew.put("PermanentFixedProperty", member.getPermanentFixedProperty());
        contentValuesNew.put("PermanentIntroducerName", member.getPermanentIntroducerName());
        contentValuesNew.put("PermanentIntroducerDesignation", member.getPermanentIntroducerDesignation());
        contentValuesNew.put("PresentDistrictId", member.getPresentDistrictId());
        contentValuesNew.put("PresentUpazila", member.getPresentUpazila());
        contentValuesNew.put("PresentUnion", member.getPermanentUnion());
        contentValuesNew.put("PresentPostOffice", member.getPresentPostOffice());
        contentValuesNew.put("PresentVillage", member.getPresentVillage());
        contentValuesNew.put("PresentRoad", member.getPresentRoad());
        contentValuesNew.put("PresentHouse", member.getPresentHouse());
        contentValuesNew.put("PresentPhone", member.getPresentPhone());
        contentValuesNew.put("SavingsDeposit", member.getSavingDeposit());
        contentValuesNew.put("SecurityDeposit", member.getCbsDeposit());
        contentValuesNew.put("ReceiveDate", 0);
        contentValuesNew.put("ReceiveType", -1);

        try {
            databaseWrite.update("P_MemberView", contentValuesOld, "Id = " + memberId, null);
            databaseWrite.update("P_MemberNew", contentValuesNew, "Id = " + memberId, null);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }

        Account accountSaving = new Account();

        if (member.getProgramId() > 140 && member.getProgramId() < 145) {
            accountSaving.setProgramId(241);
            accountSaving.setProgramName("MSME SAVINGS");
        } else if (member.getProgramId() == 129) {
            accountSaving.setProgramId(229);
            accountSaving.setProgramName("PRIMARY SAVINGS");
        } else if (member.getProgramId() == 130) {
            accountSaving.setProgramId(230);
            accountSaving.setProgramName("SPECIAL SAVINGS");
        } else if (member.getProgramId() == 131) {
            accountSaving.setProgramId(231);
            accountSaving.setProgramName("SANITATION ENTREPRENEUR SAVINGS");
        } else if (member.getProgramId() == 137) {
            accountSaving.setProgramId(237);
            accountSaving.setProgramName("SMAP SAVINGS");
        } else if (member.getProgramId() == 138) {
            accountSaving.setProgramId(238);
            accountSaving.setProgramName("CROSSBREED MILKING COW SAVINGS");
        } else {
            accountSaving.setProgramId(229);
            accountSaving.setProgramName("PRIMARY SAVINGS");
        }

        accountSaving.setMemberId(memberId);
        accountSaving.setProgramTypeId(2);
        accountSaving.setDuration(0);
        accountSaving.setInstallmentType(member.getSavingInstallmentType());
        accountSaving.setSupplementary(false);
        accountSaving.setCycle(0);
        accountSaving.setMemberSex(member.getSex());
        accountSaving.setOpeningDate(openingDay);
        accountSaving.setDisbursedAmount((float) 0);
        accountSaving.setServiceChargeAmount((float) 0);
        accountSaving.setMinimumDeposit(member.getSavingDeposit());
        accountSaving.setMeetingDayOfWeek(member.getMeetingDayOfweek());
        accountSaving.setMeetingDayOfMonth(currentDateSelection());

        String quarrySaving = "SELECT * FROM P_Account WHERE P_MemberId = " + memberId + " AND (P_ProgramId >200 AND P_ProgramId <300 )";

        Cursor cursorSaving = databaseRead.rawQuery(quarrySaving, null);
        cursorSaving.moveToFirst();
        if (cursorSaving.getCount() > 0) {
            int accountIdSaving = cursorSaving.getInt(cursorSaving.getColumnIndex("Account_ID"));
            accountSaving.setId(accountIdSaving);
            updateNewAccountWithoutLoan(accountSaving, programOfficerId);
        } else {

            int accountIdSaving = dataSourceRead.getNextAccount();
            accountSaving.setId(accountIdSaving);
            insertNewAccountWithoutLoan(accountSaving, programOfficerId);


            AccountBalance accountBalancePrimarySavings = new AccountBalance();

            accountBalancePrimarySavings.setAccountId(accountIdSaving);
            accountBalancePrimarySavings.setDate(openingDay);
            accountBalancePrimarySavings.setBalance((float) 0);
            accountBalancePrimarySavings.setDebit((float) 0);
            accountBalancePrimarySavings.setProgramType(2);
            accountBalancePrimarySavings.setCredit((float) 0);

            insertNewAccountBalance(accountBalancePrimarySavings, programOfficerId);
        }


        if (member.getCbsDeposit() > 0) {
            Account accountCBS = new Account();

            if (member.getProgramId() > 140 && member.getProgramId() < 145) {
                accountCBS.setProgramId(341);
                accountCBS.setProgramName("MSME CAPITAL BUILDUP SAVINGS");

            } else if (member.getProgramId() == 129) {
                accountCBS.setProgramId(329);
                accountCBS.setProgramName("PRIMARY CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 130) {
                accountCBS.setProgramId(330);
                accountCBS.setProgramName("SPECIAL CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 131) {
                accountCBS.setProgramId(331);
                accountCBS.setProgramName("SANITATION ENTREPRENEUR CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 137) {
                accountCBS.setProgramId(337);
                accountCBS.setProgramName("SMAP CAPITAL BUILDUP SAVINGS");
            } else if (member.getProgramId() == 138) {
                accountCBS.setProgramId(338);
                accountCBS.setProgramName("CROSSBREED MILKING COW CAPITAL BUILDUP SAVINGS");
            } else {
                accountCBS.setProgramId(329);
                accountCBS.setProgramName("PRIMARY CAPITAL BUILDUP SAVINGS");
            }

            accountCBS.setMemberId(memberId);
            accountCBS.setProgramTypeId(4);
            accountCBS.setDuration(8);

            accountCBS.setInstallmentType(member.getCbsInstallmentType());
            accountCBS.setSupplementary(false);
            accountCBS.setCycle(0);
            accountCBS.setMemberSex(member.getSex());
            accountCBS.setOpeningDate(openingDay);

            accountCBS.setDisbursedAmount((float) 0);
            accountCBS.setServiceChargeAmount((float) 0);
            accountCBS.setMinimumDeposit(member.getCbsDeposit());

            accountCBS.setMeetingDayOfWeek(member.getMeetingDayOfweek());
            accountCBS.setMeetingDayOfMonth(currentDateSelection());

            String quarryCbs = "SELECT * FROM P_Account WHERE P_MemberId = " + memberId + " AND (P_ProgramId >300 AND P_ProgramId <400) ";
            Cursor cursorCbs = databaseRead.rawQuery(quarryCbs, null);
            cursorCbs.moveToFirst();


            if (cursorCbs.getCount() > 0) {
                int accountIdCbs = cursorCbs.getInt(cursorSaving.getColumnIndex("Account_ID"));
                accountCBS.setId(accountIdCbs);
                updateNewAccountWithoutLoan(accountCBS, programOfficerId);
            } else {
                int accountIdCbs = dataSourceRead.getNextAccount();
                accountCBS.setId(accountIdCbs);
                insertNewAccountWithoutLoan(accountCBS, programOfficerId);

                AccountBalance accountBalanceCbs = new AccountBalance();

                accountBalanceCbs.setAccountId(accountIdCbs);
                accountBalanceCbs.setDate(openingDay);
                accountBalanceCbs.setBalance((float) 0);
                accountBalanceCbs.setDebit((float) 0);
                accountBalanceCbs.setProgramType(4);

                accountBalanceCbs.setCredit((float) 0);

                insertNewAccountBalance(accountBalanceCbs, programOfficerId);
            }
            cursorCbs.close();
            cursorSaving.close();


        } else {
            String quarryCbs = "SELECT * FROM P_Account WHERE P_MemberId = " + memberId + " AND (P_ProgramId >300 AND P_ProgramId <400) ";
            Cursor cursorCbs = databaseRead.rawQuery(quarryCbs, null);
            cursorCbs.moveToFirst();

            if (cursorCbs.getCount() > 0) {
                int accountIdCbs = cursorCbs.getInt(cursorSaving.getColumnIndex("Account_ID"));
                deleteLtsAccount(accountIdCbs);
            }
            cursorCbs.close();

        }

        return true;
    }

    public void insertSchedule(Schedule schedule) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("P_AccountId", schedule.getAccountId());
        contentValues.put("BaseInstallmentAmount", schedule.getBaseInstallmentAmount());
        contentValues.put("InstallmentAmount", schedule.getInstallmentAmount());
        contentValues.put("ScheduledDate ", schedule.getScheduledDate());
        contentValues.put("Scheduled", schedule.getScheduled());
        contentValues.put("NextDate", schedule.getNextDate());
        contentValues.put("PaidAmount", schedule.getPaidAmount());
        contentValues.put("AdvanceAmount ", schedule.getAdvanceAmount());
        contentValues.put("OverdueAmount", schedule.getOverDueAmount());
        contentValues.put("OutstandingAmount ", schedule.getOutstandingAmount());
        contentValues.put("PrincipalOutstanding ", schedule.getPrincipalOutstanding());
        contentValues.put("MaxInstallmentNumber", schedule.getMaxInstallmentNumber());
        contentValues.put("ScheduleID", schedule.getId());


        try {
            databaseWrite.insert("P_Schedule", null, contentValues);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }
    }

    public void insertLoanTransaction(LoanTransaction loanTransaction) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("P_AccountId", loanTransaction.getAccountId());
        contentValues.put("Date", loanTransaction.getDate());
        contentValues.put("Debit", loanTransaction.getDebit());
        contentValues.put("Status ", false);


        try {
            databaseWrite.insert("P_LoanTransaction", null, contentValues);
        } catch (SQLException e) {
            Log.i("SQL Error", e.toString());
        }
    }

    public void deleteLtsAccount(int accountId) {
        this.openWritableDatabase();
        try {
            databaseWrite.delete("P_Account", "Account_ID = " + accountId + " ", null);
            databaseWrite.delete("P_AccountBalance", "P_AccountId" + "=" + accountId, null);
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
    }

    public void deleteLoanAccount(int accountId) {
        this.openWritableDatabase();
        try {
            databaseWrite.delete("P_Account", "Account_ID = " + accountId + " ", null);
            databaseWrite.delete("P_AccountBalance", "P_AccountId" + "=" + accountId, null);
            databaseWrite.delete("P_Schedule", "P_AccountId" + "=" + accountId, null);
            databaseWrite.delete("P_LoanTransaction", "P_AccountId" + "=" + accountId, null);
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
    }

    public void deleteNewMember(int memberId) {
        this.openWritableDatabase();
        this.openReadableDatabase();
        try {

            Cursor cursor = databaseRead.rawQuery("SELECT Account_ID FROM P_Account WHERE P_MemberId = " + memberId + "", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {


                while (!cursor.isAfterLast()) {

                    int accountId = cursor.getInt(cursor.getColumnIndex("Account_ID"));

                    Cursor cursorAccountBalance = databaseRead.rawQuery("SELECT * FROM P_AccountBalance WHERE P_AccountId = " + accountId + "", null);
                    cursorAccountBalance.moveToFirst();
                    if (cursorAccountBalance.getCount() > 0) {
                        databaseWrite.delete("P_AccountBalance", "P_AccountId = " + accountId + "", null);
                        cursorAccountBalance.close();
                    }


                    databaseWrite.delete("P_Account", "Account_ID = " + accountId + "", null);
                    cursor.moveToNext();
                }
                cursor.close();
            }


            databaseWrite.delete("P_MemberView", "Id = " + memberId + " ", null);
            databaseWrite.delete("P_MemberNew", "Id" + "=" + memberId, null);

        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
    }

    public int closeWorkingDay() {
        int workingDay = dataSourceOperationsCommon.getWorkingDay();
        int position = dataSourceOperationsCommon.getWorkingDayPosition(workingDay);
        if (position >= 0 && position < 5) {
            openWritableDatabase();
            ContentValues contentValues = new ContentValues();
            if (workingDay != 1) {
                contentValues.put("OpenORClose", "Closed");
                try {
                    databaseWrite.update("Calender", contentValues, "Date = " + workingDay + "", null);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }


    }

    public void updateOldMemberDataData(int memberId, String phoneNumber, String nId, boolean updatePhone, boolean updateNid) {
        this.openWritableDatabase();
        ContentValues updateMemberData = new ContentValues();
        try {


            if (updatePhone) {

                if (phoneNumber.trim().equals("")) {
                    updateMemberData.put("Phone", phoneNumber);
                    updateMemberData.put("UpdatePhone", 0);
                } else {
                    updateMemberData.put("UpdatePhone", 1);
                    updateMemberData.put("Phone", phoneNumber);
                }

            }


            if (updateNid) {
                if (nId.trim().equals("")) {
                    updateMemberData.put("NationalIdNumber", nId);
                    updateMemberData.put("UpdateNid", 0);
                } else {
                    updateMemberData.put("NationalIdNumber", nId);
                    updateMemberData.put("UpdateNid", 1);
                }
            }
            if (updateNid || updatePhone) {
                try {
                    databaseWrite.update("P_MemberView", updateMemberData, "Id" + "=" + memberId + " ", null);
                } catch (Exception e) {
                    Log.i("Error", e.getMessage());
                }
            }


        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
    }


}
