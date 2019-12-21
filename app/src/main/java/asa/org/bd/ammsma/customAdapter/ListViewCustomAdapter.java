package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.MemberListInfo;


public class ListViewCustomAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private List<String> membersName;
    private List<String> genderList;
    private List<Boolean> realizableOrNot;
    private List<Boolean> paidOrNot;
    private List<Boolean> termOverDue;
    private List<Boolean> newOrNot;
    private List<Boolean> hasLoanOrNot;

    public ListViewCustomAdapter(Context context, MemberListInfo memberListInfo) {
        this.context = context;
        this.membersName = memberListInfo.getMembersName();
        this.genderList = memberListInfo.getMembersSex();
        this.realizableOrNot = memberListInfo.getMembersRealizedInfo();
        this.paidOrNot = memberListInfo.getMembersPaidOrNot();
        this.termOverDue = memberListInfo.getMembersTermOverDue();
        this.newOrNot = memberListInfo.getMemberNewOrOld();
        this.hasLoanOrNot = memberListInfo.getMemberHasLoanOrNot();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return membersName.size();
    }

    @Override
    public Object getItem(int position) {
        return this.membersName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.list_item, null);
        }

        TextView textViewMemberInformation = vi.findViewById(R.id.textViewName);
        ImageView memberIcon = vi.findViewById(R.id.imageViewImage);


        textViewMemberInformation.setTag(position);
        textViewMemberInformation.setText(membersName.get(position));

        if (genderList.get((Integer) textViewMemberInformation.getTag()).equals("Female")) {
            memberIcon.setBackgroundResource(R.drawable.female);
        } else {
            memberIcon.setBackgroundResource(R.drawable.male);
        }

        if (newOrNot.get((Integer) textViewMemberInformation.getTag())) {
            textViewMemberInformation.setTextColor(Color.parseColor("#311B92"));
        } else if (!hasLoanOrNot.get((Integer) textViewMemberInformation.getTag())) {
            textViewMemberInformation.setTextColor(Color.parseColor("#8E24AA"));
        } else if (termOverDue.get((Integer) textViewMemberInformation.getTag()) && !realizableOrNot.get((Integer) textViewMemberInformation.getTag())) {
            textViewMemberInformation.setTextColor(Color.parseColor("#FFB300"));
        } else if (realizableOrNot.get((Integer) textViewMemberInformation.getTag())) {
            textViewMemberInformation.setTextColor(Color.parseColor("#B71C1C"));
        } else {
            textViewMemberInformation.setTextColor(Color.parseColor("#212121"));
        }


        if (paidOrNot.get((Integer) textViewMemberInformation.getTag())) {
            vi.setBackgroundColor(Color.parseColor("#00C853"));
        } else {
            vi.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }


        return vi;
    }
}
