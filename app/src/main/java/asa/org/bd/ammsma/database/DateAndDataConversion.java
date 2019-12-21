package asa.org.bd.ammsma.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateAndDataConversion {

    public String getDateFromInt(int openingDate) {
        long diff = (openingDate - 693595);
        long totalDiff = (86400000 * diff);
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dfDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String dateText = null;
        try {
            Date d = dfDate.parse("01/01/1900");
            long initialDayLong = d.getTime();
            long last = totalDiff + initialDayLong;
            Date date = new Date(last);

            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            df2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            dateText = df2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateText;
    }

    Date getDateValueFromInt(int openingDate) {
        long diff = (openingDate - 693595);
        long totalDiff = (86400000 * diff);
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dfDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try {
            Date d = dfDate.parse("01/01/1900");
            long initialDayLong = d.getTime();
            long last = totalDiff + initialDayLong;
            Date date = new Date(last);

            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            df2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public long dateToLong(String date) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        dfDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date d = null;
        Date d1 = null;
        try {
            d = dfDate.parse("01/01/1900");
            d1 = dfDate.parse(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        assert d1 != null;
        long diff = d1.getTime() - d.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        diffDays = diffDays + 693595;
        return diffDays;
    }







    int dateFormationLongType(String date)
    {
        String finalDate;
        String[] strArr = date.split("T");
        finalDate = strArr[0];
        String[] dateTrick = finalDate.split("-");
        finalDate = dateTrick[1]+"/"+dateTrick[2]+"/"+dateTrick[0];
        final String OLD_FORMAT = "MM/dd/yyyy";
        final String NEW_FORMAT = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT,Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date d = null;
        try {
            d = sdf.parse(finalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        String newDateString = sdf.format(d);

        return (int) dateToLong(newDateString);
    }



    String deformationStringToString(String date)
    {
        String finalDate;
        String[] strArr = date.split("T");
        finalDate = strArr[0];
        String[] dateTrick = finalDate.split("-");
        finalDate = dateTrick[1]+"/"+dateTrick[2]+"/"+dateTrick[0];
        final String OLD_FORMAT = "MM/dd/yyyy";
        final String NEW_FORMAT = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT,Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date d = null;
        try {
            d = sdf.parse(finalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        return sdf.format(d);
    }

    ArrayList<Long> getFirstLastAndFriday(int dateValue){
        ArrayList<Long> fridayOfAMonthInt = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Calendar cal  = Calendar.getInstance();
        try {
            cal.setTime(df.parse(getDateFromInt(dateValue)));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            Calendar curCal = Calendar.getInstance();
            curCal.set(year, month , 1);
            int daysInMonth = curCal.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int day = 1; day <= daysInMonth; day++) {
                curCal.set(year, month, day);
                int dayOfWeek = curCal.get(Calendar.DAY_OF_WEEK);
                if(day ==1)
                {
                    fridayOfAMonthInt.add(getFirstDayOfTheMonth(dateValue));
                }
                else if (dayOfWeek == Calendar.FRIDAY) {
                    Date date = curCal.getTime();
                    fridayOfAMonthInt.add(dateToLong(df.format(date)));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean isExist = false;
        for (long dayExist:fridayOfAMonthInt) {
            if(dayExist==getLastDayOfTheMonth(dateValue))
            {
                isExist = true;
                break;
            }
        }
        if (!isExist)
        {
            fridayOfAMonthInt.add(getLastDayOfTheMonth(dateValue));
        }

        return fridayOfAMonthInt;
    }


    long  getLastDayOfTheMonth(int date) {
        String lastDayOfTheMonth = "";

        String dateString = getDateFromInt(date);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try{
            java.util.Date dt= formatter.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            java.util.Date lastDay = calendar.getTime();

            lastDayOfTheMonth = formatter.format(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToLong(lastDayOfTheMonth);
    }

    long  getFirstDayOfTheMonth(int date) {
        String firstDayOfTheMonth = "";

        String dateString = getDateFromInt(date);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try{
            java.util.Date dt= formatter.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            java.util.Date firstDayOfMonth = calendar.getTime();

            firstDayOfTheMonth = formatter.format(firstDayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToLong(firstDayOfTheMonth);
    }



    int monthCountForLts(Date startDate, Date endDate)
    {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH-1);

        if(dateDiff<0) {
            int borrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrow)-start.get(Calendar.DAY_OF_MONTH-1);
            monthsBetween--;

            if(dateDiff>0) {
                monthsBetween++;
            }
        }
        else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);
        monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;
        return monthsBetween;

    }
    long dayDifferenceBetweenTwoDays(Date startDate, Date endDate)
    {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        long diff =  end.getTimeInMillis()-start.getTimeInMillis();

        return diff / (24 * 60 * 60 * 1000);

    }






}
