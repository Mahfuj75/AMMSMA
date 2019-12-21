package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.MiscellaneousMemberBalance;


public class MiscellaneousMemberBalanceCustomAdapterOtherInfo extends ArrayAdapter<MiscellaneousMemberBalance> {

    private Context context;
    private List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList;


    public MiscellaneousMemberBalanceCustomAdapterOtherInfo(Context context, List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList) {
        super(context, R.layout.list_member_balance_others, miscellaneousMemberBalanceList);
        this.miscellaneousMemberBalanceList = miscellaneousMemberBalanceList;
        this.context = context;
    }

    private static class ViewHolder {
        TextView textViewPrimaryDisbursedDate;
        TextView textViewPrimaryDisbursed;
        TextView textViewPrimaryOverDue;
        TextView textViewSecondaryDisbursed;
        TextView textViewSecondaryOverDue;
        TextView textViewSecondaryOutstanding;
        TextView textViewPrimaryOutStanding;
        TextView textViewSavingBalance;
        TextView textViewLtsBalance;
        TextView textViewCbsBalance;
        TextView textViewNetBalance;
        TextView textViewPrimaryInstallmentNumber;
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_member_balance_others, null);

            viewHolder.textViewPrimaryDisbursedDate = rowView.findViewById(R.id.textViewPrimaryDisbursedDate);
            viewHolder.textViewPrimaryDisbursed = rowView.findViewById(R.id.textViewPrimaryDisbursed);
            viewHolder.textViewPrimaryOverDue = rowView.findViewById(R.id.textViewPrimaryOverDue);
            viewHolder.textViewSecondaryDisbursed = rowView.findViewById(R.id.textViewSecondaryDisbursed);
            viewHolder.textViewSecondaryOverDue = rowView.findViewById(R.id.textViewSecondaryOverdue);
            viewHolder.textViewSecondaryOutstanding = rowView.findViewById(R.id.textViewSecondaryOutstanding);
            viewHolder.textViewPrimaryOutStanding = rowView.findViewById(R.id.textViewPrimaryOutstanding);
            viewHolder.textViewSavingBalance = rowView.findViewById(R.id.textViewSavingBalance);
            viewHolder.textViewLtsBalance = rowView.findViewById(R.id.textViewLtsBalance);
            viewHolder.textViewCbsBalance = rowView.findViewById(R.id.textViewCbsBalance);
            viewHolder.textViewNetBalance = rowView.findViewById(R.id.textViewNetBalance);
            viewHolder.textViewPrimaryInstallmentNumber = rowView.findViewById(R.id.textViewPrimaryInstallmentNumber);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        if (viewHolder.textViewNetBalance.getText().toString().equals("") || viewHolder.textViewPrimaryDisbursed.getText().toString().trim().equals("")
                || viewHolder.textViewPrimaryOverDue.getText().toString().trim().equals("") || viewHolder.textViewSecondaryDisbursed.getText().toString().trim().equals("")
                || viewHolder.textViewSecondaryOverDue.getText().toString().trim().equals("") || viewHolder.textViewSecondaryOutstanding.getText().toString().trim().equals("")
                || viewHolder.textViewPrimaryOutStanding.getText().toString().trim().equals("") || viewHolder.textViewSavingBalance.getText().toString().trim().equals("")
                || viewHolder.textViewLtsBalance.getText().toString().trim().equals("") || viewHolder.textViewCbsBalance.getText().toString().trim().equals("")) {
            if (miscellaneousMemberBalanceList.get(position).getMemberId() == (-12345)) {
                rowView.setBackgroundColor(Color.parseColor("#B3E5FC"));

                viewHolder.textViewPrimaryDisbursedDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

                viewHolder.textViewPrimaryDisbursed.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryDisbursed()));

                if (miscellaneousMemberBalanceList.get(position).getPrimaryOverdue() < 0) {
                    viewHolder.textViewPrimaryOverDue.setText(String.valueOf(0));
                } else {
                    viewHolder.textViewPrimaryOverDue.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryOverdue()));
                }

                viewHolder.textViewPrimaryOverDue.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryOverdue()));
                viewHolder.textViewSecondaryDisbursed.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSecondaryDisbursed()));
                if (miscellaneousMemberBalanceList.get(position).getSecondaryOverdue() < 0) {
                    viewHolder.textViewSecondaryOverDue.setText(String.valueOf(0));
                } else {
                    viewHolder.textViewSecondaryOverDue.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSecondaryOverdue()));
                }

                viewHolder.textViewSecondaryOutstanding.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSecondaryOutstanding()));
                viewHolder.textViewPrimaryOutStanding.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryOutstanding()));
                viewHolder.textViewSavingBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSavingsBalance()));
                viewHolder.textViewLtsBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getLtsBalance()));
                viewHolder.textViewCbsBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getCbsBalance()));
                viewHolder.textViewNetBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getNetBalance()));

            } else {
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                viewHolder.textViewPrimaryDisbursedDate.setText(miscellaneousMemberBalanceList.get(position).getPrimaryDisbursedDate());
                viewHolder.textViewPrimaryDisbursed.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryDisbursed()));
                viewHolder.textViewPrimaryOverDue.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryOverdue()));
                viewHolder.textViewSecondaryDisbursed.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSecondaryDisbursed()));
                viewHolder.textViewSecondaryOverDue.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSecondaryOverdue()));
                viewHolder.textViewSecondaryOutstanding.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSecondaryOutstanding()));
                viewHolder.textViewPrimaryOutStanding.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryOutstanding()));
                viewHolder.textViewSavingBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getSavingsBalance()));
                viewHolder.textViewLtsBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getLtsBalance()));
                viewHolder.textViewCbsBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getCbsBalance()));
                viewHolder.textViewNetBalance.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getNetBalance()));
                viewHolder.textViewPrimaryInstallmentNumber.setText(String.valueOf(miscellaneousMemberBalanceList.get(position).getPrimaryInstallmentNumber()));

            }


            final View finalRowView = rowView;
            viewHolder.textViewPrimaryDisbursedDate.setOnClickListener(v -> {
                String text = viewHolder.textViewPrimaryDisbursedDate.getText().toString().trim();
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            });

            viewHolder.textViewPrimaryDisbursed.setOnClickListener(v -> {
                String text = "Primary Disbursed";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
            viewHolder.textViewPrimaryOverDue.setOnClickListener(v -> {
                String text = "Primary OverDue";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
            viewHolder.textViewSecondaryDisbursed.setOnClickListener(v -> {
                String text = "Secondary Disbursed";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewSecondaryOverDue.setOnClickListener(v -> {
                String text = "Secondary OverDue";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewPrimaryOutStanding.setOnClickListener(v -> {
                String text = "Primary OutStanding";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewSecondaryOutstanding.setOnClickListener(v -> {
                String text = "Secondary OutStanding";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewSavingBalance.setOnClickListener(v -> {
                String text = "Saving Balance";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewLtsBalance.setOnClickListener(v -> {
                String text = "LTS Balance";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewCbsBalance.setOnClickListener(v -> {
                String text = "CBS Balance";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            viewHolder.textViewNetBalance.setOnClickListener(v -> {
                String text = "Net Balance";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
            viewHolder.textViewPrimaryInstallmentNumber.setOnClickListener(v -> {
                String text = "Primary Outstanding Installment Number";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
        }

        return rowView;
    }

}
