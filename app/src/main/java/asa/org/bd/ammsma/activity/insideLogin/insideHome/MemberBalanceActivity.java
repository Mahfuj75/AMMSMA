package asa.org.bd.ammsma.activity.insideLogin.insideHome;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.customAdapter.SpinnerCustomAdapter;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.extra.GroupNameForSpinnerObject;
import asa.org.bd.ammsma.extra.MiscellaneousMemberBalance;
import asa.org.bd.ammsma.service.ExtraTask;
import asa.org.bd.ammsma.tableview.TableViewAdapterForMemberBalance;
import asa.org.bd.ammsma.tableview.TableViewListenerForMemberBalance;
import asa.org.bd.ammsma.tableview.TableViewModelForMemberBalance;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;

public class MemberBalanceActivity extends AppCompatActivity {

    private boolean groupLoadSuccessful = false;
    private boolean dataLoadSuccessful = false;
    private boolean firstTimeCreated = false;

    private int programOfficerId;

    private List<Integer> groupIdList = new ArrayList<>();
    private List<String> groupNames = new ArrayList<>();
    private List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList = new ArrayList<>();

    private Toolbar toolbar;

    private DataSourceRead dataSourceRead;

    private TextView textViewNoGroupSelected;
    private TextView textView_groupName;
    private Spinner spinnerGroup;
    private ExtraTask extraTask = new ExtraTask();

    private TableView tableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_balance);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);


        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        textView_groupName = findViewById(R.id.textView_groupName);
        spinnerGroup = findViewById(R.id.spinnerGroup);
        textViewNoGroupSelected = findViewById(R.id.textViewNoGroupSelected);

        dataSourceRead = new DataSourceRead(getApplicationContext());
        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        String programOfficerName = getIntent().getStringExtra("programOfficerName");
        String groupName = getIntent().getStringExtra("groupName");
        int groupId = getIntent().getIntExtra("groupId", 0);
        tableView = findViewById(R.id.tableView);



        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(programOfficerName);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        toolbarNavigationClick();


        if (groupId != 0) {

            spinnerGroup.setVisibility(View.GONE);
            textViewNoGroupSelected.setVisibility(View.GONE);
            textView_groupName.setText(groupName);
            AsyncTaskForDataLoad asyncTaskForDataLoad = new AsyncTaskForDataLoad(groupId);
            asyncTaskForDataLoad.execute();

        } else {
            AsyncTaskForGroupListLoad asyncTaskForGroupListLoad = new AsyncTaskForGroupListLoad();
            asyncTaskForGroupListLoad.execute();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }

    @SuppressLint("SetTextI18n")
    private void loadSpinner() {
        textView_groupName.setText("No Group");
        spinnerGroup.setAdapter(new SpinnerCustomAdapter().arrayListAdapterForSpinnerMemberBalance(groupNames, getApplicationContext()));
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0) {

                    textView_groupName.setText(spinnerGroup.getSelectedItem().toString().trim());
                    textViewNoGroupSelected.setVisibility(View.GONE);
                    tableView.setVisibility(View.GONE);
                    AsyncTaskForDataLoad asyncTaskForDataLoad = new AsyncTaskForDataLoad(groupIdList.get(position));
                    asyncTaskForDataLoad.execute();

                } else {
                    textView_groupName.setText("No Group");
                    tableView.setVisibility(View.GONE);
                    textViewNoGroupSelected.setText("No Group Selected");
                    textViewNoGroupSelected.setVisibility(View.VISIBLE);
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean databaseForMemberInformation(int groupId) {
        miscellaneousMemberBalanceList = dataSourceRead.getMemberBalanceInformation(groupId);
        return true;
    }

    private boolean loadSpinnerData() {

        groupIdList.clear();
        groupNames.clear();

        try {
            List<GroupNameForSpinnerObject> groupNameForSpinnerObjects = dataSourceRead.getAllGroupNameWithoutBadDebt(programOfficerId);
            groupNames.add(0, "Select Group");
            groupIdList.add(0, -55);
            for (int i = 0; i < groupNameForSpinnerObjects.size(); i++) {

                groupNames.add(i + 1, groupNameForSpinnerObjects.get(i).getGroupFullName());
                groupIdList.add(i + 1, groupNameForSpinnerObjects.get(i).getGroupId());


            }


        } catch (Exception e) {
            Log.i("error", e.toString());
        }

        return true;

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

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
                    MemberBalanceActivity.this);

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

            dialog = new Dialog(MemberBalanceActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);

            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            groupLoadSuccessful = loadSpinnerData();
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


                loadSpinner();
                groupLoadSuccessful = false;
                dialog.dismiss();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForDataLoad extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private int groupId;

        AsyncTaskForDataLoad(int groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(MemberBalanceActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            dataLoadSuccessful = databaseForMemberInformation(groupId);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dataLoadSuccessful) {


                /*if (!firstTimeCreated) {


                    listViewGroupMembers.setAdapter(new MiscellaneousMemberBalanceCustomAdapterOnlyMemberName(getApplicationContext(), miscellaneousMemberBalanceList));

                    listViewMemberOtherInformation.setAdapter(new MiscellaneousMemberBalanceCustomAdapterOtherInfo(getApplicationContext(), miscellaneousMemberBalanceList));

                    linearLayoutGroupInformation.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                    firstTimeCreated = true;

                } else {


                    listViewGroupMembers.setAdapter(new MiscellaneousMemberBalanceCustomAdapterOnlyMemberName(getApplicationContext(), miscellaneousMemberBalanceList));
                    listViewMemberOtherInformation.setAdapter(new MiscellaneousMemberBalanceCustomAdapterOtherInfo(getApplicationContext(), miscellaneousMemberBalanceList));

                    linearLayoutGroupInformation.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }*/

                TableViewModelForMemberBalance mTableViewModelForMemberBalance;
                AbstractTableAdapter<ColumnHeader, RowHeader, Cell> mTableViewAdapter;
                if(!firstTimeCreated)
                {

                    tableView.setVisibility(View.VISIBLE);
                    mTableViewModelForMemberBalance = new TableViewModelForMemberBalance(miscellaneousMemberBalanceList);

                    // Create TableView Adapter
                    mTableViewAdapter = new TableViewAdapterForMemberBalance(getApplicationContext(),miscellaneousMemberBalanceList.size());

                    tableView.setAdapter(mTableViewAdapter);
                    tableView.setIgnoreSelectionColors(true);
                    tableView.setSelectedColor(Color.parseColor("#5efc82"));
                    tableView.setTableViewListener(new TableViewListenerForMemberBalance(tableView,miscellaneousMemberBalanceList));

                    // Create an instance of a Filter and pass the TableView.
                    //mTableFilter = new Filter(mTableView);

                    // Load the dummy data to the TableView

                    mTableViewAdapter.setAllItems(mTableViewModelForMemberBalance.getColumnHeaderList(), mTableViewModelForMemberBalance
                            .getRowHeaderList(), mTableViewModelForMemberBalance.getCellList());

                    dialog.dismiss();
                    firstTimeCreated=true;

                }
                else
                {

                    tableView.setVisibility(View.VISIBLE);
                    mTableViewModelForMemberBalance = new TableViewModelForMemberBalance(miscellaneousMemberBalanceList);

                    // Create TableView Adapter
                    mTableViewAdapter = new TableViewAdapterForMemberBalance(getApplicationContext(),miscellaneousMemberBalanceList.size());

                    tableView.setAdapter(mTableViewAdapter);
                    tableView.setTableViewListener(new TableViewListenerForMemberBalance(tableView,miscellaneousMemberBalanceList));

                    // Create an instance of a Filter and pass the TableView.
                    //mTableFilter = new Filter(mTableView);

                    // Load the dummy data to the TableView
                    mTableViewAdapter.setAllItems(mTableViewModelForMemberBalance.getColumnHeaderList(), mTableViewModelForMemberBalance
                            .getRowHeaderList(), mTableViewModelForMemberBalance.getCellList());

                    dialog.dismiss();
                }


            }


        }
    }

}
