package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;


public class BadDebtAdapterForAccountInfo extends ArrayAdapter<AccountForDailyTransaction> {
    private Context context;


    public BadDebtAdapterForAccountInfo(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions) {
        super(context, R.layout.list_view_custom_adapter_bad_debt_account_info, accountForDailyTransactions);
        this.context = context;
    }

    private static class ViewHolder {
        TextView textViewName;
        TextView textViewValue;
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;


        View rowView = convertView;
        if (rowView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_view_custom_adapter_bad_debt_account_info, null);


            holder.textViewName = rowView.findViewById(R.id.loan_name);
            holder.textViewValue = rowView.findViewById(R.id.loan_amount);


            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        AccountForDailyTransaction accountForDailyTransaction = getItem(position);

        assert accountForDailyTransaction != null;

        holder.textViewValue.setTag(position);


        if (holder.textViewValue.getText().toString().trim().equals("")) {

            run(holder, accountForDailyTransaction, position);
        }


        return rowView;
    }

    @SuppressLint("SetTextI18n")
    private void run(ViewHolder holder, AccountForDailyTransaction accountForDailyTransaction, int position) {


        holder.textViewName.setText(titleCase(accountForDailyTransaction.getProgramName()) + "  ( " + 0 + (position + 1) + " )");
        holder.textViewValue.setText(String.valueOf(Math.round(accountForDailyTransaction.getBalance())));

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
}


