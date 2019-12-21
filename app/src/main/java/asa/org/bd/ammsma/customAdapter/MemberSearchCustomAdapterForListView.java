package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.SearchData;


public class MemberSearchCustomAdapterForListView extends ArrayAdapter<SearchData> {

    private Context context;

    public interface DataChangeListener {
        void onDataChange(int position);
    }

    public MemberSearchCustomAdapterForListView(Context context, List<SearchData> searchDataList) {
        super(context, R.layout.member_search_custom_adapter_list_view, searchDataList);
        this.context = context;
    }

    private static class ViewHolder {
        TextView textViewGroupName;
        TextView textViewMemberName;
        TextView textViewAdmissionDate;
        TextView textViewNationalId;
        TextView textViewPhone;


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
            rowView = layoutInflater.inflate(R.layout.member_search_custom_adapter_list_view, null);

            viewHolder.textViewGroupName = rowView.findViewById(R.id.textViewGroupName);
            viewHolder.textViewMemberName = rowView.findViewById(R.id.textViewMemberName);
            viewHolder.textViewAdmissionDate = rowView.findViewById(R.id.textViewAdmissionDate);
            viewHolder.textViewNationalId = rowView.findViewById(R.id.textViewNationalId);
            viewHolder.textViewPhone = rowView.findViewById(R.id.textViewPhone);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        SearchData searchData = getItem(position);

        assert searchData != null;

        viewHolder.textViewGroupName.setText(searchData.getGroupName() + "(" + searchData.getMeetingDay() + ")");
        viewHolder.textViewMemberName.setText(searchData.getMemberName() + "/" + searchData.getFatherOrHusbandName() + "(" + searchData.getPassbookNumber() + ")");
        viewHolder.textViewAdmissionDate.setText(searchData.getAdmissionDate());
        viewHolder.textViewNationalId.setText(searchData.getNationalIdNumber());
        viewHolder.textViewPhone.setText(searchData.getPhoneNumber());


        return rowView;
    }

}
