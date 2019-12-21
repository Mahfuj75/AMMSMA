package asa.org.bd.ammsma.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import asa.org.bd.ammsma.R;

import static android.content.Context.MODE_PRIVATE;

public class DatabaseExportXML {
    private SQLiteDatabase mDb;
    private Exporter mExporter;
    private String xmlText = "";
    private int programOfficerId;
    private int count;

    public DatabaseExportXML(SQLiteDatabase db, int programOfficerId, Context context) {
        this.mDb = db;
        this.programOfficerId = programOfficerId;
        this.mExporter = new Exporter(context);
        this.count = 0;
    }

    public int count() {

        return count;
    }


    public String exportData() {

        mExporter.startDbExport();
        String sql = "SELECT * FROM sqlite_master";

        @SuppressLint("Recycle")
        Cursor cur = mDb.rawQuery(sql, new String[0]);
        cur.moveToFirst();

        String tableName;
        while (cur.getPosition() < cur.getCount()) {
            tableName = cur.getString(cur.getColumnIndex("name"));

            if (!tableName.equals("android_metadata")
                    && !tableName.equals("sqlite_sequence")) {

                if (tableName.equals("Branch")) {
                    exportTable(tableName);
                }
                if (tableName.equals("P_ProgramOfficer")) {
                    exportTable(tableName);
                }
                if (tableName.equals("P_AccountBalance")) {
                    exportTable(tableName);
                }
                if (tableName.equals("P_MemberNew")) {
                    exportTable(tableName);
                }
                if (tableName.equals("P_MemberView")) {
                    exportTable(tableName);
                }
                if (tableName.equals("P_Account")) {
                    exportTable(tableName);
                }
                if (tableName.equals("P_LoanTransaction")) {
                    exportTable(tableName);
                }
            }
            cur.moveToNext();
        }
        mExporter.endDbExport();
        return xmlText;
    }

    private void exportTable(String tableName) {
        String sql = null;
        String sqlForNewMember;
        String loanRecordQuarry;
        String activeLoanAccount;
        if (tableName.equals("Branch")) {
            sql = "select BranchID, Name, DistrictId from " + tableName;
        }
        if (tableName.equals("P_ProgramOfficer")) {
            sql = "select Code, Name, Designation, Id FROM " + tableName
                    + " WHERE Id = '" + programOfficerId + "'";
        }
        if (tableName.equals("P_AccountBalance")) {
            sql = "SELECT P_AccountBalance.P_AccountId, P_AccountBalance.Date, P_AccountBalance.Debit, P_AccountBalance.Credit, P_Account.P_ProgramId AS  programID, P_AccountBalance.CreateDateTime, P_AccountBalance.Status FROM P_MemberView INNER JOIN  P_Account ON P_MemberView.Id  = P_Account.P_MemberId  INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId AND P_AccountBalance.StatusLoan IS null  "
                    + "  AND P_MemberView.NewStatus = 'Old'   AND P_AccountBalance.ProgramOfficerID = " + programOfficerId + " AND P_Account.NewAccount = 0 AND P_AccountBalance.Flag >0  AND P_AccountBalance.Flag < 3 AND P_AccountBalance.Type >0  AND ( P_AccountBalance.Debit>=0 OR P_AccountBalance.Credit>=0 ) ";

            sqlForNewMember = "SELECT P_AccountBalance.P_AccountId, P_AccountBalance.Date, P_AccountBalance.Debit, P_AccountBalance.Credit, P_Account.P_ProgramId AS  programID, P_AccountBalance.CreateDateTime, P_AccountBalance.Status  FROM P_MemberView INNER JOIN  P_Account ON P_MemberView.Id  = P_Account.P_MemberId  INNER JOIN P_AccountBalance ON P_Account.Account_ID = P_AccountBalance.P_AccountId  AND P_AccountBalance.StatusLoan IS null "
                    + "   AND P_AccountBalance.ProgramOfficerID = " + programOfficerId + " AND P_Account.NewAccount = 1 AND P_AccountBalance.Flag >0   AND P_AccountBalance.Flag < 3 AND  P_AccountBalance.Type > 0 AND ( P_AccountBalance.Debit>=0 OR P_AccountBalance.Credit>=0 ) ";

            Cursor newMemberTransactionCur = mDb.rawQuery(sqlForNewMember,
                    new String[0]);

            int numCols2 = newMemberTransactionCur.getColumnCount();

            newMemberTransactionCur.moveToFirst();

            if (newMemberTransactionCur.getCount() > 0) {
                count++;
                String memberTableName = "P_Transaction" + "OfNewMember";
                mExporter.startTable(memberTableName);
                while (newMemberTransactionCur.getPosition() < newMemberTransactionCur
                        .getCount()) {
                    mExporter.startRow();
                    String name;
                    String val;
                    for (int idx = 0; idx < numCols2; idx++) {
                        name = newMemberTransactionCur.getColumnName(idx);
                        val = newMemberTransactionCur.getString(idx);
                        mExporter.addColumn(name, val);
                    }

                    mExporter.endRow();
                    newMemberTransactionCur.moveToNext();
                }
                newMemberTransactionCur.close();
                mExporter.endTable();
            }
        }
        if (tableName.equals("P_MemberNew")) {
            sql = "SELECT P_MemberNew.P_GroupId AS  P_GroupId, P_MemberNew.P_ProgramId AS P_ProgramId, P_MemberNew.PassbookNumber AS PassbookNumber, P_MemberNew.Name AS Name, P_MemberNew.FatherOrHusbandName AS FatherName, P_MemberNew.IsHusband AS IsHusband, P_MemberNew.DateOfBirth AS DateOfBirth, "
                    + " P_MemberNew.AdmissionDateString AS AdmissionDate, P_MemberNew.Sex AS Sex,P_MemberNew.Id AS Member_ID, P_MemberNew.SavingsDeposit AS SavingsDeposit, P_MemberNew.SecurityDeposit AS SecurityDeposit, CASE WHEN  P_MemberNew.Phone = '' OR P_MemberNew.Phone IS NULL THEN ' ' ELSE '+880'|| P_MemberNew.Phone END  AS PhoneNumber, CASE WHEN  P_MemberNew.NationalIdNumber = '' OR P_MemberNew.NationalIdNumber is NULL THEN ' ' ELSE  P_MemberNew.NationalIdNumber END   AS NationalIdNumber, "
                    + " P_MemberNew.Status, P_MemberNew.MemberNickName, P_MemberNew.MemberFullName, P_MemberNew.FatherFullName,  CASE WHEN  P_MemberNew.FatherNickName = '' OR P_MemberNew.FatherNickName IS NULL   THEN ' ' ELSE P_MemberNew.FatherNickName END AS FatherNickName ,P_MemberNew.MotherName, CASE WHEN  P_MemberNew.EducationInfo = '' OR P_MemberNew.EducationInfo IS NULL   THEN ' ' ELSE P_MemberNew.EducationInfo END AS EducationInfo , P_MemberNew.ProfessionInfo , "
                    + " P_MemberNew.MemberAge, P_MemberNew.Nationality,P_MemberNew.ReligionInfo, P_MemberNew.Ethnicity,P_MemberNew.MaritalStatus, P_MemberNew.SpouseFullName,  CASE WHEN  P_MemberNew.SpouseNickName = '' OR P_MemberNew.SpouseNickName IS NULL   THEN ' ' ELSE P_MemberNew.SpouseNickName END AS SpouseNickName, CASE WHEN  P_MemberNew.GuardianName = '' OR  P_MemberNew.GuardianName IS NULL  THEN ' ' ELSE P_MemberNew.GuardianName END AS GuardianName , "
                    + " CASE WHEN  P_MemberNew.GuardianRelation = '' OR P_MemberNew.GuardianRelation IS  NULL  THEN ' ' ELSE P_MemberNew.GuardianRelation END AS GuardianRelation,"
                    + " CASE WHEN  P_MemberNew.BirthCertificateNumber = '' OR P_MemberNew.BirthCertificateNumber IS NULL  THEN ' ' ELSE P_MemberNew.BirthCertificateNumber END AS BirthCertificateNumber , CASE WHEN  P_MemberNew.ResidenceType = '' OR P_MemberNew.ResidenceType IS NULL  THEN ' ' ELSE P_MemberNew.ResidenceType END AS ResidenceType ,"
                    + " CASE WHEN  P_MemberNew.LandLordName = '' OR P_MemberNew.LandLordName IS NULL  THEN ' ' ELSE P_MemberNew.LandLordName END AS LandLordName ,P_MemberNew.PresentPermanentSame , P_MemberNew.PermanentDistrictId, P_MemberNew.PermanentUpazila, P_MemberNew.PermanentUnion, P_MemberNew.PermanentPostOffice, P_MemberNew.PermanentVillage, CASE WHEN  P_MemberNew.PermanentRoad = '' OR P_MemberNew.PermanentRoad IS NULL  THEN ' ' ELSE P_MemberNew.PermanentRoad END AS PermanentRoad ,"
                    + " CASE WHEN  P_MemberNew.PermanentHouse = '' OR P_MemberNew.PermanentHouse IS NULL  THEN ' ' ELSE P_MemberNew.PermanentHouse END AS PermanentHouse ,  CASE WHEN  P_MemberNew.PermanentFixedProperty = '' OR P_MemberNew.PermanentFixedProperty IS NULL  THEN ' ' ELSE P_MemberNew.PermanentFixedProperty END AS PermanentFixedProperty ,"
                    + "  CASE WHEN  P_MemberNew.PermanentIntroducerName = '' OR P_MemberNew.PermanentIntroducerName IS NULL  THEN ' ' ELSE P_MemberNew.PermanentIntroducerName END AS PermanentIntroducerName , CASE WHEN  P_MemberNew.PermanentIntroducerDesignation = '' OR P_MemberNew.PermanentIntroducerDesignation IS NULL  THEN ' ' ELSE P_MemberNew.PermanentIntroducerDesignation END AS PermanentIntroducerDesignation ,"
                    + "  P_MemberNew.PresentDistrictId, P_MemberNew.PresentUpazila, P_MemberNew.PresentUnion, P_MemberNew.PresentPostOffice, P_MemberNew.PresentVillage, CASE WHEN  P_MemberNew.PresentRoad = '' OR P_MemberNew.PresentRoad IS NULL  THEN ' ' ELSE P_MemberNew.PresentRoad END AS PresentRoad , CASE WHEN  P_MemberNew.PresentHouse = '' OR P_MemberNew.PresentHouse IS NULL  THEN ' ' ELSE P_MemberNew.PresentHouse END AS PresentHouse , CASE WHEN  P_MemberNew.PresentPhone = '' OR P_MemberNew.PresentPhone IS NULL  THEN ' ' ELSE '+880'|| P_MemberNew.PresentPhone END AS PresentPhone FROM P_MemberNew INNER JOIN P_Group ON P_Group.ID = P_MemberNew.P_GroupId AND P_Group.P_ProgramOfficerId = " + programOfficerId + "";
        }

        if (tableName.equals("P_MemberView")) {
            sql = "SELECT P_MemberView.Id , P_MemberView.P_GroupId, P_MemberView.PassbookNumber,  CASE WHEN P_MemberView.Phone IS NULL THEN '' ELSE P_MemberView.Phone END AS UpdatePhone " +
                    " , CASE WHEN P_MemberView.NationalIdNumber IS NULL THEN '' ELSE P_MemberView.NationalIdNumber END AS UpdateNationalId " +
                    " FROM P_MemberView INNER JOIN P_Group ON P_Group.ID = P_MemberView.P_GroupId " +
                    " AND P_Group.P_ProgramOfficerId = " + programOfficerId + " AND (UpdatePhone = 1 OR UpdateNid =1)";
        }

        if (tableName.equals("P_Account")) {
            sql = "SELECT P_Account.Account_ID AS Account_ID , P_Account.P_MemberId AS P_MemberId, P_Account.P_ProgramId AS P_ProgramId, P_Account.P_ProgramTypeId AS P_ProgramTypeId, P_Account.OpeningDate AS OpeningDate,"
                    + " P_Account.P_Duration AS P_Duration , P_Account.MeetingDayOfWeek AS MeetingDayOfWeek, P_Account.MeetingDayOfMonth AS MeetingDayOfMonth, P_Account.MemberSex AS MemberSex,"
                    + " P_Account.MinimumDeposit AS MinimumDeposit, CASE WHEN P_MemberView.NewStatus = 'New' THEN 'NewMember' ELSE 'OldMember' END AS StatusAccount, P_Account.P_InstallmentType AS InstallmentType  FROM "
                    + tableName
                    + " INNER JOIN P_MemberView ON P_Account.P_MemberId = P_MemberView.Id AND P_Account.NewLoan =0  AND   P_Account.Flag > 0 AND P_Account.ProgramOfficerID = " + programOfficerId + "";


            loanRecordQuarry = "SELECT  P_Account.P_MemberId AS P_MemberID, P_Account.P_ProgramId AS ProgramID, P_Account.P_Duration AS  Duration, P_Account.P_InstallmentType AS InstallmentTypeID, P_Account.OpeningDate AS DisbursedDate,"
                    + " P_Account.DisbursedAmount AS PrincipalAmount, P_Account.P_FundId  AS FundTypeID , P_Account.P_SchemeId  AS SchemeID, P_Account.LoanInsurance AS  LoanInsurance,  P_Account.ProgramOfficerID AS IntentID, "
                    + "  P_Account.GracePeriod AS GracePeriod  FROM "
                    + tableName
                    + " INNER JOIN P_MemberView ON P_Account.P_MemberId = P_MemberView.Id AND P_Account.NewLoan > 0 AND    P_Account.Flag > 0 AND P_Account.ProgramOfficerID =" + programOfficerId + "";

            Cursor loanRecord = mDb.rawQuery(loanRecordQuarry,
                    new String[0]);

            int numCols2 = loanRecord.getColumnCount();

            loanRecord.moveToFirst();

            if (loanRecord.getCount() > 0) {
                count++;
                String memberTableName = "P_LoanRecord";
                mExporter.startTable(memberTableName);
                while (loanRecord.getPosition() < loanRecord
                        .getCount()) {
                    mExporter.startRow();
                    String name;
                    String val;
                    for (int idx = 0; idx < numCols2; idx++) {
                        name = loanRecord.getColumnName(idx);
                        val = loanRecord.getString(idx);
                        mExporter.addColumn(name, val);
                    }

                    mExporter.endRow();
                    loanRecord.moveToNext();
                }
                loanRecord.close();
                mExporter.endTable();
            }
            activeLoanAccount = "SELECT P_Account.Account_ID , P_Account.P_MemberId FROM P_Account WHERE  P_Account.P_ProgramTypeId = 1 AND P_Account.NewLoan =0 AND P_Account.NewAccount = 0";

            Cursor activeLoanAccountCursor = mDb.rawQuery(activeLoanAccount,
                    null);

            int numCols3 = activeLoanAccountCursor.getColumnCount();

            activeLoanAccountCursor.moveToFirst();

            if (activeLoanAccountCursor.getCount() > 0) {
                count++;
                String memberTableName = "ActiveLoanAccount";
                mExporter.startTable(memberTableName);
                while (activeLoanAccountCursor.getPosition() < activeLoanAccountCursor
                        .getCount()) {
                    mExporter.startRow();
                    String name;
                    String val;
                    for (int idx = 0; idx < numCols3; idx++) {
                        name = activeLoanAccountCursor.getColumnName(idx);
                        val = activeLoanAccountCursor.getString(idx);
                        mExporter.addColumn(name, val);
                    }

                    mExporter.endRow();
                    activeLoanAccountCursor.moveToNext();
                }
                activeLoanAccountCursor.close();
                mExporter.endTable();
            }

        }
        if (tableName.equals("P_LoanTransaction")) {
            sql = "SELECT  P_LoanTransaction.P_AccountId AS P_AccountId, P_Account.P_ProgramId  AS  P_ProgramID ,P_LoanTransaction.Date AS Date , P_LoanTransaction.Debit AS Debit from " + tableName
                    + " INNER JOIN P_Account ON P_Account.Account_ID = P_LoanTransaction.P_AccountId INNER JOIN P_MemberView ON P_MemberView.Id = P_Account.P_MemberId  "
                    + " INNER JOIN P_Group ON P_Group.ID = P_MemberView.P_GroupId   AND P_LoanTransaction.Status > 0 AND P_Group.P_ProgramOfficerId = "
                    + programOfficerId + "";
        }
		/*if (tableName.equals("P_LoanRecord")) {
			sql = "select * from " + tableName +" WHERE IntentID = '"+programOfficerId+"'";
		}*/

        Cursor cur = mDb.rawQuery(sql, new String[0]);
        int nubColumns = cur.getColumnCount();
        cur.moveToFirst();

        if (cur.getCount() > 0) {

            switch (tableName) {
                case "Branch":
                    mExporter.startTable("Branch");
                    break;
                case "P_AccountBalance":
                    count++;
                    mExporter.startTable("P_Transaction");

                    break;
                case "P_MemberNew":
                    count++;
                    mExporter.startTable("P_Member");
                    break;
                case "P_MemberView":
                    count++;
                    mExporter.startTable("P_MemberUpdate");
                    break;
                case "P_LoanTransaction":
                    count++;
                    mExporter.startTable("P_Exemption");
                    break;
                default:
                    if (tableName.trim().equals("P_Account")) {
                        count++;
                    }

                    mExporter.startTable(tableName);
                    break;
            }


            while (cur.getPosition() < cur.getCount()) {
                mExporter.startRow();
                String name;
                String val;
                for (int idx = 0; idx < nubColumns; idx++) {
                    name = cur.getColumnName(idx);
                    val = cur.getString(idx);
                    mExporter.addColumn(name, val);
                }
                mExporter.endRow();
                cur.moveToNext();
            }
            mExporter.endTable();
        }
        cur.close();
    }

    class Exporter {

        private String tableNameMain;


        private Context context;

        Exporter(Context context) {
            this.context = context;
        }


        void setVersion() {
            String version = "<version_type>General_Availability</version_type><version_code>" + context.getString(R.string.version_value) + "</version_code>";
            xmlText += version;
        }

        void setLogData() {
            SharedPreferences sharedPreferences = this.context.getSharedPreferences(this.context.getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);
            tableNameMain = "Log";
            startTable(tableNameMain);
            startRow();
            addColumn("First_Installation", sharedPreferences.getString(this.context.getResources().getString(R.string.FirstInstallation), " "));
            for (int i = 1; i <= 7; i++) {
                boolean conditionImport = sharedPreferences.getString(this.context.getResources().getString(R.string.ImportDate) + "_" + i, "").trim().equals("") &&
                        sharedPreferences.getString(this.context.getResources().getString(R.string.ImportFileName) + "_" + i, "").trim().equals("") &&
                        sharedPreferences.getString(this.context.getResources().getString(R.string.ImportSuccessTag) + "_" + i, "").trim().equals("");

                if (!conditionImport) {

                    xmlText = String.format("%s%s", xmlText, "<" + tableNameMain + "DataImport_" + i + ">");
                    addColumn("Import_Date", sharedPreferences.getString(this.context.getResources().getString(R.string.ImportDate) + "_" + i, " "));
                    addColumn("Import_File_Name", sharedPreferences.getString(this.context.getResources().getString(R.string.ImportFileName) + "_" + i, " "));
                    addColumn("Import_Success_Tag", sharedPreferences.getString(this.context.getResources().getString(R.string.ImportSuccessTag) + "_" + i, " "));
                    xmlText = String.format("%s%s", xmlText, "</" + tableNameMain + "DataImport_" + i + ">");
                }


            }

            for (int i = 1; i <= 7; i++) {
                boolean conditionExport = sharedPreferences.getString(this.context.getResources().getString(R.string.ExportDate) + "_" + i, "").trim().equals("") &&
                        sharedPreferences.getString(this.context.getResources().getString(R.string.ExportFileName) + "_" + i, "").trim().equals("") &&
                        sharedPreferences.getString(this.context.getResources().getString(R.string.ExportSuccessTag) + "_" + i, "").trim().equals("");

                if (!conditionExport) {
                    xmlText = String.format("%s%s", xmlText, "<" + tableNameMain + "DataExport_" + i + ">");
                    addColumn("Export_Date", sharedPreferences.getString(this.context.getResources().getString(R.string.ExportDate) + "_" + i, " "));
                    addColumn("Export_File_Name", sharedPreferences.getString(this.context.getResources().getString(R.string.ExportFileName) + "_" + i, " "));
                    addColumn("Export_Success_Tag", sharedPreferences.getString(this.context.getResources().getString(R.string.ExportSuccessTag) + "_" + i, " "));
                    xmlText = String.format("%s%s", xmlText, "</" + tableNameMain + "DataExport_" + i + ">");
                }


            }

            endRow();

            endTable();
        }

        void startDbExport() {
            String stg = "<" + "DataFromTab" + ">";
            xmlText += stg;
            setVersion();
            setLogData();

        }

        void endDbExport() {
            String stg = "</" + "DataFromTab" + ">";
            xmlText += stg;
        }

        void startTable(String tableName) {
            String stg = "<" + tableName + ">";
            xmlText += stg;
            tableNameMain = tableName;
        }

        void endTable() {
            String stg = "</" + tableNameMain + ">";
            xmlText += stg;
        }

        void startRow() {
            String stg = "<" + tableNameMain + "Data" + ">";
            xmlText += stg;
        }

        void endRow() {
            String stg = "</" + tableNameMain + "Data" + ">";
            xmlText += stg;
        }

        void addColumn(String name, String val) {
            String stg = "<" + name + ">" + val + "</" + name + ">";
            xmlText += stg;

        }
    }

}
