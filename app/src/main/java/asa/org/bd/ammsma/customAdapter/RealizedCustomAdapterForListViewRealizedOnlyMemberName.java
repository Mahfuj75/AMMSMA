package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.RealizedMemberData;


public class RealizedCustomAdapterForListViewRealizedOnlyMemberName extends ArrayAdapter<RealizedMemberData> {

    private Context context;
    private List<RealizedMemberData> realizedMembersInformation;
    private static final int TOTAL_SINGLE_GROUP = -12345;
    private static final int TOTAL_GRAND_GROUP = -54321;

    public RealizedCustomAdapterForListViewRealizedOnlyMemberName(Context context, List<RealizedMemberData> realizedMembersInformation) {
        super(context, R.layout.realized_only_group_listview, realizedMembersInformation);
        this.realizedMembersInformation = realizedMembersInformation;
        this.context = context;

    }


    private static class ViewHolder {
        TextView textViewMemberName;

    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        final ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.realized_only_member_listview, null);

            viewHolder.textViewMemberName = rowView.findViewById(R.id.textViewMemberName);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final RealizedMemberData realizedMemberData = getItem(position);

        String setText;

        assert realizedMemberData != null;
        if (realizedMemberData.getMemberId() == TOTAL_SINGLE_GROUP) {

            setText = realizedMemberData.getMemberName();
            rowView.setBackgroundColor(Color.parseColor("#B3E5FC"));

            viewHolder.textViewMemberName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        } else if (realizedMemberData.getMemberId() == TOTAL_GRAND_GROUP) {
            rowView.setBackgroundColor(Color.parseColor("#EDE7F6"));
            setText = realizedMemberData.getMemberName();
        } else {

            if (realizedMembersInformation.get(position).isCollected()) {
                rowView.setBackgroundColor(Color.parseColor("#B2DFDB"));

            } else {
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
            viewHolder.textViewMemberName.setTypeface(Typeface.SERIF);
            viewHolder.textViewMemberName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            setText = realizedMemberData.getMemberName() + "/" + realizedMemberData.getFatherName() + " (" + realizedMemberData.getPassbookNumber() + ")";
        }


        viewHolder.textViewMemberName.setTag(position);
        viewHolder.textViewMemberName.setText(setText);
        final View finalRowView = rowView;
        viewHolder.textViewMemberName.setOnClickListener(v -> {

            String text = viewHolder.textViewMemberName.getText().toString().trim();
            SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
            biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
            Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        });

        viewHolder.textViewMemberName.setOnLongClickListener(v -> {

            if (!realizedMembersInformation.get(position).getMemberName().contains("Total")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        v.getRootView().getContext());
                LayoutInflater layoutInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflate != null;
                View convertView1 = layoutInflate.inflate(R.layout.only_member_info, null);
                builder.setView(convertView1);
                builder.setTitle("Member");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                TextView textViewPassBookNumber = convertView1.findViewById(R.id.textViewPassBookNumber);
                TextView textViewName = convertView1.findViewById(R.id.textViewName);
                TextView textViewFatherOrHusband = convertView1.findViewById(R.id.textViewFatherOrHusbandName);
                textViewPassBookNumber.setText(String.valueOf(realizedMemberData.getPassbookNumber()));
                textViewName.setText(titleCase(realizedMemberData.getMemberName()));
                textViewFatherOrHusband.setText(titleCase(realizedMemberData.getFatherName()));

                AlertDialog dialog = builder.create();
                dialog.show();
            }


            return false;
        });


        return rowView;
    }

    private static String titleCase(String givenString) {

        if (givenString.trim().contains(" ")) {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {
                stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
            }
            return stringBuffer.toString().trim();
        } else {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }


}
