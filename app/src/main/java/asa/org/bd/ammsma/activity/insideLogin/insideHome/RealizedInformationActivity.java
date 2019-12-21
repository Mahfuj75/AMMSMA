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
import android.widget.CheckBox;
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
import asa.org.bd.ammsma.database.DateAndDataConversion;
import asa.org.bd.ammsma.extra.GroupNameForSpinnerObject;
import asa.org.bd.ammsma.extra.RealizedGroupData;
import asa.org.bd.ammsma.extra.RealizedMemberData;
import asa.org.bd.ammsma.service.ExtraTask;
import asa.org.bd.ammsma.tableview.TableViewAdapterForRealizedInformationAll;
import asa.org.bd.ammsma.tableview.TableViewAdapterForRealizedInformationSelectedGroups;
import asa.org.bd.ammsma.tableview.TableViewListenerForRealizedInformationAll;
import asa.org.bd.ammsma.tableview.TableViewListenerForRealizedInformationSelectedGroup;
import asa.org.bd.ammsma.tableview.TableViewModelForRealizedInformationAll;
import asa.org.bd.ammsma.tableview.TableViewModelForRealizedInformationSelectedGroups;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;

public class RealizedInformationActivity extends AppCompatActivity {


    private boolean loadSpinnerDataSuccessful = false;
    private boolean loadRealizedMemberDataSuccessful = false;
    private boolean loadRealizedAllGroupsCollectionDataSuccessful = false;
    private boolean firstTime = false;

    private int programOfficerId;
    private int prePositionSpinnerDate = 0;

    private List<Integer> groupIdList;
    private List<Integer> selectedGroupPosition;
    private List<Integer> sevenWorkingDays;
    private List<String> allWorkingDaysFromRealizable;
    private List<String> groupNames;
    private List<Integer> defaultProgramIdList;
    private List<RealizedGroupData> realizedGroupsInformationFinal;
    private List<RealizedMemberData> realizedMemberInformationList;


    private DataSourceRead dataSourceRead;

    private MultiSelectSpinner spinnerGroups;
    private Spinner spinnerDate;

    private CheckBox checkboxAllGroup;

    private TextView textViewTotalLoanCollection;
    private TextView textViewTotalSavingsCollection;
    private TextView textViewTotalCbsCollection;
    private TextView textViewTotalLtsCollection;
    private TextView textViewBadDebtCollection;
    private TextView textViewTotalCollection;
    private TextView textViewSavingsWithdrawal;
    private TextView textViewCbsWithdrawal;
    private TextView textViewExemptionTotal;
    private TextView textViewNetCollectionTotal;

/*    private CustomNonScrollerListView listViewGroupNameRealized;
    private CustomNonScrollerListView listViewOtherRealizedInformation;
    private CustomNonScrollerListView listViewMemberNameRealized;
    private CustomNonScrollerListView listViewMembersOtherInformation;*/

    /*private LinearLayout linearLayoutAllGroup;
    private LinearLayout linearLayoutSelectedGroups;*/

    private int spinnerDateSelection;
    private List<GroupNameForSpinnerObject> groupNameForSpinnerObjects;

    /*
        private boolean selectedGroupMemberName = false;
        private boolean selectedGroupOthers = false;*/
    private ExtraTask extraTask = new ExtraTask();
    private TableView tableView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realized_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        textViewTotalLoanCollection = findViewById(R.id.textViewTotalLoanCollection);
        textViewTotalSavingsCollection = findViewById(R.id.textViewTotalSavingsCollection);
        textViewTotalCbsCollection = findViewById(R.id.textViewTotalCbsCollection);
        textViewTotalLtsCollection = findViewById(R.id.textViewTotalLtsCollection);
        textViewBadDebtCollection = findViewById(R.id.textViewBadDebtCollection);
        textViewTotalCollection = findViewById(R.id.textViewTotalCollection);
        textViewSavingsWithdrawal = findViewById(R.id.textViewSavingsWithdrawal);
        textViewCbsWithdrawal = findViewById(R.id.textViewCbsWithdrawal);
        textViewExemptionTotal = findViewById(R.id.textViewExemptionTotal);
        textViewNetCollectionTotal = findViewById(R.id.textViewNetCollectionTotal);

        checkboxAllGroup = findViewById(R.id.checkboxAllGroup);

        spinnerGroups = findViewById(R.id.spinnerGroup);
        spinnerDate = findViewById(R.id.spinnerDateInRealizable);

        /*listViewGroupNameRealized = findViewById(R.id.listViewGroupNameRealized);
        listViewOtherRealizedInformation = findViewById(R.id.listViewGroupOtherInformation);
        listViewMemberNameRealized = findViewById(R.id.listViewMemberNameRealized);
        listViewMembersOtherInformation = findViewById(R.id.listViewMembersOtherInformation);*/

        /*linearLayoutAllGroup = findViewById(R.id.linearLayoutAllGroup);
        linearLayoutSelectedGroups = findViewById(R.id.linearLayoutSelectedGroups);*/

        tableView = findViewById(R.id.tableView);


        groupIdList = new ArrayList<>();
        groupNames = new ArrayList<>();
        defaultProgramIdList = new ArrayList<>();
        dataSourceRead = new DataSourceRead(getApplicationContext());

        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);

        String loginId = getIntent().getStringExtra("loginId");


        /*@SuppressLint("InflateParams")
        View headerViewGroupNameRealized = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.header_view_for_all_group_only_name, null, false);
        @SuppressLint("InflateParams")
        View headerViewOtherRealizedInformation = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.header_for_all_group_others, null, false);

        @SuppressLint("InflateParams")
        View headerViewMemberNameRealized = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.header_view_for_selected_group_only_member_name, null, false);
        @SuppressLint("InflateParams")
        View headerViewMembersOtherInformation = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.header_view_for_selected_group_other, null, false);

        listViewGroupNameRealized.addHeaderView(headerViewGroupNameRealized);
        listViewOtherRealizedInformation.addHeaderView(headerViewOtherRealizedInformation);
        listViewMemberNameRealized.addHeaderView(headerViewMemberNameRealized);
        listViewMembersOtherInformation.addHeaderView(headerViewMembersOtherInformation);
*/

        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        sevenWorkingDays = new DataSourceOperationsCommon(getApplicationContext()).getSevenWorkingDays();
        prePositionSpinnerDate = 0;


        for (int i = 0; i < sevenWorkingDays.size(); i++) {
            // String abc = new DateAndDataConversion().getDateFromInt(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay());
            if (sevenWorkingDays.get(i) == new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay()) {
                spinnerDateSelection = i;
                break;
            }
        }


        AsyncTaskForSpinner asyncTaskForSpinner = new AsyncTaskForSpinner(sevenWorkingDays.get(spinnerDateSelection));
        asyncTaskForSpinner.execute();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkboxAllGroup.setOnClickListener(v -> {

            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

            if (checkboxAllGroup.isChecked()) {

                setAdapterForGroupAndDate();
                AsyncTaskForDataLoad asyncTaskForDataLoad = new AsyncTaskForDataLoad(sevenWorkingDays.get(spinnerDate.getSelectedItemPosition()));
                asyncTaskForDataLoad.execute();
                spinnerGroups.setEnabled(false);
                /*linearLayoutAllGroup.setVisibility(View.VISIBLE);
                linearLayoutSelectedGroups.setVisibility(View.GONE);*/
            } else {
                spinnerGroups.setEnabled(true);
            }
        });


        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (prePositionSpinnerDate != position) {
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
                    checkboxAllGroup.setChecked(true);
                    setAdapterForGroupAndDate();
                    /*linearLayoutAllGroup.setVisibility(View.VISIBLE);
                    linearLayoutSelectedGroups.setVisibility(View.GONE);*/
                    prePositionSpinnerDate = position;
                    AsyncTaskForDataLoad asyncTaskForDataLoad = new AsyncTaskForDataLoad(sevenWorkingDays.get(position));
                    asyncTaskForDataLoad.execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setAdapterForGroupAndDate() {
        spinnerGroups.setListAdapter(new SpinnerCustomAdapter().arrayListAdapterForRealizedINfoGroup(groupNames, getApplicationContext(), defaultProgramIdList))
                .setListener(selected -> {

                    selectedGroupPosition = new ArrayList<>();
                    int zeroGroup = 0;
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i] && groupNameForSpinnerObjects.get(i).getTotalMember() > 0) {
                            selectedGroupPosition.add(groupIdList.get(i));
                        } else if (selected[i] && groupNameForSpinnerObjects.get(i).getTotalMember() == 0) {
                            zeroGroup++;
                        }

                    }
                    if (selectedGroupPosition.size() > 0 || zeroGroup > 0) {
                        /*linearLayoutAllGroup.setVisibility(View.GONE);
                        linearLayoutSelectedGroups.setVisibility(View.VISIBLE);*/
                        AsyncTaskForMembers asyncTaskForMembers = new AsyncTaskForMembers(sevenWorkingDays.get(spinnerDate.getSelectedItemPosition()));
                        asyncTaskForMembers.execute();

                    } /*else {
                        linearLayoutAllGroup.setVisibility(View.VISIBLE);
                        linearLayoutSelectedGroups.setVisibility(View.GONE);
                    }*/


                })
                .setAllCheckedText("All Selected")
                .setAllUncheckedText("None Selected")
                .setSelectAll(false)
                .setMinSelectedItems(0)
                .setMaxSelectedItems(7)
                .setEnabled(false);


        if (!firstTime) {
            spinnerDate.setAdapter(new SpinnerCustomAdapter().arrayListAdapterForRealizedDate(allWorkingDaysFromRealizable, getApplicationContext()));
            spinnerDate.setSelection(spinnerDateSelection);


            firstTime = true;
        }

    }

    private void setCollectionTotalData() {
        int lastPosition = realizedGroupsInformationFinal.size() - 1;


        textViewTotalLoanCollection.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getLoanCollection())));
        textViewTotalSavingsCollection.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getSavingsDepositWithoutLts())));
        textViewTotalCbsCollection.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getCbsDeposit())));
        textViewTotalLtsCollection.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getLtsDeposit())));
        textViewBadDebtCollection.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getBadDebtCollection())));
        textViewTotalCollection.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getTotalCollection())));
        textViewSavingsWithdrawal.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getSavingsWithdrawal())));
        textViewCbsWithdrawal.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getCbsWithdrawal())));
        textViewExemptionTotal.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getExemptionTotal())));
        textViewNetCollectionTotal.setText(String.valueOf(Math.round(realizedGroupsInformationFinal.get(lastPosition).getNetCollection())));
    }

    private boolean loadSpinnerData() {
        groupIdList.clear();
        groupNames.clear();

        try {
            groupNameForSpinnerObjects = dataSourceRead.getGroupNameBadDebtInLast(programOfficerId);
            allWorkingDaysFromRealizable = dataSourceRead.getSevenWorkingDaysFromRealizable();


            for (int i = 0; i < allWorkingDaysFromRealizable.size(); i++) {
                if (allWorkingDaysFromRealizable.get(i).equals(new DateAndDataConversion().getDateFromInt(new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay()))) {
                    spinnerDateSelection = i;
                    break;
                }
            }

            for (int i = 0; i < groupNameForSpinnerObjects.size(); i++) {


                groupNames.add(groupNameForSpinnerObjects.get(i).getGroupFullName());
                groupIdList.add(groupNameForSpinnerObjects.get(i).getGroupId());
                defaultProgramIdList.add(groupNameForSpinnerObjects.get(i).getDefaultProgramId());


            }


        } catch (Exception e) {
            Log.i("error", e.toString());
        }


        return true;

    }

    private boolean loadRealizedCollectionDataForAllGroup(int date) {


        realizedGroupsInformationFinal = new ArrayList<>();
        realizedGroupsInformationFinal = dataSourceRead.getRealizedGroupDataSummary(date, programOfficerId);
        return true;
    }

    private boolean loadRealizedCollectionDataForSelectedGroups(int date) {
        realizedMemberInformationList = new ArrayList<>();
        realizedMemberInformationList = dataSourceRead.getRealizedGroupMemberDataSummary(selectedGroupPosition, date);
        return true;
    }

    private void setListViewMemberNameRealized(final List<RealizedMemberData> realizedMemberInformationList, final Dialog dialog) {

        int totalPosition = realizedMemberInformationList.size() - 1;
        if (totalPosition < 0) {
            textViewTotalLoanCollection.setText("0");

            textViewTotalSavingsCollection.setText("0");
            textViewTotalCbsCollection.setText("0");
            textViewTotalLtsCollection.setText("0");
            textViewBadDebtCollection.setText("0");
            textViewTotalCollection.setText("0");
            textViewSavingsWithdrawal.setText("0");
            textViewCbsWithdrawal.setText("0");
            textViewExemptionTotal.setText("0");
            textViewNetCollectionTotal.setText("0");
        } else {
            textViewTotalLoanCollection.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getPrimaryCollection()
                    + realizedMemberInformationList.get(totalPosition).getSecondaryCollection()
                    + realizedMemberInformationList.get(totalPosition).getSupplementaryCollection())));

            textViewTotalSavingsCollection.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getSavingsDepositWithoutLTS())));
            textViewTotalCbsCollection.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getCbsDeposit())));
            textViewTotalLtsCollection.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getLtsDeposit())));
            textViewBadDebtCollection.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getBadDebtCollection())));
            textViewTotalCollection.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getTotalCollection())));
            textViewSavingsWithdrawal.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getSavingsWithdrawal())));
            textViewCbsWithdrawal.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getCbsWithdrawal())));
            textViewExemptionTotal.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getExemptionTotal())));
            textViewNetCollectionTotal.setText(String.valueOf(Math.round(realizedMemberInformationList.get(totalPosition).getNetCollection())));
        }


        /*listViewMemberNameRealized.setAdapter(new RealizedCustomAdapterForListViewRealizedOnlyMemberName(getApplicationContext(), realizedMemberInformationList));
        listViewMembersOtherInformation.setAdapter(new RealizedCustomAdapterForListViewRealizedMemberOtherInformation(getApplicationContext(), realizedMemberInformationList, dialog, new RealizedCustomAdapterForListViewRealizedMemberOtherInformation.DialogListener() {
            @Override
            public void lastPosition(boolean last) {
                selectedGroupOthers = last;
                if (selectedGroupMemberName && selectedGroupOthers) {
                    dialog.dismiss();
                    selectedGroupOthers = false;
                    selectedGroupMemberName = false;
                }
            }
        }));

        colorCodeForMemberList = new ArrayList<>();*/


        TableViewModelForRealizedInformationSelectedGroups mTableViewModel;
        AbstractTableAdapter<ColumnHeader, RowHeader, Cell> mTableViewAdapter;

        mTableViewModel = new TableViewModelForRealizedInformationSelectedGroups(realizedMemberInformationList);

        // Create TableView Adapter
        mTableViewAdapter = new TableViewAdapterForRealizedInformationSelectedGroups(getApplicationContext(), realizedMemberInformationList, (positionX, positionY) -> {
            /*if((positionX ==realizedMemberInformationList.size()-1) && (positionY ==realizedMemberInformationList.size()-1))
            {
                dialog.dismiss();
                selectedGroupOthers = false;
                selectedGroupMemberName = false;
            }*/
        });

        tableView.setAdapter(mTableViewAdapter);
        tableView.setIgnoreSelectionColors(true);
        tableView.setSelectedColor(Color.parseColor("#5efc82"));
        tableView.setTableViewListener(new TableViewListenerForRealizedInformationSelectedGroup(getApplicationContext(), tableView, realizedMemberInformationList));
        dialog.dismiss();
       /* selectedGroupOthers = false;
        selectedGroupMemberName = false;*/

        mTableViewAdapter.setAllItems(mTableViewModel.getColumnHeaderList(), mTableViewModel
                .getRowHeaderList(), mTableViewModel.getCellList());


       /* if (realizedMemberInformationList.size() == 0) {
            dialog.dismiss();
            selectedGroupOthers = false;
            selectedGroupMemberName = false;
        }*/


    }

    private void setAllGroupData(Dialog dialog) {

        /*listViewGroupNameRealized.setAdapter(new RealizedCustomAdapterForListViewRealizedAllGroupOnlyGroupName(getApplicationContext(), realizedGroupsInformationFinal));
        listViewOtherRealizedInformation.setAdapter(new RealizedCustomAdapterForListViewRealizedAllGroupOtherInformation(getApplicationContext(), realizedGroupsInformationFinal));
        linearLayoutSelectedGroups.setVisibility(View.GONE);*/

        TableViewModelForRealizedInformationAll mTableViewModel;
        AbstractTableAdapter<ColumnHeader, RowHeader, Cell> mTableViewAdapter;

        mTableViewModel = new TableViewModelForRealizedInformationAll( realizedGroupsInformationFinal);

        // Create TableView Adapter
        mTableViewAdapter = new TableViewAdapterForRealizedInformationAll(getApplicationContext(), realizedGroupsInformationFinal);

        tableView.setAdapter(mTableViewAdapter);
        tableView.setIgnoreSelectionColors(true);
        tableView.setSelectedColor(Color.parseColor("#5efc82"));
        tableView.setTableViewListener(new TableViewListenerForRealizedInformationAll(getApplicationContext(), tableView, realizedGroupsInformationFinal));
        dialog.dismiss();
        /*selectedGroupOthers = false;
        selectedGroupMemberName = false;*/

        mTableViewAdapter.setAllItems(mTableViewModel.getColumnHeaderList(), mTableViewModel
                .getRowHeaderList(), mTableViewModel.getCellList());


        /*if (realizedGroupsInformationFinal.size() == 0) {
            dialog.dismiss();
            selectedGroupOthers = false;
            selectedGroupMemberName = false;
        }*/

    }

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
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    RealizedInformationActivity.this);

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

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForSpinner extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private int spinnerDate;

        AsyncTaskForSpinner(int spinnerDate) {
            this.spinnerDate = spinnerDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(RealizedInformationActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadSpinnerDataSuccessful = loadSpinnerData();
            loadRealizedAllGroupsCollectionDataSuccessful = loadRealizedCollectionDataForAllGroup(spinnerDate);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loadSpinnerDataSuccessful && loadRealizedAllGroupsCollectionDataSuccessful) {

                setAdapterForGroupAndDate();
                setCollectionTotalData();
                setAllGroupData(dialog);
                dialog.dismiss();
                loadSpinnerDataSuccessful = false;
                loadRealizedAllGroupsCollectionDataSuccessful = false;
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForMembers extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private int spinnerDate;

        AsyncTaskForMembers(int spinnerDate) {
            this.spinnerDate = spinnerDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(RealizedInformationActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            loadRealizedMemberDataSuccessful = loadRealizedCollectionDataForSelectedGroups(spinnerDate);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loadRealizedMemberDataSuccessful) {


                setListViewMemberNameRealized(realizedMemberInformationList, dialog);
                loadRealizedMemberDataSuccessful = false;
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForDataLoad extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private int spinnerDate;

        AsyncTaskForDataLoad(int spinnerDate) {
            this.spinnerDate = spinnerDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(RealizedInformationActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            loadRealizedAllGroupsCollectionDataSuccessful = loadRealizedCollectionDataForAllGroup(spinnerDate);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loadRealizedAllGroupsCollectionDataSuccessful) {

                setCollectionTotalData();
                setAllGroupData(dialog);

            }


        }
    }

}



