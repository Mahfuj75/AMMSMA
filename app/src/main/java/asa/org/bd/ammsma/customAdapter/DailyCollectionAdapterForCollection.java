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
//import asa.org.bd.ammsma.service.ExtraTask;


public class DailyCollectionAdapterForCollection extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions;
    private ArrayList<Float> collectionList = new ArrayList<>();
    private ArrayList<Float> dataChangeList = new ArrayList<>();
    private ArrayList<Integer> firstTimeList = new ArrayList<>();
    private ArrayList<Boolean> errorCollection = new ArrayList<>();
    private DataChangeListener listener;
    //private ExtraTask extraTask = new ExtraTask();

    private boolean error = false;

    private static boolean ERROR_TRUE = true;
    private static boolean ERROR_FALSE = false;


    private DataSourceRead dataSourceRead;

    public interface DataChangeListener {
        void onDataChange(float totalAmountCollection, ArrayList<Float> dataChangeList);

        void errorSet(boolean collectionError);
    }


    public DailyCollectionAdapterForCollection(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions, DataChangeListener listener) {
        super(context, R.layout.list_view_custom_adapter_daily_collection, accountForDailyTransactions);
        this.accountForDailyTransactions = accountForDailyTransactions;
        this.context = context;


        for (int i = 0; i < accountForDailyTransactions.size(); i++) {
            errorCollection.add(i, ERROR_FALSE);
            if (accountForDailyTransactions.get(i).getAccountStatus() == 1) {
                collectionList.add(i, accountForDailyTransactions.get(i).getCredit());

            } else {
                collectionList.add(i, accountForDailyTransactions.get(i).getBaseInstallmentAmount());
            }
            dataChangeList.add(i, accountForDailyTransactions.get(i).getCredit());
            firstTimeList.add(0);

        }
        dataSourceRead = new DataSourceRead(context);
        this.listener = listener;
        total(0, 0, 1);
        errorSetProgram();

    }

    private static class ViewHolder {

        TextView textViewName;
        ImageView imageViewShow;
        EditText editTextValue;
    }


    @NonNull
    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {

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
        assert accountForDailyTransaction != null;
        final int programId = accountForDailyTransaction.getProgramId();
        final int accountId = accountForDailyTransaction.getAccountId();


        if (viewHolder.editTextValue.getText().toString().trim().equals("")) {


            final View finalRowView = rowView;
            rowView.setOnClickListener(v -> {
                String text = accountForDailyTransaction.getProgramNameChange().getValidName();
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

            if (error) {
                errorSetProgram(tag, ERROR_FALSE);
            }


            if (programId > 100 && programId < 200) {



                if(dataSourceRead.hasNewPrimaryLoan(accountForDailyTransaction.getMemberId(),accountForDailyTransaction.getAccountId()))
                {
                    viewHolder.editTextValue.setEnabled(false);
                }
                else
                {
                    viewHolder.editTextValue.setEnabled(true);
                }
                if (accountForDailyTransaction.getSupplementary()) {
                    viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName() + " S ");
                } else {
                    viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
                }


                if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                    viewHolder.imageViewShow.setVisibility(View.VISIBLE);
                    viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getRealizedToday())));
                } else {
                    viewHolder.imageViewShow.setVisibility(View.INVISIBLE);
                    viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getInstallmentAmount())));
                }
                if (firstTimeList.get(tag) == 0) {
                    total(tag, Float.parseFloat(viewHolder.editTextValue.getText().toString().trim()), 2);
                    firstTimeList.add(tag, 1);
                }

            } else if (programId > 200 && programId < 300) {
                if (programId != 204) {
                    viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
                    viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getCredit())));
                    if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                        viewHolder.imageViewShow.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.imageViewShow.setVisibility(View.INVISIBLE);
                        viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getMinimumDeposit())));
                    }

                    if (firstTimeList.get(tag) == 0) {
                        total(tag, Float.parseFloat(viewHolder.editTextValue.getText().toString().trim()), 2);
                        firstTimeList.add(tag, 1);
                    }


                }


            } else if (programId > 300 && programId < 400) {
                viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName());
                viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getCredit())));


                if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                    viewHolder.imageViewShow.setVisibility(View.VISIBLE);
                } else {
                    if (accountForDailyTransaction.getBalance() + accountForDailyTransactions.get(tag).getMinimumDeposit() > 4000) {
                        int value = (int) (4000 - accountForDailyTransaction.getBalance());
                        if (value < 0) {
                            value = 0;
                        }
                        viewHolder.editTextValue.setText(String.valueOf(value));
                    } else {
                        viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getMinimumDeposit())));
                    }

                    viewHolder.imageViewShow.setVisibility(View.INVISIBLE);
                }

                if (firstTimeList.get(tag) == 0) {
                    total(tag, Float.parseFloat(viewHolder.editTextValue.getText().toString().trim()), 2);
                    firstTimeList.add(tag, 1);
                }

            }


        }

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
                if (!viewHolder.editTextValue.getText().toString().trim().equals("")) {
                    float value = Float.parseFloat(viewHolder.editTextValue.getText().toString().trim());

                    if (value != collectionList.get(tag) || value == 0) {

                        if (programId > 100 && programId < 200) {

                            int initialOutstandingBalance = dataSourceRead.getInitialBalanceForLoan(accountId);
                            int maxAmount = dataSourceRead.getMaxAmountForLoanCollection(accountId);
                            if ((accountForDailyTransaction.getAccountStatus() == 0 && accountForDailyTransaction.getBalance() == value) || (accountForDailyTransaction.getAccountStatus() == 1 && value == Math.round(accountForDailyTransaction.getBalance() + accountForDailyTransaction.getRealizedToday()))) {
                                errorSetProgram(tag, ERROR_FALSE);
                                viewHolder.editTextValue.setError(null);


                            } else if (value <= initialOutstandingBalance - accountForDailyTransaction.getRealizedPrevious()) {


                                if (value <= accountForDailyTransaction.getTermOverDue() - accountForDailyTransaction.getRealizedPrevious() && accountForDailyTransaction.getTermOverDue() > 0) {
                                    errorSetProgram(tag, ERROR_FALSE);
                                } else if (value > maxAmount) {

                                    viewHolder.editTextValue.setError("You can not give more than one Installment advance.");
                                    errorSetProgram(tag, ERROR_TRUE);
                                } else {
                                    errorSetProgram(tag, ERROR_FALSE);
                                }

                            } else {
                                viewHolder.editTextValue.setError("Deposit amount can't be more than Outstanding Balance.");
                                errorSetProgram(tag, ERROR_TRUE);
                            }


                        } else if (programId > 300 && programId < 400) {
                            if ((value + accountForDailyTransaction.getBalance()) > 4000) {
                                if (value <= accountForDailyTransactions.get(tag).getCredit() && accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                                    errorSetProgram(tag, ERROR_FALSE);
                                } else if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                                    viewHolder.editTextValue.setError("CBS Deposit Amount "
                                            + value
                                            + " causes the total CBS balance "
                                            + Math.round(((value - accountForDailyTransactions.get(tag).getCredit()) + accountForDailyTransaction.getBalance()))
                                            + " to exceed the balance limit 4000");
                                } else {
                                    viewHolder.editTextValue.setError("CBS Deposit Amount "
                                            + value
                                            + " causes the total CBS balance "
                                            + Math.round((value + accountForDailyTransaction.getBalance()))
                                            + " to exceed the balance limit 4000");

                                    errorSetProgram(tag, ERROR_TRUE);
                                }

                            }
                            else if(value!=0)
                            {
                                if(accountForDailyTransaction.getInstallmentType()==1 && value !=10)
                                {
                                    viewHolder.editTextValue.setError("CBS Deposit Amount could not be more or less than 10 tk for weekly CBS account");

                                    errorSetProgram(tag, ERROR_TRUE);
                                }
                                else if(accountForDailyTransaction.getInstallmentType()==2 && value !=50)
                                {
                                    viewHolder.editTextValue.setError("CBS Deposit Amount could not be more or less than 50 tk for monthly CBS account");

                                    errorSetProgram(tag, ERROR_TRUE);
                                }
                                else if((accountForDailyTransaction.getInstallmentType()==1 && value ==10) ||(accountForDailyTransaction.getInstallmentType()==2 && value ==50))
                                {
                                    errorSetProgram(tag, ERROR_FALSE);
                                }

                            }
                            else {
                                errorSetProgram(tag, ERROR_FALSE);
                            }
                        }
                        total(tag, value, 2);
                    }


                } else if (viewHolder.editTextValue.getText().toString().trim().equals("")) {

                    errorSetProgram(tag, ERROR_FALSE);
                    total(tag, 0, 2);
                }

            }


        });


        return rowView;
    }


    private void total(int tag, float value, int type) {

        if (type == 2) {
            collectionList.set(tag, value);
            dataChangeList.set(tag, value);

        }
        float total = 0;
        for (int i = 0; i < collectionList.size(); i++) {
            total = total + collectionList.get(i);
        }
        if (listener != null) {
            listener.onDataChange(total, dataChangeList);
        }

    }


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
