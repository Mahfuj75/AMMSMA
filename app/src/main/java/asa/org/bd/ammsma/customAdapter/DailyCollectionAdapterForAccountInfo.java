package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;

public class DailyCollectionAdapterForAccountInfo extends ArrayAdapter<AccountForDailyTransaction> {
    private Context context;
    private int ltsCount = 1;
    private boolean overDueOrAdvance = false;


    public DailyCollectionAdapterForAccountInfo(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions) {
        super(context, R.layout.list_view_custom_adapter_account_status, accountForDailyTransactions);
        this.context = context;

        for (int i = 0; i < accountForDailyTransactions.size(); i++) {
            if (accountForDailyTransactions.get(i).getAdvanceAmount() > 0 || accountForDailyTransactions.get(i).getOverdueAmount() > 0) {
                overDueOrAdvance = true;
                break;
            }
        }
    }

    private static class ViewHolder {
        TextView textViewName;
        TextView textViewValue;
        TextView textViewOverDueOrAdvance;

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
            rowView = layoutInflater.inflate(R.layout.list_view_custom_adapter_account_status, null);


            holder.textViewName = rowView.findViewById(R.id.loan_name);
            holder.textViewValue = rowView.findViewById(R.id.loan_amount);
            holder.textViewOverDueOrAdvance = rowView.findViewById(R.id.textViewOverDueOrAdvance);


            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        AccountForDailyTransaction accountForDailyTransaction = getItem(position);

        assert accountForDailyTransaction != null;

        holder.textViewValue.setTag(position);


        if (holder.textViewValue.getText().toString().trim().equals("")) {

            run(holder, accountForDailyTransaction, rowView);
        }


        return rowView;
    }

    @SuppressLint("SetTextI18n")
    private void run(ViewHolder holder, final AccountForDailyTransaction accountForDailyTransaction, final View rowView) {

        int programId = accountForDailyTransaction.getProgramId();

        rowView.setOnClickListener(v -> {

            String text = accountForDailyTransaction.getProgramNameChange().getValidName();
            SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
            biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
            Toast toast = Toast.makeText(rowView.getContext(), biggerText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        if (programId > 100 && programId < 200) {


            if (accountForDailyTransaction.getSupplementary()) {

                holder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName() + " S ");

            } else {
                holder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());

            }

            if (accountForDailyTransaction.getBalance() < 0) {
                holder.textViewValue.setText("0" + " / " + Math.round(accountForDailyTransaction.getInstallmentNumber()));
            } else {
                holder.textViewValue.setText(Math.round(accountForDailyTransaction.getBalance()) + " / " + Math.round(accountForDailyTransaction.getInstallmentNumber()));
            }

            if (accountForDailyTransaction.getAdvanceAmount() > 0) {
                SpannableStringBuilder titleTextSpan = new SpannableStringBuilder("Advance Amount :   ");
                titleTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#004D40")), 0, titleTextSpan.length(), 0);
                titleTextSpan.append(String.valueOf(accountForDailyTransaction.getAdvanceAmount()));

                holder.textViewOverDueOrAdvance.setText(titleTextSpan, TextView.BufferType.SPANNABLE);
                holder.textViewOverDueOrAdvance.setVisibility(View.VISIBLE);
            }

            if (!accountForDailyTransaction.isScheduled() && accountForDailyTransaction.getOverDueAmountActual() > 0) {
                SpannableStringBuilder titleTextSpan = new SpannableStringBuilder("Overdue Amount :   ");
                titleTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#B71C1C")), 0, titleTextSpan.length(), 0);
                titleTextSpan.append(String.valueOf(Math.round(accountForDailyTransaction.getOverDueAmountActual())));
                holder.textViewOverDueOrAdvance.setText(titleTextSpan, TextView.BufferType.SPANNABLE);
                holder.textViewOverDueOrAdvance.setVisibility(View.VISIBLE);
            } else if (accountForDailyTransaction.getAccountStatus() == 1 && accountForDailyTransaction.getOverDueAmountActual() > 0) {
                SpannableStringBuilder titleTextSpan = new SpannableStringBuilder("Overdue Amount :   ");
                titleTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#B71C1C")), 0, titleTextSpan.length(), 0);
                titleTextSpan.append(String.valueOf(Math.round(accountForDailyTransaction.getOverDueAmountActual())));

                holder.textViewOverDueOrAdvance.setText(titleTextSpan, TextView.BufferType.SPANNABLE);
                holder.textViewOverDueOrAdvance.setVisibility(View.VISIBLE);
            } else if (accountForDailyTransaction.getOverDueAmountActual() - accountForDailyTransaction.getInstallmentAmount() > 0) {
                SpannableStringBuilder titleTextSpan = new SpannableStringBuilder("Overdue Amount :   ");
                titleTextSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#B71C1C")), 0, titleTextSpan.length(), 0);
                titleTextSpan.append(String.valueOf(Math.round(accountForDailyTransaction.getOverDueAmountActual() - accountForDailyTransaction.getInstallmentAmount())));

                holder.textViewOverDueOrAdvance.setText(titleTextSpan, TextView.BufferType.SPANNABLE);
                holder.textViewOverDueOrAdvance.setVisibility(View.VISIBLE);
            } else if (overDueOrAdvance) {
                holder.textViewOverDueOrAdvance.setVisibility(View.VISIBLE);
            } else {
                holder.textViewOverDueOrAdvance.setVisibility(View.GONE);
            }
        } else if (programId > 300 && programId < 400) {
            holder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
            holder.textViewValue.setText(Math.round(accountForDailyTransaction.getBalance()) + " (" + Math.round(accountForDailyTransaction.getMinimumDeposit()) + ")");
        } else if (programId > 200 && programId < 300) {
            if (programId == 204) {
                holder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName() + " " + ltsCount);
                ltsCount++;
            } else {

                holder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
            }
            holder.textViewValue.setText(Math.round(accountForDailyTransaction.getBalance()) + " (" + Math.round(accountForDailyTransaction.getMinimumDeposit()) + ")");
        }
    }
}


