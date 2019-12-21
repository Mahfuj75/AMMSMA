

package asa.org.bd.ammsma.tableview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;

import java.util.List;

import asa.org.bd.ammsma.extra.MiscellaneousMemberBalance;


public class TableViewListenerForMemberBalance implements ITableViewListener {

    private List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList;

    public TableViewListenerForMemberBalance(TableView tableView, List<MiscellaneousMemberBalance> miscellaneousMemberBalanceList) {
        tableView.getContext();
        this.miscellaneousMemberBalanceList = miscellaneousMemberBalanceList;
    }


    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {


    }


    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, final int column,
                                  int row) {
        // Do What you want
        /*showToast(cellList.get(row).get(column).getData().toString());*/


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

        /*if (columnHeaderView instanceof ColumnHeaderViewHolder) {
            // Create Long Press Popup
            ColumnHeaderLongPressPopup popup = new ColumnHeaderLongPressPopup(
                    (ColumnHeaderViewHolder) columnHeaderView, mTableView);
            // Show
            popup.show();
        }*/
    }


    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        // Do what you want.

        String text;
        if (miscellaneousMemberBalanceList.get(row).getMemberId() != (-12345)) {

            text = miscellaneousMemberBalanceList.get(row).getMemberName();
            SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
            biggerText.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
            Toast toast = Toast.makeText(rowHeaderView.itemView.getContext(), biggerText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

        /*showToast("Row header " + row + " has been clicked.");*/
    }


    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        // Create Long Press Popup
        /*RowHeaderLongPressPopup popup = new RowHeaderLongPressPopup(rowHeaderView, mTableView);
        // Show
        popup.show();*/
    }


/*    private static String titleCase(String givenString) {

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

    }*/


    /*private void showToast(String p_strMessage) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }

        mToast.setText(p_strMessage);
        mToast.show();
    }*/
}
