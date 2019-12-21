package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;


public class AddLtsAccountCustomAdapterForListView extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;
    private DataChangeListener listener;


    public interface DataChangeListener {
        void onDataChange(int position);
    }

    public AddLtsAccountCustomAdapterForListView(Context context, ArrayList<AccountForDailyTransaction> memberDetailsInfoList, DataChangeListener listener) {
        super(context, R.layout.list_item_for_lts_add_lts, memberDetailsInfoList);
        this.context = context;
        this.listener = listener;
        new DataSourceWrite(context);

    }


    private static class ViewHolder {
        TextView textViewMain;
        TextView textViewFirstItem;
        TextView textViewSecondItem;
        TextView textViewThirdItem;
        Button buttonLtsDelete;

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_item_for_lts_add_lts, null);

            viewHolder.textViewMain = rowView.findViewById(R.id.textViewMain);
            viewHolder.textViewFirstItem = rowView.findViewById(R.id.textViewFirstItem);
            viewHolder.textViewSecondItem = rowView.findViewById(R.id.textViewSecondItem);
            viewHolder.textViewThirdItem = rowView.findViewById(R.id.textViewThirdItem);
            viewHolder.buttonLtsDelete = rowView.findViewById(R.id.buttonLtsDelete);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        final AccountForDailyTransaction accountForDailyTransaction = getItem(position);

        assert accountForDailyTransaction != null;


        SpannableStringBuilder titleTextSpan = new SpannableStringBuilder("LTS : " + (position + 1));
        titleTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#004D40")), 0, titleTextSpan.length(), 0);
        titleTextSpan.append("   ( ");
        SpannableStringBuilder openingTextSpan = new SpannableStringBuilder("Opening Date :  ");
        openingTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1A237E")), 0, openingTextSpan.length(), 0);
        openingTextSpan.append(accountForDailyTransaction.getOpeningDateValue()).append(" )");
        titleTextSpan.append(openingTextSpan);


        viewHolder.textViewMain.setText(titleTextSpan, TextView.BufferType.SPANNABLE);
        if (accountForDailyTransaction.getFlag() == 0) {
            viewHolder.buttonLtsDelete.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.buttonLtsDelete.setVisibility(View.VISIBLE);
            final View finalRowView = rowView;
            viewHolder.buttonLtsDelete.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(finalRowView.getRootView().getContext());

                builder.setMessage(
                        "Are you sure you want to delete this LTS account ?")
                        .setCancelable(false)
                        .setTitle("Delete")
                        .setPositiveButton("Yes",
                                (dialog, id) -> {
                                    Toast.makeText(context.getApplicationContext(), "LTS Account Deleted Successfully", Toast.LENGTH_LONG).show();
                                    if (listener != null) {
                                        listener.onDataChange(position);
                                        finalRowView.setVisibility(View.GONE);
                                    }
                                })
                        .setNegativeButton("No",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();


            });
        }


        SpannableStringBuilder balanceTextSpan = new SpannableStringBuilder("Balance :  ");
        balanceTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1A237E")), 0, balanceTextSpan.length(), 0);
        balanceTextSpan.append(String.valueOf(Math.round(accountForDailyTransaction.getBalance())));
        SpannableStringBuilder minimumDepositTextSpan = new SpannableStringBuilder("  Minimum Deposit :  ");
        minimumDepositTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1A237E")), 0, minimumDepositTextSpan.length(), 0);
        minimumDepositTextSpan.append(String.valueOf(Math.round(accountForDailyTransaction.getMinimumDeposit())));
        balanceTextSpan.append(minimumDepositTextSpan);


        SpannableStringBuilder durationTextSpan = new SpannableStringBuilder("Duration :  ");
        durationTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1A237E")), 0, durationTextSpan.length(), 0);
        durationTextSpan.append(String.valueOf(Math.round(accountForDailyTransaction.getDuration()))).append(" years");
        SpannableStringBuilder meetingDayTextSpan = new SpannableStringBuilder("  Meeting Day : ");
        meetingDayTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1A237E")), 0, meetingDayTextSpan.length(), 0);
        meetingDayTextSpan.append(titleCase(new DataSourceOperationsCommon(context).getRealShortDayName(accountForDailyTransaction.getMeetingDayOfWeek())));
        durationTextSpan.append(meetingDayTextSpan);


        SpannableStringBuilder lastAccountDetailsTextSpan = new SpannableStringBuilder("Last Payment Date :  ");
        lastAccountDetailsTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#1A237E")), 0, lastAccountDetailsTextSpan.length(), 0);
        lastAccountDetailsTextSpan.append(accountForDailyTransaction.getLastAccountDetailsDate());


        viewHolder.textViewFirstItem.setText(balanceTextSpan, TextView.BufferType.SPANNABLE);
        viewHolder.textViewSecondItem.setText(durationTextSpan, TextView.BufferType.SPANNABLE);
        viewHolder.textViewThirdItem.setText(lastAccountDetailsTextSpan, TextView.BufferType.SPANNABLE);


        return rowView;
    }

    private static String titleCase(String givenString) {


        if (givenString.trim().contains(" ")) {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {
                stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
            }
            if (stringBuffer.toString().trim().equals("Primary Capital Buildup Savings")) {
                stringBuffer = new StringBuilder();
                stringBuffer.append("Capital Buildup Savings");
            }
            return stringBuffer.toString().trim();
        } else {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }

}
