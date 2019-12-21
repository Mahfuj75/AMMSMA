package asa.org.bd.ammsma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import asa.org.bd.ammsma.R;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AMMSMADBSYSTEMBD.db";
    private int flag = 0;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, Integer.parseInt(context.getResources().getString(R.string.database_version)));

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (flag == 0) {
            createDBTables(db);
            insertPrimaryData(db);
            flag++;
        }
    }


    private void createDBTables(SQLiteDatabase db) {
        CreateTableScripts scripts = new CreateTableScripts();


        String sqlTransactionCreation = scripts.createTransaction();
        String sqlTransactionHistory = scripts.createTransactionHistory();
        String sqlLoan = scripts.createLoanRecord();
        String sqlInsurance = scripts.createTempInsurance();
        String sqlAdminCreation = scripts.createAdmin();
        String sqlDaysCreation = scripts.createDays();
        String createInstallmentType = scripts.createInstallmentType();
        String sqlGroupType = scripts.createGroupType();
        String sqlType = scripts.createType();
        String sqlProcess = scripts.createProcess();
        String sqlScheme = scripts.createScheme();
        String sqlFund = scripts.createFund();
        String sqlBranchCreation = scripts.createBranch();
        String sqlProgramOfficerCreation = scripts.createProgramOfficer();
        String sqlAccountBalanceCreation = scripts.createAccountBalance();
        String sqlNewMemberCreation = scripts.createMemberNew();
        String sqlAccountCreation = scripts.createAccount();
        String sqlLoanTransactionCreation = scripts.createLoanTransaction();
        String sqlAccountDetailsCreation = scripts.createAccountDetails();
        String sqlCalenderCreation = scripts.createCalender();
        String sqlDurationCreation = scripts.createDuration();
        String sqlGracePeriodCreation = scripts.createGracePeriod();
        String sqlTableCreation = scripts.createGroup();
        String sqlInstallmentAmountCreation = scripts.createInstallmentAmount();
        String sqlInstallmentCountCreation = scripts.createInstallmentCount();
        String sqlLoanGroupCreation = scripts.createLoanGroup();
        String sqlLoanGroupDurationCreation = scripts.createLoanGroupDuration();
        String sqlLoanGroupInstallmentCreation = scripts.createLoanGroupInstallment();
        String sqlMemberViewCreation = scripts.createMemberView();
        String sqlOtherFeeCreation = scripts.createOtherFee();
        String sqlProgram = scripts.createProgram();
        String sqlProgramGroupType = scripts.createProgramGroupType();
        String sqlScheduleCreation = scripts.createSchedule();
        String sqlServiceChargeCreation = scripts.createServiceCharge();
        String sqlUserCreation = scripts.createUser();
        String sqlTempRealizedGroupCreation = scripts.createTempRealizedGroup();
        String sqlProgramNameChange = scripts.createProgramNameChange();
        String insertProgramNameChange = scripts.insertProgramNameChange();
        try {
            db.execSQL(sqlTransactionHistory);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlLoan);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlTransactionCreation);
            db.execSQL("CREATE INDEX indexname3 ON P_Transaction(P_AccountId);");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlInsurance);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ///////////////FIXED TABLE START///////////////////


        try {
            db.execSQL(sqlBranchCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlProgramOfficerCreation);
            db.execSQL("CREATE INDEX indextype1 ON P_ProgramOfficer(Id);");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlAccountBalanceCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlNewMemberCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlAccountCreation);
            db.execSQL("CREATE INDEX indexname2 ON P_Account(Account_ID);");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlLoanTransactionCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlAdminCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlDaysCreation);
            db.execSQL("CREATE INDEX indexname ON Days(DayID);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(createInstallmentType);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlGroupType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlType);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlProcess);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlScheme);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlFund);
            Log.v("sqlFund: ", "created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlTempRealizedGroupCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlAccountDetailsCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlCalenderCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlDurationCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlGracePeriodCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlTableCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlInstallmentAmountCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlInstallmentCountCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlLoanGroupCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlLoanGroupDurationCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlLoanGroupInstallmentCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlMemberViewCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlOtherFeeCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(sqlProgram);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlProgramGroupType);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlScheduleCreation);
            db.execSQL("CREATE INDEX indexname4 ON P_Schedule(P_AccountId);");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlServiceChargeCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(sqlUserCreation);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(sqlProgramNameChange);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(insertProgramNameChange);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        flag++;
    }

    private void insertPrimaryData(SQLiteDatabase db) {

        String[] adminLoginData = {"Admin", "Admin123"};

        String[] dayName = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "None"};
        String[] dayShortName = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT", "None"};
        String[] dayId = {"0", "1", "2", "3", "4", "5", "6", "7"};

        int[] installmentTypeId = {-1, 1, 2, 3, 4, 5, 6};
        String[] installmentTypeName = {"ALL", "WEEKLY", "MONTHLY", "FORTNIGHTLY", "SHORT TERM", "THREE MONTHLY", "SIX MONTHLY"};

        String[] groupType = {"GENERAL", "SMALL BUSINESS MONTHLY", "SEL", "BAD DEBT"};

        long[] typeId = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16386, 32768, 65536, 1073741827, 1073741828, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741825, 1073741826, 1073741829};
        String[] typeName = {"LOAN_DISBURSED", "LOAN_SERVICE_CHARGE", "LOAN_COLLECTION", "LOAN_TRANSFER", "PAYMENT_LSRF", "ADJUST_WITH_SAVINGS", "ADJUST_WITH_CBS", "TRANSFER_TO_BAD_DEBT", "BAD_DEBT_COLLECTION", "BAD_DEBT_TRANSFER", "SAVINGS_DEPOSIT",
                "SAVINGS_INTEREST", "SAVINGS_LATE_FEE", "SAVINGS_ADJUST_WITH_LOAN", "SAVINGS_WITHDRAWAL", "SAVINGS_RETURN", "SAVINGS_LAPSED", "LOAN_EXCESS_SERVICE_CHARGE", "ALLOWANCE_ON_DEATH", "CBS_DEPOSIT", "INTEREST_PAYMENT_ON_DEATH_CBSF",
                "CBS_RESOLVED", "CBS_WITHDRAWAL", "CBS_INTEREST", "CBS_RETURN", "CBS_ADJUST_WITH_LOAN", "CBS_LAPSED", "ADMISSION_FEE", "PASS_BOOK", "LOAN_SECURITY_AND_RISK_FUND", "APPRAISAL_FEE", "LOAN_LATE_FEE", "LOAN_SERVICE_CHARGE_EXEMPTION", "LOAN_PROCESSING_FEE", "HONORARIUM_FOR_RETIRED_MEMBER_FEE"};

        Log.i("TypeIDNameSize", typeId.length + " / " + typeName.length);
        int[] processId = {1, 2, 4, 8};
        String[] processName = {"CASH", "CHEQUE", "ADJUST", "TRANSFER"};
        ContentValues adminData = new ContentValues();
        adminData.put("Name", adminLoginData[0]);
        adminData.put("Pass", adminLoginData[1]);
        db.insert("Admin", null, adminData);

        for (int i = 0; i < dayName.length; i++) {
            ContentValues daysData = new ContentValues();
            daysData.put("Day", dayName[i]);
            daysData.put("ShortName", dayShortName[i]);
            daysData.put("DayID", dayId[i]);
            db.insert("Days", null, daysData);
        }

        for (int i = 0; i < installmentTypeName.length; i++) {
            ContentValues installmentTypeData = new ContentValues();
            installmentTypeData.put("Installment_Type", installmentTypeId[i]);
            installmentTypeData.put("Name", installmentTypeName[i]);
            db.insert("P_InstallmentType", null, installmentTypeData);
        }

        for (String aGroupType : groupType) {
            ContentValues groupTypeValues = new ContentValues();
            groupTypeValues.put("Name", aGroupType);
            db.insert("P_GroupType", null, groupTypeValues);
        }

        for (int i = 0; i < typeName.length; i++) {
            ContentValues typeValues = new ContentValues();
            typeValues.put("Type", typeId[i]);
            typeValues.put("TypeName", typeName[i]);
            db.insert("P_Type", null, typeValues);
        }

        for (int i = 0; i < processName.length; i++) {
            ContentValues processValues = new ContentValues();
            processValues.put("Process", processId[i]);
            processValues.put("ProcessName", processName[i]);
            db.insert("P_Process", null, processValues);
        }

        flag++;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Login");
        db.execSQL("DROP TABLE IF EXISTS Days");
        db.execSQL("DROP TABLE IF EXISTS P_InstallmentType");
        db.execSQL("DROP TABLE IF EXISTS P_GroupType");
        db.execSQL("DROP TABLE IF EXISTS P_Type");
        db.execSQL("DROP TABLE IF EXISTS P_Process");

        if (newVersion == 2 && oldVersion == 1) {
            db.execSQL("ALTER TABLE P_MemberView ADD COLUMN UpdateNid INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE P_MemberView ADD COLUMN UpdatePhone INTEGER DEFAULT 0");
        }

        if (newVersion == 3 && oldVersion == 2) {
            db.execSQL("DROP TABLE IF EXISTS P_MemberNew");
            db.execSQL("ALTER TABLE P_MemberView ADD COLUMN BirthCertificateNumber VARCHAR(50) DEFAULT '' ");
        }

        if (newVersion == 4 && oldVersion == 3) {
            db.execSQL("ALTER TABLE P_MemberView ADD COLUMN IsWithoutLoan BOOLEAN NOT NULL DEFAULT 0 ");
        }

        onCreate(db);
    }

}
