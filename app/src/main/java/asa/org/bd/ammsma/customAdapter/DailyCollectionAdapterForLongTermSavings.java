package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

public class DailyCollectionAdapterForLongTermSavings extends ArrayAdapter<AccountForDailyTransaction> {

    private Context context;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions;
    private ArrayList<Float> collectionList = new ArrayList<>();
    private ArrayList<Boolean> errorCollection = new ArrayList<>();
    private DataChangeListener listener;

    private DataSourceRead dataSourceRead;
    private boolean error = false;

    private static boolean ERROR_TRUE = true;
    private static boolean ERROR_FALSE = false;
    private int initial = 0;
    //private ExtraTask extraTask = new ExtraTask();

    public interface DataChangeListener {
        void onDataChange(float totalAmountCollection);

        void errorSet(boolean ltsError);
    }


    public DailyCollectionAdapterForLongTermSavings(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions, DataChangeListener listener) {
        super(context, R.layout.list_view_custom_adapter_daily_collection, accountForDailyTransactions);
        this.accountForDailyTransactions = accountForDailyTransactions;
        this.context = context;


        for (int i = 0; i < accountForDailyTransactions.size(); i++) {
            collectionList.add(i, accountForDailyTransactions.get(i).getCredit());
            errorCollection.add(i, ERROR_FALSE);
        }
        this.listener = listener;
        dataSourceRead = new DataSourceRead(context);
        total(0, 0, 1);
        errorSetProgram();
    }

    private static class ViewHolder {

        TextView textViewName;
        ImageView imageViewShow;
        EditText editTextValue;
    }


    @SuppressLint({"SetTextI18n", "InflateParams"})
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
        assert accountForDailyTransaction != null;
        final int accountId = accountForDailyTransaction.getAccountId();
        int tag = Integer.parseInt(viewHolder.editTextValue.getTag().toString().trim());


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

            if (collectionList.get(tag) != 0 || initial < accountForDailyTransactions.size()) {

                int flag = dataSourceRead.getTransactionHistoryCheck(accountId);
                if (flag == 1) {
                    viewHolder.textViewName.setTextColor(Color.parseColor("#00c853"));
                }

                viewHolder.textViewName.setText(accountForDailyTransaction.getProgramNameChange().getChangedName() + " " + (position + 1)
                        + " (" + Math.round(accountForDailyTransaction.getBalance())
                        + " / " + Math.round(accountForDailyTransaction.getMinimumDeposit())
                        + ")");

                if (flag == 0) {
                    viewHolder.editTextValue.setText(String.valueOf(Math.round(collectionList.get(position))));
                } else if (flag == 1) {
                    viewHolder.editTextValue.setText("0");
                }

                if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                    viewHolder.imageViewShow.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.imageViewShow.setVisibility(View.INVISIBLE);

                }
                initial++;
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
                if(viewHolder.editTextValue.getText().toString().trim().equals("0"))
                {
                    errorSetProgram(tag, ERROR_FALSE);
                    if (accountForDailyTransactions.get(tag).getCredit() != 0) {
                        total(tag, 0, 2);
                    }
                }
                else if (!viewHolder.editTextValue.getText().toString().trim().equals("")) {
                    float value = Float.parseFloat(viewHolder.editTextValue.getText().toString().trim());

                    if (value != accountForDailyTransactions.get(tag).getCredit()) {

                        int flag = dataSourceRead.getTransactionHistoryCheck(accountId);
                        int minimumDeposit = Math.round(accountForDailyTransaction.getMinimumDeposit());
                        if (accountForDailyTransaction.getDuration() == 10 && (accountForDailyTransaction.getBalance() / accountForDailyTransaction.getMinimumDeposit()) >= 120) {
                            if (value == 0) {
                                errorSetProgram(tag, ERROR_FALSE);
                            } else {
                                viewHolder.editTextValue.setError("LTS Payment can't be more than  120 times for 10 years");
                                errorSetProgram(tag, ERROR_TRUE);
                            }

                        } else if (accountForDailyTransaction.getDuration() == 5 && (accountForDailyTransaction.getBalance() / accountForDailyTransaction.getMinimumDeposit()) >= 60) {
                            if (value == 0) {
                                errorSetProgram(tag, ERROR_FALSE);
                            } else {
                                viewHolder.editTextValue.setError("LTS Payment can't be more than  60 times for 5 years");
                                errorSetProgram(tag, ERROR_TRUE);
                            }

                        } else if (accountForDailyTransaction.getDuration() == 5 && accountForDailyTransaction.getMissingLtsCount() > 11) {
                            if (value == 0) {
                                errorSetProgram(tag, ERROR_FALSE);
                            } else {
                                viewHolder.editTextValue.setError("LTS Missing Payment can't be more than  10 times for 5 years");
                                errorSetProgram(tag, ERROR_TRUE);
                            }

                        } else if (accountForDailyTransaction.getDuration() == 10 && accountForDailyTransaction.getMissingLtsCount() > 21) {
                            if (value == 0) {
                                errorSetProgram(tag, ERROR_FALSE);
                            } else {
                                viewHolder.editTextValue.setError("LTS Missing Payment can't be more than  20 times for 10 years");
                                errorSetProgram(tag, ERROR_TRUE);
                            }

                        }
                        else if (accountForDailyTransaction.getDuration() == 3 && accountForDailyTransaction.getMissingLtsCount() > 5) {
                            if (value == 0) {
                                errorSetProgram(tag, ERROR_FALSE);
                            } else {
                                viewHolder.editTextValue.setError("LTS Missing Payment can't be more than  4 times for 3 years");
                                errorSetProgram(tag, ERROR_TRUE);
                            }

                        }else if (flag == 1) {
                            viewHolder.editTextValue.setError("You already gave LTS in this month");

                            errorSetProgram(tag, ERROR_TRUE);
                        } else if (flag == 0 && (value != minimumDeposit)) {

                            if (value == 0) {
                                errorSetProgram(tag, ERROR_FALSE);
                            } else {
                                viewHolder.editTextValue.setError("LTS deposit amount should be minimum deposit amount (" + minimumDeposit + ") or Zero (0)");
                                errorSetProgram(tag, ERROR_TRUE);
                            }

                        } else {
                            errorSetProgram(tag, ERROR_FALSE);
                        }
                        total(tag, value, 2);
                    }


                } else if (viewHolder.editTextValue.getText().toString().trim().equals("")) {


                    if (error) {
                        errorSetProgram(tag, ERROR_FALSE);
                    }
                    errorSetProgram(tag, ERROR_FALSE);

                    if (accountForDailyTransactions.get(tag).getCredit() != 0) {
                        total(tag, 0, 2);
                    }
                }

            }


        });

        return rowView;
    }


    private void total(int tag, float value, int type) {

        if (type == 2) {
            collectionList.set(tag, value);
            accountForDailyTransactions.get(tag).setCredit(value);
        }
        float total = 0;
        for (int i = 0; i < collectionList.size(); i++) {
            total = total + collectionList.get(i);
        }
        if (listener != null) {
            listener.onDataChange(total);
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
