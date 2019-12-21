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
import asa.org.bd.ammsma.extra.TransactionHistory;

public class CustomAdapterForAccountDetails extends ArrayAdapter<TransactionHistory> {

    private Context context;

    public CustomAdapterForAccountDetails(Context context, ArrayList<TransactionHistory> transactionHistories) {
        super(context, R.layout.account_details_single_item, transactionHistories);
        this.context = context;
    }


    private static class ViewHolder {
        TextView textViewDate;
        TextView textViewAmount;
        TextView textViewType;
        TextView textViewProcess;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.account_details_single_item, null);

            viewHolder.textViewDate = rowView.findViewById(R.id.textViewDate);
            viewHolder.textViewAmount = rowView.findViewById(R.id.textViewAmount);
            viewHolder.textViewType = rowView.findViewById(R.id.textViewType);
            viewHolder.textViewProcess = rowView.findViewById(R.id.textViewProcess);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        TransactionHistory transactionHistory = getItem(position);
        assert transactionHistory != null;
        viewHolder.textViewDate.setText(transactionHistory.getDate());
        viewHolder.textViewAmount.setText(transactionHistory.getAmount());
        viewHolder.textViewType.setText(transactionHistory.getType());
        viewHolder.textViewProcess.setText(transactionHistory.getProcess());
        return rowView;
    }


}
