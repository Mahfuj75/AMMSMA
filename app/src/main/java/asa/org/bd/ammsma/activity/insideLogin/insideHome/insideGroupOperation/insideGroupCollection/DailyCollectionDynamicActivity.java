package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.insideGroupCollection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.customAdapter.CustomAdapterForAccountDetails;
import asa.org.bd.ammsma.customAdapter.DailyCollectionAdapterForAccountInfo;
import asa.org.bd.ammsma.customAdapter.DailyCollectionAdapterForCollection;
import asa.org.bd.ammsma.customAdapter.DailyCollectionAdapterForLongTermSavings;
import asa.org.bd.ammsma.customAdapter.DailyCollectionAdapterForMembersAccountList;
import asa.org.bd.ammsma.customAdapter.DailyCollectionAdapterForWithdrawal;
import asa.org.bd.ammsma.customAdapter.DailyCollectionCustomAdapterForExemption;
import asa.org.bd.ammsma.customAdapter.SpinnerCustomAdapter;
import asa.org.bd.ammsma.customListView.CustomNonScrollerListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.extra.DynamicFieldForListViewObject;
import asa.org.bd.ammsma.extra.MemberDetailsInfo;
import asa.org.bd.ammsma.extra.MemberListInfo;
import asa.org.bd.ammsma.extra.SavingsFriendly;
import asa.org.bd.ammsma.extra.TransactionHistory;
import asa.org.bd.ammsma.service.ExtraTask;

public class DailyCollectionDynamicActivity extends AppCompatActivity {

    private int memberId;

    private float collectionSum=0;
    private float withdrawalSum=0;
    private float exemptionSum =0;
    private float longTermSavingsSum =0;
    private float totalCollectionForDynamic =0;
    private boolean loadDataSuccessful = false;

    private ArrayList<Float> dataChangeListCollectionMain = new ArrayList<>();
    private ArrayList<Boolean> checkWithdrawalMain = new ArrayList<>();


    private Spinner spinnerMember;

    private DataSourceRead dataSourceRead;
    private DataSourceWrite dataSourceWrite;

    private CustomNonScrollerListView dailyCollectionAccountLoanListView;
    private CustomNonScrollerListView dailyCollectionSavingsListView;
    private CustomNonScrollerListView collectionListView;
    private CustomNonScrollerListView withdrawalListView;
    private CustomNonScrollerListView longTermCollectionList;
    private CustomNonScrollerListView exemptionListView;

    private TextView textViewNetAmount;

    private Button buttonMemberDetails;

    private DynamicFieldForListViewObject dynamicFieldForListViewObject;
    private ArrayList<MemberDetailsInfo> memberAccountDetails;


    private boolean aCollectionError = false;
    private boolean aLtsError = false;
    private boolean aWithdrawalError = false;
    private boolean mainError = false;
    private MemberListInfo memberListInfo = new MemberListInfo();



    private int groupId;
    private int programOfficerId;
    private String realGroupName;


    private int memberPosition;
    private ImageButton imageButtonLeft;
    private ImageButton imageButtonRight;
    private List<Integer> savedData = new ArrayList<>();
    private List<Integer> finalSavedData = new ArrayList<>();



    private ScrollView scrollViewDailyCollection;
    private TextView textViewNoAccountInformation;
    private LinearLayout linearLayoutAccountStatus;
    private LinearLayout linearLayoutCollectionAndLts;
    private LinearLayout linearLayoutCollection;
    private LinearLayout linearLayoutWithdrawal;
    private LinearLayout linearLayoutWithdrawalAndExemption;
    private LinearLayout linearLayoutLtsCollection;
    private LinearLayout linearLayoutExemption;

    private boolean firstTimeBackButtonWork = false;
    private boolean saveButtonFreeze = false;
    private ExtraTask extraTask = new ExtraTask();
    private boolean spinnerSelectAuto = false;
    private boolean firstTime = true;
    private float previousSavingCollection =0;
    private float recentSavingCollection =0;
    private float totalWithoutLts=0;
    private SavingsFriendly savingsFriendly;
    private double totalBalanceWithoutLoan=0;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_collection_dynamic);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);


        imageButtonLeft =  findViewById(R.id.imageButtonLeft);
        imageButtonRight =  findViewById(R.id.imageButtonRight);
        TextView textViewProgramOfficerName =  findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName =  findViewById(R.id.textView_groupName);




        dailyCollectionAccountLoanListView =  findViewById(R.id.daily_collection_Account_loan_listView);
        dailyCollectionAccountLoanListView.setEnabled(false);
        dailyCollectionSavingsListView =  findViewById(R.id.daily_collection_Account_savings_listView);
        dailyCollectionSavingsListView.setEnabled(false);
        collectionListView =  findViewById(R.id.listView_daily_collection);
        collectionListView.setEnabled(false);
        withdrawalListView =  findViewById(R.id.listView_daily_withdrawal);
        withdrawalListView.setEnabled(false);
        longTermCollectionList =  findViewById(R.id.listView_lts_collection);
        longTermCollectionList.setEnabled(false);
        exemptionListView =  findViewById(R.id.listView_exemption);
        exemptionListView.setEnabled(false);
        exemptionListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        spinnerMember =  findViewById(R.id.spinner_members);

        textViewNetAmount =  findViewById(R.id.textView_net_amount);
        buttonMemberDetails =  findViewById(R.id.buttonMemberDetails);
        Button buttonSaveToDatabase =  findViewById(R.id.buttonSave);
        Button buttonClose = findViewById(R.id.buttonClose);

        scrollViewDailyCollection = findViewById(R.id.scrollViewDailyCollection);
        textViewNoAccountInformation = findViewById(R.id.textViewNoAccountInformation);
        linearLayoutAccountStatus = findViewById(R.id.linearLayoutAccountStatus);
        linearLayoutCollectionAndLts = findViewById(R.id.linearLayoutCollectionAndLts);
        linearLayoutCollection = findViewById(R.id.linearLayoutCollection);
        linearLayoutWithdrawal = findViewById(R.id.linearLayoutWithdrawal);
        linearLayoutWithdrawalAndExemption = findViewById(R.id.linearLayoutWithdrawalAndExemption);
        linearLayoutLtsCollection = findViewById(R.id.linearLayoutLtsCollection);
        linearLayoutExemption = findViewById(R.id.linearLayoutExemption);





        dataSourceRead = new DataSourceRead(getApplicationContext());
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        groupId = getIntent().getIntExtra("groupId", 0);
        memberPosition = getIntent().getIntExtra("position", 0);
        String loginId = getIntent().getStringExtra("loginId");
        String groupName = getIntent().getStringExtra("groupName");
        realGroupName = getIntent().getStringExtra("realGroupName");
        memberId = getIntent().getIntExtra("memberId",0);

        savingsFriendly = dataSourceRead.savingFriendlyLoanValidation(memberId);

        if(dataSourceRead.checkLoan(memberId)){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyCollectionDynamicActivity.this);

            builder.setMessage(
                    "This member does not have any active lone for more than 6 months.")
                    .setCancelable(false)
                    .setTitle("Warning")
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            saveButtonFreeze = false;
        }




        if (programOfficerId != -1)
        {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) +" - "+ loginId);
            textViewGroupName.setText(groupName);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }


        AsyncTaskForSpinnerData asyncTaskForSpinnerData = new AsyncTaskForSpinnerData();
        asyncTaskForSpinnerData.execute();


        buttonSaveToDatabase.setOnClickListener(v -> {

            if(!saveButtonFreeze)
            {
                saveButtonFreeze = true;
                if(generalSavingsBalanceMoreThan60Check())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            DailyCollectionDynamicActivity.this);

                    builder.setMessage(
                            "General Savings Balance is  more than 60% against Loan")
                            .setCancelable(false)
                            .setTitle("Warning")
                            .setPositiveButton("Ok",(dialog, which) ->dataSaveOperation())
                            .setNegativeButton("Cancel",((dialog, which) -> dialog.dismiss()));
                    AlertDialog alert = builder.create();
                    alert.show();
                    saveButtonFreeze = false;
                }
                else
                {
                    dataSaveOperation();
                }
                //
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            }

        });

        buttonClose.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.putIntegerArrayListExtra("SavedDataValue", (ArrayList<Integer>) finalSavedData);
            setResult(RESULT_OK, intent);
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop,getApplicationContext());
            finish();
        });




        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign,getApplicationContext());
    }


    private boolean loadData()
    {
        memberListInfo = dataSourceRead.getAllMembersForDynamicSpinner(groupId);

        for(int i=0; i<memberListInfo.getMembersInfo().size();i++)
        {
            savedData.add(i,0);
        }

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initialDataSet(boolean buttonSave) {

        aCollectionError = false;
        aLtsError = false;
        aWithdrawalError = false;
        mainError = false;
        checkWithdrawalMain= new ArrayList<>();





        AsyncTaskForInitialData asyncTaskForInitialData = new AsyncTaskForInitialData(buttonSave);
        asyncTaskForInitialData.execute();


    }

    private boolean  generalSavingsBalanceMoreThan60Check()
    {
        if(!dynamicFieldForListViewObject.getLoanList().isEmpty() && !dynamicFieldForListViewObject.getSavingsList().isEmpty())
        {

            for(int i=0;i<dynamicFieldForListViewObject.getAccountWithoutLong().size();i++)
            {
                if(dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()>200 && dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()<300
                        && dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()!=204
                        && Math.round(dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getBalance()+ dataChangeListCollectionMain.get(i))>(dynamicFieldForListViewObject.getLoanList().get(0).getDisbursedAmount()*0.6) && dataChangeListCollectionMain.get(i)>0)
                {
                    return  true;
                }

            }
        }
        return false;
    }



    private void  dataSaveOperation()
    {

        boolean exemptionCheck = false;

        for(int i=0 ; i<dynamicFieldForListViewObject.getLoanList().size() ; i++) {
            AccountForDailyTransaction accountForDailyTransaction = dynamicFieldForListViewObject.getLoanList().get(i);
            if(dynamicFieldForListViewObject.getLoanList().get(i).getLoanTransactionAmount()>0)
            {
                if (!checkWithdrawalMain.get(i)
                        && ((accountForDailyTransaction.getAccountStatus() == 0 && accountForDailyTransaction.getBalance() == dataChangeListCollectionMain.get(i))
                        || (accountForDailyTransaction.getAccountStatus() == 1
                        && dataChangeListCollectionMain.get(i) == Math.round(accountForDailyTransaction.getBalance() + accountForDailyTransaction.getCredit())))) {
                    exemptionCheck = true;
                }
            }

        }

        if(memberListInfo.getMembersInfo().get(spinnerMember.getSelectedItemPosition()).getMemberOldOrNew().equals("New")
                && memberListInfo.getMembersInfo().get(spinnerMember.getSelectedItemPosition()).getAdmissionDateInteger()
                != new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyCollectionDynamicActivity.this);

            builder.setMessage(
                    "Unable to Save data. Day-Closed new-member's transaction data can't be saved in this system")
                    .setCancelable(false)
                    .setTitle("Error")
                    .setPositiveButton("Ok",
                            (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
            saveButtonFreeze = false;
        }
        else if(exemptionCheck)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyCollectionDynamicActivity.this);

            builder.setMessage(
                    "Unable to Save data. Full-Paid member's exemption is required")
                    .setCancelable(false)
                    .setTitle("Warning")
                    .setPositiveButton("Ok",
                            (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
            saveButtonFreeze = false;
        }
        else if(!mainError)
        {
            float totalWithdrawal = 0;
            for(int i=0 ; i<dynamicFieldForListViewObject.getAccountWithdrawal().size() ; i++)
            {
                View view = withdrawalListView.getAdapter().getView(i,null,null);
                EditText editText =  view.findViewById(R.id.loan_amount);

                float collect =0;
                if(!editText.getText().toString().trim().isEmpty())
                {
                    collect = Float.parseFloat(editText.getText().toString().trim());
                }
                totalWithdrawal+=collect;


            }
            if(savingsFriendly!=null  && savingsFriendly.getSavingFriendlyLoanCount()>0  && (totalBalanceWithoutLoan+(recentSavingCollection-previousSavingCollection)-totalWithdrawal)<savingsFriendly.getMaxWithdrawal() && totalWithdrawal >0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        DailyCollectionDynamicActivity.this);

                builder.setMessage(
                        "Unable to Save data. for savings-friendly loan there must be have 20% savings")
                        .setCancelable(false)
                        .setTitle("Error")
                        .setPositiveButton("Ok",
                                (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
                saveButtonFreeze = false;
            }
            else
            {
                if(savedData.get(spinnerMember.getSelectedItemPosition())==0)
                {
                    finalSavedData.add(spinnerMember.getSelectedItemPosition());
                    savedData.set(spinnerMember.getSelectedItemPosition(),1);
                }
                Calendar calendar = Calendar.getInstance();


                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                df.setTimeZone(TimeZone.getTimeZone("GMT+6"));

                String currentDateTime = df.format(calendar.getTime());
                for(int i=0;i<dynamicFieldForListViewObject.getAccountWithoutLong().size();i++)
                {
                    AccountForDailyTransaction accountForDailyTransactionWithOutLts = dynamicFieldForListViewObject.getAccountWithoutLong().get(i);

                    if(dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()>100 && dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()<200 )
                    {
                        dataSourceWrite.insertOrUpdateAccountBalanceCredit(accountForDailyTransactionWithOutLts, dataChangeListCollectionMain.get(i),currentDateTime,1,programOfficerId);
                    }
                    else if(dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()>200 && dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()<300  && dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()!=204)
                    {
                        dataSourceWrite.insertOrUpdateAccountBalanceCredit(accountForDailyTransactionWithOutLts, dataChangeListCollectionMain.get(i),currentDateTime,2,programOfficerId);
                    }
                    else if(dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()>300 && dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramId()<400 )
                    {
                        dataSourceWrite.insertOrUpdateAccountBalanceCredit(accountForDailyTransactionWithOutLts, dataChangeListCollectionMain.get(i),currentDateTime,4,programOfficerId);
                    }

                }
                for(int i=0 ; i<dynamicFieldForListViewObject.getAccountOnlyLong().size();i++)
                {
                    View view = longTermCollectionList.getAdapter().getView(i,null,null);
                    EditText editText =  view.findViewById(R.id.loan_amount);
                    float collect =0;
                    if(!editText.getText().toString().trim().isEmpty())
                    {
                        collect = Float.parseFloat(editText.getText().toString().trim());
                    }


                    if(dynamicFieldForListViewObject.getAccountOnlyLong().get(i).getProgramId()==204)
                    {
                        dataSourceWrite.insertOrUpdateAccountBalanceCredit(dynamicFieldForListViewObject.getAccountOnlyLong().get(i),collect,currentDateTime,2,programOfficerId);
                    }


                }
                for(int i=0 ; i<dynamicFieldForListViewObject.getAccountWithdrawal().size() ; i++)
                {
                    View view = withdrawalListView.getAdapter().getView(i,null,null);
                    EditText editText =  view.findViewById(R.id.loan_amount);

                    float collect =0;
                    if(!editText.getText().toString().trim().isEmpty())
                    {
                        collect = Float.parseFloat(editText.getText().toString().trim());
                    }
                    if(dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getProgramId()>200 && dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getProgramId()<300  && dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getProgramId()!=204)
                    {
                        dataSourceWrite.insertOrUpdateAccountBalanceDebit(dynamicFieldForListViewObject.getAccountWithdrawal().get(i),collect,currentDateTime,2,programOfficerId);
                    }
                    else if(dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getProgramId()>300 && dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getProgramId()<400 )
                    {
                        dataSourceWrite.insertOrUpdateAccountBalanceDebit(dynamicFieldForListViewObject.getAccountWithdrawal().get(i),collect,currentDateTime,4,programOfficerId);
                    }

                }
                for(int i=0 ; i<dynamicFieldForListViewObject.getLoanList().size() ; i++)
                {
                    AccountForDailyTransaction accountForDailyTransaction = dynamicFieldForListViewObject.getLoanList().get(i);
                    if(checkWithdrawalMain.get(i)
                            && ((accountForDailyTransaction.getAccountStatus()==0 && accountForDailyTransaction.getBalance()== dataChangeListCollectionMain.get(i))
                            || (accountForDailyTransaction.getAccountStatus()==1
                            &&  dataChangeListCollectionMain.get(i) == Math.round(accountForDailyTransaction.getBalance()+ accountForDailyTransaction.getCredit()))))
                    {
                        dataSourceWrite.updateExemptionData(accountForDailyTransaction.getAccountId(),true);
                    }

                    else if(checkWithdrawalMain.get(i)
                            && ((accountForDailyTransaction.getAccountStatus()==0 && accountForDailyTransaction.getBalance()!= dataChangeListCollectionMain.get(i))
                            || (accountForDailyTransaction.getAccountStatus()==1
                            &&  dataChangeListCollectionMain.get(i) != Math.round(accountForDailyTransaction.getBalance()+ accountForDailyTransaction.getCredit()))))
                    {
                        dataSourceWrite.updateExemptionData(accountForDailyTransaction.getAccountId(),false);
                    }
                    else if(!checkWithdrawalMain.get(i))
                    {
                        dataSourceWrite.updateExemptionData(accountForDailyTransaction.getAccountId(),false);
                    }

                }
                dataSourceWrite.insertTempData(groupId,realGroupName);

                initialDataSet(true);
                memberListInfo = dataSourceRead.getAllMembersForDynamicSpinner(groupId);
                int memberSelection = spinnerMember.getSelectedItemPosition();
                spinnerMember.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListDailyCollection(getApplicationContext(),memberListInfo.getMembersName(),memberListInfo));
                spinnerMember.setSelection(memberSelection);
            }






        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    DailyCollectionDynamicActivity.this);

            builder.setMessage(
                    "Unable to Save data. Please correct the errors and try again.")
                    .setCancelable(false)
                    .setTitle("Error")
                    .setPositiveButton("Ok",
                            (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
            saveButtonFreeze = false;
        }

    }



    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForSpinnerData extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            loadDataSuccessful = loadData();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(loadDataSuccessful)
            {
                spinnerMember.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListDailyCollection(getApplicationContext(),memberListInfo.getMembersName(),memberListInfo));
                spinnerMember.setSelection(memberPosition);

                spinnerSelectAuto = true;


                //initialDataSet(false);

                spinnerMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
                        memberId = memberListInfo.getMembersInfo().get(position).getId();

                        if(!spinnerSelectAuto || firstTime)
                        {
                            initialDataSet(false);
                            spinnerSelectAuto = false;
                            firstTime = false;
                        }
                        else
                        {
                            spinnerSelectAuto = false;
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                imageButtonLeft.setOnClickListener(v -> {
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
                    if((spinnerMember.getSelectedItemPosition()-1)>=0)
                    {
                        memberId = memberListInfo.getMembersInfo().get(spinnerMember.getSelectedItemPosition()-1).getId();
                        spinnerMember.setSelection(spinnerMember.getSelectedItemPosition()-1);
                        totalCollectionForDynamic=0;
                        collectionSum=0;
                        exemptionSum =0;
                        longTermSavingsSum=0;
                        withdrawalSum=0;
                        checkWithdrawalMain= new ArrayList<>();
                        initialDataSet(false);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No More Members",Toast.LENGTH_LONG).show();
                    }

                });
                imageButtonRight.setOnClickListener(v -> {
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
                    if((spinnerMember.getSelectedItemPosition()+1)<memberListInfo.getMembersInfo().size())
                    {
                        memberId = memberListInfo.getMembersInfo().get(spinnerMember.getSelectedItemPosition()+1).getId();
                        spinnerMember.setSelection(spinnerMember.getSelectedItemPosition()+1);
                        totalCollectionForDynamic=0;
                        collectionSum=0;
                        exemptionSum =0;
                        longTermSavingsSum=0;
                        withdrawalSum=0;
                        checkWithdrawalMain= new ArrayList<>();
                        initialDataSet(false);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"No More Members" ,Toast.LENGTH_LONG).show();
                    }
                });





                loadDataSuccessful=false;
                /*dialog.dismiss();*/
            }

        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForInitialData extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private boolean buttonSave;


        AsyncTaskForInitialData(boolean buttonSave) {
            this.buttonSave = buttonSave;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            firstTimeBackButtonWork= false;

            dialog = new Dialog(DailyCollectionDynamicActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dynamicFieldForListViewObject = dataSourceRead.getAccountForAccountInfo(memberId);
            firstTimeBackButtonWork = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            if(dynamicFieldForListViewObject.getLoanAndCbsList().isEmpty() && dynamicFieldForListViewObject.getLoanList().isEmpty() && dynamicFieldForListViewObject.getAccountWithdrawal().isEmpty()
                    && dynamicFieldForListViewObject.getAccountOnlyLong().isEmpty() && dynamicFieldForListViewObject.getAccountWithoutLong().isEmpty() && dynamicFieldForListViewObject.getSavingsList().isEmpty())
            {
                textViewNoAccountInformation.setVisibility(View.VISIBLE);
                scrollViewDailyCollection.setVisibility(View.GONE);
            }
            else
            {
                textViewNoAccountInformation.setVisibility(View.GONE);
                scrollViewDailyCollection.setVisibility(View.VISIBLE);
            }

            if(dynamicFieldForListViewObject.getLoanAndCbsList().isEmpty() && dynamicFieldForListViewObject.getSavingsList().isEmpty())
            {
                linearLayoutAccountStatus.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutAccountStatus.setVisibility(View.VISIBLE);
            }


            if(dynamicFieldForListViewObject.getAccountWithoutLong().isEmpty() && dynamicFieldForListViewObject.getAccountOnlyLong().isEmpty())
            {
                linearLayoutCollectionAndLts.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutCollectionAndLts.setVisibility(View.VISIBLE);
            }

            if(dynamicFieldForListViewObject.getAccountWithoutLong().isEmpty())
            {
                linearLayoutCollection.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutCollection.setVisibility(View.VISIBLE);
            }

            if(dynamicFieldForListViewObject.getAccountWithdrawal().isEmpty())
            {
                linearLayoutWithdrawal.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutWithdrawal.setVisibility(View.VISIBLE);
            }

            if(dynamicFieldForListViewObject.getAccountOnlyLong().isEmpty())
            {
                linearLayoutLtsCollection.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutLtsCollection.setVisibility(View.VISIBLE);
            }





            if(dynamicFieldForListViewObject.getLoanList().isEmpty())
            {
                linearLayoutExemption.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutExemption.setVisibility(View.VISIBLE);
            }

            if(dynamicFieldForListViewObject.getAccountWithdrawal().isEmpty() && dynamicFieldForListViewObject.getLoanList().isEmpty())
            {
                linearLayoutWithdrawalAndExemption.setVisibility(View.INVISIBLE);
            }
            else
            {
                linearLayoutWithdrawalAndExemption.setVisibility(View.VISIBLE);
            }


            for(int i=0; i<dynamicFieldForListViewObject.getAccountWithoutLong().size();i++)
            {
                if(dataChangeListCollectionMain.isEmpty())
                {
                    dataChangeListCollectionMain.add(i,dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getCredit());
                }
                else if(dataChangeListCollectionMain.size()<i+1)
                {
                    dataChangeListCollectionMain.add(i,dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getCredit());
                }




            }

            for(int i=0; i<dynamicFieldForListViewObject.getLoanList().size();i++)
            {


                if(checkWithdrawalMain.isEmpty())
                {
                    checkWithdrawalMain.add(i,dynamicFieldForListViewObject.getLoanList().get(i).isExemptedOrNot());
                }
                else if(checkWithdrawalMain.size()<i+1)
                {
                    checkWithdrawalMain.add(i,dynamicFieldForListViewObject.getLoanList().get(i).isExemptedOrNot());
                }

            }

            final DailyCollectionAdapterForAccountInfo dailyCollectionAdapterForAccountInfoLoanAndCbs
                    = new DailyCollectionAdapterForAccountInfo(getApplicationContext(), dynamicFieldForListViewObject.getLoanAndCbsList()) ;
            dailyCollectionAccountLoanListView.setAdapter( dailyCollectionAdapterForAccountInfoLoanAndCbs);
            final DailyCollectionAdapterForAccountInfo dailyCollectionAdapterForAccountInfoForSavings
                    = new DailyCollectionAdapterForAccountInfo(getApplicationContext(),dynamicFieldForListViewObject.getSavingsList());
            dailyCollectionSavingsListView.setAdapter(dailyCollectionAdapterForAccountInfoForSavings);

            previousSavingCollection=0;
            totalBalanceWithoutLoan=0;
            for (int i=0 ; i<dynamicFieldForListViewObject.getAccountWithdrawal().size();i++)
            {
                previousSavingCollection+= dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getCredit();
                totalBalanceWithoutLoan+=dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getBalance()+dynamicFieldForListViewObject.getAccountWithdrawal().get(i).getDebit();
            }
            for (int i=0 ; i<dynamicFieldForListViewObject.getAccountOnlyLong().size();i++)
            {
                previousSavingCollection+= dynamicFieldForListViewObject.getAccountOnlyLong().get(i).getCredit();
                totalBalanceWithoutLoan+=dynamicFieldForListViewObject.getAccountOnlyLong().get(i).getBalance()+dynamicFieldForListViewObject.getAccountOnlyLong().get(i).getDebit();
            }



            DailyCollectionAdapterForCollection dailyCollectionAdapterForCollection
                    = new DailyCollectionAdapterForCollection(getApplicationContext(), dynamicFieldForListViewObject.getAccountWithoutLong()
                    , new DailyCollectionAdapterForCollection.DataChangeListener()
            {
                @Override
                public void onDataChange(float totalAmountCollection,ArrayList<Float> dataChangeList ) {
                    collectionSum =0;
                    collectionSum = totalAmountCollection;
                    totalCollectionForDynamic = ( collectionSum + longTermSavingsSum ) - ( withdrawalSum + exemptionSum);
                    textViewNetAmount.setText(String.valueOf(Math.round(totalCollectionForDynamic)));
                    totalWithoutLts =0;
                    for(int i =0 ; i<dataChangeList.size();i++)
                    {
                        dataChangeListCollectionMain.set(i,dataChangeList.get(i));

                        if(dynamicFieldForListViewObject.getAccountWithoutLong().get(i).getProgramTypeId()!=1)
                        {
                            totalWithoutLts+=dataChangeList.get(i);

                        }
                    }
                    recentSavingCollection = totalWithoutLts+ longTermSavingsSum;
                }


                @Override
                public void errorSet(boolean collectionError) {


                    aCollectionError = collectionError;

                    mainError = !(!aLtsError && !aWithdrawalError && !aCollectionError);


                }
            });

            collectionListView.setAdapter(dailyCollectionAdapterForCollection);

            DailyCollectionAdapterForWithdrawal dailyCollectionAdapterForWithdrawal
                    = new DailyCollectionAdapterForWithdrawal(getApplicationContext(), dynamicFieldForListViewObject.getAccountWithdrawal(),savingsFriendly, dynamicFieldForListViewObject.getAccountOnlyLong()
                    , new DailyCollectionAdapterForWithdrawal.DataChangeListener()
            {
                @Override
                public void onDataChange(float totalAmountCollection) {
                    withdrawalSum = 0;
                    withdrawalSum = totalAmountCollection;
                    totalCollectionForDynamic = ( collectionSum + longTermSavingsSum ) - ( withdrawalSum + exemptionSum);
                    textViewNetAmount.setText(String.valueOf(Math.round(totalCollectionForDynamic)));
                }

                @Override
                public void errorSet(boolean withdrawalError) {

                    aWithdrawalError= withdrawalError;

                    mainError = !(!aLtsError && !aWithdrawalError && !aCollectionError);

                }
            });


            withdrawalListView.setAdapter(dailyCollectionAdapterForWithdrawal);
            DailyCollectionAdapterForLongTermSavings dailyCollectionAdapterForLongTermSavings
                    = new DailyCollectionAdapterForLongTermSavings(getApplicationContext(), dynamicFieldForListViewObject.getAccountOnlyLong()
                    , new DailyCollectionAdapterForLongTermSavings.DataChangeListener()
            {
                @Override
                public void onDataChange(float totalAmountCollection) {
                    longTermSavingsSum=0;
                    longTermSavingsSum = totalAmountCollection;
                    totalCollectionForDynamic = ( collectionSum + longTermSavingsSum ) - ( withdrawalSum + exemptionSum);
                    textViewNetAmount.setText(String.valueOf(Math.round(totalCollectionForDynamic)));


                }

                @Override
                public void errorSet(boolean ltsError) {
                    aLtsError = ltsError;
                    mainError = !(!aLtsError && !aWithdrawalError && !aCollectionError);
                }
            });
            longTermCollectionList.setAdapter(dailyCollectionAdapterForLongTermSavings);

            DailyCollectionCustomAdapterForExemption dailyCollectionCustomAdapterForExemption
                    = new DailyCollectionCustomAdapterForExemption(getApplicationContext(), dynamicFieldForListViewObject.getLoanList()
                    , new DailyCollectionCustomAdapterForExemption.DataChangeListener()
            {
                @Override
                public void onDataChange(float total) {
                    exemptionSum = 0;
                    exemptionSum = total;
                    totalCollectionForDynamic = ( collectionSum + longTermSavingsSum ) - ( withdrawalSum + exemptionSum);
                    textViewNetAmount.setText(String.valueOf(Math.round(totalCollectionForDynamic)));
                }

                @Override
                public void onExemptionChange(int position, boolean change) {
                    checkWithdrawalMain.set(position,change);
                }
            });
            exemptionListView.setAdapter(dailyCollectionCustomAdapterForExemption);


            buttonMemberDetails.setOnClickListener(v -> {
                AsyncTaskForInitialDataForMemberAccountDetails asyncTaskForInitialDataForMemberAccountDetails = new AsyncTaskForInitialDataForMemberAccountDetails();
                asyncTaskForInitialDataForMemberAccountDetails.execute();

            });
            dialog.dismiss();
            saveButtonFreeze =false;
            if(buttonSave)
            {
                String text = "Transaction saved successfully.";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getApplicationContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForInitialDataForMemberAccountDetails extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;

        AsyncTaskForInitialDataForMemberAccountDetails() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(DailyCollectionDynamicActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            memberAccountDetails = dataSourceRead.getAccountForAccountInfoMemberDetails(memberId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();

            DailyCollectionAdapterForMembersAccountList dailyCollectionAdapterForMembersAccountList
                    = new DailyCollectionAdapterForMembersAccountList(getApplicationContext(),memberAccountDetails);


            //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DailyCollectionDynamicActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams")
            View convertView = inflater.inflate(R.layout.list_for_member_details, null);
            alertDialog.setView(convertView);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
            ListView customNonScrollerListView = convertView.findViewById(R.id.memberAccountList);

            AlertDialog dialog = alertDialog.create();
            dialog.show();




            customNonScrollerListView.setAdapter(dailyCollectionAdapterForMembersAccountList);

            customNonScrollerListView.setOnItemClickListener((parent, view, position, id) -> {

                //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
                int accountId = memberAccountDetails.get(position).getAccountId();

                AsyncTaskForInitialDataForMemberAccountDetailsList asyncTaskForInitialDataForMemberAccountDetailsList = new AsyncTaskForInitialDataForMemberAccountDetailsList(accountId);
                asyncTaskForInitialDataForMemberAccountDetailsList.execute();

            });






        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForInitialDataForMemberAccountDetailsList extends AsyncTask<Void, Integer, Void> {
/*        private Dialog dialog;*/
        private int accountId;
        private ArrayList<TransactionHistory> transactionHistories = new ArrayList<>();
        AsyncTaskForInitialDataForMemberAccountDetailsList(int accountId) {
            this.accountId = accountId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*
            dialog = new Dialog(DailyCollectionDynamicActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            transactionHistories =  dataSourceRead
                    .getTransactionHistory(accountId);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /*            dialog.dismiss();*/

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    DailyCollectionDynamicActivity.this);
            alertDialog.setCancelable(false).setPositiveButton(
                    "Done", (dialog, id) -> dialog.cancel());
            LayoutInflater inflater = getLayoutInflater();

            @SuppressLint("InflateParams")
            View convertView =  inflater.inflate(R.layout.list_for_account_details , null);

            alertDialog.setView(convertView);

            TextView textViewTitle =  convertView.findViewById(R.id.textViewTitle);
            textViewTitle.setText(String.format("Transaction History {%s}", spinnerMember.getSelectedItem().toString()));
            CustomNonScrollerListView listView =  convertView.findViewById(R.id.listView_transaction);
            CustomAdapterForAccountDetails customAdapterForAccountDetails = new CustomAdapterForAccountDetails(getApplicationContext(),transactionHistories);
            listView.setAdapter(customAdapterForAccountDetails);
            alertDialog.show();






        }
    }

    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_amms, menu);
        try {
            String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = format1.parse(currentDate);

            SimpleDateFormat format2 = new SimpleDateFormat("EE",Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            menu.findItem(R.id.action_working_day).setTitle(currentDate+" "+day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String branchName = dataSourceRead.getBranchName();
        menu.findItem(R.id.action_location).setTitle(branchName);
        menu.findItem(R.id.action_close_current_day).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            if(firstTimeBackButtonWork)
            {
                Intent intent = new Intent();
                intent.putIntegerArrayListExtra("SavedDataValue", (ArrayList<Integer>) finalSavedData);
                setResult(RESULT_OK, intent);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop,getApplicationContext());
                finish();
            }

            return true;
        }
        else if(item.getItemId() == R.id.action_logout)
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    DailyCollectionDynamicActivity.this);

            builder.setMessage(
                    "Are you sure Log-Out from Application?")
                    .setCancelable(false)
                    .setTitle("Log Out")
                    .setPositiveButton("Yes",
                            (dialog, id1) -> {
                                Intent i = new Intent(getApplicationContext(),LoginAmmsActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill,getApplicationContext());
                                startActivity(i);
                                finish();
                            })
                    .setNegativeButton("No",
                            (dialog, id12) -> dialog.cancel());
            android.support.v7.app.AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
