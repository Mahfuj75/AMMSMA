package asa.org.bd.ammsma.activity.insideLogin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.GroupOperationActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.ImportDataNewActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.MemberBalanceActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.MemberSearchActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.OverdueMemberActivity;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.RealizedInformationActivity;
import asa.org.bd.ammsma.crypto.Cryptography;
import asa.org.bd.ammsma.customAdapter.CustomGridAdapter;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.database.DatabaseExportXML;
import asa.org.bd.ammsma.database.DatabaseHelper;
import asa.org.bd.ammsma.service.ExtraTask;


public class HomeActivity extends AppCompatActivity {

    private GridView gridViewHome;

    private Toolbar toolbar;
    private int programOfficerId;
    private String loginId;
    private DataSourceRead dataSourceRead;

    private int isSuccessful = 0;

    private int groupListSize = 0;
    private DatabaseHelper databaseHelper;

    private int[] imageId = {
            R.drawable.group_operation,
            R.drawable.member_balance,
            R.drawable.realized_information,
            R.drawable.search_icon,
            R.drawable.import_data,
            R.drawable.overdue_img,
            R.drawable.export_data,
    };


    private Menu menu;

    private String encryptedResult;

    private static final int SLEEP_TIME = (1000 / 100);
    private ExtraTask extraTask = new ExtraTask();


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);
        TextView programOfficerName = findViewById(R.id.textView_programOfficerName);
        dataSourceRead = new DataSourceRead(getApplicationContext());

        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        loginId = getIntent().getStringExtra("loginId");
        databaseHelper = new DatabaseHelper(getApplicationContext());


        if (programOfficerId != -1) {
            programOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
        }
        if (programOfficerId == -1) {
            programOfficerName.setText(R.string.user_admin);
        }


        String[] gridItemList = getResources().getStringArray(R.array.home_grid_name);
        gridViewHome = findViewById(R.id.gridViewHome);
        gridViewHome.setAdapter(new CustomGridAdapter(HomeActivity.this, gridItemList, imageId));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        gridItemClickAction();
        toolbarNavigationClick();

        /*Intent i = new Intent(getApplicationContext(),LogoutTimerService.class);
        startService(i);*/

        AsyncTaskForSpinner asyncTaskForSpinner = new AsyncTaskForSpinner();
        asyncTaskForSpinner.execute();


        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private int loadSpinnerData() {


        try {
            groupListSize = dataSourceRead.getGroupNameBadDebtInLastSize(programOfficerId);


        } catch (Exception e) {
            Log.i("error", e.toString());
        }


        return 1;

    }


    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    HomeActivity.this);

            builder.setMessage("Are you sure you want to Logout from Application?")
                    .setCancelable(false)
                    .setTitle("Logout")
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
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    private void gridItemClickAction() {

        gridViewHome.setOnItemClickListener((parent, view, position, id) -> {

            //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

            if (position == 0) {


                /*LogoutTimerService.timer.cancel();*/
                if (groupListSize == 0 && programOfficerId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id1) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (programOfficerId != -1) {
                   /* LogoutTimerService.timer.cancel();
                    stopService(new Intent(getApplicationContext(),LogoutTimerService.class));*/
                    Intent i = new Intent(getApplicationContext(), GroupOperationActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("loginId", loginId);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("Please login with your  \"Loan Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id12) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            } else if (position == 1) {
                if (groupListSize == 0 && programOfficerId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id13) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (programOfficerId != -1) {

                    Intent i = new Intent(getApplicationContext(), MemberBalanceActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                    i.putExtra("loginId", loginId);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("Please login with your  \"Loan Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id14) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else if (position == 2) {

                if (groupListSize == 0 && programOfficerId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id15) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (programOfficerId != -1) {
                    Intent i = new Intent(getApplicationContext(), RealizedInformationActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("loginId", loginId);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("Please login with your  \"Loan Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id16) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else if (position == 3) {

                if (groupListSize == 0 && programOfficerId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id17) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (programOfficerId != -1) {
                    Intent i = new Intent(getApplicationContext(), MemberSearchActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                    i.putExtra("loginId", loginId);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("Please login with your  \"Loan Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id18) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else if (position == 4) {
                if (programOfficerId == -1) {
                    Intent i = new Intent(getApplicationContext(), ImportDataNewActivity.class);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("You do not have privilege to import TAB data. Please Login as Admin user to import TAB data. ")
                            .setPositiveButton("Ok",
                                    (dialog, id19) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
            else if (position == 5) {
                if (groupListSize == 0 && programOfficerId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id110) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (programOfficerId != -1) {

                    Intent i = new Intent(getApplicationContext(), OverdueMemberActivity.class);
                    i.putExtra("ProgramOfficerId", programOfficerId);
                    i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                    i.putExtra("loginId", loginId);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("Please login with your  \"Loan Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id111) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            else if (position == 6) {
                if (groupListSize == 0 && programOfficerId != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("NO DATABASE FOUND")
                            .setPositiveButton("Ok",
                                    (dialog, id112) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (programOfficerId != -1) {

                    Environment.getExternalStorageDirectory();

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                            HomeActivity.this);
                    builder1.setTitle("Export Data");
                    builder1.setMessage("Do you want to Export Data from Tab?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Yes", (dialog, id113) -> {

                        AsyncTaskForXml asyncTaskForXml = new AsyncTaskForXml();
                        asyncTaskForXml.execute();

                    });
                    builder1.setNegativeButton("No",
                            (dialog, which) -> dialog.cancel());
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage("Please login with your  \"Loan Officer\" account")
                            .setPositiveButton("Ok",
                                    (dialog, id114) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForXml extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private boolean tag;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(HomeActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String dates = dataSourceRead.getFirstAndLastWorkingDate();
            String my_new_str = dates.replaceAll("/", "-");
            int branchCode = dataSourceRead.getBranchCode();

            String fileString = "/Tab Data/AMMSMA_NEW" + "_(" + my_new_str + ")_" + branchCode + "_" + loginId + "_version(" + getResources().getString(R.string.version_control) + ")" + ".001";
            String root = Environment.getExternalStorageDirectory().toString();
            File file = new File(root + fileString);
            file.delete();
            if (file.exists()) {
                try {
                    file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    getApplicationContext().deleteFile(file.getName());
                }
            }

            for (int i = 1; i < 5; i++) {
                if (isCancelled()) {
                    break;
                } else {
                    publishProgress(i);
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            DatabaseExportXML databaseExportXML = new DatabaseExportXML(databaseHelper.getReadableDatabase(), programOfficerId, getApplicationContext());
            String xmlText = databaseExportXML.exportData();

            tag = !xmlText.equals("");
            encryptedResult = "";

            try {
                encryptedResult = Cryptography.Encrypt(xmlText);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (tag) {
                AsyncTaskForWriteXml asyncTaskForWriteXml = new AsyncTaskForWriteXml();
                asyncTaskForWriteXml.execute();

            } else {

                String text = "Data is not exported in Device Storage. There is some problems while exporting.";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(HomeActivity.this, biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForWriteXml extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private boolean tag;


        AsyncTaskForWriteXml() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(HomeActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            tag = writeEncryptedFile(encryptedResult);


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (tag) {

                String text = "Data is exported in Device Storage  in Tab Data folder.";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(HomeActivity.this, biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {

                String text = "Data is not exported in Device Storage. There is some problems while exporting.";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(HomeActivity.this, biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForSpinner extends AsyncTask<Void, Integer, Void> {
        Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(HomeActivity.this);
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
                dialog.dismiss();
            }


        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_amms, menu);
        this.menu = menu;

        try {
            String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = format1.parse(currentDate);

            SimpleDateFormat format2 = new SimpleDateFormat("EE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            this.menu.findItem(R.id.action_working_day).setTitle(currentDate + " " + day);

            if (programOfficerId == -1) {
                this.menu.findItem(R.id.action_close_current_day).setVisible(false);
            } else {
                this.menu.findItem(R.id.action_close_current_day).setVisible(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        String branchName = dataSourceRead.getBranchName();
        this.menu.findItem(R.id.action_location).setTitle(branchName);
        this.menu.findItem(R.id.action_otp).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
            return true;
        }
        else if(id== R.id.action_otp)
        {

            if(programOfficerId != -1)
            {
                String employeeId =String.valueOf(dataSourceRead.getAllProgramOfficerForImport().get(0).getCode());

                SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(this.getApplicationContext().getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);
                String otpTime = sharedPreferences.getString("OtpTime_"+employeeId , "").trim();


                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dfDateWithTime = new SimpleDateFormat(getResources().getString(R.string.day_pattern_online),Locale.getDefault());
                dfDateWithTime.setTimeZone(TimeZone.getTimeZone(getResources().getString(R.string.time_zone)));
                String formattedDateWithTime = dfDateWithTime.format(currentTime);


                if(!otpTime.equals("") && timeDifference(otpTime,formattedDateWithTime)<=5)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage(sharedPreferences.getString("Password_"+employeeId,""))
                            .setCancelable(false)
                            .setTitle("OTP")
                            .setPositiveButton("Ok",
                                    (dialog, id1) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {
                    SimpleDateFormat dfDateTime = new SimpleDateFormat(getResources().getString(R.string.time_pattern_online),Locale.getDefault());
                    dfDateTime.setTimeZone(TimeZone.getTimeZone(getResources().getString(R.string.time_zone)));
                    String time = dfDateTime.format(currentTime);

                    byte[] encryptedData = Cryptography.encrypt128WithIv(employeeId.concat(formattedDateWithTime).getBytes(StandardCharsets.UTF_8)
                            , getResources().getString(R.string.encryption_decryption_key_online).getBytes(StandardCharsets.UTF_8)
                            , getResources().getString(R.string.encryption_decryption_iv_online).getBytes(StandardCharsets.UTF_8));
                    byte[] base64HumanReadable= android.util.Base64.encode(encryptedData, android.util.Base64.DEFAULT);

                    String encrypted = new String(base64HumanReadable,StandardCharsets.UTF_8).replaceAll(getString(R.string.special), "");
                    if(encrypted.length()>8)
                    {
                        encrypted = encrypted.substring(encrypted.length()-8).concat(time);
                    }
                    else{
                        encrypted = encrypted.concat(time);
                    }
                    sharePreferenceOtp(encrypted,formattedDateWithTime,employeeId);
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            HomeActivity.this);

                    builder.setMessage(encrypted)
                            .setCancelable(false)
                            .setTitle("OTP")
                            .setPositiveButton("Ok",
                                    (dialog, id12) -> dialog.dismiss());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        HomeActivity.this);

                builder.setMessage("You are not allowed to perform this operation")
                        .setCancelable(false)
                        .setTitle("OTP")
                        .setPositiveButton("Ok",
                                (dialog, id14) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            }



        }
        else if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    HomeActivity.this);

            builder.setMessage(
                    "Are you sure you want to Logout from Application?")
                    .setCancelable(false)
                    .setTitle("Logout")
                    .setPositiveButton("Yes",
                            (dialog, id13) -> {
                                Intent i = new Intent(getApplicationContext(), LoginAmmsActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
                                startActivity(i);
                                finish();
                            })
                    .setNegativeButton("No",
                            (dialog, id15) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        } else if (id == R.id.action_close_current_day) {
            if (programOfficerId != -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("If you have any task pending for today, you can not close today's working day. Do you Have any Task Pending Today?")
                        .setCancelable(false)
                        .setNegativeButton("No", (dialog, id16) -> {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(HomeActivity.this);
                            builder2.setMessage("Are you sure you want to Close Current Day?")
                                    .setCancelable(false)
                                    .setNegativeButton("No", (dialog1, which) -> dialog1.cancel())
                                    .setPositiveButton("Yes", (dialog12, which) -> {
                                        int flag = new DataSourceWrite(getApplicationContext()).closeWorkingDay();
                                        if (flag == 0) {
                                            dialog12.dismiss();
                                            Toast.makeText(getApplicationContext(), "\"No Working Day is available.\"", Toast.LENGTH_LONG).show();
                                        } else {
                                            dialog12.dismiss();
                                            try {
                                                setForNewWorkingDay();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    });
                            AlertDialog alert2 = builder2.create();
                            alert2.show();
                        })
                        .setPositiveButton("Yes",
                                (dialog, id17) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        HomeActivity.this);

                builder.setMessage("Please login with your  \"Loan Officer\" account")
                        .setPositiveButton("Ok",
                                (dialog, id18) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setForNewWorkingDay() throws ParseException {
        String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();

        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date dt1 = format1.parse(currentDate);

        SimpleDateFormat format2 = new SimpleDateFormat("EE", Locale.getDefault());
        format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String day = format2.format(dt1);

        this.menu.findItem(R.id.action_working_day).setTitle(currentDate + " " + day);


        AsyncTaskForSpinner asyncTaskForSpinner = new AsyncTaskForSpinner();
        asyncTaskForSpinner.execute();
        Toast.makeText(getApplicationContext(), "\"Current Working Day has been changed to next Working Day.\"", Toast.LENGTH_LONG).show();
    }


    private void sharePreferenceExport(String fileName, boolean flag) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);

        int counter = sharedPreferences.getInt(getResources().getString(R.string.ExportCounter), 0);
        if (counter == 0) {
            counter = 1;
        } else if (counter == 7) {
            counter = 1;
        } else {
            counter += 1;
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("GMT+6"));

        String currentDateTime = df.format(c.getTime());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.ExportDate) + "_" + counter, currentDateTime);
        editor.putString(getResources().getString(R.string.ExportFileName) + "_" + counter, fileName);
        editor.putInt(getResources().getString(R.string.ExportCounter), counter);

        if (flag) {
            editor.putString(getResources().getString(R.string.ExportSuccessTag) + "_" + counter, "Successful");
        } else {
            editor.putString(getResources().getString(R.string.ExportSuccessTag) + "_" + counter, "Not_Successful");
        }

        editor.apply();
        editor.commit();

    }

    private void sharePreferenceOtp(String otp,String Time,String employeeId) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(employeeId, true);
        editor.putString("OtpTime_"+employeeId, Time);
        editor.putString("Password_"+employeeId,otp);
        editor.apply();
        editor.commit();

    }

    int timeDifference(String time1, String time2)
    {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.day_pattern_online));
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        assert date2 != null;
        long difference = date2.getTime() - date1.getTime();
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        return (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
    }



    public static void exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + "asa.org.bd.ammsma" + "//databases//" + "AMMSMADBSYSTEMBD.db";
                String backupDBPath = "AMMSMA.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception ignored) {

        }

    }


    private boolean writeEncryptedFile(String input) {
        int branchCode = dataSourceRead.getBranchCode();
        String dates = dataSourceRead.getFirstAndLastWorkingDate();
        String folder_main = "Tab Data";
        File file = new File(Environment.getExternalStorageDirectory(),
                folder_main);

        if (file.exists()) {
            file.delete();
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.toString()}, null, null);
        String my_new_str = dates.replaceAll("/", "-");
        String fileString = file.getAbsolutePath() + "/AMMSMA_NEW" + "_(" + my_new_str + ")_" + branchCode + "_" + loginId + "_version(" + getResources().getString(R.string.version_control) + ")" + ".001";

        File fileOutput = new File(fileString);
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{fileOutput.toString()}, null, null);
        boolean flag = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput))) {
            writer.write(input);
            flag = true;

        } catch (IOException e) {
            Log.i("Write: ", e.toString());
        }
        sharePreferenceExport(fileString.split("/")[fileString.split("/").length - 1].replace(".001", ""), flag);
        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }
}
