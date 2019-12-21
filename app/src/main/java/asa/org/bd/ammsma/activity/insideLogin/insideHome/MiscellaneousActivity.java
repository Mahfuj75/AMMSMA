package asa.org.bd.ammsma.activity.insideLogin.insideHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.PrimarySecondaryLoanActivity;
import asa.org.bd.ammsma.customAdapter.CustomGridForMiscellaneousAdapter;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.service.ExtraTask;


public class MiscellaneousActivity extends AppCompatActivity {


    private GridView gridViewMiscellaneous;

    private int[] imageId = {
            R.drawable.loan_disburse,
            R.drawable.long_term_savings,
            R.drawable.search_icon,
            R.drawable.member_balance,
    };
    private Toolbar toolbar;

    private DataSourceRead dataSourceRead;

    private int programOfficerId;
    private int groupId;
    private String loginId;
    private String groupName;
    private String programOfficerName;
    private ExtraTask extraTask = new ExtraTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        String[] gridItemList = getResources().getStringArray(R.array.miscellaneous_grid_name);

        gridViewMiscellaneous = findViewById(R.id.gridViewMiscellaneous);
        dataSourceRead = new DataSourceRead(getApplicationContext());
        gridViewMiscellaneous.setAdapter(new CustomGridForMiscellaneousAdapter(MiscellaneousActivity.this, gridItemList, imageId));


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        loginId = getIntent().getStringExtra("loginId");
        groupName = getIntent().getStringExtra("groupName");
        groupId = getIntent().getIntExtra("groupId", 0);


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

        gridViewClickAction();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());


    }

    private void gridViewClickAction() {

        gridViewMiscellaneous.setOnItemClickListener((parent, view, position, id) -> {



            if (position == 0) {
                Intent i = new Intent(getApplicationContext(), PrimarySecondaryLoanActivity.class);
                i.putExtra("ProgramOfficerId", programOfficerId);
                i.putExtra("programOfficerName", programOfficerName);
                i.putExtra("loginId", loginId);
                i.putExtra("groupName", groupName);
                i.putExtra("groupId", groupId);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                startActivity(i);
            } else if (position == 3) {
                Intent i = new Intent(getApplicationContext(), MemberBalanceActivity.class);
                i.putExtra("ProgramOfficerId", programOfficerId);
                i.putExtra("programOfficerName", programOfficerName);
                i.putExtra("loginId", loginId);
                i.putExtra("groupName", groupName);
                i.putExtra("groupId", groupId);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                startActivity(i);
            } else if (position == 2) {
                Intent i = new Intent(getApplicationContext(), MemberSearchActivity.class);
                i.putExtra("ProgramOfficerId", programOfficerId);
                i.putExtra("programOfficerName", programOfficerName);
                i.putExtra("loginId", loginId);
                i.putExtra("groupName", groupName);
                i.putExtra("groupId", groupId);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                startActivity(i);
            }
        });
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
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MiscellaneousActivity.this);

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
