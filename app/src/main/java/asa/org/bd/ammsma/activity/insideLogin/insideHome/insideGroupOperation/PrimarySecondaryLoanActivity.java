package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.customAdapter.LonaInformationCustomAdapterForListView;
import asa.org.bd.ammsma.customAdapter.SpinnerCustomAdapter;
import asa.org.bd.ammsma.customListView.CustomNonScrollerListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.extendedObject.AccountForDailyTransaction;
import asa.org.bd.ammsma.extra.GroupNameForSpinnerObject;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;
import asa.org.bd.ammsma.service.ExtraTask;

public class PrimarySecondaryLoanActivity extends AppCompatActivity {

    private boolean groupLoadSuccessful = false;
    private boolean firstTime = false;

    private int selectedMemberId;
    private int groupTypeId;
    private int groupId;
    private int programOfficerId;

    private List<Integer> membersID;
    private List<Integer> groupIdList = new ArrayList<>();
    private List<String> groupNames = new ArrayList<>();
    private List<SpannableStringBuilder> newMembers = new ArrayList<>();
    private List<Integer> programIdList = new ArrayList<>();
    private String groupName;
    private String loginId;
    private String programOfficerName;
    private List<Member> groupMembers = new ArrayList<>();
    private List<AccountForDailyTransaction> loanAccountInformation;
    private List<GroupNameForSpinnerObject> groupNameForSpinnerObjects = new ArrayList<>();


    private int spinnerMemberSelectPosition = 0;


    private Toolbar toolbar;

    private Button buttonCreateLoan;

    private Spinner spinnerGroup;
    private Spinner spinnerMembers;

    private DataSourceRead dataSourceRead;

    private TextView textViewNoGroupSelected;
    private TextView textViewGroupName;

    private ScrollView scrollViewPrimarySecondaryLoan;
    private CustomNonScrollerListView listViewLoanAccounts;

    private DataSourceWrite dataSourceWrite;
    private ExtraTask extraTask = new ExtraTask();

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_secondary_loan);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);


        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        dataSourceRead = new DataSourceRead(getApplicationContext());
        spinnerGroup = findViewById(R.id.spinnerGroup);
        spinnerMembers = findViewById(R.id.spinnerMembers);
        textViewGroupName = findViewById(R.id.textViewGroupName);
        textViewNoGroupSelected = findViewById(R.id.textViewNoGroupSelected);
        scrollViewPrimarySecondaryLoan = findViewById(R.id.scrollViewPrimarySecondaryLoan);
        listViewLoanAccounts = findViewById(R.id.listViewLoanAccounts);
        buttonCreateLoan = findViewById(R.id.buttonCreateLoan);
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        programOfficerName = getIntent().getStringExtra("programOfficerName");
        loginId = getIntent().getStringExtra("loginId");
        groupName = getIntent().getStringExtra("groupName");
        groupId = getIntent().getIntExtra("groupId", 0);
        groupTypeId = getIntent().getIntExtra("groupTypeId", 1);

        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(programOfficerName);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        toolbarNavigationClick();

        if (groupId != 0) {

            spinnerGroup.setVisibility(View.GONE);
            textViewGroupName.setText(groupName);
            textViewNoGroupSelected.setText("No Member Selected");
            AsyncTaskForSpinnerMemberData asyncTaskForSpinnerMemberData = new AsyncTaskForSpinnerMemberData();
            asyncTaskForSpinnerMemberData.execute();


        } else {
            AsyncTaskForGroupListLoad asyncTaskForGroupListLoad = new AsyncTaskForGroupListLoad();
            asyncTaskForGroupListLoad.execute();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        buttonCreateLoan.setOnClickListener(v -> {
            if(dataSourceRead.hasNIdOrBirthCertificate(membersID.get(spinnerMembers.getSelectedItemPosition())))
            {



                firstTime = true;
                Intent i = new Intent(getApplicationContext(), LoanDisburseActivity.class);
                i.putExtra("ProgramOfficerId", programOfficerId);
                i.putExtra("programOfficerName", programOfficerName);
                i.putExtra("loginId", loginId);
                i.putExtra("groupName", textViewGroupName.getText().toString().trim());
                if (groupId > 0) {
                    i.putExtra("groupId", groupId);
                } else {
                    i.putExtra("groupId", groupIdList.get(spinnerGroup.getSelectedItemPosition()));
                }
                i.putExtra("memberName", newMembers.get(spinnerMembers.getSelectedItemPosition()).toString());
                i.putExtra("memberId", membersID.get(spinnerMembers.getSelectedItemPosition()));
                i.putExtra("memberSex", groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getSex());
                if (spinnerGroup.getVisibility() == View.VISIBLE) {
                    i.putExtra("groupTypeId", groupNameForSpinnerObjects.get(spinnerGroup.getSelectedItemPosition() - 1).getGroupTypeId());
                } else {
                    i.putExtra("groupTypeId", groupTypeId);
                }
                i.putExtra("loanGroupProgramId", groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId());
                i.putIntegerArrayListExtra("programIdList", (ArrayList<Integer>) programIdList);

                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                startActivity(i);
            }
            else
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PrimarySecondaryLoanActivity.this);

                builder.setMessage(
                        "Loan can't be disbursed without member's NID/Birth registration number")
                        .setCancelable(false)
                        .setTitle("Error")
                        .setPositiveButton("Ok",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

            }




        });
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }

    private boolean loadSpinnerGroupData() {

        groupIdList.clear();
        groupNames.clear();

        try {
            groupNameForSpinnerObjects = dataSourceRead.getGroupNameWithoutBadDebt(programOfficerId);
            groupNames.add(0, "Select Group");
            groupIdList.add(0, -55);
            for (int i = 0; i < groupNameForSpinnerObjects.size(); i++) {

                if (groupNameForSpinnerObjects.get(i).isMeetingDay()) {
                    if (groupNameForSpinnerObjects.get(i).isRealizedOrNot()) {
                        groupNames.add(
                                groupNameForSpinnerObjects.get(i).getGroupName()
                                        + " - " + groupNameForSpinnerObjects.get(i).getDayName()
                                        + " (R)  * (" + groupNameForSpinnerObjects.get(i).getTotalMember() + ")");
                    } else {
                        groupNames.add(
                                groupNameForSpinnerObjects.get(i).getGroupName()
                                        + " - " + groupNameForSpinnerObjects.get(i).getDayName()
                                        + "  * (" + groupNameForSpinnerObjects.get(i).getTotalMember() + ")");
                    }
                } else {
                    if (groupNameForSpinnerObjects.get(i).isRealizedOrNot()) {
                        groupNames.add(
                                groupNameForSpinnerObjects.get(i).getGroupName()
                                        + " - " + groupNameForSpinnerObjects.get(i).getDayName()
                                        + " (R) (" + groupNameForSpinnerObjects.get(i).getTotalMember() + ")");
                    } else {
                        groupNames.add(
                                groupNameForSpinnerObjects.get(i).getGroupName()
                                        + " - " + groupNameForSpinnerObjects.get(i).getDayName()
                                        + " (" + groupNameForSpinnerObjects.get(i).getTotalMember() + ")");
                    }
                }

                groupIdList.add(groupNameForSpinnerObjects.get(i).getGroupId());
            }


        } catch (Exception e) {
            Log.i("error", e.toString());
        }

        return true;

    }

    @SuppressLint("SetTextI18n")
    private void loadSpinnerForGroup() {
        textViewGroupName.setText("No Group");
        spinnerGroup.setAdapter(new SpinnerCustomAdapter().arrayListAdapterForSpinnerLoan(groupNames, getApplicationContext()));
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position != 0) {

                    textViewGroupName.setText(spinnerGroup.getSelectedItem().toString().trim());
                    textViewNoGroupSelected.setText("No Member Selected");
                    scrollViewPrimarySecondaryLoan.setVisibility(View.GONE);
                    AsyncTaskForSpinnerMemberData asyncTaskForSpinnerMemberData = new AsyncTaskForSpinnerMemberData();
                    asyncTaskForSpinnerMemberData.execute();

                } else {
                    textViewGroupName.setText("No Group");
                    scrollViewPrimarySecondaryLoan.setVisibility(View.GONE);
                    textViewNoGroupSelected.setVisibility(View.VISIBLE);
                    scrollViewPrimarySecondaryLoan.setVisibility(View.GONE);
                    textViewNoGroupSelected.setText("No Group Selected");
                    spinnerMembers.setAdapter(null);
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();



        if (firstTime) {
            spinnerMemberSelectPosition = spinnerMembers.getSelectedItemPosition();
            AsyncTaskForSpinnerMemberData asyncTaskForSpinnerMemberData = new AsyncTaskForSpinnerMemberData();
            asyncTaskForSpinnerMemberData.execute();
        }


        if (firstTime) {
            buttonCreateLoan.setVisibility(View.VISIBLE);
            loanAccountInformation = dataSourceRead.getLoanAccountsOfMembers(selectedMemberId);
            programIdList = new ArrayList<>();
            /*for (int i = 0; i < loanAccountInformation.size(); i++) {
                programIdList.add(i, loanAccountInformation.get(i).getProgramId());
            }*/
            for (int i = 0; i < loanAccountInformation.size(); i++) {
                if(loanAccountInformation.get(i).getBalance()!=0)
                {
                    programIdList.add(loanAccountInformation.get(i).getProgramId());
                }


            }
            dataSourceRead.getProgram(groupTypeId, groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId(), programIdList);
            if (dataSourceRead.hasPrimaryLoanOutstandingNotZero(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getId())) {
                buttonCreateLoan.setVisibility(View.GONE);
            } else {
                buttonCreateLoan.setVisibility(View.VISIBLE);
            }
            loanAccountInformation = dataSourceRead.getLoanAccountsOfMembers(selectedMemberId);
            listViewLoanAccounts.setAdapter(new LonaInformationCustomAdapterForListView(getApplicationContext(), loanAccountInformation, position -> {
                dataSourceWrite.deleteLoanAccount(loanAccountInformation.get(position).getAccountId());
                loanAccountInformation = dataSourceRead.getLoanAccountsOfMembers(selectedMemberId);
                listViewLoanAccounts.invalidateViews();
                listViewLoanAccounts.invalidate();


                programIdList = new ArrayList<>();
                /*for (int i = 0; i < loanAccountInformation.size(); i++) {
                    programIdList.add(i, loanAccountInformation.get(i).getProgramId());

                }*/
                for (int i = 0; i < loanAccountInformation.size(); i++) {
                    if(loanAccountInformation.get(i).getBalance()!=0)
                    {
                        programIdList.add(loanAccountInformation.get(i).getProgramId());
                    }


                }

                dataSourceRead.getProgram(groupTypeId, groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId(), programIdList);
                if (dataSourceRead.hasPrimaryLoanOutstandingNotZero(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getId())) {
                    buttonCreateLoan.setVisibility(View.GONE);
                } else {
                    buttonCreateLoan.setVisibility(View.VISIBLE);
                }


            }));

            listViewItemOnClickEvent();

        }

        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

    }

    private void listViewItemOnClickEvent() {
        listViewLoanAccounts.setOnItemClickListener((parent, view, position, id) -> {

            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

            if (loanAccountInformation.get(position).getFlag() == 1) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrimarySecondaryLoanActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams")
                View convertView = inflater.inflate(R.layout.dialog_for_mermers_loan, null);
                alertDialog.setView(convertView);
                alertDialog.setCancelable(false);

                TextView textViewLoanName = convertView.findViewById(R.id.textViewLoanName);
                TextView textViewOpeningDate = convertView.findViewById(R.id.textViewOpeningDate);
                TextView textViewDisburseAmount = convertView.findViewById(R.id.textViewDisburseAmount);
                TextView textViewServiceCharge = convertView.findViewById(R.id.textViewServiceCharge);
                TextView textViewOutstandingAmount = convertView.findViewById(R.id.textViewOutstandingAmount);
                TextView textViewInstallmentAmount = convertView.findViewById(R.id.textViewInstallmentAmount);
                TextView textViewDuration = convertView.findViewById(R.id.textViewDuration);
                TextView textViewFundType = convertView.findViewById(R.id.textViewFundType);
                TextView textViewInstallmentType = convertView.findViewById(R.id.textViewInstallmentType);
                TextView textViewSchemeType = convertView.findViewById(R.id.textViewSchemeType);
                TextView textViewLoanInsurance = convertView.findViewById(R.id.textViewLoanInsurance);
                TextView textViewInstallmentNumber = convertView.findViewById(R.id.textViewInstallmentNumber);

                textViewLoanName.setText(titleCase(loanAccountInformation.get(position).getProgramName()));
                textViewOpeningDate.setText((loanAccountInformation.get(position).getOpeningDateValue()));
                textViewDisburseAmount.setText(String.valueOf(Math.round(loanAccountInformation.get(position).getDisbursedAmount())));
                textViewServiceCharge.setText(String.valueOf(Math.round(loanAccountInformation.get(position).getServiceChargeAmount())));
                textViewOutstandingAmount.setText(String.valueOf(Math.round(loanAccountInformation.get(position).getBalance())));
                textViewInstallmentAmount.setText(String.valueOf(Math.round(loanAccountInformation.get(position).getBaseInstallmentAmount())));
                textViewDuration.setText(String.valueOf(Math.round(loanAccountInformation.get(position).getDuration())));
                textViewFundType.setText(titleCase(dataSourceRead.getFundName(loanAccountInformation.get(position).getFundId())));
                textViewInstallmentType.setText(titleCase(dataSourceRead.getInstallmentTypeName(loanAccountInformation.get(position).getInstallmentType())));
                textViewSchemeType.setText(titleCase(dataSourceRead.getSchemeName(loanAccountInformation.get(position).getSchemeId())));
                textViewLoanInsurance.setText(String.valueOf(Math.round(loanAccountInformation.get(position).getLoanInsuranceAmount())));
                textViewInstallmentNumber.setText(String.valueOf(loanAccountInformation.get(position).getInstallmentNumber()));
                alertDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());


                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }


        });
    }

    private void loadData() {

        if (groupId > 0) {
            groupMembers = dataSourceRead.getAllMembers(groupId);
        } else {
            groupMembers = dataSourceRead.getAllMembers(groupIdList.get(spinnerGroup.getSelectedItemPosition()));
        }

        membersID = new ArrayList<>();
        newMembers = new ArrayList<>();
        membersID.add(0, -12345);
        newMembers.add(0, new SpannableStringBuilder("Select Member"));
        for (int i = 0; i < groupMembers.size(); i++) {

            membersID.add(i + 1, groupMembers.get(i).getId());


            SpannableStringBuilder loanNameColor = new SpannableStringBuilder();


            boolean isEmptyLoanNameColor = loanNameColor.toString().trim().equals("");
            if (groupMembers.get(i).getMemberExtra().getPrimaryBranchLoan() > 0) {
                if (isEmptyLoanNameColor) {

                    SpannableStringBuilder primaryLoanBranch = new SpannableStringBuilder(" - " + groupMembers.get(i).getMemberExtra().getPrimaryBranchLoan() + " Primary ");
                    primaryLoanBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#311B92")), 0, primaryLoanBranch.length(), 0);
                    loanNameColor.append(primaryLoanBranch);

                } else {

                    SpannableStringBuilder primaryLoanBranch = new SpannableStringBuilder(", " + groupMembers.get(i).getMemberExtra().getPrimaryBranchLoan() + " Primary");
                    primaryLoanBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#311B92")), 0, primaryLoanBranch.length(), 0);
                    loanNameColor.append(primaryLoanBranch);
                }

            }
            if (groupMembers.get(i).getMemberExtra().getPrimaryTabLoan() > 0) {
                if (isEmptyLoanNameColor) {

                    SpannableStringBuilder primaryLoanTab = new SpannableStringBuilder(" - " + groupMembers.get(i).getMemberExtra().getPrimaryTabLoan() + " Primary (Tab) ");
                    primaryLoanTab.setSpan(new ForegroundColorSpan(Color.parseColor("#E65100")), 0, primaryLoanTab.length(), 0);
                    loanNameColor.append(primaryLoanTab);
                } else {

                    SpannableStringBuilder primaryLoanTab = new SpannableStringBuilder(", " + groupMembers.get(i).getMemberExtra().getPrimaryTabLoan() + " Primary (Tab) ");
                    primaryLoanTab.setSpan(new ForegroundColorSpan(Color.parseColor("#E65100")), 0, primaryLoanTab.length(), 0);
                    loanNameColor.append(primaryLoanTab);
                }
            }
            if (groupMembers.get(i).getMemberExtra().getSecondaryBranchLoan() > 0) {
                if (isEmptyLoanNameColor) {

                    SpannableStringBuilder secondaryLoanBranch = new SpannableStringBuilder(" - " + groupMembers.get(i).getMemberExtra().getSecondaryBranchLoan() + " Secondary");
                    secondaryLoanBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#1B5E20")), 0, secondaryLoanBranch.length(), 0);
                    loanNameColor.append(secondaryLoanBranch);
                } else {


                    SpannableStringBuilder secondaryLoanBranch = new SpannableStringBuilder(", " + groupMembers.get(i).getMemberExtra().getSecondaryBranchLoan() + " Secondary");
                    secondaryLoanBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#1B5E20")), 0, secondaryLoanBranch.length(), 0);
                    loanNameColor.append(secondaryLoanBranch);
                }

            }
            if (groupMembers.get(i).getMemberExtra().getSupplementaryLoan() > 0) {
                if (isEmptyLoanNameColor) {

                    SpannableStringBuilder supplementaryLoanBranch = new SpannableStringBuilder(" - " + groupMembers.get(i).getMemberExtra().getSupplementaryLoan() + " Supplementary");
                    supplementaryLoanBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#3E2723")), 0, supplementaryLoanBranch.length(), 0);
                    loanNameColor.append(supplementaryLoanBranch);
                } else {

                    SpannableStringBuilder supplementaryLoanBranch = new SpannableStringBuilder(", " + groupMembers.get(i).getMemberExtra().getSupplementaryLoan() + " Supplementary");
                    supplementaryLoanBranch.setSpan(new ForegroundColorSpan(Color.parseColor("#3E2723")), 0, supplementaryLoanBranch.length(), 0);
                    loanNameColor.append(supplementaryLoanBranch);
                }
            }

            if (!groupMembers.get(i).getMemberOldOrNew().equals("New")) {
                SpannableStringBuilder main = new SpannableStringBuilder(groupMembers.get(i).getName() + "/" + groupMembers.get(i).getFatherName() + " (" + groupMembers.get(i).getPassbookNumber() + ") ");
                main.append(loanNameColor);

                newMembers.add(i + 1, main);
            } else {
                SpannableStringBuilder main = new SpannableStringBuilder(groupMembers.get(i).getName() + "/" + groupMembers.get(i).getFatherName() + " (" + groupMembers.get(i).getPassbookNumber() + ")" + " -" + groupMembers.get(i).getMemberOldOrNew() + " ");
                main.append(loanNameColor);
                newMembers.add(i + 1, main);
            }

        }
    }

    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
        });
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

            String day = convertedDate.format(dfd);
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
        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    PrimarySecondaryLoanActivity.this);

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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForGroupListLoad extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(PrimarySecondaryLoanActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);

            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            groupLoadSuccessful = loadSpinnerGroupData();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (groupLoadSuccessful) {


                loadSpinnerForGroup();
                groupLoadSuccessful = false;
                dialog.dismiss();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForSpinnerMemberData extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(PrimarySecondaryLoanActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadData();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            spinnerMembers.setAdapter(new SpinnerCustomAdapter().arrayAdapterForMemberListMembersLoan(getApplicationContext(), newMembers));
            selectedMemberId = membersID.get(spinnerMembers.getSelectedItemPosition());


            spinnerMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

                    selectedMemberId = membersID.get(position);


                    if (position == 0) {

                        if (groupId > 0) {
                            textViewGroupName.setText(groupName);
                        } else {
                            textViewGroupName.setText(spinnerGroup.getSelectedItem().toString().trim());
                        }

                        buttonCreateLoan.setVisibility(View.GONE);
                        textViewNoGroupSelected.setText("No Member Selected");
                        scrollViewPrimarySecondaryLoan.setVisibility(View.GONE);
                        textViewNoGroupSelected.setVisibility(View.VISIBLE);


                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getMemberOldOrNew().equals("New")) {
                        buttonCreateLoan.setVisibility(View.GONE);
                        textViewNoGroupSelected.setText("Disbursing loan to \"New Member\" is not allowed from this system");
                        scrollViewPrimarySecondaryLoan.setVisibility(View.GONE);
                        textViewNoGroupSelected.setVisibility(View.VISIBLE);
                    } else if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145 && dataSourceRead.getLoanAccountsOfMembers(selectedMemberId).size() == 0) {
                        buttonCreateLoan.setVisibility(View.GONE);
                        textViewNoGroupSelected.setText("MSME loans can't be disbursed from this system");
                        scrollViewPrimarySecondaryLoan.setVisibility(View.GONE);
                        textViewNoGroupSelected.setVisibility(View.VISIBLE);
                    } else {

                        if (groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() > 140 && groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId() < 145) {
                            buttonCreateLoan.setVisibility(View.GONE);
                        } else {
                            buttonCreateLoan.setVisibility(View.VISIBLE);
                        }

                        textViewNoGroupSelected.setVisibility(View.GONE);
                        scrollViewPrimarySecondaryLoan.setVisibility(View.VISIBLE);
                        loanAccountInformation = dataSourceRead.getLoanAccountsOfMembers(selectedMemberId);
                        programIdList = new ArrayList<>();
                        /*for (int i = 0; i < loanAccountInformation.size(); i++) {
                            programIdList.add(i, loanAccountInformation.get(i).getProgramId());

                        }*/
                        for (int i = 0; i < loanAccountInformation.size(); i++) {
                            if(loanAccountInformation.get(i).getBalance()!=0)
                            {
                                programIdList.add( loanAccountInformation.get(i).getProgramId());
                            }


                        }

                        dataSourceRead.getProgram(groupTypeId, groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId(), programIdList);
                        if (dataSourceRead.hasPrimaryLoanOutstandingNotZero(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getId())) {
                            buttonCreateLoan.setVisibility(View.GONE);
                        } else {

                            buttonCreateLoan.setVisibility(View.VISIBLE);
                        }
                        listViewLoanAccounts.setAdapter(new LonaInformationCustomAdapterForListView(getApplicationContext(), loanAccountInformation, position1 -> {
                            dataSourceWrite.deleteLoanAccount(loanAccountInformation.get(position1).getAccountId());
                            loanAccountInformation = dataSourceRead.getLoanAccountsOfMembers(selectedMemberId);
                            listViewLoanAccounts.invalidateViews();
                            listViewLoanAccounts.invalidate();
                            spinnerMemberSelectPosition = spinnerMembers.getSelectedItemPosition();
                            AsyncTaskForSpinnerMemberData asyncTaskForSpinnerMemberData = new AsyncTaskForSpinnerMemberData();
                            asyncTaskForSpinnerMemberData.execute();


                            programIdList = new ArrayList<>();
                            for (int i = 0; i < loanAccountInformation.size(); i++) {
                                programIdList.add(loanAccountInformation.get(i).getProgramId());

                            }

                            dataSourceRead.getProgram(groupTypeId, groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getProgramId(), programIdList);
                            if (dataSourceRead.hasPrimaryLoanOutstandingNotZero(groupMembers.get(spinnerMembers.getSelectedItemPosition() - 1).getId())) {
                                buttonCreateLoan.setVisibility(View.GONE);
                            } else {
                                buttonCreateLoan.setVisibility(View.VISIBLE);
                            }


                        }));
                        listViewItemOnClickEvent();

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinnerMembers.setSelection(spinnerMemberSelectPosition);
            dialog.dismiss();


        }
    }

}
