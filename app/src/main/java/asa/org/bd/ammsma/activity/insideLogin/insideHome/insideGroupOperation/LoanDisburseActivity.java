package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.customAdapter.ScheduleAdapterForListView;
import asa.org.bd.ammsma.customListView.CustomNonScrollerListView;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.database.DateAndDataConversion;
import asa.org.bd.ammsma.extra.CalenderData;
import asa.org.bd.ammsma.extra.FundSchemeList;
import asa.org.bd.ammsma.extra.LoanDisburseData;
import asa.org.bd.ammsma.jsonJavaViceVersa.Account;
import asa.org.bd.ammsma.jsonJavaViceVersa.AccountBalance;
import asa.org.bd.ammsma.jsonJavaViceVersa.InstallmentAmount;
import asa.org.bd.ammsma.jsonJavaViceVersa.LoanTransaction;
import asa.org.bd.ammsma.jsonJavaViceVersa.Schedule;
import asa.org.bd.ammsma.service.ExtraTask;


public class LoanDisburseActivity extends AppCompatActivity {
    boolean selectedProgram = false;
    private Toolbar toolbar;
    private TextView textViewLoanDisbursedDate;
    private TextView textViewFirstInstallmentDate;
    private TextView textViewServiceChargeRate;
    private EditText editTextPrincipalAmount;
    private TextView textViewTotalDisburse;
    private TextView textViewProgramName;
    private TextView textViewFundType;
    private TextView textViewSchemeType;
    private TextView textViewLoanInsurance;
    private int memberId;
    private int memberSex;
    private int groupMeetingDay;
    private int principalAmount = 0;
    private DataSourceRead dataSourceRead;
    private FundSchemeList fundSchemeList;
    private Spinner spinnerFundType;
    private Spinner spinnerScheme;
    private Spinner spinnerGracePeriod;
    private Spinner spinnerProgramName;
    private Spinner spinnerDuration;
    private Spinner spinnerInstallmentType;
    private int groupTypeId;
    private int loanGroupProgramId;
    private LoanDisburseData allProgram;
    private LoanDisburseData durationList =new LoanDisburseData();
    private LoanDisburseData installmentList = new LoanDisburseData();
    private LoanDisburseData gracePeriod = new LoanDisburseData();
    private TextView textViewServiceCharge;
    private float serviceChargeRate =0 ;
    private int installmentCount;
    private String disburseDayString;
    private LoanDisburseData installmentDates;
    private CustomNonScrollerListView listViewScheduleDates;
    private List<Float> serviceNode = new ArrayList<>();
    private InstallmentAmount instalmentAmount = new InstallmentAmount();
    private List<Float> principal = new ArrayList<>();
    private List<Float> service = new ArrayList<>();
    private List<Float> installmentAmount = new ArrayList<>();
    private List<Integer> programIdList = new ArrayList<>();
    private DataSourceWrite dataSourceWrite;

    private int secondClickPrevent =0;

    private int firstTimeScheme =0;
    private int firstTimeFund =0;
    private ExtraTask extraTask = new ExtraTask();



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_disburse);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);
        TimeZone tz = TimeZone.getDefault();
        if(!tz.getDisplayName(false, TimeZone.SHORT).equals(getString(R.string.actual_timezone)))
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                    LoanDisburseActivity.this);

            builder.setMessage(
                    R.string.time_zone_problem)
                    .setCancelable(false)
                    .setTitle(R.string.error)
                    .setPositiveButton(R.string.ok,
                            (dialog, id) -> dialog.dismiss());
            android.app.AlertDialog alert = builder.create();
            alert.show();
        }


        TextView textView_programOfficerName =  findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName =  findViewById(R.id.textViewGroupName);
        TextView txtViewMemberName =  findViewById(R.id.txtViewMemberName);

        ImageView imageViewMaleOrFemale =  findViewById(R.id.imageViewMaleOrFemale);

        Button buttonDisburse =  findViewById(R.id.buttonDisburse);

        String text = "Failed to Disburse Loan. Please correct the errors in this form and try again.";
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
        final Toast toast = Toast.makeText(getApplicationContext(), biggerText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);



        textViewLoanDisbursedDate =  findViewById(R.id.textViewLoanDisbursedDate);
        textViewServiceCharge =  findViewById(R.id.textViewServiceCharge);
        textViewTotalDisburse =  findViewById(R.id.textViewTotalDisburse);
        textViewFirstInstallmentDate =  findViewById(R.id.textViewFirstInstallmentDate);
        textViewServiceChargeRate =  findViewById(R.id.textViewServiceChargeRate);
        textViewProgramName =  findViewById(R.id.textViewProgramName);
        textViewFundType =  findViewById(R.id.textViewFundType);
        textViewSchemeType =  findViewById(R.id.textViewSchemeType);
        textViewLoanInsurance =  findViewById(R.id.textViewLoanInsurance);
        editTextPrincipalAmount =  findViewById(R.id.editTextPrincipalAmount);

        listViewScheduleDates =  findViewById(R.id.listViewScheduleDates);

        spinnerFundType =  findViewById(R.id.spinnerFundType);
        spinnerScheme =  findViewById(R.id.spinnerScheme);
        spinnerProgramName =  findViewById(R.id.spinnerProgramName);
        spinnerDuration =  findViewById(R.id.spinnerDuration);
        spinnerInstallmentType =  findViewById(R.id.spinnerInstallmentType);
        spinnerGracePeriod =  findViewById(R.id.spinnerGracePeriod);

        dataSourceRead = new DataSourceRead(getApplicationContext());
        dataSourceWrite = new DataSourceWrite(getApplicationContext());


        final int programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        int groupId = getIntent().getIntExtra("groupId", 0);

        String programOfficerName = getIntent().getStringExtra("programOfficerName");
        getIntent().getStringExtra("loginId");
        String groupName = getIntent().getStringExtra("groupName");
        String memberName = getIntent().getStringExtra("memberName");

        memberId = getIntent().getIntExtra("memberId",0);
        memberSex = getIntent().getIntExtra("memberSex",-1);
        groupTypeId = getIntent().getIntExtra("groupTypeId",1);
        loanGroupProgramId = getIntent().getIntExtra("loanGroupProgramId",1);
        programIdList = getIntent().getIntegerArrayListExtra("programIdList");
        groupMeetingDay = dataSourceRead.groupMeetingDayActual(groupId);

        @SuppressLint("InflateParams") View headerViewMemberName = ((LayoutInflater) Objects.requireNonNull(getSystemService(Context.LAYOUT_INFLATER_SERVICE)))
                .inflate(R.layout.list_view_sechedle_date_item_title,
                        null, false);

        listViewScheduleDates.addHeaderView(headerViewMemberName);


        String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();
        disburseDayString = currentDate.trim();

        SimpleDateFormat dateFromDatabase = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFromDatabase.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date dfd = null;
        try {
            dfd = dateFromDatabase.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat convertedDate = new SimpleDateFormat("EE",Locale.getDefault());
        convertedDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        textViewLoanDisbursedDate.setText(convertedDate.format(dfd)+", "+disburseDayString);


        fundSchemeList = dataSourceRead.getFundAndScheme();
        allProgram = dataSourceRead.getProgramForLoanDisburse(groupTypeId, loanGroupProgramId);

        ArrayAdapter<String> adapterFund = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                fundSchemeList.getFundListName() );
        adapterFund.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterScheme = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                fundSchemeList.getSchemeListName() );
        adapterScheme.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterPrograms = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                allProgram.getNameList() );
        adapterPrograms.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        spinnerFundType.setAdapter(adapterFund);
        spinnerScheme.setAdapter(adapterScheme);
        spinnerProgramName.setAdapter(adapterPrograms);

        spinnerFundType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(firstTimeFund==0 && position==0)
                {
                    textViewFundType.setError(null);
                    firstTimeFund++;
                }
                else if(position==0 && firstTimeFund>0)
                {
                    firstTimeFund++;
                    textViewFundType.setError("Select a \"Fund Type\"");
                }
                else
                {
                    firstTimeFund++;
                    textViewFundType.setError(null);
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(firstTimeScheme==0 && position==0)
                {
                    textViewSchemeType.setError(null);
                    firstTimeScheme++;
                }
                else if(position==0 &&  firstTimeScheme>0)
                {
                    firstTimeScheme++;
                    textViewSchemeType.setError("Select a \"Scheme Type\"");
                }
                else
                {
                    textViewSchemeType.setError(null);
                    firstTimeScheme++;
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerProgramName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
                setAdapterForDuration(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       if(memberSex==2)
       {
           imageViewMaleOrFemale.setImageResource(R.drawable.male);
       }
       else
       {
           imageViewMaleOrFemale.setImageResource(R.drawable.female);
       }

        txtViewMemberName.setText(memberName);







        if (programOfficerId != -1)
        {
            textView_programOfficerName.setText(programOfficerName);
        }
        if (programOfficerId == -1) {
            textView_programOfficerName.setText(R.string.user_admin);
        }

        textViewGroupName.setText(groupName);


        editTextPrincipalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!editTextPrincipalAmount.getText().toString().trim().equals(""))
                {
                    int value = Integer.parseInt(editTextPrincipalAmount.getText().toString().trim());
                    principalAmount = value;
                    if(value>0)
                    {
                        if(memberSex==1)
                        {
                            BigDecimal roundingLoanInsurance = new BigDecimal((0.005*value)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
                            textViewLoanInsurance.setText(String.valueOf((int) (roundingLoanInsurance.floatValue())));
                        }
                        else if(memberSex==2 || memberSex==4 || memberSex==8)
                        {
                            BigDecimal roundingLoanInsurance = new BigDecimal((0.003*value)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
                            textViewLoanInsurance.setText(String.valueOf((int) (roundingLoanInsurance.floatValue())));
                        }
                        else
                        {
                            BigDecimal roundingLoanInsurance = new BigDecimal((0.005*value)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
                            textViewLoanInsurance.setText(String.valueOf((int) (roundingLoanInsurance.floatValue())));
                        }
                        principalInterestCalculation(value, serviceNode,instalmentAmount,installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition()));
                    }
                    else
                    {
                        principalAmount =0;
                        listViewScheduleDates.setVisibility(View.GONE);
                        textViewServiceCharge.setText("");
                        textViewTotalDisburse.setText("");
                        textViewLoanInsurance.setText("");
                    }
                }
                else
                {
                    listViewScheduleDates.setVisibility(View.GONE);
                    textViewServiceCharge.setText("");
                    textViewTotalDisburse.setText("");
                    textViewLoanInsurance.setText("");
                    principalAmount =0;
                }
            }
        });


        buttonDisburse.setOnClickListener(v -> {

            TimeZone tz1 = TimeZone.getDefault();
            if(!tz1.getDisplayName(false, TimeZone.SHORT).equals(getString(R.string.actual_timezone)))
            {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
                        LoanDisburseActivity.this);

                builder.setMessage(
                        R.string.time_zone_problem)
                        .setCancelable(false)
                        .setTitle(R.string.error)
                        .setPositiveButton(R.string.ok,
                                (dialog, id) -> dialog.dismiss());
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }

            else if(secondClickPrevent ==0 && tz1.getDisplayName(false, TimeZone.SHORT).equals(getString(R.string.actual_timezone)))
            {

                boolean[] errorCheck = new boolean[7] ;
                boolean error = false;

                for(int i=0;i<programIdList.size();i++)
                {
                    if(Objects.equals(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition()), programIdList.get(i)) && dataSourceRead.hasPrimaryLoanOutstandingNotZero(memberId))
                    {
                        selectedProgram=true;
                        break;
                    }
                    selectedProgram=false;
                }

                if(selectedProgram)
                {
                    errorCheck[0] = true;
                    textViewProgramName.setError("this program already belongs to \"You\"");

                }
                else
                {
                    errorCheck[0] = false;
                    textViewProgramName.setError(null);

                }



                if(spinnerFundType.getSelectedItemPosition()==0)
                {
                    errorCheck[1] = true;
                    textViewFundType.setError("Select a \"Fund Type\"");
                }
                else
                {
                    errorCheck[1] = false;
                    textViewFundType.setError(null);
                }

                if(spinnerScheme.getSelectedItemPosition()==0)
                {
                    errorCheck[2] = true;
                    textViewSchemeType.setError("Select a \"Scheme Type\"");
                }
                else
                {
                    errorCheck[2] = false;
                    textViewSchemeType.setError(null);
                }


                if(principalAmount==0)
                {
                    errorCheck[3] = true;
                    editTextPrincipalAmount.setError("Principal Amount Must be more than \'0\'");
                    //Toast.makeText(getApplicationContext(),"Principal Amount Must be more than \'0\'",Toast.LENGTH_LONG).show();
                }
                else
                {
                    errorCheck[3] = false;
                    editTextPrincipalAmount.setError(null);
                }

                if(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())>140 && allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())<145)
                {
                    errorCheck[4] = true;

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoanDisburseActivity.this);

                    builder.setMessage(
                            "You can't add \"MSME Loan\" from this system")
                            .setCancelable(false)
                            .setTitle("MSME Loan problem")
                            .setCancelable(false)
                            .setNegativeButton("cancel",
                                    (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else
                {
                    errorCheck[4] = false;

                }

                if(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())==129 && fundSchemeList.getSchemeIdList().get(spinnerScheme.getSelectedItemPosition())==164)
                {
                    errorCheck[5] = (durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()) != 6 && durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()) != 12)
                            || new DateAndDataConversion().dateToLong("31/05/2018") > new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay();

                    if (errorCheck[5] && !errorCheck[2]) {
                        textViewSchemeType.setError("The scheme 'Biogas Plant' is applicable for only 'Primary Loan' with duration 6 and 12 months.");
                    } else if (!errorCheck[5] && !errorCheck[2]) {
                        textViewSchemeType.setError(null);
                    }
                }
                else if((allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())==129
                        ||allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())==130)
                        && gracePeriod.getIdList().get(spinnerGracePeriod.getSelectedItemPosition())==4)
                {

                    errorCheck[5] = fundSchemeList.getSchemeIdList().get(spinnerScheme.getSelectedItemPosition()) != 32;

                    if (errorCheck[5] && !errorCheck[2]) {
                        textViewSchemeType.setError("You must select Fisheries scheme for (\" "+gracePeriod.getIdList().get(spinnerGracePeriod.getSelectedItemPosition())+" \")month grace period");
                    } else if (!errorCheck[5] && !errorCheck[2]) {
                        textViewSchemeType.setError(null);
                    }
                }
                else
                {
                    errorCheck[5] = fundSchemeList.getSchemeIdList().get(spinnerScheme.getSelectedItemPosition()) == 164;

                    if(errorCheck[5] && !errorCheck[2])
                    {
                        textViewSchemeType.setError("The scheme 'Biogas Plant' is applicable for only 'Primary Loan' with duration 6 and 12 months.");
                    }
                    else if(!errorCheck[5] && !errorCheck[2])
                    {
                        textViewSchemeType.setError(null);
                    }
                }

                errorCheck[6] = loanDisburseLimit(Integer.parseInt(editTextPrincipalAmount.getText().toString().trim()),allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition()));
                if(errorCheck[6])
                {
                    editTextPrincipalAmount.setError("Disbursed amount error");
                }
                else
                {
                    editTextPrincipalAmount.setError(null);
                }





                for (boolean anErrorCheck : errorCheck) {
                    if (anErrorCheck) {
                        error = true;
                        break;
                    }
                }

                if(!error)
                {
                    secondClickPrevent =1;
                    Account loanAccount = new Account();

                    int accountId= dataSourceRead.getNextAccount();
                    int disbursedAmountWithSC = Integer.parseInt(textViewTotalDisburse.getText().toString().trim());


                    loanAccount.setMemberId(memberId);
                    loanAccount.setProgramId(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition()));
                    loanAccount.setProgramTypeId(1);
                    loanAccount.setDuration(durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()));
                    loanAccount.setInstallmentType(installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition()));
                    loanAccount.setSupplementary(false);
                    loanAccount.setMemberSex(memberSex);
                    loanAccount.setOpeningDate((int) new DateAndDataConversion().dateToLong(disburseDayString));



                    loanAccount.setDisbursedAmount(Float.valueOf(editTextPrincipalAmount.getText().toString().trim()));
                    loanAccount.setServiceChargeAmount(Float.valueOf(textViewServiceCharge.getText().toString().trim()));
                    loanAccount.setMinimumDeposit((float) 0);
                    loanAccount.setMeetingDayOfWeek(groupMeetingDay);
                    loanAccount.setMeetingDayOfMonth(groupMeetingDay);
                    loanAccount.setFirstInstallmentDate(textViewFirstInstallmentDate.getText().toString().trim());
                    loanAccount.setGracePeriod(gracePeriod.getIdList().get(spinnerGracePeriod.getSelectedItemPosition()));
                    loanAccount.setSchemeId(fundSchemeList.getSchemeIdList().get(spinnerScheme.getSelectedItemPosition()));
                    loanAccount.setFundId(fundSchemeList.getFundIdList().get(spinnerFundType.getSelectedItemPosition()));
                    loanAccount.setId(accountId);

                    loanAccount.setDisbursedAmountWithSC((float) disbursedAmountWithSC);
                    loanAccount.setProgramName(allProgram.getNameList().get(spinnerProgramName.getSelectedItemPosition()));
                    loanAccount.setLoanInsuranceAmount(Float.parseFloat(textViewLoanInsurance.getText().toString().trim()));
                    loanAccount.setProgramOfficerId(programOfficerId);

                    boolean successful =false;
                    try
                    {
                        successful  = dataSourceWrite.insertLoanAccount(loanAccount);
                    }
                    catch (Exception e)
                    {
                        Log.i("LoanError",e.getLocalizedMessage());
                    }

                    if(successful)
                    {

                        AccountBalance accountBalanceLoan = new AccountBalance();

                        accountBalanceLoan.setAccountId(accountId);
                        accountBalanceLoan.setDate(installmentDates.getIdList().get(0));

                        accountBalanceLoan.setBalance((float) disbursedAmountWithSC);
                        accountBalanceLoan.setDebit((float) 0);
                        accountBalanceLoan.setProgramType(1);
                        accountBalanceLoan.setCredit((float) 0);
                        dataSourceWrite.insertNewAccountBalance(accountBalanceLoan,programOfficerId);


                        Schedule schedule = new Schedule();
                        schedule.setAccountId(accountId);
                        schedule.setBaseInstallmentAmount(installmentAmount.get(0));
                        schedule.setInstallmentAmount(installmentAmount.get(0));
                        schedule.setScheduledDate(installmentDates.getIdList().get(0));
                        schedule.setScheduled(true);
                        try {
                            if(installmentCount==1)
                            {
                                schedule.setNextDate(installmentDates.getIdList().get(0));
                            }
                            else
                            {
                                schedule.setNextDate(nextScheduleDay(installmentDates.getIdList().get(1)));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        schedule.setPaidAmount((float) 0);
                        schedule.setAdvanceAmount((float) 0);
                        schedule.setOverDueAmount((float) 0);
                        schedule.setOutstandingAmount((float) disbursedAmountWithSC);
                        schedule.setPrincipalOutstanding((float) principalAmount);

                        int  installmentNumberActual=0;
                        for (int i=0 ; i<installmentDates.getNameList().size();i++)
                        {
                            if (service.get(i).intValue() != 0 || principal.get(i).intValue() != 0 || installmentAmount.get(i).intValue() != 0
                                    || service.get(installmentDates.getNameList().size() - 1).intValue() != 0
                                    || principal.get(installmentDates.getNameList().size() - 1).intValue() != 0
                                    || installmentAmount.get(installmentDates.getNameList().size() - 1).intValue() != 0
                                    || installmentDates.getNameList().size() <= 70) {
                                        installmentNumberActual++;
                                    }
                        }

                        schedule.setMaxInstallmentNumber(installmentNumberActual);
                        schedule.setId(-12345);
                        dataSourceWrite.insertSchedule(schedule);

                        LoanTransaction loanTransaction = new LoanTransaction();

                        loanTransaction.setAccountId(accountId);
                        loanTransaction.setDebit(Float.parseFloat(textViewServiceCharge.getText().toString().trim()));
                        loanTransaction.setDate((int) new DateAndDataConversion().dateToLong(disburseDayString));
                        dataSourceWrite.insertLoanTransaction(loanTransaction);

                        Toast.makeText(getApplicationContext(),"Loan Account added \"successfully\"",Toast.LENGTH_LONG).show();
                        secondClickPrevent =0;
                        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop,getApplicationContext());
                        finish();
                    }


                }
                else
                {

                    if(!toast.getView().isShown())
                    {
                        toast.show();
                    }
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            }


        });

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        toolbarNavigationClick();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign,getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
        editTextPrincipalAmount.setText("");

    }

    private boolean loanDisburseLimit(int disburseAmount, int p_ProgramId)
    {
        return (p_ProgramId == 129 && disburseAmount > 99000)
                || (p_ProgramId == 106 && disburseAmount > 20000)
                || (p_ProgramId == 108 && disburseAmount > 5000)
                || (p_ProgramId == 132 && disburseAmount > 15000)
                || (p_ProgramId == 135 && (disburseAmount < 10000 || disburseAmount > 50000))
                || (p_ProgramId == 130 && (disburseAmount < 100000 || disburseAmount > 1000000))
                || (p_ProgramId == 131 && disburseAmount > 1000000)
                || (p_ProgramId == 137 && (disburseAmount < 5000 || disburseAmount > 200000))
                || (p_ProgramId == 138 && disburseAmount > 1000000);

    }



    private void setAdapterForGracePeriod(int programId, int duration, int installmentType)
    {
        gracePeriod = dataSourceRead.getGracePeriod(programId,duration,installmentType,memberSex);


        ArrayAdapter<String> adapterGracePeriod = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member,
                gracePeriod.getNameList() );
        adapterGracePeriod.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        spinnerGracePeriod.setAdapter(adapterGracePeriod);




        spinnerGracePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                textViewServiceCharge.setText("");
                textViewTotalDisburse.setText("");
                listViewScheduleDates.setVisibility(View.GONE);


                serviceChargeRate = dataSourceRead.getServiceChargeRate(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())
                        ,durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()),installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition())
                        ,memberSex,new DataSourceOperationsCommon(getApplicationContext()).getWorkingDay(),-1);


                installmentCount = dataSourceRead.getInstallmentCount(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())
                        ,durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()),installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition())
                        ,gracePeriod.getIdList().get(position),memberSex);


                instalmentAmount  = dataSourceRead.getInstallmentAmount(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition())
                        ,durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()),installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition())
                        ,gracePeriod.getIdList().get(spinnerGracePeriod.getSelectedItemPosition()),memberSex);

                textViewServiceChargeRate.setText(Math.round(serviceChargeRate*100)+"%");







                if(installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition())==1)
                {
                    try {
                        weeklyInstallmentDayCalculation(Integer.parseInt(spinnerGracePeriod.getSelectedItem().toString().trim()),groupMeetingDay, installmentCount);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.i("Error",e.getMessage());
                    }
                }
                else if(installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition())==2)
                {
                    try {
                        monthlyInstallmentDayCalculation(Integer.parseInt(spinnerGracePeriod.getSelectedItem().toString().trim()),groupMeetingDay, installmentCount);
                    } catch (ParseException e) {
                        e.printStackTrace();

                        Log.i("Error",e.getMessage());
                    }
                }

                else if(installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition())==4)
                {
                    try {

                        shortTermInstallmentDayCalculation(durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()),groupMeetingDay);
                    } catch (ParseException e) {
                        e.printStackTrace();

                        Log.i("Error",e.getMessage());
                    }
                }



                if(!editTextPrincipalAmount.getText().toString().trim().equals(""))
                {
                    int value = Integer.parseInt(editTextPrincipalAmount.getText().toString().trim());
                    principalAmount = value;
                    if(value>0)
                    {
                        if(memberSex==1)
                        {
                            BigDecimal roundingLoanInsurance = new BigDecimal((0.005*value)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
                            textViewLoanInsurance.setText(String.valueOf((int) (roundingLoanInsurance.floatValue())));
                        }
                        else if(memberSex==2 || memberSex==4 || memberSex==8)
                        {
                            BigDecimal roundingLoanInsurance = new BigDecimal((0.003*value)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
                            textViewLoanInsurance.setText(String.valueOf((int) (roundingLoanInsurance.floatValue())));
                        }
                        else
                        {
                            BigDecimal roundingLoanInsurance = new BigDecimal((0.005*value)).setScale(0, BigDecimal.ROUND_HALF_EVEN);
                            textViewLoanInsurance.setText(String.valueOf((int) (roundingLoanInsurance.floatValue())));
                        }
                        principalInterestCalculation(value, serviceNode,instalmentAmount,installmentList.getIdList().get(spinnerInstallmentType.getSelectedItemPosition()));
                    }
                    else
                    {
                        principalAmount =0;
                        listViewScheduleDates.setVisibility(View.GONE);
                        textViewServiceCharge.setText("");
                        textViewTotalDisburse.setText("");
                        textViewLoanInsurance.setText("");
                    }
                }
                else
                {
                    listViewScheduleDates.setVisibility(View.GONE);
                    textViewServiceCharge.setText("");
                    textViewTotalDisburse.setText("");
                    textViewLoanInsurance.setText("");
                    principalAmount =0;
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAdapterForDuration(int position)
    {
        durationList = dataSourceRead.getDuration(groupTypeId,loanGroupProgramId, allProgram.getIdList().get(position));


        ArrayAdapter<String> adapterDuration = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member,
                durationList.getNameList() );
        adapterDuration.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        spinnerDuration.setAdapter(adapterDuration);
        for(int i=0;i<durationList.getIdList().size();i++)
        {
            if(durationList.getIdList().get(i)==12)
            {
                spinnerDuration.setSelection(i);
            }
        }
        setAdapterInstalmentType(position,spinnerDuration.getSelectedItemPosition());


        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAdapterInstalmentType(spinnerProgramName.getSelectedItemPosition(),position);
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    private void setAdapterInstalmentType(int positionProgram , int positionDuration)
    {

        installmentList = dataSourceRead.getInstallment(groupTypeId,loanGroupProgramId, allProgram.getIdList().get(positionProgram),durationList.getIdList().get(positionDuration));

        ArrayAdapter<String> adapterInstallment = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_for_add_new_member,
                installmentList.getNameList() );
        adapterInstallment.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        spinnerInstallmentType.setAdapter(adapterInstallment);



        spinnerInstallmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setAdapterForGracePeriod(allProgram.getIdList().get(spinnerProgramName.getSelectedItemPosition()),
                        durationList.getIdList().get(spinnerDuration.getSelectedItemPosition()),
                        installmentList.getIdList().get(position));

                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset,getApplicationContext());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void weeklyInstallmentDayCalculation(int grace, int groupMeetingDay, int installmentCount) throws ParseException {


        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String disburseDay = disburseDayString;
        final Date date = format.parse(disburseDay);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayAdd = (grace * 7);
        calendar.add(Calendar.DAY_OF_YEAR, dayAdd);
        installmentDates = new LoanDisburseData();


        int searchStartDay = (int) new DateAndDataConversion().dateToLong(format.format(calendar.getTime()));


        installmentDates = dataSourceRead.getInstallmentDaysForWeek(searchStartDay,groupMeetingDay,installmentCount,grace);

        if(installmentDates.getNameList().size()==0)
        {
            textViewFirstInstallmentDate.setText("Can,t Generated");
        }
        else
        {
            textViewFirstInstallmentDate.setText(installmentDates.getNameList().get(0));
        }


        dayDifferBetweenDates(textViewLoanDisbursedDate.getText().toString().trim());



    }
    private int nextScheduleDay(int day) throws ParseException {

        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String disburseDay = new DateAndDataConversion().getDateFromInt(day);
        final Date date = format.parse(disburseDay);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        String nextDay = format.format(calendar.getTime());

        return (int) new DateAndDataConversion().dateToLong(nextDay);


    }


    private void shortTermInstallmentDayCalculation(int duration, int groupMeetingDay) throws ParseException {

        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String disburseDay = disburseDayString;
        final Date date = format.parse(disburseDay);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH)+1+duration;
        int year = calendar.get(Calendar.YEAR);
        int count =0;
        if(month>12)
        {
            year += month/12;
            month %=12;
        }

        installmentDates = new LoanDisburseData();
        List<String> datesString = new ArrayList<>();
        List<Integer> datesInteger = new ArrayList<>();


        while (installmentCount > count)
        {
            CalenderData calenderData = dataSourceRead.getInstallmentDayForMonth(day,month,year,groupMeetingDay);

            if(calenderData.getDayInteger()>0)
            {
                datesString.add(calenderData.getDateString());
                datesInteger.add(calenderData.getDateInteger());
                count++;
            }





        }
        installmentDates.setNameList(datesString);
        installmentDates.setIdList(datesInteger);

        textViewFirstInstallmentDate.setText(installmentDates.getNameList().get(0));
        dayDifferBetweenDates(textViewLoanDisbursedDate.getText().toString().trim());



    }



    private void monthlyInstallmentDayCalculation(int grace, int groupMeetingDay, int installmentCount) throws ParseException {

        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String disburseDay = disburseDayString;
        final Date date = format.parse(disburseDay);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.MONTH, grace);



        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        int count =0;

        installmentDates = new LoanDisburseData();
        List<String> datesString = new ArrayList<>();
        List<Integer> datesInteger = new ArrayList<>();


        while (installmentCount > count)
        {
            if(month<12)
            {
                month+=1;
            }
            else
            {
                month =1;
                year +=1;
            }

            CalenderData calenderData = dataSourceRead.getInstallmentDayForMonth(day,month,year,groupMeetingDay);

            if(calenderData.getDayInteger()>0)
            {


                datesString.add(calenderData.getDateString());
                datesInteger.add(calenderData.getDateInteger());
                count++;
            }



        }
        installmentDates.setNameList(datesString);
        installmentDates.setIdList(datesInteger);

        textViewFirstInstallmentDate.setText(installmentDates.getNameList().get(0));
        dayDifferBetweenDates(textViewLoanDisbursedDate.getText().toString().trim());

    }

    @SuppressLint("DefaultLocale")
    private void principalInterestCalculation(int principalAmount, List<Float> serviceNode, InstallmentAmount instalmentAmount, int installmentType) {




        if(installmentType==1 || installmentType==2)
        {

            int serviceNodeSize= serviceNode.size();
            float serviceFinal = 0;
            BigDecimal roundingInstalmentAmount = new BigDecimal((instalmentAmount.getAmountPerBase() / instalmentAmount.getBaseAmount()) * principalAmount).setScale(0, BigDecimal.ROUND_HALF_EVEN);
            float instalmentAmountFinal = roundingInstalmentAmount.floatValue();

            float principalAmountChange = (float) principalAmount;

            float tempService = 0;
            int tempFlag = 0;
            int negativePrincipalFlag =0;
            float extraService =0;



            for (int i = 0; i < serviceNodeSize-1; i++) {


                if (principalAmountChange >= instalmentAmountFinal) {
                    BigDecimal roundingService = new BigDecimal((serviceNode.get(i) * principalAmountChange)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    float serviceValue = roundingService.floatValue()+extraService;
                    extraService =0;
                    serviceFinal += serviceValue;

                    if(serviceValue>=instalmentAmountFinal)
                    {
                        extraService= serviceValue-instalmentAmountFinal;
                        serviceValue = instalmentAmountFinal;
                    }

                    BigDecimal roundingPrincipal = new BigDecimal(instalmentAmountFinal - serviceValue).setScale(2, BigDecimal.ROUND_HALF_UP);



                    float principalValue = roundingPrincipal.floatValue();

                    if (principalValue < 0) {

                        if (tempFlag == 0) {
                            tempService = 0;
                            tempService += serviceValue;
                            principalValue = 0;
                            serviceValue = 0;
                            tempFlag = 1;

                        } else {
                            tempService += serviceValue;
                            principalValue = 0;
                            serviceValue = 0;
                            tempFlag = 1;

                        }

                    } else {
                        serviceValue += tempService;
                        tempFlag = 0;
                        tempService = 0;

                    }


                    principalAmountChange -= principalValue;
                    if(principalAmountChange<0)
                    {
                        negativePrincipalFlag =1;
                    }


                    principal.set(i, principalValue);
                    service.set(i, serviceValue);
                    installmentAmount.set(i, instalmentAmountFinal);

                }
                else if(principalAmountChange >0 && principalAmountChange<=instalmentAmountFinal && installmentCount>70){

                    float totalCollectedPrincipal=0;
                    for (int j=0 ;j<i;j++ ) {
                        totalCollectedPrincipal= new BigDecimal(totalCollectedPrincipal+principal.get(j)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                    }
                    principalAmountChange = (principalAmount-totalCollectedPrincipal);
                    BigDecimal roundingService = new BigDecimal((serviceNode.get(i) * principalAmountChange)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    float serviceValue = roundingService.floatValue()+extraService;
                    extraService=0;
                    tempFlag = 0;
                    tempService = 0;


                    float value =new BigDecimal(principalAmountChange+serviceValue).setScale(0, BigDecimal.ROUND_CEILING).floatValue();
                    serviceValue = value-principalAmountChange;
                    if(serviceValue<0)
                    {
                        principalAmountChange =value;
                        serviceValue = 0;
                    }
                    principal.set(i, principalAmountChange);
                    service.set(i, serviceValue);
                    installmentAmount.set(i,  value);
                    serviceFinal += serviceValue;

                    principalAmountChange =0;

                }
                else {
                    negativePrincipalFlag=1;
                    principal.set(i, (float) 0);
                    service.set(i, (float) 0);
                    installmentAmount.set(i, (float) 0);
                }

            }




            if(negativePrincipalFlag==1)
            {
                BigDecimal roundingPrincipal = new BigDecimal(principalAmountChange).setScale(2, BigDecimal.ROUND_HALF_UP);
                float negativePrincipal = roundingPrincipal.floatValue();


                if(negativePrincipal>0)
                {
                    for(int i = serviceNodeSize-1 ;i>0;i--)
                    {

                        if(negativePrincipal>0)
                        {
                            if(service.get(i)<negativePrincipal)
                            {
                                BigDecimal serviceTRA = new BigDecimal(negativePrincipal-service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal principalTRA = new BigDecimal(principal.get(i)+service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                negativePrincipal= serviceTRA.floatValue();
                                principal.set(i,principalTRA.floatValue());
                                service.set(i, (float) 0);
                            }
                            else
                            {

                                BigDecimal principalTRA = new BigDecimal(principal.get(i)+negativePrincipal).setScale(2, BigDecimal.ROUND_HALF_UP);
                                principal.set(i,principalTRA.floatValue());
                                BigDecimal serviceTRA = new BigDecimal(service.get(i)-negativePrincipal).setScale(2, BigDecimal.ROUND_HALF_UP);
                                service.set(i, serviceTRA.floatValue());
                                negativePrincipal=0;
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }


            BigDecimal roundingService = new BigDecimal((serviceNode.get(serviceNodeSize-1) * principalAmountChange)).setScale(2, BigDecimal.ROUND_HALF_UP);
            float serviceValue = roundingService.floatValue()+extraService;
            serviceFinal += serviceValue;

            BigDecimal roundingPrincipal = new BigDecimal(principalAmountChange).setScale(2, BigDecimal.ROUND_HALF_UP);
            float principalValue = roundingPrincipal.floatValue();
            BigDecimal roundingInstallment = new BigDecimal(serviceValue+principalValue).setScale(0, BigDecimal.ROUND_CEILING);

            principal.set(serviceNodeSize-1, principalValue);
            service.set(serviceNodeSize-1, serviceValue);
            installmentAmount.set(serviceNodeSize-1, roundingInstallment.floatValue());

            BigDecimal roundingFinalService = new BigDecimal(serviceFinal - (int) serviceFinal).setScale(2, BigDecimal.ROUND_HALF_UP);
            float serviceChange = roundingFinalService.floatValue();


            if(tempService>0)
            {
                BigDecimal roundingTempService = new BigDecimal(tempService).setScale(2, BigDecimal.ROUND_HALF_UP);
                service.set(serviceNodeSize-1,roundingTempService.floatValue());
                BigDecimal roundingTempInstallment = new BigDecimal(installmentAmount.get(serviceNodeSize-1)+roundingTempService.floatValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
                installmentAmount.set(serviceNodeSize-1,roundingTempInstallment.floatValue());
                serviceChange = roundingTempService.floatValue()-(int)roundingTempService.floatValue();
            }

            if(serviceChange>0)
            {
                for(int i = serviceNodeSize-1 ;i>0;i--)
                {

                    if(serviceChange>0)
                    {

                        if(service.get(serviceNodeSize-1)==0 && principal.get(serviceNodeSize-1)==0)
                        {
                            break;
                        }

                        if(service.get(i)<serviceChange)
                        {
                            BigDecimal serviceTRA = new BigDecimal(serviceChange-service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal principalTRA = new BigDecimal(principal.get(i)+service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            serviceChange= serviceTRA.floatValue();
                            principal.set(i,principalTRA.floatValue());
                            service.set(i, (float) 0);
                            if((principal.get(i)+service.get(i))>installmentAmount.get(i))
                            {
                                principal.set(i,installmentAmount.get(i));
                            }


                        }
                        else
                        {

                            BigDecimal principalTRA = new BigDecimal(principal.get(i)+serviceChange).setScale(2, BigDecimal.ROUND_HALF_UP);
                            principal.set(i,principalTRA.floatValue());
                            BigDecimal serviceTRA = new BigDecimal(service.get(i)-serviceChange).setScale(2, BigDecimal.ROUND_HALF_UP);
                            service.set(i, serviceTRA.floatValue());
                            if((principal.get(i)+service.get(i))>installmentAmount.get(i))
                            {
                                principal.set(i,installmentAmount.get(i));
                            }
                            serviceChange=0;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }

            //lastCalculation(principalAmountChange,serviceNodeSize,extraService);
            if(service.get(serviceNodeSize-1)>0 && principal.get(serviceNodeSize-1)+service.get(serviceNodeSize-1)>installmentAmount.get(serviceNodeSize-1))
            {
                installmentAmount.set(serviceNodeSize - 1,new BigDecimal(service.get(serviceNodeSize-1)+principal.get(serviceNodeSize-1)).setScale(0, BigDecimal.ROUND_FLOOR).floatValue());
                principal.set(serviceNodeSize-1,new BigDecimal(installmentAmount.get(serviceNodeSize-1)-service.get(serviceNodeSize-1)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                service.set(serviceNodeSize-1,new BigDecimal(installmentAmount.get(serviceNodeSize-1)-principal.get(serviceNodeSize-1)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());

            }
            else if(service.get(serviceNodeSize-1)==0 && !Objects.equals(principal.get(serviceNodeSize - 1), installmentAmount.get(serviceNodeSize - 1)))
            {
                principal.set(serviceNodeSize - 1, installmentAmount.get(serviceNodeSize - 1));
            }
            else if(service.get(serviceNodeSize-1)>0 && principal.get(serviceNodeSize-1)+service.get(serviceNodeSize-1)<installmentAmount.get(serviceNodeSize-1)) {

                installmentAmount.set(serviceNodeSize - 1,new BigDecimal(service.get(serviceNodeSize-1)+principal.get(serviceNodeSize-1)).setScale(0, BigDecimal.ROUND_FLOOR).floatValue());
                principal.set(serviceNodeSize - 1, new BigDecimal(installmentAmount.get(serviceNodeSize - 1) - service.get(serviceNodeSize - 1)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }

            if(principalAmount<principal.get(serviceNodeSize-1))
            {
                principal.set(serviceNodeSize-1,new BigDecimal(principalAmount).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                service.set(serviceNodeSize-1,new BigDecimal(installmentAmount.get(serviceNodeSize-1)-principal.get(serviceNodeSize-1)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }

            float principalFinal =0;

            for(int i=0;i<serviceNodeSize;i++)
            {
                principalFinal+= principal.get(i);
            }
           float principalDifference = new BigDecimal(principalAmount-principalFinal).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
           float principalDif = principalDifference;

           if(principalDifference>0)
           {
               for(int i = serviceNodeSize; i >= 0; i--)
               {
                   if(principal.get(i)>0 && service.get(i)>0 && service.get(i)>=principalDifference)
                   {
                       principal.set(i,new BigDecimal(principal.get(i)+principalDifference).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                       service.set(i,new BigDecimal(service.get(i)-principalDifference).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                       principalDifference =0;
                   }
                   else if(principal.get(i)>0 && service.get(i)>0 && service.get(i)<principalDifference)
                   {
                       principal.set(i,new BigDecimal(principal.get(i)+service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                       principalDifference  = new BigDecimal(principalDifference - service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                       service.set(i,new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                   }

               }
           }
            if(principalDifference<0) {
                principalDifference = Math.abs(principalDifference);
                for (int i = serviceNodeSize; i >= 0; i--) {
                    if (principal.get(i) > 0 && service.get(i) > 0 && principal.get(i) >= principalDifference) {
                        principal.set(i, new BigDecimal(principal.get(i) - principalDifference).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        service.set(i, new BigDecimal(service.get(i) + principalDifference).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        break;
                    } else if (principal.get(i) > 0 && service.get(i) > 0 && principal.get(i) < principalDifference) {
                        service.set(i, new BigDecimal(principal.get(i) + service.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        principalDifference = new BigDecimal(principalDifference - principal.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                        principal.set(i, new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        if(principalDifference==0)
                        {
                            break;
                        }
                    }
                }
            }


            if((int) Math.abs(principalDif)>=1 && principalDif<0)
            {
                int done = 0;
                for(int i = serviceNodeSize; i >= 0; i--)
                {
                    if( service.get(i)>(int) Math.abs(principalDif) )
                    {

                        if(installmentAmount.get(serviceNodeSize - 1)>(int) Math.abs(principalDif)*2 && principal.get(serviceNodeSize-1)>(int) Math.abs(principalDif))
                        {
                            installmentAmount.set(serviceNodeSize - 1,new BigDecimal(installmentAmount.get(serviceNodeSize-1)-(int) Math.abs(principalDif)).setScale(0, BigDecimal.ROUND_FLOOR).floatValue());
                            principal.set(serviceNodeSize-1,new BigDecimal(principal.get(serviceNodeSize-1)-(int) Math.abs(principalDif)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            service.set(serviceNodeSize-1,new BigDecimal(installmentAmount.get(serviceNodeSize-1)-principal.get(serviceNodeSize-1)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            done = 1;
                        }
                        if(done== 1)
                        {
                            principal.set(i,new BigDecimal(principal.get(i)+(int) Math.abs(principalDif)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            service.set(i,new BigDecimal(service.get(i)-(int) Math.abs(principalDif)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            break;
                        }

                    }
                }
            }
            serviceFinal =0;
            for(int i=0;i<serviceNodeSize;i++)
            {
                serviceFinal+= service.get(i);
            }
            textViewServiceCharge.setText(String.valueOf(new BigDecimal(serviceFinal).setScale(0, BigDecimal.ROUND_HALF_EVEN).intValue()));
            textViewTotalDisburse.setText(String.valueOf(Math.round(principalAmount + new BigDecimal(serviceFinal).setScale(0, BigDecimal.ROUND_HALF_EVEN).intValue())));
            listViewScheduleDates.setVisibility(View.VISIBLE);

            listViewScheduleDates.invalidateViews();
            listViewScheduleDates.invalidate();
        }
        else if(installmentType==4)
        {
            for(int i= 0;i<installmentCount;i++)
            {
                String startDate = textViewLoanDisbursedDate.getText().toString().trim();
                SimpleDateFormat sdf = new SimpleDateFormat("EE, dd/MM/yyyy",Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
                Date startDay = null;
                Date endDay = null;
                try {
                    if(i==0)
                    {
                        startDay = sdf.parse(startDate);
                        endDay = sdf.parse(installmentDates.getNameList().get(0));
                    }
                    else
                    {
                        startDay = sdf.parse(installmentDates.getNameList().get(i-1));
                        endDay = sdf.parse(installmentDates.getNameList().get(i));
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert endDay != null;
                long diff = endDay.getTime() - startDay.getTime();

                int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                principal.set(i, (float) principalAmount);
                BigDecimal roundingService = new BigDecimal(principalAmount*((days*serviceChargeRate)/365)).setScale(0, BigDecimal.ROUND_HALF_UP);
                service.set(i,roundingService.floatValue());
                installmentAmount.set(i, (float) Math.round(principalAmount+roundingService.floatValue()));
            }

            textViewServiceCharge.setText(String.valueOf(Math.round(service.get(0))));

            textViewTotalDisburse.setText(String.valueOf(Math.round(installmentAmount.get(0))));


            listViewScheduleDates.setVisibility(View.VISIBLE);

            listViewScheduleDates.invalidateViews();
            listViewScheduleDates.invalidate();
        }

    }

/*    private void lastCalculation(float principalAmountChange,int serviceNodeSize,float extraService)
    {

        float totalCollectedPrincipal=0;
        for (int i=0 ;i<serviceNodeSize-1;i++ ) {
            totalCollectedPrincipal = new BigDecimal(totalCollectedPrincipal + principal.get(i)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        principalAmountChange=new BigDecimal(principalAmount-totalCollectedPrincipal).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        BigDecimal roundingPrincipal = new BigDecimal(principalAmountChange).setScale(2, BigDecimal.ROUND_HALF_UP);
        float principalValue = roundingPrincipal.floatValue();
        BigDecimal roundingService = new BigDecimal((serviceNode.get(serviceNodeSize-1) * principalAmountChange)).setScale(2, BigDecimal.ROUND_HALF_UP);
        float serviceValue = roundingService.floatValue()+extraService;

        BigDecimal roundingInstallment = new BigDecimal(serviceValue+principalValue).setScale(0, BigDecimal.ROUND_CEILING);



        if(((serviceValue+principalValue)-roundingInstallment.floatValue()) > 0.7)
        {
            roundingInstallment = new BigDecimal(serviceValue+principalValue).setScale(0, BigDecimal.ROUND_HALF_UP);
            principal.set(serviceNodeSize-1, principalValue);
            service.set(serviceNodeSize-1, roundingInstallment.floatValue()-principalValue);
            installmentAmount.set(serviceNodeSize-1, roundingInstallment.floatValue());

        }
        else
        {

            principal.set(serviceNodeSize-1, principalValue);
            service.set(serviceNodeSize-1,serviceValue);
            installmentAmount.set(serviceNodeSize-1, roundingInstallment.floatValue());
        }
    }*/



    private void dayDifferBetweenDates(String startDate)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd/MM/yyyy",Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date startDay = null;
        Date endDay = null;
        List<Integer> dayDifference = new ArrayList<>();
        serviceNode = new ArrayList<>();


        try {
            startDay = sdf.parse(startDate);
            endDay = sdf.parse(installmentDates.getNameList().get(0));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i=0;i<installmentDates.getNameList().size();i++)
        {

            assert endDay != null;
            long diff = endDay.getTime() - startDay.getTime();

            int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            dayDifference.add(days);
            serviceNode.add((days*serviceChargeRate)/365);
            if(installmentDates.getNameList().size()> i+1)
            {
                try {
                    startDay =sdf.parse(installmentDates.getNameList().get(i));
                    endDay = sdf.parse(installmentDates.getNameList().get(i+1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            principal.add(i,(float) 0);
            BigDecimal roundingPrice = new BigDecimal((days*serviceChargeRate)/365).setScale(2, BigDecimal.ROUND_HALF_UP);
            service.add(i,roundingPrice.floatValue());
            installmentAmount.add(i,(float) 0);



            if(i==0)
            {
                listViewScheduleDates.setVisibility(View.GONE);
                listViewScheduleDates.setAdapter(new ScheduleAdapterForListView(getApplicationContext(),installmentDates.getNameList(),principal,service,installmentAmount));
            }


        }


    }


    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop,getApplicationContext());
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_amms, menu);
        try {
            String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();

            SimpleDateFormat dateFromDatabase = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
            dateFromDatabase.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dfd = dateFromDatabase.parse(currentDate);

            SimpleDateFormat convertedDate = new SimpleDateFormat("EE",Locale.getDefault());
            convertedDate.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            menu.findItem(R.id.action_working_day).setTitle(currentDate+" "+convertedDate.format(dfd));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String branchName = new DataSourceRead(getApplicationContext()).getBranchName();
        menu.findItem(R.id.action_location).setTitle(branchName);
        menu.findItem(R.id.action_close_current_day).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout)
        {
            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    LoanDisburseActivity.this);

            builder.setMessage(
                    "Are you sure Log-Out from Application?")
                    .setCancelable(false)
                    .setTitle("Log Out")
                    .setPositiveButton("Yes",
                            (dialog, id1) -> {
                                Intent i = new Intent(getApplicationContext(),LoginAmmsActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Kill,getApplicationContext());
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
}
