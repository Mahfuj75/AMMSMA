package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
//import asa.org.bd.ammsma.service.ExtraTask;


public class BadDebtAdapterForCollection extends ArrayAdapter<AccountForDailyTransaction> {

    private static boolean ERROR_TRUE = true;
    private static boolean ERROR_FALSE = false;
    private Context context;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions;
    private ArrayList<Float> collectionList = new ArrayList<>();
    private ArrayList<Float> dataChangeList = new ArrayList<>();
    private ArrayList<Boolean> errorCollection = new ArrayList<>();
    private DataChangeListener listener;
    //private ExtraTask extraTask = new ExtraTask();
    private boolean error = false;


    public BadDebtAdapterForCollection(Context context, ArrayList<AccountForDailyTransaction> accountForDailyTransactions, DataChangeListener listener) {
        super(context, R.layout.list_view_custom_adapter_for_bad_debt_collection, accountForDailyTransactions);
        this.accountForDailyTransactions = accountForDailyTransactions;
        this.context = context;


        for (int i = 0; i < accountForDailyTransactions.size(); i++) {
            errorCollection.add(i, ERROR_FALSE);
            collectionList.add(i, accountForDailyTransactions.get(i).getCredit());
            dataChangeList.add(i, accountForDailyTransactions.get(i).getCredit());

        }

        this.listener = listener;
        total(0, 0, 1);
        errorSetProgram();

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

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_view_custom_adapter_for_bad_debt_collection, null);

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

        if (viewHolder.editTextValue.getText().toString().trim().equals("")) {

            if (!error) {
                errorSetProgram(tag, ERROR_FALSE);
            } else {
                errorSetProgram(tag, ERROR_TRUE);
            }

            viewHolder.textViewName.setText(titleCase(accountForDailyTransactions.get(tag).getProgramName()) + "  ( " + 0 + (position + 1) + " )");
            viewHolder.editTextValue.setText(String.valueOf(Math.round(accountForDailyTransactions.get(tag).getCredit())));

            if (accountForDailyTransactions.get(tag).getAccountStatus() == 1) {
                viewHolder.imageViewShow.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imageViewShow.setVisibility(View.INVISIBLE);
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


                        if (value > accountForDailyTransaction.getDebit()) {
                            viewHolder.editTextValue.setError("Bad-Debt collection can't be grater than " + Math.round(accountForDailyTransaction.getDebit()));
                            errorSetProgram(tag, ERROR_TRUE);
                        } else {
                            errorSetProgram(tag, ERROR_FALSE);
                        }
                        total(tag, value, 2);
                    }


                } else if (viewHolder.editTextValue.getText().toString().trim().equals("")) {

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


    public interface DataChangeListener {
        void onDataChange(float totalAmountCollection, ArrayList<Float> dataChangeList);

        void errorSet(boolean collectionError);
    }

    private static class ViewHolder {

        TextView textViewName;
        ImageView imageViewShow;
        EditText editTextValue;
    }
}
