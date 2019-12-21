package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.annotation.SuppressLint;
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
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.insideGroupCollection.BadDebtActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.insideGroupCollection.DailyCollectionDynamicActivity;
import asa.org.bd.ammsma.customAdapter.ListViewCustomAdapter;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.extra.MemberListInfo;
import asa.org.bd.ammsma.service.ExtraTask;

public class GroupCollectionActivity extends AppCompatActivity {


    private DataSourceRead dataSourceRead;

    private ListView listViewGroupMembers;

    private String groupName;
    private String loginId;

    private int groupId;
    private int programOfficerId;
    private int defaultProgramId;

    private int spinnerPosition;
    private MemberListInfo memberListInfo = new MemberListInfo();


    private String realGroupName;

    private boolean signal = false;

    private List<Integer> savedData = new ArrayList<>();
    private boolean firstTimeBackButtonWork = false;
    private ExtraTask extraTask = new ExtraTask();


    @SuppressLint("SetTextI18n")
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_collection);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName = findViewById(R.id.textView_groupName);

        listViewGroupMembers = findViewById(R.id.listViewGroupMembersName);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);
        dataSourceRead = new DataSourceRead(getApplicationContext());


        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logowith_space);


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        loginId = getIntent().getStringExtra("loginId");
        groupName = getIntent().getStringExtra("groupName");
        groupId = getIntent().getIntExtra("groupId", 0);
        realGroupName = getIntent().getStringExtra("realGroupName");
        defaultProgramId = getIntent().getIntExtra("defaultProgramId", 0);
        spinnerPosition = getIntent().getIntExtra("spinnerPosition", 0);


        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
        }
        if (programOfficerId == -1) {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        textViewGroupName.setText(groupName);

        AsyncTaskForListView objMyTask = new AsyncTaskForListView();
        objMyTask.execute();

        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());


    }

    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();



        if (signal) {
            AsyncTaskForSavedData asyncTaskForSavedData = new AsyncTaskForSavedData();
            asyncTaskForSavedData.execute();

        }
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

            if (firstTimeBackButtonWork) {
                Intent intent = new Intent();
                intent.putExtra("GroupRefresh", spinnerPosition);
                setResult(RESULT_OK, intent);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                finish();
            }
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    GroupCollectionActivity.this);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 129) {
            if (resultCode == RESULT_OK) {
                savedData = data.getIntegerArrayListExtra("SavedDataValue");
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForListView extends AsyncTask<Void, Integer, Void> {
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            firstTimeBackButtonWork = false;

            try {
                dialog = new Dialog(GroupCollectionActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.progress_dialog);
                dialog.show();
            } catch (Exception e) {
                Log.i("Problem", e.toString());
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            memberListInfo = dataSourceRead.getAllMembersForCollectionListNew(groupId);
            firstTimeBackButtonWork = true;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            dialog.dismiss();
            try {
                listViewGroupMembers.setAdapter(new ListViewCustomAdapter(GroupCollectionActivity.this,
                        memberListInfo));
                listViewGroupMembers.setVisibility(View.VISIBLE);


                listViewGroupMembers.setOnItemClickListener((parent, view, position, id) -> {

                    //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

                    int memberId = memberListInfo.getMembersInfo().get(position).getId();
                    if (defaultProgramId != 999) {


                        Intent i = new Intent(getApplicationContext(), DailyCollectionDynamicActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", groupName);
                        i.putExtra("realGroupName", realGroupName);
                        i.putExtra("groupId", groupId);
                        i.putExtra("memberId", memberId);
                        i.putExtra("position", position);
                        i.putExtra("defaultProgramId", defaultProgramId);
                        signal = true;
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivityForResult(i, 129);


                    } else {
                        Intent i = new Intent(getApplicationContext(), BadDebtActivity.class);
                        i.putExtra("ProgramOfficerId", programOfficerId);
                        i.putExtra("loginId", loginId);
                        i.putExtra("groupName", groupName);
                        i.putExtra("realGroupName", realGroupName);
                        i.putExtra("groupId", groupId);
                        i.putExtra("memberId", memberId);
                        i.putExtra("position", position);
                        i.putExtra("defaultProgramId", defaultProgramId);
                        signal = true;
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                        startActivityForResult(i, 129);
                    }


                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForSavedData extends AsyncTask<Void, Integer, Void> {
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                dialog = new Dialog(GroupCollectionActivity.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.progress_dialog);
                dialog.show();
            } catch (Exception e) {
                Log.i("Problem", e.toString());
            }

        }

        @Override
        protected Void doInBackground(Void... params) {


            for (int i = 0; i < savedData.size(); i++) {
                boolean updatePaidOrNot = dataSourceRead.getAlreadyPaidMemberData(memberListInfo.getMembersInfo().get(savedData.get(i)).getId());
                if (updatePaidOrNot != memberListInfo.getMembersPaidOrNot().get(savedData.get(i))) {
                    memberListInfo.updatePaidOrNotData(savedData.get(i), updatePaidOrNot);
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listViewGroupMembers.invalidateViews();
            listViewGroupMembers.invalidate();
            dialog.dismiss();

        }
    }


}
