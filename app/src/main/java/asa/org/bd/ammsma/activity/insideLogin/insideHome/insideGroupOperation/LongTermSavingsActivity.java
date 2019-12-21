package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
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
import asa.org.bd.ammsma.customAdapter.AddLtsAccountCustomAdapterForListView;
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
import asa.org.bd.ammsma.service.ExtraTask;


public class LongTermSavingsActivity extends AppCompatActivity {

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
    private CustomNonScrollerListView listViewLtsAccounts;
    private ArrayList<AccountForDailyTransaction> accountForDailyTransactions = new ArrayList<>();
    private Button buttonCreateLts;

    private ScrollView scrollViewLongTermSavingsContent;
    private TextView textViewNoMemberSelected;
    private int meetingDaySpinnerPosition = 0;
    private int secondClickPreventDialog = 0;
    private int spinnerSelectedItemPosition = 0;
    private ExtraTask extraTask = new ExtraTask();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_term_sevings);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName = findViewById(R.id.textView_groupName);
        spinnerMembers = findViewById(R.id.spinnerMembers);
        listViewLtsAccounts = findViewById(R.id.listViewLtsAccounts);
        buttonCreateLts = findViewById(R.id.buttonCreateLts);

        scrollViewLongTermSavingsContent = findViewById(R.id.scrollViewLongTermSavingsContent);
        textViewNoMemberSelected = findViewById(R.id.textViewNoMemberSelected);


        dataSourceRead = new DataSourceRead(getApplicationContext());
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        final int programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
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


        AsyncTaskForData asyncTaskForData = new AsyncTaskForData();
        asyncTaskForData.execute();


        buttonCreateLts.setOnClickListener(v -> {



            if (secondClickPreventDialog == 0) {
                secondClickPreventDialog = 1;

                if (dataSourceRead.hasActiveLoanAccount(membersID.get(spinnerMembers.getSelectedItemPosition()))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LongTermSavingsActivity.this);

                    builder.setMessage(
                            "LTS Account can't be added without active loan")
                            .setCancelable(false)
                            .setTitle("Notify")
                            .setPositiveButton("Cancel",
                                    (dialog, id) -> {
                                        secondClickPreventDialog = 0;
                                        dialog.cancel();
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LongTermSavingsActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    @SuppressLint("InflateParams")
                    View convertView = inflater.inflate(R.layout.add_new_lts_account, null);
                    alertDialog.setView(convertView);
                    alertDialog.setCancelable(false);


                    final Spinner spinnerDuration = convertView.findViewById(R.id.spinnerDuration);
                    final Spinner spinnerDay = convertView.findViewById(R.id.spinnerDay);
                    final Spinner spinnerDate = convertView.findViewById(R.id.spinnerDate);
                    final EditText editTextMinimumDeposit = convertView.findViewById(R.id.editTextMinimumDeposit);
                    TextView textViewGroupName1 = convertView.findViewById(R.id.textViewGroupName);
                    TextView textViewMemberName = convertView.findViewById(R.id.textViewMemberName);
                    TextView textViewOpeningDate = convertView.findViewById(R.id.textViewOpeningDate);
                    Button buttonCancel = convertView.findViewById(R.id.buttonCancel);
                    Button buttonSave = convertView.findViewById(R.id.buttonSave);


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


                    textViewGroupName1.setText(groupName);
                    textViewMemberName.setText(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getName() + " (" + groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getPassbookNumber() + ")");
                    textViewOpeningDate.setText("" + new DateAndDataConversion().getDateFromInt(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay()));


                    editTextMinimumDeposit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            int value;

                            if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                                value = 0;
                            } else {
                                value = Integer.parseInt(editTextMinimumDeposit.getText().toString().trim());
                            }

                            if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145) {

                                if (value > 10000) {
                                    editTextMinimumDeposit.setError("Minimum Deposit can't be greater than 10000Tk");
                                }

                            } else {
                                if (value > 1000) {
                                    editTextMinimumDeposit.setError("Minimum Deposit can't be greater than 1000Tk");
                                }
                            }


                        }
                    });


                    ArrayAdapter<String> adapterDuration = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.lts_Duration));
                    adapterDuration.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

                    ArrayAdapter<String> adapterDay = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.new_account_days));
                    adapterDuration.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

                    ArrayAdapter<String> adapterDate = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member, getResources().getStringArray(R.array.new_account_date));
                    adapterDuration.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

                    spinnerDuration.setAdapter(adapterDuration);
                    spinnerDay.setAdapter(adapterDay);
                    spinnerDate.setAdapter(adapterDate);

                    spinnerDay.setSelection(meetingDaySpinnerPosition);
                    spinnerDate.setSelection(date);


                    final AlertDialog dialog = alertDialog.create();
                    dialog.show();

                    buttonCancel.setOnClickListener(v1 -> {
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                        secondClickPreventDialog = 0;
                        dialog.dismiss();
                    });
                    buttonSave.setOnClickListener(v12 -> {

                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

                        int spinnerDayPosition;

                        String s = spinnerDay.getSelectedItem().toString().trim();
                        switch (s) {
                            case "Sunday":
                                spinnerDayPosition = 0;
                                break;
                            case "Monday":
                                spinnerDayPosition = 1;
                                break;
                            case "Tuesday":
                                spinnerDayPosition = 2;
                                break;
                            case "Wednesday":
                                spinnerDayPosition = 3;
                                break;
                            case "Thursday":
                                spinnerDayPosition = 4;
                                break;
                            case "Saturday":
                                spinnerDayPosition = 6;
                                break;
                            default:
                                spinnerDayPosition = 7;
                                break;
                        }


                        int checkValue;

                        if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                            checkValue = 0;
                        } else {
                            checkValue = Integer.parseInt(editTextMinimumDeposit.getText().toString().trim());
                        }


                        if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145) {
                            if (checkValue < 1000 || checkValue > 10000) {
                                if (checkValue > 10000) {
                                    editTextMinimumDeposit.setError("Minimum Deposit can't be greater than 10000Tk");
                                } else {
                                    editTextMinimumDeposit.setError("Minimum Deposit can't be smaller than 1000TK");
                                }
                            } else if (checkValue % 500 == 0) {

                                Account ltsAccount = new Account();

                                ltsAccount.setMemberId(membersID.get(spinnerMembers.getSelectedItemPosition()));
                                ltsAccount.setProgramId(204);
                                ltsAccount.setProgramTypeId(2);

                                if (spinnerDuration.getSelectedItemPosition() == 0) {
                                    ltsAccount.setDuration(5);
                                } else if ((spinnerDuration.getSelectedItemPosition() == 1)) {
                                    ltsAccount.setDuration(10);
                                }

                                ltsAccount.setInstallmentType(2);
                                ltsAccount.setSupplementary(false);
                                ltsAccount.setCycle(0);
                                ltsAccount.setMemberSex(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getSex());
                                ltsAccount.setOpeningDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());

                                ltsAccount.setDisbursedAmount((float) 0);
                                ltsAccount.setServiceChargeAmount((float) 0);
                                if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                                    ltsAccount.setMinimumDeposit((float) 0);
                                } else {
                                    ltsAccount.setMinimumDeposit(Float.parseFloat(editTextMinimumDeposit.getText().toString().trim()));
                                }

                                ltsAccount.setMeetingDayOfWeek(spinnerDayPosition);
                                ltsAccount.setMeetingDayOfMonth(Integer.parseInt(spinnerDate.getSelectedItem().toString().trim()));
                                int accountId = dataSourceRead.getNextAccount();
                                ltsAccount.setId(accountId);
                                ltsAccount.setProgramName("LONG TERM SAVINGS");
                                dataSourceWrite.insertNewAccountWithoutLoan(ltsAccount, programOfficerId);


                                AccountBalance accountBalanceLts = new AccountBalance();

                                accountBalanceLts.setAccountId(accountId);
                                accountBalanceLts.setDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());
                                accountBalanceLts.setBalance((float) 0);
                                accountBalanceLts.setDebit((float) 0);
                                accountBalanceLts.setProgramType(2);

                                if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                                    accountBalanceLts.setCredit((float) 0);
                                } else {
                                    accountBalanceLts.setCredit(Float.parseFloat(editTextMinimumDeposit.getText().toString().trim()));
                                }

                                dataSourceWrite.insertNewAccountBalance(accountBalanceLts, programOfficerId);

                                listViewAccountTask(spinnerMembers.getSelectedItemPosition(), true);

                                secondClickPreventDialog = 0;
                                dialog.dismiss();

                            } else {
                                editTextMinimumDeposit.setError("Minimum Deposit should be greater than or equal  1000TK and less than or equal 10000TK and multiple of 500TK");
                            }
                        } else {
                            if (checkValue == 0 || checkValue % 100 != 0 || checkValue > 1000) {
                                if (checkValue > 1000) {
                                    editTextMinimumDeposit.setError("Minimum Deposit can't be greater than 1000Tk");
                                } else {
                                    editTextMinimumDeposit.setError("Minimum Deposit Should be multiple of 100Tk");
                                }
                            } else {
                                Account ltsAccount = new Account();

                                ltsAccount.setMemberId(membersID.get(spinnerMembers.getSelectedItemPosition()));
                                ltsAccount.setProgramId(204);
                                ltsAccount.setProgramTypeId(2);

                                if (spinnerDuration.getSelectedItemPosition() == 0) {
                                    ltsAccount.setDuration(5);
                                } else if ((spinnerDuration.getSelectedItemPosition() == 1)) {
                                    ltsAccount.setDuration(10);
                                }

                                ltsAccount.setInstallmentType(2);
                                ltsAccount.setSupplementary(false);
                                ltsAccount.setCycle(0);
                                ltsAccount.setMemberSex(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getSex());
                                ltsAccount.setOpeningDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());

                                ltsAccount.setDisbursedAmount((float) 0);
                                ltsAccount.setServiceChargeAmount((float) 0);
                                if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                                    ltsAccount.setMinimumDeposit((float) 0);
                                } else {
                                    ltsAccount.setMinimumDeposit(Float.parseFloat(editTextMinimumDeposit.getText().toString().trim()));
                                }


                                ltsAccount.setMeetingDayOfWeek(spinnerDayPosition);
                                ltsAccount.setMeetingDayOfMonth(spinnerDate.getSelectedItemPosition() + 1);
                                int accountId = dataSourceRead.getNextAccount();
                                ltsAccount.setId(accountId);
                                ltsAccount.setProgramName("LONG TERM SAVINGS");
                                dataSourceWrite.insertNewAccountWithoutLoan(ltsAccount, programOfficerId);


                                AccountBalance accountBalanceLts = new AccountBalance();

                                accountBalanceLts.setAccountId(accountId);
                                accountBalanceLts.setDate(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());
                                accountBalanceLts.setBalance((float) 0);
                                accountBalanceLts.setDebit((float) 0);
                                accountBalanceLts.setProgramType(2);

                                if (editTextMinimumDeposit.getText().toString().trim().equals("")) {
                                    accountBalanceLts.setCredit((float) 0);
                                } else {
                                    accountBalanceLts.setCredit(Float.parseFloat(editTextMinimumDeposit.getText().toString().trim()));
                                }

                                dataSourceWrite.insertNewAccountBalance(accountBalanceLts, programOfficerId);

                                listViewAccountTask(spinnerMembers.getSelectedItemPosition(), true);
                                secondClickPreventDialog = 0;
                                dialog.dismiss();
                            }
                        }


                    });
                }

                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            }


        });


        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
            textViewGroupName.setText(groupName);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        toolbarNavigationClick();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForData extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(LongTermSavingsActivity.this);
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

                scrollViewLongTermSavingsContent.setVisibility(View.GONE);
                textViewNoMemberSelected.setText(R.string.no_member_selected);
                textViewNoMemberSelected.setVisibility(View.VISIBLE);


                spinnerMembers.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListLTS(newMembers, getApplicationContext()));

                spinnerMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position == 0) {
                            scrollViewLongTermSavingsContent.setVisibility(View.GONE);
                            textViewNoMemberSelected.setText(R.string.no_member_selected);
                            textViewNoMemberSelected.setVisibility(View.VISIBLE);
                            spinnerSelectedItemPosition = position;
                        } else {
                            listViewAccountTask(position, false);
                            spinnerSelectedItemPosition = position;
                        }
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerMembers.setSelection(spinnerSelectedItemPosition);
                loadDataSuccessful = false;
                dialog.dismiss();
            }

        }
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
            textViewNoMemberSelected.setText(R.string.no_member_selected);
            textViewNoMemberSelected.setVisibility(View.VISIBLE);
        } else {
            textViewNoMemberSelected.setVisibility(View.GONE);

            accountForDailyTransactions = new ArrayList<>();
            accountForDailyTransactions = dataSourceRead.getAccountForAccountLTSInfo(membersID.get(position));

            if (accountForDailyTransactions.size() >= 3) {
                buttonCreateLts.setVisibility(View.GONE);
            } else {
                if (!groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                    buttonCreateLts.setVisibility(View.VISIBLE);
                } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getAdmissionDateInteger() == new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay() && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                    buttonCreateLts.setVisibility(View.VISIBLE);
                } else {
                    buttonCreateLts.setVisibility(View.GONE);
                    if (accountForDailyTransactions.size() == 0) {
                        textViewNoMemberSelected.setText(R.string.lts_permission);
                        textViewNoMemberSelected.setVisibility(View.VISIBLE);
                    }
                }

            }

            listViewLtsAccounts.setAdapter(new AddLtsAccountCustomAdapterForListView(getApplicationContext(), accountForDailyTransactions, position1 -> {

                dataSourceWrite.deleteLtsAccount(accountForDailyTransactions.get(position1).getAccountId());
                accountForDailyTransactions = dataSourceRead.getAccountForAccountLTSInfo(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getId());

                if (accountForDailyTransactions.size() >= 3) {
                    buttonCreateLts.setVisibility(View.GONE);
                } else {
                    if (!groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                        buttonCreateLts.setVisibility(View.VISIBLE);
                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getAdmissionDateInteger() == new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay() && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                        buttonCreateLts.setVisibility(View.VISIBLE);
                    } else {
                        buttonCreateLts.setVisibility(View.GONE);
                    }
                }

                listViewAccountTask(spinnerMembers.getSelectedItemPosition(), true);

            }));


            listViewLtsAccounts.setOnItemClickListener((parent, view, position12, id) -> {

                if (secondClickPreventDialog == 0) {
                    secondClickPreventDialog = 1;
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LongTermSavingsActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    @SuppressLint("InflateParams")
                    View convertView = inflater.inflate(R.layout.lts_dialog, null);
                    alertDialog.setView(convertView);
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Done", (dialog, which) -> {
                        secondClickPreventDialog = 0;
                        dialog.dismiss();
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


                    textViewTitle.setText("LTS " + (position12 + 1));
                    textViewGroupName.setText(groupName);
                    textViewMemberName.setText(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getName() + " (" + groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getPassbookNumber() + ")");
                    textViewOpeningDate.setText(accountForDailyTransactions.get(position12).getOpeningDateValue());
                    textViewDuration.setText(accountForDailyTransactions.get(position12).getDuration() + " years");

                    textViewMeetingDay.setText(new DataSourceOperationsCommon(getApplicationContext()).getRealDayName(accountForDailyTransactions.get(position12).getMeetingDayOfWeek()));
                    textViewMeetingDate.setText("" + accountForDailyTransactions.get(position12).getMeetingDayOfMonth());
                    textViewBalance.setText("" + Math.round(accountForDailyTransactions.get(position12).getBalance()));
                    textViewMinimumDeposit.setText("" + Math.round(accountForDailyTransactions.get(position12).getMinimumDeposit()));
                    textViewLastPaymentDate.setText(accountForDailyTransactions.get(position12).getLastAccountDetailsDate());


                    if (accountForDailyTransactions.get(position12).getInstallmentType() == 1) {
                        textViewInstallmentType.setText("WEEKLY");
                    } else if (accountForDailyTransactions.get(position12).getInstallmentType() == 2) {
                        textViewInstallmentType.setText("Monthly");
                    }

                    if (accountForDailyTransactions.get(position12).getLastAccountDetailsDate().equals("NOT FOUND") && accountForDailyTransactions.get(position12).getFlag() > 0) {
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


    private boolean loadData() {
        groupMembers = dataSourceRead.getAllMembers(groupId);
        membersID = new ArrayList<>();
        newMembers = new ArrayList<>();
        membersID.add(0, -12345);
        newMembers.add(0, new SpannableStringBuilder("Select Member"));
        for (int i = 0; i < groupMembers.size(); i++) {

            membersID.add(i + 1, groupMembers.get(i).getId());

            SpannableStringBuilder ltsColor = new SpannableStringBuilder();


            if (groupMembers.get(i).getMemberExtra().getLongTermSavingsTotal() > 0) {
                SpannableStringBuilder ltsTotal = new SpannableStringBuilder(" - " + groupMembers.get(i).getMemberExtra().getLongTermSavingsTotal() + " LTS { ");
                ltsTotal.setSpan(new ForegroundColorSpan(Color.parseColor("#311B92")), 0, ltsTotal.length(), 0);
                ltsColor.append(ltsTotal);
                if (groupMembers.get(i).getMemberExtra().getLongTermSavingsBranch() > 0 && groupMembers.get(i).getMemberExtra().getLongTermSavingsTab() > 0) {
                    SpannableStringBuilder ltsBranch = new SpannableStringBuilder(" " + groupMembers.get(i).getMemberExtra().getLongTermSavingsBranch() + " LTS (B), ");
                    ltsBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#1B5E20")), 0, ltsBranch.length(), 0);
                    ltsColor.append(ltsBranch);
                    SpannableStringBuilder ltsTab = new SpannableStringBuilder(" " + groupMembers.get(i).getMemberExtra().getLongTermSavingsTab() + " LTS (T)");
                    ltsTab.setSpan(new ForegroundColorSpan(Color.parseColor("#F57F17")), 0, ltsTab.length(), 0);
                    ltsColor.append(ltsTab);

                } else if (groupMembers.get(i).getMemberExtra().getLongTermSavingsBranch() > 0) {
                    SpannableStringBuilder ltsBranch = new SpannableStringBuilder(" " + groupMembers.get(i).getMemberExtra().getLongTermSavingsBranch() + " LTS (B)");
                    ltsBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#1B5E20")), 0, ltsBranch.length(), 0);
                    ltsColor.append(ltsBranch);
                } else if (groupMembers.get(i).getMemberExtra().getLongTermSavingsTab() > 0) {
                    SpannableStringBuilder ltsTab = new SpannableStringBuilder(" " + groupMembers.get(i).getMemberExtra().getLongTermSavingsTab() + " LTS (T)");
                    ltsTab.setSpan(new ForegroundColorSpan(Color.parseColor("#F57F17")), 0, ltsTab.length(), 0);
                    ltsColor.append(ltsTab);
                }
                ltsTotal = new SpannableStringBuilder(" }");
                ltsTotal.setSpan(new ForegroundColorSpan(Color.parseColor("#311B92")), 0, ltsTotal.length(), 0);
                ltsColor.append(ltsTotal);
            }

            if (!groupMembers.get(i).getMemberOldOrNew().equals("New")) {
                SpannableStringBuilder main = new SpannableStringBuilder(groupMembers.get(i).getName() + "/" + groupMembers.get(i).getFatherName() + " (" + groupMembers.get(i).getPassbookNumber() + ")");
                main.append(ltsColor);
                newMembers.add(i + 1, main);
            } else {
                SpannableStringBuilder main = new SpannableStringBuilder(groupMembers.get(i).getName() + "/" + groupMembers.get(i).getFatherName() + " (" + groupMembers.get(i).getPassbookNumber() + ")" + " -" + groupMembers.get(i).getMemberOldOrNew());
                main.append(ltsColor);
                newMembers.add(i + 1, main);
            }

        }
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_amms, menu);
        try {
            String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();
            SimpleDateFormat dateFromDatabase = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateFromDatabase.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dfd = dateFromDatabase.parse(currentDate);

            SimpleDateFormat convertedDate = new SimpleDateFormat("EE", Locale.getDefault());
            convertedDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            menu.findItem(R.id.action_working_day).setTitle(currentDate + " " + convertedDate.format(dfd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String branchName = new DataSourceRead(getApplicationContext()).getBranchName();
        menu.findItem(R.id.action_location).setTitle(branchName);
        menu.findItem(R.id.action_close_current_day).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    LongTermSavingsActivity.this);

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

}
