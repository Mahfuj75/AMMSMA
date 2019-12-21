package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.extra.SavingsFriendly;
import asa.org.bd.ammsma.service.ExtraTask;


public class DailyCollectionAdapterForWithdrawal extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions;
    private ArrayList<Float> withdrawalList = new ArrayList<>();
    private ArrayList<Float> debitList = new ArrayList<>();
    private ArrayList<Boolean> errorCollection = new ArrayList<>();
    private DataChangeListener listener;

    private static boolean ERROR_TRUE = true;
    private static boolean ERROR_FALSE = false;
    private float totalBalanceWithoutLoan;
    private SavingsFriendly savingsFriendly;

    private boolean error = false;
    private int initial = 0;
    private ExtraTask extraTask = new ExtraTask();

    public interface DataChangeListener {
        void onDataChange(float totalAmountCollection);

        void errorSet(boolean withdrawalError);
    }


    public DailyCollectionAdapterForWithdrawal(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions,SavingsFriendly savingsFriendly,ArrayList<AccountForDailyTransaction> onlyLongList, DataChangeListener listener) {
        super(context, R.layout.list_view_custom_adapter_daily_collection, accountForDailyTransactions);
        this.accountForDailyTransactions = accountForDailyTransactions;
        this.context = context;
        new DataSourceRead(context);
        this.savingsFriendly = savingsFriendly;

        this.totalBalanceWithoutLoan=0;

        for (AccountForDailyTransaction transaction:accountForDailyTransactions
                ) {
            if(transaction.getProgramTypeId()!=1)
            {
                this.totalBalanceWithoutLoan+=transaction.getBalance()+transaction.getDebit();
            }

        }

        for (AccountForDailyTransaction onlyLong:onlyLongList
             ) {
            if(onlyLong.getProgramTypeId()!=1)
            {
                this.totalBalanceWithoutLoan+=onlyLong.getBalance()+onlyLong.getDebit();
            }
        }

        for (int i = 0; i < accountForDailyTransactions.size(); i++) {
            withdrawalList.add(i, accountForDailyTransactions.get(i).getDebit());
            debitList.add(i, accountForDailyTransactions.get(i).getDebit());
            errorCollection.add(i, ERROR_FALSE);
        }
        this.listener = listener;
        total(0, 0, 1);
        errorSetProgram();
    }

    private static class ViewHolder {
        TextView textViewName;
        ImageView imageViewShow;
        EditText editTextValue;
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
            rowView = layoutInflater.inflate(R.layout.list_view_custom_adapter_daily_collection, null);

            viewHolder.textViewName = rowView.findViewById(R.id.loan_name);
            viewHolder.editTextValue = rowView.findViewById(R.id.loan_amount);
            viewHolder.imageViewShow = rowView.findViewById(R.id.imageShow);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.editTextValue.setTag(position);
        final AccountForDailyTransaction accountForDailyTransaction = getItem(position);
        int tag = Integer.parseInt(viewHolder.editTextValue.getTag().toString().trim());


        if (viewHolder.editTextValue.getText().toString().trim().equals("")) {


            final View finalRowView = rowView;
            rowView.setOnClickListener(v -> {

                assert accountForDailyTransaction != null;
                String text = accountForDailyTransaction.getProgramNameChange().getValidName();
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
            if (withdrawalList.get(tag) != 0 || initial < accountForDailyTransactions.size()) {
                assert accountForDailyTransaction != null;
                if (accountForDailyTransaction.getProgramId() > 200 && accountForDailyTransaction.getProgramId() < 300) {

                    if (accountForDailyTransaction.getDebit() < 0) {
                        viewHolder.editTextValue.setText(String.valueOf(0));
                    } else {
                        viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransaction.getDebit())));
                    }
                    viewHolder.editTextValue.setError(null);
                    viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
                    viewHolder.textViewName.setTag(accountForDailyTransaction.getProgramId());

                    if (Math.round(accountForDailyTransaction.getDebit()) > 0) {
                        viewHolder.imageViewShow.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.imageViewShow.setVisibility(View.INVISIBLE);
                    }
                } else if (accountForDailyTransaction.getProgramId() > 300 && accountForDailyTransaction.getProgramId() < 400) {

                    viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
                    if (accountForDailyTransaction.getDebit() < 0) {
                        viewHolder.editTextValue.setText(String.valueOf(0));
                    } else {
                        viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransaction.getDebit())));
                    }

                    viewHolder.editTextValue.setError(null);
                    viewHolder.textViewName.setTag(accountForDailyTransaction.getProgramId());

                    if (Math.round(accountForDailyTransaction.getDebit()) > 0) {
                        viewHolder.imageViewShow.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.imageViewShow.setVisibility(View.INVISIBLE);
                    }
                }
                initial++;
            }
        }


        assert accountForDailyTransaction != null;

        viewHolder.editTextValue.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, context.getApplicationContext());
                int tag = Integer.parseInt(viewHolder.editTextValue.getTag().toString().trim());
                float withdrawTotal =0;
                for(int i =0 ; i<withdrawalList.size();i++)
                {
                    if(i!=tag)
                    {
                        withdrawTotal+=withdrawalList.get(i);
                    }
                }


                if (!viewHolder.editTextValue.getText().toString().trim().equals("")) {

                    float value = Float.parseFloat(viewHolder.editTextValue.getText().toString().trim());

                    if (accountForDailyTransactions.get(tag).getProgramId() > 200 && accountForDailyTransactions.get(tag).getProgramId() < 300) {

                        if(!accountForDailyTransactions.get(tag).isWithdrawPermission())
                        {
                            viewHolder.editTextValue.setError("Withdrawal is limited to 4 times in a month or 1 time in a week");
                            if (!errorCollection.get(tag)) {
                                errorSetProgram(tag, ERROR_TRUE);
                            }
                        }
                        else if(accountForDailyTransaction.getTermOverDue()==0 && value >20000)
                        {
                            viewHolder.editTextValue.setError("Withdrawal amount can't be more than 20000");
                            if (!errorCollection.get(tag)) {
                                errorSetProgram(tag, ERROR_TRUE);
                            }
                        }
                        else if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {

                            if (value > Math.round(debitList.get(tag) + accountForDailyTransactions.get(tag).getBalance())) {
                                viewHolder.editTextValue.setError("Withdrawal amount can't be more than Savings Balance (" + (debitList.get(tag) + accountForDailyTransactions.get(tag).getBalance()) + ")");
                                if (!errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_TRUE);
                                }

                            }
                            else if(savingsFriendly!=null && savingsFriendly.getSavingFriendlyLoanCount()>0)
                            {
                                if(savingsFriendly.getPrimaryLoanCount()<=0)
                                {
                                    viewHolder.editTextValue.setError("if member have savings-friendly loan then Without primary loan account any withdrawal can't be done in this system");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else if(value > Math.round(totalBalanceWithoutLoan-savingsFriendly.getMaxWithdrawal()-withdrawTotal))
                                {
                                    viewHolder.editTextValue.setError("20% savings must be needed, if member have savings-friendly loan");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else
                                {
                                    viewHolder.editTextValue.setError(null);
                                    errorSetProgram(tag, ERROR_FALSE);
                                }
                            }
                            else if (viewHolder.editTextValue.getError() != null) {
                                if (errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_FALSE);
                                }

                                viewHolder.editTextValue.setError(null);

                            }
                        } else {
                            if (value > (accountForDailyTransactions.get(tag).getBalance())) {
                                viewHolder.editTextValue.setError("Withdrawal amount can't be more than Savings Balance (" + (accountForDailyTransactions.get(tag).getBalance()) + ")");
                                if (!errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_TRUE);
                                }

                            }
                            else if(savingsFriendly!=null && savingsFriendly.getSavingFriendlyLoanCount()>0)
                            {
                                if(savingsFriendly.getPrimaryLoanCount()<=0)
                                {
                                    viewHolder.editTextValue.setError("if member have savings-friendly loan then Without primary loan account any withdrawal can't be done in this system");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else if(value > Math.round(totalBalanceWithoutLoan-savingsFriendly.getMaxWithdrawal()-withdrawTotal))
                                {
                                    viewHolder.editTextValue.setError("20% savings must be needed, if member have savings-friendly loan");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else
                                {
                                    viewHolder.editTextValue.setError(null);
                                    errorSetProgram(tag, ERROR_FALSE);
                                }
                            }
                            else if (viewHolder.editTextValue.getError() != null) {
                                if (errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_FALSE);
                                }

                                viewHolder.editTextValue.setError(null);

                            }
                        }


                    } else if (accountForDailyTransactions.get(tag).getProgramId() > 300 && accountForDailyTransactions.get(tag).getProgramId() < 400) {
                        if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                            if (value > Math.round(debitList.get(tag) + accountForDailyTransactions.get(tag).getBalance())) {
                                viewHolder.editTextValue.setError("Withdrawal amount can't be more than CBS Balance (" + Math.round(debitList.get(tag) + accountForDailyTransactions.get(tag).getBalance()) + ")");
                                if (!errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_TRUE);
                                }
                            }
                            else if(savingsFriendly!=null && savingsFriendly.getSavingFriendlyLoanCount()>0)
                            {
                                if(savingsFriendly.getPrimaryLoanCount()<=0)
                                {
                                    viewHolder.editTextValue.setError("if member have savings-friendly loan then Without primary loan account any withdrawal can't be done in this system");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else if(value > Math.round(totalBalanceWithoutLoan-savingsFriendly.getMaxWithdrawal())-withdrawTotal)
                                {
                                    viewHolder.editTextValue.setError("20% savings must be needed, if member have savings-friendly loan");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else
                                {
                                    viewHolder.editTextValue.setError(null);
                                    errorSetProgram(tag, ERROR_FALSE);
                                }
                            }
                            else if (viewHolder.editTextValue.getError() != null) {
                                if (errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_FALSE);
                                }
                                viewHolder.editTextValue.setError(null);

                            }
                        } else {
                            if (value > (accountForDailyTransactions.get(tag).getBalance())) {
                                viewHolder.editTextValue.setError("Withdrawal amount can't be more than CBS Balance (" + (accountForDailyTransactions.get(tag).getBalance()) + ")");
                                if (!errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_TRUE);
                                }
                            }
                            else if(savingsFriendly!=null && savingsFriendly.getSavingFriendlyLoanCount()>0)
                            {
                                if(savingsFriendly.getPrimaryLoanCount()<=0)
                                {
                                    viewHolder.editTextValue.setError("if member have savings-friendly loan then Without primary loan account any withdrawal can't be done in this system");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else if(value > Math.round(totalBalanceWithoutLoan-savingsFriendly.getMaxWithdrawal()-withdrawTotal))
                                {
                                    viewHolder.editTextValue.setError("20% savings must be needed, if member have savings-friendly loan");
                                    if (!errorCollection.get(tag)) {
                                        errorSetProgram(tag, ERROR_TRUE);
                                    }
                                }
                                else
                                {
                                    viewHolder.editTextValue.setError(null);
                                    errorSetProgram(tag, ERROR_FALSE);
                                }
                            }
                            else if (viewHolder.editTextValue.getError() != null) {
                                if (errorCollection.get(tag)) {
                                    errorSetProgram(tag, ERROR_FALSE);
                                }
                                viewHolder.editTextValue.setError(null);

                            }
                        }


                    }
                    total(tag, value, 2);


                } else if (viewHolder.editTextValue.getText().toString().trim().equals("")) {

                    if (error) {
                        errorSetProgram(tag, ERROR_FALSE);
                    }

                    if (accountForDailyTransactions.get(tag).getDebit() != 0) {
                        total(tag, 0, 2);
                    }
                }
            }


        });


        return rowView;
    }

    private void total(int tag, float value, int type) {

        if (type == 2) {
            withdrawalList.set(tag, value);
            accountForDailyTransactions.get(tag).setDebit(value);
        }
        float total = 0;
        for (int i = 0; i < withdrawalList.size(); i++) {
            total = total + withdrawalList.get(i);
        }
        if (listener != null) {
            listener.onDataChange(total);
        }

    }

    /*private static String titleCase(String givenString) {

        if(givenString.trim().contains(" "))
        {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {
                stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
            }
            return stringBuffer.toString().trim();
        }
        else
        {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }*/


    private void errorSetProgram(int tag, boolean errorCode) {
        errorCollection.set(tag, errorCode);
        for (int i = 0; i < errorCollection.size(); i++) {
            if (errorCollection.get(i)) {
                error = true;
                break;
            } else {
                error = false;

            }
        }

        if (listener != null) {
            if (error) {
                listener.errorSet(true);
            } else {
                listener.errorSet(false);
            }
        }
    }

    private void errorSetProgram() {
        for (int i = 0; i < errorCollection.size(); i++) {
            if (errorCollection.get(i)) {
                error = true;
                break;
            } else {
                error = false;

            }
        }

        if (listener != null) {
            if (error) {
                listener.errorSet(true);
            } else {
                listener.errorSet(false);
            }
        }


    }
}
