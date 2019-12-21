package asa.org.bd.ammsma.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import asa.org.bd.ammsma.BuildConfig;
import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.insideLogin.HomeActivity;
import asa.org.bd.ammsma.crypto.Cryptography;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.service.ExtraTask;

public class LoginAmmsActivity extends AppCompatActivity {


    private TextInputEditText userIdForLogin;
    private TextInputEditText passwordForLogin;

    private Toolbar toolbar;
    private DataSourceRead dataSourceRead;
    private static final int FILE_STORAGE_PERMISSION = 673;
    private static final int CELL_PHONE_PERMISSION = 674;

    private ExtraTask extraTask = new ExtraTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login_amms);
        TimeZone tz = TimeZone.getDefault();
        if(!tz.getDisplayName(false, TimeZone.SHORT).equals(getString(R.string.actual_timezone)))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginAmmsActivity.this);

            builder.setMessage(
                    R.string.time_zone_problem)
                    .setCancelable(false)
                    .setTitle(R.string.error)
                    .setPositiveButton(R.string.ok,
                            (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
        }


        toolbar = findViewById(R.id.toolbarLogin);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);
        String firstInstallation = sharedPreferences.getString(getResources().getString(R.string.FirstInstallation), "");
        if (firstInstallation.trim().equals("")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
            df.setTimeZone(TimeZone.getTimeZone("GMT+6"));

            String currentDateTime = df.format(c.getTime());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getResources().getString(R.string.FirstInstallation), currentDateTime);
            editor.apply();
            editor.commit();
        }


        Button loginButton = findViewById(R.id.sign_in_button);
        Button resetButton = findViewById(R.id.reset_button);

        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);
        String title = "AMMSMA Version ( " + BuildConfig.VERSION_NAME + " )";
        getSupportActionBar().setTitle(title);

        toolbarNavigationClick();
        userIdForLogin = findViewById(R.id.userId);
        passwordForLogin = findViewById(R.id.password);

        dataSourceRead = new DataSourceRead(getApplicationContext());
        loginButton.setOnClickListener(view -> {
            TimeZone tz1 = TimeZone.getDefault();
            if(tz1.getDisplayName(false, TimeZone.SHORT).equals(getString(R.string.actual_timezone)))
            {
                loginButtonClick();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginAmmsActivity.this);

                builder.setMessage(
                        R.string.time_zone_problem)
                        .setCancelable(false)
                        .setTitle(R.string.error)
                        .setPositiveButton(R.string.ok,
                                (dialog, id) -> {
                                    clearField();
                                    dialog.dismiss();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
        resetButton.setOnClickListener(v -> clearField());
        createInitialFolders();


        int storagePermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int phonePermissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE);

        if (storagePermissionCheck < 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(LoginAmmsActivity.this)
                        .setTitle("Storage Permission")
                        .setMessage("Need File-Storage Permission")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(LoginAmmsActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    FILE_STORAGE_PERMISSION);
                        })
                        .create()
                        .show();
            }
        }
        if (phonePermissionCheck < 0) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(LoginAmmsActivity.this)
                        .setTitle("Cellphone Permission")
                        .setMessage("Need cellphone Permission for call functionality")
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialogInterface, i) -> ActivityCompat.requestPermissions(LoginAmmsActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                CELL_PHONE_PERMISSION))
                        .create()
                        .show();
            }
        }
        try {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
        } catch (Exception e) {
            Log.e("ErrorService", e.getMessage());
        }

    }


    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    LoginAmmsActivity.this);

            builder.setMessage(
                    "want to exit from application ?")
                    .setCancelable(false)
                    .setTitle("EXIT")
                    .setPositiveButton("Yes",
                            (dialog, id) -> {
                                trimCache(getApplicationContext());
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                            })
                    .setNegativeButton("No",
                            (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
        } catch (Exception e) {
            Log.e("ErrorService", e.getMessage());
        }
        //extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill,getApplicationContext());

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill, getApplicationContext());
        } catch (Exception e) {
            Log.e("ErrorService", e.getMessage());
        }
    }

    private void loginButtonClick() {
        if (userIdForLogin.getText().toString().trim().length() == 0 && passwordForLogin.getText().toString().trim().length() == 0) {
            userIdForLogin.setError(getString(R.string.error_field_required));
            passwordForLogin.setError(getString(R.string.error_field_required));
        } else if (userIdForLogin.getText().toString().trim().length() == 0) {
            userIdForLogin.setError(getString(R.string.error_field_required));
        } else if (passwordForLogin.getText().toString().trim().length() == 0) {
            passwordForLogin.setError(getString(R.string.error_field_required));
        } else {

            loginProcessToAmms();

        }

    }


    private void loginProcessToAmms() {

        String idFromLogin = userIdForLogin.getText().toString();
        String passFromLogin = passwordForLogin.getText().toString();


        if (idFromLogin.equals("Admin")) {
            HomeActivity.exportDB();
            int result = dataSourceRead.checkAdminData(idFromLogin, passFromLogin);

            if (result != 0) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("ProgramOfficerID", -1);
                startActivity(i);
                LoginAmmsActivity.this.finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginAmmsActivity.this);

                builder.setMessage(
                        "Login information is not correct")
                        .setCancelable(false)
                        .setTitle("Error")
                        .setPositiveButton("Ok",
                                (dialog, id) -> {
                                    clearField();
                                    dialog.dismiss();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            int result = dataSourceRead.getProgramOfficerIdForLogin(idFromLogin, passFromLogin);
            if (result != 0) {
                if (dataSourceRead.getProgramOfficerDataIsInserted(idFromLogin, passFromLogin)) {
                    Intent i = new Intent(LoginAmmsActivity.this,
                            HomeActivity.class);
                    i.putExtra("ProgramOfficerId", result);
                    i.putExtra("loginId", idFromLogin);
                    startActivity(i);
                    clearField();
                    finish();
                } else if(!dataSourceRead.getLoginProgramOfficer(idFromLogin).trim().isEmpty())
                {

                    String employeeId = dataSourceRead.getLoginProgramOfficer(idFromLogin).trim();

                    SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(this.getApplicationContext().getResources().getString(R.string.SharePreferencesName), MODE_PRIVATE);
                    String otpTime = sharedPreferences.getString("OtpTime_"+employeeId , "").trim();

                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat dfDateWithTime = new SimpleDateFormat(getResources().getString(R.string.day_pattern_online),Locale.getDefault());
                    dfDateWithTime.setTimeZone(TimeZone.getTimeZone(getResources().getString(R.string.time_zone)));

                    String formattedDateWithTime = dfDateWithTime.format(currentTime);


                    if(!otpTime.equals("") && timeDifference(otpTime,formattedDateWithTime)<=5)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                LoginAmmsActivity.this);

                        builder.setMessage(sharedPreferences.getString("Password_"+employeeId,""))
                                .setCancelable(false)
                                .setTitle("OTP")
                                .setPositiveButton("Ok",
                                        (dialog, id) -> dialog.dismiss());
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
                                LoginAmmsActivity.this);

                        builder.setMessage(encrypted)
                                .setCancelable(false)
                                .setTitle("OTP")
                                .setPositiveButton("Ok",
                                        (dialog, id) -> {
                                            clearField();
                                            dialog.dismiss();
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginAmmsActivity.this);

                    builder.setMessage(
                            "Login information is not correct")
                            .setCancelable(false)
                            .setTitle("Error")
                            .setPositiveButton("Ok",
                                    (dialog, id) -> {
                                        clearField();
                                        dialog.dismiss();
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            } else {

                /*dataSourceRead.getAllProgramOfficer();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LoginAmmsActivity.this);

                builder.setMessage(
                        "Login information is not correct")
                        .setCancelable(false)
                        .setTitle("Error")
                        .setPositiveButton("Ok",
                                (dialog, id) -> {
                                    clearField();
                                    dialog.dismiss();
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }


    }



    private void clearField() {
        userIdForLogin.setText("");
        passwordForLogin.setText("");

    }


    private void createInitialFolders() {
        String apkFolder = "/AMMSMA/";
        String dataFolder = "/AMMS DATA/";
        File folderOne = new File(Environment.getExternalStorageDirectory().getPath() + apkFolder);
        File folderTwo = new File(Environment.getExternalStorageDirectory().getPath() + dataFolder);


        if (!folderOne.exists()) {
            folderOne.setExecutable(true);
            folderOne.setReadable(true);
            folderOne.setWritable(true);
            folderOne.mkdirs();
        }
        if (!folderTwo.exists()) {
            folderTwo.setExecutable(true);
            folderTwo.setReadable(true);
            folderTwo.setWritable(true);

            folderTwo.mkdirs();
        }


        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(folderOne);
        mediaScannerIntent.setData(fileContentUri);
        this.sendBroadcast(mediaScannerIntent);

        Intent mediaScannerIntent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri2 = Uri.fromFile(folderTwo);
        mediaScannerIntent2.setData(fileContentUri2);
        this.sendBroadcast(mediaScannerIntent2);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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


    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        assert dir != null;
        return dir.delete();
    }

}

