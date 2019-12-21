package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.RealizedMemberData;


public class RealizedCustomAdapterForListViewRealizedMemberOtherInformation extends ArrayAdapter<RealizedMemberData> {

    private Context context;
    private List<RealizedMemberData> realizedMembersInformation;
    private static final int TOTAL_SINGLE_GROUP = -12345;
    private static final int TOTAL_GRAND_GROUP = -54321;
    private DialogListener listener;
    private Dialog dialog;

    public interface DialogListener {
        void lastPosition(boolean last);

    }

    public RealizedCustomAdapterForListViewRealizedMemberOtherInformation(Context context, List<RealizedMemberData> realizedMembersInformation, Dialog dialog, DialogListener listener) {
        super(context, R.layout.realized_member_others_information, realizedMembersInformation);
        this.realizedMembersInformation = realizedMembersInformation;

        this.listener = listener;
        this.context = context;
        this.dialog = dialog;
    }

    private static class ViewHolder {
        TextView textViewGroupName;
        TextView textViewPrimary;
        TextView textViewSecondary;
        TextView textViewSupplementary;
        TextView textViewSavingsDepositWithoutLts;
        TextView textViewSavingsWithdrawal;
        TextView textViewCbsDeposit;
        TextView textViewCbsWithdrawal;
        TextView textViewLtsDeposit;
        TextView textViewBadDebtCollection;
        TextView textViewExemptionAmount;
        TextView textViewNetCollection;
        TextView textViewRealizableTotal;

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
            rowView = layoutInflater.inflate(R.layout.realized_member_others_information, null);

            viewHolder.textViewGroupName = rowView.findViewById(R.id.textViewGroupName);
            viewHolder.textViewPrimary = rowView.findViewById(R.id.textViewPrimary);
            viewHolder.textViewSecondary = rowView.findViewById(R.id.textViewSecondary);
            viewHolder.textViewSupplementary = rowView.findViewById(R.id.textViewSupplementary);
            viewHolder.textViewSavingsDepositWithoutLts = rowView.findViewById(R.id.textViewSavingsDepositWithoutLts);
            viewHolder.textViewSavingsWithdrawal = rowView.findViewById(R.id.textViewSavingsWithdrawal);
            viewHolder.textViewCbsDeposit = rowView.findViewById(R.id.textViewCbsDeposit);
            viewHolder.textViewCbsWithdrawal = rowView.findViewById(R.id.textViewCbsWithdrawal);
            viewHolder.textViewLtsDeposit = rowView.findViewById(R.id.textViewLtsDeposit);
            viewHolder.textViewBadDebtCollection = rowView.findViewById(R.id.textViewBadDebtCollection);
            viewHolder.textViewExemptionAmount = rowView.findViewById(R.id.textViewExemptionAmount);
            viewHolder.textViewNetCollection = rowView.findViewById(R.id.textViewNetCollection);
            viewHolder.textViewRealizableTotal = rowView.findViewById(R.id.textViewRealizableTotal);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        RealizedMemberData realizedMemberData = getItem(position);

        assert realizedMemberData != null;
        if (realizedMemberData.getMemberId() == TOTAL_SINGLE_GROUP) {


            rowView.setBackgroundColor(Color.parseColor("#B3E5FC"));


            viewHolder.textViewGroupName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewPrimary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSecondary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSupplementary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSavingsDepositWithoutLts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSavingsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewCbsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewCbsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewLtsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewBadDebtCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewExemptionAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewNetCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewRealizableTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        } else if (realizedMemberData.getMemberId() == TOTAL_GRAND_GROUP) {
            rowView.setBackgroundColor(Color.parseColor("#EDE7F6"));

            viewHolder.textViewGroupName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewPrimary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSecondary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSupplementary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSavingsDepositWithoutLts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewSavingsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewCbsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewCbsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewLtsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewBadDebtCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewExemptionAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewNetCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            viewHolder.textViewRealizableTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        } else {

            if (realizedMembersInformation.get(position).isCollected()) {
                rowView.setBackgroundColor(Color.parseColor("#B2DFDB"));

            } else {
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }


            viewHolder.textViewGroupName.setTypeface(Typeface.SERIF);
            viewHolder.textViewPrimary.setTypeface(Typeface.SERIF);
            viewHolder.textViewSecondary.setTypeface(Typeface.SERIF);
            viewHolder.textViewSupplementary.setTypeface(Typeface.SERIF);
            viewHolder.textViewSavingsDepositWithoutLts.setTypeface(Typeface.SERIF);
            viewHolder.textViewSavingsWithdrawal.setTypeface(Typeface.SERIF);
            viewHolder.textViewCbsDeposit.setTypeface(Typeface.SERIF);
            viewHolder.textViewCbsWithdrawal.setTypeface(Typeface.SERIF);
            viewHolder.textViewLtsDeposit.setTypeface(Typeface.SERIF);
            viewHolder.textViewBadDebtCollection.setTypeface(Typeface.SERIF);
            viewHolder.textViewExemptionAmount.setTypeface(Typeface.SERIF);
            viewHolder.textViewNetCollection.setTypeface(Typeface.SERIF);
            viewHolder.textViewRealizableTotal.setTypeface(Typeface.SERIF);


            viewHolder.textViewGroupName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewPrimary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewSecondary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewSupplementary.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewSavingsDepositWithoutLts.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewSavingsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewCbsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewCbsWithdrawal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewLtsDeposit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewBadDebtCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewExemptionAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewNetCollection.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            viewHolder.textViewRealizableTotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        viewHolder.textViewGroupName.setText(realizedMembersInformation.get(position).getGroupName());
        viewHolder.textViewPrimary.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getPrimaryCollection())));
        viewHolder.textViewSecondary.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getSecondaryCollection())));
        viewHolder.textViewSupplementary.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getSupplementaryCollection())));
        viewHolder.textViewSavingsDepositWithoutLts.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getSavingsDepositWithoutLTS())));
        viewHolder.textViewSavingsWithdrawal.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getSavingsWithdrawal())));
        viewHolder.textViewCbsDeposit.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getCbsDeposit())));
        viewHolder.textViewCbsWithdrawal.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getCbsWithdrawal())));
        viewHolder.textViewLtsDeposit.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getLtsDeposit())));
        viewHolder.textViewBadDebtCollection.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getBadDebtCollection())));
        viewHolder.textViewExemptionAmount.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getExemptionTotal())));
        viewHolder.textViewNetCollection.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getNetCollection())));
        viewHolder.textViewRealizableTotal.setText(String.valueOf(Math.round(realizedMembersInformation.get(position).getTotalRealizable())));

        if (position == realizedMembersInformation.size() - 1) {
            listener.lastPosition(true);
            dialog.dismiss();
        }
        return rowView;
    }

}
