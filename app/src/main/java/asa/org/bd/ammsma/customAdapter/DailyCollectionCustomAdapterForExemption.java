package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
//import asa.org.bd.ammsma.service.ExtraTask;


public class DailyCollectionCustomAdapterForExemption extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions;
    private ArrayList<Float> collectionList = new ArrayList<>();
    private int initial = 0;
    private int runtime = 0;
    private ArrayList<Integer> checkList = new ArrayList<>();
    //private ExtraTask extraTask = new ExtraTask();

    private DataChangeListener listener;

    public interface DataChangeListener {
        void onDataChange(float total);

        void onExemptionChange(int position, boolean change);
    }


    public DailyCollectionCustomAdapterForExemption(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions, DataChangeListener listener) {
        super(context, R.layout.list_view_exemption_layout, accountForDailyTransactions);
        this.accountForDailyTransactions = accountForDailyTransactions;
        this.context = context;
        this.listener = listener;

        for (int i = 0; i < accountForDailyTransactions.size(); i++) {
            collectionList.add(i, accountForDailyTransactions.get(i).getLoanTransactionAmount());
            if (accountForDailyTransactions.get(i).isExemptedOrNot()) {
                checkList.add(i, 1);
            } else {
                checkList.add(i, 0);
            }

        }


    }


    private static class ViewHolder {

        TextView textViewName;
        CheckBox checkBox;

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_view_exemption_layout, null);

            viewHolder.textViewName = rowView.findViewById(R.id.loan_name);
            viewHolder.checkBox = rowView.findViewById(R.id.checkbox);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        viewHolder.checkBox.setTag(position);

        AccountForDailyTransaction accountForDailyTransaction = getItem(position);
        int tag = Integer.parseInt(viewHolder.checkBox.getTag().toString().trim());
        assert accountForDailyTransaction != null;


        if (viewHolder.checkBox.getText().toString().trim().equals("")) {

            if (collectionList.get(tag) != 0 || initial < accountForDailyTransactions.size()) {
                run(viewHolder, accountForDailyTransaction, rowView);
                initial++;
            }
        }

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, context.getApplicationContext());
            if (isChecked) {
                int tag1 = (int) viewHolder.checkBox.getTag();
                checkList.set(tag1, 1);
                listener.onExemptionChange(tag1, true);
                total();
            } else {
                int tag1 = (int) viewHolder.checkBox.getTag();
                checkList.set(tag1, 0);
                listener.onExemptionChange(tag1, false);
                total();
            }

        });


        return rowView;
    }

    @SuppressLint("SetTextI18n")
    private void run(ViewHolder viewHolder, final AccountForDailyTransaction accountForDailyTransaction, final View rowView) {

        int programId = accountForDailyTransaction.getProgramId();
        Log.i("programId", programId + accountForDailyTransaction.getProgramName());
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
                viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName() + " S ");
            } else {
                viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
            }
            viewHolder.checkBox.setChecked(accountForDailyTransaction.isExemptedOrNot());
           /* if (accountForDailyTransaction.getBalance() == 0) {
                viewHolder.checkBox.setEnabled(true);
                viewHolder.checkBox.setChecked(accountForDailyTransaction.isExemptedOrNot());

            } else {
                viewHolder.checkBox.setEnabled(false);
                viewHolder.checkBox.setChecked(accountForDailyTransaction.isExemptedOrNot());
            }*/
            viewHolder.checkBox.setText(String.valueOf(Math.round(accountForDailyTransaction.getLoanTransactionAmount())));

        }
        runtime++;
        Log.i("runtime Collection", runtime + " / " + programId);
        total();
    }

    private void total() {
        float total = 0;
        for (int i = 0; i < collectionList.size(); i++) {
            if (checkList.get(i) == 1) {
                total = total + collectionList.get(i);
            }

        }
        listener.onDataChange(total);
        Log.i("Exemption", String.valueOf(total));

    }
}
