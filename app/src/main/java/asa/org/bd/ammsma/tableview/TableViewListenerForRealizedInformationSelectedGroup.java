package asa.org.bd.ammsma.tableview;

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
import asa.org.bd.ammsma.extra.RealizedMemberData;


public class TableViewListenerForRealizedInformationSelectedGroup implements ITableViewListener {

    private List<RealizedMemberData> realizedMemberInformationList;
    private Context mContext;
    public TableViewListenerForRealizedInformationSelectedGroup(Context mContext, TableView tableView, List<RealizedMemberData> realizedMemberInformationList) {
        tableView.getContext();
        this.realizedMemberInformationList =  realizedMemberInformationList;
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
        // Do what you want.
       /* showToast("Column header  " + column + " has been clicked.");*/

    }


    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int
            column) {

    }


    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        // Do what you want.

        String text;
        if (realizedMemberInformationList.get(row).getGroupId() == (-12345)) {
            text = realizedMemberInformationList.get(row).getGroupName();

        } else {
            text =realizedMemberInformationList.get(row).getMemberName() + "/" + realizedMemberInformationList.get(row).getFatherName() + " (" + realizedMemberInformationList.get(row).getPassbookNumber() + ")";
        }


        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
        Toast toast = Toast.makeText(mContext, biggerText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        // Create Long Press Popup
        /*RowHeaderLongPressPopup popup = new RowHeaderLongPressPopup(rowHeaderView, mTableView);
        // Show
        popup.show();*/
        if (!realizedMemberInformationList.get(row).getMemberName().contains("Total")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    rowHeaderView.itemView.getRootView().getContext());
            LayoutInflater layoutInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflate != null;
            View convertView = layoutInflate.inflate(R.layout.only_member_info, null);
            builder.setView(convertView);
            builder.setTitle("Member");
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            TextView textViewPassBookNumber = convertView.findViewById(R.id.textViewPassBookNumber);
            TextView textViewName = convertView.findViewById(R.id.textViewName);
            TextView textViewFatherOrHusband = convertView.findViewById(R.id.textViewFatherOrHusbandName);
            textViewPassBookNumber.setText(String.valueOf(realizedMemberInformationList.get(row).getPassbookNumber()));
            textViewName.setText(titleCase(realizedMemberInformationList.get(row).getMemberName()));
            textViewFatherOrHusband.setText(titleCase(realizedMemberInformationList.get(row).getFatherName()));

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
}
