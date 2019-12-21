package asa.org.bd.ammsma.activity.insideLogin.insideHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.lingala.zip4j.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.crypto.Cryptography;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.database.DatabaseExportXML;
import asa.org.bd.ammsma.database.DatabaseHelper;
import asa.org.bd.ammsma.file.FilePicker;
import asa.org.bd.ammsma.jsonJavaViceVersa.ProgramOfficer;
import asa.org.bd.ammsma.jsonJavaViceVersa.main.JsonJavaViceVersaImport;
import asa.org.bd.ammsma.service.ExtraTask;
//import ir.mahdi.mzip.zip.ZipArchive;

public class ImportDataNewActivity extends AppCompatActivity implements ComponentCallbacks2 {


    private static final int REQUEST_PICK_FILE = 1;
    private static final int FILE_STORAGE_PERMISSION = 673;
    private int successFlag;
    private DataSourceWrite dataSourceWrite;
    private File selectedFile;
    private TextView textViewSelectedFile;
    private TextView textViewImportDataError;
    private DatabaseHelper databaseHelper;
    private DataSourceRead dataSourceRead;


    private ArrayList<String> xmlList = new ArrayList<>();
    private ArrayList<Integer> loginList = new ArrayList<>();
    private ExtraTask extraTask = new ExtraTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data_new);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);

        Button buttonBrowse = findViewById(R.id.buttonBrowse);
        Button buttonImport = findViewById(R.id.buttonImport);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back_icon);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        dataSourceRead = new DataSourceRead(getApplicationContext());
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logowith_space);


        textViewSelectedFile = findViewById(R.id.textViewSelectedFile);
        textViewImportDataError = findViewById(R.id.textViewImportDataError);

        buttonBrowse.setOnClickListener(v -> {

            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermissionCheck= ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writePermissionCheck < 0) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(ImportDataNewActivity.this)
                            .setTitle("Storage Permission")
                            .setMessage("Need File-Storage Permission")
                            .setPositiveButton("Ok", (dialogInterface, i) -> ActivityCompat.requestPermissions(ImportDataNewActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    FILE_STORAGE_PERMISSION))
                            .create()
                            .show();

                }
            }

            if (permissionCheck < 0) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(ImportDataNewActivity.this)
                            .setTitle("Storage Permission")
                            .setMessage("Need File-Storage Permission")
                            .setPositiveButton("Ok", (dialogInterface, i) -> ActivityCompat.requestPermissions(ImportDataNewActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    FILE_STORAGE_PERMISSION))
                            .create()
                            .show();

                }
            } else {
                Intent intent = new Intent(getApplicationContext(),
                        FilePicker.class);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }


        });

        buttonImport.setOnClickListener(v -> buttonImportOnClickTask());


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        assert activityManager != null;
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    private void buttonImportOnClickTask() {


        if (selectedFile != null) {
            boolean check = dataSourceWrite.isTableExists();
            if (check) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImportDataNewActivity.this);

                alertDialogBuilder.setTitle("Import Data")
                        .setMessage(R.string.previous_data_remove_warning)
                        .setCancelable(true)
                        .setPositiveButton("Yes",
                                (dialog, id) -> {

                                    AsyncTaskForXml asyncTaskForXml = new AsyncTaskForXml();
                                    asyncTaskForXml.execute();


                                    dialog.dismiss();
                                })
                        .setNegativeButton("No",
                                (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                AsyncTaskForXml asyncTaskForXml = new AsyncTaskForXml();
                asyncTaskForXml.execute();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_FILE) {
                if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {
                    selectedFile = new File(
                            data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                    textViewSelectedFile.setText(selectedFile.getPath());

                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FILE_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
               /* if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }*/
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void writeEncryptedFile(String input, int loginId) {
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

        Long tsLong = System.currentTimeMillis() / 1000;

        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.toString()}, null, null);
        String my_new_str = dates.replaceAll("/", "-");
        String fileString = file.getAbsolutePath() + "/AMMSMA_NEW" + "_(" + my_new_str + ")_" + branchCode + "_" + loginId + "_version(" + getResources().getString(R.string.version_control) + ")_Backup(" + tsLong.toString() + ")" + ".001";

        File fileOutput = new File(fileString);
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{fileOutput.toString()}, null, null);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput))) {
            writer.write(input);

        } catch (IOException e) {
            Log.i("Write: ", e.toString());
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
            menu.findItem(R.id.action_close_current_day).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String branchName = new DataSourceRead(getApplicationContext()).getBranchName();
        menu.findItem(R.id.action_location).setTitle(branchName);
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
        if (id == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    ImportDataNewActivity.this);

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


    @SuppressLint("SdCardPath")
    private JsonJavaViceVersaImport jsonToJavaTask(File selectedFile) {
        JsonJavaViceVersaImport jsonToJava = null;
        try {

            ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

            if (!memoryInfo.lowMemory) {
                //new ZipArchive();
                String destinationPath = selectedFile.getAbsolutePath();
                //ZipArchive.unzip(destinationPath, selectedFile.getParent(), Cryptography.DecryptPass(getResources().getString(R.string.key_encrypted_password), getResources().getString(R.string.key)));
                try {

                    ActivityCompat.requestPermissions(ImportDataNewActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            FILE_STORAGE_PERMISSION);

                    new ZipFile(selectedFile, Cryptography.DecryptPass(getResources().getString(R.string.key_encrypted_password), getResources().getString(R.string.key)).toCharArray()).extractAll(selectedFile.getParent());
                }
                catch (Exception e)
                {
                    Log.i("ZipError",e.getMessage());
                }



                File file = new File(destinationPath.replace(".001", ".json"));
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, size);
                buf.close();
                Reader targetReader = new InputStreamReader(new ByteArrayInputStream(bytes));
                jsonToJava = new Gson().fromJson(targetReader, JsonJavaViceVersaImport.class);
                targetReader.close();


                if (file.exists()) {
                    file.delete();
                }

                if (jsonToJava == null) {
                    successFlag = 3;
                }


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ImportDataNewActivity.this);

                builder.setMessage(
                        "Please close other Application, to run this process properly")
                        .setCancelable(false)
                        .setTitle("Low Memory")
                        .setPositiveButton("Cancel",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }

            return jsonToJava;


        } catch (Exception e) {
            successFlag = 2;
            dataSourceWrite.truncateAllTables();
            Log.i("error", e.toString());
            return null;
        }


    }

    private void dataInsertTask(JsonJavaViceVersaImport jsonToJava, int position) {
        try {


            int flag = dataSourceWrite.truncateAllTables();
            if (flag == 1) {
                successFlag = dataSourceWrite.dataInsert(jsonToJava, position);

            } else {
                successFlag = 2;
            }


        } catch (Exception e) {
            successFlag = 2;
            dataSourceWrite.truncateAllTables();
            sharePreferenceImport(successFlag);
            Log.i("error", e.toString());
        }


    }

    private void sharePreferenceImport(int flag) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);

        int counter = sharedPreferences.getInt(getResources().getString(R.string.ImportCounter), 0);
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

        String path[] = selectedFile.getPath().split("/");
        String fileName;
        if (path.length - 1 < 0) {
            fileName = path[0].replace(".001", "");
        } else {
            fileName = path[path.length - 1].replace(".001", "");
        }

        String currentDateTime = df.format(c.getTime());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.ImportDate) + "_" + counter, currentDateTime);
        editor.putString(getResources().getString(R.string.ImportFileName) + "_" + counter, fileName);
        editor.putInt(getResources().getString(R.string.ImportCounter), counter);
        if (flag == 1) {
            editor.putString(getResources().getString(R.string.ImportSuccessTag) + "_" + counter, "Successful");
        } else if (flag == 2) {
            editor.putString(getResources().getString(R.string.ImportSuccessTag) + "_" + counter, "Not_Successful");
        } else if (flag == 3) {
            editor.putString(getResources().getString(R.string.ImportSuccessTag) + "_" + counter, "Wrong_File");
        } else {
            editor.putString(getResources().getString(R.string.ImportSuccessTag) + "_" + counter, "Not_Defined");
        }

        editor.apply();
        editor.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForXml extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;
        private boolean tag;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(ImportDataNewActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            ArrayList<ProgramOfficer> allProgramOfficer = dataSourceRead.getAllProgramOfficerForImport();
            String xmlText;
            xmlList = new ArrayList<>();
            loginList = new ArrayList<>();
            for (int i = 0; i < allProgramOfficer.size(); i++) {
                DatabaseExportXML databaseExportXML = new DatabaseExportXML(databaseHelper.getReadableDatabase(), allProgramOfficer.get(i).getId(), getApplicationContext());
                xmlText = databaseExportXML.exportData();
                if (databaseExportXML.count() > 0) {
                    xmlList.add(xmlText);
                    loginList.add(allProgramOfficer.get(i).getCode());
                }


            }

            tag = xmlList.size() > 0;


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
                DataImportInBackGroundTask dataImportInBackGround = new DataImportInBackGroundTask();
                dataImportInBackGround.execute();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskForWriteXml extends AsyncTask<Void, Integer, Void> {
        private Dialog dialog;



        AsyncTaskForWriteXml() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(ImportDataNewActivity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_dialog);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            for (int i = 0; i < xmlList.size(); i++) {
                try {
                    writeEncryptedFile(Cryptography.Encrypt(xmlList.get(i)), loginList.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

            DataImportInBackGroundTask dataImportInBackGround = new DataImportInBackGroundTask();
            dataImportInBackGround.execute();


        }
    }

    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class DataImportInBackGroundTask extends AsyncTask<Void, Integer, Void> {


        Dialog dialog;
        TextView textViewLoading;
        TextView textViewTitle;
        JsonJavaViceVersaImport jsonJavaViceVersaImport;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(ImportDataNewActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressdialog);
            textViewLoading = dialog.findViewById(R.id.textViewLoading);
            textViewTitle = dialog.findViewById(R.id.textViewTitle);
            textViewLoading.setText(R.string.processing_warning);
            textViewTitle.setText(R.string.processing_data);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            jsonJavaViceVersaImport = jsonToJavaTask(selectedFile);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {
                if (jsonJavaViceVersaImport == null) {

                    sharePreferenceImport(successFlag);

                    dialog.dismiss();
                    textViewImportDataError.setText(R.string.merging_failed_warning_data_marge_problem);
                    SpannableStringBuilder biggerText = new SpannableStringBuilder(getString(R.string.merging_failed_warning_data_marge_problem));

                    biggerText.setSpan(new RelativeSizeSpan(2f), 0, getString(R.string.merging_failed_warning_data_marge_problem).length(), 0);

                    Toast toast = Toast.makeText(ImportDataNewActivity.this, biggerText, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    ArrayList<String> programOfficerList = new ArrayList<>();

                    for (int i = 0; i < jsonJavaViceVersaImport.getUserList().size(); i++) {
                        programOfficerList.add(jsonJavaViceVersaImport.getUserList().get(i).getName() + " (" + jsonJavaViceVersaImport.getUserList().get(i).getLogin() + ")");

                    }
                    final String users[] = programOfficerList.toArray(new String[0]);

                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ImportDataNewActivity.this);
                    builder.setTitle("Users")
                            .setIcon(R.drawable.person_mini)
                            .setCancelable(false)
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .setSingleChoiceItems(users, -1, (dialog, which) -> {
                                DataImportInBackGroundTaskForProgramOfficer dataImportInBackGround2 = new DataImportInBackGroundTaskForProgramOfficer(jsonJavaViceVersaImport, which, users[which]);
                                dataImportInBackGround2.execute();
                                dialog.dismiss();
                                /*jsonToJavaTask2(jsonJavaViceVersaImport,which);*/
                            });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DataImportInBackGroundTaskForProgramOfficer extends AsyncTask<Void, Integer, Void> {


        Dialog dialog;
        TextView textViewLoading;
        TextView textViewTitle;
        JsonJavaViceVersaImport jsonJavaViceVersaImport;
        int position;
        String programOfficer;

        DataImportInBackGroundTaskForProgramOfficer(JsonJavaViceVersaImport jsonJavaViceVersaImport, int position, String programOfficer) {
            this.jsonJavaViceVersaImport = jsonJavaViceVersaImport;
            this.position = position;
            this.programOfficer = programOfficer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new Dialog(ImportDataNewActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressdialog);
            dialog.setTitle(programOfficer);
            textViewLoading = dialog.findViewById(R.id.textViewLoading);
            textViewTitle = dialog.findViewById(R.id.textViewTitle);

            textViewLoading.setText(R.string.merging_warning);
            textViewTitle.setText(String.format("Import Data (%s)", programOfficer));
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataInsertTask(jsonJavaViceVersaImport, position);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            try {
                if (successFlag == 1) {


                    sharePreferenceImport(successFlag);

                    dialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), LoginAmmsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
                    startActivity(i);
                    finish();
                } else if (successFlag == 2) {
                    dialog.dismiss();
                    dataSourceWrite.truncateAllTables();

                    sharePreferenceImport(successFlag);

                    textViewImportDataError.setText(R.string.merging_failed_warning);
                    SpannableStringBuilder biggerText = new SpannableStringBuilder(getString(R.string.merging_failed_warning));

                    biggerText.setSpan(new RelativeSizeSpan(2f), 0, getString(R.string.merging_failed_warning).length(), 0);

                    Toast toast = Toast.makeText(ImportDataNewActivity.this, biggerText, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
