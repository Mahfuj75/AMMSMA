package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.customAdapter.AddCbsAndSavingAccountCustomAdapterForListView;
import asa.org.bd.ammsma.service.ExtraTask;
import asa.org.bd.ammsma.customAdapter.SpinnerCustomAdapter;
import asa.org.bd.ammsma.customListView.CustomNonScrollerListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.database.DateAndDataConversion;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.jsonJavaViceVersa.Account;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountBalance;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;

public class NewSavingAccountActivity extends AppCompatActivity {


    private DataSourceRead dataSourceRead;
    private DataSourceWrite dataSourceWrite;
    private Toolbar toolbar;
    private boolean loadDataSuccessful;

    private int groupId;
    private String groupName;
    private List<Member> groupMembers;
    private List<SpannableStringBuilder> newMembers = new ArrayList<>();
    private List<Integer> membersID = new ArrayList<>();
    private Spinner spinnerMembers;
    private CustomNonScrollerListView listViewSavingAccounts;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions = new ArrayList<>();
    private Button buttonCreateSaving;

    private ScrollView scrollViewLongTermSavingsContent;
    private TextView textViewNoMemberSelected;
    private int meetingDaySpinnerPosition = 0;
    private int programOfficerId;
    private int secondClickPreventDialog = 0;
    private int spinnerSelectedItemPosition = 0;
    private ExtraTask extraTask = new ExtraTask();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_saving_account);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName = findViewById(R.id.textView_groupName);
        spinnerMembers = findViewById(R.id.spinnerMembers);
        listViewSavingAccounts = findViewById(R.id.listViewSavingAccounts);
        buttonCreateSaving = findViewById(R.id.buttonCreateSaving);

        scrollViewLongTermSavingsContent = findViewById(R.id.scrollViewLongTermSavingsContent);
        textViewNoMemberSelected = findViewById(R.id.textViewNoMemberSelected);


        dataSourceRead = new DataSourceRead(getApplicationContext());
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        groupId = getIntent().getIntExtra("groupId", 0);
        final String loginId = getIntent().getStringExtra("loginId");
        groupName = getIntent().getStringExtra("groupName");
        getIntent().getStringExtra("realGroupName");
        int groupMeetingDay = dataSourceRead.groupMeetingDayActual(groupId);
        meetingDaySpinnerPosition = (groupMeetingDay == 0) ? 0
                : ((groupMeetingDay == 1) ? 1
                : ((groupMeetingDay == 2) ? 2
                : ((groupMeetingDay == 3) ? 3
                : ((groupMeetingDay == 4) ? 4
                : ((groupMeetingDay == 5) ? 6
                : ((groupMeetingDay == 6) ? 5
                : 6))))));

        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
            textViewGroupName.setText(groupName);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        toolbarNavigationClick();
        AsyncTaskForData asyncTaskForData = new AsyncTaskForData();
        asyncTaskForData.execute();

        buttonCreateSaving.setOnClickListener(v -> {

            if (secondClickPreventDialog == 0) {
                savingCreateOperation();
            }

        });
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }


    @SuppressLint("SetTextI18n")
    private void savingCreateOperation() {
        secondClickPreventDialog = 1;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewSavingAccountActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View convertView = inflater.inflate(R.layout.cbs_create_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);


        final Spinner spinnerDuration = convertView.findViewById(R.id.spinnerDuration);
        final Spinner spinnerDay = convertView.findViewById(R.id.spinnerDay);
        final Spinner spinnerDate = convertView.findViewById(R.id.spinnerDate);
        final Spinner spinnerInstallmentType = convertView.findViewById(R.id.spinnerInstallmentType);
        final EditText editTextMinimumDeposit = convertView.findViewById(R.id.editTextMinimumDeposit);
        TextView textViewGroupName = convertView.findViewById(R.id.textViewGroupName);
        TextView textViewMemberName = convertView.findViewById(R.id.textViewMemberName);
        TextView textViewOpeningDate = convertView.findViewById(R.id.textViewOpeningDate);
        Button buttonCancel = convertView.findViewById(R.id.buttonCancel);
        Button buttonSave = convertView.findViewById(R.id.buttonSave);
        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);

        textViewTitle.setText("Add New Saving Account");


        textViewGroupName.setText(groupName);
        textViewMemberName.setText(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getName() + " (" + groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getPassbookNumber() + ")");
        textViewOpeningDate.setText("" + new DateAndDataConversion().getDateFromInt(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay()));


        String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();
        SimpleDateFormat dateFromDatabase = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFromDatabase.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date dfd = null;
        int date;
        try {
            dfd = dateFromDatabase.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat convertedDate = new SimpleDateFormat("dd", Locale.getDefault());
        convertedDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        try {
            date = Integer.parseInt(convertedDate.format(dfd)) - 1;
            if (date < 0) {
                date = 0;
            }
        } catch (Exception e) {
            date = 0;
        }


        ArrayAdapter<String> adapterDuration = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.saving_Duration));
        adapterDuration.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterDay = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.new_account_days));
        adapterDay.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterDate = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.new_account_date));
        adapterDate.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterInstallmentType;
        if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145) {
            List<String> installmentType = new ArrayList<>();
            installmentType.add("Monthly");
            adapterInstallmentType = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, installmentType);
        } else {
            adapterInstallmentType = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.installment_type_general));
        }
        adapterInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        spinnerDuration.setAdapter(adapterDuration);
        spinnerDay.setAdapter(adapterDay);
        spinnerDate.setAdapter(adapterDate);
        spinnerInstallmentType.setAdapter(adapterInstallmentType);


        spinnerDay.setSelection(meetingDaySpinnerPosition);
        spinnerDate.setSelection(date);


        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        buttonCancel.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            secondClickPreventDialog = 0;
            dialog.dismiss();
        });
        buttonSave.setOnClickListener(v -> {

            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());


            int checkValue;

            if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                checkValue = 0;
            } else {
                checkValue = Integer.parseInt(editTextMinimumDeposit.getText().toString().trim());
            }


            if (checkValue == 0) {
                editTextMinimumDeposit.setError("Minimum Deposit can't be 0");
            } else {


                int spinnerDayPosition;
                switch (spinnerDay.getSelectedItem().toString().trim()) {

                    case "Sunday": {
                        spinnerDayPosition = 0;
                        break;
                    }
                    case "Monday": {
                        spinnerDayPosition = 1;
                        break;
                    }
                    case "Tuesday": {
                        spinnerDayPosition = 2;
                        break;
                    }
                    case "Wednesday": {
                        spinnerDayPosition = 3;
                        break;
                    }
                    case "Thursday": {
                        spinnerDayPosition = 4;
                        break;
                    }
                    case "Saturday": {
                        spinnerDayPosition = 6;
                        break;
                    }
                    default: {
                        spinnerDayPosition = 7;
                    }


                }

                if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145) {

                    Account accountSaving = new Account();

                    accountSaving.setMemberId(membersID.get(spinnerMembers.getSelectedItemPosition()));
                    accountSaving.setProgramId(241);
                    accountSaving.setProgramTypeId(2);
                    if (spinnerDuration.getSelectedItemPosition() == 0) {
                        accountSaving.setDuration(0);
                    }


                    accountSaving.setInstallmentType(2);
                    accountSaving.setSupplementary(false);
                    accountSaving.setCycle(0);
                    accountSaving.setMemberSex(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getSex());
                    accountSaving.setOpeningDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());

                    accountSaving.setDisbursedAmount((float) 0);
                    accountSaving.setServiceChargeAmount((float) 0);
                    accountSaving.setMinimumDeposit(Float.parseFloat(editTextMinimumDeposit.getText().toString().trim()));


                    accountSaving.setMeetingDayOfWeek(spinnerDayPosition);
                    accountSaving.setMeetingDayOfMonth(Integer.parseInt(spinnerDate.getSelectedItem().toString().trim()));
                    int accountIdCbs = dataSourceRead.getNextAccount();
                    accountSaving.setId(accountIdCbs);
                    accountSaving.setProgramName("MSME SAVINGS");
                    accountSaving.setProgramTypeId(2);
                    dataSourceWrite.insertNewAccountWithoutLoan(accountSaving, programOfficerId);


                    AccountBalance accountBalanceSaving = new AccountBalance();

                    accountBalanceSaving.setAccountId(accountIdCbs);
                    accountBalanceSaving.setDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());
                    accountBalanceSaving.setBalance((float) 0);
                    accountBalanceSaving.setDebit((float) 0);
                    accountBalanceSaving.setProgramType(2);

                    if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                        accountBalanceSaving.setCredit((float) 0);
                    } else {
                        accountBalanceSaving.setCredit((float) 0);
                    }

                    dataSourceWrite.insertNewAccountBalance(accountBalanceSaving, programOfficerId);

                    listViewAccountTask(spinnerMembers.getSelectedItemPosition(), true);

                    secondClickPreventDialog = 0;
                    dialog.dismiss();
                } else {
                    Account accountSaving = new Account();

                    accountSaving.setMemberId(membersID.get(spinnerMembers.getSelectedItemPosition()));
                    accountSaving.setProgramId(229);
                    accountSaving.setProgramTypeId(2);
                    if (spinnerDuration.getSelectedItemPosition() == 0) {
                        accountSaving.setDuration(0);
                    }


                    if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145) {
                        accountSaving.setInstallmentType(2);
                    } else {
                        accountSaving.setInstallmentType(spinnerInstallmentType.getSelectedItemPosition() + 1);
                    }
                    accountSaving.setInstallmentType(spinnerInstallmentType.getSelectedItemPosition() + 1);
                    accountSaving.setSupplementary(false);
                    accountSaving.setCycle(0);
                    accountSaving.setMemberSex(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getSex());
                    accountSaving.setOpeningDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());

                    accountSaving.setDisbursedAmount((float) 0);
                    accountSaving.setServiceChargeAmount((float) 0);
                    accountSaving.setMinimumDeposit(Float.parseFloat(editTextMinimumDeposit.getText().toString().trim()));

                    accountSaving.setMeetingDayOfWeek(spinnerDayPosition);
                    accountSaving.setMeetingDayOfMonth(Integer.parseInt(spinnerDate.getSelectedItem().toString().trim()));
                    int accountIdCbs = dataSourceRead.getNextAccount();
                    accountSaving.setId(accountIdCbs);


                    if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() == 129) {
                        accountSaving.setProgramId(229);
                        accountSaving.setProgramName("PRIMARY SAVINGS");
                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() == 130) {
                        accountSaving.setProgramId(230);
                        accountSaving.setProgramName("SPECIAL SAVINGS");
                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() == 131) {
                        accountSaving.setProgramId(231);
                        accountSaving.setProgramName("SANITATION ENTREPRENEUR SAVINGS");
                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() == 137) {
                        accountSaving.setProgramId(237);
                        accountSaving.setProgramName("SMAP SAVINGS");
                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() == 138) {
                        accountSaving.setProgramId(238);
                        accountSaving.setProgramName("CROSSBREED MILKING COW SAVINGS");
                    } else {
                        accountSaving.setProgramId(229);
                        accountSaving.setProgramName("PRIMARY SAVINGS");
                    }
                    accountSaving.setProgramTypeId(2);
                    dataSourceWrite.insertNewAccountWithoutLoan(accountSaving, programOfficerId);


                    AccountBalance accountBalanceSaving = new AccountBalance();

                    accountBalanceSaving.setAccountId(accountIdCbs);
                    accountBalanceSaving.setDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());
                    accountBalanceSaving.setBalance((float) 0);
                    accountBalanceSaving.setDebit((float) 0);
                    accountBalanceSaving.setProgramType(2);

                    if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                        accountBalanceSaving.setCredit((float) 0);
                    } else {
                        accountBalanceSaving.setCredit((float) 0);
                    }

                    dataSourceWrite.insertNewAccountBalance(accountBalanceSaving, programOfficerId);

                    listViewAccountTask(spinnerMembers.getSelectedItemPosition(), true);

                    secondClickPreventDialog = 0;
                    dialog.dismiss();
                }

            }
        });
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }


    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForData extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(NewSavingAccountActivity.this);
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (loadDataSuccessful) {

                scrollViewLongTermSavingsContent.setVisibility(View.GONE);
                textViewNoMemberSelected.setText("No Member Selected");
                textViewNoMemberSelected.setVisibility(View.VISIBLE);


                spinnerMembers.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListLTS(newMembers, getApplicationContext()));

                spinnerMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                        if (position == 0) {
                            scrollViewLongTermSavingsContent.setVisibility(View.GONE);
                            textViewNoMemberSelected.setText("No Member Selected");
                            textViewNoMemberSelected.setVisibility(View.VISIBLE);
                            spinnerSelectedItemPosition = position;
                        } else {
                            spinnerSelectedItemPosition = position;
                            listViewAccountTask(position, false);
                        }
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                loadDataSuccessful = false;
                dialog.dismiss();
            }

        }
    }

    private boolean loadData() {
        groupMembers = dataSourceRead.getAllMembers(groupId);
        membersID = new ArrayList<>();
        newMembers = new ArrayList<>();
        membersID.add(0, -12345);
        newMembers.add(0, new SpannableStringBuilder("Select Member"));
        for (int i = 0; i < groupMembers.size(); i++) {

            membersID.add(i + 1, groupMembers.get(i).getId());


            SpannableStringBuilder savingsColor = new SpannableStringBuilder();

            if (groupMembers.get(i).getMemberExtra().getSavingsBranch() > 0) {
                SpannableStringBuilder savingsBranch = new SpannableStringBuilder(" { " + groupMembers.get(i).getMemberExtra().getSavingsBranch() + " Savings (B) }");
                savingsBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#1B5E20")), 0, savingsBranch.length(), 0);
                savingsColor.append(savingsBranch);
            } else if (groupMembers.get(i).getMemberExtra().getSavingsTab() > 0) {
                SpannableStringBuilder savingsTab = new SpannableStringBuilder(" { " + groupMembers.get(i).getMemberExtra().getSavingsTab() + " Savings (T) }");
                savingsTab.setSpan(new ForegroundColorSpan(Color.parseColor("#F57F17")), 0, savingsTab.length(), 0);
                savingsColor.append(savingsTab);
            }

            if (!groupMembers.get(i).getMemberOldOrNew().equals("New")) {
                SpannableStringBuilder main = new SpannableStringBuilder(groupMembers.get(i).getName() + "/" + groupMembers.get(i).getFatherName() + " (" + groupMembers.get(i).getPassbookNumber() + ")");
                main.append(savingsColor);
                newMembers.add(i + 1, main);
            } else {

                SpannableStringBuilder main = new SpannableStringBuilder(groupMembers.get(i).getName() + "/" + groupMembers.get(i).getFatherName() + " (" + groupMembers.get(i).getPassbookNumber() + ")" + " -" + groupMembers.get(i).getMemberOldOrNew());
                main.append(savingsColor);
                newMembers.add(i + 1, main);
            }

        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void listViewAccountTask(int position, boolean update) {

        if (update) {
            loadData();
            spinnerSelectedItemPosition = spinnerMembers.getSelectedItemPosition();
            spinnerMembers.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListLTS(newMembers, getApplicationContext()));
            spinnerMembers.setSelection(spinnerSelectedItemPosition);
        }


        if (position == 0) {
            scrollViewLongTermSavingsContent.setVisibility(View.GONE);
            textViewNoMemberSelected.setText("No Member Selected");
            textViewNoMemberSelected.setVisibility(View.VISIBLE);
        } else {


            textViewNoMemberSelected.setVisibility(View.GONE);

            accountForDailyTransactions = new ArrayList<>();
            accountForDailyTransactions = dataSourceRead.getSavingAccountInfo(membersID.get(position));

            if (accountForDailyTransactions.size() >= 1) {
                buttonCreateSaving.setVisibility(View.GONE);
            } else {
                if (!groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                    buttonCreateSaving.setVisibility(View.VISIBLE);
                } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getAdmissionDateInteger() == new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay() && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                    buttonCreateSaving.setVisibility(View.VISIBLE);
                } else {
                    buttonCreateSaving.setVisibility(View.GONE);
                    if (accountForDailyTransactions.size() == 0) {
                        textViewNoMemberSelected.setText("This member does not have the permission to add new Saving Account");
                        textViewNoMemberSelected.setVisibility(View.VISIBLE);
                    }
                }
            }

            listViewSavingAccounts.setAdapter(new AddCbsAndSavingAccountCustomAdapterForListView(getApplicationContext(), accountForDailyTransactions, position12 -> {

                dataSourceWrite.deleteLtsAccount(accountForDailyTransactions.get(position12).getAccountId());
                accountForDailyTransactions = dataSourceRead.getSavingAccountInfo(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getId());

                listViewAccountTask(spinnerMembers.getSelectedItemPosition(), true);

            }));


            listViewSavingAccounts.setOnItemClickListener((parent, view, position1, id) -> {




                if (secondClickPreventDialog == 0) {
                    secondClickPreventDialog = 1;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewSavingAccountActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    @SuppressLint("InflateParams")
                    View convertView = inflater.inflate(R.layout.cbs_and_saving_dialog, null);
                    alertDialog.setView(convertView);
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Done", (dialog, which) -> {
                        dialog.dismiss();
                        secondClickPreventDialog = 0;
                    });


                    TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
                    TextView textViewGroupName = convertView.findViewById(R.id.textViewGroupName);
                    TextView textViewMemberName = convertView.findViewById(R.id.textViewMemberName);
                    TextView textViewOpeningDate = convertView.findViewById(R.id.textViewOpeningDate);
                    TextView textViewDuration = convertView.findViewById(R.id.textViewDuration);
                    TextView textViewMeetingDay = convertView.findViewById(R.id.textViewMeetingDay);
                    TextView textViewMeetingDate = convertView.findViewById(R.id.textViewMeetingDate);
                    TextView textViewBalance = convertView.findViewById(R.id.textViewBalance);
                    TextView textViewMinimumDeposit = convertView.findViewById(R.id.textViewMinimumDeposit);
                    TextView textViewLastPaymentDate = convertView.findViewById(R.id.textViewLastPaymentDate);
                    TextView textViewInstallmentType = convertView.findViewById(R.id.textViewInstallmentType);
                    LinearLayout linearLayoutLastPayment = convertView.findViewById(R.id.linearLayoutLastPayment);

                    textViewTitle.setText(titleCase(accountForDailyTransactions.get(position1).getProgramName()));
                    textViewGroupName.setText(groupName);
                    textViewMemberName.setText(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getName() + " (" + groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getPassbookNumber() + ")");
                    textViewOpeningDate.setText(accountForDailyTransactions.get(position1).getOpeningDateValue());
                    textViewDuration.setText(accountForDailyTransactions.get(position1).getDuration() + " years");
                    textViewMeetingDay.setText(new DataSourceOperationsCommon(getApplicationContext()).getRealDayName(accountForDailyTransactions.get(position1).getMeetingDayOfWeek()));
                    textViewMeetingDate.setText("" + accountForDailyTransactions.get(position1).getMeetingDayOfMonth());
                    textViewBalance.setText("" + Math.round(accountForDailyTransactions.get(position1).getBalance()));
                    textViewMinimumDeposit.setText("" + Math.round(accountForDailyTransactions.get(position1).getMinimumDeposit()));


                    textViewLastPaymentDate.setText("" + accountForDailyTransactions.get(position1).getLastAccountDetailsDate());

                    if (accountForDailyTransactions.get(position1).getInstallmentType() == 1) {
                        textViewInstallmentType.setText("WEEKLY");
                    } else if (accountForDailyTransactions.get(position1).getInstallmentType() == 2) {
                        textViewInstallmentType.setText("Monthly");
                    }


                    if (accountForDailyTransactions.get(position1).getLastAccountDetailsDate().equals("NOT FOUND") && accountForDailyTransactions.get(position1).getFlag() > 0) {
                        linearLayoutLastPayment.setVisibility(View.GONE);
                    } else {
                        linearLayoutLastPayment.setVisibility(View.VISIBLE);
                    }
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                }


            });

            scrollViewLongTermSavingsContent.setVisibility(View.VISIBLE);

        }


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
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
            return true;
        } else if (id == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    NewSavingAccountActivity.this);

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

    private static String titleCase(String givenString) {


        if (givenString.trim().contains(" ")) {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {


                if (aSplit.equals("MSME")) {
                    stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toUpperCase()).append(" ");
                } else {
                    stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
                }
            }
            if (stringBuffer.toString().trim().contains("Capital Buildup Savings")) {
                stringBuffer = new StringBuilder();
                stringBuffer.append("Capital Buildup Savings");
            }
            return stringBuffer.toString().trim();
        } else {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }


}
