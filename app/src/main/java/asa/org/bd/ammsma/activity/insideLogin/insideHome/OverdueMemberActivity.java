package asa.org.bd.ammsma.activity.insideLogin.insideHome;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import asa.org.bd.ammsma.extra.OverDueMember;
import asa.org.bd.ammsma.service.ExtraTask;
import asa.org.bd.ammsma.tableview.TableViewAdapterForOverDueMember;
import asa.org.bd.ammsma.tableview.TableViewListenerForOverDueMember;
import asa.org.bd.ammsma.tableview.TableViewModelForOverDueMmber;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;

public class OverdueMemberActivity extends AppCompatActivity {


    private boolean groupLoadSuccessful = false;

    private int programOfficerId;

    private List<Integer> groupIdList = new ArrayList<>();
    private List<String> groupNames = new ArrayList<>();

    private Toolbar toolbar;

    private DataSourceRead dataSourceRead;

    private TextView textViewNoGroupSelected;
    private TextView textView_groupName;
    private Spinner spinnerGroup;
    private ExtraTask extraTask = new ExtraTask();

    private TableView tableView;
    private List<OverDueMember> overDueMemberArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue_member);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        textView_groupName = findViewById(R.id.textView_groupName);
        //CustomNonScrollerListView listViewGroupMembers = findViewById(R.id.listViewGroupMembersName);
        //CustomNonScrollerListView listViewMemberOtherInformation = findViewById(R.id.listViewMemberOtherInformation);
        spinnerGroup = findViewById(R.id.spinnerGroup);
        textViewNoGroupSelected = findViewById(R.id.textViewNoGroupSelected);

        dataSourceRead = new DataSourceRead(getApplicationContext());
        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        String programOfficerName = getIntent().getStringExtra("programOfficerName");
        String groupName = getIntent().getStringExtra("groupName");
        int groupId = getIntent().getIntExtra("groupId", 0);
        tableView = findViewById(R.id.tableView);


        /*View headerViewMemberName = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.header_view_list_view_member_balance_member_name, null, false);

        View headerViewMemberOthers = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.list_title_member_balance_others, null, false);

        listViewGroupMembers.addHeaderView(headerViewMemberName);
        listViewMemberOtherInformation.addHeaderView(headerViewMemberOthers);*/


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

        }
        else {
            AsyncTaskForGroupListLoad asyncTaskForGroupListLoad = new AsyncTaskForGroupListLoad();
            asyncTaskForGroupListLoad.execute();
        }

        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForGroupListLoad extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(OverdueMemberActivity.this);
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

    private boolean loadSpinnerData() {

        groupIdList.clear();
        groupNames.clear();

        try {
            List<GroupNameForSpinnerObject> groupNameForSpinnerObjects = dataSourceRead.getAllGroupNameWithoutBadDebtForOverDueMember(programOfficerId);
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

    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
        });
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
                    textViewNoGroupSelected.setText(R.string.no_group_selected);
                    textViewNoGroupSelected.setVisibility(View.VISIBLE);

                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

            dialog = new Dialog(OverdueMemberActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            overDueMemberArrayList =  dataSourceRead.getAllMembersForOverDue(groupId);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (overDueMemberArrayList!= null && overDueMemberArrayList.size()>0) {


                tableView.setVisibility(View.VISIBLE);
                textViewNoGroupSelected.setVisibility(View.GONE);
                textViewNoGroupSelected.setText(R.string.no_group_selected);

                TableViewModelForOverDueMmber tableViewModelForOverDueMmber;
                AbstractTableAdapter<ColumnHeader, RowHeader, Cell> mTableViewAdapter;
                tableViewModelForOverDueMmber = new TableViewModelForOverDueMmber(overDueMemberArrayList);
                mTableViewAdapter = new TableViewAdapterForOverDueMember(getApplicationContext(),overDueMemberArrayList);

                tableView.setAdapter(mTableViewAdapter);
                tableView.setIgnoreSelectionColors(true);
                tableView.setSelectedColor(Color.parseColor("#5efc82"));
                tableView.setTableViewListener(new TableViewListenerForOverDueMember(tableView,overDueMemberArrayList));


                mTableViewAdapter.setAllItems(tableViewModelForOverDueMmber.getColumnHeaderList(), tableViewModelForOverDueMmber
                        .getRowHeaderList(), tableViewModelForOverDueMmber.getCellList());

                dialog.dismiss();


            }
            else
            {
                textViewNoGroupSelected.setVisibility(View.VISIBLE);
                textViewNoGroupSelected.setText(R.string.no_overdue_member_found);
                tableView.setVisibility(View.GONE);
                dialog.dismiss();
            }
            dialog.dismiss();


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
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    OverdueMemberActivity.this);

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
            AlertDialog alert = builder.create();
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
