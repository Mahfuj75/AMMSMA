package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;
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
import asa.org.bd.ammsma.service.ExtraTask;
import asa.org.bd.ammsma.customAdapter.MemberInformationCustomAdapterForListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;

public class MemberInformationActivity extends AppCompatActivity {
    private DataSourceRead dataSourceRead;

    private String groupName;
    private boolean isBadDebt;
    private String loginId;
    private String realGroupName;
    private int groupId;
    private int programOfficerId;
    private int defaultProgramId;

    private List<Member> members = new ArrayList<>();
    private ListView listViewMemberInformation;
    private Toolbar toolbar;
    private static final int CELL_PHONE_PERMISSION = 674;
    private TextView textViewGroupName;
    private TextInputEditText textInputEditTextSearchMember;
    private ExtraTask extraTask = new ExtraTask();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_information);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logowith_space);


        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        textViewGroupName = findViewById(R.id.textView_groupName);
        listViewMemberInformation = findViewById(R.id.listViewMemberInformation);
        textInputEditTextSearchMember = findViewById(R.id.textInputEditTextSearchMember);

        dataSourceRead = new DataSourceRead(getApplicationContext());

        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        loginId = getIntent().getStringExtra("loginId");
        groupName = getIntent().getStringExtra("groupName");
        isBadDebt = getIntent().getBooleanExtra("isBadDebt", false);
        groupId = getIntent().getIntExtra("groupId", 0);
        realGroupName = getIntent().getStringExtra("realGroupName");
        defaultProgramId = getIntent().getIntExtra("defaultProgramId", 0);


        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
        } else {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        textViewGroupName.setText(groupName);


        setListViewAdapter();


        int phonePermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE);

        if (phonePermissionCheck < 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(MemberInformationActivity.this)
                        .setTitle("Cellphone Permission")
                        .setMessage("Need cellphone Permission for call functionality")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MemberInformationActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    CELL_PHONE_PERMISSION);
                        })
                        .create()
                        .show();
            }
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (defaultProgramId == 999) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MemberInformationActivity.this);

                builder.setMessage("New-Member can't be added on \"BAD-DEBT\" Group")
                        .setPositiveButton("Ok",
                                (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                boolean isMemberMaxOut = dataSourceRead.isMemberMaxOut(groupId);

                if (isMemberMaxOut) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            MemberInformationActivity.this);

                    builder.setMessage("Total member can't be more than 35")
                            .setPositiveButton("Ok",
                                    (dialog, id) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                } else {
                    Intent i = new Intent(getApplicationContext(), AddNewMemberActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                    i.putExtra("loginId", loginId);
                    i.putExtra("groupName", textViewGroupName.getText().toString().trim());
                    i.putExtra("groupId", groupId);
                    i.putExtra("realGroupName", realGroupName);
                    i.putExtra("defaultProgramId", defaultProgramId);
                    i.putExtra("update", 0);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                }


            }


        });
        toolbarNavigationClick();


        textInputEditTextSearchMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setListViewAdapter();
            }
        });

        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
        setListViewAdapter();

    }


    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
        });
    }

    private void setListViewAdapter() {
        members = dataSourceRead.getGroupMemberInformation(groupId, textInputEditTextSearchMember.getText().toString().trim());
        setGroupName(members.size());
        MemberInformationCustomAdapterForListView customAdapterForListView = new MemberInformationCustomAdapterForListView(getApplicationContext(),
                members, defaultProgramId, programOfficerId, loginId, groupName, groupId, realGroupName,
                new MemberInformationCustomAdapterForListView.DataChangeListener() {
                    @Override
                    public void onDataChange(int position) {
                        DataSourceWrite dataSourceWrite = new DataSourceWrite(getApplicationContext());
                        dataSourceWrite.deleteNewMember(members.get(position).getId());
                        members = dataSourceRead.getGroupMemberInformation(groupId, textInputEditTextSearchMember.getText().toString().trim());
                        setListViewAdapter();


                    }

                    @Override
                    public void onRefresh(int position) {
                        members = dataSourceRead.getGroupMemberInformation(groupId, textInputEditTextSearchMember.getText().toString().trim());
                        setListViewAdapter();
                    }

            /*@Override
            public void onUpdate(int position) {
               *//* Intent i = new Intent(getApplicationContext(),AddNewMemberActivity.class);
                i.putExtra("ProgramOfficerId", programOfficerId);
                i.putExtra("programOfficerName",dataSourceRead.getLOName(programOfficerId) +" - "+ loginId);
                i.putExtra("loginId", loginId);
                i.putExtra("groupName",groupName);
                i.putExtra("groupId",groupId);
                i.putExtra("realGroupName",realGroupName);
                i.putExtra("defaultProgramId",defaultProgramId);
                i.putExtra("update",75);
                i.putExtra("memberId",members.get(position).getId());
                startActivity(i);*//*
            }*/
                });
        listViewMemberInformation.setAdapter(customAdapterForListView);
        listViewMemberInformation.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

                InputMethodManager inputMethodManger = (InputMethodManager) getApplicationContext().getSystemService(Activity
                        .INPUT_METHOD_SERVICE);
                assert inputMethodManger != null;
                inputMethodManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setGroupName(int memberTotal) {

        if (isBadDebt) {
            textViewGroupName.setText(groupName + "(" + memberTotal + ") " + "{B}");
        } else {
            textViewGroupName.setText(groupName + "(" + memberTotal + ")");
        }


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

        if (item.getItemId() == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    MemberInformationActivity.this);

            builder.setMessage(
                    "Are you sure Log-Out from Application?")
                    .setCancelable(false)
                    .setTitle("Log Out")
                    .setPositiveButton("Yes",
                            (dialog, id) -> {
                                Intent i = new Intent(getApplicationContext(), LoginAmmsActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
                                startActivity(i);
                                finish();
                            })
                    .setNegativeButton("No",
                            (dialog, id) -> dialog.cancel());
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

}
