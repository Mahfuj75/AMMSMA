package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;


public class LonaInformationCustomAdapterForListView extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;
    private DataChangeListener listener;
    private List<AccountForDailyTransaction> loanList;

    public interface DataChangeListener {
        void onDataChange(int position);
    }

    public LonaInformationCustomAdapterForListView(Context context, List<AccountForDailyTransaction> loanList, DataChangeListener listener) {
        super(context, R.layout.list_item_for_previous_loan, loanList);
        this.context = context;
        this.listener = listener;
        this.loanList = loanList;
    }

    private static class ViewHolder {

        TextView textViewLoanName;
        TextView textViewOpeningDate;
        TextView textViewOutstandingAmount;
        TextView textViewInstallmentAmount;
        TextView textViewDisburseAmount;
        TextView textViewServiceCharge;
        Button buttonLtsDelete;


    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_item_for_previous_loan, null);

            viewHolder.textViewLoanName = rowView.findViewById(R.id.textViewLoanName);
            viewHolder.textViewOpeningDate = rowView.findViewById(R.id.textViewOpeningDate);
            viewHolder.textViewOutstandingAmount = rowView.findViewById(R.id.textViewOutstandingAmount);
            viewHolder.textViewInstallmentAmount = rowView.findViewById(R.id.textViewInstallmentAmount);
            viewHolder.buttonLtsDelete = rowView.findViewById(R.id.buttonLtsDelete);
            viewHolder.textViewDisburseAmount = rowView.findViewById(R.id.textViewDisburseAmount);
            viewHolder.textViewServiceCharge = rowView.findViewById(R.id.textViewServiceCharge);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.textViewLoanName.setText(titleCase(loanList.get(position).getProgramName()));
        viewHolder.textViewInstallmentAmount.setText(String.valueOf(Math.round(loanList.get(position).getBaseInstallmentAmount())));
        viewHolder.textViewOpeningDate.setText(loanList.get(position).getOpeningDateValue());
        viewHolder.textViewOutstandingAmount.setText(String.valueOf(Math.round(loanList.get(position).getBalance())));
        viewHolder.textViewDisburseAmount.setText(String.valueOf(Math.round(loanList.get(position).getDisbursedAmount())));
        viewHolder.textViewServiceCharge.setText(String.valueOf(Math.round(loanList.get(position).getServiceChargeAmount())));




        if (loanList.get(position).getFlag() == 1 && loanList.get(position).getOpeningDate() == new DataSourceOperationsCommon(context).getWorkingDay()) {
            viewHolder.buttonLtsDelete.setVisibility(View.VISIBLE);
            final View finalRowView = rowView;
            viewHolder.buttonLtsDelete.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(finalRowView.getRootView().getContext());

                builder.setMessage(
                        "Are you sure you want to delete this loan account ?")
                        .setCancelable(false)
                        .setTitle("Delete")
                        .setPositiveButton("Yes",
                                (dialog, id) -> {
                                    Toast.makeText(context.getApplicationContext(), "Delete Complete", Toast.LENGTH_LONG).show();
                                    if (listener != null) {
                                        listener.onDataChange(position);
                                        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                                        convertView.setVisibility(View.GONE);
                                    }
                                })
                        .setNegativeButton("No",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();


            });
        }
        else {
            viewHolder.buttonLtsDelete.setVisibility(View.INVISIBLE);
        }


        return rowView;
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
