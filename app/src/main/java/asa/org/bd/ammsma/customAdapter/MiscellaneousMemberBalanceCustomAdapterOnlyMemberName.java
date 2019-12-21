package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import asa.org.bd.ammsma.extra.MiscellaneousMemberBalance;


public class MiscellaneousMemberBalanceCustomAdapterOnlyMemberName extends ArrayAdapter<MiscellaneousMemberBalance> {

    private Context context;
    private List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList;


    public MiscellaneousMemberBalanceCustomAdapterOnlyMemberName(Context context, List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList) {
        super(context, R.layout.realized_only_member_listview, miscellaneousMemberBalanceList);
        this.miscellaneousMemberBalanceList = miscellaneousMemberBalanceList;
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

        if (viewHolder.textViewMemberName.getText().toString().trim().equals("")) {
            if (miscellaneousMemberBalanceList.get(position).getMemberId() == (-12345)) {
                rowView.setBackgroundColor(Color.parseColor("#B3E5FC"));

                viewHolder.textViewMemberName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewHolder.textViewMemberName.setText(miscellaneousMemberBalanceList.get(position).getMemberName());

            } else {
                rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                viewHolder.textViewMemberName.setText(miscellaneousMemberBalanceList.get(position).getMemberName());
            }

            final View finalRowView = rowView;
            viewHolder.textViewMemberName.setOnClickListener(v -> {

                String text = viewHolder.textViewMemberName.getText().toString().trim();
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
                Toast toast = Toast.makeText(finalRowView.getContext(), biggerText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });

        }


        return rowView;
    }

   /* private static String titleCase(String givenString) {

        if(givenString.trim().contains(" "))
        {
            String[] split = givenString.split(" ");
            StringBuilder stringBuffer = new StringBuilder();

            for (String aSplit : split) {
                stringBuffer.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1).toLowerCase()).append(" ");
            }
            return stringBuffer.toString().trim();
        }
        else
        {
            return (givenString.substring(0, 1).toUpperCase() + givenString.substring(1).toLowerCase()).trim();
        }

    }*/

}
