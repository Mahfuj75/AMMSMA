package asa.org.bd.ammsma.tableview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.RealizedGroupData;


public class TableViewListenerForRealizedInformationAll implements ITableViewListener {

    private List<RealizedGroupData> realizedGroupsInformation;
    private Context mContext;
    public TableViewListenerForRealizedInformationAll(Context mContext, TableView tableView, List<RealizedGroupData> realizedGroupsInformation) {
        tableView.getContext();
        this.realizedGroupsInformation =  realizedGroupsInformation;
        this.mContext = mContext;
    }




    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {



    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, final int column,
                                  int row) {

    }


    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {

    }


    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {

    }


    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        String text;
        if (realizedGroupsInformation.get(row).getGroupId() == (-12345)) {
            text = realizedGroupsInformation.get(row).getGroupName();

        } else {
            text = realizedGroupsInformation.get(row).getGroupName() + " (" + titleCase(realizedGroupsInformation.get(row).getMeetingDay()) + ")";
        }


        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
        Toast toast = Toast.makeText(mContext, biggerText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {


        if (row < realizedGroupsInformation.size() - 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    rowHeaderView.itemView.getRootView().getContext());
            LayoutInflater layoutInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflate != null;
            @SuppressLint("InflateParams")
            View convertView = layoutInflate.inflate(R.layout.only_group_info, null);
            builder.setView(convertView);
            builder.setTitle("Group");
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            TextView textViewGroupName = convertView.findViewById(R.id.textViewGroupName);
            TextView textViewName = convertView.findViewById(R.id.textViewDay);
            textViewGroupName.setText(titleCase(realizedGroupsInformation.get(row).getGroupName()));
            textViewName.setText(titleCase(realizedGroupsInformation.get(row).getMeetingDay()));


            AlertDialog dialog = builder.create();
            dialog.show();
        }
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


    /*private void showToast(String p_strMessage) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }

        mToast.setText(p_strMessage);
        mToast.show();
    }*/
}
