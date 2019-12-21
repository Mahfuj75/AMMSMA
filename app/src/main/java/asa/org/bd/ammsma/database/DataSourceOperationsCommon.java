package asa.org.bd.ammsma.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataSourceOperationsCommon {

    private DatabaseHelper helper;
    private SQLiteDatabase databaseRead;

    public DataSourceOperationsCommon(Context context) {
        helper = new DatabaseHelper(context);
    }

    private void openReadableDatabase() {
        databaseRead = helper.getWritableDatabase();
    }

    public String getFirstRealDate() {
        String realDate = "";
        this.openReadableDatabase();

        try {
            Cursor res = databaseRead.rawQuery("SELECT RealDate ,Calender_Id  from Calender WHERE OpenORClose = 'Open' ORDER BY Calender_Id ASC", null);
            res.moveToFirst();
            realDate = res.getString(res.getColumnIndex("RealDate"));
            res.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return realDate;
    }

    int getCalenderFirstDay() {
        int workingDay = 1;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT Date from Calender ORDER BY Calender_Id ASC", null);
            cursor.moveToFirst();


            if (cursor.getCount() > 0) {
                workingDay = cursor.getInt(cursor.getColumnIndex("Date"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return workingDay;
    }


    public int getWorkingDay() {
        int workingDay = 1;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT Date from Calender WHERE OpenORClose = 'Open' ORDER BY Calender_Id ASC", null);
            cursor.moveToFirst();


            if (cursor.getCount() > 0) {
                workingDay = cursor.getInt(cursor.getColumnIndex("Date"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return workingDay;
    }

    String getRealWorkingDay() {

        String RealDate = "";
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT RealDate from Calender WHERE OpenORClose = 'Open' ORDER BY Calender_Id ASC", null);
            cursor.moveToFirst();


            if (cursor.getCount() == 0) {
                RealDate = "NOT FOUND";
            } else {
                RealDate = cursor.getString(cursor.getColumnIndex("RealDate"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return RealDate;
    }

    int getMeetingDayInWorkingDay() {
        int meetingDay = 0;
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT DayId FROM Calender WHERE OpenORClose = 'Open' ORDER BY Calender_Id ASC", null);
            cursor.moveToFirst();


            if (cursor.getCount() == 0) {
                meetingDay = 1;
            } else {
                meetingDay = cursor.getInt(cursor.getColumnIndex("DayId"));
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return meetingDay;
    }

    int getWorkingDayPosition(int workingDay) {

        int position = -12345;

        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * from Calender ", null);
            cursor.moveToFirst();


            while (!cursor.isAfterLast()) {
                if (cursor.getPosition() == 7) {
                    break;
                }

                if (cursor.getInt(cursor.getColumnIndex("Date")) == workingDay) {
                    position = cursor.getPosition();
                }
                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return position;
    }

    public List<Integer> getSevenWorkingDays() {
        List<Integer> allDate = new ArrayList<>();
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT Date, RealDate, Calender_Id from Calender  ORDER BY Calender_Id ASC", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {


                if (cursor.getPosition() == 7) {
                    break;
                }
                allDate.add(cursor.getInt(cursor.getColumnIndex("Date")));
                cursor.moveToNext();
            }

            if (cursor.getCount() == 0) {
                allDate.add(1);
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return allDate;
    }

    int getWorkingDayInfo() {
        int dayId = 7;
        int workingDay = getWorkingDay();
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT * FROM Calender WHERE Date = " + workingDay + " AND  OpenORClose = 'Open'   ORDER BY Calender_Id ASC", null);
            cursor.moveToFirst();

            dayId = cursor.getInt(cursor.getColumnIndex("DayId"));


            if (cursor.getCount() == 0) {
                dayId = 7;
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return dayId;
    }

    public String getRealShortDayName(int dayId) {
        String shortName = "None";
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT ShortName from Days WHERE DayID = " + dayId, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                shortName = cursor.getString(cursor.getColumnIndex("ShortName"));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return shortName;
    }

    public String getRealDayName(int dayId) {
        String dayName = "None";
        this.openReadableDatabase();

        try {
            Cursor cursor = databaseRead.rawQuery(
                    "SELECT Day from Days WHERE DayID = " + dayId, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                dayName = cursor.getString(cursor.getColumnIndex("Day"));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.i("Exception", e.getMessage());
        }

        return dayName;
    }

}
