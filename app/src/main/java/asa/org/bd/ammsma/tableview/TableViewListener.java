
package asa.org.bd.ammsma.tableview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import java.util.List;
import asa.org.bd.ammsma.tableview.model.Cell;


public class TableViewListener implements ITableViewListener {

    public TableViewListener(TableView tableView, List<List<Cell>> cellList) {
        tableView.getContext();
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

        // Do what you want.
        /*showToast(cellList.get(row).get(column).getData().toString());*/


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


        /*showToast("Row header " + row + " has been clicked.");*/
    }


    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

        // Create Long Press Popup
        /*RowHeaderLongPressPopup popup = new RowHeaderLongPressPopup(rowHeaderView, mTableView);
        // Show
        popup.show();*/
    }


    /*private void showToast(String p_strMessage) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }

        mToast.setText(p_strMessage);
        mToast.show();
    }*/
}
