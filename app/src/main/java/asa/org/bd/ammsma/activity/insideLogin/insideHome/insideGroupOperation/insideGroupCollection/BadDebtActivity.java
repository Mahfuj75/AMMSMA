package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.insideGroupCollection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import asa.org.bd.ammsma.customAdapter.BadDebtAdapterForAccountInfo;
import asa.org.bd.ammsma.customAdapter.BadDebtAdapterForCollection;
import asa.org.bd.ammsma.customAdapter.BadDebtAdapterForMembersAccountList;
import asa.org.bd.ammsma.customAdapter.CustomAdapterForAccountDetails;
import asa.org.bd.ammsma.customAdapter.SpinnerCustomAdapter;
import asa.org.bd.ammsma.customListView.CustomNonScrollerListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.extra.MemberListInfo;
import asa.org.bd.ammsma.extra.TransactionHistory;
import asa.org.bd.ammsma.service.ExtraTask;


public class BadDebtActivity extends AppCompatActivity {


    private ImageButton imageButtonLeft;
    private ImageButton imageButtonRight;


    private Button buttonMemberDetails;

    private int groupId;
    private String realGroupName;
    private int memberId;
    private int memberPosition;
    private int programOfficerId;

    private DataSourceRead dataSourceRead;
    private DataSourceWrite dataSourceWrite;
    private Spinner spinnerMember;

    private boolean loadDataSuccessful = false;
    private CustomNonScrollerListView badDebtAccountListView;
    private CustomNonScrollerListView badDebtCollectionListView;

    private ArrayList<AccountForDailyTransaction> badDebtAccountList;

    private TextView textViewNetAmount;
    private boolean mainError = false;
    private ArrayList<Float> badDebtChangeList;
    private ListView customNonScrollerListView;
    private List<Integer> savedData = new ArrayList<>();
    private List<Integer> finalSavedData = new ArrayList<>();
    private MemberListInfo memberListInfo = new MemberListInfo();
    private ExtraTask extraTask = new ExtraTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_debt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);


        imageButtonLeft = findViewById(R.id.imageButtonLeft);
        imageButtonRight = findViewById(R.id.imageButtonRight);
        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName = findViewById(R.id.textView_groupName);


        textViewNetAmount = findViewById(R.id.textView_net_amount);
        buttonMemberDetails = findViewById(R.id.buttonMemberDetails);
        Button buttonSaveToDatabase = findViewById(R.id.buttonSave);
        Button buttonClose = findViewById(R.id.buttonClose);
        spinnerMember = findViewById(R.id.spinner_members);
        badDebtAccountListView = findViewById(R.id.badDebtAccountListView);
        badDebtCollectionListView = findViewById(R.id.badDebtCollectionListView);


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        groupId = getIntent().getIntExtra("groupId", 0);
        memberPosition = getIntent().getIntExtra("position", 0);
        String loginId = getIntent().getStringExtra("loginId");
        String groupName = getIntent().getStringExtra("groupName");
        realGroupName = getIntent().getStringExtra("realGroupName");
        memberId = getIntent().getIntExtra("memberId", 0);


        dataSourceRead = new DataSourceRead(getApplicationContext());
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(String.format("%s - %s", dataSourceRead.getLOName(programOfficerId), loginId));
            textViewGroupName.setText(groupName);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }


        AsyncTaskForData asyncTaskForData = new AsyncTaskForData();
        asyncTaskForData.execute();


        buttonSaveToDatabase.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            buttonSaveOperation();
        });

        buttonClose.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            Intent intent = new Intent();
            intent.putIntegerArrayListExtra("SavedDataValue", (ArrayList<Integer>) finalSavedData);
            setResult(RESULT_OK, intent);
            finish();
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }


    private boolean loadData() {
        savedData = new ArrayList<>();
        memberListInfo = dataSourceRead.getAllMembersForDynamicSpinnerBadDebt(groupId);

        for (int i = 0; i < memberListInfo.getMembersInfo().size(); i++) {
            savedData.add(i, 0);
        }
        return true;
    }

    public void loadListData() {


        mainError = false;
        badDebtAccountList = dataSourceRead.getBadDebtAccountInfo(memberId);
        badDebtChangeList = new ArrayList<>();


        badDebtAccountListView.setAdapter(new BadDebtAdapterForAccountInfo(getApplicationContext(), badDebtAccountList));
        badDebtCollectionListView.setAdapter(new BadDebtAdapterForCollection(getApplicationContext(), badDebtAccountList, new BadDebtAdapterForCollection.DataChangeListener() {
            @Override
            public void onDataChange(float totalAmountCollection, ArrayList<Float> dataChangeList) {
                textViewNetAmount.setText(String.valueOf(Math.round(totalAmountCollection)));
                badDebtChangeList = new ArrayList<>();
                badDebtChangeList = dataChangeList;
            }

            @Override
            public void errorSet(boolean collectionError) {
                mainError = collectionError;

            }
        }));


        final BadDebtAdapterForMembersAccountList badDebtAdapterForMembersAccountList = new BadDebtAdapterForMembersAccountList(getApplicationContext(), badDebtAccountList);
        buttonMemberDetails.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BadDebtActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams")
            View convertView = inflater.inflate(R.layout.list_for_member_details, null);
            alertDialog.setView(convertView);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
            customNonScrollerListView = convertView.findViewById(R.id.memberAccountList);

            AlertDialog dialog = alertDialog.create();
            dialog.show();


            customNonScrollerListView.setAdapter(badDebtAdapterForMembersAccountList);

            customNonScrollerListView.setOnItemClickListener((parent, view, position, id) -> {
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                int accountId = badDebtAccountList.get(position).getAccountId();

                ArrayList<TransactionHistory> transactionHistories = dataSourceRead
                        .getTransactionHistory(accountId);
                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
                        BadDebtActivity.this);
                alertDialog1.setCancelable(false).setPositiveButton(
                        "Done", (dialog1, id1) -> dialog1.cancel());
                LayoutInflater inflater1 = getLayoutInflater();

                @SuppressLint("InflateParams")
                View convertView1 = inflater1.inflate(R.layout.list_for_account_details, null);

                alertDialog1.setView(convertView1);

                TextView textViewTitle = convertView1.findViewById(R.id.textViewTitle);
                textViewTitle.setText("Transaction History {" + spinnerMember.getSelectedItem().toString() + "}");

                CustomNonScrollerListView listView = convertView1.findViewById(R.id.listView_transaction);
                CustomAdapterForAccountDetails customAdapterForAccountDetails = new CustomAdapterForAccountDetails(getApplicationContext(), transactionHistories);
                listView.setAdapter(customAdapterForAccountDetails);
                alertDialog1.show();
            });
        });
    }

    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }

    public void buttonSaveOperation() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+6"));

        String currentDateTime = simpleDateFormat.format(calendar.getTime());
        if (!mainError) {

            if (savedData.get(spinnerMember.getSelectedItemPosition()) == 0) {
                finalSavedData.add(spinnerMember.getSelectedItemPosition());
                savedData.set(spinnerMember.getSelectedItemPosition(), 1);
            }

            for (int i = 0; i < badDebtAccountList.size(); i++) {
                dataSourceWrite.insertOrUpdateAccountBalanceCredit(badDebtAccountList.get(i), badDebtChangeList.get(i), currentDateTime, 8, programOfficerId);
            }

            dataSourceWrite.insertTempData(groupId, realGroupName);
            loadListData();
            memberPosition = spinnerMember.getSelectedItemPosition();
            AsyncTaskForData asyncTaskForData = new AsyncTaskForData();
            asyncTaskForData.execute();

        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(
                    BadDebtActivity.this);

            builder.setMessage(
                    "Unable to Save data. Please correct the errors and try again.")
                    .setCancelable(false)
                    .setTitle("Error")
                    .setPositiveButton("Ok",
                            (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        }
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_amms, menu);
        try {
            String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = format1.parse(currentDate);

            SimpleDateFormat format2 = new SimpleDateFormat("EE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            menu.findItem(R.id.action_working_day).setTitle(currentDate + " " + day);
            menu.findItem(R.id.action_close_current_day).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String branchName = dataSourceRead.getBranchName();
        menu.findItem(R.id.action_location).setTitle(branchName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            Intent intent = new Intent();
            intent.putIntegerArrayListExtra("SavedDataValue", (ArrayList<Integer>) finalSavedData);
            setResult(RESULT_OK, intent);
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();

            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    BadDebtActivity.this);

            builder.setMessage(
                    "Are you sure Log-Out from Application?")
                    .setCancelable(false)
                    .setTitle("Log Out")
                    .setPositiveButton("Yes",
                            (dialog, id1) -> {
                                Intent i = new Intent(getApplicationContext(), LoginAmmsActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
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

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForData extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(BadDebtActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
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

            if (loadDataSuccessful) {
                spinnerMember.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListDailyCollection(getApplicationContext(), memberListInfo.getMembersName(), memberListInfo));
                spinnerMember.setSelection(memberPosition);
                loadListData();


                spinnerMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                        memberId = memberListInfo.getMembersInfo().get(position).getId();
                        loadListData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                imageButtonLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                        if ((spinnerMember.getSelectedItemPosition() - 1) >= 0) {
                            memberId = memberListInfo.getMembersInfo().get(spinnerMember.getSelectedItemPosition() - 1).getId();
                            spinnerMember.setSelection(spinnerMember.getSelectedItemPosition() - 1);
                            loadListData();

                        } else {
                            Toast.makeText(getApplicationContext(), "No More Members", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                imageButtonRight.setOnClickListener(v -> {
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                    if ((spinnerMember.getSelectedItemPosition() + 1) < memberListInfo.getMembersInfo().size()) {
                        memberId = memberListInfo.getMembersInfo().get(spinnerMember.getSelectedItemPosition() + 1).getId();
                        spinnerMember.setSelection(spinnerMember.getSelectedItemPosition() + 1);
                        loadListData();
                    } else {
                        Toast.makeText(getApplicationContext(), "No More Members", Toast.LENGTH_LONG).show();
                    }
                });


                loadDataSuccessful = false;
                dialog.dismiss();
            }

        }
    }

}
