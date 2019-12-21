package asa.org.bd.ammsma.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.extra.CalenderData;
import asa.org.bd.ammsma.extra.DynamicFieldForListViewObject;
import asa.org.bd.ammsma.extra.FundSchemeList;
import asa.org.bd.ammsma.extra.GroupNameForSpinnerObject;
import asa.org.bd.ammsma.extra.LoanDisburseData;
import asa.org.bd.ammsma.extra.MemberDetailsInfo;
import asa.org.bd.ammsma.extra.MemberExtra;
import asa.org.bd.ammsma.extra.MemberListInfo;
import asa.org.bd.ammsma.extra.MiscellaneousMemberBalance;
import asa.org.bd.ammsma.extra.NewMember;
import asa.org.bd.ammsma.extra.OverDueMember;
import asa.org.bd.ammsma.extra.ProgramNameChange;
import asa.org.bd.ammsma.extra.RealizedGroupData;
import asa.org.bd.ammsma.extra.RealizedMemberData;
import asa.org.bd.ammsma.extra.SavingsFriendly;
import asa.org.bd.ammsma.extra.SearchData;
import asa.org.bd.ammsma.extra.TransactionHistory;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountDetails;
import asa.org.bd.ammsma.jsonJavaViceVersa.Group;
import asa.org.bd.ammsma.jsonJavaViceVersa.InstallmentAmount;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanTransaction;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;
import asa.org.bd.ammsma.jsonJavaViceVersa.ProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.Schedule;


public class DataSourceRead {

    private DatabaseHelper helper;
    private SQLiteDatabase databaseRead;
    private DataSourceOperationsCommon dataSourceOperationsCommon;
    private DataSourceOperationsRead dataSourceOperationsRead;

    public DataSourceRead(Context context) {
        helper = new DatabaseHelper(context);
        dataSourceOperationsCommon = new DataSourceOperationsCommon(context);
        dataSourceOperationsRead = new DataSourceOperationsRead(context);

    }

    private static String titleCase(String givenString) {

        if (givenString.trim().contains(" ")) {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {
                stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
            }
            return stringBuffer.toString().trim();
        } else {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }

    private void openReadableDatabase() {
        databaseRead = helper.getReadableDatabase();
    }

    public int getProgramOfficerIdForLogin(String loginId, String password) {

        int programOfficerID = 0;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT User.ProgramOfficerId FROM User,P_ProgramOfficer WHERE User.Login = '"
                            + loginId + "' AND User.Password = '"
                            + password + "' AND P_ProgramOfficer.Id = User.ProgramOfficerId", null);

            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                programOfficerID = cursor.getInt(cursor.getColumnIndex("ProgramOfficerId"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return programOfficerID;

    }

    /*public void getAllProgramOfficer() {

        this.openReadableDatabase();

        try{
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT  User.ProgramOfficerId AS ProgramOfficerId , User.Login AS Login, User.Password AS Password FROM User,P_ProgramOfficer WHERE P_ProgramOfficer.Id = User.ProgramOfficerId", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {

                Log.i("PROGRAMANDPASS",cursor.getInt(cursor.getColumnIndex("ProgramOfficerId"))
                        +" / "+cursor.getString(cursor.getColumnIndex("Login"))+" / "+cursor.getString(cursor.getColumnIndex("Password"))+"\"");
                cursor.moveToNext();

            }
            cursor.close();
        }
        catch (Exception e)
        {
            Log.i("Exception",e.getMessage());
        }

    }*/

    public boolean getProgramOfficerDataIsInserted(String loginId, String password) {

        boolean isInserted = false;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT User.IsActive  FROM User,P_ProgramOfficer WHERE User.Login = '"
                            + loginId + "' AND User.Password = '"
                            + password + "' AND P_ProgramOfficer.Id = User.ProgramOfficerId", null);

            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                isInserted = cursor.getInt(cursor.getColumnIndex("IsActive")) > 0;
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return isInserted;

    }
    public String getLoginProgramOfficer(String loginId) {

        this.openReadableDatabase();
        String programOfficerId = "";

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_ProgramOfficer  INNER JOIN [User] ON P_ProgramOfficer.Id = [User].ProgramOfficerId AND [User].Login = '"+loginId+"' ", null);

            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                programOfficerId = cursor.getString(cursor.getColumnIndex("Code"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return programOfficerId;

    }

    public ArrayList<ProgramOfficer> getAllProgramOfficerForImport() {

        this.openReadableDatabase();
        ArrayList<ProgramOfficer> programOfficerList = new ArrayList<>();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT  User.ProgramOfficerId AS ProgramOfficerId , User.Login AS Login, User.Password AS Password FROM User,P_ProgramOfficer WHERE P_ProgramOfficer.Id = User.ProgramOfficerId AND User.IsActive = 1 GROUP BY User.ProgramOfficerId", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ProgramOfficer programOfficer = new ProgramOfficer();
                programOfficer.setCode(cursor.getInt(cursor.getColumnIndex("Login")));
                programOfficer.setId(cursor.getInt(cursor.getColumnIndex("ProgramOfficerId")));

                programOfficerList.add(programOfficer);

                cursor.moveToNext();

            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return programOfficerList;

    }

    public int checkAdminData(String idFromLogin, String passFromLogin) {
        this.openReadableDatabase();
        int result = 0;


        try {
            Cursor cursor = databaseRead.rawQuery("SELECT * FROM Admin WHERE Name = '" + idFromLogin + "' AND Pass = '" + passFromLogin + "'", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                result++;
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return result;
    }

    public String getLOName(int programOfficerID) {
        this.openReadableDatabase();
        String name = "";
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT Name FROM P_ProgramOfficer WHERE Id= '" + programOfficerID + "'", null);
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (name != null) {

            return name;
        } else {
            name = "LO Name is not found";
            return name;
        }
    }

    public List<GroupNameForSpinnerObject> getGroupNameWithoutBadDebt(int programOfficerId) {
        List<GroupNameForSpinnerObject> groupNames = new ArrayList<>();


        this.openReadableDatabase();


        try {
            String currentDate = dataSourceOperationsCommon.getFirstRealDate();
            String[] strArr = currentDate.split(" ");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = null;
            try {
                dt1 = format1.parse(strArr[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat format2 = new SimpleDateFormat("EEE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            String shortName = day.toUpperCase();

            Cursor res = databaseRead.rawQuery("SELECT P_Group.Name, P_Group.ID, Days.ShortName, P_DefaultProgramId AS DefaultProgramId, P_GroupTypeId FROM P_Group INNER JOIN Days ON P_Group.MeetingDay = Days.DayID where P_Group.P_ProgramOfficerId = '"
                    + programOfficerId + "' AND P_DefaultProgramId <> 999 ORDER BY (Days.ShortName = '"
                    + shortName
                    + "') DESC, Days.ShortName", null);


            res.moveToFirst();
            while (!res.isAfterLast()) {
                GroupNameForSpinnerObject singleGroupName = new GroupNameForSpinnerObject();
                String days = res.getString(res.getColumnIndex("ShortName"));

                singleGroupName.setGroupId(res.getInt(res.getColumnIndex("ID")));
                singleGroupName.setDayName(res.getString(res.getColumnIndex("ShortName")));
                singleGroupName.setGroupName(res.getString(res.getColumnIndex("Name")));
                singleGroupName.setDefaultProgramId(res.getInt(res.getColumnIndex("DefaultProgramId")));
                singleGroupName.setTotalMember(dataSourceOperationsRead.getMemberCount(res.getInt(res.getColumnIndex("ID"))));
                singleGroupName.setRealizedOrNot(dataSourceOperationsRead.getGroupInfoFromSavedData(res.getString(res.getColumnIndex("Name"))));
                singleGroupName.setGroupTypeId(res.getInt(res.getColumnIndex("P_GroupTypeId")));
                if (days.equals(shortName)) {
                    singleGroupName.setMeetingDay(true);
                } else {
                    singleGroupName.setMeetingDay(false);
                }
                groupNames.add(singleGroupName);
                res.moveToNext();
            }
            res.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return groupNames;
    }

    public List<GroupNameForSpinnerObject> getGroupNameBadDebtInLast(int programOfficerId) {
        List<GroupNameForSpinnerObject> groupNames = new ArrayList<>();


        this.openReadableDatabase();


        try {
            String currentDate = dataSourceOperationsCommon.getFirstRealDate();
            String[] strArr = currentDate.split(" ");

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = null;
            try {
                dt1 = format1.parse(strArr[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat format2 = new SimpleDateFormat("EEE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            String shortName = day.toUpperCase();

            Cursor res = databaseRead.rawQuery("SELECT P_Group.Name, P_Group.ID, Days.ShortName, P_DefaultProgramId AS DefaultProgramId, P_GroupTypeId FROM P_Group INNER JOIN Days ON P_Group.MeetingDay = Days.DayID where P_Group.P_ProgramOfficerId = "
                    + programOfficerId + "  ORDER BY (P_DefaultProgramId = 999) ASC,   (Days.ShortName = '"
                    + shortName
                    + "') DESC, P_Group.MeetingDay ASC", null);


            res.moveToFirst();
            while (!res.isAfterLast()) {
                GroupNameForSpinnerObject singleGroupName = new GroupNameForSpinnerObject();
                String days = res.getString(res.getColumnIndex("ShortName"));

                singleGroupName.setGroupId(res.getInt(res.getColumnIndex("ID")));
                singleGroupName.setDayName(res.getString(res.getColumnIndex("ShortName")));
                singleGroupName.setGroupName(res.getString(res.getColumnIndex("Name")));
                singleGroupName.setDefaultProgramId(res.getInt(res.getColumnIndex("DefaultProgramId")));
                singleGroupName.setTotalMember(dataSourceOperationsRead.getMemberCount(res.getInt(res.getColumnIndex("ID"))));
                singleGroupName.setRealizedOrNot(dataSourceOperationsRead.getGroupInfoFromSavedData(res.getString(res.getColumnIndex("Name"))));
                singleGroupName.setGroupTypeId(res.getInt(res.getColumnIndex("P_GroupTypeId")));


                if (days.equals(shortName)) {
                    singleGroupName.setMeetingDay(true);
                    if (singleGroupName.isRealizedOrNot() && singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else if (singleGroupName.isRealizedOrNot()) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ")");
                    } else if (singleGroupName.getDefaultProgramId() > 900) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + "  * (" + singleGroupName.getTotalMember()
                                + ")");
                    }
                } else {
                    singleGroupName.setMeetingDay(false);
                    if (singleGroupName.isRealizedOrNot() && singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R) (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else if (singleGroupName.isRealizedOrNot()) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R) (" + singleGroupName.getTotalMember()
                                + ")");
                    } else if (singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (" + singleGroupName.getTotalMember()
                                + ")");
                    }
                }

                groupNames.add(singleGroupName);
                res.moveToNext();
            }
            res.close();


        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return groupNames;
    }

    public int getGroupNameBadDebtInLastSize(int programOfficerId) {
        this.openReadableDatabase();
        int size = 0;


        try {
            String currentDate = dataSourceOperationsCommon.getFirstRealDate();
            String[] strArr = currentDate.split(" ");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = null;
            try {
                dt1 = format1.parse(strArr[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat format2 = new SimpleDateFormat("EEE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            String shortName = day.toUpperCase();

            Cursor cursor = databaseRead.rawQuery("SELECT P_Group.Name, P_Group.ID, Days.ShortName, P_DefaultProgramId AS DefaultProgramId, P_GroupTypeId FROM P_Group INNER JOIN Days ON P_Group.MeetingDay = Days.DayID where P_Group.P_ProgramOfficerId = "
                    + programOfficerId + "  ORDER BY (P_DefaultProgramId = 999) ASC,   (Days.ShortName = '"
                    + shortName
                    + "') DESC, P_Group.MeetingDay ASC", null);


            cursor.moveToFirst();

            size = cursor.getCount();
            cursor.close();


        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return size;
    }

    public List<GroupNameForSpinnerObject> getAllGroupNameWithoutBadDebt(int programOfficerId) {
        List<GroupNameForSpinnerObject> groupNames = new ArrayList<>();


        this.openReadableDatabase();


        try {
            String currentDate = dataSourceOperationsCommon.getFirstRealDate();
            String[] strArr = currentDate.split(" ");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = null;
            try {
                dt1 = format1.parse(strArr[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat format2 = new SimpleDateFormat("EEE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            String shortName = day.toUpperCase();

            Cursor res = databaseRead.rawQuery("SELECT P_Group.Name, P_Group.ID, Days.ShortName, P_DefaultProgramId AS DefaultProgramId, P_GroupTypeId FROM P_Group INNER JOIN Days ON P_Group.MeetingDay = Days.DayID where P_Group.P_ProgramOfficerId = "
                    + programOfficerId + " AND  P_DefaultProgramId != 999  ORDER BY (P_DefaultProgramId = 999) ASC,   (Days.ShortName = '"
                    + shortName
                    + "') DESC, P_Group.MeetingDay ASC", null);


            res.moveToFirst();
            while (!res.isAfterLast()) {
                GroupNameForSpinnerObject singleGroupName = new GroupNameForSpinnerObject();
                String days = res.getString(res.getColumnIndex("ShortName"));

                singleGroupName.setGroupId(res.getInt(res.getColumnIndex("ID")));
                singleGroupName.setDayName(res.getString(res.getColumnIndex("ShortName")));
                singleGroupName.setGroupName(res.getString(res.getColumnIndex("Name")));
                singleGroupName.setDefaultProgramId(res.getInt(res.getColumnIndex("DefaultProgramId")));
                singleGroupName.setTotalMember(dataSourceOperationsRead.getMemberCount(res.getInt(res.getColumnIndex("ID"))));
                singleGroupName.setRealizedOrNot(dataSourceOperationsRead.getGroupInfoFromSavedData(res.getString(res.getColumnIndex("Name"))));
                singleGroupName.setGroupTypeId(res.getInt(res.getColumnIndex("P_GroupTypeId")));


                if (days.equals(shortName)) {
                    singleGroupName.setMeetingDay(true);
                    if (singleGroupName.isRealizedOrNot() && singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else if (singleGroupName.isRealizedOrNot()) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ")");
                    } else if (singleGroupName.getDefaultProgramId() > 900) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + "  * (" + singleGroupName.getTotalMember()
                                + ")");
                    }
                } else {
                    singleGroupName.setMeetingDay(false);
                    if (singleGroupName.isRealizedOrNot() && singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R) (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else if (singleGroupName.isRealizedOrNot()) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R) (" + singleGroupName.getTotalMember()
                                + ")");
                    } else if (singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (" + singleGroupName.getTotalMember()
                                + ")");
                    }
                }

                groupNames.add(singleGroupName);
                res.moveToNext();
            }
            res.close();

        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return groupNames;
    }

    public List<GroupNameForSpinnerObject> getAllGroupNameWithoutBadDebtForOverDueMember(int programOfficerId) {
        List<GroupNameForSpinnerObject> groupNames = new ArrayList<>();


        this.openReadableDatabase();


        try {
            String currentDate = dataSourceOperationsCommon.getFirstRealDate();
            String[] strArr = currentDate.split(" ");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = null;
            try {
                dt1 = format1.parse(strArr[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat format2 = new SimpleDateFormat("EEE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            String shortName = day.toUpperCase();

            Cursor cursor = databaseRead.rawQuery("SELECT P_Group.Name, P_Group.ID, Days.ShortName, P_DefaultProgramId AS DefaultProgramId, P_GroupTypeId FROM P_Group INNER JOIN Days ON P_Group.MeetingDay = Days.DayID where P_Group.P_ProgramOfficerId = "
                    + programOfficerId + " AND  P_DefaultProgramId != 999  ORDER BY (P_DefaultProgramId = 999) ASC,   (Days.ShortName = '"
                    + shortName
                    + "') DESC, P_Group.MeetingDay ASC", null);


            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                GroupNameForSpinnerObject singleGroupName = new GroupNameForSpinnerObject();


                singleGroupName.setGroupId(-12345);
                singleGroupName.setDayName("All");
                singleGroupName.setGroupName("All");
                singleGroupName.setDefaultProgramId(101);
                singleGroupName.setTotalMember(-1);
                singleGroupName.setRealizedOrNot(false);
                singleGroupName.setGroupTypeId(1);
                singleGroupName.setGroupFullName("All");
                groupNames.add(singleGroupName);
            }
            while (!cursor.isAfterLast()) {
                GroupNameForSpinnerObject singleGroupName = new GroupNameForSpinnerObject();
                String days = cursor.getString(cursor.getColumnIndex("ShortName"));

                singleGroupName.setGroupId(cursor.getInt(cursor.getColumnIndex("ID")));
                singleGroupName.setDayName(cursor.getString(cursor.getColumnIndex("ShortName")));
                singleGroupName.setGroupName(cursor.getString(cursor.getColumnIndex("Name")));
                singleGroupName.setDefaultProgramId(cursor.getInt(cursor.getColumnIndex("DefaultProgramId")));
                singleGroupName.setTotalMember(dataSourceOperationsRead.getMemberCount(cursor.getInt(cursor.getColumnIndex("ID"))));
                singleGroupName.setRealizedOrNot(dataSourceOperationsRead.getGroupInfoFromSavedData(cursor.getString(cursor.getColumnIndex("Name"))));
                singleGroupName.setGroupTypeId(cursor.getInt(cursor.getColumnIndex("P_GroupTypeId")));


                if (days.equals(shortName)) {
                    singleGroupName.setMeetingDay(true);
                    if (singleGroupName.isRealizedOrNot() && singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else if (singleGroupName.isRealizedOrNot()) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ")");
                    } else if (singleGroupName.getDefaultProgramId() > 900) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R)  * (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + "  * (" + singleGroupName.getTotalMember()
                                + ")");
                    }
                } else {
                    singleGroupName.setMeetingDay(false);
                    if (singleGroupName.isRealizedOrNot() && singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R) (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else if (singleGroupName.isRealizedOrNot()) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (R) (" + singleGroupName.getTotalMember()
                                + ")");
                    } else if (singleGroupName.getDefaultProgramId() == 999) {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (" + singleGroupName.getTotalMember()
                                + ") {B}");
                    } else {
                        singleGroupName.setGroupFullName(singleGroupName.getGroupName()
                                + " - " + singleGroupName.getDayName()
                                + " (" + singleGroupName.getTotalMember()
                                + ")");
                    }
                }

                groupNames.add(singleGroupName);
                cursor.moveToNext();
            }
            cursor.close();

        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return groupNames;
    }

    public List<Member> getAllMembers(int groupID) {
        this.openReadableDatabase();

        List<Member> members = new ArrayList<>();


        Cursor cursor = databaseRead.rawQuery(
                "SELECT T_Member.Id AS Id, T_Member.Name AS Name, T_Member.FatherOrHusbandName AS FatherOrHusbandName, T_Member.PassbookNumber AS PassbookNumber, T_Member.NewStatus AS NewStatus, T_Member.Sex AS Sex, T_member.P_ProgramId AS P_ProgramId,  "
                        + " T_HasLoanAccount.HasLoan AS HasLoan, T_HasLoanAccount.LTS_Branch AS LTS_Branch  , "
                        + " T_HasLoanAccount.LTS_Tab AS LTS_Tab ,T_HasLoanAccount.LTS_Total AS LTS_Total , "
                        + " T_HasLoanAccount.Primary_Branch AS Primary_Branch , T_HasLoanAccount.Primary_Tab AS Primary_Tab , "
                        + " T_HasLoanAccount.Savings_Branch AS Savings_Branch , T_HasLoanAccount.Savings_Tab AS Savings_Tab , "
                        + " T_HasLoanAccount.CBS_Branch AS CBS_Branch , T_HasLoanAccount.CBS_Tab AS CBS_Tab , "
                        + " T_HasLoanAccount.Secondary_Branch AS Secondary_Branch , "
                        + " T_HasLoanAccount.Supplementary_Branch AS Supplementary_Branch ,  T_Member.AdmissionDate AS AdmissionDate FROM (SELECT * FROM P_MemberView WHERE P_GroupId = "
                        + groupID
                        + "  ORDER BY PassbookNumber) AS T_Member "
                        + "LEFT JOIN "
                        + "( SELECT SUM(CASE WHEN P_Account.P_ProgramId > 100 AND P_Account.P_ProgramId < 200 THEN 1 ELSE 0 END) AS HasLoan  , MAX(P_Account.P_MemberId) AS P_MemberId , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId = 204 AND  P_Account.NewLoan = 0 AND P_Account.NewAccount = 0 THEN 1 ELSE 0 END)  AS LTS_Branch , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId = 204 AND  P_Account.NewLoan = 0 AND P_Account.NewAccount = 1 THEN 1 ELSE 0 END) AS LTS_Tab , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId >200 AND  P_Account.P_ProgramId <300 AND P_Account.P_ProgramId != 204 AND  P_Account.NewLoan = 0 AND P_Account.NewAccount = 0 THEN 1 ELSE 0 END)  AS Savings_Branch , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId >200 AND  P_Account.P_ProgramId <300 AND P_Account.P_ProgramId != 204 AND  P_Account.NewLoan = 0 AND P_Account.NewAccount = 1 THEN 1 ELSE 0 END) AS Savings_Tab , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId >300 AND  P_Account.P_ProgramId <400 AND  P_Account.NewLoan = 0 AND P_Account.NewAccount = 0 THEN 1 ELSE 0 END)  AS CBS_Branch , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId >300 AND  P_Account.P_ProgramId <400 AND  P_Account.NewLoan = 0 AND P_Account.NewAccount = 1 THEN 1 ELSE 0 END) AS CBS_Tab , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId = 204 AND  P_Account.NewLoan = 0  THEN 1 ELSE 0 END)  AS LTS_Total , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId > 100 AND P_Account.P_ProgramId < 200 AND P_Program.IsPrimary =1 AND P_Account.IsSupplementary = 0  AND P_Account.NewLoan = 0 THEN 1 ELSE 0 END)  AS Primary_Branch , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId > 100 AND P_Account.P_ProgramId < 200 AND P_Program.IsPrimary =1 AND P_Account.IsSupplementary = 0 AND P_Account.NewLoan = 1 THEN 1 ELSE 0 END) AS Primary_Tab , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId > 100 AND P_Account.P_ProgramId < 200 AND P_Program.IsPrimary =0 AND P_Account.NewLoan = 0 THEN 1 ELSE 0 END)  AS Secondary_Branch , "
                        + " SUM(CASE WHEN P_Account.P_ProgramId > 100 AND P_Account.P_ProgramId < 200 AND P_Program.IsPrimary =1 AND P_Account.IsSupplementary = 1 AND P_Account.NewLoan = 0 THEN 1 ELSE 0 END) AS Supplementary_Branch "
                        + " FROM P_Account "
                        + " INNER JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId    GROUP BY P_Account.P_MemberId ) AS T_HasLoanAccount ON T_Member.Id =T_HasLoanAccount.P_MemberId GROUP BY Id ORDER BY PassbookNumber", null);
        cursor.moveToFirst();

        try {
            while (!cursor.isAfterLast()) {

                Member member = new Member();
                member.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                member.setName(cursor.getString(cursor.getColumnIndex("Name")));
                member.setFatherName(cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")));
                member.setPassbookNumber(cursor.getInt(cursor.getColumnIndex("PassbookNumber")));
                member.setMemberOldOrNew(cursor.getString(cursor.getColumnIndex("NewStatus")));
                member.setSex(cursor.getInt(cursor.getColumnIndex("Sex")));
                member.setProgramId(cursor.getInt(cursor.getColumnIndex("P_ProgramId")));
                member.setStatus(cursor.getInt(cursor.getColumnIndex("HasLoan")));
                member.setAdmissionDateInteger(cursor.getInt(cursor.getColumnIndex("AdmissionDate")));

                MemberExtra memberExtra = new MemberExtra();

                memberExtra.setPrimaryBranchLoan(cursor.getInt(cursor.getColumnIndex("Primary_Branch")));
                memberExtra.setPrimaryTabLoan(cursor.getInt(cursor.getColumnIndex("Primary_Tab")));

                memberExtra.setSecondaryBranchLoan(cursor.getInt(cursor.getColumnIndex("Secondary_Branch")));
                memberExtra.setSupplementaryLoan(cursor.getInt(cursor.getColumnIndex("Supplementary_Branch")));
                memberExtra.setLongTermSavingsBranch(cursor.getInt(cursor.getColumnIndex("LTS_Branch")));
                memberExtra.setLongTermSavingsTab(cursor.getInt(cursor.getColumnIndex("LTS_Tab")));
                memberExtra.setLongTermSavingsTotal(cursor.getInt(cursor.getColumnIndex("LTS_Total")));
                memberExtra.setSavingsBranch(cursor.getInt(cursor.getColumnIndex("Savings_Branch")));
                memberExtra.setSavingsTab(cursor.getInt(cursor.getColumnIndex("Savings_Tab")));
                memberExtra.setCbsBranch(cursor.getInt(cursor.getColumnIndex("CBS_Branch")));
                memberExtra.setCbsTab(cursor.getInt(cursor.getColumnIndex("CBS_Tab")));
                member.setMemberExtra(memberExtra);


                members.add(member);

                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {

            Log.i("Exception", e.getMessage());
        }
        return members;
    }

    public DynamicFieldForListViewObject getAccountForAccountInfo(int memberId) {


        this.openReadableDatabase();

        DynamicFieldForListViewObject dynamicFieldForListViewObject = new DynamicFieldForListViewObject();
        ArrayList<AccountForDailyTransaction> accountLoanWithCbsList = new ArrayList<>();
        ArrayList<AccountForDailyTransaction> accountSavingsList = new ArrayList<>();
        ArrayList<AccountForDailyTransaction> accountWithoutLong = new ArrayList<>();
        ArrayList<AccountForDailyTransaction> accountOnlyLong = new ArrayList<>();
        ArrayList<AccountForDailyTransaction> accountWithdrawal = new ArrayList<>();
        ArrayList<AccountForDailyTransaction> accountLoanList = new ArrayList<>();


        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        String query = "SELECT  Account_ID, ServiceChargeAmount,P_InstallmentType, OpeningDate,ProgramType, ProgramName,Cycle, DisbursedAmount, IsSupplementary, P_Account.NewAccount AS NewAccount ,P_Account.P_Duration AS P_Duration, P_Account.GracePeriod AS GracePeriod , P_Account.ReceiveDate AS ReceiveDate ,"
                + "MAX(P_Account.P_ProgramId) AS ProgramId , P_ProgramTypeId, SUM(P_AccountBalance.Debit) AS Debit, SUM(P_AccountBalance.Credit) AS Credit,"
                + " SUM( CASE WHEN P_AccountBalance.Date =" + workingDay + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedToday, SUM( CASE WHEN P_AccountBalance.Date <> " + workingDay + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedBefore, "
                + "   MAX(MinimumDeposit) AS MinimumDeposit, MAX(CASE WHEN  P_AccountBalance.Date = " + workingDay + " THEN P_AccountBalance.Type ELSE 0 END ) AS Type FROM P_Account "
                +
                "LEFT JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId WHERE P_Account.P_MemberId = "
                + memberId
                + " AND P_AccountBalance.Date <= "
                + workingDay
                + "  GROUP BY Account_ID  ORDER BY ProgramId ASC";

        try (Cursor cursor = databaseRead.rawQuery(query, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int programType = cursor.getInt(cursor.getColumnIndex("P_ProgramTypeId"));

                AccountForDailyTransaction memberAccount = new AccountForDailyTransaction();


                int account_ID = cursor.getInt(cursor.getColumnIndex("Account_ID"));
                float minimumDeposit = cursor.getFloat(cursor.getColumnIndex("MinimumDeposit"));
                int programID = cursor.getInt(cursor.getColumnIndex("ProgramId"));
                float debit = cursor.getFloat(cursor.getColumnIndex("Debit"));


                float credit = cursor.getFloat(cursor.getColumnIndex("Credit"));
                String programName = cursor.getString(cursor.getColumnIndex("ProgramName"));

                memberAccount.setAccountId(account_ID);
                memberAccount.setProgramId(programID);
                memberAccount.setProgramName(programName);
                memberAccount.setDebit(debit);
                memberAccount.setCredit(credit);
                memberAccount.setProgramTypeId(cursor.getInt(cursor.getColumnIndex("ProgramType")));
                memberAccount.setPaymentNumber(getTransactionLastDate(account_ID));
                memberAccount.setMemberId(memberId);
                memberAccount.setTermOverDue(getTermOverDue(memberId));
                memberAccount.setDuration(cursor.getInt(cursor.getColumnIndex("P_Duration")));
                memberAccount.setInstallmentType(cursor.getInt(cursor.getColumnIndex("P_InstallmentType")));


                ProgramNameChange programNameChange = getProgramNameValue(programID, programName);

                memberAccount.setProgramNameChange(programNameChange);


                int openingDateInt = cursor.getInt(cursor.getColumnIndex("OpeningDate"));


                int type = cursor.getInt(cursor.getColumnIndex("Type"));


                if (type > 0) {
                    memberAccount.setAccountStatus(1);
                } else {
                    memberAccount.setAccountStatus(0);

                }

                Schedule schedule = getScheduleInformation(account_ID, workingDay);
                Schedule scheduleValid = getInstallmentAmountFromSchedule(account_ID, workingDay);

                memberAccount.setInstallmentAmount(scheduleValid.getBaseInstallmentAmount());

                if (programType == 1 || programType == 8) {
                    int totalDisbursedAmount = (int) (cursor.getFloat(cursor.getColumnIndex("DisbursedAmount")) + cursor.getFloat(cursor.getColumnIndex("ServiceChargeAmount")));
                    int installmentNumber = schedule.getMaxInstallmentNumber();

                    int remainingInstallmentNumber = installmentNumber;
                    if (programID > 100 && programID < 200) {

                        float balance = debit - credit;

                        minimumDeposit = schedule.getBaseInstallmentAmount();
                        memberAccount.setBaseInstallmentAmount(schedule.getBaseInstallmentAmount());
                        memberAccount.setInstallmentAmount(scheduleValid.getInstallmentAmount());
                        memberAccount.setRealizedToday(cursor.getInt(cursor.getColumnIndex("RealizedToday")));
                        memberAccount.setRealizedPrevious(cursor.getInt(cursor.getColumnIndex("RealizedBefore")));
                        memberAccount.setScheduled(schedule.getScheduled());
                        if (minimumDeposit > 0) {
                            remainingInstallmentNumber = installmentNumber - (int) (((totalDisbursedAmount - balance) / minimumDeposit));
                        } else {
                            remainingInstallmentNumber = installmentNumber;
                        }


                        if (balance == 0 || remainingInstallmentNumber < 0) {
                            remainingInstallmentNumber = 0;
                        }

                        memberAccount.setBalance(balance);
                        LoanTransaction loanTransaction = getLoanTransactionAmount(account_ID, workingDay);
                        memberAccount.setLoanTransactionAmount(loanTransaction.getDebit());
                        memberAccount.setExemptedOrNot(loanTransaction.getStatus());


                        if (type > 0) {
                            float overdue = schedule.getOverDueAmount() - memberAccount.getRealizedToday() - cursor.getInt(cursor.getColumnIndex("RealizedBefore"));
                            if (overdue < 0) {
                                overdue = 0;
                            }
                            memberAccount.setOverDueAmountActual(Math.round(overdue));
                        } else {
                            if (schedule.getOverDueAmount() - cursor.getInt(cursor.getColumnIndex("RealizedBefore")) < 0) {
                                memberAccount.setOverDueAmountActual(0);
                            } else {
                                memberAccount.setOverDueAmountActual(Math.round(schedule.getOverDueAmount() - cursor.getInt(cursor.getColumnIndex("RealizedBefore"))));
                            }


                        }
                        memberAccount.setOverdueAmount(Math.round(schedule.getOverDueAmount()));
                        memberAccount.setAdvanceAmount(Math.round(schedule.getAdvanceAmount()));


                    }


                    memberAccount.setSupplementary(cursor.getInt(cursor.getColumnIndex("IsSupplementary")) > 0);
                    memberAccount.setInstallmentNumber(remainingInstallmentNumber);


                    if (getIsPrimary(programID) && !cursor.isFirst() && !(cursor.getInt(cursor.getColumnIndex("IsSupplementary")) > 0)) {
                        AccountForDailyTransaction memberAccountNotPrimary = accountLoanWithCbsList.get(0);


                        if (accountLoanWithCbsList.size() == 0) {
                            accountLoanWithCbsList.add(0, memberAccount);
                        } else {
                            accountLoanWithCbsList.set(0, memberAccount);
                        }


                        if (accountLoanList.size() == 0) {
                            accountLoanList.add(0, memberAccount);
                        } else {
                            accountLoanList.set(0, memberAccount);
                        }

                        if (accountWithoutLong.size() == 0) {
                            accountWithoutLong.add(0, memberAccount);
                        } else {
                            accountWithoutLong.set(0, memberAccount);
                        }

                        accountLoanWithCbsList.add(memberAccountNotPrimary);
                        accountLoanList.add(memberAccountNotPrimary);
                        accountWithoutLong.add(memberAccountNotPrimary);
                    } else if (cursor.getInt(cursor.getColumnIndex("IsSupplementary")) > 0 && cursor.getPosition() > 1) {

                        AccountForDailyTransaction memberAccountNotPrimarySupplementary = accountLoanWithCbsList.get(1);


                        accountLoanWithCbsList.set(1, memberAccount);
                        accountLoanList.set(1, memberAccount);
                        accountWithoutLong.set(1, memberAccount);
                        accountLoanWithCbsList.add(memberAccountNotPrimarySupplementary);
                        accountLoanList.add(memberAccountNotPrimarySupplementary);
                        accountWithoutLong.add(memberAccountNotPrimarySupplementary);
                    } else if (programID != 204) {
                        accountLoanWithCbsList.add(memberAccount);
                        accountLoanList.add(memberAccount);
                        accountWithoutLong.add(memberAccount);
                    }


                } else if (programType == 4 || programType == 2) {


                    float balance = credit - debit;

                    int gracePeriod = cursor.getInt(cursor.getColumnIndex("GracePeriod"));
                    if (gracePeriod < 0) {
                        gracePeriod = 0;
                    }
                    int receiveDate = cursor.getInt(cursor.getColumnIndex("ReceiveDate"));
                    if (receiveDate < 0) {
                        receiveDate = 0;
                    }


                    memberAccount.setBalance(balance);
                    memberAccount.setMinimumDeposit(minimumDeposit);
                    memberAccount.setCredit(getCreditForCollection(account_ID));
                    memberAccount.setDebit(getDebitForCollection(account_ID));
                    if (programType == 2) {
                        accountSavingsList.add(memberAccount);
                        if (programID != 204) {
                            accountWithoutLong.add(memberAccount);
                            memberAccount.setWithdrawPermission(getAccountWithdrawalPermission(memberAccount.getAccountId()));
                            accountWithdrawal.add(memberAccount);
                        } else {
                            memberAccount.setMissingLtsCount(missingLtsCount(account_ID, openingDateInt, receiveDate, gracePeriod, (int) minimumDeposit));
                            accountOnlyLong.add(memberAccount);
                        }
                    } else {
                        accountLoanWithCbsList.add(memberAccount);
                        if (programID != 204) {

                            accountWithoutLong.add(memberAccount);
                            memberAccount.setWithdrawPermission(getAccountWithdrawalPermission(memberAccount.getAccountId()));
                            accountWithdrawal.add(memberAccount);
                        } else {
                            memberAccount.setMissingLtsCount(missingLtsCount(account_ID, openingDateInt, receiveDate, gracePeriod, (int) minimumDeposit));
                            accountOnlyLong.add(memberAccount);
                        }
                    }

                }

                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.i("SqlErrorInMemberDetails", e.toString());
        }

        dynamicFieldForListViewObject.setLoanAndCbsList(accountLoanWithCbsList);
        dynamicFieldForListViewObject.setSavingsList(accountSavingsList);
        dynamicFieldForListViewObject.setAccountWithoutLong(accountWithoutLong);
        dynamicFieldForListViewObject.setAccountOnlyLong(accountOnlyLong);
        dynamicFieldForListViewObject.setAccountWithdrawal(accountWithdrawal);
        dynamicFieldForListViewObject.setLoanList(accountLoanList);
        return dynamicFieldForListViewObject;
    }

    public ArrayList<MemberDetailsInfo> getAccountForAccountInfoMemberDetails(int memberId) {


        this.openReadableDatabase();

        ArrayList<MemberDetailsInfo> memberDetailsInfoArrayList = new ArrayList<>();


        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        String query = "SELECT  Account_ID, ServiceChargeAmount, OpeningDate,ProgramType, ProgramName,Cycle, DisbursedAmount, IsSupplementary, P_Account.NewAccount AS NewAccount ,P_Account.P_Duration AS P_Duration, P_Account.GracePeriod AS GracePeriod , P_Account.ReceiveDate AS ReceiveDate ,"
                + "MAX(P_Account.P_ProgramId) AS ProgramId , P_ProgramTypeId, SUM(P_AccountBalance.Debit) AS Debit, SUM(P_AccountBalance.Credit) AS Credit,"
                + " SUM( CASE WHEN P_AccountBalance.Date =" + workingDay + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedToday, SUM( CASE WHEN P_AccountBalance.Date <> " + workingDay + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedBefore, "
                + "   MAX(MinimumDeposit) AS MinimumDeposit, MAX(CASE WHEN  P_AccountBalance.Date = " + workingDay + " THEN P_AccountBalance.Type ELSE 0 END ) AS Type FROM P_Account "
                +
                "LEFT JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId WHERE P_Account.P_MemberId = "
                + memberId
                + " AND P_AccountBalance.Date <= "
                + workingDay
                + "  GROUP BY Account_ID  ORDER BY ProgramId ASC";

        try (Cursor cursor = databaseRead.rawQuery(query, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int programType = cursor.getInt(cursor.getColumnIndex("P_ProgramTypeId"));
                MemberDetailsInfo memberDetailsInfo = new MemberDetailsInfo();


                int account_ID = cursor.getInt(cursor.getColumnIndex("Account_ID"));
                float minimumDeposit = cursor.getFloat(cursor.getColumnIndex("MinimumDeposit"));
                int programID = cursor.getInt(cursor.getColumnIndex("ProgramId"));
                float debit = cursor.getFloat(cursor.getColumnIndex("Debit"));


                float credit = cursor.getFloat(cursor.getColumnIndex("Credit"));
                String programName = cursor.getString(cursor.getColumnIndex("ProgramName"));
                ProgramNameChange programNameChange = getProgramNameValue(programID, programName);
                memberDetailsInfo.setProgramName(programName);
                memberDetailsInfo.setAccountId(account_ID);
                memberDetailsInfo.setProgramNameChange(programNameChange);

                int openingDateInt = cursor.getInt(cursor.getColumnIndex("OpeningDate"));

                String openingDateString = new DateAndDataConversion().getDateFromInt(openingDateInt);

                memberDetailsInfo.setDisburseOrSavingOpeningDate(openingDateString);
                memberDetailsInfo.setProgramId(programID);

                memberDetailsInfo.setInstallmentAmountOrMinimumDeposit(String.valueOf(Math.round(minimumDeposit)));

                int type = cursor.getInt(cursor.getColumnIndex("Type"));
                Schedule schedule = getScheduleInformation(account_ID, workingDay);

                if (programType == 1 || programType == 8) {
                    int totalDisbursedAmount = (int) (cursor.getFloat(cursor.getColumnIndex("DisbursedAmount")) + cursor.getFloat(cursor.getColumnIndex("ServiceChargeAmount")));
                    int installmentNumber = schedule.getMaxInstallmentNumber();

                    int remainingInstallmentNumber;
                    if (programID > 100 && programID < 200) {

                        float balance = debit - credit;

                        minimumDeposit = schedule.getBaseInstallmentAmount();
                        memberDetailsInfo.setInstallmentAmountOrMinimumDeposit(String.valueOf(Math.round(schedule.getBaseInstallmentAmount())));
                        if (minimumDeposit > 0) {
                            remainingInstallmentNumber = installmentNumber - (int) (((totalDisbursedAmount - balance) / minimumDeposit));
                        } else {
                            remainingInstallmentNumber = installmentNumber;
                        }


                        if (balance == 0 || remainingInstallmentNumber < 0) {
                            remainingInstallmentNumber = 0;
                        }


                        if (type > 0) {
                            float overdue = schedule.getOverDueAmount() - cursor.getInt(cursor.getColumnIndex("RealizedToday")) - cursor.getInt(cursor.getColumnIndex("RealizedBefore"));
                            if (overdue < 0) {
                                overdue = 0;
                            }
                            memberDetailsInfo.setOverdueAmountActual(String.valueOf(Math.round(overdue)));
                        } else {
                            if (schedule.getOverDueAmount() - cursor.getInt(cursor.getColumnIndex("RealizedBefore")) < 0) {
                                memberDetailsInfo.setOverdueAmountActual("0");
                            } else {
                                memberDetailsInfo.setOverdueAmountActual(String.valueOf(Math.round(schedule.getOverDueAmount() - cursor.getInt(cursor.getColumnIndex("RealizedBefore")))));
                            }


                        }

                        memberDetailsInfo.setDisburseAmountWithServiceCharge(String.valueOf(totalDisbursedAmount));
                        memberDetailsInfo.setDisbursePrincipal((String.valueOf(Math.round(cursor.getFloat(cursor.getColumnIndex("DisbursedAmount"))))));
                        memberDetailsInfo.setOutstandingAmountOrBalance(String.valueOf(Math.round(balance)));
                        memberDetailsInfo.setOverdueAmount(String.valueOf(Math.round(schedule.getOverDueAmount())));
                        memberDetailsInfo.setRemainingInstallmentNumber(String.valueOf(remainingInstallmentNumber));
                        memberDetailsInfo.setCycle(String.valueOf(cursor.getInt(cursor.getColumnIndex("Cycle"))));
                        memberDetailsInfo.setSupplementary(cursor.getInt(cursor.getColumnIndex("IsSupplementary")) > 0);
                        memberDetailsInfo.setAdvanceAmount(String.valueOf(Math.round(schedule.getAdvanceAmount())));


                    }


                    if (getIsPrimary(programID) && !cursor.isFirst() && !(cursor.getInt(cursor.getColumnIndex("IsSupplementary")) > 0)) {
                        MemberDetailsInfo memberDetailsInfoNotPrimary = memberDetailsInfoArrayList.get(0);

                        if (memberDetailsInfoArrayList.size() == 0) {
                            memberDetailsInfoArrayList.add(0, memberDetailsInfo);
                        } else {
                            memberDetailsInfoArrayList.set(0, memberDetailsInfo);
                        }
                        memberDetailsInfoArrayList.add(memberDetailsInfoNotPrimary);


                    } else if (cursor.getInt(cursor.getColumnIndex("IsSupplementary")) > 0 && cursor.getPosition() > 1) {


                        MemberDetailsInfo memberDetailsInfoNotPrimaryAndSupplementary = memberDetailsInfoArrayList.get(1);

                        memberDetailsInfoArrayList.set(1, memberDetailsInfo);
                        memberDetailsInfoArrayList.add(memberDetailsInfoNotPrimaryAndSupplementary);

                    } else if (programID != 204) {
                        memberDetailsInfoArrayList.add(memberDetailsInfo);
                    }


                } else if (programType == 4 || programType == 2) {


                    if (programID >= 200 && programID < 300 && programID != 204) {
                        memberDetailsInfo.setServiceChargeInterest(serviceChargeInterest(account_ID));
                    }

                    float balance = credit - debit;

                    memberDetailsInfo.setOutstandingAmountOrBalance(String.valueOf(Math.round(balance)));
                    if (programID == 204) {

                        int gracePeriod = cursor.getInt(cursor.getColumnIndex("GracePeriod"));
                        if (gracePeriod < 0) {
                            gracePeriod = 0;
                        }
                        int receiveDate = cursor.getInt(cursor.getColumnIndex("ReceiveDate"));
                        if (receiveDate < 0) {
                            receiveDate = 0;
                        }

                        memberDetailsInfo.setMissingLtsPremium(missingLtsCount(account_ID, openingDateInt, receiveDate, gracePeriod, (int) minimumDeposit));
                    }
                    memberDetailsInfoArrayList.add(memberDetailsInfo);

                }

                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.i("SqlErrorInMemberDetails", e.toString());
        }

        return memberDetailsInfoArrayList;
    }

    public String getBranchName() {
        this.openReadableDatabase();
        String branchName = " ";


        try {
            Cursor cursor = databaseRead.rawQuery("SELECT Name FROM Branch", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                branchName = cursor.getString(cursor.getColumnIndex("Name"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return branchName;
    }

    public int getBranchCode() {
        this.openReadableDatabase();
        int branchCode = 0;


        try {
            Cursor cursor = databaseRead.rawQuery("SELECT BranchID FROM Branch", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                branchCode = cursor.getInt(cursor.getColumnIndex("BranchID"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return branchCode;
    }
    public int getBranchType() {
        this.openReadableDatabase();
        int branchType = 0;


        try {
            Cursor cursor = databaseRead.rawQuery("SELECT BranchType FROM Branch", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                branchType = cursor.getInt(cursor.getColumnIndex("BranchType"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return branchType;
    }

    private LoanTransaction getLoanTransactionAmount(int accountID, int workingDay) {
        this.openReadableDatabase();

        LoanTransaction loanTransaction = new LoanTransaction();
        try (Cursor res = databaseRead.rawQuery("SELECT * FROM P_LoanTransaction WHERE P_AccountId = '" + accountID + "' AND Date = '" + workingDay + "'", null)) {
            res.moveToFirst();
            if (res.getCount() > 0) {
                boolean status = res.getInt(res.getColumnIndex("Status")) > 0;
                loanTransaction.setDebit(res.getFloat(res.getColumnIndex("Debit")));
                loanTransaction.setStatus(status);
            } else {
                loanTransaction.setDebit((float) 0);
                loanTransaction.setStatus(false);
            }
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return loanTransaction;
    }

    private Schedule getInstallmentAmountFromSchedule(int account_ID, int workingDay) {
        this.openReadableDatabase();
        Schedule schedule = new Schedule();

        String queryGetLoan = "SELECT * FROM P_Schedule WHERE P_AccountId = '" + account_ID + "' AND ScheduledDate = " + workingDay + "";
        try (Cursor cursor = databaseRead.rawQuery(queryGetLoan, null)) {
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {

                schedule.setOverDueAmount(cursor.getFloat(cursor.getColumnIndex("OverdueAmount")));
                schedule.setBaseInstallmentAmount(cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount")));
                schedule.setMaxInstallmentNumber(cursor.getInt(cursor.getColumnIndex("MaxInstallmentNumber")));
                schedule.setInstallmentAmount(cursor.getFloat(cursor.getColumnIndex("InstallmentAmount")));
                schedule.setAdvanceAmount(cursor.getFloat(cursor.getColumnIndex("AdvanceAmount")));
                schedule.setPaidAmount(cursor.getFloat(cursor.getColumnIndex("PaidAmount")));
            } else {
                schedule.setOverDueAmount((float) 0);
                schedule.setBaseInstallmentAmount((float) 0);
                schedule.setMaxInstallmentNumber(0);
                schedule.setInstallmentAmount((float) 0);
                schedule.setAdvanceAmount((float) 0);
            }
        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return schedule;
    }

    private Schedule getScheduleInformation(int accountID, int workingDay) {
        this.openReadableDatabase();

        Schedule schedule = new Schedule();

        String queryGetLoan = "Select OverdueAmount, BaseInstallmentAmount ,PaidAmount, MaxInstallmentNumber, InstallmentAmount, AdvanceAmount, ScheduledDate, NextDate  From P_Schedule WHERE P_AccountId = '"
                + accountID + "' AND (" + workingDay
                + " BETWEEN ScheduledDate AND NextDate)";
        try {
            Cursor cursor = databaseRead.rawQuery(queryGetLoan, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                schedule.setOverDueAmount(cursor.getFloat(cursor.getColumnIndex("OverdueAmount")));
                schedule.setBaseInstallmentAmount(cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount")));
                schedule.setMaxInstallmentNumber(cursor.getInt(cursor.getColumnIndex("MaxInstallmentNumber")));
                schedule.setInstallmentAmount(cursor.getFloat(cursor.getColumnIndex("InstallmentAmount")));
                schedule.setAdvanceAmount(cursor.getFloat(cursor.getColumnIndex("AdvanceAmount")));
                schedule.setPaidAmount(cursor.getFloat(cursor.getColumnIndex("PaidAmount")));
                if (workingDay == cursor.getInt(cursor.getColumnIndex("ScheduledDate"))) {
                    schedule.setScheduled(true);
                } else {
                    schedule.setScheduled(false);
                }
                schedule.setId(-777);

            } else {
                schedule.setOverDueAmount((float) 0);
                schedule.setBaseInstallmentAmount((float) 0);
                schedule.setMaxInstallmentNumber(0);
                schedule.setInstallmentAmount((float) 0);
                schedule.setAdvanceAmount((float) 0);
                schedule.setPaidAmount((float) 0);
                schedule.setScheduled(false);
                schedule.setId(-555);
            }
            cursor.close();
        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return schedule;
    }

    private Schedule getScheduleInformationWithOutWorkingDay(int accountID) {
        this.openReadableDatabase();

        Schedule schedule = new Schedule();

        String queryGetLoan = "Select OverdueAmount, BaseInstallmentAmount , MaxInstallmentNumber, InstallmentAmount, AdvanceAmount  From P_Schedule WHERE P_AccountId = '"
                + accountID + "'";
        try {
            Cursor cursor = databaseRead.rawQuery(queryGetLoan, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                schedule.setOverDueAmount(cursor.getFloat(cursor.getColumnIndex("OverdueAmount")));
                schedule.setBaseInstallmentAmount(cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount")));
                schedule.setMaxInstallmentNumber(cursor.getInt(cursor.getColumnIndex("MaxInstallmentNumber")));
                schedule.setInstallmentAmount(cursor.getFloat(cursor.getColumnIndex("InstallmentAmount")));
                schedule.setAdvanceAmount(cursor.getFloat(cursor.getColumnIndex("AdvanceAmount")));
            }
            cursor.close();
        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return schedule;
    }

    public ArrayList<TransactionHistory> getTransactionHistory(int accountID) {

        ArrayList<TransactionHistory> transactionHistories = new ArrayList<>();
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery("SELECT * from P_AccountDetails WHERE P_AccountId = '" + accountID + "' AND NewlyCreated = 0 ORDER BY P_LoanTransactionDate ASC", null);
            cursor.moveToFirst();

            if (cursor.getCount() > 25) {
                for (int i = (cursor.getCount() - 25); i <= cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    TransactionHistory transactionHistory = new TransactionHistory();

                    transactionHistory.setDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("P_LoanTransactionDate"))));
                    transactionHistory.setAmount(String.valueOf(Math.round(cursor.getFloat(cursor.getColumnIndex("Amount")))));
                    transactionHistory.setType(getType(cursor.getInt(cursor.getColumnIndex("Type"))));
                    transactionHistory.setProcess(getProcess(cursor.getInt(cursor.getColumnIndex("Process"))));
                    transactionHistories.add(transactionHistory);
                }

            } else {
                while (!cursor.isAfterLast()) {
                    TransactionHistory transactionHistory = new TransactionHistory();

                    transactionHistory.setDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("P_LoanTransactionDate"))));
                    transactionHistory.setAmount(String.valueOf(Math.round(cursor.getFloat(cursor.getColumnIndex("Amount")))));
                    transactionHistory.setType(getType(cursor.getInt(cursor.getColumnIndex("Type"))));
                    transactionHistory.setProcess(getProcess(cursor.getInt(cursor.getColumnIndex("Process"))));
                    transactionHistories.add(transactionHistory);

                    cursor.moveToNext();
                }
            }

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return transactionHistories;
    }

    private int getTransactionLastDate(int accountID) {


        this.openReadableDatabase();

        int count = 1000000;


        try {
            Cursor res = databaseRead.rawQuery("SELECT * from P_AccountDetails WHERE P_AccountId = '" + accountID + "'", null);
            res.moveToFirst();

            count = res.getCount();

            res.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return count;
    }

    private String getType(int type) {
        this.openReadableDatabase();
        String typeName = null;

        try {
            Cursor cursor = databaseRead.rawQuery("select TypeName from P_Type WHERE Type = '" + type + "'", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                typeName = cursor.getString(cursor.getColumnIndex("TypeName"));
            }
            cursor.close();
            if (typeName == null) {
                typeName = "NOT_FOUND";
            }
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return typeName;
    }

    private String getProcess(int process) {
        this.openReadableDatabase();
        String processName = null;

        try {
            Cursor cursor = databaseRead.rawQuery("select ProcessName from P_Process WHERE Process = '"
                    + process + "'", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                processName = cursor.getString(cursor.getColumnIndex("ProcessName"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return processName;
    }

    private int getOverdueOrPaidAmount(int accountId) {
        this.openReadableDatabase();
        int amount = 0;
        int workingDay = dataSourceOperationsCommon.getWorkingDay();
        String queryGetLoan = "SELECT OverdueAmount , PaidAmount, AdvanceAmount, ScheduledDate  FROM P_Schedule WHERE P_AccountId = '"
                + accountId + "' AND ('" + workingDay + "' BETWEEN ScheduledDate AND NextDate)";
        try (Cursor res = databaseRead.rawQuery(queryGetLoan, null)) {
            res.moveToFirst();
            if (res.getCount() > 0) {

                amount = (int) (res.getFloat(res.getColumnIndex("OverdueAmount")));
            }
        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return amount;
    }

    public int getMaxAmountForLoanCollection(int accountId) {
        this.openReadableDatabase();
        int amount = 0;
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        String query = "SELECT OverdueAmount, BaseInstallmentAmount, ScheduledDate, PaidAmount ,AdvanceAmount,  NextDate , InstallmentAmount FROM P_Schedule WHERE P_AccountId = "
                + accountId + "  AND (" + workingDay + " BETWEEN ScheduledDate AND NextDate ) ORDER BY ScheduledDate";
        try (Cursor cursor = databaseRead.rawQuery(query, null)) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {

                String quarryPreviousBalance = "SELECT SUM(CASE WHEN Date = " + cursor.getInt(cursor.getColumnIndex("ScheduledDate")) + " THEN Credit ELSE 0 END  ) AS Balance, SUM(CASE WHEN Date = " + workingDay + " THEN Credit ELSE 0 END  ) AS RealizedToday , SUM(CASE WHEN Date <> " + cursor.getInt(cursor.getColumnIndex("ScheduledDate")) + " THEN Credit ELSE 0 END  ) AS PreviousBalance FROM P_AccountBalance WHERE P_AccountId = " + accountId + "   AND Type > 0";
                Cursor cursorBalance = databaseRead.rawQuery(quarryPreviousBalance, null);
                cursorBalance.moveToFirst();

                if (cursor.getInt(cursor.getColumnIndex("ScheduledDate")) == workingDay) {
                    if (cursor.getFloat(cursor.getColumnIndex("OverdueAmount")) == 0) {
                        amount = (int) (cursor.getFloat(cursor.getColumnIndex("PaidAmount")) + cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount"))
                                - cursorBalance.getInt(cursorBalance.getColumnIndex("PreviousBalance")) - cursorBalance.getInt(cursorBalance.getColumnIndex("Balance"))
                                - cursor.getFloat(cursor.getColumnIndex("AdvanceAmount")) + cursorBalance.getInt(cursorBalance.getColumnIndex("RealizedToday")));
                    } else {
                        amount = (int) (cursor.getFloat(cursor.getColumnIndex("OverdueAmount")) + cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount"))
                                - cursorBalance.getInt(cursorBalance.getColumnIndex("PreviousBalance")) - cursorBalance.getInt(cursorBalance.getColumnIndex("Balance")) - cursor.getFloat(cursor.getColumnIndex("AdvanceAmount"))
                                + cursorBalance.getInt(cursorBalance.getColumnIndex("RealizedToday")));
                    }

                } else {


                    if (cursor.getFloat(cursor.getColumnIndex("OverdueAmount")) == 0) {

                        amount = (int) (cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount")) - cursor.getFloat(cursor.getColumnIndex("AdvanceAmount"))
                                - cursorBalance.getInt(cursorBalance.getColumnIndex("Balance")) - cursorBalance.getInt(cursorBalance.getColumnIndex("PreviousBalance")) + cursorBalance.getInt(cursorBalance.getColumnIndex("RealizedToday")));
                    } else {
                        if (cursor.getInt(cursor.getColumnIndex("ScheduledDate")) < dataSourceOperationsCommon.getCalenderFirstDay()) {
                            amount = (int) (cursor.getFloat(cursor.getColumnIndex("OverdueAmount"))
                                    - cursorBalance.getInt(cursorBalance.getColumnIndex("Balance")) - cursorBalance.getInt(cursorBalance.getColumnIndex("PreviousBalance"))
                                    - cursor.getFloat(cursor.getColumnIndex("AdvanceAmount")) + cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount")) + cursorBalance.getInt(cursorBalance.getColumnIndex("RealizedToday")));
                        } else {
                            amount = (int) (cursor.getFloat(cursor.getColumnIndex("OverdueAmount"))
                                    - cursorBalance.getInt(cursorBalance.getColumnIndex("Balance")) - cursorBalance.getInt(cursorBalance.getColumnIndex("PreviousBalance"))
                                    - cursor.getFloat(cursor.getColumnIndex("AdvanceAmount")) + cursor.getFloat(cursor.getColumnIndex("BaseInstallmentAmount")) + cursorBalance.getInt(cursorBalance.getColumnIndex("RealizedToday")));
                        }
                    }

                }
                cursorBalance.close();
            }
        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return amount;
    }

    public int getInitialBalanceForLoan(int accountID) {
        this.openReadableDatabase();
        int initialAmount = 0;


        String queryGetBalance = "Select Debit From P_AccountBalance WHERE P_AccountId = '"
                + accountID + "' AND Credit = '0' AND Type = '0'";
        try {
            Cursor cursor = databaseRead.rawQuery(queryGetBalance, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                initialAmount = (int) cursor.getFloat(cursor.getColumnIndex("Debit"));
            }
            cursor.close();
        } catch (SQLException e) {
            Log.i("BalanceForLoan", e.toString());
        }
        return initialAmount;
    }

    private float getCreditForCollection(int accountId) {
        this.openReadableDatabase();
        float credit = 0;

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT SUM(Credit) AS CreditSum FROM P_AccountBalance WHERE P_AccountId = '"
                            + accountId + "' AND Credit >0 AND Type <>'0' AND Date = " + dataSourceOperationsCommon.getWorkingDay() + " ", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                credit = cursor.getFloat(cursor.getColumnIndex("CreditSum"));

            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return credit;
    }
    private float getDebitForCollection(int accountId) {
        this.openReadableDatabase();
        float debit = 0;

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT SUM(Debit) AS DebitSum FROM P_AccountBalance WHERE P_AccountId = '"
                            + accountId + "' AND Debit >0 AND Type <>'0' AND Date = " + dataSourceOperationsCommon.getWorkingDay() + " ", null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                debit = cursor.getFloat(cursor.getColumnIndex("DebitSum"));

            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return debit;
    }

    public int getTransactionHistoryCheck(int accountID) {
        this.openReadableDatabase();
        int flag = 0;
        String queryStr = "SELECT * FROM P_AccountDetails WHERE P_AccountId = "
                + accountID + " AND (Process =1 OR Process =2) AND  P_LoanTransactionDate != " + dataSourceOperationsCommon.getWorkingDay() + " ORDER BY P_LoanTransactionDate DESC LIMIT 1";

        try {
            Cursor res = databaseRead.rawQuery(queryStr, null);
            res.moveToFirst();
            if (res.getCount() > 0) {


                String date = new DateAndDataConversion().getDateFromInt(res.getInt(res.getColumnIndex("P_LoanTransactionDate")));
                String currentDate = new DateAndDataConversion().getDateFromInt(dataSourceOperationsCommon.getWorkingDay());
                String arr[] = date.split("/");
                String arr2[] = currentDate.split("/");
                if (arr[1].equals(arr2[1])) {
                    flag = 1;
                }
            }
            res.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return flag;
    }

    public List<String> getSevenWorkingDaysFromRealizable() {


        List<String> realDate = new ArrayList<>();
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery("SELECT RealDate from Calender ORDER BY Calender_Id", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {


                if (cursor.getPosition() == 7) {
                    break;
                }
                String realDateStr = cursor.getString(cursor.getColumnIndex("RealDate"));
                realDate.add(realDateStr);

                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return realDate;
    }

    public List<RealizedGroupData> getRealizedGroupDataSummary(int date, int programOfficerId) {
        List<RealizedGroupData> realizedGroupsInformation = new ArrayList<>();
        this.openReadableDatabase();

        float loanCollectionTotal = 0;
        float savingsDepositTotal = 0;
        float savingsDepositWithoutLtsTotal = 0;
        float ltsDepositTotal = 0;
        float cbsDepositTotal = 0;
        float savingsWithdrawalTotal = 0;
        float cbsWithdrawalTotal = 0;
        float badDebtCollectionTotal = 0;
        float exemptionTotalTotal = 0;
        float totalCollectionTotal = 0;
        float totalWithdrawalTotal = 0;
        float netCollectionTotal = 0;
        float loanRealizableTotal = 0;


        try {

            Cursor cursor = databaseRead.rawQuery(
                    "SELECT T1.GroupId , T1.GroupName, T1.GroupMeetingDay,T2.SavingsDeposit, T2.LoanCollection, T2.SecondaryCollection, T2.LoanCollectionALL, T4.Realizable,T4.Realizable2, T2.SavingsDepositWithoutLTS, T2.LTSDeposit, T2.CbsDeposit, T2.SavingsWithdrawal, T2.CbsWithdrawal, T2.BadDebtCollection, T3.ExemptionTotal  FROM "
                            + "(SELECT  P_Group.ID AS GroupId ,P_Group.Name AS GroupName,Days.ShortName AS GroupMeetingDay "
                            + " FROM P_Account INNER JOIN P_AccountBalance"
                            + " ON P_AccountBalance.P_AccountId = P_Account.Account_ID"
                            + " AND P_AccountBalance.Date = "
                            + date
                            + " INNER JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId"
                            + " INNER JOIN P_Group ON P_Group.ID =P_MemberView.P_GroupId"
                            + " INNER JOIN Days ON Days.DayID = P_Group.MeetingDay"
                            + " AND P_ProgramOfficerId = '"
                            + programOfficerId
                            + "' GROUP BY GroupName, GroupId) AS T1 "
                            + "LEFT JOIN  (SELECT P_Group.Id AS GroupId , P_Group.Name AS GroupName, "
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 1 AND P_AccountBalance.Type = 4  THEN Credit ELSE 0 END) AS LoanCollection ,"
                            + " SUM( CASE WHEN P_Account.P_ProgramTypeId = 1 AND  P_Program.IsPrimary = 0 AND P_AccountBalance.Type = 4 THEN P_AccountBalance.Credit ELSE 0 END) AS SecondaryCollection, "
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 1   THEN Credit ELSE 0 END) AS LoanCollectionALL ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 1024 THEN Credit ELSE 0 END) AS SavingsDeposit ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 1024 AND P_Account.P_ProgramId <> 204 THEN Credit ELSE 0 END) AS SavingsDepositWithoutLTS ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 1024 AND P_Account.P_ProgramId = 204 THEN Credit ELSE 0 END) AS LTSDeposit ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 4 AND P_AccountBalance.Type = 131072 THEN Credit ELSE 0 END) AS CbsDeposit ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 16386 THEN debit ELSE 0 END) AS SavingsWithdrawal ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 4 AND P_AccountBalance.Type = 48576 THEN debit ELSE 0 END) AS CbsWithdrawal ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 8 THEN Credit ELSE 0 END) AS BadDebtCollection "
                            + " FROM P_Account INNER JOIN P_AccountBalance   "
                            + " ON P_AccountBalance.P_AccountId = P_Account.Account_ID "
                            + " AND P_AccountBalance.Date = "
                            + date
                            + " INNER JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId "
                            + " INNER JOIN P_Group ON P_Group.Id =P_MemberView.P_GroupId "
                            + " AND P_Group.P_ProgramOfficerId = "
                            + programOfficerId
                            + " LEFT JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId "
                            + " GROUP BY GroupName, GroupId) AS T2  ON T1.GroupId = T2.GroupId "
                            + "LEFT JOIN (SELECT P_Group.Id AS GroupId , P_Group.Name AS GroupName, "
                            + " SUM(CASE WHEN P_LoanTransaction.Status = 1 THEN ROUND( Debit ) ELSE 0 END) AS ExemptionTotal"
                            + " FROM P_Account INNER JOIN P_LoanTransaction ON P_LoanTransaction.P_AccountId = P_Account.Account_ID "
                            + " INNER JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId "
                            + " INNER JOIN P_Group ON P_Group.Id =P_MemberView.P_GroupId "
                            + " AND P_Group.P_ProgramOfficerId = '"
                            + programOfficerId
                            + "' GROUP BY GroupName, GroupId) AS T3 ON T2.GroupId = T3.GroupId "
                            + "LEFT JOIN  (SELECT P_Group.Id AS GroupId , P_Group.Name AS GroupName, "
                            + " SUM (CASE WHEN P_Account.P_ProgramId >100 AND P_Account.P_ProgramId <200  THEN CASE WHEN P_Account.P_ProgramTypeId = 1  AND P_AccountBalance.Credit >  P_Schedule.InstallmentAmount THEN P_AccountBalance.Credit ELSE P_Schedule.InstallmentAmount END  ELSE 0 END) AS Realizable, "
                            + " SUM (CASE WHEN P_Account.P_ProgramId >100 AND P_Account.P_ProgramId <200 AND  P_Schedule.InstallmentAmount IS NULL AND P_AccountBalance.Credit > 0  THEN P_AccountBalance.Credit END) AS Realizable2"
                            + " FROM P_Account  "
                            + " LEFT JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId "
                            + " LEFT JOIN P_Group ON P_Group.Id =P_MemberView.P_GroupId "
                            + " LEFT JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID  AND P_AccountBalance.Date = " + date + " AND  P_AccountBalance.Credit > 0 "
                            + " LEFT JOIN P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID  "
                            + " AND  P_Schedule.ScheduledDate = " + date + " "
                            + " AND  P_Group.P_ProgramOfficerId = "
                            + programOfficerId
                            + " AND P_Account.P_ProgramId >100 AND P_Account.P_ProgramId <200 GROUP BY GroupName, GroupId) AS T4 ON T1.GroupId = T4.GroupId  ", null);


            if (cursor.getCount() == 0) {
                RealizedGroupData realizedGroupDataFinal = new RealizedGroupData();

                realizedGroupDataFinal.setGroupId(-12345);
                realizedGroupDataFinal.setGroupName("Total");
                realizedGroupDataFinal.setLoanCollection(loanCollectionTotal);
                realizedGroupDataFinal.setSavingsDeposit(savingsDepositTotal);
                realizedGroupDataFinal.setSavingsDepositWithoutLts(savingsDepositWithoutLtsTotal);
                realizedGroupDataFinal.setLtsDeposit(ltsDepositTotal);
                realizedGroupDataFinal.setCbsDeposit(cbsDepositTotal);
                realizedGroupDataFinal.setSavingsWithdrawal(savingsWithdrawalTotal);
                realizedGroupDataFinal.setCbsWithdrawal(cbsWithdrawalTotal);
                realizedGroupDataFinal.setExemptionTotal(exemptionTotalTotal);
                realizedGroupDataFinal.setBadDebtCollection(badDebtCollectionTotal);
                realizedGroupDataFinal.setTotalCollection(totalCollectionTotal);
                realizedGroupDataFinal.setTotalWithdrawal(totalWithdrawalTotal);
                realizedGroupDataFinal.setNetCollection(netCollectionTotal);
                realizedGroupDataFinal.setLoanRealizable(loanRealizableTotal);

                realizedGroupsInformation.add(realizedGroupDataFinal);
            }


            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {


                float loanCollection = cursor.getFloat(cursor.getColumnIndex("LoanCollection"));
                float savingsDeposit = cursor.getFloat(cursor.getColumnIndex("SavingsDeposit"));
                float savingsDepositWithoutLTS = cursor.getFloat(cursor.getColumnIndex("SavingsDepositWithoutLTS"));
                float ltsDeposit = cursor.getFloat(cursor.getColumnIndex("LTSDeposit"));
                float cbsDeposit = cursor.getFloat(cursor.getColumnIndex("CbsDeposit"));
                float savingsWithdrawal = cursor.getFloat(cursor.getColumnIndex("SavingsWithdrawal"));
                float cbsWithdrawal = cursor.getFloat(cursor.getColumnIndex("CbsWithdrawal"));
                float badDebtCollection = cursor.getFloat(cursor.getColumnIndex("BadDebtCollection"));
                float exemptionTotal = cursor.getFloat(cursor.getColumnIndex("ExemptionTotal"));
                float loanRealizable = cursor.getFloat(cursor.getColumnIndex("Realizable"));

                if (loanCollection > 0 || savingsDeposit > 0 || savingsDepositWithoutLTS > 0 ||
                        ltsDeposit > 0 || cbsDeposit > 0 || savingsWithdrawal > 0 ||
                        cbsWithdrawal > 0 || badDebtCollection > 0 || exemptionTotal > 0) {


                    float totalCollection = loanCollection + savingsDepositWithoutLTS + ltsDeposit + cbsDeposit + badDebtCollection;
                    float totalWithdrawal = savingsWithdrawal + cbsWithdrawal + exemptionTotal;
                    float netCollection = totalCollection - totalWithdrawal;
                    RealizedGroupData realizedGroupData = new RealizedGroupData();
                    realizedGroupData.setGroupId(cursor.getInt(cursor.getColumnIndex("GroupId")));
                    realizedGroupData.setGroupName(cursor.getString(cursor.getColumnIndex("GroupName")));
                    realizedGroupData.setLoanCollection(loanCollection);
                    realizedGroupData.setSavingsDeposit(savingsDeposit);
                    realizedGroupData.setSavingsDepositWithoutLts(savingsDepositWithoutLTS);
                    realizedGroupData.setLtsDeposit(ltsDeposit);
                    realizedGroupData.setCbsDeposit(cbsDeposit);
                    realizedGroupData.setSavingsWithdrawal(savingsWithdrawal);
                    realizedGroupData.setCbsWithdrawal(cbsWithdrawal);
                    realizedGroupData.setExemptionTotal(exemptionTotal);
                    realizedGroupData.setBadDebtCollection(badDebtCollection);
                    realizedGroupData.setTotalCollection(totalCollection);
                    realizedGroupData.setTotalWithdrawal(totalWithdrawal);
                    realizedGroupData.setNetCollection(netCollection);
                    realizedGroupData.setMeetingDay(cursor.getString(cursor.getColumnIndex("GroupMeetingDay")));
                    realizedGroupData.setLoanRealizable(loanRealizable);
                    realizedGroupsInformation.add(realizedGroupData);

                    loanCollectionTotal += loanCollection;
                    savingsDepositTotal += savingsDeposit;
                    savingsDepositWithoutLtsTotal += savingsDepositWithoutLTS;
                    ltsDepositTotal += ltsDeposit;
                    cbsDepositTotal += cbsDeposit;
                    savingsWithdrawalTotal += savingsWithdrawal;
                    cbsWithdrawalTotal += cbsWithdrawal;
                    badDebtCollectionTotal += badDebtCollection;
                    exemptionTotalTotal += exemptionTotal;
                    totalCollectionTotal += totalCollection;
                    totalWithdrawalTotal += totalWithdrawal;
                    netCollectionTotal += netCollection;
                    loanRealizableTotal += loanRealizable;
                }


                if (cursor.isLast()) {
                    RealizedGroupData realizedGroupDataFinal = new RealizedGroupData();

                    realizedGroupDataFinal.setGroupId(-12345);
                    realizedGroupDataFinal.setGroupName("Total");
                    realizedGroupDataFinal.setLoanCollection(loanCollectionTotal);
                    realizedGroupDataFinal.setSavingsDeposit(savingsDepositTotal);
                    realizedGroupDataFinal.setSavingsDepositWithoutLts(savingsDepositWithoutLtsTotal);
                    realizedGroupDataFinal.setLtsDeposit(ltsDepositTotal);
                    realizedGroupDataFinal.setCbsDeposit(cbsDepositTotal);
                    realizedGroupDataFinal.setSavingsWithdrawal(savingsWithdrawalTotal);
                    realizedGroupDataFinal.setCbsWithdrawal(cbsWithdrawalTotal);
                    realizedGroupDataFinal.setExemptionTotal(exemptionTotalTotal);
                    realizedGroupDataFinal.setBadDebtCollection(badDebtCollectionTotal);
                    realizedGroupDataFinal.setTotalCollection(totalCollectionTotal);
                    realizedGroupDataFinal.setTotalWithdrawal(totalWithdrawalTotal);
                    realizedGroupDataFinal.setNetCollection(netCollectionTotal);
                    realizedGroupDataFinal.setLoanRealizable(loanRealizableTotal);
                    realizedGroupsInformation.add(realizedGroupDataFinal);
                }
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return realizedGroupsInformation;
    }

    public List<MiscellaneousMemberBalance> getMemberBalanceInformation(int groupID) {
        this.openReadableDatabase();
        List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList = new ArrayList<>();

        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        int primaryDisbursed = 0;
        int primaryOutstanding = 0;
        int primaryOverdueAmount = 0;
        int secondaryDisbursed = 0;
        int secondaryOverDue = 0;
        int secondaryOutstanding = 0;
        int savingsBalance = 0;
        int LTSBalance = 0;
        int CBSBalance = 0;
        int netBalance = 0;


        int memberId = 0;
        String memberName = "";
        String fatherOrHusbandName = "";
        int passbookNumber = 0;
        String primaryDisbursedDate = "";
        int primaryInstallmentNumber = 0;
        boolean hasPrimary = false;


        int totalPrimaryDisbursed = 0;
        int totalPrimaryOverDue = 0;
        int totalSecondaryDisbursed = 0;
        int totalSecondaryOverdue = 0;
        int totalSecondaryOutstanding = 0;
        int totalPrimaryOutstanding = 0;
        int totalSavings = 0;
        int totalLts = 0;
        int totalCbs = 0;
        int totalNet = 0;


        try {

            Cursor cursor = databaseRead.rawQuery
                    ("SELECT P_MemberView.Id , P_MemberView.P_GroupId AS GroupId,  P_MemberView.Name, P_MemberView.PassbookNumber,P_Account.IsSupplementary AS IsSupplementary, P_Account.NewAccount AS NewAccount, "
                            + " P_MemberView.FatherOrHusbandName, Account_ID, IsSupplementary, OpeningDate, ServiceChargeAmount, DisbursedAmount,"
                            + " MAX(P_Account.P_ProgramId) AS P_ProgramId, CASE WHEN MAX(P_ProgramTypeId) IN (1,8) THEN SUM(Debit - Credit) ELSE SUM(Credit - Debit) END AS Balance,"
                            + " MAX(MinimumDeposit) AS MinimumDeposit, MAX(CASE WHEN  P_AccountBalance.Date = " + workingDay + " THEN P_AccountBalance.Type ELSE 0 END ) AS Type ," +
                            " SUM(CASE WHEN P_AccountBalance.Date =" + workingDay + " THEN  P_AccountBalance.Credit ELSE 0 END) AS RealizedToday, SUM( CASE WHEN P_AccountBalance.Date <> " + workingDay + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedBefore FROM P_Account "
                            + "INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId "
                            + " INNER JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId AND P_MemberView.P_GroupId = '"
                            + groupID
                            + "' AND P_MemberView.Status = 1 AND P_AccountBalance.Date <= '"
                            + workingDay
                            + "' Group by Account_ID ORDER BY PassbookNumber", null);


            cursor.moveToFirst();


            while (!cursor.isAfterLast()) {


                if (memberId != cursor.getInt(cursor.getColumnIndex("Id")) && memberId != 0) {

                    MiscellaneousMemberBalance miscellaneousMemberBalance = new MiscellaneousMemberBalance();

                    if (primaryOverdueAmount < 0) {
                        miscellaneousMemberBalance.setPrimaryOverdue(0);
                    } else {
                        miscellaneousMemberBalance.setPrimaryOverdue(primaryOverdueAmount);
                    }

                    miscellaneousMemberBalance.setPrimaryDisbursed(primaryDisbursed);
                    miscellaneousMemberBalance.setPrimaryOutstanding(primaryOutstanding);

                    miscellaneousMemberBalance.setSecondaryDisbursed(secondaryDisbursed);
                    miscellaneousMemberBalance.setSecondaryOutstanding(secondaryOutstanding);

                    if (secondaryOverDue < 0) {
                        miscellaneousMemberBalance.setSecondaryOverdue(0);
                    } else {
                        miscellaneousMemberBalance.setSecondaryOverdue(secondaryOverDue);
                    }

                    miscellaneousMemberBalance.setLtsBalance(LTSBalance);
                    miscellaneousMemberBalance.setSavingsBalance(savingsBalance);
                    miscellaneousMemberBalance.setCbsBalance(CBSBalance);
                    miscellaneousMemberBalance.setNetBalance(netBalance);
                    miscellaneousMemberBalance.setMemberId(memberId);
                    miscellaneousMemberBalance.setMemberName(memberName);
                    miscellaneousMemberBalance.setPassbookNumber(passbookNumber);
                    miscellaneousMemberBalance.setFatherOrHusband(fatherOrHusbandName);
                    miscellaneousMemberBalance.setPrimaryDisbursedDate(primaryDisbursedDate);
                    miscellaneousMemberBalance.setPrimaryInstallmentNumber(primaryInstallmentNumber);


                    totalPrimaryDisbursed += miscellaneousMemberBalance.getPrimaryDisbursed();
                    totalPrimaryOverDue += miscellaneousMemberBalance.getPrimaryOverdue();
                    totalSecondaryDisbursed += miscellaneousMemberBalance.getSecondaryDisbursed();
                    totalSecondaryOverdue += miscellaneousMemberBalance.getSecondaryOverdue();
                    totalSecondaryOutstanding += miscellaneousMemberBalance.getSecondaryOutstanding();
                    totalPrimaryOutstanding += miscellaneousMemberBalance.getPrimaryOutstanding();
                    totalSavings += miscellaneousMemberBalance.getSavingsBalance();
                    totalLts += miscellaneousMemberBalance.getLtsBalance();
                    totalCbs += miscellaneousMemberBalance.getCbsBalance();
                    totalNet += miscellaneousMemberBalance.getNetBalance();

                    miscellaneousMemberBalanceList.add(miscellaneousMemberBalance);


                    primaryDisbursed = primaryOutstanding = primaryOverdueAmount = secondaryDisbursed
                            = secondaryOverDue = secondaryOutstanding = savingsBalance = LTSBalance = CBSBalance = netBalance = primaryInstallmentNumber = 0;

                    memberId = passbookNumber = 0;
                    memberName = fatherOrHusbandName = primaryDisbursedDate = "";
                    hasPrimary = false;
                }

                if (memberId == 0 || memberId == cursor.getInt(cursor.getColumnIndex("Id"))) {

                    memberId = cursor.getInt(cursor.getColumnIndex("Id"));
                    memberName = cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + "(" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")";
                    passbookNumber = cursor.getInt(cursor.getColumnIndex("PassbookNumber"));
                    fatherOrHusbandName = cursor.getString(cursor.getColumnIndex("FatherOrHusbandName"));
                }

                int account_ID = cursor.getInt(cursor.getColumnIndex("Account_ID"));
                int programID = cursor.getInt(cursor.getColumnIndex("P_ProgramId"));


                if ((programID < 200 || programID == 999)) {

                    if (getIsPrimary(programID)) {
                        Schedule schedule = getScheduleInformation(account_ID, workingDay);


                        float realizedToday;

                        if (cursor.getInt(cursor.getColumnIndex("Type")) > 0) {
                            realizedToday = cursor.getFloat(cursor.getColumnIndex("RealizedToday"));
                        } else {
                            realizedToday = 0;
                        }

                        int supplementaryOrPrimaryOrSecondaryDisbursed;
                        supplementaryOrPrimaryOrSecondaryDisbursed = (int) (cursor.getFloat(cursor.getColumnIndex("ServiceChargeAmount")) + cursor.getFloat(cursor.getColumnIndex("DisbursedAmount")));
                        int loanOutstanding = (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                        int loanOverdueAmount = getOverdueOrPaidAmount(account_ID);
                        int installmentNumber = schedule.getMaxInstallmentNumber();

                        netBalance += -loanOutstanding;

                        if (cursor.getInt(cursor.getColumnIndex("IsSupplementary")) == 0) {

                            hasPrimary = true;
                            if (schedule.getBaseInstallmentAmount() > 0 && schedule.getId() == -777) {
                                primaryInstallmentNumber = installmentNumber - (Math.round((supplementaryOrPrimaryOrSecondaryDisbursed - loanOutstanding) / schedule.getBaseInstallmentAmount()));
                                if (primaryInstallmentNumber < 0) {
                                    primaryInstallmentNumber = 0;
                                }
                            } else if (schedule.getId() == -777) {
                                primaryInstallmentNumber = installmentNumber;
                                if (primaryInstallmentNumber < 0) {
                                    primaryInstallmentNumber = 0;
                                }
                            } else if (schedule.getId() == -555 && loanOutstanding > 0) {
                                primaryInstallmentNumber = 1;
                            }
                            primaryDisbursedDate = new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate")));
                        } else if (!hasPrimary && cursor.getInt(cursor.getColumnIndex("IsSupplementary")) == 1) {
                            if (schedule.getBaseInstallmentAmount() > 0 && schedule.getId() == -777) {
                                if (primaryInstallmentNumber == 0) {
                                    primaryInstallmentNumber = installmentNumber - (int) (((supplementaryOrPrimaryOrSecondaryDisbursed - loanOutstanding) / schedule.getBaseInstallmentAmount()));
                                    if (primaryInstallmentNumber < 0) {
                                        primaryInstallmentNumber = 0;
                                    }
                                }

                            } else if (schedule.getId() == -777) {
                                if (primaryInstallmentNumber == 0) {
                                    primaryInstallmentNumber = installmentNumber;
                                    if (primaryInstallmentNumber < 0) {
                                        primaryInstallmentNumber = 0;
                                    }
                                }
                            } else if (schedule.getId() == -555 && loanOutstanding > 0) {
                                primaryInstallmentNumber = 1;
                            }
                            if (primaryDisbursedDate.equals("")) {
                                primaryDisbursedDate = new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate")));
                            }

                        }


                        primaryDisbursed += supplementaryOrPrimaryOrSecondaryDisbursed;
                        primaryOutstanding += loanOutstanding;
                        if (loanOverdueAmount - realizedToday - cursor.getInt(cursor.getColumnIndex("RealizedBefore")) > 0) {
                            primaryOverdueAmount += loanOverdueAmount - realizedToday - cursor.getInt(cursor.getColumnIndex("RealizedBefore"));
                        } else {
                            primaryOverdueAmount += 0;
                        }
                    } else {
                        int supplementaryOrPrimaryOrSecondaryDisbursed;
                        supplementaryOrPrimaryOrSecondaryDisbursed = (int) (cursor.getFloat(cursor.getColumnIndex("ServiceChargeAmount")) + cursor.getFloat(cursor.getColumnIndex("DisbursedAmount")));
                        int loanOutstanding = (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                        int loanOverdueAmount = getOverdueOrPaidAmount(account_ID);
                        netBalance += -loanOutstanding;

                        float realizedToday;

                        if (cursor.getInt(cursor.getColumnIndex("Type")) > 0) {
                            realizedToday = cursor.getFloat(cursor.getColumnIndex("RealizedToday"));

                        } else {
                            realizedToday = 0;
                        }
                        secondaryDisbursed += supplementaryOrPrimaryOrSecondaryDisbursed;
                        secondaryOutstanding += loanOutstanding;

                        if (loanOverdueAmount - realizedToday - cursor.getInt(cursor.getColumnIndex("RealizedBefore")) > 0) {
                            secondaryOverDue += loanOverdueAmount - realizedToday - cursor.getInt(cursor.getColumnIndex("RealizedBefore"));
                        } else {
                            secondaryOverDue += 0;
                        }
                    }

                } else {

                    if (programID > 200 && programID < 300 && programID != 204) {

                        savingsBalance += (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                        netBalance += (int) cursor.getFloat(cursor.getColumnIndex("Balance"));

                    } else if (programID == 204) {
                        LTSBalance += (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                        netBalance += (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                    } else if (programID > 300 && programID < 400) {
                        CBSBalance += (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                        netBalance += (int) cursor.getFloat(cursor.getColumnIndex("Balance"));
                    }

                }


                if (cursor.isLast()) {
                    MiscellaneousMemberBalance miscellaneousMemberBalance = new MiscellaneousMemberBalance();
                    if (primaryOverdueAmount < 0) {
                        miscellaneousMemberBalance.setPrimaryOverdue(0);
                    } else {
                        miscellaneousMemberBalance.setPrimaryOverdue(primaryOverdueAmount);
                    }

                    miscellaneousMemberBalance.setPrimaryDisbursed(primaryDisbursed);
                    miscellaneousMemberBalance.setPrimaryOutstanding(primaryOutstanding);

                    miscellaneousMemberBalance.setSecondaryDisbursed(secondaryDisbursed);
                    miscellaneousMemberBalance.setSecondaryOutstanding(secondaryOutstanding);

                    if (secondaryOverDue < 0) {
                        miscellaneousMemberBalance.setSecondaryOverdue(0);
                    } else {
                        miscellaneousMemberBalance.setSecondaryOverdue(secondaryOverDue);
                    }


                    miscellaneousMemberBalance.setLtsBalance(LTSBalance);
                    miscellaneousMemberBalance.setSavingsBalance(savingsBalance);
                    miscellaneousMemberBalance.setCbsBalance(CBSBalance);
                    miscellaneousMemberBalance.setNetBalance(netBalance);
                    miscellaneousMemberBalance.setMemberId(memberId);
                    miscellaneousMemberBalance.setMemberName(memberName);
                    miscellaneousMemberBalance.setPassbookNumber(passbookNumber);
                    miscellaneousMemberBalance.setFatherOrHusband(fatherOrHusbandName);
                    miscellaneousMemberBalance.setPrimaryDisbursedDate(primaryDisbursedDate);
                    miscellaneousMemberBalance.setPrimaryInstallmentNumber(primaryInstallmentNumber);


                    if (miscellaneousMemberBalance.getPrimaryOverdue() < 0) {
                        totalPrimaryOverDue += 0;
                    } else {
                        totalPrimaryOverDue += miscellaneousMemberBalance.getPrimaryOverdue();
                    }

                    if (miscellaneousMemberBalance.getSecondaryOverdue() < 0) {
                        totalSecondaryOverdue += 0;
                    } else {
                        totalSecondaryOverdue += miscellaneousMemberBalance.getSecondaryOverdue();
                    }

                    totalPrimaryDisbursed += miscellaneousMemberBalance.getPrimaryDisbursed();
                    totalPrimaryOverDue += miscellaneousMemberBalance.getPrimaryOverdue();
                    totalSecondaryDisbursed += miscellaneousMemberBalance.getSecondaryDisbursed();
                    totalSecondaryOverdue += miscellaneousMemberBalance.getSecondaryOverdue();
                    totalSecondaryOutstanding += miscellaneousMemberBalance.getSecondaryOutstanding();
                    totalPrimaryOutstanding += miscellaneousMemberBalance.getPrimaryOutstanding();
                    totalSavings += miscellaneousMemberBalance.getSavingsBalance();
                    totalLts += miscellaneousMemberBalance.getLtsBalance();
                    totalCbs += miscellaneousMemberBalance.getCbsBalance();
                    totalNet += miscellaneousMemberBalance.getNetBalance();

                    miscellaneousMemberBalanceList.add(miscellaneousMemberBalance);

                    MiscellaneousMemberBalance miscellaneousMemberBalanceTotal = new MiscellaneousMemberBalance();

                    miscellaneousMemberBalanceTotal.setMemberName("Total");
                    miscellaneousMemberBalanceTotal.setMemberId(-12345);
                    miscellaneousMemberBalanceTotal.setPrimaryDisbursed(totalPrimaryDisbursed);
                    miscellaneousMemberBalanceTotal.setPrimaryOverdue(totalPrimaryOverDue);
                    miscellaneousMemberBalanceTotal.setSecondaryDisbursed(totalSecondaryDisbursed);
                    miscellaneousMemberBalanceTotal.setSecondaryOverdue(totalSecondaryOverdue);
                    miscellaneousMemberBalanceTotal.setPrimaryOutstanding(totalPrimaryOutstanding);
                    miscellaneousMemberBalanceTotal.setSecondaryOutstanding(totalSecondaryOutstanding);
                    miscellaneousMemberBalanceTotal.setSavingsBalance(totalSavings);
                    miscellaneousMemberBalanceTotal.setLtsBalance(totalLts);
                    miscellaneousMemberBalanceTotal.setCbsBalance(totalCbs);
                    miscellaneousMemberBalanceTotal.setNetBalance(totalNet);
                    miscellaneousMemberBalanceList.add(miscellaneousMemberBalanceTotal);
                }


                cursor.moveToNext();
            }

            cursor.close();


        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return miscellaneousMemberBalanceList;

    }

    public List<RealizedMemberData> getRealizedGroupMemberDataSummary(List<Integer> selectedGroups, int date) {
        List<RealizedMemberData> realizedMembersInformation = new ArrayList<>();

        this.openReadableDatabase();

        StringBuilder selectedGroupsString = new StringBuilder();
        int token = selectedGroups.size();


        for (int i = 0; i < selectedGroups.size(); i++) {

            if (getGroupSize(selectedGroups.get(i)) > 0) {
                selectedGroupsString.append(" ").append(selectedGroups.get(i));
                if (selectedGroups.size() - 1 > i) {
                    selectedGroupsString.append(" , ");
                }
            }


        }


        try {
            Cursor cursor = databaseRead.rawQuery
                    ("SELECT  Id, GroupId ,Name, PassbookNumber, FatherOrHusbandName "
                            + " ,PrimaryCollection, PrimaryRealizable, SecondaryCollection, SecondaryRealizable, SupplementaryCollection,SupplementaryRealizable, SavingsDeposit, SavingsDepositWithoutLTS, LTSDeposit, CbsDeposit, SavingsWithdrawal, CbsWithdrawal, BadDebtCollection"
                            + ",ExemptionTotal  "
                            + "FROM (SELECT Id , P_GroupId AS GroupId,  Name, PassbookNumber, FatherOrHusbandName  FROM P_MemberView WHERE P_GroupId IN ("
                            + selectedGroupsString
                            + " )  AND Status = 1  ORDER BY GroupId , PassbookNumber ASC ) AS T_Member "
                            + " LEFT JOIN "
                            + "( SELECT P_Account.P_MemberId AS MemberId,  "
                            + "SUM(CASE WHEN P_LoanTransaction.Status = 1 THEN ROUND(Debit) ELSE 0 END) AS ExemptionTotal"
                            + " FROM P_MemberView INNER JOIN  P_Account ON P_MemberView.Id = P_Account.P_MemberId AND P_LoanTransaction.Date ="
                            + date
                            + "  AND P_MemberView.P_GroupId IN ("
                            + selectedGroupsString + " ) "
                            + "INNER JOIN P_LoanTransaction ON P_LoanTransaction.P_AccountId = P_Account.Account_ID GROUP BY  MemberId ) AS T_Exemption ON  T_Exemption.MemberId =  T_Member.Id"
                            + " LEFT JOIN "
                            + "( SELECT P_Account.P_MemberId AS MemberId, "
                            + " SUM( CASE WHEN P_Program.IsPrimary = 1 AND P_AccountBalance.Type = 4 AND P_Account.IsSupplementary = 0  THEN P_AccountBalance.Credit ELSE 0 END) AS PrimaryCollection, "
                            + " SUM( CASE WHEN P_Program.IsPrimary = 0 AND P_AccountBalance.Type = 4 AND IsSupplementary = 0 THEN P_AccountBalance.Credit ELSE 0 END) AS SecondaryCollection, "
                            + " SUM ( CASE WHEN P_Account.IsSupplementary = 1 AND P_AccountBalance.Type = 4 THEN Credit ELSE 0 END) AS SupplementaryCollection , "
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 1024  THEN Credit ELSE 0 END) AS SavingsDeposit ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 1024 AND P_Account.P_ProgramId <> 204 THEN Credit ELSE 0 END) AS SavingsDepositWithoutLTS ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_Account.P_ProgramId = 204 AND P_AccountBalance.Type = 1024 THEN Credit ELSE 0 END) AS LTSDeposit ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 4 AND P_AccountBalance.Type = 131072 THEN Credit ELSE 0 END) AS CbsDeposit ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 2 AND P_AccountBalance.Type = 16386   THEN Debit ELSE 0 END) AS SavingsWithdrawal ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 4 AND P_AccountBalance.Type = 48576   THEN Debit ELSE 0 END) AS CbsWithdrawal ,"
                            + " SUM(CASE WHEN P_Account.P_ProgramTypeId = 8 THEN Credit ELSE 0 END) AS BadDebtCollection "
                            + " FROM P_Account  INNER JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId  INNER JOIN P_AccountBalance  ON P_Account.Account_ID = P_AccountBalance.P_AccountId  AND P_AccountBalance.Date ="
                            + date
                            + " INNER JOIN P_MemberView ON P_Account.P_MemberId = P_MemberView.Id  AND P_MemberView.P_GroupId IN ("
                            + selectedGroupsString + " ) "
                            + "  GROUP BY MemberId )  AS T_Loan ON T_Member.Id = T_Loan.MemberId "
                            + " LEFT JOIN "
                            + "( SELECT P_Account.P_MemberId AS MemberId, "
                            + " SUM( CASE WHEN P_Program.IsPrimary = 1  AND P_Account.IsSupplementary = 0 AND ScheduledDate = " + date + "   THEN P_Schedule.InstallmentAmount ELSE 0 END) AS PrimaryRealizable, "
                            + " SUM( CASE WHEN P_Program.IsPrimary = 0  AND IsSupplementary = 0 AND  ScheduledDate = " + date + "   THEN P_Schedule.InstallmentAmount ELSE 0 END) AS SecondaryRealizable, "
                            + " SUM ( CASE WHEN P_Account.IsSupplementary = 1  AND ScheduledDate = " + date + "   THEN P_Schedule.InstallmentAmount ELSE 0 END) AS SupplementaryRealizable  "
                            + " FROM P_Account "
                            + " LEFT JOIN P_MemberView ON P_Account.P_MemberId = P_MemberView.Id  AND P_MemberView.P_GroupId IN ("
                            + selectedGroupsString + " ) "
                            + " LEFT JOIN P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID AND  ScheduledDate =  " + date + " "
                            + " LEFT JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId "
                            + "  GROUP BY MemberId )  AS T_Schedule ON T_Member.Id = T_Schedule.MemberId ", null);


            int groupId = 0;

            float primaryCollectionGrandTotal = 0;
            float secondaryCollectionGrandTotal = 0;
            float supplementaryCollectionGrandTotal = 0;
            float savingsDepositGrandTotal = 0;
            float savingsDepositWithoutLtsGrandTotal = 0;
            float ltsDepositGrandTotal = 0;
            float cbsDepositGrandTotal = 0;
            float savingsWithdrawalGrandTotal = 0;
            float cbsWithdrawalGrandTotal = 0;
            float badDebtCollectionGrandTotal = 0;
            float exemptionTotalGrandTotal = 0;
            float totalCollectionGrandTotal = 0;
            float totalWithdrawalGrandTotal = 0;
            float netCollectionGrandTotal = 0;
            float primaryRealizableGrandTotal = 0;
            float secondaryRealizableGrandTotal = 0;
            float supplementaryRealizableGrandTotal = 0;
            float totalRealizableGrandTotal = 0;

            float primaryCollectionTotal = 0;
            float secondaryCollectionTotal = 0;
            float supplementaryCollectionTotal = 0;
            float savingsDepositTotal = 0;
            float savingsDepositWithoutLtsTotal = 0;
            float ltsDepositTotal = 0;
            float cbsDepositTotal = 0;
            float savingsWithdrawalTotal = 0;
            float cbsWithdrawalTotal = 0;
            float badDebtCollectionTotal = 0;
            float exemptionTotalTotal = 0;
            float totalCollectionTotal = 0;
            float totalWithdrawalTotal = 0;
            float netCollectionTotal = 0;
            float primaryRealizableTotal = 0;
            float secondaryRealizableTotal = 0;
            float supplementaryRealizableTotal = 0;
            float totalRealizableTotal = 0;


            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RealizedMemberData realizedMemberData = new RealizedMemberData();


                realizedMemberData.setGroupId(cursor.getInt(cursor.getColumnIndex("GroupId")));
                realizedMemberData.setGroupName(getGroupName(cursor.getInt(cursor.getColumnIndex("GroupId"))));

                realizedMemberData.setMemberId(cursor.getInt(cursor.getColumnIndex("Id")));
                realizedMemberData.setMemberName(cursor.getString(cursor.getColumnIndex("Name")));
                realizedMemberData.setPassbookNumber(cursor.getInt(cursor.getColumnIndex("PassbookNumber")));
                realizedMemberData.setFatherName(cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")));


                float primaryCollection = cursor.getFloat(cursor.getColumnIndex("PrimaryCollection"));
                float secondaryCollection = cursor.getFloat(cursor.getColumnIndex("SecondaryCollection"));
                float supplementaryCollection = cursor.getFloat(cursor.getColumnIndex("SupplementaryCollection"));
                float savingsDeposit = cursor.getFloat(cursor.getColumnIndex("SavingsDeposit"));
                float savingsDepositWithoutLTS = cursor.getFloat(cursor.getColumnIndex("SavingsDepositWithoutLTS"));
                float ltsDeposit = cursor.getFloat(cursor.getColumnIndex("LTSDeposit"));
                float cbsDeposit = cursor.getFloat(cursor.getColumnIndex("CbsDeposit"));
                float savingsWithdrawal = cursor.getFloat(cursor.getColumnIndex("SavingsWithdrawal"));
                float cbsWithdrawal = cursor.getFloat(cursor.getColumnIndex("CbsWithdrawal"));
                float badDebtCollection = cursor.getFloat(cursor.getColumnIndex("BadDebtCollection"));
                float exemptionTotal = cursor.getFloat(cursor.getColumnIndex("ExemptionTotal"));
                float primaryRealizable = cursor.getInt(cursor.getColumnIndex("PrimaryRealizable"));
                float secondaryRealizable = cursor.getInt(cursor.getColumnIndex("SecondaryRealizable"));
                float supplementaryRealizable = cursor.getInt(cursor.getColumnIndex("SupplementaryRealizable"));

                if (primaryRealizable < primaryCollection) {
                    primaryRealizable = primaryCollection;
                }
                if (secondaryRealizable < secondaryCollection) {
                    secondaryRealizable = secondaryCollection;
                }
                if (supplementaryRealizable < supplementaryCollection) {
                    supplementaryRealizable = supplementaryCollection;
                }

                float totalRealizable = (primaryRealizable + secondaryRealizable + supplementaryRealizable);
                float totalCollection = primaryCollection + secondaryCollection + supplementaryCollection + savingsDepositWithoutLTS + ltsDeposit + cbsDeposit + badDebtCollection;
                float totalWithdrawal = savingsWithdrawal + cbsWithdrawal + exemptionTotal;
                float netCollection = totalCollection - totalWithdrawal;


                if (groupId == 0 || groupId == cursor.getInt(cursor.getColumnIndex("GroupId"))) {
                    if (groupId == 0) {
                        groupId = cursor.getInt(cursor.getColumnIndex("GroupId"));
                    }

                    primaryCollectionTotal += primaryCollection;
                    secondaryCollectionTotal += secondaryCollection;
                    supplementaryCollectionTotal += supplementaryCollection;
                    savingsDepositTotal += savingsDeposit;
                    savingsDepositWithoutLtsTotal += savingsDepositWithoutLTS;
                    ltsDepositTotal += ltsDeposit;
                    cbsDepositTotal += cbsDeposit;
                    savingsWithdrawalTotal += savingsWithdrawal;
                    cbsWithdrawalTotal += cbsWithdrawal;
                    badDebtCollectionTotal += badDebtCollection;
                    exemptionTotalTotal += exemptionTotal;
                    totalCollectionTotal += totalCollection;
                    totalWithdrawalTotal += totalWithdrawal;
                    netCollectionTotal += netCollection;

                    primaryRealizableTotal += primaryRealizable;
                    secondaryRealizableTotal += secondaryRealizable;
                    supplementaryRealizableTotal += supplementaryRealizable;
                    totalRealizableTotal += totalRealizable;

                } else {
                    RealizedMemberData realizedMemberDataTotal = new RealizedMemberData();
                    realizedMemberDataTotal.setMemberId(-12345);
                    realizedMemberDataTotal.setMemberName("Total" + " (" + getGroupName(groupId) + ")");

                    realizedMemberDataTotal.setPrimaryCollection(primaryCollectionTotal);
                    realizedMemberDataTotal.setSecondaryCollection(secondaryCollectionTotal);
                    realizedMemberDataTotal.setSupplementaryCollection(supplementaryCollectionTotal);
                    realizedMemberDataTotal.setSavingsDeposit(savingsDepositTotal);
                    realizedMemberDataTotal.setSavingsDepositWithoutLTS(savingsDepositWithoutLtsTotal);
                    realizedMemberDataTotal.setLtsDeposit(ltsDepositTotal);
                    realizedMemberDataTotal.setCbsDeposit(cbsDepositTotal);
                    realizedMemberDataTotal.setSavingsWithdrawal(savingsWithdrawalTotal);
                    realizedMemberDataTotal.setCbsWithdrawal(cbsWithdrawalTotal);
                    realizedMemberDataTotal.setBadDebtCollection(badDebtCollectionTotal);
                    realizedMemberDataTotal.setExemptionTotal(exemptionTotalTotal);
                    realizedMemberDataTotal.setTotalCollection(totalCollectionTotal);
                    realizedMemberDataTotal.setTotalWithdrawal(totalWithdrawalTotal);
                    realizedMemberDataTotal.setNetCollection(netCollectionTotal);

                    realizedMemberDataTotal.setPrimaryRealizable(primaryRealizableTotal);
                    realizedMemberDataTotal.setSecondaryRealizable(secondaryRealizableTotal);
                    realizedMemberDataTotal.setSupplementaryRealizable(supplementaryRealizableTotal);
                    realizedMemberDataTotal.setTotalRealizable(totalRealizableTotal);


                    realizedMembersInformation.add(realizedMemberDataTotal);

                    primaryCollectionGrandTotal += primaryCollectionTotal;
                    secondaryCollectionGrandTotal += secondaryCollectionTotal;
                    supplementaryCollectionGrandTotal += supplementaryCollectionTotal;
                    savingsDepositGrandTotal += savingsDepositTotal;
                    savingsDepositWithoutLtsGrandTotal += savingsDepositWithoutLtsTotal;
                    ltsDepositGrandTotal += ltsDepositTotal;
                    cbsDepositGrandTotal += cbsDepositTotal;
                    savingsWithdrawalGrandTotal += savingsWithdrawalTotal;
                    cbsWithdrawalGrandTotal += cbsWithdrawalTotal;
                    badDebtCollectionGrandTotal += badDebtCollectionTotal;
                    exemptionTotalGrandTotal += exemptionTotalTotal;
                    totalCollectionGrandTotal += totalCollectionTotal;
                    totalWithdrawalGrandTotal += totalWithdrawalTotal;
                    netCollectionGrandTotal += netCollectionTotal;
                    primaryRealizableGrandTotal += primaryRealizableTotal;
                    secondaryRealizableGrandTotal += secondaryRealizableTotal;
                    supplementaryRealizableGrandTotal += supplementaryRealizableTotal;
                    totalRealizableGrandTotal += totalRealizableTotal;

                    primaryCollectionTotal = secondaryCollectionTotal = supplementaryCollectionTotal = savingsDepositTotal
                            = savingsDepositWithoutLtsTotal = ltsDepositTotal = cbsDepositTotal = savingsWithdrawalTotal
                            = cbsWithdrawalTotal = badDebtCollectionTotal = exemptionTotalTotal = totalCollectionTotal
                            = totalWithdrawalTotal = netCollectionTotal = primaryRealizableTotal
                            = secondaryRealizableTotal = supplementaryRealizableTotal = totalRealizableTotal = 0;


                    groupId = cursor.getInt(cursor.getColumnIndex("GroupId"));

                    primaryCollectionTotal += primaryCollection;
                    secondaryCollectionTotal += secondaryCollection;
                    supplementaryCollectionTotal += supplementaryCollection;
                    savingsDepositTotal += savingsDeposit;
                    savingsDepositWithoutLtsTotal += savingsDepositWithoutLTS;
                    ltsDepositTotal += ltsDeposit;
                    cbsDepositTotal += cbsDeposit;
                    savingsWithdrawalTotal += savingsWithdrawal;
                    cbsWithdrawalTotal += cbsWithdrawal;
                    badDebtCollectionTotal += badDebtCollection;
                    exemptionTotalTotal += exemptionTotal;
                    totalCollectionTotal += totalCollection;
                    totalWithdrawalTotal += totalWithdrawal;
                    netCollectionTotal += netCollection;

                    primaryRealizableTotal += primaryRealizable;
                    secondaryRealizableTotal += secondaryRealizable;
                    supplementaryRealizableTotal += supplementaryRealizable;
                    totalRealizableTotal += totalRealizable;

                }

                realizedMemberData.setPrimaryCollection(primaryCollection);
                realizedMemberData.setSecondaryCollection(secondaryCollection);
                realizedMemberData.setSupplementaryCollection(supplementaryCollection);
                realizedMemberData.setSavingsDeposit(savingsDeposit);
                realizedMemberData.setSavingsDepositWithoutLTS(savingsDepositWithoutLTS);
                realizedMemberData.setLtsDeposit(ltsDeposit);
                realizedMemberData.setCbsDeposit(cbsDeposit);
                realizedMemberData.setSavingsWithdrawal(savingsWithdrawal);
                realizedMemberData.setCbsWithdrawal(cbsWithdrawal);
                realizedMemberData.setBadDebtCollection(badDebtCollection);
                realizedMemberData.setExemptionTotal(exemptionTotal);
                realizedMemberData.setTotalCollection(totalCollection);
                realizedMemberData.setTotalWithdrawal(totalWithdrawal);
                realizedMemberData.setNetCollection(netCollection);
                realizedMemberData.setPrimaryRealizable(primaryRealizable);
                realizedMemberData.setSecondaryRealizable(secondaryRealizable);
                realizedMemberData.setSupplementaryRealizable(supplementaryRealizable);
                realizedMemberData.setTotalRealizable(totalRealizable);


                if (primaryCollection > 0 || secondaryCollection > 0 || supplementaryCollection > 0 || savingsDeposit > 0 || savingsDepositWithoutLTS > 0 || ltsDeposit > 0
                        || cbsDeposit > 0 || savingsWithdrawal > 0 || cbsWithdrawal > 0 || badDebtCollection > 0 || exemptionTotal > 0 || totalCollection > 0
                        || totalWithdrawal > 0 || netCollection > 0) {
                    realizedMemberData.setCollected(true);
                }
                realizedMembersInformation.add(realizedMemberData);

                if (cursor.isLast()) {
                    RealizedMemberData realizedMemberDataTotal = new RealizedMemberData();

                    realizedMemberDataTotal.setMemberId(-12345);
                    realizedMemberDataTotal.setMemberName("Total" + " (" + getGroupName(groupId) + ")");

                    realizedMemberDataTotal.setPrimaryCollection(primaryCollectionTotal);
                    realizedMemberDataTotal.setSecondaryCollection(secondaryCollectionTotal);
                    realizedMemberDataTotal.setSupplementaryCollection(supplementaryCollectionTotal);
                    realizedMemberDataTotal.setSavingsDeposit(savingsDepositTotal);
                    realizedMemberDataTotal.setSavingsDepositWithoutLTS(savingsDepositWithoutLtsTotal);
                    realizedMemberDataTotal.setLtsDeposit(ltsDepositTotal);
                    realizedMemberDataTotal.setCbsDeposit(cbsDepositTotal);
                    realizedMemberDataTotal.setSavingsWithdrawal(savingsWithdrawalTotal);
                    realizedMemberDataTotal.setCbsWithdrawal(cbsWithdrawalTotal);
                    realizedMemberDataTotal.setBadDebtCollection(badDebtCollectionTotal);
                    realizedMemberDataTotal.setExemptionTotal(exemptionTotalTotal);
                    realizedMemberDataTotal.setTotalCollection(totalCollectionTotal);
                    realizedMemberDataTotal.setTotalWithdrawal(totalWithdrawalTotal);
                    realizedMemberDataTotal.setNetCollection(netCollectionTotal);

                    realizedMemberDataTotal.setPrimaryRealizable(primaryRealizableTotal);
                    realizedMemberDataTotal.setSecondaryRealizable(secondaryRealizableTotal);
                    realizedMemberDataTotal.setSupplementaryRealizable(supplementaryRealizableTotal);
                    realizedMemberDataTotal.setTotalRealizable(totalRealizableTotal);


                    realizedMembersInformation.add(realizedMemberDataTotal);

                    primaryCollectionGrandTotal += primaryCollectionTotal;
                    secondaryCollectionGrandTotal += secondaryCollectionTotal;
                    supplementaryCollectionGrandTotal += supplementaryCollectionTotal;
                    savingsDepositGrandTotal += savingsDepositTotal;
                    savingsDepositWithoutLtsGrandTotal += savingsDepositWithoutLtsTotal;
                    ltsDepositGrandTotal += ltsDepositTotal;
                    cbsDepositGrandTotal += cbsDepositTotal;
                    savingsWithdrawalGrandTotal += savingsWithdrawalTotal;
                    cbsWithdrawalGrandTotal += cbsWithdrawalTotal;
                    badDebtCollectionGrandTotal += badDebtCollectionTotal;
                    exemptionTotalGrandTotal += exemptionTotalTotal;
                    totalCollectionGrandTotal += totalCollectionTotal;
                    totalWithdrawalGrandTotal += totalWithdrawalTotal;
                    netCollectionGrandTotal += netCollectionTotal;
                    primaryRealizableGrandTotal += primaryRealizableTotal;
                    secondaryRealizableGrandTotal += secondaryRealizableTotal;
                    supplementaryRealizableGrandTotal += supplementaryRealizableTotal;
                    totalRealizableGrandTotal += totalRealizableTotal;
                }
                cursor.moveToNext();
            }
            cursor.close();

            if (token > 1) {
                RealizedMemberData realizedMemberDataTotal = new RealizedMemberData();

                realizedMemberDataTotal.setMemberId(-54321);
                realizedMemberDataTotal.setMemberName("Grand-Total");

                realizedMemberDataTotal.setPrimaryCollection(primaryCollectionGrandTotal);
                realizedMemberDataTotal.setSecondaryCollection(secondaryCollectionGrandTotal);
                realizedMemberDataTotal.setSupplementaryCollection(supplementaryCollectionGrandTotal);
                realizedMemberDataTotal.setSavingsDeposit(savingsDepositGrandTotal);
                realizedMemberDataTotal.setSavingsDepositWithoutLTS(savingsDepositWithoutLtsGrandTotal);
                realizedMemberDataTotal.setLtsDeposit(ltsDepositGrandTotal);
                realizedMemberDataTotal.setCbsDeposit(cbsDepositGrandTotal);
                realizedMemberDataTotal.setSavingsWithdrawal(savingsWithdrawalGrandTotal);
                realizedMemberDataTotal.setCbsWithdrawal(cbsWithdrawalGrandTotal);
                realizedMemberDataTotal.setBadDebtCollection(badDebtCollectionGrandTotal);
                realizedMemberDataTotal.setExemptionTotal(exemptionTotalGrandTotal);
                realizedMemberDataTotal.setTotalCollection(totalCollectionGrandTotal);
                realizedMemberDataTotal.setTotalWithdrawal(totalWithdrawalGrandTotal);
                realizedMemberDataTotal.setNetCollection(netCollectionGrandTotal);
                realizedMemberDataTotal.setPrimaryRealizable(primaryRealizableGrandTotal);
                realizedMemberDataTotal.setSecondaryRealizable(secondaryRealizableGrandTotal);
                realizedMemberDataTotal.setSupplementaryRealizable(supplementaryRealizableGrandTotal);
                realizedMemberDataTotal.setTotalRealizable(totalRealizableGrandTotal);

                realizedMembersInformation.add(realizedMemberDataTotal);
            }


        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return realizedMembersInformation;
    }

    public List<Member> getGroupMemberInformation(int groupId, String searchValue) {
        this.openReadableDatabase();
        List<Member> members = new ArrayList<>();


        try {
            Cursor cursor;
            if (searchValue.trim().equals("")) {
                cursor = databaseRead.rawQuery("Select Id, NewStatus, PassbookNumber, Name, FatherOrHusbandName, IsHusband, AdmissionDate, NationalIdNumber, Sex, CASE WHEN  Phone <> 'null' THEN  Phone ELSE 'Not Set' END AS Phone, ReceiveDate, ReceiveType, UpdateNid , UpdatePhone  From P_MemberView WHERE P_GroupId = '"
                        + groupId + "' AND Status = '1' ORDER BY PassbookNumber", null);
            } else {
                cursor = databaseRead.rawQuery("Select Id, NewStatus, PassbookNumber, Name, FatherOrHusbandName, IsHusband, AdmissionDate, NationalIdNumber, Sex, CASE WHEN  Phone <> 'null' THEN  Phone ELSE 'Not Set' END AS Phone, ReceiveDate, ReceiveType, UpdateNid , UpdatePhone  From P_MemberView WHERE P_GroupId = '"
                        + groupId + "' AND Status = '1' AND  (Name LIKE '" + searchValue.trim() + "%' OR FatherOrHusbandName LIKE '" + searchValue.trim() + "%' OR NationalIdNumber LIKE '" + searchValue.trim() + "%')  ORDER BY PassbookNumber", null);
            }

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Member member = new Member();

                member.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                member.setPassbookNumber(cursor.getInt(cursor.getColumnIndex("PassbookNumber")));
                member.setName(cursor.getString(cursor.getColumnIndex("Name")));
                member.setFatherName(cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")));
                member.setHusband(cursor.getInt(cursor.getColumnIndex("IsHusband")) > 0);
                member.setAdmissionDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("AdmissionDate"))));
                member.setnIdNum(cursor.getString(cursor.getColumnIndex("NationalIdNumber")) == null ? "" : cursor.getString(cursor.getColumnIndex("NationalIdNumber")));
                member.setSex(cursor.getInt(cursor.getColumnIndex("Sex")));
                member.setPhone(cursor.getString(cursor.getColumnIndex("Phone")));
                member.setAdmissionDateInteger(cursor.getInt(cursor.getColumnIndex("AdmissionDate")));
                member.setReceiveDate(cursor.getInt(cursor.getColumnIndex("ReceiveDate")));
                member.setReceiveType(cursor.getInt(cursor.getColumnIndex("ReceiveType")));
                member.setUpdateNid((cursor.getInt(cursor.getColumnIndex("UpdateNid"))));
                member.setUpdatePhone((cursor.getInt(cursor.getColumnIndex("UpdatePhone"))));
                if (cursor.getString(cursor.getColumnIndex("NewStatus")).equals("New")) {
                    member.setStatus(1);
                } else {
                    member.setStatus(0);
                }


                members.add(member);

                cursor.moveToNext();

            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return members;
    }

    private int getGroupSize(int groupId) {
        this.openReadableDatabase();
        int size = 0;

        try {
            Cursor cursor = databaseRead.rawQuery("Select *  From P_MemberView WHERE P_GroupId = '" + groupId + "' ", null);
            cursor.moveToFirst();
            size = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return size;
    }

    private String getGroupName(int groupId) {
        String groupName = "";
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery("SELECT Name FROM P_Group WHERE ID =" + groupId, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                groupName = cursor.getString(cursor.getColumnIndex("Name"));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return groupName;
    }

    private boolean getIsPrimary(int programID) {
        this.openReadableDatabase();
        boolean valueOfIsPrimary = false;
        String queryGetLoan = "Select IsPrimary From P_Program WHERE Program_ID = '"
                + programID + "'";
        try {
            Cursor cursor = databaseRead.rawQuery(queryGetLoan, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                valueOfIsPrimary = cursor.getInt(cursor.getColumnIndex("IsPrimary")) > 0;
            }

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return valueOfIsPrimary;
    }

    public ArrayList<AccountForDailyTransaction> getAccountForAccountLTSInfo(int memberId) {


        this.openReadableDatabase();


        ArrayList<AccountForDailyTransaction> accountOnlyLong = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        String query = "SELECT Account_ID,  OpeningDate, ProgramType, ProgramName, DisbursedAmount, IsSupplementary, Duration, MeetingDayOfWeek, MeetingDayOfMonth, InstallmentType, "
                + "ProgramId , P_ProgramTypeId, Balance, Flag, MinimumDeposit , T_Account.NewAccount AS NewAccount , MAX(CASE WHEN  P_AccountDetails.P_LoanTransactionDate <> 'null'  THEN  P_AccountDetails.P_LoanTransactionDate ELSE 0 END ) "
                + " AS LoanTransactionDate FROM (SELECT  Account_ID,  OpeningDate, ProgramType, ProgramName, DisbursedAmount, IsSupplementary, P_Duration AS Duration, MeetingDayOfWeek, MeetingDayOfMonth, P_InstallmentType AS  InstallmentType,  "
                + "P_Account.P_ProgramId AS ProgramId , P_ProgramTypeId, P_Account.NewAccount AS NewAccount , SUM(P_AccountBalance.Credit - P_AccountBalance.Debit) AS Balance, P_Account.Flag AS Flag , "
                + "  MAX(MinimumDeposit) AS MinimumDeposit   FROM P_Account "
                + " INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId AND P_Account.P_MemberId = "
                + memberId
                + "  AND P_ProgramId = 204 AND P_AccountBalance.Date <= "
                + workingDay
                + "  GROUP BY Account_ID  ORDER BY Account_ID) AS T_Account LEFT JOIN P_AccountDetails ON P_AccountDetails.P_AccountId = T_Account.Account_ID AND P_AccountDetails.NewlyCreated = 0  GROUP BY Account_ID ";


        try (Cursor cursor = databaseRead.rawQuery(query, null)) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                AccountForDailyTransaction accountForDailyTransaction = new AccountForDailyTransaction();


                accountForDailyTransaction.setAccountId(cursor.getInt(cursor.getColumnIndex("Account_ID")));

                accountForDailyTransaction.setOpeningDateValue(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate"))));

                accountForDailyTransaction.setDuration(cursor.getInt(cursor.getColumnIndex("Duration")));
                accountForDailyTransaction.setMeetingDayOfWeek(cursor.getInt(cursor.getColumnIndex("MeetingDayOfWeek")));
                accountForDailyTransaction.setMinimumDeposit(cursor.getFloat(cursor.getColumnIndex("MinimumDeposit")));
                accountForDailyTransaction.setMeetingDayOfMonth(cursor.getInt(cursor.getColumnIndex("MeetingDayOfMonth")));
                accountForDailyTransaction.setInstallmentType(cursor.getInt(cursor.getColumnIndex("InstallmentType")));


                if (cursor.getInt(cursor.getColumnIndex("LoanTransactionDate")) == 0) {
                    accountForDailyTransaction.setLastAccountDetailsDate("NOT FOUND");
                } else {
                    accountForDailyTransaction.setLastAccountDetailsDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("LoanTransactionDate"))));
                }
                accountForDailyTransaction.setBalance(cursor.getFloat(cursor.getColumnIndex("Balance")));

                if (cursor.getInt(cursor.getColumnIndex("OpeningDate")) == workingDay && cursor.getInt(cursor.getColumnIndex("Flag")) > 0) {
                    accountForDailyTransaction.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
                } else {
                    accountForDailyTransaction.setFlag(0);
                }


                accountOnlyLong.add(accountForDailyTransaction);


                cursor.moveToNext();

            }
        } catch (SQLException e) {
            Log.i("SqlErrorInMemberDetails", e.toString());
        }


        return accountOnlyLong;
    }

    public ArrayList<AccountForDailyTransaction> getCbsAccountInfo(int memberId) {
        this.openReadableDatabase();
        ArrayList<AccountForDailyTransaction> accountOnlyLong = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();


        String query = "SELECT Account_ID,  OpeningDate, ProgramType, ProgramName, DisbursedAmount, IsSupplementary, Duration, MeetingDayOfWeek, MeetingDayOfMonth, InstallmentType ,  "
                + "ProgramId , P_ProgramTypeId, Balance, Flag, MinimumDeposit , T_Account.NewAccount AS NewAccount , MAX(CASE WHEN  P_AccountDetails.P_LoanTransactionDate <> 'null'  THEN  P_AccountDetails.P_LoanTransactionDate ELSE 0 END ) "
                + " AS LoanTransactionDate FROM (SELECT  Account_ID,  OpeningDate, ProgramType, ProgramName, DisbursedAmount, IsSupplementary, P_Duration AS Duration, P_InstallmentType AS  InstallmentType, MeetingDayOfWeek, MeetingDayOfMonth, "
                + "P_Account.P_ProgramId AS ProgramId , P_Account.NewAccount AS NewAccount , P_ProgramTypeId, SUM(P_AccountBalance.Credit - P_AccountBalance.Debit) AS Balance, P_Account.Flag AS Flag , "
                + "  MAX(MinimumDeposit) AS MinimumDeposit   FROM P_Account "
                + " INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId AND P_Account.P_MemberId = "
                + memberId
                + "   AND P_ProgramId > 300 AND P_ProgramId < 400  AND P_AccountBalance.Date <= "
                + workingDay
                + "  GROUP BY Account_ID  ORDER BY Account_ID) AS T_Account LEFT JOIN P_AccountDetails ON P_AccountDetails.P_AccountId = T_Account.Account_ID AND P_AccountDetails.NewlyCreated = 0  GROUP BY Account_ID ";

        try (Cursor cursor = databaseRead.rawQuery(query, null)) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                AccountForDailyTransaction accountForDailyTransaction = new AccountForDailyTransaction();

                accountForDailyTransaction.setProgramName(cursor.getString(cursor.getColumnIndex("ProgramName")));
                accountForDailyTransaction.setProgramId(cursor.getInt(cursor.getColumnIndex("ProgramId")));
                accountForDailyTransaction.setAccountId(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                accountForDailyTransaction.setOpeningDateValue(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate"))));
                accountForDailyTransaction.setDuration(cursor.getInt(cursor.getColumnIndex("Duration")));
                accountForDailyTransaction.setMeetingDayOfWeek(cursor.getInt(cursor.getColumnIndex("MeetingDayOfWeek")));
                accountForDailyTransaction.setMinimumDeposit(cursor.getFloat(cursor.getColumnIndex("MinimumDeposit")));
                accountForDailyTransaction.setMeetingDayOfMonth(cursor.getInt(cursor.getColumnIndex("MeetingDayOfMonth")));
                accountForDailyTransaction.setInstallmentType(cursor.getInt(cursor.getColumnIndex("InstallmentType")));

                if (cursor.getInt(cursor.getColumnIndex("LoanTransactionDate")) == 0) {
                    accountForDailyTransaction.setLastAccountDetailsDate("NOT FOUND");
                } else {
                    accountForDailyTransaction.setLastAccountDetailsDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("LoanTransactionDate"))));
                }

                accountForDailyTransaction.setBalance(cursor.getFloat(cursor.getColumnIndex("Balance")));


                if (cursor.getInt(cursor.getColumnIndex("OpeningDate")) == workingDay && cursor.getInt(cursor.getColumnIndex("Flag")) > 0) {
                    accountForDailyTransaction.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
                } else {
                    accountForDailyTransaction.setFlag(0);
                }

                accountOnlyLong.add(accountForDailyTransaction);


                cursor.moveToNext();

            }
        } catch (SQLException e) {
            Log.i("SqlErrorInMemberDetails", e.toString());
        }


        return accountOnlyLong;
    }

    public ArrayList<AccountForDailyTransaction> getSavingAccountInfo(int memberId) {

        this.openReadableDatabase();

        ArrayList<AccountForDailyTransaction> accountOnlySaving = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();


        String query = "SELECT Account_ID,  OpeningDate, ProgramType, ProgramName, DisbursedAmount, IsSupplementary, Duration, MeetingDayOfWeek, MeetingDayOfMonth, InstallmentType, "
                + "ProgramId , P_ProgramTypeId, Balance,  T_Account.NewAccount AS NewAccount, Flag, MinimumDeposit , MAX(CASE WHEN  P_AccountDetails.P_LoanTransactionDate <> 'null'  THEN  P_AccountDetails.P_LoanTransactionDate ELSE 0 END ) "
                + " AS LoanTransactionDate FROM (SELECT  Account_ID,  OpeningDate, ProgramType, ProgramName, DisbursedAmount, IsSupplementary, P_Duration AS Duration, MeetingDayOfWeek, MeetingDayOfMonth, P_InstallmentType AS  InstallmentType, "
                + "P_Account.P_ProgramId AS ProgramId , P_Account.NewAccount AS NewAccount,  P_ProgramTypeId, SUM(P_AccountBalance.Credit - P_AccountBalance.Debit) AS Balance, P_Account.Flag AS Flag , "
                + "  MAX(MinimumDeposit) AS MinimumDeposit   FROM P_Account "
                + " INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId AND P_Account.P_MemberId = "
                + memberId
                + "  AND P_ProgramId > 200 AND P_ProgramId < 300 AND P_ProgramId != 204 AND P_AccountBalance.Date <= "
                + workingDay
                + "  GROUP BY Account_ID  ORDER BY Account_ID) AS T_Account LEFT JOIN P_AccountDetails ON P_AccountDetails.P_AccountId = T_Account.Account_ID AND P_AccountDetails.NewlyCreated = 0  GROUP BY Account_ID ";


        try (Cursor cursor = databaseRead.rawQuery(query, null)) {

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                AccountForDailyTransaction accountForDailyTransaction = new AccountForDailyTransaction();

                accountForDailyTransaction.setProgramName(cursor.getString(cursor.getColumnIndex("ProgramName")));
                accountForDailyTransaction.setProgramId(cursor.getInt(cursor.getColumnIndex("ProgramId")));
                accountForDailyTransaction.setAccountId(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                accountForDailyTransaction.setOpeningDateValue(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate"))));
                accountForDailyTransaction.setDuration(cursor.getInt(cursor.getColumnIndex("Duration")));
                accountForDailyTransaction.setMeetingDayOfWeek(cursor.getInt(cursor.getColumnIndex("MeetingDayOfWeek")));
                accountForDailyTransaction.setMinimumDeposit(cursor.getFloat(cursor.getColumnIndex("MinimumDeposit")));
                accountForDailyTransaction.setMeetingDayOfMonth(cursor.getInt(cursor.getColumnIndex("MeetingDayOfMonth")));
                accountForDailyTransaction.setInstallmentType(cursor.getInt(cursor.getColumnIndex("InstallmentType")));

                if (cursor.getInt(cursor.getColumnIndex("LoanTransactionDate")) == 0) {
                    accountForDailyTransaction.setLastAccountDetailsDate("NOT FOUND");
                } else {
                    accountForDailyTransaction.setLastAccountDetailsDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("LoanTransactionDate"))));
                }
                accountForDailyTransaction.setBalance(cursor.getFloat(cursor.getColumnIndex("Balance")));

                if (cursor.getInt(cursor.getColumnIndex("OpeningDate")) == workingDay && cursor.getInt(cursor.getColumnIndex("Flag")) > 0) {
                    accountForDailyTransaction.setFlag(cursor.getInt(cursor.getColumnIndex("Flag")));
                } else {
                    accountForDailyTransaction.setFlag(0);
                }

                accountOnlySaving.add(accountForDailyTransaction);


                cursor.moveToNext();

            }
        } catch (SQLException e) {
            Log.i("SqlErrorInMemberDetails", e.toString());
        }


        return accountOnlySaving;
    }

    public int getNextAccount() {
        this.openReadableDatabase();

        int nextAccountId = -1234567;
        try {

            Cursor cursor = databaseRead.rawQuery("SELECT MAX(Account_ID) AS MAXId FROM P_Account", null);
            cursor.moveToFirst();

            nextAccountId = cursor.getInt(cursor.getColumnIndex("MAXId")) + 1;
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return nextAccountId;
    }

    public ArrayList<AccountForDailyTransaction> getBadDebtAccountInfo(int memberId) {

        this.openReadableDatabase();
        ArrayList<AccountForDailyTransaction> badDebtAccount = new ArrayList<>();

        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        String query = "SELECT  Account_ID, OpeningDate,ProgramType, ProgramName, P_Account.NewAccount AS NewAccount,  "
                + "MAX(P_Account.P_ProgramId) AS ProgramId , P_ProgramTypeId, SUM(P_AccountBalance.Debit) AS Debit, SUM(P_AccountBalance.Debit - P_AccountBalance.Credit) AS Balance, SUM(P_AccountBalance.Credit) AS Credit, MAX(P_AccountBalance.Type) AS Type  FROM P_Account "
                + "INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId AND  P_ProgramTypeId = 8 AND P_Account.P_MemberId = '"
                + memberId
                + "' AND P_AccountBalance.Date <= '"
                + workingDay
                + "' GROUP BY Account_ID  ORDER BY ProgramId";


        try (Cursor cursor = databaseRead.rawQuery(query, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                AccountForDailyTransaction accountForDailyTransaction = new AccountForDailyTransaction();

                int type = cursor.getInt(cursor.getColumnIndex("Type"));

                if (type > 0) {
                    accountForDailyTransaction.setAccountStatus(1);
                } else {
                    accountForDailyTransaction.setAccountStatus(0);
                }

                accountForDailyTransaction.setProgramTypeId(type);
                accountForDailyTransaction.setAccountId(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                int openingDateInt = cursor.getInt(cursor.getColumnIndex("OpeningDate"));

                String openingDateString = new DateAndDataConversion().getDateFromInt(openingDateInt);
                accountForDailyTransaction.setOpeningDateValue(openingDateString);
                accountForDailyTransaction.setProgramName(cursor.getString(cursor.getColumnIndex("ProgramName")));
                accountForDailyTransaction.setProgramId(cursor.getInt(cursor.getColumnIndex("ProgramId")));
                accountForDailyTransaction.setProgramTypeId(cursor.getInt(cursor.getColumnIndex("P_ProgramTypeId")));
                accountForDailyTransaction.setBalance(cursor.getFloat(cursor.getColumnIndex("Balance")));
                accountForDailyTransaction.setCredit(cursor.getFloat(cursor.getColumnIndex("Credit")));
                accountForDailyTransaction.setDebit(cursor.getFloat(cursor.getColumnIndex("Debit")));

                badDebtAccount.add(accountForDailyTransaction);


                cursor.moveToNext();
            }
        } catch (SQLException e) {
            Log.i("SqlErrorInMemberDetails", e.toString());
        }
        return badDebtAccount;
    }

    public int getDistrictId(String branchName) {

        int districtId = 0;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT DistrictId FROM Branch WHERE Name = '" + branchName + "'", null);

            cursor.moveToFirst();
            districtId = cursor.getInt(cursor.getColumnIndex("DistrictId"));

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return districtId;
    }

    public int getGroupLastPassbookNumber(int groupId) {

        int passBookNumber = 1001;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery("SELECT MAX(PassbookNumber) AS lastPassbookNumber FROM P_MemberView WHERE P_GroupId = '" + groupId + "'", null);

            cursor.moveToFirst();

            passBookNumber = cursor.getInt(cursor.getColumnIndex("lastPassbookNumber"));
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return passBookNumber;
    }

    public boolean getValidationPassbookNumber(int passbookNumber, int groupId) {

        boolean hasPassBookNumber = false;
        this.openReadableDatabase();


        try {
            Cursor cursor = databaseRead.rawQuery("SELECT PassbookNumber, Name FROM P_MemberView WHERE PassbookNumber = " + passbookNumber + " AND P_GroupId = '" + groupId + "'", null);

            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                hasPassBookNumber = true;
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }


        return hasPassBookNumber;
    }

    int getGroupNextMemberId() {

        int lastId = 0;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery("SELECT MAX(Id) AS lastId FROM P_MemberView ", null);

            cursor.moveToFirst();

            lastId = cursor.getInt(cursor.getColumnIndex("lastId")) + 1;

            cursor.close();

        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return lastId;

    }

    public MemberListInfo getAllMembersForCollectionListNew(int groupID) {
        this.openReadableDatabase();
        MemberListInfo memberListInfo = new MemberListInfo();
        List<String> membersName = new ArrayList<>();
        List<String> membersSex = new ArrayList<>();
        List<Boolean> membersRealizedInfo = new ArrayList<>();
        List<Boolean> membersPaidOrNot = new ArrayList<>();
        List<Member> membersInfo = new ArrayList<>();
        List<Boolean> membersTermOverDue = new ArrayList<>();
        List<Boolean> memberNewOrOld = new ArrayList<>();
        List<Boolean> hsaLoanOrNot = new ArrayList<>();


        Cursor cursor = databaseRead.rawQuery("SELECT  T_Member.Id AS Id, T_Member.NewStatus AS NewStatus,  T_Member.Name AS Name, T_Member.FatherOrHusbandName AS FatherOrHusbandName, T_Member.Sex AS Sex , T_Member.PassbookNumber AS PassbookNumber , T_RealizableMember.InstallmentAmount AS InstallmentAmount , T_AlreadyPaidMember.Type AS Type, T_TermOverDueStatus.TermOverDue AS TermOverDue , T_HasLoanAccount.P_ProgramId AS P_ProgramId, T_OverDue.MaxOverdueAmount AS OverDueStatus " +
                " FROM (SELECT Id, NewStatus, Name , FatherOrHusbandName, PassbookNumber,Sex  FROM P_MemberView WHERE P_GroupId = " + groupID + " ORDER BY PassbookNumber) AS T_Member "
                + " LEFT JOIN "
                + "( SELECT MAX(InstallmentAmount) AS InstallmentAmount, P_Account.P_MemberId AS MemberId   FROM P_Account INNER JOIN P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID AND ScheduledDate =  " + dataSourceOperationsCommon.getWorkingDay() +
                " AND P_Account.P_ProgramId BETWEEN 100 AND 200 GROUP BY MemberId ) AS T_RealizableMember ON T_RealizableMember.MemberId = T_Member.Id "
                + " LEFT JOIN "
                + " ( SELECT MAX (P_AccountBalance.Type) AS Type, P_Account.P_MemberId AS MemberId FROM P_Account INNER JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID "
                + " AND P_AccountBalance.Date = " + dataSourceOperationsCommon.getWorkingDay() + "  GROUP BY P_Account.P_MemberId ) AS T_AlreadyPaidMember ON  T_AlreadyPaidMember.MemberId =  T_Member.Id "
                + " LEFT JOIN "
                + "( Select MAX(CASE WHEN  P_Schedule.OutstandingAmount> 0 AND  P_Schedule.OverdueAmount > 0 AND  P_Schedule.OverdueAmount =P_Schedule.OutstandingAmount THEN P_Schedule.OverdueAmount ELSE 0 END) AS TermOverDue , P_Account.P_MemberId AS MemberId FROM P_Account LEFT JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID "
                + " AND (" + dataSourceOperationsCommon.getWorkingDay()
                + " BETWEEN ScheduledDate AND NextDate) GROUP BY MemberId ) AS T_TermOverDueStatus ON T_TermOverDueStatus.MemberId = T_Member.Id"
                + "  LEFT JOIN "
                + "( SELECT  MAX(P_ProgramId) AS P_ProgramId  , MAX(P_Account.P_MemberId) AS P_MemberId  FROM P_Account  WHERE  P_ProgramId > 100 AND P_ProgramId < 200  GROUP BY P_Account.P_MemberId ) AS T_HasLoanAccount ON T_HasLoanAccount.P_MemberId =  T_Member.Id "
                + " LEFT JOIN"
                + " ( Select MAX(OverdueAmount  - InstallmentAmount) AS MaxOverdueAmount ,  P_Account.P_MemberId AS MemberId  FROM P_Account INNER JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID"
                + " AND (" + dataSourceOperationsCommon.getWorkingDay()
                + " BETWEEN ScheduledDate AND NextDate) GROUP BY P_MemberId) AS T_OverDue ON  T_OverDue.MemberId = T_Member.Id", null);
        cursor.moveToFirst();


        try {
            while (!cursor.isAfterLast()) {


                Member member = new Member();
                member.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                membersInfo.add(member);


                if (!cursor.getString(cursor.getColumnIndex("NewStatus")).equals("New")) {


                    if (cursor.getFloat(cursor.getColumnIndex("OverDueStatus")) > 0) {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")  *");
                    } else {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")");
                    }

                    memberNewOrOld.add(cursor.getPosition(), false);

                } else {

                    if (getOverDueStatusForMember(cursor.getInt(cursor.getColumnIndex("Id")))) {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")" + " -" + cursor.getString(cursor.getColumnIndex("NewStatus")) + " *");
                    } else {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")" + " -" + cursor.getString(cursor.getColumnIndex("NewStatus")));
                    }


                    memberNewOrOld.add(cursor.getPosition(), true);
                }


                if (cursor.getInt(cursor.getColumnIndex("Sex")) == 2) {

                    membersSex.add(cursor.getPosition(), "Male");
                } else {

                    membersSex.add(cursor.getPosition(), "Female");
                }


                if (cursor.getInt(cursor.getColumnIndex("InstallmentAmount")) > 0) {
                    membersRealizedInfo.add(cursor.getPosition(), true);
                } else {
                    membersRealizedInfo.add(cursor.getPosition(), false);
                }

                if (cursor.getInt(cursor.getColumnIndex("Type")) > 0) {
                    membersPaidOrNot.add(cursor.getPosition(), true);
                } else {
                    membersPaidOrNot.add(cursor.getPosition(), false);
                }


                if (cursor.getInt(cursor.getColumnIndex("TermOverDue")) > 0) {
                    membersTermOverDue.add(cursor.getPosition(), true);
                } else {
                    membersTermOverDue.add(cursor.getPosition(), false);
                }

                if (cursor.getInt(cursor.getColumnIndex("P_ProgramId")) > 0) {
                    hsaLoanOrNot.add(cursor.getPosition(), true);
                } else {
                    hsaLoanOrNot.add(cursor.getPosition(), false);
                }


                cursor.moveToNext();


            }

            cursor.close();


            memberListInfo.setMembersName(membersName);
            memberListInfo.setMembersPaidOrNot(membersPaidOrNot);
            memberListInfo.setMembersRealizedInfo(membersRealizedInfo);
            memberListInfo.setMembersSex(membersSex);
            memberListInfo.setMembersInfo(membersInfo);
            memberListInfo.setMembersTermOverDue(membersTermOverDue);
            memberListInfo.setMemberNewOrOld(memberNewOrOld);
            memberListInfo.setMemberHasLoanOrNot(hsaLoanOrNot);
        } catch (Exception e) {
            Log.i("ERROR", e.getLocalizedMessage());
        }


        return memberListInfo;
    }

    private float getTermOverDue(int memberId) {
        this.openReadableDatabase();

        float termOverDue = 0;

        Cursor cursor = databaseRead.rawQuery(" Select MAX(CASE WHEN  P_Schedule.OutstandingAmount> 0  "
                + "AND  P_Schedule.OverdueAmount > 0 AND  P_Schedule.OverdueAmount =P_Schedule.OutstandingAmount THEN P_Schedule.OverdueAmount ELSE 0 END)"
                + " AS TermOverDue  FROM P_Account LEFT JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID "
                + "  AND  P_Account.P_MemberId = " + memberId + " AND (" + dataSourceOperationsCommon.getWorkingDay()
                + " BETWEEN ScheduledDate AND NextDate )", null);
        cursor.moveToFirst();

        try {
            while (!cursor.isAfterLast()) {


                termOverDue = cursor.getFloat(cursor.getColumnIndex("TermOverDue"));

                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
        }


        return termOverDue;
    }

    public MemberListInfo getAllMembersForDynamicSpinner(int groupID) {
        this.openReadableDatabase();
        MemberListInfo memberListInfo = new MemberListInfo();
        List<String> membersName = new ArrayList<>();

        List<Member> membersInfo = new ArrayList<>();
        List<Boolean> membersPaidOrNot = new ArrayList<>();


        Cursor cursor = databaseRead.rawQuery("SELECT  T_Member.Id AS Id, T_Member.NewStatus AS NewStatus, T_Member.AdmissionDate AS AdmissionDate ,  T_Member.Name AS Name, T_Member.FatherOrHusbandName AS FatherOrHusbandName, T_Member.Sex AS Sex , T_Member.PassbookNumber AS PassbookNumber , T_AlreadyPaidMember.Type AS Type,  T_OverDue.MaxOverdueAmount AS OverDueStatus " +
                " FROM (SELECT Id, NewStatus, Name , FatherOrHusbandName, PassbookNumber, Sex , AdmissionDate  FROM P_MemberView WHERE P_GroupId = " + groupID + " ORDER BY PassbookNumber) AS T_Member "
                + " LEFT JOIN "
                + " ( SELECT MAX (P_AccountBalance.Type) AS Type, P_Account.P_MemberId AS MemberId FROM P_Account INNER JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID "
                + " AND P_AccountBalance.Date = " + dataSourceOperationsCommon.getWorkingDay() + "  GROUP BY P_Account.P_MemberId ) AS T_AlreadyPaidMember ON  T_AlreadyPaidMember.MemberId =  T_Member.Id "
                + " LEFT JOIN "
                + "( Select MAX(OverdueAmount  - InstallmentAmount) AS MaxOverdueAmount ,  P_Account.P_MemberId AS MemberId  FROM P_Account INNER JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID"
                + " AND (" + dataSourceOperationsCommon.getWorkingDay()
                + " BETWEEN ScheduledDate AND NextDate) GROUP BY P_MemberId) AS T_OverDue ON  T_OverDue.MemberId = T_Member.Id", null);


        cursor.moveToFirst();

        try {
            while (!cursor.isAfterLast()) {


                Member member = new Member();
                member.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                member.setAdmissionDateInteger(cursor.getInt(cursor.getColumnIndex("AdmissionDate")));
                member.setMemberOldOrNew(cursor.getString(cursor.getColumnIndex("NewStatus")));
                membersInfo.add(member);


                if (!cursor.getString(cursor.getColumnIndex("NewStatus")).equals("New")) {


                    if (cursor.getFloat(cursor.getColumnIndex("OverDueStatus")) > 0) {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")  *");
                    } else {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")");
                    }
                } else {

                    if (getOverDueStatusForMember(cursor.getInt(cursor.getColumnIndex("Id")))) {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")" + " -" + cursor.getString(cursor.getColumnIndex("NewStatus")) + " *");
                    } else {
                        membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name")) + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")" + " -" + cursor.getString(cursor.getColumnIndex("NewStatus")));
                    }

                }

                if (cursor.getInt(cursor.getColumnIndex("Type")) > 0) {
                    membersPaidOrNot.add(cursor.getPosition(), true);
                } else {
                    membersPaidOrNot.add(cursor.getPosition(), false);
                }


                cursor.moveToNext();
            }

            cursor.close();

            memberListInfo.setMembersName(membersName);
            memberListInfo.setMembersInfo(membersInfo);
            memberListInfo.setMembersPaidOrNot(membersPaidOrNot);
        } catch (Exception e) {

            Log.i("Exception", e.getMessage());
        }


        return memberListInfo;
    }

    public MemberListInfo getAllMembersForDynamicSpinnerBadDebt(int groupID) {
        this.openReadableDatabase();
        MemberListInfo memberListInfo = new MemberListInfo();
        List<String> membersName = new ArrayList<>();

        List<Member> membersInfo = new ArrayList<>();
        List<Boolean> membersPaidOrNot = new ArrayList<>();


        Cursor cursor = databaseRead.rawQuery("SELECT  T_Member.Id AS Id, T_Member.NewStatus AS NewStatus,  T_Member.Name AS Name, T_Member.FatherOrHusbandName AS FatherOrHusbandName, T_Member.Sex AS Sex , T_Member.PassbookNumber AS PassbookNumber , T_AlreadyPaidMember.Type AS Type" +
                " FROM (SELECT Id, NewStatus, Name , FatherOrHusbandName, PassbookNumber,Sex  FROM P_MemberView WHERE P_GroupId = " + groupID + " ORDER BY PassbookNumber) AS T_Member "
                + " LEFT JOIN "
                + " ( SELECT MAX (P_AccountBalance.Type) AS Type, P_Account.P_MemberId AS MemberId FROM P_Account INNER JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID "
                + " AND P_AccountBalance.Date = " + dataSourceOperationsCommon.getWorkingDay() + "  GROUP BY P_Account.P_MemberId ) AS T_AlreadyPaidMember ON  T_AlreadyPaidMember.MemberId =  T_Member.Id ", null);


        cursor.moveToFirst();

        try {
            while (!cursor.isAfterLast()) {


                Member member = new Member();
                member.setId(cursor.getInt(cursor.getColumnIndex("Id")));
                membersInfo.add(member);


                if (!cursor.getString(cursor.getColumnIndex("NewStatus")).equals("New")) {

                    membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name"))
                            + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName"))
                            + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")");

                } else {

                    membersName.add(cursor.getPosition(), cursor.getString(cursor.getColumnIndex("Name"))
                            + "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName"))
                            + " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")"
                            + " -" + cursor.getString(cursor.getColumnIndex("NewStatus")));
                }

                if (cursor.getInt(cursor.getColumnIndex("Type")) > 0) {
                    membersPaidOrNot.add(cursor.getPosition(), true);
                } else {
                    membersPaidOrNot.add(cursor.getPosition(), false);
                }


                cursor.moveToNext();
            }

            cursor.close();

            memberListInfo.setMembersName(membersName);
            memberListInfo.setMembersInfo(membersInfo);
            memberListInfo.setMembersPaidOrNot(membersPaidOrNot);
        } catch (Exception e) {

            Log.i("Exception", e.getMessage());
        }


        return memberListInfo;
    }

    public boolean getAlreadyPaidMemberData(int memberID) {
        this.openReadableDatabase();
        boolean isPaid = false;
        Cursor cursor = databaseRead.rawQuery(
                "SELECT MAX(P_AccountBalance.Type) AS Type FROM P_Account INNER JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID AND P_AccountBalance.Date = " + dataSourceOperationsCommon.getWorkingDay() + "  AND P_Account.P_MemberId = " + memberID + " GROUP BY P_Account.P_MemberId",
                null);
        cursor.moveToFirst();

        try {

            if (cursor.getCount() > 0) {
                while (!cursor.isAfterLast()) {
                    if (cursor.getInt(cursor.getColumnIndex("Type")) > 0) {
                        isPaid = true;
                        break;
                    }
                    cursor.moveToNext();
                }

            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.toString());
        }
        return isPaid;

    }

    private Boolean getOverDueStatusForMember(int memberID) {
        this.openReadableDatabase();
        boolean isOverDue = false;
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        String queryGetLoan = "Select MAX(OverdueAmount - InstallmentAmount) AS MaxOverdueAmount , P_MemberId FROM P_Account INNER JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID  AND P_Account.P_MemberId = '"
                + memberID + "' AND ('" + workingDay
                + "' BETWEEN ScheduledDate AND NextDate) GROUP BY P_MemberId";
        try {
            Cursor cursor = databaseRead.rawQuery(queryGetLoan, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                if (cursor.getFloat(cursor.getColumnIndex("MaxOverdueAmount")) > 0) {
                    isOverDue = true;
                    break;
                }
                cursor.moveToNext();
            }
            cursor.close();

        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return isOverDue;
    }

    public boolean hasPrimaryLoanOutstandingNotZero(int memberId) {

        this.openReadableDatabase();
        Cursor cursor = databaseRead.rawQuery(" SELECT P_Program.IsPrimary AS IsPrimary , P_Program.Program_ID AS ProgramId , T_Account.Balance AS Balance , T_Account.IsSupplementary As IsSupplementary FROM " +
                "( SELECT P_Account.P_ProgramId AS P_ProgramId , P_Account.IsSupplementary As IsSupplementary , SUM(P_AccountBalance.Debit - P_AccountBalance.Credit) AS Balance " +
                " FROM  P_MemberView   INNER JOIN P_Account ON  P_Account.P_MemberId = P_MemberView.Id   AND P_MemberView.Id= " + memberId + " " +
                " INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId GROUP BY P_Account.Account_ID) AS T_Account " +
                " LEFT JOIN P_Program ON T_Account.P_ProgramId = P_Program.Program_ID  ", null);

        cursor.moveToFirst();

        try {

            while (!cursor.isAfterLast()) {
                if (cursor.getInt(cursor.getColumnIndex("IsPrimary")) > 0
                        && cursor.getInt(cursor.getColumnIndex("ProgramId")) > 100
                        && cursor.getInt(cursor.getColumnIndex("ProgramId")) < 200
                        && cursor.getDouble(cursor.getColumnIndex("Balance")) > 0 && cursor.getInt(cursor.getColumnIndex("IsSupplementary"))==0) {

                    return true;
                }

                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }


        return false;

    }

    public boolean hasNewPrimaryLoan(int memberId,int accountId) {

        this.openReadableDatabase();
        Cursor cursor = databaseRead.rawQuery("SELECT NewLoanAccount.NewLoan AS TotalNewLoan  FROM P_Account INNER JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId INNER JOIN (SELECT P_Account.NewLoan , P_Account.P_MemberId FROM P_Account WHERE P_Account.NewLoan = 1 AND P_Account.P_MemberId = "+memberId+" ) AS NewLoanAccount ON NewLoanAccount.P_MemberId AND P_Account.P_MemberId = "+memberId+" AND P_Account.P_ProgramTypeId = 1 AND P_Account.IsSupplementary = 0 AND P_Account.NewLoan =0 AND P_Program.IsPrimary = 1 AND P_Account.Account_ID = "+accountId+"", null);

        //Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_Account  INNER JOIN (SELECT MAX(P_Account.P_MemberId ) AS P_MemberId , SUM (P_Account.NewLoan) AS NewLoanTotal  FROM P_Account WHERE P_Account.NewLoan = 1 GROUP BY P_Account.P_MemberId  ) AS NewLoanTable ON NewLoanTable.P_MemberId = P_Account.Account_ID  AND P_Account.P_MemberId = " + memberId + " AND Account_ID = "+accountId+" AND  IsSupplementary = 0 AND   P_Account.P_ProgramId = "+programId+"", null);
        cursor.moveToFirst();

        try {
            if(cursor.getCount()>0 && cursor.getInt(cursor.getColumnIndex("TotalNewLoan"))>0)
            {
                return true;
            }


        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }
        cursor.close();
        return false;

    }

    public List<AccountForDailyTransaction> getLoanAccountsOfMembers(int memberID) {
        this.openReadableDatabase();
        List<AccountForDailyTransaction> accountLoanList = new ArrayList<>();

        String query = "SELECT P_Account.ProgramName,ServiceChargeAmount, P_Account.P_Duration, P_Account.NewAccount AS NewAccount,  P_Account.LoanInsurance, P_Account.P_SchemeId, P_Account.P_InstallmentType, P_Account.P_FundId, DisbursedAmount, P_Account.Flag AS Flag, Account_ID, P_Account.P_ProgramId,   P_Account.OpeningDate, SUM(P_AccountBalance.Debit - P_AccountBalance.Credit) AS Balance, MAX(MinimumDeposit) AS MinimumDeposit, P_ProgramId AS ProgramId    FROM P_Account " +
                "INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId AND  ProgramId > 100 AND ProgramId <200 AND P_Account.P_MemberId = '"
                + memberID
                + "' GROUP BY P_Account.Account_ID  ORDER BY ProgramId";
        try {
            Cursor cursor = databaseRead.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                boolean primary = getIsPrimary(cursor.getInt(cursor.getColumnIndex("P_ProgramId")));

                if (primary && !cursor.isFirst()) {
                    AccountForDailyTransaction accountForDailyTransaction;
                    accountForDailyTransaction = accountLoanList.get(0);
                    Schedule schedule = getScheduleInformationWithOutWorkingDay(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                    AccountForDailyTransaction accountForDailyTransactionPrimary = new AccountForDailyTransaction();
                    accountForDailyTransactionPrimary.setProgramName(cursor.getString(cursor.getColumnIndex("ProgramName")));
                    accountForDailyTransactionPrimary.setProgramId(cursor.getInt(cursor.getColumnIndex("P_ProgramId")));
                    accountForDailyTransactionPrimary.setBalance(cursor.getFloat(cursor.getColumnIndex("Balance")));
                    accountForDailyTransactionPrimary.setOpeningDateValue(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate"))));
                    accountForDailyTransactionPrimary.setAccountId(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                    accountForDailyTransactionPrimary.setServiceChargeAmount(cursor.getFloat(cursor.getColumnIndex("ServiceChargeAmount")));
                    accountForDailyTransactionPrimary.setDisbursedAmount(cursor.getFloat(cursor.getColumnIndex("DisbursedAmount")));
                    accountForDailyTransactionPrimary.setOpeningDate(cursor.getInt(cursor.getColumnIndex("OpeningDate")));
                    if (cursor.getInt(cursor.getColumnIndex("Flag")) == 1) {
                        accountForDailyTransactionPrimary.setFlag(1);
                        accountForDailyTransactionPrimary.setDuration(cursor.getInt(cursor.getColumnIndex("P_Duration")));
                        accountForDailyTransactionPrimary.setLoanInsuranceAmount(cursor.getFloat(cursor.getColumnIndex("LoanInsurance")));
                        accountForDailyTransactionPrimary.setFundId(cursor.getInt(cursor.getColumnIndex("P_FundId")));
                        accountForDailyTransactionPrimary.setSchemeId(cursor.getInt(cursor.getColumnIndex("P_SchemeId")));
                        accountForDailyTransactionPrimary.setInstallmentType(cursor.getInt(cursor.getColumnIndex("P_InstallmentType")));
                        accountForDailyTransactionPrimary.setInstallmentNumber(schedule.getMaxInstallmentNumber());
                    } else {
                        accountForDailyTransactionPrimary.setFlag(0);
                    }


                    if (schedule.getBaseInstallmentAmount() == null) {
                        accountForDailyTransactionPrimary.setBaseInstallmentAmount(0);
                    } else {
                        accountForDailyTransactionPrimary.setBaseInstallmentAmount(schedule.getBaseInstallmentAmount());
                    }
                    accountLoanList.set(0, accountForDailyTransactionPrimary);
                    accountLoanList.add(accountForDailyTransaction);
                } else {
                    Schedule schedule = getScheduleInformationWithOutWorkingDay(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                    AccountForDailyTransaction accountForDailyTransaction = new AccountForDailyTransaction();
                    accountForDailyTransaction.setProgramName(cursor.getString(cursor.getColumnIndex("ProgramName")));
                    accountForDailyTransaction.setProgramId(cursor.getInt(cursor.getColumnIndex("P_ProgramId")));
                    accountForDailyTransaction.setBalance(cursor.getFloat(cursor.getColumnIndex("Balance")));
                    accountForDailyTransaction.setOpeningDateValue(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("OpeningDate"))));
                    accountForDailyTransaction.setAccountId(cursor.getInt(cursor.getColumnIndex("Account_ID")));
                    accountForDailyTransaction.setServiceChargeAmount(cursor.getFloat(cursor.getColumnIndex("ServiceChargeAmount")));
                    accountForDailyTransaction.setDisbursedAmount(cursor.getFloat(cursor.getColumnIndex("DisbursedAmount")));
                    accountForDailyTransaction.setOpeningDate(cursor.getInt(cursor.getColumnIndex("OpeningDate")));
                    if (cursor.getInt(cursor.getColumnIndex("Flag")) == 1) {
                        accountForDailyTransaction.setFlag(1);
                        accountForDailyTransaction.setDuration(cursor.getInt(cursor.getColumnIndex("P_Duration")));
                        accountForDailyTransaction.setLoanInsuranceAmount(cursor.getFloat(cursor.getColumnIndex("LoanInsurance")));
                        accountForDailyTransaction.setFundId(cursor.getInt(cursor.getColumnIndex("P_FundId")));
                        accountForDailyTransaction.setSchemeId(cursor.getInt(cursor.getColumnIndex("P_SchemeId")));
                        accountForDailyTransaction.setInstallmentType(cursor.getInt(cursor.getColumnIndex("P_InstallmentType")));
                        accountForDailyTransaction.setInstallmentNumber(schedule.getMaxInstallmentNumber());

                    } else {
                        accountForDailyTransaction.setFlag(0);
                    }


                    if (schedule.getBaseInstallmentAmount() == null) {
                        accountForDailyTransaction.setBaseInstallmentAmount(0);
                    } else {
                        accountForDailyTransaction.setBaseInstallmentAmount(schedule.getBaseInstallmentAmount());
                    }

                    accountLoanList.add(accountForDailyTransaction);
                }

                cursor.moveToNext();
            }
            cursor.close();

        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        return accountLoanList;
    }

    public FundSchemeList getFundAndScheme() {
        this.openReadableDatabase();
        FundSchemeList fundSchemeList = new FundSchemeList();
        List<String> fundNameList = new ArrayList<>();
        List<Integer> fundIdList = new ArrayList<>();
        List<String> schemeNameList = new ArrayList<>();
        List<Integer> schemeIdList = new ArrayList<>();
        try {
            Cursor cursor = databaseRead.rawQuery("Select * FROM P_Fund ORDER BY Name", null);
            cursor.moveToFirst();

            fundNameList.add(0, "Select Fund Type");
            fundIdList.add(0, 0);
            while (!cursor.isAfterLast()) {

                fundNameList.add(cursor.getString(cursor.getColumnIndex("Name")));
                fundIdList.add(cursor.getInt(cursor.getColumnIndex("Id")));


                cursor.moveToNext();
            }


            cursor.close();

            cursor = databaseRead.rawQuery("Select * FROM P_Scheme ORDER BY Name", null);
            cursor.moveToFirst();
            schemeNameList.add(0, "Select Scheme");
            schemeIdList.add(0, 0);
            while (!cursor.isAfterLast()) {

                schemeNameList.add(cursor.getString(cursor.getColumnIndex("Name")));
                schemeIdList.add(cursor.getInt(cursor.getColumnIndex("Id")));
                cursor.moveToNext();
            }
            cursor.close();

        } catch (SQLException e) {
            Log.i("Sql", e.toString());
        }
        fundSchemeList.setFundListName(fundNameList);
        fundSchemeList.setFundIdList(fundIdList);
        fundSchemeList.setSchemeListName(schemeNameList);
        fundSchemeList.setSchemeIdList(schemeIdList);
        return fundSchemeList;
    }

    public String getFundName(int fundId) {
        this.openReadableDatabase();
        String fundName = null;
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_Fund WHERE Id = " + fundId + "", null);
            cursor.moveToFirst();
            fundName = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }


        return fundName;
    }

    public String getSchemeName(int schemeId) {
        this.openReadableDatabase();
        String schemeName = null;
        try {
            Cursor cursor = databaseRead.rawQuery("Select Name FROM P_Scheme WHERE Id =" + schemeId + "", null);
            cursor.moveToFirst();
            schemeName = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
        }


        return schemeName;
    }

    public LoanDisburseData getProgram(int groupTypeId, int loanGroupProgramId, List<Integer> programIdList) {
        this.openReadableDatabase();

        LoanDisburseData loanDisburseData = new LoanDisburseData();
        List<String> programsName = new ArrayList<>();
        List<Integer> programsIdentity = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();
        StringBuilder programList = new StringBuilder();
        for (int i = 0; i < programIdList.size(); i++) {
            if (i == 0) {
                programList.append(" ").append(programIdList.get(i)).append(" ");
            } else {
                programList.append(",").append(programIdList.get(i)).append(" ");
            }
        }

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_LoanGroup" +
                " INNER JOIN  P_Program  ON P_ProgramId = P_Program.Program_ID AND P_Program.Program_ID NOT IN (" + programList + ")  AND  P_GroupTypeId = " + groupTypeId + "  AND P_LoanGroupProgramId = " + loanGroupProgramId +
                "    AND P_ProgramTypeId = 1 AND IsPrimary =1 AND '" + workingDay + "' BETWEEN StartingDate AND EndingDate GROUP BY Program_ID ORDER BY IsPrimary DESC ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            programsName.add(cursor.getString(cursor.getColumnIndex("ShortName")));
            programsIdentity.add(cursor.getInt(cursor.getColumnIndex("Program_ID")));

            cursor.moveToNext();
        }


        cursor.close();

        loanDisburseData.setNameList(programsName);
        loanDisburseData.setIdList(programsIdentity);

        return loanDisburseData;
    }


    public LoanDisburseData getProgramForLoanDisburse(int groupTypeId, int loanGroupProgramId) {
        this.openReadableDatabase();

        LoanDisburseData loanDisburseData = new LoanDisburseData();
        List<String> programsName = new ArrayList<>();
        List<Integer> programsIdentity = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_LoanGroup" +
                " INNER JOIN  P_Program  ON P_ProgramId = P_Program.Program_ID AND   P_GroupTypeId = " + groupTypeId + "  AND P_LoanGroupProgramId = " + loanGroupProgramId +
                "    AND P_ProgramTypeId = 1 AND IsPrimary =1 AND '" + workingDay + "' BETWEEN StartingDate AND EndingDate GROUP BY Program_ID ORDER BY IsPrimary DESC ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            programsName.add(cursor.getString(cursor.getColumnIndex("ShortName")));
            programsIdentity.add(cursor.getInt(cursor.getColumnIndex("Program_ID")));

            cursor.moveToNext();
        }


        cursor.close();

        loanDisburseData.setNameList(programsName);
        loanDisburseData.setIdList(programsIdentity);

        return loanDisburseData;
    }

    public LoanDisburseData getDuration(int groupTypeId, int LoanGroupProgramId, int programId) {
        this.openReadableDatabase();

        LoanDisburseData loanDisburseData = new LoanDisburseData();
        List<String> durationList = new ArrayList<>();
        List<Integer> durationIdentityList = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_LoanGroupDuration WHERE P_GroupTypeId =  " + groupTypeId + "  AND P_LoanGroupProgramId = " + LoanGroupProgramId +
                " AND P_ProgramId = " + programId + "  AND  " + workingDay + "  BETWEEN StartingDateDuration AND EndingDateDuration  ORDER BY P_Duration ASC", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int duration = cursor.getInt(cursor.getColumnIndex("P_Duration"));
            durationList.add(String.valueOf(duration));
            durationIdentityList.add(duration);
            cursor.moveToNext();
        }


        cursor.close();

        loanDisburseData.setIdList(durationIdentityList);
        loanDisburseData.setNameList(durationList);

        return loanDisburseData;
    }

    public LoanDisburseData getInstallment(int groupTypeId, int LoanGroupProgramId, int programId, int duration) {
        this.openReadableDatabase();

        LoanDisburseData loanDisburseData = new LoanDisburseData();
        List<String> installmentNameList = new ArrayList<>();
        List<Integer> installmentIdentityList = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_LoanGroupInstallment WHERE P_GroupTypeId =  " + groupTypeId + "  AND P_LoanGroupProgramId = " + LoanGroupProgramId +
                " AND P_ProgramId = " + programId + "  AND ( P_Duration = " + duration + " OR P_Duration = -1 ) AND  " + workingDay + "  BETWEEN StartingDateDuration AND EndingDateDuration GROUP BY P_InstallmentType  ORDER BY P_InstallmentType", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int check = cursor.getInt(cursor.getColumnIndex("P_InstallmentType"));

            installmentIdentityList.add(check);
            installmentNameList.add(getInstallmentTypeName(check));


            cursor.moveToNext();
        }


        cursor.close();


        loanDisburseData.setNameList(installmentNameList);
        loanDisburseData.setIdList(installmentIdentityList);


        return loanDisburseData;
    }

    public String getInstallmentTypeName(int InstallmentTypeId) {
        this.openReadableDatabase();

        String InstallmentTypeName;


        Cursor cursor = databaseRead.rawQuery("SELECT Name FROM P_InstallmentType WHERE Installment_Type = " + InstallmentTypeId + "", null);
        cursor.moveToFirst();

        InstallmentTypeName = cursor.getString(cursor.getColumnIndex("Name"));


        cursor.close();


        return InstallmentTypeName;
    }

    public LoanDisburseData getGracePeriod(int programId, int duration, int installmentType, int sex) {
        this.openReadableDatabase();

        LoanDisburseData loanDisburseData = new LoanDisburseData();
        List<String> gracePeriodList = new ArrayList<>();
        List<Integer> gracePeriodIdentityList = new ArrayList<>();
        int workingDay = dataSourceOperationsCommon.getWorkingDay();

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_GracePeriod WHERE P_ProgramId =  " + programId + "  AND (P_Duration =  " + duration +
                "  OR P_Duration = -1)  AND  ( P_InstallmentType = " + installmentType + " OR P_InstallmentType = -1 )  AND ( Sex =" + sex + " OR Sex = -1 )  AND  " + workingDay + "  BETWEEN StartingDate AND EndingDate  ORDER BY GracePeriod", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int value = cursor.getInt(cursor.getColumnIndex("GracePeriod"));
            gracePeriodList.add(String.valueOf(value));
            gracePeriodIdentityList.add(value);
            cursor.moveToNext();
        }


        cursor.close();

        loanDisburseData.setIdList(gracePeriodIdentityList);
        loanDisburseData.setNameList(gracePeriodList);

        return loanDisburseData;
    }

    public int groupMeetingDayActual(int groupId) {
        this.openReadableDatabase();

        int groupMeetingDay;

        Cursor cursor = databaseRead.rawQuery("SELECT MeetingDay FROM P_Group WHERE ID = " + groupId + "", null);
        cursor.moveToFirst();

        groupMeetingDay = cursor.getInt(cursor.getColumnIndex("MeetingDay"));

        cursor.close();


        return groupMeetingDay;
    }

    public float getServiceChargeRate(int programId, int duration, int installmentType, int sex, int openingDate, int fundId) {
        this.openReadableDatabase();
        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_ServiceCharge WHERE P_ProgramId = " + programId + "  AND " + openingDate + " BETWEEN StartingDateInteger AND EndingDateInteger  "
                + "AND  (P_Duration = " + duration + " OR  P_Duration = -1 ) AND  (P_InstallmentType = " + installmentType + " OR P_InstallmentType =  -1 ) " +
                " AND (Sex = " + sex + " OR Sex =  -1 )  AND  (P_FundId = " + fundId + " OR P_FundId =  -1 ) ORDER BY P_FundId DESC", null);
        cursor.moveToFirst();
        float serviceChargeRate = cursor.getFloat(cursor.getColumnIndex("DecliningServiceCharge"));


        cursor.close();


        return serviceChargeRate;
    }

    public LoanDisburseData getInstallmentDaysForWeek(int searchStartDay, int meetingDay, int installmentCount, int gracePeriod) {
        this.openReadableDatabase();

        LoanDisburseData installmentDates = new LoanDisburseData();
        List<String> datesString = new ArrayList<>();
        List<Integer> datesInteger = new ArrayList<>();

        int dayIdMatching = dataSourceOperationsCommon.getWorkingDayInfo();


        int startFlag = 0;
        Cursor cursor;
        if (meetingDay == 7) {
            cursor = databaseRead.rawQuery("SELECT * FROM Calender WHERE DayId = " + dataSourceOperationsCommon.getMeetingDayInWorkingDay() + "  AND Date > " + searchStartDay + " AND IsWeeklyHoliday!= 1 AND IsSpecialHoliday!= 1 ORDER BY  Date ", null);
        } else {


            cursor = databaseRead.rawQuery("SELECT * FROM Calender WHERE DayId = " + meetingDay + "  AND Date > " + searchStartDay + " AND IsWeeklyHoliday!= 1 AND IsSpecialHoliday!= 1 ORDER BY  Date ", null);
        }

        /*Date workingDate = new DateAndDataConversion().getDateValueFromInt(dataSourceOperationsCommon.getWorkingDay());
        Date cursorDate = new DateAndDataConversion().getDateValueFromInt(cursor.getInt(cursor.getColumnIndex("Date")));*/



        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {


            /*if ((dayIdMatching != meetingDay) && startFlag == 0 && gracePeriod == 0) {
                cursor.moveToNext();

            }*/

            if(new DateAndDataConversion().dayDifferenceBetweenTwoDays(
                    new DateAndDataConversion().getDateValueFromInt(dataSourceOperationsCommon.getWorkingDay()),
                    new DateAndDataConversion().getDateValueFromInt(cursor.getInt(cursor.getColumnIndex("Date"))))<14)
            {
                cursor.moveToNext();
            }
            else if (installmentCount == startFlag) {
                break;
            }

            datesString.add(cursor.getString(cursor.getColumnIndex("DayShortName")) + ", " + cursor.getString(cursor.getColumnIndex("RealDate")));
            datesInteger.add(cursor.getInt(cursor.getColumnIndex("Date")));

            startFlag++;
            cursor.moveToNext();
        }


        cursor.close();


        installmentDates.setIdList(datesInteger);
        installmentDates.setNameList(datesString);

        return installmentDates;
    }

    public CalenderData getInstallmentDayForMonth(int day, int month, int year, int meetingDay) {
        this.openReadableDatabase();

        List<CalenderData> calenderDataList = new ArrayList<>();
        CalenderData calenderData;

        Cursor cursor;
        if (meetingDay == 7) {

            cursor = databaseRead.rawQuery("SELECT * FROM Calender WHERE Day >= " + day + "  AND Month =  " + month + " AND Year = " + year + " AND IsWeeklyHoliday!= 1 AND IsSpecialHoliday!= 1 ORDER BY  Date ", null);

        } else {
            cursor = databaseRead.rawQuery("SELECT * FROM Calender WHERE DayId = " + meetingDay + "  AND Month =  " + month + " AND Year = " + year + " AND IsWeeklyHoliday!= 1 AND IsSpecialHoliday!= 1 ORDER BY  Date ", null);
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            calenderData = new CalenderData();
            calenderData.setDateInteger(cursor.getInt(cursor.getColumnIndex("Date")));
            calenderData.setDateString(cursor.getString(cursor.getColumnIndex("DayShortName")) + ", " + cursor.getString(cursor.getColumnIndex("RealDate")));
            calenderData.setDayInteger(cursor.getInt(cursor.getColumnIndex("Day")));

            if (calenderData.getDayInteger() == day) {
                return calenderData;
            }
            calenderDataList.add(calenderData);
            cursor.moveToNext();

        }
        cursor.close();
        calenderData = new CalenderData();
        calenderData.setDateString("");
        calenderData.setDayInteger(0);
        calenderData.setDateInteger(0);


        if (meetingDay != 7) {
            for (int j = calenderDataList.size() - 1; j >= 0; j--) {
                if (calenderDataList.get(j).getDayInteger() < day) {

                    if(day-calenderDataList.get(j).getDayInteger()>6)
                    {
                        break;
                    }
                    else
                    {
                        return calenderDataList.get(j);
                    }

                }
            }


            for (int j = 0; j < calenderDataList.size(); j++) {
                if (calenderDataList.get(j).getDayInteger() >= day) {
                    return calenderDataList.get(j);
                }
            }

            for (int j = calenderDataList.size() - 1; j >= 0; j--) {
                if (calenderDataList.get(j).getDayInteger() <= day) {
                    return calenderDataList.get(j);
                }
            }

        } else {
            for (int j = 0; j < calenderDataList.size(); j++) {
                if (calenderDataList.get(j).getDayInteger() >= day) {
                    calenderData = calenderDataList.get(j);
                    break;
                }
            }

            for (int j = calenderDataList.size() - 1; j >= 0; j--) {
                if (calenderDataList.get(j).getDayInteger() <= day) {
                    return calenderDataList.get(j);
                }
            }
        }
        return calenderData;
    }

    public int getInstallmentCount(int programId, int duration, int installmentType, int gracePeriod, int sex) {
        this.openReadableDatabase();

        int installmentCount;

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_InstallmentCount WHERE P_ProgramId = " + programId + "  AND " + dataSourceOperationsCommon.getWorkingDay() + " BETWEEN StartingDate AND EndingDate  "
                + "AND  (P_Duration = " + duration + "  OR P_Duration = -1) AND  P_InstallmentType = " + installmentType +
                " AND GracePeriod = " + gracePeriod + " AND (Sex = " + sex + " OR Sex =  -1 )", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            installmentCount = cursor.getInt(cursor.getColumnIndex("InstallmentCount"));
        } else {
            installmentCount = 0;
        }
        cursor.close();
        return installmentCount;
    }

    private ProgramNameChange getProgramNameValue(int programId, String programName) {
        this.openReadableDatabase();

        ProgramNameChange programNameChange = new ProgramNameChange();

        Cursor cursor = databaseRead.rawQuery("SELECT * FROM ProgramNameChange WHERE Id = " + programId + "", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            programNameChange.setId(programId);
            programNameChange.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));
            programNameChange.setChangedName(cursor.getString(cursor.getColumnIndex("ChangedName")));
            programNameChange.setValidName(cursor.getString(cursor.getColumnIndex("ValidName")));
        } else {
            programNameChange.setId(programId);
            programNameChange.setChangedName(programName);
            programNameChange.setShortName(programName);
            programNameChange.setValidName(titleCase(programName));
        }


        cursor.close();


        return programNameChange;
    }

    public InstallmentAmount getInstallmentAmount(int programId, int duration, int installmentType, int gracePeriod, int sex) {
        this.openReadableDatabase();


        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_InstallmentAmount WHERE P_ProgramId = " + programId + "  AND " + dataSourceOperationsCommon.getWorkingDay() + " BETWEEN StartingDate AND EndingDate  "
                + "AND  P_Duration = " + duration + " AND  ( P_InstallmentType = " + installmentType +
                " OR P_InstallmentType = -1) AND (GracePeriod = " + gracePeriod + " OR GracePeriod = -1)  AND (Sex = " + sex + " OR Sex =  -1 )", null);

        cursor.moveToFirst();
        InstallmentAmount installmentAmount = new InstallmentAmount();
        if (cursor.getCount() > 0) {


            installmentAmount.setBaseAmount(cursor.getFloat(cursor.getColumnIndex("BaseAmount")));
            installmentAmount.setAmountPerBase(cursor.getFloat(cursor.getColumnIndex("AmountPerBase")));
            installmentAmount.setCalculationMode(cursor.getInt(cursor.getColumnIndex("CalculationMode")));
        } else {
            installmentAmount.setBaseAmount((float) 0);
            installmentAmount.setAmountPerBase((float) 0);
            installmentAmount.setCalculationMode(0);
        }
        cursor.close();
        return installmentAmount;
    }

    public Group getGroupInfo(int groupId) {

        this.openReadableDatabase();
        Group group = new Group();

        String quarry = "SELECT * FROM P_Group WHERE ID = " + groupId + "";
        try {
            Cursor cursor = databaseRead.rawQuery(quarry, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                group.setDefaultProgramId(cursor.getInt(cursor.getColumnIndex("P_DefaultProgramId")));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
        return group;
    }

    public List<SearchData> getSearchData(String groupName, String memberName, String fatherOrHusbandName, String NID, int programOfficerId) {

        this.openReadableDatabase();
        List<SearchData> searchDataList = new ArrayList<>();


        String quarry = "SELECT P_Group.Name AS GroupName,  P_MemberView.NewStatus AS NewStatus, Days.ShortName AS MeetingDay,  P_MemberView.Name AS MemberName, CASE WHEN  P_MemberView.Phone <> 'null' THEN  P_MemberView.Phone ELSE 'Not Set' END AS Phone, "
                + " P_MemberView.FatherOrHusbandName AS FatherOrHusbandName, P_MemberView.PassbookNumber AS PassbookNumber ,CASE WHEN  P_MemberView.NationalIdNumber <> 'null' THEN  P_MemberView.NationalIdNumber ELSE 'Not Set' END  AS NationalID , P_MemberView.AdmissionDate FROM P_Group INNER JOIN Days ON Days.DayID = P_Group.MeetingDay AND P_ProgramOfficerId= " + programOfficerId + " AND P_Group.Name LIKE '%" + groupName.trim() + "%'  " +
                " INNER JOIN P_MemberView ON P_Group.ID = P_MemberView.P_GroupId  AND P_MemberView.Name LIKE '%" + memberName.trim() + "%'   AND   P_MemberView.FatherOrHusbandName   LIKE '%" + fatherOrHusbandName.trim() + "%'  AND NationalID LIKE '%" + NID.trim() + "%'  "
                + " ORDER BY (CASE WHEN GroupName like '" + groupName.trim() + "%' then 0 else 1 end)  , (CASE WHEN MemberName like '" + memberName.trim() + "%' then 0 else 1 end) , (CASE WHEN FatherOrHusbandName like '" + fatherOrHusbandName.trim() + "%' then 0 else 1 end) , (CASE WHEN NationalID like '" + NID.trim() + "%' then 0 else 1 end) ,  GroupName , MemberName  , FatherOrHusbandName  , NationalID , PassbookNumber ";
        try {
            Cursor cursor = databaseRead.rawQuery(quarry, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                SearchData searchData = new SearchData();

                searchData.setPassbookNumber(cursor.getInt(cursor.getColumnIndex("PassbookNumber")));
                searchData.setGroupName(cursor.getString(cursor.getColumnIndex("GroupName")));
                searchData.setMeetingDay(cursor.getString(cursor.getColumnIndex("MeetingDay")));
                searchData.setFatherOrHusbandName(cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")));
                searchData.setMemberName(cursor.getString(cursor.getColumnIndex("MemberName")));

                searchData.setNationalIdNumber(cursor.getString(cursor.getColumnIndex("NationalID")));
                searchData.setAdmissionDate(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("AdmissionDate"))));

                searchData.setPhoneNumber(cursor.getString(cursor.getColumnIndex("Phone")));

                searchDataList.add(searchData);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
        return searchDataList;
    }

    public boolean isNidValid(String nid) {

        this.openReadableDatabase();
        boolean valid = false;
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_MemberView WHERE NationalIdNumber = '" + nid + "'", null);
            cursor.moveToFirst();
            valid = cursor.getCount() <= 0;

            cursor.close();
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }
        return valid;
    }

    public boolean isNidValidForOldMember(String nid, int memberId) {

        this.openReadableDatabase();
        boolean valid = false;
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_MemberView WHERE NationalIdNumber = '" + nid + "' AND Id != " + memberId + "", null);
            cursor.moveToFirst();
            valid = cursor.getCount() <= 0;

            cursor.close();
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }

        return valid;
    }

    public boolean hasNIdOrBirthCertificate(int memberId) {

        this.openReadableDatabase();
        boolean valid = false;
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_MemberView WHERE Id = " + memberId + "", null);
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                String nid = "";
                String birthCertificateNumber = "";
                if (cursor.getString(cursor.getColumnIndex("NationalIdNumber")) != null) {
                    nid = cursor.getString(cursor.getColumnIndex("NationalIdNumber"));
                }
                if (cursor.getString(cursor.getColumnIndex("BirthCertificateNumber")) != null) {
                    birthCertificateNumber = cursor.getString(cursor.getColumnIndex("BirthCertificateNumber"));
                }

                valid = !nid.trim().equals("")
                        || !birthCertificateNumber.trim().equals("");
            }


            cursor.close();
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }

        return valid;
    }

    public SavingsFriendly savingFriendlyLoanValidation(int memberId) {

        this.openReadableDatabase();
        SavingsFriendly savingsFriendly = new SavingsFriendly();
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT SUM(CASE WHEN P_Account.P_ProgramId = 135 THEN 1 ELSE 0 END) AS SavingFriendlyLoanCount, SUM(CASE WHEN P_Program.P_ProgramTypeId = 1 AND P_Program.Program_ID != 135 THEN 1 ELSE 0 END) AS PrimaryLoanCount, MAX(CASE WHEN P_Program.P_ProgramTypeId = 1 AND P_Program.Program_ID != 135 THEN P_Account.DisbursedAmount *.2  ELSE 0 END) AS PrimaryAmountWithdrawal FROM P_MemberView INNER JOIN P_Account ON P_Account.P_MemberId = P_MemberView.Id INNER JOIN P_Program ON P_Account.P_ProgramId = P_Program.Program_ID AND P_MemberView.Id = " + memberId + " GROUP BY P_MemberView.Id ", null);
            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                savingsFriendly.setSavingFriendlyLoanCount(cursor.getInt(cursor.getColumnIndex("SavingFriendlyLoanCount")));
                savingsFriendly.setPrimaryLoanCount(cursor.getInt(cursor.getColumnIndex("PrimaryLoanCount")));
                savingsFriendly.setMaxWithdrawal(cursor.getFloat(cursor.getColumnIndex("PrimaryAmountWithdrawal")));

            }


            cursor.close();
        } catch (SQLException e) {
            Log.i("Error", e.toString());
        }

        return savingsFriendly;
    }

    public NewMember getNewMember(int memberId) {
        Cursor cursor = databaseRead.rawQuery("SELECT * FROM P_MemberNew WHERE Id = " + memberId + "", null);
        cursor.moveToFirst();

        NewMember newMember = new NewMember();

        newMember.setGroupId(cursor.getInt(cursor.getColumnIndex("P_GroupId")));
        newMember.setMemberId(cursor.getInt(cursor.getColumnIndex("Id")));
        newMember.setProgramId(cursor.getInt(cursor.getColumnIndex("P_ProgramId")));
        newMember.setPassbookNumber(cursor.getInt(cursor.getColumnIndex("PassbookNumber")));
        newMember.setMemberNickName(cursor.getString(cursor.getColumnIndex("MemberNickName")));
        newMember.setSpouseNickName(cursor.getString(cursor.getColumnIndex("SpouseNickName")));
        newMember.setFatherNickName(cursor.getString(cursor.getColumnIndex("FatherNickName")));
        newMember.setHusband(cursor.getInt(cursor.getColumnIndex("IsHusband")) > 0);
        newMember.setMemberAge(cursor.getInt(cursor.getColumnIndex("MemberAge")));

        newMember.setDateOfBirth(cursor.getString(cursor.getColumnIndex("DateOfBirth")));
        newMember.setNationality(cursor.getInt(cursor.getColumnIndex("Nationality")));

        newMember.setStatus(cursor.getInt(cursor.getColumnIndex("Status")));

        newMember.setAdmissionDate(cursor.getString(cursor.getColumnIndex("AdmissionDate")));
        newMember.setNationalID(cursor.getString(cursor.getColumnIndex("NationalIdNumber")));
        newMember.setSex(cursor.getInt(cursor.getColumnIndex("Sex")));

        newMember.setPresentPermanentSame(cursor.getInt(cursor.getColumnIndex("PresentPermanentSame")) > 0);


        newMember.setMemberFullName(cursor.getColumnName(cursor.getColumnIndex("Name")));
        newMember.setFatherFullName(cursor.getString(cursor.getColumnIndex("FatherFullName")));
        newMember.setMotherName(cursor.getString(cursor.getColumnIndex("MotherName")));
        newMember.setEducationInfo(cursor.getString(cursor.getColumnIndex("EducationInfo")));
        newMember.setProfessionInfo(cursor.getString(cursor.getColumnIndex("ProfessionInfo")));
        newMember.setMemberAge(cursor.getInt(cursor.getColumnIndex("MemberAge")));

        newMember.setNationality(cursor.getInt(cursor.getColumnIndex("Nationality")));

        newMember.setReligionInfo(cursor.getInt(cursor.getColumnIndex("ReligionInfo")));
        newMember.setEthnicity(cursor.getInt(cursor.getColumnIndex("Ethnicity")));
        newMember.setMaritalStatus(cursor.getInt(cursor.getColumnIndex("MaritalStatus")));


        newMember.setSpouseFullName(cursor.getString(cursor.getColumnIndex("SpouseFullName")));
        newMember.setGuardianName(cursor.getString(cursor.getColumnIndex("GuardianName")));
        newMember.setGuardianRelation(cursor.getString(cursor.getColumnIndex("GuardianRelation")));
        newMember.setBirthCertificateNumber(cursor.getString(cursor.getColumnIndex("BirthCertificateNumber")));
        newMember.setResidenceType(cursor.getString(cursor.getColumnIndex("ResidenceType")));


        newMember.setLandLordName(cursor.getString(cursor.getColumnIndex("LandLordName")));
        newMember.setPermanentDistrictId(cursor.getInt(cursor.getColumnIndex("PermanentDistrictId")));
        newMember.setPermanentUpazila(cursor.getString(cursor.getColumnIndex("PermanentUpazila")));
        newMember.setPermanentUnion(cursor.getString(cursor.getColumnIndex("PermanentUnion")));
        newMember.setPermanentPostOffice(cursor.getString(cursor.getColumnIndex("PermanentPostOffice")));
        newMember.setPermanentVillage(cursor.getString(cursor.getColumnIndex("PermanentVillage")));
        newMember.setPermanentRoad(cursor.getString(cursor.getColumnIndex("PermanentRoad")));
        newMember.setPermanentHouse(cursor.getString(cursor.getColumnIndex("PermanentHouse")));
        newMember.setPermanentFixedProperty(cursor.getString(cursor.getColumnIndex("PermanentFixedProperty")));
        newMember.setPermanentIntroducerName(cursor.getString(cursor.getColumnIndex("PermanentIntroducerName")));
        newMember.setPermanentIntroducerDesignation(cursor.getString(cursor.getColumnIndex("PermanentIntroducerDesignation")));
        newMember.setPermanentPhone(cursor.getString(cursor.getColumnIndex("Phone")));

        newMember.setPresentDistrictId(cursor.getInt(cursor.getColumnIndex("PresentDistrictId")));
        newMember.setPresentUpazila(cursor.getString(cursor.getColumnIndex("PermanentUpazila")));
        newMember.setPresentUnion(cursor.getString(cursor.getColumnIndex("PresentUnion")));
        newMember.setPresentPostOffice(cursor.getString(cursor.getColumnIndex("PresentPostOffice")));
        newMember.setPresentVillage(cursor.getString(cursor.getColumnIndex("PresentVillage")));
        newMember.setPresentRoad(cursor.getString(cursor.getColumnIndex("PresentRoad")));
        newMember.setPresentHouse(cursor.getString(cursor.getColumnIndex("PresentHouse")));
        newMember.setPresentPhone(cursor.getString(cursor.getColumnIndex("PresentPhone")));


        newMember.setPresentPermanentSame(cursor.getInt(cursor.getColumnIndex("PresentPermanentSame")) > 0);
        newMember.setSavingDeposit(0);
        newMember.setCbsDeposit(0);


        newMember.setCbsInstallmentType(1);
        newMember.setCbsDeposit(0);

        Cursor cursorAccounts = databaseRead.rawQuery("SELECT * FROM P_Account WHERE P_MemberId = " + memberId + "", null);
        cursorAccounts.moveToFirst();
        while (!cursorAccounts.isAfterLast()) {

            if (cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_ProgramId")) > 200 && cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_ProgramId")) != 204 && cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_ProgramId")) < 300) {

                newMember.setSavingInstallmentType(cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_InstallmentType")));
                newMember.setSavingDeposit(cursorAccounts.getInt(cursorAccounts.getColumnIndex("MinimumDeposit")));
            } else if (cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_ProgramId")) > 300 && cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_ProgramId")) < 400) {
                newMember.setCbsInstallmentType(cursorAccounts.getInt(cursorAccounts.getColumnIndex("P_InstallmentType")));
                newMember.setCbsDeposit(cursorAccounts.getInt(cursorAccounts.getColumnIndex("MinimumDeposit")));
            }
            cursorAccounts.moveToNext();
        }

        cursor.close();
        cursorAccounts.close();
        return newMember;

    }

    public String getFirstAndLastWorkingDate() {
        String datesInFileName;
        String finalDay = getSevenWorkingDaysFromRealizable().get(0).split(" ")[0].trim();
        String firstRealDate = dataSourceOperationsCommon.getRealWorkingDay().split(" ")[0].trim();

        if (finalDay.equals(firstRealDate)) {
            datesInFileName = finalDay;
        } else {
            datesInFileName = firstRealDate + "_" + finalDay;
        }
        Log.i("Dates", datesInFileName);
        return datesInFileName;
    }

    public boolean isMemberMaxOut(int groupId) {

        this.openReadableDatabase();

        boolean memberMaxOut = false;

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_MemberView WHERE P_GroupId= " + groupId + "", null);

            cursor.moveToFirst();
            memberMaxOut = cursor.getCount() >= 35;

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return memberMaxOut;

    }


    private int serviceChargeInterest(int accountId) {
        this.openReadableDatabase();

        double currentBalance = 0;
        double previousBalance = 0;
        int monthValue = 0;
        float totalBalance = 0;
        int cursorValueCount = 0;

        try {
            Cursor cursorBalance = databaseRead.rawQuery(
                    "SELECT SUM(Credit-Debit) AS Balance, SUM(CASE WHEN Type > 0 THEN (Credit-Debit) ELSE  0 END) AS BalanceRemove  FROM P_AccountBalance WHERE P_AccountId= " + accountId + " ", null);

            cursorBalance.moveToFirst();

            currentBalance = cursorBalance.getInt(cursorBalance.getColumnIndex("Balance"))
                    - cursorBalance.getInt(cursorBalance.getColumnIndex("BalanceRemove"));
            previousBalance = currentBalance;

            cursorBalance.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_AccountDetails WHERE P_AccountId= " + accountId + " ORDER  BY  P_LoanTransactionDate DESC", null);
            cursor.moveToFirst();
            cursorValueCount = cursor.getCount();
            int runTotal = Integer.valueOf(new DateAndDataConversion().getDateFromInt(dataSourceOperationsCommon.getWorkingDay()).split("/")[1]);
            double[] previousBalanceArray = new double[12];
            Arrays.fill(previousBalanceArray, 0);
            double[] currentBalanceArray = new double[12];
            Arrays.fill(currentBalanceArray, 0);
            double[] balanceFound = new double[12];
            Arrays.fill(balanceFound, 0);

            int[] depositMonthFound = new int[12];
            Arrays.fill(depositMonthFound, 0);


            while (!cursor.isAfterLast()) {

                if (cursor.getInt(cursor.getColumnIndex("Type")) == 2048) {

                    if (cursorValueCount == 1) {
                        cursorValueCount = 0;
                    }
                    if (currentBalance != previousBalance && monthValue != runTotal) {
                        if (runTotal - 1 >= monthValue - 1) {
                            currentBalanceArray[monthValue - 1] = currentBalance;
                            previousBalanceArray[monthValue - 1] = previousBalance;
                            balanceFound[monthValue - 1] = 1;
                        }
                    }
                    break;
                } else {
                    if (cursor.getInt(cursor.getColumnIndex("Type")) == 1024
                            || cursor.getInt(cursor.getColumnIndex("Type")) == 16386 || cursor.getInt(cursor.getColumnIndex("Type")) == 1073741827) {
                        int value = Integer.valueOf(new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("P_LoanTransactionDate"))).split("/")[1]);
                        depositMonthFound[value - 1] = 1;
                        if (monthValue != value && monthValue != 0) {
                            if (monthValue != runTotal) {
                                currentBalanceArray[monthValue - 1] = currentBalance;
                                previousBalanceArray[monthValue - 1] = previousBalance;
                                balanceFound[monthValue - 1] = 1;
                            }
                            monthValue = value;
                            currentBalance = previousBalance;
                        } else {
                            monthValue = value;
                        }

                        if (cursor.getInt(cursor.getColumnIndex("Type")) == 1024) {
                            previousBalance -= cursor.getDouble(cursor.getColumnIndex("Amount"));
                        } else if (cursor.getInt(cursor.getColumnIndex("Type")) == 16386) {
                            previousBalance += cursor.getDouble(cursor.getColumnIndex("Amount"));
                        } else if (cursor.getInt(cursor.getColumnIndex("Type")) == 1073741827) {
                            previousBalance += cursor.getDouble(cursor.getColumnIndex("Amount"));
                        }

                    } else {
                        previousBalance = 0;
                    }
                }


                if (cursor.isLast()) {

                    if (currentBalance != previousBalance && monthValue != runTotal) {
                        if (runTotal - 1 >= monthValue - 1) {
                            currentBalanceArray[monthValue - 1] = currentBalance;
                            previousBalanceArray[monthValue - 1] = previousBalance;
                            balanceFound[monthValue - 1] = 1;

                            if (cursor.getCount() == 1) {
                                currentBalanceArray[monthValue - 1] = currentBalance;
                                previousBalanceArray[monthValue - 1] = currentBalance;
                                balanceFound[monthValue - 1] = 1;
                            }

                            if (monthValue - 2 > 0) {
                                if (currentBalanceArray[monthValue - 2] == 0 && previousBalanceArray[monthValue - 2] == 0 && cursor.getCount() == 1) {
                                    currentBalanceArray[monthValue - 2] = currentBalance;
                                    previousBalanceArray[monthValue - 2] = previousBalance;
                                    balanceFound[monthValue - 2] = 1;
                                }
                            }


                        }
                    }
                }

                cursor.moveToNext();
            }
            cursor.close();


            for (int i = 0; i < runTotal; i++) {
                if (depositMonthFound[i] == 0) {
                    for (int j = i; j >= 0; j--) {
                        if (currentBalanceArray[j] > 0 && currentBalanceArray[i] == 0 && previousBalanceArray[i] == 0) {
                            currentBalanceArray[i] = currentBalanceArray[j];
                            previousBalanceArray[i] = currentBalanceArray[j];
                            balanceFound[i] = 1;
                            depositMonthFound[i] = 1;
                        } else if (previousBalanceArray[j] > 0 && currentBalanceArray[i] == 0 && previousBalanceArray[i] == 0) {
                            currentBalanceArray[i] = previousBalanceArray[j];
                            previousBalanceArray[i] = previousBalanceArray[j];
                            balanceFound[i] = 1;
                            depositMonthFound[i] = 1;
                        }
                    }
                }
            }
            /*if(runTimes>0)
            {
                for(int i=0;i<runTotal-1;i++)
                {
                    if(balanceFound[i]==0)
                    {
                        int found = 0;
                        for(int j = i; j<runTotal-1; j++)
                        {
                            if(i!=j && balanceFound[j] == 1)
                            {
                                currentBalanceArray[i] = previousBalanceArray[j];
                                previousBalanceArray[i] = previousBalanceArray[j];
                                balanceFound[i] = 1;
                                found =1;
                                break;
                            }
                        }
                        if(found==0)
                        {
                            for(int j=runTotal-1;j>=i-1;j--)
                            {
                                if(balanceFound[j] == 1 && j!=i)
                                {
                                    currentBalanceArray[i] = currentBalanceArray[j];
                                    previousBalanceArray[i] = currentBalanceArray[j];
                                    balanceFound[i] = 1;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for(int i=0;i<runTotal-1;i++)
                {
                    currentBalanceArray[i] = balanceReserved;
                    previousBalanceArray[i] = balanceReserved;
                    balanceFound[i] = 1;
                }
            }*/
            for (int i = 0; i < 12; i++) {
                if (balanceFound[i] == 1) {
                    totalBalance += Math.round((previousBalanceArray[i] + currentBalanceArray[i]) / 2);
                }


            }

        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        if (cursorValueCount == 0) {
            return 0;
        } else {
            /*int v = (int) (Math.round(totalBalance)*(.005));
            double k = new BigDecimal(Math.round(totalBalance)*(.06/12) ).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();*/
            return Math.round(new BigDecimal(Math.round(totalBalance) * (.06 / 12)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }

    }

    private int missingLtsCount(int accountId, int openingDateInt, int receiveDate, int gracePeriod, int minimumDeposit) {

        this.openReadableDatabase();
        int workingDate = dataSourceOperationsCommon.getWorkingDay();
        int missingLtsCount = 0;

        try {
            /*Cursor cursorNew = databaseRead.rawQuery("SELECT COUNT(P_AccountId) As TotalMonth , " +
                    " Sum(Amount) AS TotalAmount ,MAX(P_Account.MinimumDeposit) AS Deposit, MAX(P_LoanTransactionDate) AS P_LoanTransactionDate, " +
                    " MAX(P_Account.OpeningDate) AS OpeningDate, MAX(P_Account.GracePeriod) AS MissingLtsPrevious , " +
                    "CASE WHEN P_MemberView.ReceiveDate IS NOT NULL AND P_MemberView.ReceiveDate> 736694 THEN P_MemberView.ReceiveDate ELSE 0 END AS ReceivedDate" +
                    " FROM P_AccountDetails " +
                    "INNER JOIN P_Account ON P_Account.Account_ID = P_AccountDetails.P_AccountId " +
                    "INNER JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId " +
                    " AND P_LoanTransactionDate > CASE WHEN P_MemberView.ReceiveDate IS NOT NULL AND P_MemberView.ReceiveDate> 736694 THEN P_MemberView.ReceiveDate-1 ELSE 736693 END " +
                    " AND Type = 1024 AND P_Account.P_ProgramId = 204 AND  P_AccountDetails.P_AccountId = "+accountId+"",null);*/

            /*Cursor cursorNew = databaseRead.rawQuery("SELECT COUNT(P_AccountId) As TotalMonth ,Sum(Amount) AS TotalAmount ," +
                    " MAX(CASE WHEN P_Account.Account_ID = " + accountId + " THEN P_Account.MinimumDeposit ELSE 0 END) AS Deposit, " +
                    " MAX( CASE WHEN P_LoanTransactionDate IS NULL AND P_Account.Account_ID  = " + accountId + " THEN CASE WHEN P_MemberView.ReceiveDate IS NOT NULL AND P_Account.Account_ID  = " + accountId + " THEN P_MemberView.ReceiveDate ELSE P_Account.OpeningDate END ELSE NULL END ) AS P_LoanTransactionDate,  MAX(P_Account.OpeningDate) AS OpeningDate," +
                    " MAX(CASE WHEN  P_Account.Account_ID  = " + accountId + " THEN P_Account.GracePeriod ELSE  0 END) AS MissingLtsPrevious , " +
                    " MAX(CASE WHEN P_MemberView.ReceiveDate IS NOT NULL AND P_Account.Account_ID  = " + accountId + " THEN P_MemberView.ReceiveDate ELSE 0 END)  AS  ReceivedDate  FROM   P_MemberView " +
                    " INNER JOIN P_Account ON P_MemberView.Id = P_Account.P_MemberId LEFT JOIN P_AccountDetails ON P_Account.Account_ID = P_AccountDetails.P_AccountId " +
                    " AND P_LoanTransactionDate > CASE WHEN P_MemberView.ReceiveDate IS NOT NULL AND P_MemberView.ReceiveDate> 736694 THEN P_MemberView.ReceiveDate ELSE 736693 END  AND Type = 1024 AND " +
                    " P_Account.P_ProgramId = 204 AND  P_Account.Account_ID  = " + accountId + "", null);*/

            Cursor cursorNew = databaseRead.rawQuery("SELECT Count(ID) AS TotalMonth ," +
                    "SUM(Amount) AS TotalAmount , MAX(P_AccountDetails.P_LoanTransactionDate) AS P_LoanTransactionDate" +
                    " FROM P_AccountDetails WHERE P_AccountDetails.P_AccountId = " + accountId + " AND P_AccountDetails.P_LoanTransactionDate > " +
                    "CASE WHEN " + receiveDate + " > 736694 THEN " + receiveDate + " ELSE 736693 END", null);




            /*cursor.moveToFirst();*/
            cursorNew.moveToFirst();
            int openingDate = openingDateInt;
            if (openingDate < 736693) {
                openingDate = 736694;
            }

            if (receiveDate > 736694) {
                openingDate = receiveDate;
            }
            DateAndDataConversion dateAndDataConversion = new DateAndDataConversion();

            missingLtsCount = new DateAndDataConversion().monthCountForLts(dateAndDataConversion.getDateValueFromInt(openingDate),
                    dateAndDataConversion.getDateValueFromInt(workingDate))
                    - (cursorNew.getInt(cursorNew.getColumnIndex("TotalAmount")) / minimumDeposit);


            if (receiveDate > 736694 && gracePeriod > 0) {
                missingLtsCount += gracePeriod + 1;

            }


            if (cursorNew.getInt(cursorNew.getColumnIndex(" ")) != 0) {
                String[] lastDate = dateAndDataConversion.getDateFromInt(cursorNew.getInt(cursorNew.getColumnIndex("P_LoanTransactionDate"))).split("/");
                String[] workDate = dateAndDataConversion.getDateFromInt(workingDate).split("/");

                if (!lastDate[1].trim().equals(workDate[1].trim()) || !lastDate[2].trim().equals(workDate[2].trim())) {
                    if (missingLtsCount - 1 < 0) {
                        missingLtsCount = 0;
                    } else {
                        missingLtsCount -= 1;
                    }

                }
            }
            cursorNew.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return missingLtsCount;

    }

    public boolean hasActiveLoanAccount(int memberId) {
        this.openReadableDatabase();
        boolean hasAccount = false;

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_Account  WHERE P_MemberId = " + memberId + " AND P_ProgramTypeId = 1", null);

            cursor.moveToFirst();
            hasAccount = cursor.getCount() > 0;

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return !hasAccount;
    }
    public double  activeLoanAccountDisburseAmount(int memberId) {
        this.openReadableDatabase();
        double  disburseAmount = 0;

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_Account INNER JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId AND P_Account.P_MemberId = " + memberId + " AND P_Program.IsPrimary = 1 AND P_Account.P_ProgramTypeId = 1", null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                if(cursor.getDouble(cursor.getColumnIndex("DisbursedAmount"))>disburseAmount)
                {
                    disburseAmount = cursor.getDouble(cursor.getColumnIndex("DisbursedAmount"));
                }
                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return disburseAmount;
    }


    public String getScheduleDate(int accountId) {
        this.openReadableDatabase();
        String scheduledDate = "";

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_Schedule  WHERE P_AccountId = " + accountId + " ", null);

            cursor.moveToFirst();

            try {
                if (cursor.getInt(cursor.getColumnIndex("NextDate")) > 0) {

                    scheduledDate = new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("NextDate")) + 1);
                }
            } catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }


            scheduledDate = new DateAndDataConversion().getDateFromInt(cursor.getInt(cursor.getColumnIndex("NextDate")) + 1);

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return scheduledDate;
    }

    private ArrayList<AccountDetails> getAccountDetailsFromCurrentMonthForWithdraw(int accountId) {

        this.openReadableDatabase();
        ArrayList<AccountDetails> accountDetailsArrayList = new ArrayList<>();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM P_AccountDetails  WHERE P_AccountId = "
                            + accountId + " AND Type = 16386 AND P_LoanTransactionDate != "+dataSourceOperationsCommon.getWorkingDay()+"  AND  P_LoanTransactionDate  BETWEEN "
                            + new DateAndDataConversion().getFirstDayOfTheMonth(dataSourceOperationsCommon.getWorkingDay())
                            +" AND "+new DateAndDataConversion().getLastDayOfTheMonth(dataSourceOperationsCommon.getWorkingDay())+"", null);


            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                AccountDetails accountDetails = new AccountDetails();
                accountDetails.setAccountId(cursor.getInt(cursor.getColumnIndex("P_AccountId")));
                accountDetails.setLoanTransactionDate(cursor.getInt(cursor.getColumnIndex("P_LoanTransactionDate")));
                accountDetails.setType(cursor.getInt(cursor.getColumnIndex("Type")));
                accountDetails.setAmount(cursor.getFloat(cursor.getColumnIndex("Amount")));
                accountDetails.setProcess(cursor.getInt(cursor.getColumnIndex("Process")));
                accountDetailsArrayList.add(accountDetails);

                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }
        return accountDetailsArrayList;
    }



    private boolean getAccountWithdrawalPermission(int accountId) {

        int workingDay = dataSourceOperationsCommon.getWorkingDay();
        boolean withdrawPermission = true;
        ArrayList<AccountDetails> accountDetailsArrayList = getAccountDetailsFromCurrentMonthForWithdraw(accountId);
        ArrayList<Long> betweenDatesList = new DateAndDataConversion().getFirstLastAndFriday(workingDay);

        if(accountDetailsArrayList.size()>=4)
        {
            withdrawPermission = false;
        }
        else
        {
            long startDate = 0;
            long endDate = 0;
            for (int i =0 ; i<betweenDatesList.size();i++)
            {
                if(betweenDatesList.get(i)>workingDay)
                {
                    endDate = betweenDatesList.get(i);
                    startDate = i>0 ? betweenDatesList.get(i-1):betweenDatesList.get(i);
                    break;
                }
            }

            for (AccountDetails ad:accountDetailsArrayList
            ) {
                if(ad.getLoanTransactionDate()>startDate && ad.getLoanTransactionDate()<endDate)
                {
                    withdrawPermission = false;
                    break;
                }
            }


        }

        return withdrawPermission;
    }


    public List<OverDueMember> getAllMembersForOverDue(int groupID) {
        this.openReadableDatabase();

        List<OverDueMember> overDueMemberList = new ArrayList<>();

        Cursor cursor;

        OverDueMember overDueMemberTotal = new OverDueMember();
        overDueMemberTotal.setMemberName("Total");
        overDueMemberTotal.setAccountOpeningDate("");
        double totalDisbursedAmount = 0;
        double totalOutStandingAmount = 0;
        double totalOverDueAmount = 0;
        OverDueMember overDueGroupTotal = new OverDueMember();

        double totalDisbursedAmountForGroup = 0;
        double totalOutStandingAmountForGroup = 0;
        double totalOverDueAmountForGroup = 0;
        int allGroup = 0;
        String groupName = "";


        if (groupID == -12345) {
            cursor = databaseRead.rawQuery("SELECT  T_Member.Id AS MemberId, T_Member.GroupId AS GroupId, T_Member.GroupName AS GroupName ," +
                    " T_Member.NewStatus AS OldOrNewMember,  T_Member.Name AS MemberName,T_Member.PassbookNumber AS PassbookNumber ," +
                    " T_Member.FatherOrHusbandName AS FatherOrHusbandName, T_Member.Sex AS Sex , T_OverDue.AccountId AS AccountId , T_OverDue.ScheduleDate AS ScheduleDate," +
                    " T_OverDue.ProgramName AS ProgramName, T_OverDue.IsSupplementary AS IsSupplementary ,T_OverDue.OpeningDate AS AccountOpeningDate ,T_OverDue.DisbursedAmount AS DisbursedAmount ," +
                    " T_OverDue.MaxOverdueAmount AS OverDueAmount ,T_OverDue.CreditBalance AS CreditBalance,T_OverDue.RealizedToday AS RealizedToday ,T_OverDue.RealizedBefore AS RealizedBefore, T_OverDue.BaseInstallmentAmount AS BaseInstallmentAmount , " +
                    " T_OverDue.OutstandingAmount AS OutstandingAmount, T_TermOverDueStatus.TermOverDue AS TermOverDue " +
                    "  FROM (SELECT  P_MemberView.Id,  P_MemberView.NewStatus, P_MemberView.Name ,  " +
                    "  P_MemberView.FatherOrHusbandName,  P_MemberView.PassbookNumber, P_MemberView.Sex , P_Group.Name AS GroupName , P_Group.ID AS GroupId " +
                    "  FROM P_MemberView INNER JOIN P_Group ON P_Group.ID = P_MemberView.P_GroupId   ORDER BY P_MemberView.P_GroupId,  PassbookNumber ) AS T_Member " +
                    " INNER JOIN " +
                    " ( Select MAX(P_Schedule.ScheduledDate) AS ScheduleDate,  P_Account.IsSupplementary AS IsSupplementary , MAX(CASE WHEN "+dataSourceOperationsCommon.getWorkingDay()+" == P_Schedule.ScheduledDate  THEN OverdueAmount-InstallmentAmount ELSE OverdueAmount END ) AS MaxOverdueAmount, SUM(P_AccountBalance.Credit) AS CreditBalance ," +
                    " SUM( CASE WHEN P_AccountBalance.Date =" + dataSourceOperationsCommon.getWorkingDay() + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedToday, SUM( CASE WHEN P_AccountBalance.Date <> " + dataSourceOperationsCommon.getWorkingDay() + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedBefore, MAX (BaseInstallmentAmount) AS BaseInstallmentAmount ,  " +
                    "   P_Account.P_MemberId AS MemberId ,  P_Account.Account_ID AS AccountId , P_Account.OpeningDate AS OpeningDate , (P_Account.DisbursedAmount + P_Account.ServiceChargeAmount)  AS DisbursedAmount , MAX(P_Schedule.OutstandingAmount)  AS OutstandingAmount , P_Program.Name as ProgramName " +
                    "   FROM P_Account INNER JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId INNER JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID INNER JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID  AND P_Account.P_ProgramId >100 AND P_Account.P_ProgramId <200" +
                    "  AND ( " + dataSourceOperationsCommon.getWorkingDay() + " BETWEEN ScheduledDate AND NextDate) GROUP BY P_Account.Account_ID) AS T_OverDue ON  T_OverDue.MemberId = T_Member.Id  AND T_OverDue.MaxOverdueAmount >0 "
                    + "LEFT JOIN "
                    + "( Select MAX(CASE WHEN  P_Schedule.OutstandingAmount> 0 AND  P_Schedule.OverdueAmount > 0 AND  P_Schedule.OverdueAmount =P_Schedule.OutstandingAmount THEN P_Schedule.OverdueAmount ELSE 0 END) AS TermOverDue , P_Account.P_MemberId AS MemberId FROM P_Account LEFT JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID "
                    + " AND (" + dataSourceOperationsCommon.getWorkingDay()
                    + " BETWEEN ScheduledDate AND NextDate) GROUP BY MemberId ) AS T_TermOverDueStatus ON T_TermOverDueStatus.MemberId = T_Member.Id ", null);
        } else {
            cursor = databaseRead.rawQuery("SELECT  T_Member.Id AS MemberId, T_Member.GroupId AS GroupId, T_Member.GroupName AS GroupName ," +
                    " T_Member.NewStatus AS OldOrNewMember,  T_Member.Name AS MemberName,T_Member.PassbookNumber AS PassbookNumber ," +
                    " T_Member.FatherOrHusbandName AS FatherOrHusbandName, T_Member.Sex AS Sex , T_OverDue.AccountId AS AccountId ,T_OverDue.ScheduleDate AS ScheduleDate," +
                    " T_OverDue.ProgramName AS ProgramName, T_OverDue.IsSupplementary AS IsSupplementary  ,T_OverDue.OpeningDate AS AccountOpeningDate ,T_OverDue.DisbursedAmount AS DisbursedAmount ," +
                    " T_OverDue.MaxOverdueAmount AS OverDueAmount, T_OverDue.CreditBalance AS CreditBalance ,T_OverDue.RealizedBefore AS RealizedBefore ,T_OverDue.RealizedToday AS RealizedToday , T_OverDue.BaseInstallmentAmount AS BaseInstallmentAmount , " +
                    " T_OverDue.OutstandingAmount AS OutstandingAmount, T_TermOverDueStatus.TermOverDue AS TermOverDue " +
                    "  FROM (SELECT  P_MemberView.Id,  P_MemberView.NewStatus, P_MemberView.Name ,  " +
                    "  P_MemberView.FatherOrHusbandName,  P_MemberView.PassbookNumber, P_MemberView.Sex , P_Group.Name AS GroupName , P_Group.ID AS GroupId " +
                    "  FROM P_MemberView INNER JOIN P_Group ON P_Group.ID = P_MemberView.P_GroupId  AND P_Group.ID = " + groupID + " ORDER BY PassbookNumber, GroupName ) AS T_Member " +
                    " INNER JOIN " +
                    " ( Select MAX(P_Schedule.ScheduledDate) AS ScheduleDate,  P_Account.IsSupplementary AS IsSupplementary , MAX(CASE WHEN "+dataSourceOperationsCommon.getWorkingDay()+" == P_Schedule.ScheduledDate  THEN OverdueAmount-InstallmentAmount ELSE OverdueAmount END ) AS MaxOverdueAmount, SUM(P_AccountBalance.Credit) AS CreditBalance ," +
                    " SUM( CASE WHEN P_AccountBalance.Date =" + dataSourceOperationsCommon.getWorkingDay() + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedToday, SUM( CASE WHEN P_AccountBalance.Date <> " + dataSourceOperationsCommon.getWorkingDay() + " THEN  P_AccountBalance.Credit ELSE 0 END ) AS RealizedBefore, MAX (BaseInstallmentAmount) AS BaseInstallmentAmount ,  " +
                    "   P_Account.P_MemberId AS MemberId ,  P_Account.Account_ID AS AccountId , P_Account.OpeningDate AS OpeningDate , (P_Account.DisbursedAmount + P_Account.ServiceChargeAmount)  AS DisbursedAmount , MAX(P_Schedule.OutstandingAmount)  AS OutstandingAmount , P_Program.Name as ProgramName " +
                    "   FROM P_Account INNER JOIN P_Program ON P_Program.Program_ID = P_Account.P_ProgramId INNER JOIN P_AccountBalance ON P_AccountBalance.P_AccountId = P_Account.Account_ID  INNER JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID  AND P_Account.P_ProgramId >100 AND P_Account.P_ProgramId <200" +
                    "  AND ( " + dataSourceOperationsCommon.getWorkingDay() + " BETWEEN ScheduledDate AND NextDate) GROUP BY P_Account.Account_ID) AS T_OverDue ON  T_OverDue.MemberId = T_Member.Id  AND T_OverDue.MaxOverdueAmount >0 "
                    + "LEFT JOIN "
                    + "( Select MAX(CASE WHEN  P_Schedule.OutstandingAmount> 0 AND  P_Schedule.OverdueAmount > 0 AND  P_Schedule.OverdueAmount =P_Schedule.OutstandingAmount THEN P_Schedule.OverdueAmount ELSE 0 END) AS TermOverDue , P_Account.P_MemberId AS MemberId FROM P_Account LEFT JOIN  P_Schedule ON P_Schedule.P_AccountId = P_Account.Account_ID "
                    + " AND (" + dataSourceOperationsCommon.getWorkingDay()
                    + " BETWEEN ScheduledDate AND NextDate) GROUP BY MemberId ) AS T_TermOverDueStatus ON T_TermOverDueStatus.MemberId = T_Member.Id ", null);
        }


        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            allGroup = cursor.getInt(cursor.getColumnIndex("GroupId"));
        }


        try {
            while (!cursor.isAfterLast()) {
                if (groupID == -12345 && allGroup != cursor.getInt(cursor.getColumnIndex("GroupId"))) {
                    overDueGroupTotal.setMemberName("Total (Group)");
                    overDueGroupTotal.setAccountOpeningDate("");
                    overDueGroupTotal.setGroupName(groupName);
                    overDueGroupTotal.setGroupId(-54321);
                    overDueGroupTotal.setDisbursedAmount(totalDisbursedAmountForGroup);
                    overDueGroupTotal.setOutstandingAmount(totalOutStandingAmountForGroup);
                    overDueGroupTotal.setOverDueAmount(totalOverDueAmountForGroup);
                    overDueMemberList.add(overDueGroupTotal);
                    allGroup = cursor.getInt(cursor.getColumnIndex("GroupId"));
                    overDueGroupTotal = new OverDueMember();

                    totalDisbursedAmountForGroup = 0;
                    totalOutStandingAmountForGroup = 0;
                    totalOverDueAmountForGroup = 0;


                }
                OverDueMember overDueMember = new OverDueMember();
                groupName = cursor.getString(cursor.getColumnIndex("GroupName"));

                overDueMember.setMemberId(cursor.getInt(cursor.getColumnIndex("MemberId")));
                overDueMember.setGroupId(cursor.getInt(cursor.getColumnIndex("GroupId")));
                overDueMember.setGroupName(cursor.getString(cursor.getColumnIndex("GroupName")));
                overDueMember.setOldOrNewMember(cursor.getString(cursor.getColumnIndex("OldOrNewMember")));
                overDueMember.setMemberName(cursor.getString(cursor.getColumnIndex("MemberName")) +
                        "/" + cursor.getString(cursor.getColumnIndex("FatherOrHusbandName")) +
                        " (" + cursor.getInt(cursor.getColumnIndex("PassbookNumber")) + ")");
                overDueMember.setSex(cursor.getInt(cursor.getColumnIndex("Sex")));
                overDueMember.setAccountId(cursor.getInt(cursor.getColumnIndex("AccountId")));
                overDueMember.setProgramName(cursor.getString(cursor.getColumnIndex("ProgramName")));
                overDueMember.setSupplementary(cursor.getInt(cursor.getColumnIndex("IsSupplementary"))>0);
                overDueMember.setAccountOpeningDate(new
                        DateAndDataConversion().getDateFromInt(
                        cursor.getInt(cursor.getColumnIndex("AccountOpeningDate"))));
                overDueMember.setDisbursedAmount(cursor.getDouble(cursor.getColumnIndex("DisbursedAmount")));
                overDueMember.setOverDueAmount(cursor.getDouble(cursor.getColumnIndex("OverDueAmount")));
                overDueMember.setOutstandingAmount(cursor.getDouble(cursor.getColumnIndex("OutstandingAmount")));
                //overDueMember.setOverDueAmount(cursor.getDouble(cursor.getColumnIndex("OverDueAmount"))-cursor.getInt(cursor.getColumnIndex("CreditBalance")));
                //overDueMember.setOutstandingAmount(cursor.getDouble(cursor.getColumnIndex("OutstandingAmount"))-cursor.getDouble(cursor.getColumnIndex("CreditBalance")));
                /*if (cursor.getInt(cursor.getColumnIndex("TermOverDue")) > 0) {

                    overDueMember.setOverDueAmount(cursor.getDouble(cursor.getColumnIndex("OverDueAmount")) + cursor.getDouble(cursor.getColumnIndex("BaseInstallmentAmount")));
                } else {

                }*/






                if(overDueMember.getOverDueAmount()>0)
                {
                    totalDisbursedAmount += overDueMember.getDisbursedAmount();
                    totalOutStandingAmount += overDueMember.getOutstandingAmount();
                    totalOverDueAmount += overDueMember.getOverDueAmount();

                    totalDisbursedAmountForGroup += overDueMember.getDisbursedAmount();
                    totalOutStandingAmountForGroup += overDueMember.getOutstandingAmount();
                    totalOverDueAmountForGroup += overDueMember.getOverDueAmount();

                    overDueMemberList.add(overDueMember);
                }




                if (cursor.isLast()) {


                    if (groupID == -12345) {
                        overDueGroupTotal.setMemberName("Total(Group)");
                        overDueGroupTotal.setGroupId(-54321);
                        overDueGroupTotal.setAccountOpeningDate("");
                        overDueGroupTotal.setGroupName(groupName);
                        overDueGroupTotal.setDisbursedAmount(totalDisbursedAmountForGroup);
                        overDueGroupTotal.setOutstandingAmount(totalOutStandingAmountForGroup);
                        overDueGroupTotal.setOverDueAmount(totalOverDueAmountForGroup);
                        overDueMemberList.add(overDueGroupTotal);

                        overDueMemberTotal.setMemberName("Grand Total");
                    } else {
                        overDueMemberTotal.setMemberName("Total");
                    }

                    overDueMemberTotal.setAccountOpeningDate("");
                    overDueMemberTotal.setGroupName("");
                    overDueMemberTotal.setGroupId(-12345);
                    overDueMemberTotal.setDisbursedAmount(totalDisbursedAmount);
                    overDueMemberTotal.setOutstandingAmount(totalOutStandingAmount);
                    overDueMemberTotal.setOverDueAmount(totalOverDueAmount);
                    overDueMemberList.add(overDueMemberTotal);


                }

                cursor.moveToNext();


            }

            cursor.close();

        } catch (Exception e) {
            Log.i("ERROR", e.getLocalizedMessage());
        }


        return overDueMemberList;
    }

    public boolean checkLoan(int memberId) {
        this.openReadableDatabase();
        boolean check = false;
        try {
            Cursor cursor = databaseRead.rawQuery("SELECT IsWithoutLoan FROM P_MemberView WHERE Id= '" + memberId + "'", null);
            cursor.moveToFirst();
            check = cursor.getInt(cursor.getColumnIndex("IsWithoutLoan")) > 0;
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

}
