package asa.org.bd.ammsma.dialog;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.database.DataSourceOperationsCommon;

public class DatePickerFragmentForMemberAge extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String currentDate = new DataSourceOperationsCommon(getContext()).getFirstRealDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(currentDate);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
        dialog.getDatePicker().setMaxDate(timeInMilliseconds - (31556952L * 18000));

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ((DatePickerDialog.OnDateSetListener) Objects.requireNonNull(getActivity())).onDateSet(view, year, month, dayOfMonth);
    }


}
