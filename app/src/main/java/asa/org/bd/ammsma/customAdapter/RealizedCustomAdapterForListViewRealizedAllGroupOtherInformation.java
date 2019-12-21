package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.RealizedGroupData;


public class RealizedCustomAdapterForListViewRealizedAllGroupOtherInformation extends ArrayAdapter<RealizedGroupData> {

    private Context context;
    private List<RealizedGroupData> realizedGroupDataArrayList;

    public RealizedCustomAdapterForListViewRealizedAllGroupOtherInformation(Context context, List<RealizedGroupData> realizedGroupDataArrayList) {
        super(context, R.layout.realized_all_group_other_information, realizedGroupDataArrayList);
        this.realizedGroupDataArrayList = realizedGroupDataArrayList;
        this.context = context;
    }

    private static class ViewHolder {
        TextView textViewLoanCollection;
        TextView textViewSavingsDeposit;
        TextView textViewSavingsWithdrawal;
        TextView textViewCbsDeposit;
        TextView textViewCbsWithdrawal;
        TextView textViewLtsDeposit;
        TextView textViewBadDebtCollection;
        TextView textViewExemptionAmount;
        TextView textViewNetCollection;
        TextView textViewLoanRealizableTotal;

    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.realized_all_group_other_information, null);

            viewHolder.textViewLoanCollection = rowView.findViewById(R.id.textViewLoanCollection);
            viewHolder.textViewSavingsDeposit = rowView.findViewById(R.id.textViewSavingsDeposit);
            viewHolder.textViewSavingsWithdrawal = rowView.findViewById(R.id.textViewSavingsWithdrawal);
            viewHolder.textViewCbsDeposit = rowView.findViewById(R.id.textViewCbsDeposit);
            viewHolder.textViewCbsWithdrawal = rowView.findViewById(R.id.textViewCbsWithdrawal);
            viewHolder.textViewLtsDeposit = rowView.findViewById(R.id.textViewLtsDeposit);
            viewHolder.textViewBadDebtCollection = rowView.findViewById(R.id.textViewBadDebtCollection);
            viewHolder.textViewExemptionAmount = rowView.findViewById(R.id.textViewExemptionTotal);
            viewHolder.textViewNetCollection = rowView.findViewById(R.id.textViewNetCollection);
            viewHolder.textViewLoanRealizableTotal = rowView.findViewById(R.id.textViewLoanRealizableTotal);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (viewHolder.textViewLoanCollection.getText().toString().trim().equals("")) {
            if (realizedGroupDataArrayList.get(position).getGroupId() == (-12345)) {

                rowView.setBackgroundResource(0);
                rowView.setBackgroundColor(Color.parseColor("#B3E5FC"));


                viewHolder.textViewLoanCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewSavingsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewSavingsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewCbsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewCbsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewLtsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewBadDebtCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewExemptionAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewNetCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewLoanRealizableTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);


            } else {
                rowView.setBackgroundResource(0);
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                viewHolder.textViewLoanCollection.setLines(1);
                viewHolder.textViewSavingsDeposit.setLines(1);
                viewHolder.textViewSavingsWithdrawal.setLines(1);
                viewHolder.textViewCbsDeposit.setLines(1);
                viewHolder.textViewCbsWithdrawal.setLines(1);
                viewHolder.textViewLtsDeposit.setLines(1);
                viewHolder.textViewBadDebtCollection.setLines(1);
                viewHolder.textViewExemptionAmount.setLines(1);
                viewHolder.textViewNetCollection.setLines(1);
                viewHolder.textViewLoanRealizableTotal.setLines(1);
            }
            viewHolder.textViewLoanCollection.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getLoanCollection())));
            viewHolder.textViewSavingsDeposit.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getSavingsDepositWithoutLts())));
            viewHolder.textViewSavingsWithdrawal.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getSavingsWithdrawal())));
            viewHolder.textViewCbsDeposit.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getCbsDeposit())));
            viewHolder.textViewCbsWithdrawal.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getCbsWithdrawal())));
            viewHolder.textViewLtsDeposit.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getLtsDeposit())));
            viewHolder.textViewBadDebtCollection.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getBadDebtCollection())));
            viewHolder.textViewExemptionAmount.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getExemptionTotal())));
            viewHolder.textViewNetCollection.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getNetCollection())));
            viewHolder.textViewLoanRealizableTotal.setText(String.valueOf(Math.round(realizedGroupDataArrayList.get(position).getLoanRealizable())));
        }


        return rowView;
    }

}
