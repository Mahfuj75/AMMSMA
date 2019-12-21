package asa.org.bd.ammsma.activity.insideLogin.insideHome;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.AddNewMemberActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.GroupCollectionActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.LongTermSavingsActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.MemberInformationActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.NewCbsAccountActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.NewSavingAccountActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.PrimarySecondaryLoanActivity;
import asa.org.bd.ammsma.customAdapter.CustomGridAdapter;
import asa.org.bd.ammsma.customAdapter.SpinnerCustomAdapter;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.extra.GroupNameForSpinnerObject;
import asa.org.bd.ammsma.service.ExtraTask;

public class GroupOperationActivity extends AppCompatActivity {


    private int programOfficerId;
    private int spinnerPosition;

    private String loginId;

    private DataSourceRead dataSourceRead;


    private GridView gridViewGroupOperation;


    private int[] imageId = {
            R.drawable.person,
            R.drawable.member_information,
            R.drawable.member_balance,
            R.drawable.new_member,
            R.drawable.long_term_savings,
            R.drawable.loan_disburse,
            R.drawable.cbs_new,
            R.drawable.saving
    };

    private Spinner spinnerGroups;
    private int isSuccessful = 0;
    private List<String> groupNames;
    private List<Integer> groupIdList;
    private List<GroupNameForSpinnerObject> groupNameForSpinnerObjects;
    private List<Integer> defaultProgramIdList;

    private boolean firstTimeBackButtonWork = false;
    private ExtraTask extraTask = new ExtraTask();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_operation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());

        TextView textViewProgramOfficer = findViewById(R.id.textView_programOfficerName);


        gridViewGroupOperation = findViewById(R.id.gridViewGroupOperation);
        dataSourceRead = new DataSourceRead(getApplicationContext());

        String[] gridItemList = getResources().getStringArray(R.array.group_operation_grid_name);

        gridViewGroupOperation.setAdapter(new CustomGridAdapter(GroupOperationActivity.this, gridItemList, imageId));
        spinnerGroups = findViewById(R.id.spinnerGroup);


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        loginId = getIntent().getStringExtra("loginId");


        if (programOfficerId != -1) {
            textViewProgramOfficer.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficer.setText(R.string.user_admin);
        }


        AsyncTaskForSpinner asyncTaskForSpinner = new AsyncTaskForSpinner();
        asyncTaskForSpinner.execute();
        gridItemClickOperation();

        spinnerGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (spinnerPosition != position) {
                    spinnerPosition = position;
                    spinnerGroups.setAdapter(new SpinnerCustomAdapter().arrayListAdapterForSpinner(groupNames, getApplicationContext(), defaultProgramIdList, spinnerPosition));
                    spinnerGroups.setSelection(position);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private int loadSpinnerData() {


        groupIdList = new ArrayList<>();
        groupNames = new ArrayList<>();
        defaultProgramIdList = new ArrayList<>();

        try {
            groupNameForSpinnerObjects = dataSourceRead.getGroupNameBadDebtInLast(programOfficerId);


            groupNames.add(0, "Groups");
            defaultProgramIdList.add(0, -125);

            for (int i = 0; i < groupNameForSpinnerObjects.size(); i++) {


                groupNames.add(groupNameForSpinnerObjects.get(i).getGroupFullName());
                groupIdList.add(groupNameForSpinnerObjects.get(i).getGroupId());
                defaultProgramIdList.add(groupNameForSpinnerObjects.get(i).getDefaultProgramId());
            }


        } catch (Exception e) {
            Log.i("error", e.toString());
        }


        return 1;

    }

    private void gridItemClickOperation() {
        gridViewGroupOperation.setOnItemClickListener((parent, view, position, id) -> {



            if (position == 0) {


                if (spinnerGroups.getSelectedItem() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id14) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {
                    Intent i = new Intent(getApplicationContext(), GroupCollectionActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("loginId", loginId);
                    i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                    i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());
                    i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                    i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                    i.putExtra("spinnerPosition", spinnerGroups.getSelectedItemPosition());
                    i.putExtra("groupTypeId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupTypeId());
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivityForResult(i, 145);

                } else if ((programOfficerId != -1) && spinnerGroups != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("Please select a \"Group\" ")
                            .setPositiveButton("Ok",
                                    (dialog, id1) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("Please login with your  \"Loan-Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id12) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else if (position == 1) {


                if (spinnerGroups.getSelectedItem() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id13) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {
                    Intent i = new Intent(getApplicationContext(), MemberInformationActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("loginId", loginId);

                    i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());

                    String unchangedName;
                    boolean isBadDebt;


                    if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).isMeetingDay()) {

                        if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).isRealizedOrNot() && groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId() == 999) {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " (R)  * ";
                            isBadDebt = true;
                        } else if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).isRealizedOrNot()) {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " (R)  * ";
                            isBadDebt = false;
                        } else if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId() == 999) {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " (R)  * ";
                            isBadDebt = true;
                        } else {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + "  * ";
                            isBadDebt = false;
                        }
                    } else {

                        if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).isRealizedOrNot() && groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId() == 999) {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " (R) ";
                            isBadDebt = true;
                        } else if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).isRealizedOrNot()) {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " (R) ";
                            isBadDebt = false;
                        } else if (groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId() == 999) {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " ";
                            isBadDebt = true;
                        } else {
                            unchangedName = groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName()
                                    + " - " + groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDayName()
                                    + " ";
                            isBadDebt = false;
                        }
                    }
                    i.putExtra("groupName", unchangedName);
                    i.putExtra("isBadDebt", isBadDebt);


                    i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                    i.putExtra("spinnerPosition", spinnerGroups.getSelectedItemPosition());
                    i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivityForResult(i, 145);


                } else if ((programOfficerId != -1) && spinnerGroups != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("Please select a \"Group\" ")
                            .setPositiveButton("Ok",
                                    (dialog, id15) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("Please login with your  \"Loan-Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id16) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else if (position == 2) {

                if (defaultProgramIdList.get(spinnerGroups.getSelectedItemPosition()) == 999) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("Member Balance Report for \"Bad Debt\" group members is not applicable.")
                            .setPositiveButton("Ok",
                                    (dialog, id17) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {

                    if (spinnerGroups.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("NO DATABASE FOUND")
                                .setPositiveButton("Ok",
                                        (dialog, id18) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {
                        Intent i = new Intent(getApplicationContext(), MemberBalanceActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                        i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                        i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivity(i);


                    } else if ((programOfficerId != -1) && spinnerGroups != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please select a \"Group\" ")
                                .setPositiveButton("Ok",
                                        (dialog, id19) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please login with your  \"Loan-Officer\" account")
                                .setPositiveButton("Ok",
                                        (dialog, id131) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }

            } else if (position == 3) {


                if (defaultProgramIdList.get(spinnerGroups.getSelectedItemPosition()) == 999) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("New-Member can't be added on \"BAD-DEBT\" Group")
                            .setPositiveButton("Ok",
                                    (dialog, id110) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {


                    if (spinnerGroups.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("NO DATABASE FOUND")
                                .setPositiveButton("Ok",
                                        (dialog, id111) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {


                        boolean isMemberMaxOut = dataSourceRead.isMemberMaxOut(groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));

                        if (isMemberMaxOut) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    GroupOperationActivity.this);

                            builder.setMessage("Total member can't be more than 35")
                                    .setPositiveButton("Ok",
                                            (dialog, id112) -> dialog.dismiss());
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Intent i = new Intent(getApplicationContext(), AddNewMemberActivity.class);
                            i.putExtra("ProgramOfficerId", programOfficerId);
                            i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                            i.putExtra("loginId", loginId);
                            i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                            i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                            i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());
                            i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                            startActivity(i);
                        }


                    } else if ((programOfficerId != -1) && spinnerGroups != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please select a \"Group\" ")
                                .setPositiveButton("Ok",
                                        (dialog, id113) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please login with your  \"Loan-Officer\" account")
                                .setPositiveButton("Ok",
                                        (dialog, id114) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }

            } else if (position == 4) {


                if (defaultProgramIdList.get(spinnerGroups.getSelectedItemPosition()) == 999) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("LTS can't be added on \"BAD-DEBT\" Group")
                            .setPositiveButton("Ok",
                                    (dialog, id115) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {


                    if (spinnerGroups.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("NO DATABASE FOUND")
                                .setPositiveButton("Ok",
                                        (dialog, id116) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {


                        Intent i = new Intent(getApplicationContext(), LongTermSavingsActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                        i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());
                        i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                        i.putExtra("position", position);
                        i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivity(i);


                    } else if ((programOfficerId != -1) && spinnerGroups != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please select a \"Group\" ")
                                .setPositiveButton("Ok",
                                        (dialog, id117) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please login with your  \"Loan-Officer\" account")
                                .setPositiveButton("Ok",
                                        (dialog, id118) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }

            } else if (position == 5) {
                if (defaultProgramIdList.get(spinnerGroups.getSelectedItemPosition()) == 999) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("Loan can't be added on \"BAD-DEBT\" Group")
                            .setPositiveButton("Ok",
                                    (dialog, id119) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {


                    if (spinnerGroups.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("NO DATABASE FOUND")
                                .setPositiveButton("Ok",
                                        (dialog, id120) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {


                        Intent i = new Intent(getApplicationContext(), PrimarySecondaryLoanActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                        i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());
                        i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                        i.putExtra("position", position);
                        i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                        i.putExtra("groupTypeId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupTypeId());
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivity(i);


                    } else if ((programOfficerId != -1) && spinnerGroups != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please select a \"Group\" ")
                                .setPositiveButton("Ok",
                                        (dialog, id121) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please login with your  \"Loan-Officer\" account")
                                .setPositiveButton("Ok",
                                        (dialog, id122) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }
            } else if (position == 6) {


                if (defaultProgramIdList.get(spinnerGroups.getSelectedItemPosition()) == 999) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("CBS can't be added on \"BAD-DEBT\" Group")
                            .setPositiveButton("Ok",
                                    (dialog, id123) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(dataSourceRead.getBranchType()==3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("You are not allowed to add CBS account in MSME Branch")
                            .setPositiveButton("Ok",
                                    (dialog, id123) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {


                    if (spinnerGroups.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("NO DATABASE FOUND")
                                .setPositiveButton("Ok",
                                        (dialog, id124) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {


                        Intent i = new Intent(getApplicationContext(), NewCbsAccountActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                        i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());
                        i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                        i.putExtra("position", position);
                        i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivity(i);


                    } else if ((programOfficerId != -1) && spinnerGroups != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please select a \"Group\" ")
                                .setPositiveButton("Ok",
                                        (dialog, id125) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please login with your  \"Loan-Officer\" account")
                                .setPositiveButton("Ok",
                                        (dialog, id126) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }

            } else if (position == 7) {


                if (defaultProgramIdList.get(spinnerGroups.getSelectedItemPosition()) == 999) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            GroupOperationActivity.this);

                    builder.setMessage("\'Primary-Saving-Account\' can't be added on \"BAD-DEBT\" Group")
                            .setPositiveButton("Ok",
                                    (dialog, id127) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {


                    if (spinnerGroups.getSelectedItem() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("NO DATABASE FOUND")
                                .setPositiveButton("Ok",
                                        (dialog, id128) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (!spinnerGroups.getSelectedItem().toString().equals("Groups") || spinnerGroups.getSelectedItem().toString().equals("")) {


                        Intent i = new Intent(getApplicationContext(), NewSavingAccountActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", spinnerGroups.getSelectedItem().toString());
                        i.putExtra("realGroupName", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getGroupName());
                        i.putExtra("groupId", groupIdList.get(spinnerGroups.getSelectedItemPosition() - 1));
                        i.putExtra("position", position);
                        i.putExtra("defaultProgramId", groupNameForSpinnerObjects.get(spinnerGroups.getSelectedItemPosition() - 1).getDefaultProgramId());
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivity(i);


                    } else if ((programOfficerId != -1) && spinnerGroups != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please select a \"Group\" ")
                                .setPositiveButton("Ok",
                                        (dialog, id129) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                GroupOperationActivity.this);

                        builder.setMessage("Please login with your  \"Loan-Officer\" account")
                                .setPositiveButton("Ok",
                                        (dialog, id130) -> dialog.dismiss());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 145) {
            if (resultCode == RESULT_OK) {
                spinnerPosition = spinnerGroups.getSelectedItemPosition();
                AsyncTaskForSpinner asyncTaskForSpinner = new AsyncTaskForSpinner();
                asyncTaskForSpinner.execute();

            }
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

            if (firstTimeBackButtonWork) {
                Intent intent = new Intent();
                intent.putExtra("GroupRefresh", spinnerPosition);
                setResult(RESULT_OK, intent);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                finish();
            }
            return true;
        } else if (id == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    GroupOperationActivity.this);

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
    protected void onResume() {
        super.onResume();


        try {
            AsyncTaskForSpinner asyncTaskForSpinner = new AsyncTaskForSpinner();
            asyncTaskForSpinner.execute();
        } catch (Exception e) {
            Log.i("Error", e.getLocalizedMessage());
        }
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForSpinner extends AsyncTask<Void, Integer, Void> {
        Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            firstTimeBackButtonWork = false;

            dialog = new Dialog(GroupOperationActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            isSuccessful = loadSpinnerData();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (isSuccessful == 1) {
                try {
                    spinnerGroups.setAdapter(new SpinnerCustomAdapter().arrayListAdapterForSpinner(groupNames, getApplicationContext(), defaultProgramIdList, spinnerPosition));
                    spinnerGroups.setSelection(spinnerPosition);

                    spinnerPosition = spinnerGroups.getSelectedItemPosition();


                    dialog.dismiss();

                    firstTimeBackButtonWork = true;

                } catch (Exception e) {
                    Log.i("Error", e.getMessage());
                    firstTimeBackButtonWork = true;
                }


            }

        }
    }


}
