package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;


public class BadDebtAdapterForMembersAccountList extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;


    public BadDebtAdapterForMembersAccountList(Context context, ArrayList<AccountForDailyTransaction> badDebtAccountList) {
        super(context, R.layout.bad_debt_account_details, badDebtAccountList);
        this.context = context;
    }


    private static class ViewHolder {

        TextView textViewProgramName;
        TextView textViewBadDebtAmount;
        TextView textViewOpeningDate;


    }


    @NonNull
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.bad_debt_account_details, null);

            viewHolder.textViewProgramName = rowView.findViewById(R.id.textViewProgramName);
            viewHolder.textViewBadDebtAmount = rowView.findViewById(R.id.textViewBadDebtAmount);
            viewHolder.textViewOpeningDate = rowView.findViewById(R.id.textViewOpeningDate);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        AccountForDailyTransaction accountForDailyTransaction = getItem(position);


        assert accountForDailyTransaction != null;
        viewHolder.textViewProgramName.setText(titleCase(accountForDailyTransaction.getProgramName()) + "  ( " + (position + 1) + " )");
        viewHolder.textViewBadDebtAmount.setText(String.valueOf(Math.round(accountForDailyTransaction.getBalance())));
        viewHolder.textViewOpeningDate.setText(accountForDailyTransaction.getOpeningDateValue());


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
