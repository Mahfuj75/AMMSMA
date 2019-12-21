package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.extra.MemberDetailsInfo;


public class DailyCollectionAdapterForMembersAccountList extends ArrayAdapter<MemberDetailsInfo> {

    private Context context;
    private DataSourceRead dataSourceRead;


    public DailyCollectionAdapterForMembersAccountList(Context context, ArrayList<MemberDetailsInfo> memberDetailsInfoList) {
        super(context, R.layout.account_details, memberDetailsInfoList);
        this.context = context;
        dataSourceRead = new DataSourceRead(context);
    }


    private static class ViewHolder {
        LinearLayout linearLayoutPrimaryLoan;
        LinearLayout linearLayoutSecondaryLoan;
        LinearLayout linearLayoutServiceCharge;
        LinearLayout linearLayoutForLts;
        LinearLayout linearLayoutForShortTermLoan;

        TextView textViewBalance;
        TextView textViewMinimumDeposit;
        TextView textViewSavingOpeningDate;
        TextView textViewServiceChargeInterest;
        TextView textViewProgramNamePrimary;
        TextView textViewProgramNameOthers;
        TextView textViewDisburseDate;
        TextView textViewDisburseWithScAmount;
        TextView textViewDisbursePrincipal;
        TextView textViewOutstandingAmount;
        TextView textViewOverdueAmount;
        TextView textViewInstallmentAmount;
        TextView textViewRemainingInstallmentNumber;
        TextView textViewLoanCycle;
        TextView textViewAdvanceAmount;
        TextView textViewMissingLtsCount;
        TextView textViewShortTermLoan;


    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.account_details, null);

            viewHolder.textViewBalance = rowView.findViewById(R.id.textViewBalance);
            viewHolder.textViewProgramNamePrimary = rowView.findViewById(R.id.textViewProgramNamePrimary);
            viewHolder.textViewProgramNameOthers = rowView.findViewById(R.id.textViewProgramNameOthers);
            viewHolder.textViewMinimumDeposit = rowView.findViewById(R.id.textViewMinimumDeposit);
            viewHolder.textViewDisburseDate = rowView.findViewById(R.id.textViewDisburseDate);
            viewHolder.textViewSavingOpeningDate = rowView.findViewById(R.id.textViewSavingOpeningDate);
            viewHolder.textViewDisburseWithScAmount = rowView.findViewById(R.id.textViewDisburseWithScAmount);
            viewHolder.textViewDisbursePrincipal = rowView.findViewById(R.id.textViewDisbursePrincipal);
            viewHolder.linearLayoutPrimaryLoan = rowView.findViewById(R.id.linearLayoutPrimaryLoan);
            viewHolder.linearLayoutSecondaryLoan = rowView.findViewById(R.id.linearLayoutSecondaryLoan);
            viewHolder.textViewOutstandingAmount = rowView.findViewById(R.id.textViewOutstandingAmount);
            viewHolder.textViewOverdueAmount = rowView.findViewById(R.id.textViewOverdueAmount);
            viewHolder.textViewInstallmentAmount = rowView.findViewById(R.id.textViewInstallmentAmount);
            viewHolder.textViewRemainingInstallmentNumber = rowView.findViewById(R.id.textViewRemainingInstallmentNumber);
            viewHolder.textViewLoanCycle = rowView.findViewById(R.id.textViewLoanCycle);
            viewHolder.textViewAdvanceAmount = rowView.findViewById(R.id.textViewAdvanceAmount);
            viewHolder.textViewServiceChargeInterest = rowView.findViewById(R.id.textViewServiceChargeInterest);
            viewHolder.linearLayoutServiceCharge = rowView.findViewById(R.id.linearLayoutServiceCharge);
            viewHolder.linearLayoutForLts = rowView.findViewById(R.id.linearLayoutForLts);
            viewHolder.linearLayoutForShortTermLoan = rowView.findViewById(R.id.linearLayoutForShortTermLoan);
            viewHolder.textViewShortTermLoan = rowView.findViewById(R.id.textViewShortTermLoan);
            viewHolder.textViewMissingLtsCount = rowView.findViewById(R.id.textViewMissingLtsCount);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        MemberDetailsInfo memberDetailInfo = getItem(position);

        assert memberDetailInfo != null;
        if (memberDetailInfo.getProgramId() > 100 && memberDetailInfo.getProgramId() < 200) {
            viewHolder.textViewProgramNamePrimary.setText(memberDetailInfo.getProgramNameChange().getValidName());
            viewHolder.linearLayoutPrimaryLoan.setVisibility(View.VISIBLE);
            viewHolder.linearLayoutSecondaryLoan.setVisibility(View.GONE);
            viewHolder.linearLayoutServiceCharge.setVisibility(View.GONE);

            if (memberDetailInfo.getProgramId() == 135 || memberDetailInfo.getProgramId() == 109) {
                viewHolder.linearLayoutForShortTermLoan.setVisibility(View.VISIBLE);
                if (this.dataSourceRead.getScheduleDate(memberDetailInfo.getAccountId()).equals("")) {
                    viewHolder.linearLayoutForShortTermLoan.setVisibility(View.GONE);
                } else {
                    viewHolder.linearLayoutForShortTermLoan.setVisibility(View.VISIBLE);
                    viewHolder.textViewShortTermLoan.setText(this.dataSourceRead.getScheduleDate(memberDetailInfo.getAccountId()));
                }

            } else {
                viewHolder.linearLayoutForShortTermLoan.setVisibility(View.GONE);
            }

            viewHolder.textViewDisburseDate.setText(memberDetailInfo.getDisburseOrSavingOpeningDate());
            viewHolder.textViewDisburseWithScAmount.setText(memberDetailInfo.getDisburseAmountWithServiceCharge());
            viewHolder.textViewDisbursePrincipal.setText(memberDetailInfo.getDisbursePrincipal());
            viewHolder.textViewOutstandingAmount.setText(memberDetailInfo.getOutstandingAmountOrBalance());
            viewHolder.textViewOverdueAmount.setText(memberDetailInfo.getOverdueAmountActual());
            viewHolder.textViewInstallmentAmount.setText(memberDetailInfo.getInstallmentAmountOrMinimumDeposit());
            viewHolder.textViewRemainingInstallmentNumber.setText(memberDetailInfo.getRemainingInstallmentNumber());
            viewHolder.textViewLoanCycle.setText(memberDetailInfo.getCycle());
            viewHolder.textViewAdvanceAmount.setText(memberDetailInfo.getAdvanceAmount());
        } else {

            if (memberDetailInfo.getProgramId() != 204 && memberDetailInfo.getProgramId() >= 200 && memberDetailInfo.getProgramId() < 300) {
                viewHolder.linearLayoutServiceCharge.setVisibility(View.VISIBLE);
                viewHolder.textViewServiceChargeInterest.setText(String.valueOf((int) memberDetailInfo.getServiceChargeInterest()));
            } else {
                viewHolder.linearLayoutServiceCharge.setVisibility(View.GONE);
            }

            if (memberDetailInfo.getProgramId() == 204) {
                viewHolder.linearLayoutForLts.setVisibility(View.VISIBLE);
                viewHolder.textViewMissingLtsCount.setText(String.valueOf(memberDetailInfo.getMissingLtsPremium()));
            } else {
                viewHolder.linearLayoutForLts.setVisibility(View.GONE);
            }

            viewHolder.linearLayoutPrimaryLoan.setVisibility(View.GONE);
            viewHolder.linearLayoutSecondaryLoan.setVisibility(View.VISIBLE);
            viewHolder.textViewProgramNameOthers.setText(memberDetailInfo.getProgramNameChange().getValidName());
            viewHolder.textViewBalance.setText(memberDetailInfo.getOutstandingAmountOrBalance());
            viewHolder.textViewMinimumDeposit.setText(memberDetailInfo.getInstallmentAmountOrMinimumDeposit());
            viewHolder.textViewSavingOpeningDate.setText(memberDetailInfo.getDisburseOrSavingOpeningDate());

        }
        return rowView;
    }

}
