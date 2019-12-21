package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import asa.org.bd.ammsma.extra.RealizedGroupData;


public class RealizedCustomAdapterForListViewRealizedAllGroupOnlyGroupName extends ArrayAdapter<RealizedGroupData> {

    private Context context;
    private List<RealizedGroupData> realizedGroupDataArrayList;

    public RealizedCustomAdapterForListViewRealizedAllGroupOnlyGroupName(Context context, List<RealizedGroupData> realizedGroupDataArrayList) {
        super(context, R.layout.realized_only_group_listview, realizedGroupDataArrayList);
        this.realizedGroupDataArrayList = realizedGroupDataArrayList;
        this.context = context;
    }

    private static class ViewHolder {
        TextView textViewGroupName;

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
            rowView = layoutInflater.inflate(R.layout.realized_only_group_listview, null);

            viewHolder.textViewGroupName = rowView.findViewById(R.id.textViewGroupName);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        if (viewHolder.textViewGroupName.getText().toString().trim().equals("")) {
            if (realizedGroupDataArrayList.get(position).getGroupId() == (-12345)) {
                rowView.setBackgroundColor(Color.parseColor("#B3E5FC"));

                viewHolder.textViewGroupName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewGroupName.setText(realizedGroupDataArrayList.get(position).getGroupName());

            } else {
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                viewHolder.textViewGroupName.setText(realizedGroupDataArrayList.get(position).getGroupName() + " (" + titleCase(realizedGroupDataArrayList.get(position).getMeetingDay()) + ")");
            }

        }

        final View finalRowView = rowView;
        viewHolder.textViewGroupName.setOnClickListener(v -> {

            String text = viewHolder.textViewGroupName.getText().toString().trim();
            SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
            biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
            Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });

        viewHolder.textViewGroupName.setOnLongClickListener(v -> {

            if (position < realizedGroupDataArrayList.size() - 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        v.getRootView().getContext());
                LayoutInflater layoutInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflate != null;
                View convertView1 = layoutInflate.inflate(R.layout.only_group_info, null);
                builder.setView(convertView1);
                builder.setTitle("Group");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                TextView textViewGroupName = convertView1.findViewById(R.id.textViewGroupName);
                TextView textViewName = convertView1.findViewById(R.id.textViewDay);
                textViewGroupName.setText(titleCase(realizedGroupDataArrayList.get(position).getGroupName()));
                textViewName.setText(titleCase(realizedGroupDataArrayList.get(position).getMeetingDay()));


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
