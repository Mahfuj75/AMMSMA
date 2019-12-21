package asa.org.bd.ammsma.customAdapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.activity.insideLogin.insideHome.insideGroupOperation.AddNewMemberActivity;
import asa.org.bd.ammsma.database.DataSourceOperationsCommon;
import asa.org.bd.ammsma.database.DataSourceRead;
import asa.org.bd.ammsma.database.DataSourceWrite;
import asa.org.bd.ammsma.database.DateAndDataConversion;
import asa.org.bd.ammsma.jsonJavaViceVersa.Member;
import asa.org.bd.ammsma.service.ExtraTask;


public class MemberInformationCustomAdapterForListView extends ArrayAdapter<Member> {

    private Context context;
    private DataChangeListener listener;
    private int defaultProgramId;
    private int programOfficerId;
    private String loginId;
    private String groupName;
    private int groupId;
    private String realGroupName;
    private DataSourceRead dataSourceRead;
    private DataSourceWrite dataSourceWrite;
    private ExtraTask extraTask = new ExtraTask();


    public interface DataChangeListener {
        void onDataChange(int position);

        void onRefresh(int position);
    }

    public MemberInformationCustomAdapterForListView(Context context, List<Member> memberList, int defaultProgramId, int programOfficerId, String loginId, String groupName, int groupId, String realGroupName, DataChangeListener listener) {
        super(context, R.layout.member_details_information, memberList);
        this.listener = listener;
        this.context = context;
        this.defaultProgramId = defaultProgramId;
        this.programOfficerId = programOfficerId;
        this.loginId = loginId;
        this.groupName = groupName;
        this.groupId = groupId;
        this.realGroupName = realGroupName;
        this.dataSourceRead = new DataSourceRead(context);
        this.dataSourceWrite = new DataSourceWrite(context);
    }

    private static class ViewHolder {
        CardView cardViewMemberInfo;
        TextView textViewMemberName;
        TextView textViewFatherOrHusbandNameIdentifier;
        TextView textViewFatherOrHusbandName;
        TextView textViewNidNumber;
        TextView textViewAdmissionDate;
        TextView textViewPhone;
        ImageView imageViewMaleOrFemale;
        LinearLayout linearLayoutReceived;
        TextView textViewReceivedDate;

    }


    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.member_details_information, null);


            viewHolder.cardViewMemberInfo = rowView.findViewById(R.id.cardViewMemberInfo);
            viewHolder.textViewMemberName = rowView.findViewById(R.id.textViewMemberName);
            viewHolder.textViewFatherOrHusbandNameIdentifier = rowView.findViewById(R.id.textViewFatherOrHusbandNameIdentifier);
            viewHolder.textViewFatherOrHusbandName = rowView.findViewById(R.id.textViewFatherOrHusbandName);
            viewHolder.textViewNidNumber = rowView.findViewById(R.id.textViewNidNumber);
            viewHolder.textViewAdmissionDate = rowView.findViewById(R.id.textViewAdmissionDate);
            viewHolder.textViewPhone = rowView.findViewById(R.id.textViewPhone);
            viewHolder.imageViewMaleOrFemale = rowView.findViewById(R.id.imageViewMaleOrFemale);
            viewHolder.linearLayoutReceived = rowView.findViewById(R.id.linearLayoutReceived);
            viewHolder.textViewReceivedDate = rowView.findViewById(R.id.textViewReceivedDate);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final Member member = getItem(position);
        assert member != null;
        viewHolder.textViewMemberName.setText(member.getName() + " (" + member.getPassbookNumber() + ")");
        if (member.getHusband()) {
            viewHolder.textViewFatherOrHusbandNameIdentifier.setText("Husband's Name");
        } else {
            viewHolder.textViewFatherOrHusbandNameIdentifier.setText("Father's Name");
        }
        viewHolder.textViewFatherOrHusbandName.setText(member.getFatherName());
        viewHolder.textViewNidNumber.setText(member.getnIdNum());
        viewHolder.textViewAdmissionDate.setText(member.getAdmissionDate());


        if (member.getPhone() != null && !member.getPhone().equals("") && !member.getPhone().equals("Not Set")) {

            if (member.getStatus() == 1) {
                viewHolder.textViewPhone.setText("+880" + member.getPhone());
            } else {
                viewHolder.textViewPhone.setText(member.getPhone());
            }

        } else {
            viewHolder.textViewPhone.setText("Not Set");
        }

        if (Objects.requireNonNull(getItem(position)).getStatus() == 1) {
            viewHolder.cardViewMemberInfo.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
        } else {
            viewHolder.cardViewMemberInfo.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
        }


        if (member.getSex() == 2) {
            viewHolder.imageViewMaleOrFemale.setImageResource(R.drawable.male);
        } else {
            viewHolder.imageViewMaleOrFemale.setImageResource(R.drawable.female);
        }

        if (member.getReceiveType() < 0) {
            viewHolder.linearLayoutReceived.setVisibility(View.GONE);
        } else {
            viewHolder.linearLayoutReceived.setVisibility(View.VISIBLE);
            viewHolder.textViewReceivedDate.setText(new DateAndDataConversion().getDateFromInt(member.getReceiveDate()));
        }


        final View finalRowView = rowView;
        viewHolder.cardViewMemberInfo.setOnClickListener(v -> {

            extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, context.getApplicationContext());

            if (Objects.requireNonNull(getItem(position)).getStatus() == 1 && Objects.requireNonNull(getItem(position)).getAdmissionDateInteger() == new DataSourceOperationsCommon(context).getWorkingDay()) {

                String[] items = {"Edit", "Call"};


                AlertDialog.Builder builder = new AlertDialog.Builder(finalRowView.getRootView().getContext());
                builder.setTitle("Actions")
                        .setCancelable(false)
                        .setItems(items, (dialog, which) -> {
                            if (which == 1) {
                                if (!Objects.equals(member.getPhone(), "Not Set")) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (Objects.requireNonNull(getItem(position)).getStatus() == 1) {
                                        intent.setData(Uri.parse("tel:" + "+880" + member.getPhone()));
                                    } else {
                                        intent.setData(Uri.parse("tel:" + member.getPhone()));
                                    }
                                    if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Phone-Number not found", Toast.LENGTH_LONG).show();
                                }
                            } else if (which == 0) {
                                if (defaultProgramId == 999) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                            finalRowView.getRootView().getContext());

                                    builder1.setMessage("New-Member can't be added on \"BAD-DEBT\" Group")
                                            .setPositiveButton("Ok",
                                                    (dialog1, id) -> dialog1.dismiss());
                                    AlertDialog alert = builder1.create();
                                    alert.show();
                                } else {
                                    Intent i = new Intent(finalRowView.getRootView().getContext(), AddNewMemberActivity.class);
                                    i.putExtra("ProgramOfficerId", programOfficerId);
                                    i.putExtra("programOfficerName", dataSourceRead.getLOName(programOfficerId) + " - " + loginId);
                                    i.putExtra("loginId", loginId);
                                    i.putExtra("groupName", groupName);
                                    i.putExtra("groupId", groupId);
                                    i.putExtra("realGroupName", realGroupName);
                                    i.putExtra("defaultProgramId", defaultProgramId);
                                    i.putExtra("passbookNumber", member.getPassbookNumber());
                                    i.putExtra("memberId", member.getId());
                                    i.putExtra("update", 332);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                }
                            }
                        }).setNegativeButton(
                        "Cancel", (dialog, which) -> dialog.dismiss()
                );

                AlertDialog alert = builder.create();
                alert.show();
            } else {


                String[] items = {"Update", "Call"};


                final AlertDialog.Builder builder = new AlertDialog.Builder(finalRowView.getRootView().getContext());
                builder.setTitle("Actions");
                builder.setCancelable(false);
                builder.setItems(items, (dialog, which) -> {
                    if (which == 1) {
                        if (!Objects.equals(member.getPhone(), "Not Set")) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Objects.requireNonNull(getItem(position)).getStatus() == 1) {
                                intent.setData(Uri.parse("tel:" + "+880" + member.getPhone()));
                            } else {
                                intent.setData(Uri.parse("tel:" + member.getPhone()));
                            }
                            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Phone-Number not found", Toast.LENGTH_LONG).show();
                        }
                    } else if (which == 0) {
                        if (defaultProgramId == 999) {
                            AlertDialog.Builder builder12 = new AlertDialog.Builder(
                                    finalRowView.getRootView().getContext());

                            builder12.setMessage("New-Member can't be added on \"BAD-DEBT\" Group")
                                    .setPositiveButton("Ok",
                                            (dialog12, id) -> dialog12.dismiss());
                            AlertDialog alert = builder12.create();
                            alert.show();
                        } else {
                            dialog.dismiss();
                            if (Objects.equals(member.getPhone(), "Not Set") || (!Objects.equals(member.getPhone(), "Not Set") && Objects.equals(member.getUpdatePhone(), 1))
                                    || Objects.equals(member.getnIdNum(), "") || (!Objects.equals(member.getnIdNum(), "") && Objects.equals(member.getUpdateNid(), 1))) {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(finalRowView.getRootView().getContext());
                                LayoutInflater inflater = LayoutInflater.from(finalRowView.getContext());
                                @SuppressLint("InflateParams")
                                View convertView1 = inflater.inflate(R.layout.old_member_edit_information, null);
                                alertDialog.setView(convertView1);
                                alertDialog.setCancelable(false);

                                TextView textViewTitle = convertView1.findViewById(R.id.textViewTitle);

                                textViewTitle.setText(member.getName() + " (" + member.getPassbookNumber() + ") ");
                                final EditText editTextNationalId = convertView1.findViewById(R.id.editTextNationalId);
                                final EditText editTextPhoneNumber = convertView1.findViewById(R.id.editTextPhoneNumber);
                                Button buttonCancel = convertView1.findViewById(R.id.buttonCancel);
                                Button buttonSave = convertView1.findViewById(R.id.buttonSave);
                                final LinearLayout linearLayoutNid = convertView1.findViewById(R.id.linearLayoutNid);
                                final LinearLayout linearLayoutPhoneNumber = convertView1.findViewById(R.id.linearLayoutPhoneNumber);


                                final AlertDialog dialogInside = alertDialog.create();
                                dialogInside.show();

                                if (Objects.equals(member.getPhone(), "Not Set") || (!Objects.equals(member.getPhone(), "Not Set") && Objects.equals(member.getUpdatePhone(), 1))) {
                                    linearLayoutPhoneNumber.setVisibility(View.VISIBLE);
                                    if (Objects.equals(member.getUpdatePhone(), 1)) {
                                        editTextPhoneNumber.setText(member.getPhone());
                                        editTextPhoneNumber.setSelection(editTextPhoneNumber.getText().length());
                                    } else {
                                        editTextPhoneNumber.setText("");
                                    }
                                } else {
                                    linearLayoutPhoneNumber.setVisibility(View.GONE);
                                }

                                if (Objects.equals(member.getnIdNum(), "") || (!Objects.equals(member.getnIdNum(), "") && Objects.equals(member.getUpdateNid(), 1))) {
                                    linearLayoutNid.setVisibility(View.VISIBLE);
                                    if (Objects.equals(member.getUpdateNid(), 1)) {
                                        editTextNationalId.setText(member.getnIdNum());
                                        editTextNationalId.setSelection(editTextNationalId.getText().length());
                                    } else {
                                        editTextNationalId.setText("");
                                    }
                                } else {
                                    linearLayoutNid.setVisibility(View.GONE);
                                }


                                buttonSave.setOnClickListener(v1 -> {
                                    boolean updateNid;
                                    boolean updatePhone;
                                    boolean errorNid;
                                    boolean errorPhone = false;


                                    int lengthNid = editTextNationalId.getText().toString().trim().length();
                                    int lengthPhone = editTextPhoneNumber.getText().toString().trim().length();

                                    if (linearLayoutNid.getVisibility() == View.VISIBLE) {
                                        if (lengthNid == 10 || lengthNid == 13 || lengthNid == 17) {
                                            boolean nidValid = dataSourceRead.isNidValidForOldMember(editTextNationalId.getText().toString().trim(), member.getId());
                                            if (nidValid) {
                                                editTextNationalId.setError(null);
                                                errorNid = false;
                                            } else {
                                                editTextNationalId.setError("This National ID already exists.");
                                                errorNid = true;
                                            }

                                        } else {
                                            editTextNationalId.setError("\'National-ID\" must be in 10 , 13 or 17 Digit");
                                            errorNid = true;
                                        }
                                    } else {
                                        errorNid = false;
                                    }

                                    if(linearLayoutPhoneNumber.getVisibility() == View.VISIBLE)
                                    {
                                        if(lengthPhone==0)
                                        {
                                            editTextPhoneNumber.setError(null);
                                            errorPhone = false;
                                        }
                                        else if (lengthPhone == 11) {


                                            if(editTextPhoneNumber.getText().toString().trim().startsWith("013")||
                                                    editTextPhoneNumber.getText().toString().trim().startsWith("014")||
                                                    editTextPhoneNumber.getText().toString().trim().startsWith("015")||
                                                    editTextPhoneNumber.getText().toString().trim().startsWith("016")||
                                                    editTextPhoneNumber.getText().toString().trim().startsWith("017")||
                                                    editTextPhoneNumber.getText().toString().trim().startsWith("018")||
                                                    editTextPhoneNumber.getText().toString().trim().startsWith("019"))
                                            {
                                                editTextPhoneNumber.setError(null);
                                                errorPhone = false;
                                            }
                                            else
                                            {
                                                editTextPhoneNumber.setError("\'Phone-Number\' Invalid");
                                                errorPhone = true;
                                            }


                                        }
                                        else {
                                            editTextPhoneNumber.setError("\'Phone-Number\" must be 11 Digits");
                                            errorPhone = true;
                                        }
                                    }

                                    if (!errorNid && !errorPhone) {
                                        if (!Objects.equals(member.getnIdNum(), editTextNationalId.getText().toString().trim()) || !Objects.equals(member.getPhone(), editTextPhoneNumber.getText().toString().trim())) {
                                            updatePhone = (editTextPhoneNumber.getText().toString().trim().equals("")
                                                    && member.getUpdatePhone() == 1)
                                                    || (!editTextPhoneNumber.getText().toString().trim().equals("")
                                                    && member.getUpdatePhone() == 1)
                                                    || (!editTextPhoneNumber.getText().toString().trim().equals("")
                                                    && member.getUpdatePhone() == 0);

                                            updateNid = editTextNationalId.getText().toString().trim().equals("")
                                                    && member.getUpdateNid() == 1
                                                    || !editTextNationalId.getText().toString().trim().equals("")
                                                    && member.getUpdateNid() == 1
                                                    || !editTextNationalId.getText().toString().trim().equals("")
                                                    && member.getUpdateNid() == 0;

                                            dataSourceWrite.updateOldMemberDataData(member.getId(),
                                                    editTextPhoneNumber.getText().toString().trim(),
                                                    editTextNationalId.getText().toString().trim(),
                                                    updatePhone, updateNid);

                                            listener.onRefresh(position);
                                            dialogInside.dismiss();
                                        } else {
                                            dialogInside.dismiss();
                                        }
                                    }


                                });


                                buttonCancel.setOnClickListener(v1 -> dialogInside.dismiss());
                            } else {
                                AlertDialog.Builder builder12 = new AlertDialog.Builder(
                                        finalRowView.getRootView().getContext());

                                builder12.setTitle(member.getName() + "(" + member.getPassbookNumber() + ")");
                                builder12.setMessage("You can not update member's NID & Phone-Number in this system")
                                        .setPositiveButton("Ok",
                                                (dialog13, id) -> dialog13.dismiss());
                                AlertDialog alert = builder12.create();
                                alert.show();
                            }
                        }
                    }
                });
                builder.setNegativeButton(
                        "Cancel", (dialog, which) -> dialog.dismiss()
                );

                AlertDialog alert = builder.create();
                alert.show();
                /*if(!Objects.equals(member.getPhone(), "Not Set"))
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if(Objects.requireNonNull(getItem(position)).getOldOrNewMember()==1)
                    {
                        intent.setData(Uri.parse("tel:" +"+880".concat(member.getPhone())));
                    }
                    else
                    {
                        intent.setData(Uri.parse("tel:" +member.getPhone()));
                    }
                    if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Phone-Number not found",Toast.LENGTH_LONG).show();
                }*/
            }


        });

        viewHolder.cardViewMemberInfo.setOnLongClickListener(v -> {
            if (Objects.requireNonNull(getItem(position)).getStatus() == 1 && Objects.requireNonNull(getItem(position)).getAdmissionDateInteger() == new DataSourceOperationsCommon(context).getWorkingDay()) {
                extraTask.serviceTimerTaskReset(ExtraTask.TaskType.Reset, context.getApplicationContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        finalRowView.getRootView().getContext());

                builder.setMessage(
                        "Are you sure ?")
                        .setCancelable(false)
                        .setTitle("Delete Member")
                        .setPositiveButton("Yes",
                                (dialog, id) -> {
                                    listener.onDataChange(position);
                                    finalRowView.setVisibility(View.GONE);

                                })
                        .setNegativeButton("No",
                                (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();

            }
            return false;
        });


        return rowView;
    }

}
