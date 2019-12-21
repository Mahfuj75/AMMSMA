package asa.org.bd.ammsma.activity.insideLogin.insideHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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
import asa.org.bd.ammsma.customAdapter.MemberSearchCustomAdapterForListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.extra.SearchData;
import asa.org.bd.ammsma.service.ExtraTask;

public class MemberSearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private int programOfficerId;
    private DataSourceRead dataSourceRead;

    private TextInputEditText textInputEditTextGroupName;
    private TextInputEditText textInputEditTextMemberName;
    private TextInputEditText textInputEditTextFatherOrHusband;
    private TextInputEditText textInputEditTextNationalIdNumber;

    private ListView listViewSearchMember;

    private HorizontalScrollView horizontalScrollview;
    private TextView textViewInsetSearchData;

    private String searchGroupName;
    private String searchMemberName;
    private String searchFatherOrHusband;
    private String searchNationalId;
    private List<SearchData> searchDataList = new ArrayList<>();
    private ExtraTask extraTask = new ExtraTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_search);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);

        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        String loginId = getIntent().getStringExtra("loginId");


        dataSourceRead = new DataSourceRead(getApplicationContext());

        textInputEditTextGroupName = findViewById(R.id.textInputEditTextGroupName);
        textInputEditTextMemberName = findViewById(R.id.textInputEditTextMemberName);
        textInputEditTextFatherOrHusband = findViewById(R.id.textInputEditTextFatherOrHusband);
        textInputEditTextNationalIdNumber = findViewById(R.id.textInputEditTextNationalIdNumber);

        listViewSearchMember = findViewById(R.id.listViewSearchMember);

        horizontalScrollview = findViewById(R.id.horizontalScrollview);
        textViewInsetSearchData = findViewById(R.id.textViewInsetSearchData);
        Button buttonRefresh = findViewById(R.id.buttonRefresh);


        String programOfficerName;
        if (programOfficerId != -1) {
            programOfficerName = dataSourceRead.getLOName(programOfficerId) + " - " + loginId;
            textViewProgramOfficerName.setText(programOfficerName);
        }
        if (programOfficerId == -1) {
            programOfficerName = getResources().getString(R.string.user_admin);
            textViewProgramOfficerName.setText(programOfficerName);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        toolbarNavigationClick();
        watcherEditText();

        buttonRefresh.setOnClickListener(v -> {
            horizontalScrollview.setVisibility(View.GONE);
            textViewInsetSearchData.setVisibility(View.VISIBLE);
            textInputEditTextGroupName.setText("");
            textInputEditTextMemberName.setText("");
            textInputEditTextFatherOrHusband.setText("");
            textInputEditTextNationalIdNumber.setText("");
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

        });
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }


    private void watcherEditText() {
        textInputEditTextGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchGroupName = textInputEditTextGroupName.getText().toString().trim();
                searchMemberName = textInputEditTextMemberName.getText().toString().trim();
                searchFatherOrHusband = textInputEditTextFatherOrHusband.getText().toString().trim();
                searchNationalId = textInputEditTextNationalIdNumber.getText().toString().trim();

                if (searchGroupName.equals("") && searchMemberName.equals("") && searchFatherOrHusband.equals("") && searchNationalId.equals("")) {
                    horizontalScrollview.setVisibility(View.GONE);
                    textViewInsetSearchData.setVisibility(View.VISIBLE);
                } else {
                    textViewInsetSearchData.setVisibility(View.GONE);
                    searchDataList = dataSourceRead.getSearchData(searchGroupName, searchMemberName, searchFatherOrHusband, searchNationalId, programOfficerId);
                    listViewSearchMember.setAdapter(new MemberSearchCustomAdapterForListView(getApplicationContext(), searchDataList));
                    horizontalScrollview.setVisibility(View.VISIBLE);
                }

            }
        });


        textInputEditTextMemberName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchGroupName = textInputEditTextGroupName.getText().toString().trim();
                searchMemberName = textInputEditTextMemberName.getText().toString().trim();
                searchFatherOrHusband = textInputEditTextFatherOrHusband.getText().toString().trim();
                searchNationalId = textInputEditTextNationalIdNumber.getText().toString().trim();

                if (searchGroupName.equals("") && searchMemberName.equals("") && searchFatherOrHusband.equals("") && searchNationalId.equals("")) {
                    horizontalScrollview.setVisibility(View.GONE);
                    textViewInsetSearchData.setVisibility(View.VISIBLE);
                } else {
                    textViewInsetSearchData.setVisibility(View.GONE);
                    searchDataList = dataSourceRead.getSearchData(searchGroupName, searchMemberName, searchFatherOrHusband, searchNationalId, programOfficerId);

                    listViewSearchMember.setAdapter(new MemberSearchCustomAdapterForListView(getApplicationContext(), searchDataList));
                    horizontalScrollview.setVisibility(View.VISIBLE);
                }

            }
        });

        textInputEditTextFatherOrHusband.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchGroupName = textInputEditTextGroupName.getText().toString().trim();
                searchMemberName = textInputEditTextMemberName.getText().toString().trim();
                searchFatherOrHusband = textInputEditTextFatherOrHusband.getText().toString().trim();
                searchNationalId = textInputEditTextNationalIdNumber.getText().toString().trim();

                if (searchGroupName.equals("") && searchMemberName.equals("") && searchFatherOrHusband.equals("") && searchNationalId.equals("")) {
                    horizontalScrollview.setVisibility(View.GONE);
                    textViewInsetSearchData.setVisibility(View.VISIBLE);
                } else {
                    textViewInsetSearchData.setVisibility(View.GONE);
                    searchDataList = dataSourceRead.getSearchData(searchGroupName, searchMemberName, searchFatherOrHusband, searchNationalId, programOfficerId);
                    listViewSearchMember.setAdapter(new MemberSearchCustomAdapterForListView(getApplicationContext(), searchDataList));
                    horizontalScrollview.setVisibility(View.VISIBLE);
                }

            }
        });

        textInputEditTextNationalIdNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                searchGroupName = textInputEditTextGroupName.getText().toString().trim();
                searchMemberName = textInputEditTextMemberName.getText().toString().trim();
                searchFatherOrHusband = textInputEditTextFatherOrHusband.getText().toString().trim();
                searchNationalId = textInputEditTextNationalIdNumber.getText().toString().trim();

                if (searchGroupName.equals("") && searchMemberName.equals("") && searchFatherOrHusband.equals("") && searchNationalId.equals("")) {
                    horizontalScrollview.setVisibility(View.GONE);
                    textViewInsetSearchData.setVisibility(View.VISIBLE);
                } else {
                    textViewInsetSearchData.setVisibility(View.GONE);
                    searchDataList = dataSourceRead.getSearchData(searchGroupName, searchMemberName, searchFatherOrHusband, searchNationalId, programOfficerId);
                    listViewSearchMember.setAdapter(new MemberSearchCustomAdapterForListView(getApplicationContext(), searchDataList));
                    horizontalScrollview.setVisibility(View.VISIBLE);

                }

            }
        });
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
                    MemberSearchActivity.this);

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

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }


}
