package asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.LoginAmmsActivity;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.dialog.DatePickerFragmentForMemberAge;
import asa.org.bd.ammsma.extra.NewMember;
import asa.org.bd.ammsma.jsonJavaViceVersa.Group;
import asa.org.bd.ammsma.service.ExtraTask;


public class AddNewMemberActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private DataSourceRead dataSourceRead;
    private Spinner spinnerDefaultProgram;
    private Spinner spinnerMemberSex;
    private Spinner spinnerMaritalStatus;
    private Spinner spinnerReligion;
    private Spinner spinnerNationality;
    private Spinner spinnerEthnicity;
    private Spinner spinnerMemberStatus;

    private Spinner spinnerSavingInstallmentType;
    private Spinner spinnerCBSInstallmentType;

    private DataSourceWrite dataSourceWrite;

    private LinearLayout linearLayoutMemberSaved;


    private TextInputEditText textInputEditTextPassbookNumber;
    private TextInputEditText textInputEditTextMemberNickName;
    private TextInputEditText textInputEditTextMemberFullName;
    private TextView textViewFatherOrHusbandName;
    private CheckBox checkboxIsHusband;
    private TextInputEditText textInputEditTextFatherFullName;
    private TextInputEditText textInputEditTextFatherNickName;
    private TextInputEditText textInputEditTextMotherName;
    private TextInputEditText textInputEditTextEducation;
    private TextInputEditText textInputEditTextProfession;
    private TextInputEditText textInputEditTextSpouseFullName;
    private TextInputEditText textInputEditTextSpouseNickName;
    private TextInputEditText textInputEditTextGuardianName;
    private TextInputEditText textInputEditTextGuardianRelation;
    private TextInputEditText textInputEditTextNationalIdNumber;
    private TextInputEditText textInputEditTextBirthCertificateNumber;
    private TextInputEditText textInputEditTextResidenceType;
    private TextInputEditText textInputEditTextLandLordName;
    private TextInputEditText textInputEditTextPermanentUpazila;
    private TextInputEditText textInputEditTextPermanentUnionOrWard;
    private TextInputEditText textInputEditTextPermanentPostOffice;
    private TextInputEditText textInputEditTextPermanentVillage;
    private TextInputEditText textInputEditTextPermanentRoad;
    private TextInputEditText textInputEditTextPermanentHouse;
    private TextInputEditText textInputEditTextPermanentPhone;
    private TextInputEditText textInputEditTextPermanentFixedProperty;
    private TextInputEditText textInputEditTextPermanentIntroducerName;
    private TextInputEditText textInputEditTextPermanentIntroducerDesignation;


    private TextInputEditText textInputEditTextMinimumSavingsDeposit;
    private TextInputEditText textInputEditTextMinimumCBSDeposit;


    private TextInputEditText textInputEditTextPresentUpazila;
    private TextInputEditText textInputEditTextPresentUnionOrWard;
    private TextInputEditText textInputEditTextPresentPostOffice;
    private TextInputEditText textInputEditTextPresentVillage;
    private TextInputEditText textInputEditTextPresentRoad;
    private TextInputEditText textInputEditTextPresentHouse;
    private TextInputEditText textInputEditTextPresentPhone;

    private CheckBox checkboxPresentPermanentSame;
    private LinearLayout linearLayoutPresentAddress;

    private NewMember newMember;

    private int spinnerPosition;


    private int groupId;
    private Spinner spinnerPermanentDistrict;
    private Spinner spinnerPresentAddress;
    private String branchName;

    private DatePickerFragmentForMemberAge datePickerFragmentForMemberAge = new DatePickerFragmentForMemberAge();


    private EditText editTextMemberAge;
    private TextView textViewMemberBirthDay;
    private TextView textViewAdmissionDate;

    private boolean ageBool = false;
    private boolean admissionBool = false;

    private boolean[] errorSignal = new boolean[24];
    private boolean errorSignalMain;
    private boolean checked = false;
    private int programOfficerId;
    private int editORCreate;
    private int memberId;


    private ImageView imageViewFatherNickName;
    private ImageView imageViewSpouseNickName;
    private ImageView imageViewSpouseFullName;


    private ArrayAdapter<String> arrayAdapterSavingsInstallmentType;
    private ArrayAdapter<String> arrayAdapterCBSsInstallmentType;
    private ExtraTask extraTask = new ExtraTask();

    public enum AddOrUpdate {
        Add(1), Update(332);

        int code;

        AddOrUpdate(int code) {
            this.code = code;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_icon);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setLogo(R.drawable.logowith_space);


        getSupportActionBar().setLogo(R.drawable.logowith_space);

        TextView textViewProgramOfficerName = findViewById(R.id.textView_programOfficerName);
        TextView textViewGroupName = findViewById(R.id.textView_groupName);


        imageViewFatherNickName = findViewById(R.id.imageViewFatherNickName);
        imageViewSpouseNickName = findViewById(R.id.imageViewSpouseNickName);
        imageViewSpouseFullName = findViewById(R.id.imageViewSpouseFullName);


        ImageButton imageButtonCalenderAge = findViewById(R.id.imageButtonCalenderAge);
        ImageButton imageButtonCalenderAdmission = findViewById(R.id.imageButtonCalenderAdmission);
        editTextMemberAge = findViewById(R.id.editTextMemberAge);
        textViewMemberBirthDay = findViewById(R.id.textViewMemberBirthDay);
        textViewAdmissionDate = findViewById(R.id.textViewAdmissionDate);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonClose = findViewById(R.id.buttonClose);

        dataSourceRead = new DataSourceRead(getApplicationContext());
        branchName = new DataSourceRead(getApplicationContext()).getBranchName();
        int districtId = dataSourceRead.getDistrictId(branchName);


        programOfficerId = getIntent().getIntExtra("ProgramOfficerId", -1);
        String loginId = getIntent().getStringExtra("loginId");
        String groupName = getIntent().getStringExtra("groupName");
        groupId = getIntent().getIntExtra("groupId", 0);
        editORCreate = getIntent().getIntExtra("update", 0);
        dataSourceWrite = new DataSourceWrite(getApplicationContext());
        spinnerPosition = getIntent().getIntExtra("spinnerPosition", 0);

        if (editORCreate == AddOrUpdate.Update.code) {
            memberId = getIntent().getIntExtra("memberId", -12345);
            newMember = dataSourceRead.getNewMember(memberId);
        }


        if (programOfficerId != -1) {
            textViewProgramOfficerName.setText(dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
        } else {
            textViewProgramOfficerName.setText(R.string.user_admin);
        }

        textViewGroupName.setText(groupName);


        spinnerDefaultProgram = findViewById(R.id.spinnerDefaultProgram);
        spinnerMemberSex = findViewById(R.id.spinnerSex);
        spinnerMaritalStatus = findViewById(R.id.spinnerMaritalStatus);
        spinnerReligion = findViewById(R.id.spinnerReligion);
        spinnerNationality = findViewById(R.id.spinnerNationality);
        spinnerEthnicity = findViewById(R.id.spinnerEthnicity);
        spinnerMemberStatus = findViewById(R.id.spinnerMemberStatus);
        spinnerPermanentDistrict = findViewById(R.id.spinnerPermanentDistrict);
        spinnerPresentAddress = findViewById(R.id.spinnerPresentAddress);


        checkboxIsHusband = findViewById(R.id.checkboxIsHusband);

        linearLayoutMemberSaved = findViewById(R.id.linearLayoutMemberSaved);

        textInputEditTextPassbookNumber = findViewById(R.id.textInputEditTextPassbookNumber);
        textInputEditTextMemberNickName = findViewById(R.id.textInputEditTextMemberNickName);
        textInputEditTextMemberFullName = findViewById(R.id.textInputEditTextMemberFullName);
        textViewFatherOrHusbandName = findViewById(R.id.textViewFatherOrHusbandName);
        textInputEditTextFatherFullName = findViewById(R.id.textInputEditTextFatherFullName);
        textInputEditTextFatherNickName = findViewById(R.id.textInputEditTextFatherNickName);
        textInputEditTextMotherName = findViewById(R.id.textInputEditTextMotherName);
        textInputEditTextEducation = findViewById(R.id.textInputEditTextEducation);
        textInputEditTextProfession = findViewById(R.id.textInputEditTextProfession);
        textInputEditTextSpouseFullName = findViewById(R.id.textInputEditTextSpouseFullName);
        textInputEditTextSpouseNickName = findViewById(R.id.textInputEditTextSpouseNickName);
        textInputEditTextGuardianName = findViewById(R.id.textInputEditTextGuardianName);
        textInputEditTextGuardianRelation = findViewById(R.id.textInputEditTextGuardianRelation);
        textInputEditTextNationalIdNumber = findViewById(R.id.textInputEditTextNationalIdNumber);
        textInputEditTextBirthCertificateNumber = findViewById(R.id.textInputEditTextBirthCertificateNumber);
        textInputEditTextResidenceType = findViewById(R.id.textInputEditTextResidenceType);
        textInputEditTextLandLordName = findViewById(R.id.textInputEditTextLandLordName);
        textInputEditTextPermanentUpazila = findViewById(R.id.textInputEditTextPermanentUpazila);
        textInputEditTextPermanentUnionOrWard = findViewById(R.id.textInputEditTextPermanentUnionOrWard);
        textInputEditTextPermanentPostOffice = findViewById(R.id.textInputEditTextPermanentPostOffice);
        textInputEditTextPermanentVillage = findViewById(R.id.textInputEditTextPermanentVillage);
        textInputEditTextPermanentRoad = findViewById(R.id.textInputEditTextPermanentRoad);
        textInputEditTextPermanentHouse = findViewById(R.id.textInputEditTextPermanentHouse);
        textInputEditTextPermanentPhone = findViewById(R.id.textInputEditTextPermanentPhone);
        textInputEditTextPermanentFixedProperty = findViewById(R.id.textInputEditTextPermanentFixedProperty);
        textInputEditTextPermanentIntroducerName = findViewById(R.id.textInputEditTextPermanentIntroducerName);
        textInputEditTextPermanentIntroducerDesignation = findViewById(R.id.textInputEditTextPermanentIntroducerDesignation);


        checkboxPresentPermanentSame = findViewById(R.id.checkboxPresentPermanentSame);
        linearLayoutPresentAddress = findViewById(R.id.linearLayoutPresentAddress);


        textInputEditTextMinimumSavingsDeposit = findViewById(R.id.textInputEditTextMinimumSavingsDeposit);
        textInputEditTextMinimumCBSDeposit = findViewById(R.id.textInputEditTextMinimumCBSDeposit);


        textInputEditTextPresentUpazila = findViewById(R.id.textInputEditTextPresentUpazila);
        textInputEditTextPresentUnionOrWard = findViewById(R.id.textInputEditTextPresentUnionOrWard);
        textInputEditTextPresentPostOffice = findViewById(R.id.textInputEditTextPresentPostOffice);
        textInputEditTextPresentVillage = findViewById(R.id.textInputEditTextPresentVillage);
        textInputEditTextPresentRoad = findViewById(R.id.textInputEditTextPresentRoad);
        textInputEditTextPresentHouse = findViewById(R.id.textInputEditTextPresentHouse);
        textInputEditTextPresentPhone = findViewById(R.id.textInputEditTextPresentPhone);

        textInputEditTextFatherNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!checkboxIsHusband.isChecked()) {
                    textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString());

                }
            }
        });


        textInputEditTextSpouseNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (checkboxIsHusband.isChecked()) {
                    textViewFatherOrHusbandName.setText(textInputEditTextSpouseNickName.getText().toString());

                }
            }
        });


        checkboxIsHusband.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                textViewFatherOrHusbandName.setText(textInputEditTextSpouseNickName.getText().toString());
                imageViewFatherNickName.setVisibility(View.INVISIBLE);
                imageViewSpouseNickName.setVisibility(View.VISIBLE);
            } else {
                textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString());
                imageViewFatherNickName.setVisibility(View.VISIBLE);
                imageViewSpouseNickName.setVisibility(View.INVISIBLE);
            }
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
        });


        checkboxPresentPermanentSame.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayoutPresentAddress.setVisibility(View.GONE);
                checked = true;

            } else {
                linearLayoutPresentAddress.setVisibility(View.VISIBLE);
                checked = false;
            }
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
        });


        textInputEditTextPassbookNumber.setText(String.valueOf(dataSourceRead.getGroupLastPassbookNumber(groupId) + 1));


        Group groupInfo = dataSourceRead.getGroupInfo(groupId);

        ArrayAdapter<String> adapterDefaultPrograms;


        spinnerCBSInstallmentType = findViewById(R.id.spinnerCBSInstallmentType);
        spinnerSavingInstallmentType = findViewById(R.id.spinnerSavingInstallmentType);

        if (groupInfo.getDefaultProgramId() > 140 && groupInfo.getDefaultProgramId() < 145) {
            adapterDefaultPrograms = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.default_programs_name_msme));
            adapterDefaultPrograms.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);


            arrayAdapterSavingsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_msme));
            arrayAdapterSavingsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

            arrayAdapterCBSsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_msme));
            arrayAdapterCBSsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);


        } else {
            adapterDefaultPrograms = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.default_programs_name));
            adapterDefaultPrograms.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

            arrayAdapterSavingsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_general));
            arrayAdapterSavingsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

            arrayAdapterCBSsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_general));
            arrayAdapterCBSsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);
        }


        ArrayAdapter<String> adapterMemberSex = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.member_sex));
        adapterMemberSex.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.marital_status));
        adapterMaritalStatus.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);


        ArrayAdapter<String> adapterReligion = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.member_religion));
        adapterReligion.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterNationality = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.member_nationality));
        adapterNationality.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterEthnicity = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.member_Ethnicity));
        adapterEthnicity.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterMemberStatus = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.member_status));
        adapterMemberStatus.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

        ArrayAdapter<String> adapterDistrict = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                getResources().getStringArray(R.array.district_name));
        adapterDistrict.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);


        spinnerDefaultProgram.setAdapter(adapterDefaultPrograms);
        if (groupInfo.getDefaultProgramId() > 140 && groupInfo.getDefaultProgramId() < 145) {
            if (groupInfo.getDefaultProgramId() == 141) {
                spinnerDefaultProgram.setSelection(5);
            } else if (groupInfo.getDefaultProgramId() == 142) {
                spinnerDefaultProgram.setSelection(6);
            } else if (groupInfo.getDefaultProgramId() == 143) {
                spinnerDefaultProgram.setSelection(7);
            } else if (groupInfo.getDefaultProgramId() == 144) {
                spinnerDefaultProgram.setSelection(8);
            }

        } else {
            if (groupInfo.getDefaultProgramId() == 129) {
                spinnerDefaultProgram.setSelection(0);
            } else if (groupInfo.getDefaultProgramId() == 130) {
                spinnerDefaultProgram.setSelection(1);
            } else if (groupInfo.getDefaultProgramId() == 131) {
                spinnerDefaultProgram.setSelection(2);
            } else if (groupInfo.getDefaultProgramId() == 137) {
                spinnerDefaultProgram.setSelection(3);
            } else if (groupInfo.getDefaultProgramId() == 138) {
                spinnerDefaultProgram.setSelection(4);
            } else {
                spinnerDefaultProgram.setSelection(0);
            }


        }
        spinnerMemberSex.setAdapter(adapterMemberSex);
        spinnerMaritalStatus.setAdapter(adapterMaritalStatus);
        spinnerReligion.setAdapter(adapterReligion);
        spinnerNationality.setAdapter(adapterNationality);
        spinnerEthnicity.setAdapter(adapterEthnicity);
        spinnerMemberStatus.setAdapter(adapterMemberStatus);
        spinnerPermanentDistrict.setAdapter(adapterDistrict);


        spinnerCBSInstallmentType.setAdapter(arrayAdapterCBSsInstallmentType);
        spinnerSavingInstallmentType.setAdapter(arrayAdapterSavingsInstallmentType);

        spinnerPresentAddress.setAdapter(adapterDistrict);

        spinnerPermanentDistrict.setSelection(districtId - 1);
        spinnerPresentAddress.setSelection(districtId - 1);


        spinnerMemberStatus.setEnabled(false);

        spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position == 0) {
                    conditionApplication(spinnerMemberSex.getSelectedItemPosition(), position);


                } else if (position == 1) {
                    conditionApplication(spinnerMemberSex.getSelectedItemPosition(), position);

                } else if (position == 2) {
                    conditionApplication(spinnerMemberSex.getSelectedItemPosition(), position);
                } else if (position == 3) {
                    conditionApplication(spinnerMemberSex.getSelectedItemPosition(), position);
                } else if (position == 4) {
                    conditionApplication(spinnerMemberSex.getSelectedItemPosition(), position);
                } else if (position == 5) {
                    conditionApplication(spinnerMemberSex.getSelectedItemPosition(), position);
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMemberSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if (position == 0) {
                    conditionApplication(position, spinnerMaritalStatus.getSelectedItemPosition());
                } else if (position == 1) {

                    conditionApplication(position, spinnerMaritalStatus.getSelectedItemPosition());


                } else if (position == 2) {
                    conditionApplication(position, spinnerMaritalStatus.getSelectedItemPosition());
                } else if (position == 3) {
                    conditionApplication(position, spinnerMaritalStatus.getSelectedItemPosition());
                }
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imageButtonCalenderAge.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());

            datePickerFragmentForMemberAge.show(getSupportFragmentManager(), "DateForAge");
            ageBool = true;
        });

        imageButtonCalenderAdmission.setVisibility(View.GONE);


        buttonClose.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
        });


        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
        });


        spinnerDefaultProgram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




                if (editORCreate == AddOrUpdate.Update.code) {
                    installmentTypeSpinnerOperation(position, 1);
                } else {
                    installmentTypeSpinnerOperation(position, 0);
                }

                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonSave.setOnClickListener(v -> {
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());


            for (int i = 0; i < errorSignal.length; i++) {
                errorSignal[i] = false;
            }


            if (textInputEditTextPassbookNumber.getText().toString().trim().equals("0")) {
                textInputEditTextPassbookNumber.setError("Passbook number not valid");

                errorSignal[0] = true;
            } else if (textInputEditTextPassbookNumber.getText().toString().trim().equals("")) {
                textInputEditTextPassbookNumber.setError("Passbook number not found");

                errorSignal[0] = true;
            } else if ((editORCreate == AddOrUpdate.Update.code && newMember.getPassbookNumber() == Integer.parseInt(textInputEditTextPassbookNumber.getText().toString().trim()))) {
                textInputEditTextPassbookNumber.setError(null);
                errorSignal[0] = false;
            } else if (dataSourceRead.getValidationPassbookNumber(Integer.parseInt(textInputEditTextPassbookNumber.getText().toString().trim()), groupId)) {
                textInputEditTextPassbookNumber.setError("Passbook number already exist");
                errorSignal[0] = true;
            } else {
                textInputEditTextPassbookNumber.setError(null);
                errorSignal[0] = false;
            }


            if (textInputEditTextMemberNickName.getText().toString().trim().equals("")) {
                textInputEditTextMemberNickName.setError("Please enter member's nick-name");
                errorSignal[1] = true;

            } else {
                textInputEditTextMemberNickName.setError(null);
                errorSignal[1] = false;
            }


            if (textInputEditTextMemberFullName.getText().toString().trim().equals("")) {
                textInputEditTextMemberFullName.setError("Please enter member's full-name");
                errorSignal[2] = true;
            } else {
                textInputEditTextMemberFullName.setError(null);
                errorSignal[2] = false;
            }


            if (textInputEditTextFatherFullName.getText().toString().trim().equals("")) {
                textInputEditTextFatherFullName.setError("Please enter father's full-name");
                errorSignal[3] = true;
            } else {
                textInputEditTextFatherFullName.setError(null);
                errorSignal[3] = false;
            }

            if (textInputEditTextFatherNickName.getText().toString().trim().equals("") && !checkboxIsHusband.isChecked()) {
                textInputEditTextFatherNickName.setError("Please enter father's nick-name");
                errorSignal[4] = true;
            } else {
                textInputEditTextFatherNickName.setError(null);
                errorSignal[4] = false;
            }


            if (textInputEditTextMotherName.getText().toString().trim().equals("")) {
                textInputEditTextMotherName.setError("Please enter mother's name");
                errorSignal[5] = true;
            } else {
                textInputEditTextMotherName.setError(null);
                errorSignal[5] = false;
            }

            if (textInputEditTextProfession.getText().toString().trim().equals("")) {
                textInputEditTextProfession.setError("Please enter member's profession");
                errorSignal[6] = true;
            } else {
                textInputEditTextProfession.setError(null);
                errorSignal[6] = false;
            }

            if (editTextMemberAge.getText().toString().trim().equals("")) {
                editTextMemberAge.setError("Please enter member age");
                errorSignal[7] = true;
            } else if (Integer.parseInt(editTextMemberAge.getText().toString().trim()) < 18) {
                editTextMemberAge.setError("member age must be greater than 18 years");
                errorSignal[7] = true;
            } else if (Integer.parseInt(editTextMemberAge.getText().toString().trim()) > 65) {
                editTextMemberAge.setError("member age can,t be more than 65 years");
                errorSignal[7] = true;
            } else {
                editTextMemberAge.setError(null);
                errorSignal[7] = false;
            }

            if (textInputEditTextSpouseFullName.getText().toString().trim().equals("") && textInputEditTextSpouseFullName.isEnabled()) {
                textInputEditTextSpouseFullName.setError("Please enter member's spouse full name");
                errorSignal[8] = true;
            } else {
                textInputEditTextSpouseFullName.setError(null);
                errorSignal[8] = false;
            }


            if (textInputEditTextSpouseNickName.getText().toString().trim().equals("") && checkboxIsHusband.isChecked()) {
                textInputEditTextSpouseNickName.setError("Please enter member's spouse nick name");
                errorSignal[9] = true;
            } else {
                textInputEditTextSpouseNickName.setError(null);
                errorSignal[9] = false;
            }


            if (textInputEditTextNationalIdNumber.getText().toString().trim().equals("")) {
                if(textInputEditTextNationalIdNumber.getText().toString().trim().equals("") && textInputEditTextBirthCertificateNumber.getText().toString().trim().equals(""))
                {
                    textInputEditTextNationalIdNumber.setError("Please provide National-Id or Birth-Certificate-Number");
                    errorSignal[10] = true;
                }
                else
                {
                    textInputEditTextNationalIdNumber.setError(null);
                    errorSignal[10] = false;
                }

            } else if (textInputEditTextNationalIdNumber.getText().toString().trim().length() > 0) {
                int length = textInputEditTextNationalIdNumber.getText().toString().trim().length();


                if (length == 10 || length == 13 || length == 17) {
                    boolean nidValid;
                    if (editORCreate == AddOrUpdate.Update.code) {
                        nidValid = dataSourceRead.isNidValidForOldMember(textInputEditTextNationalIdNumber.getText().toString().trim(), memberId);
                    } else {
                        nidValid = dataSourceRead.isNidValid(textInputEditTextNationalIdNumber.getText().toString().trim());
                    }

                    if (nidValid) {
                        textInputEditTextNationalIdNumber.setError(null);
                        errorSignal[10] = false;
                    } else {
                        textInputEditTextNationalIdNumber.setError("This National ID already exists.");
                        errorSignal[10] = true;
                    }

                } else {
                    textInputEditTextNationalIdNumber.setError("\'National-ID\" must be in 10 , 13 or 17 Digit");
                    errorSignal[10] = true;
                }

            }

            if (textInputEditTextBirthCertificateNumber.getText().toString().trim().equals("")) {

                if(textInputEditTextNationalIdNumber.getText().toString().trim().equals("") && textInputEditTextBirthCertificateNumber.getText().toString().trim().equals(""))
                {
                    textInputEditTextBirthCertificateNumber.setError("Please provide National-Id or Birth-Certificate-Number");
                    errorSignal[11] = true;
                }
                else
                {
                    textInputEditTextBirthCertificateNumber.setError(null);
                    errorSignal[11] = false;
                }

            } else if (textInputEditTextBirthCertificateNumber.getText().toString().trim().length() > 0) {
                int length = textInputEditTextBirthCertificateNumber.getText().toString().trim().length();

                if (length == 13 || length == 17) {
                    textInputEditTextBirthCertificateNumber.setError(null);
                    errorSignal[11] = false;
                } else {
                    textInputEditTextBirthCertificateNumber.setError("\"Birth Certificate Number\" must be in 13 or 17 Digit");
                    errorSignal[11] = true;
                }

            }


            if (textInputEditTextPermanentUpazila.getText().toString().trim().equals("")) {
                textInputEditTextPermanentUpazila.setError("Please enter permanent address \"Upazila\"");
                errorSignal[12] = true;
            } else {
                textInputEditTextPermanentUpazila.setError(null);
                errorSignal[12] = false;
            }


            if (textInputEditTextPermanentUnionOrWard.getText().toString().trim().equals("")) {
                textInputEditTextPermanentUnionOrWard.setError("Please enter permanent address \"Union / Ward\"");
                errorSignal[13] = true;
            } else {
                textInputEditTextPermanentUnionOrWard.setError(null);
                errorSignal[13] = false;
            }


            if (textInputEditTextPermanentPostOffice.getText().toString().trim().equals("")) {
                textInputEditTextPermanentPostOffice.setError("Please enter permanent address \"Post Office\"");
                errorSignal[14] = true;
            } else {
                textInputEditTextPermanentPostOffice.setError(null);
                errorSignal[14] = false;
            }


            if (textInputEditTextPermanentVillage.getText().toString().trim().equals("")) {
                textInputEditTextPermanentVillage.setError("Please enter permanent address \"Village\"");
                errorSignal[15] = true;
            } else {
                textInputEditTextPermanentVillage.setError(null);
                errorSignal[15] = false;
            }


            if (textInputEditTextPresentUpazila.getText().toString().trim().equals("") && !checkboxPresentPermanentSame.isChecked()) {
                textInputEditTextPresentUpazila.setError("Please enter present address \"Upazila\"");
                errorSignal[16] = true;
            } else {
                textInputEditTextPresentUpazila.setError(null);
                errorSignal[16] = false;
            }


            if (textInputEditTextPresentUnionOrWard.getText().toString().trim().equals("") && !checkboxPresentPermanentSame.isChecked()) {
                textInputEditTextPresentUnionOrWard.setError("Please enter present address \"Union / Ward\"");
                errorSignal[17] = true;
            } else {
                textInputEditTextPresentUnionOrWard.setError(null);
                errorSignal[17] = false;
            }


            if (textInputEditTextPresentPostOffice.getText().toString().trim().equals("") && !checkboxPresentPermanentSame.isChecked()) {
                textInputEditTextPresentPostOffice.setError("Please enter present address \"Post Office\"");
                errorSignal[18] = true;
            } else {
                textInputEditTextPresentPostOffice.setError(null);
                errorSignal[18] = false;
            }


            if (textInputEditTextPresentVillage.getText().toString().trim().equals("") && !checkboxPresentPermanentSame.isChecked()) {
                textInputEditTextPresentVillage.setError("Please enter present address \"Village\"");
                errorSignal[19] = true;
            } else {
                textInputEditTextPresentVillage.setError(null);
                errorSignal[19] = false;
            }


            if (textInputEditTextMinimumSavingsDeposit.getText().toString().trim().equals("") || Integer.parseInt(textInputEditTextMinimumSavingsDeposit.getText().toString().trim()) == 0) {
                textInputEditTextMinimumSavingsDeposit.setError("Please enter minimum deposit amount");
                errorSignal[20] = true;
            } else {
                textInputEditTextMinimumSavingsDeposit.setError(null);
                errorSignal[20] = false;
            }

            if (editORCreate == AddOrUpdate.Update.code && newMember.getCbsDeposit() > 0 && textInputEditTextMinimumCBSDeposit.getText().toString().trim().equals("")) {
                textInputEditTextMinimumCBSDeposit.setError("Minimum deposit can't be '0' in edit-mode");
                errorSignal[21] = true;
            } else if (editORCreate == AddOrUpdate.Update.code && newMember.getCbsDeposit() > 0 && Integer.parseInt(textInputEditTextMinimumCBSDeposit.getText().toString().trim()) == 0) {
                textInputEditTextMinimumCBSDeposit.setError("Minimum deposit can't be '0' in edit-mode");
                errorSignal[21] = true;
            } else if (editORCreate == AddOrUpdate.Update.code && newMember.getCbsDeposit() == 0 && textInputEditTextMinimumCBSDeposit.getText().toString().trim().equals("")) {
                textInputEditTextMinimumCBSDeposit.setError(null);
                errorSignal[21] = false;
            } else if (editORCreate == AddOrUpdate.Update.code && newMember.getCbsDeposit() == 0 && Integer.parseInt(textInputEditTextMinimumCBSDeposit.getText().toString().trim()) == 0) {
                textInputEditTextMinimumCBSDeposit.setError(null);
                errorSignal[21] = false;
            } else if (editORCreate == AddOrUpdate.Update.code && Integer.parseInt(textInputEditTextMinimumCBSDeposit.getText().toString().trim()) > 4000) {
                textInputEditTextMinimumCBSDeposit.setError("Minimum deposit can't be more than 4000");
                errorSignal[21] = true;
            } else if (editORCreate == AddOrUpdate.Update.code && Integer.parseInt(textInputEditTextMinimumCBSDeposit.getText().toString().trim()) < 4000) {
                textInputEditTextMinimumCBSDeposit.setError(null);
                errorSignal[21] = false;
            }


            if (editORCreate != AddOrUpdate.Update.code && textInputEditTextMinimumCBSDeposit.getText().toString().equals("")) {
                textInputEditTextMinimumCBSDeposit.setError(null);
                errorSignal[21] = false;
            } else if (editORCreate != AddOrUpdate.Update.code && Integer.parseInt(textInputEditTextMinimumCBSDeposit.getText().toString().trim()) > 4000) {
                textInputEditTextMinimumCBSDeposit.setError("Minimum deposit can't be more than 4000");
                errorSignal[21] = true;
            }


            String presentPhone = textInputEditTextPresentPhone.getText().toString().trim();
            if (textInputEditTextPresentPhone.getText().toString().trim().length()==10 &&
                    (presentPhone.startsWith("13")  || presentPhone.startsWith("14") ||
                            presentPhone.startsWith("15")  || presentPhone.startsWith("16")) ||
                    presentPhone.startsWith("17")  || presentPhone.startsWith("18")|| presentPhone.startsWith("19")) {
                textInputEditTextPresentPhone.setError(null);
                errorSignal[22] = false;
            } else if(presentPhone.length()==0) {
                textInputEditTextPresentPhone.setError(null);
                errorSignal[22] = false;
            }
            else
            {
                textInputEditTextPresentPhone.setError("Invalid Phone Number");
                errorSignal[22] = true;
            }

            String permanentPhoneNumber = textInputEditTextPermanentPhone.getText().toString().trim();
            if (textInputEditTextPermanentPhone.getText().toString().trim().length()==10 &&
                    (permanentPhoneNumber.startsWith("13")  || permanentPhoneNumber.startsWith("14") ||
                            permanentPhoneNumber.startsWith("15")  || permanentPhoneNumber.startsWith("16")) ||
                    permanentPhoneNumber.startsWith("17")  || permanentPhoneNumber.startsWith("18")|| permanentPhoneNumber.startsWith("19")) {
                textInputEditTextPermanentPhone.setError(null);
                errorSignal[23] = false;
            } else if(permanentPhoneNumber.length()==0) {
                textInputEditTextPermanentPhone.setError(null);
                errorSignal[23] = false;
            }
            else
            {
                textInputEditTextPermanentPhone.setError("Invalid Phone Number");
                errorSignal[23] = true;
            }


            for (boolean anErrorSignal : errorSignal) {
                if (anErrorSignal) {
                    errorSignalMain = true;
                    break;
                }
                errorSignalMain = false;
            }


            if (!errorSignalMain) {

                newMemberDataInsert();

            }

        });


        editTextMemberAge.setText("18");
        try {
            getBirthday(18);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        editTextMemberAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    if (editTextMemberAge.getText().toString().trim().equals("")) {
                        getBirthday(0);
                    } else {
                        getBirthday(Integer.parseInt(editTextMemberAge.getText().toString().trim()));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        if (editORCreate == AddOrUpdate.Update.code) {

            if (newMember.getProgramId() == 129) {
                spinnerDefaultProgram.setSelection(0);
                installmentTypeSpinnerOperation(0, 1);
            } else if (newMember.getProgramId() == 130) {
                spinnerDefaultProgram.setSelection(1);
                installmentTypeSpinnerOperation(1, 1);
            } else if (newMember.getProgramId() == 131) {
                spinnerDefaultProgram.setSelection(2);
                installmentTypeSpinnerOperation(2, 1);
            } else if (newMember.getProgramId() == 137) {
                spinnerDefaultProgram.setSelection(3);
                installmentTypeSpinnerOperation(3, 1);

            } else if (newMember.getProgramId() == 138) {
                spinnerDefaultProgram.setSelection(4);
                installmentTypeSpinnerOperation(4, 1);
            } else if (newMember.getProgramId() == 141) {
                spinnerDefaultProgram.setSelection(5);
                installmentTypeSpinnerOperation(5, 1);
            } else if (newMember.getProgramId() == 142) {
                spinnerDefaultProgram.setSelection(6);
                installmentTypeSpinnerOperation(6, 1);
            } else if (newMember.getProgramId() == 143) {
                spinnerDefaultProgram.setSelection(7);
                installmentTypeSpinnerOperation(7, 1);
            } else if (newMember.getProgramId() == 144) {
                spinnerDefaultProgram.setSelection(8);
                installmentTypeSpinnerOperation(8, 1);
            }


            if (spinnerDefaultProgram.getSelectedItemPosition() > 4) {
                spinnerCBSInstallmentType.setSelection(0);
                spinnerSavingInstallmentType.setSelection(0);

            } else {
                if (newMember.getCbsInstallmentType() == 2) {
                    spinnerCBSInstallmentType.setSelection(1);
                } else {
                    spinnerCBSInstallmentType.setSelection(0);
                }

                if (newMember.getSavingInstallmentType() == 2) {
                    spinnerSavingInstallmentType.setSelection(1);
                } else {
                    spinnerSavingInstallmentType.setSelection(0);
                }
            }


            textInputEditTextPassbookNumber.setText(String.valueOf(newMember.getPassbookNumber()));
            textInputEditTextMemberNickName.setText(newMember.getMemberNickName());
            textInputEditTextMemberFullName.setText(newMember.getMemberFullName());

            if (newMember.isHusband()) {
                checkboxIsHusband.setChecked(true);
                textViewFatherOrHusbandName.setText(newMember.getSpouseNickName());
            } else {
                checkboxIsHusband.setChecked(false);
                textViewFatherOrHusbandName.setText(newMember.getFatherNickName());
            }
            textInputEditTextFatherFullName.setText(newMember.getFatherFullName());
            textInputEditTextFatherNickName.setText(newMember.getFatherNickName());
            textInputEditTextMotherName.setText(newMember.getMotherName());
            textInputEditTextEducation.setText(newMember.getEducationInfo());
            textInputEditTextProfession.setText(newMember.getProfessionInfo());
            editTextMemberAge.setText(String.valueOf(newMember.getMemberAge()));
            textViewMemberBirthDay.setText(newMember.getDateOfBirth());

            spinnerNationality.setSelection(newMember.getNationality() - 1);


            if (newMember.getSex() == 1) {
                spinnerMemberSex.setSelection(0);
            } else if (newMember.getSex() == 2) {
                spinnerMemberSex.setSelection(1);
            } else if (newMember.getSex() == 4) {
                spinnerMemberSex.setSelection(2);
            } else if (newMember.getSex() == 8) {
                spinnerMemberSex.setSelection(3);
            }


            if (newMember.getReligionInfo() == 1) {
                spinnerReligion.setSelection(0);
            } else if (newMember.getReligionInfo() == 2) {
                spinnerReligion.setSelection(1);
            } else if (newMember.getReligionInfo() == 4) {
                spinnerReligion.setSelection(2);
            } else if (newMember.getReligionInfo() == 8) {
                spinnerReligion.setSelection(3);
            } else {
                spinnerReligion.setSelection(4);
            }

            spinnerEthnicity.setSelection(newMember.getEthnicity() - 1);


            if (newMember.getMaritalStatus() == 1) {
                spinnerMaritalStatus.setSelection(0);
                textInputEditTextSpouseFullName.setEnabled(false);
            } else if (newMember.getMaritalStatus() == 2) {
                spinnerMaritalStatus.setSelection(1);
                textInputEditTextSpouseFullName.setEnabled(true);
            } else if (newMember.getMaritalStatus() == 4) {
                spinnerMaritalStatus.setSelection(2);
                textInputEditTextSpouseFullName.setEnabled(true);
            } else if (newMember.getMaritalStatus() == 8) {
                spinnerMaritalStatus.setSelection(3);
                textInputEditTextSpouseFullName.setEnabled(true);
            } else if (newMember.getMaritalStatus() == 16) {
                spinnerMaritalStatus.setSelection(4);
                textInputEditTextSpouseFullName.setEnabled(true);
            } else if (newMember.getMaritalStatus() == 32) {
                spinnerMaritalStatus.setSelection(5);
                textInputEditTextSpouseFullName.setEnabled(true);
            }

            textViewAdmissionDate.setText(newMember.getAdmissionDate());


            textInputEditTextSpouseFullName.setText(newMember.getSpouseFullName());
            textInputEditTextSpouseNickName.setText(newMember.getSpouseNickName());
            textInputEditTextGuardianName.setText(newMember.getGuardianName());
            textInputEditTextGuardianRelation.setText(newMember.getGuardianRelation());
            textInputEditTextNationalIdNumber.setText(newMember.getNationalID());
            textInputEditTextBirthCertificateNumber.setText(newMember.getBirthCertificateNumber());
            textInputEditTextResidenceType.setText(newMember.getResidenceType());
            textInputEditTextLandLordName.setText(newMember.getLandLordName());

            spinnerPermanentDistrict.setSelection(newMember.getPermanentDistrictId() - 1);
            textInputEditTextPermanentUpazila.setText(newMember.getPermanentUpazila());
            textInputEditTextPermanentUnionOrWard.setText(newMember.getPermanentUnion());
            textInputEditTextPermanentPostOffice.setText(newMember.getPermanentPostOffice());
            textInputEditTextPermanentVillage.setText(newMember.getPermanentVillage());
            textInputEditTextPermanentRoad.setText(newMember.getPermanentRoad());
            textInputEditTextPermanentHouse.setText(newMember.getPermanentHouse());
            textInputEditTextPermanentPhone.setText(newMember.getPermanentPhone());
            textInputEditTextPermanentFixedProperty.setText(newMember.getPermanentFixedProperty());
            textInputEditTextPermanentIntroducerName.setText(newMember.getPermanentIntroducerName());
            textInputEditTextPermanentIntroducerDesignation.setText(newMember.getPermanentIntroducerDesignation());

            textInputEditTextMinimumSavingsDeposit.setText(String.valueOf(Math.round(newMember.getSavingDeposit())));
            textInputEditTextMinimumCBSDeposit.setText(String.valueOf(Math.round(newMember.getCbsDeposit())));

            if (newMember.isPresentPermanentSame()) {
                checkboxPresentPermanentSame.setChecked(true);
                linearLayoutPresentAddress.setVisibility(View.GONE);
                checked = true;
            } else {
                linearLayoutPresentAddress.setVisibility(View.VISIBLE);
                checked = false;
                spinnerPresentAddress.setSelection(newMember.getPresentDistrictId() - 1);
                textInputEditTextPresentUpazila.setText(newMember.getPresentUpazila());
                textInputEditTextPresentUnionOrWard.setText(newMember.getPresentUnion());
                textInputEditTextPresentPostOffice.setText(newMember.getPresentPostOffice());
                textInputEditTextPresentVillage.setText(newMember.getPresentVillage());
                textInputEditTextPresentRoad.setText(newMember.getPresentRoad());
                textInputEditTextPresentHouse.setText(newMember.getPresentHouse());
                textInputEditTextPresentPhone.setText(newMember.getPresentPhone());
            }


        }


        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Assign, getApplicationContext());
        toolbarNavigationClick();
    }

    private void installmentTypeSpinnerOperation(int position, int selection) {

        if (position <= 4) {
            arrayAdapterSavingsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_general));
            arrayAdapterSavingsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

            arrayAdapterCBSsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_general));
            arrayAdapterCBSsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);
        } else {
            arrayAdapterSavingsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_msme));
            arrayAdapterSavingsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);

            arrayAdapterCBSsInstallmentType = new ArrayAdapter<>(this, R.layout.spinner_list_for_add_new_member,
                    getResources().getStringArray(R.array.installment_type_msme));
            arrayAdapterCBSsInstallmentType.setDropDownViewResource(R.layout.spinner_list_for_add_new_member);
        }


        spinnerCBSInstallmentType.setAdapter(arrayAdapterCBSsInstallmentType);
        spinnerSavingInstallmentType.setAdapter(arrayAdapterSavingsInstallmentType);


        if (editORCreate == AddOrUpdate.Update.code) {
            if (position > 4) {
                spinnerCBSInstallmentType.setSelection(0);
                spinnerSavingInstallmentType.setSelection(0);

            } else {
                if (selection == 1) {
                    if (newMember.getCbsInstallmentType() == 2) {
                        spinnerCBSInstallmentType.setSelection(1);
                    } else {
                        spinnerCBSInstallmentType.setSelection(0);
                    }

                    if (newMember.getSavingInstallmentType() == 2) {
                        spinnerSavingInstallmentType.setSelection(1);
                    } else {
                        spinnerSavingInstallmentType.setSelection(0);
                    }

                }
            }
        }


    }

    private void conditionApplication(int memberSexPosition, int memberMarriedStatusPosition) {
        if ((memberSexPosition == 0 || memberSexPosition == 2 || memberSexPosition == 3) && (memberMarriedStatusPosition == 0 || memberMarriedStatusPosition == 2 || memberMarriedStatusPosition == 4 || memberMarriedStatusPosition == 5)) {
            checkboxIsHusband.setEnabled(true);

            textInputEditTextSpouseFullName.setFocusableInTouchMode(true);
            textInputEditTextSpouseFullName.setClickable(true);
            textInputEditTextSpouseFullName.setEnabled(true);

            textInputEditTextSpouseNickName.setFocusableInTouchMode(true);
            textInputEditTextSpouseNickName.setClickable(true);
            textInputEditTextSpouseNickName.setEnabled(true);

            imageViewSpouseFullName.setVisibility(View.VISIBLE);

        } else if ((memberSexPosition == 0 || memberSexPosition == 2 || memberSexPosition == 3) && (memberMarriedStatusPosition == 1 || memberMarriedStatusPosition == 3)) {
            checkboxIsHusband.setEnabled(false);

            textInputEditTextSpouseFullName.setText("");
            textInputEditTextSpouseFullName.setFocusable(false);
            textInputEditTextSpouseFullName.setEnabled(false);

            textInputEditTextSpouseNickName.setText("");
            textInputEditTextSpouseNickName.setFocusable(false);
            textInputEditTextSpouseNickName.setEnabled(false);
            checkboxIsHusband.setChecked(false);
            checkboxIsHusband.setEnabled(false);

            textInputEditTextSpouseNickName.setError(null);
            textInputEditTextSpouseFullName.setError(null);

            textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString().trim());

            imageViewFatherNickName.setVisibility(View.VISIBLE);
            imageViewSpouseNickName.setVisibility(View.INVISIBLE);
            imageViewSpouseFullName.setVisibility(View.INVISIBLE);
            textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString().trim());


        } else if (memberSexPosition == 1 && (memberMarriedStatusPosition == 0 || memberMarriedStatusPosition == 2 || memberMarriedStatusPosition == 4 || memberMarriedStatusPosition == 5)) {
            checkboxIsHusband.setEnabled(false);
            checkboxIsHusband.setChecked(false);

            textInputEditTextSpouseFullName.setFocusableInTouchMode(true);
            textInputEditTextSpouseFullName.setClickable(true);
            textInputEditTextSpouseFullName.setEnabled(true);

            textInputEditTextSpouseNickName.setFocusableInTouchMode(true);
            textInputEditTextSpouseNickName.setClickable(true);
            textInputEditTextSpouseNickName.setError(null);
            textInputEditTextSpouseNickName.setEnabled(true);

            imageViewFatherNickName.setVisibility(View.VISIBLE);
            imageViewSpouseNickName.setVisibility(View.INVISIBLE);
            imageViewSpouseFullName.setVisibility(View.VISIBLE);
            textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString().trim());


        } else if (memberSexPosition == 1 && (memberMarriedStatusPosition == 1 || memberMarriedStatusPosition == 3)) {

            checkboxIsHusband.setEnabled(false);
            checkboxIsHusband.setChecked(false);

            textInputEditTextSpouseFullName.setText("");
            textInputEditTextSpouseFullName.setFocusable(false);
            textInputEditTextSpouseFullName.setEnabled(false);

            textInputEditTextSpouseNickName.setText("");
            textInputEditTextSpouseNickName.setFocusable(false);
            textInputEditTextSpouseNickName.setEnabled(false);


            textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString().trim());

            imageViewFatherNickName.setVisibility(View.VISIBLE);
            imageViewSpouseNickName.setVisibility(View.INVISIBLE);
            imageViewSpouseFullName.setVisibility(View.INVISIBLE);
            textInputEditTextSpouseNickName.setError(null);
            textInputEditTextSpouseFullName.setError(null);
            textViewFatherOrHusbandName.setText(textInputEditTextFatherNickName.getText().toString().trim());


        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, getApplicationContext());
    }


    private void newMemberDataInsert() {
        NewMember newMember = new NewMember();

        newMember.setGroupId(groupId);

        if (spinnerDefaultProgram.getSelectedItemPosition() == 0) {
            newMember.setProgramId(129);

        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 1) {
            newMember.setProgramId(130);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 2) {
            newMember.setProgramId(131);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 3) {
            newMember.setProgramId(137);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 4) {
            newMember.setProgramId(138);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 5) {
            newMember.setProgramId(141);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 6) {
            newMember.setProgramId(142);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 7) {
            newMember.setProgramId(143);
        } else if (spinnerDefaultProgram.getSelectedItemPosition() == 8) {
            newMember.setProgramId(144);
        }

        newMember.setPassbookNumber(Integer.parseInt(textInputEditTextPassbookNumber.getText().toString().trim()));
        newMember.setMemberNickName(textInputEditTextMemberNickName.getText().toString().trim());
        newMember.setSpouseNickName(textInputEditTextSpouseNickName.getText().toString().trim());
        newMember.setFatherNickName(textInputEditTextFatherNickName.getText().toString().trim());

        if (checkboxIsHusband.isChecked()) {
            newMember.setHusband(true);
        } else {
            newMember.setHusband(false);
        }

        newMember.setDateOfBirth(textViewMemberBirthDay.getText().toString().trim());

        newMember.setStatus(spinnerMemberStatus.getSelectedItemPosition() + 1);

        newMember.setAdmissionDate(textViewAdmissionDate.getText().toString().trim());
        newMember.setNationalID(textInputEditTextNationalIdNumber.getText().toString().trim());

        if (spinnerMemberSex.getSelectedItemPosition() == 0) {
            newMember.setSex(1);
        } else if (spinnerMemberSex.getSelectedItemPosition() == 1) {
            newMember.setSex(2);
        } else if (spinnerMemberSex.getSelectedItemPosition() == 2) {
            newMember.setSex(4);
        } else if (spinnerMemberSex.getSelectedItemPosition() == 3) {
            newMember.setSex(8);
        }


        newMember.setMemberFullName(textInputEditTextMemberFullName.getText().toString().trim());
        newMember.setFatherFullName(textInputEditTextFatherFullName.getText().toString().trim());
        newMember.setMotherName(textInputEditTextMotherName.getText().toString().trim());
        newMember.setEducationInfo(textInputEditTextEducation.getText().toString().trim());
        newMember.setProfessionInfo(textInputEditTextProfession.getText().toString().trim());
        newMember.setMemberAge(Integer.parseInt(editTextMemberAge.getText().toString().trim()));
        newMember.setMeetingDayOfweek(dataSourceRead.groupMeetingDayActual(groupId));

        newMember.setNationality(spinnerNationality.getSelectedItemPosition() + 1);


        if (spinnerReligion.getSelectedItemPosition() == 0) {
            newMember.setReligionInfo(1);
        } else if (spinnerReligion.getSelectedItemPosition() == 1) {
            newMember.setReligionInfo(2);
        } else if (spinnerReligion.getSelectedItemPosition() == 2) {
            newMember.setReligionInfo(4);
        } else if (spinnerReligion.getSelectedItemPosition() == 3) {
            newMember.setReligionInfo(8);
        } else {
            newMember.setReligionInfo(16);
        }

        newMember.setEthnicity(spinnerEthnicity.getSelectedItemPosition() + 1);

        if (spinnerMaritalStatus.getSelectedItemPosition() == 0) {
            newMember.setMaritalStatus(1);
        } else if (spinnerMaritalStatus.getSelectedItemPosition() == 1) {
            newMember.setMaritalStatus(2);
        } else if (spinnerMaritalStatus.getSelectedItemPosition() == 2) {
            newMember.setMaritalStatus(4);
        } else if (spinnerMaritalStatus.getSelectedItemPosition() == 3) {
            newMember.setMaritalStatus(8);
        } else if (spinnerMaritalStatus.getSelectedItemPosition() == 4) {
            newMember.setMaritalStatus(16);
        } else if (spinnerMaritalStatus.getSelectedItemPosition() == 5) {
            newMember.setMaritalStatus(32);
        }

        newMember.setSpouseFullName(textInputEditTextSpouseFullName.getText().toString().trim());
        newMember.setGuardianName(textInputEditTextGuardianName.getText().toString().trim());
        newMember.setGuardianRelation(textInputEditTextGuardianRelation.getText().toString().trim());
        newMember.setBirthCertificateNumber(textInputEditTextBirthCertificateNumber.getText().toString().trim());
        newMember.setResidenceType(textInputEditTextResidenceType.getText().toString().trim());
        newMember.setLandLordName(textInputEditTextLandLordName.getText().toString().trim());
        newMember.setPermanentDistrictId(spinnerPermanentDistrict.getSelectedItemPosition() + 1);
        newMember.setPermanentUpazila(textInputEditTextPermanentUpazila.getText().toString().trim());
        newMember.setPermanentUnion(textInputEditTextPermanentUnionOrWard.getText().toString().trim());
        newMember.setPermanentPostOffice(textInputEditTextPermanentPostOffice.getText().toString().trim());
        newMember.setPermanentVillage(textInputEditTextPermanentVillage.getText().toString().trim());
        newMember.setPermanentRoad(textInputEditTextPermanentRoad.getText().toString().trim());
        newMember.setPermanentHouse(textInputEditTextPermanentHouse.getText().toString().trim());
        newMember.setPermanentFixedProperty(textInputEditTextPermanentFixedProperty.getText().toString().trim());
        newMember.setPermanentIntroducerName(textInputEditTextPermanentIntroducerName.getText().toString().trim());
        newMember.setPermanentIntroducerDesignation(textInputEditTextPermanentIntroducerDesignation.getText().toString().trim());
        newMember.setPermanentPhone(textInputEditTextPermanentPhone.getText().toString().trim());


        if (checked) {
            newMember.setPresentDistrictId(spinnerPermanentDistrict.getSelectedItemPosition() + 1);
            newMember.setPresentUpazila(textInputEditTextPermanentUpazila.getText().toString().trim());
            newMember.setPresentUnion(textInputEditTextPermanentUnionOrWard.getText().toString().trim());
            newMember.setPresentPostOffice(textInputEditTextPermanentPostOffice.getText().toString().trim());
            newMember.setPresentVillage(textInputEditTextPermanentVillage.getText().toString().trim());
            newMember.setPresentRoad(textInputEditTextPermanentRoad.getText().toString().trim());
            newMember.setPresentHouse(textInputEditTextPermanentHouse.getText().toString().trim());
            newMember.setPresentPhone(textInputEditTextPermanentPhone.getText().toString().trim());
            newMember.setPresentPermanentSame(checked);
        } else {
            newMember.setPresentDistrictId(spinnerPresentAddress.getSelectedItemPosition() + 1);
            newMember.setPresentUpazila(textInputEditTextPresentUpazila.getText().toString().trim());
            newMember.setPresentUnion(textInputEditTextPresentUnionOrWard.getText().toString().trim());
            newMember.setPresentPostOffice(textInputEditTextPresentPostOffice.getText().toString().trim());
            newMember.setPresentVillage(textInputEditTextPresentVillage.getText().toString().trim());
            newMember.setPresentRoad(textInputEditTextPresentRoad.getText().toString().trim());
            newMember.setPresentHouse(textInputEditTextPresentHouse.getText().toString().trim());
            newMember.setPresentPhone(textInputEditTextPresentPhone.getText().toString().trim());
            newMember.setPresentPermanentSame(checked);
        }


        newMember.setSavingDeposit(Integer.parseInt(textInputEditTextMinimumSavingsDeposit.getText().toString().trim()));
        if (newMember.getProgramId() > 140 && newMember.getProgramId() < 145) {
            newMember.setSavingInstallmentType(2);
        } else {
            newMember.setSavingInstallmentType(spinnerSavingInstallmentType.getSelectedItemPosition() + 1);
        }

        if (textInputEditTextMinimumCBSDeposit.getText().toString().trim().equals("")) {
            newMember.setCbsDeposit(0);
            if (newMember.getProgramId() > 140 && newMember.getProgramId() < 145) {
                newMember.setCbsInstallmentType(2);
            } else {
                newMember.setCbsInstallmentType(spinnerCBSInstallmentType.getSelectedItemPosition() + 1);
            }
        } else {

            newMember.setCbsDeposit(Integer.parseInt(textInputEditTextMinimumCBSDeposit.getText().toString().trim()));

            if (newMember.getProgramId() > 140 && newMember.getProgramId() < 145) {
                newMember.setCbsInstallmentType(2);
            } else {
                newMember.setCbsInstallmentType(spinnerCBSInstallmentType.getSelectedItemPosition() + 1);
            }
        }


        boolean dataSaved;
        String message;

        if (editORCreate != AddOrUpdate.Update.code) {
            dataSaved = dataSourceWrite.insertNewMember(newMember, programOfficerId);
            message = "saved";
        } else {
            dataSaved = dataSourceWrite.updateNewMember(newMember, programOfficerId, memberId);
            message = "updated";
        }


        if (dataSaved) {
            linearLayoutMemberSaved.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    AddNewMemberActivity.this);

            builder.setMessage(
                    "New Member's data is " + message + " successfully")
                    .setCancelable(false)
                    .setTitle("Member Admission")
                    .setPositiveButton("Ok",
                            (dialog, id) -> {
                                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
                                finish();
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


    }


    private void toolbarNavigationClick() {

        toolbar.setNavigationOnClickListener(v -> {

            Intent intent = new Intent();
            intent.putExtra("GroupRefresh", spinnerPosition);
            setResult(RESULT_OK, intent);
            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Stop, getApplicationContext());
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        if (ageBool) {
            try {
                getAge(year, month, dayOfMonth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (admissionBool) {


            if (dayOfMonth < 10 && month < 10) {
                textViewAdmissionDate.setText("0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
            } else if (dayOfMonth < 10) {
                textViewAdmissionDate.setText("0" + dayOfMonth + "/" + (month + 1) + "/" + year);
            } else if (month < 10) {
                textViewAdmissionDate.setText(dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
            } else {
                textViewAdmissionDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
            admissionBool = false;
        }


    }

    @SuppressLint("SetTextI18n")
    private void getBirthday(int ageValue) throws ParseException {


        String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date dt1 = format1.parse(currentDate);
        SimpleDateFormat daySDF = new SimpleDateFormat("dd", Locale.getDefault());
        daySDF.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String daysToday = daySDF.format(dt1);

        SimpleDateFormat monthSDF = new SimpleDateFormat("MM", Locale.getDefault());
        monthSDF.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String monthToday = monthSDF.format(dt1);

        SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy", Locale.getDefault());
        yearSDF.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        int yearToday = Integer.parseInt(yearSDF.format(dt1));


        textViewMemberBirthDay.setText(daysToday + "-" + (monthToday) + "-" + (yearToday - ageValue));


    }


    @SuppressLint("SetTextI18n")
    private void getAge(int year, int month, int day) throws ParseException {


        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();


        String currentDate = new DataSourceOperationsCommon(getApplicationContext()).getFirstRealDate();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        Date dt1 = format1.parse(currentDate);

        SimpleDateFormat daySDF = new SimpleDateFormat("dd", Locale.getDefault());
        daySDF.setTimeZone(TimeZone.getTimeZone("GMT+6"));

        int daysToday = Integer.parseInt(daySDF.format(dt1));

        SimpleDateFormat monthSDF = new SimpleDateFormat("MM", Locale.getDefault());
        monthSDF.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        int monthToday = Integer.parseInt(monthSDF.format(dt1)) - 1;

        SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy", Locale.getDefault());
        yearSDF.setTimeZone(TimeZone.getTimeZone("GMT+6"));

        int yearToday = Integer.parseInt(yearSDF.format(dt1));


        dob.set(year, month, day);
        today.set(yearToday, monthToday, daysToday);

        Date dateDOB = dob.getTime();
        Date dateToday = today.getTime();


        long dayDifference = TimeUnit.DAYS.convert(dateToday.getTime() - dateDOB.getTime(), TimeUnit.MILLISECONDS);

        for (int i = dob.get(Calendar.YEAR); i <= today.get(Calendar.YEAR); i++) {
            if (i > 0 && i % 4 == 0 && (i % 100 == 0 || i % 400 != 0)) {
                dayDifference--;
            }
        }

        int age = (int) (dayDifference / 356);
        String ageS = String.valueOf(age);

        editTextMemberAge.setText(ageS);

        if (day < 10 && month + 1 < 10) {
            textViewMemberBirthDay.setText("0" + day + "-" + "0" + (month + 1) + "-" + year);
        } else if (day < 10) {
            textViewMemberBirthDay.setText("0" + day + "-" + (month + 1) + "-" + year);
        } else if (month + 1 < 10) {
            textViewMemberBirthDay.setText(day + "-" + "0" + (month + 1) + "-" + year);
        } else {
            textViewMemberBirthDay.setText(day + "-" + (month + 1) + "-" + year);
        }


        ageBool = false;
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

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            format1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            Date dt1 = format1.parse(currentDate);

            SimpleDateFormat format2 = new SimpleDateFormat("EE", Locale.getDefault());
            format2.setTimeZone(TimeZone.getTimeZone("GMT+6"));
            String day = format2.format(dt1);
            menu.findItem(R.id.action_working_day).setTitle(currentDate + " " + day);
            menu.findItem(R.id.action_close_current_day).setVisible(false);
            textViewAdmissionDate.setText(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        menu.findItem(R.id.action_location).setTitle(branchName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                    AddNewMemberActivity.this);

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


}
