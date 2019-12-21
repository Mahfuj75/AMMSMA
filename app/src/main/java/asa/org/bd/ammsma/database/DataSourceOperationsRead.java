package asa.org.bd.ammsma.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


class DataSourceOperationsRead {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase databaseRead;
    private DataSourceOperationsCommon dataSourceOperationsCommon;

    DataSourceOperationsRead(Context context) {
        databaseHelper = new DatabaseHelper(context);
        dataSourceOperationsCommon = new DataSourceOperationsCommon(context);
    }

    private void openReadableDatabase() {
        databaseRead = databaseHelper.getWritableDatabase();
    }

    int getMemberCount(int groupIdCheck) {
        this.openReadableDatabase();

        int count = 0;

        try {
            Cursor res = databaseRead.rawQuery("SELECT * FROM P_MemberView WHERE P_GroupId = '"
                    + groupIdCheck + "'", null);

            res.moveToFirst();
            if (res.getCount() > 0) {
                count = res.getCount();
            }
            res.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return count;
    }

    boolean getGroupInfoFromSavedData(String groupName) {

        this.openReadableDatabase();

        boolean flag = false;
        int date = dataSourceOperationsCommon.getWorkingDay();
        try {
            Cursor res = databaseRead.rawQuery("SELECT * FROM TempRealizedGroup WHERE GroupName = '" + groupName + "' AND WorkingDay = '" + date + "'", null);

            res.moveToFirst();
            if (res.getCount() > 0) {
                flag = true;
            }
            res.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return flag;
    }

}
