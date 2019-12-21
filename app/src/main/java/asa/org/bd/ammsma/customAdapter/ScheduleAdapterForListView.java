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


public class ScheduleAdapterForListView extends ArrayAdapter<String> {

    private Context context;
    private List<String> dateString;
    private List<Float> principal;
    private List<Float> service;
    private List<Float> installmentAmount;

    public ScheduleAdapterForListView(Context context, List<String> dateString, List<Float> principal, List<Float> service, List<Float> installmentAmount) {
        super(context, R.layout.list_view_sechedle_date_item, dateString);
        this.dateString = dateString;
        this.principal = principal;
        this.service = service;
        this.installmentAmount = installmentAmount;
        this.context = context;
    }

    private static class ViewHolder {

        TextView textViewNumber;
        TextView textViewDate;
        TextView textViewPrincipal;
        TextView textViewService;
        TextView textViewInstallmentAmount;


    }


    @SuppressLint({"InflateParams", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            rowView = layoutInflater.inflate(R.layout.list_view_sechedle_date_item, null);

            viewHolder.textViewNumber = rowView.findViewById(R.id.textViewNumber);
            viewHolder.textViewDate = rowView.findViewById(R.id.textViewDate);
            viewHolder.textViewPrincipal = rowView.findViewById(R.id.textViewPrincipal);
            viewHolder.textViewService = rowView.findViewById(R.id.textViewService);
            viewHolder.textViewInstallmentAmount = rowView.findViewById(R.id.textViewInstallmentAmount);

            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }


        viewHolder.textViewNumber.setText(String.valueOf(position + 1));
        viewHolder.textViewDate.setText(dateString.get(position));
        viewHolder.textViewService.setText(String.format("%.2f", (float) (service.get(position))));
        viewHolder.textViewPrincipal.setText(String.format("%.2f", (float) (principal.get(position))));
        viewHolder.textViewInstallmentAmount.setText(String.format("%.2f", (float) (installmentAmount.get(position))));

        if(service.get(position).intValue()==0&& principal.get(position).intValue() ==0 && installmentAmount.get(position).intValue() ==0 && service.get(dateString.size()-1).intValue()==0&& principal.get(dateString.size()-1).intValue() ==0 && installmentAmount.get(dateString.size()-1).intValue() ==0 && dateString.size()>70)
        {
            rowView.setVisibility(View.GONE);
        }
        else {
            rowView.setVisibility(View.VISIBLE);
        }


        return rowView;
    }
}
