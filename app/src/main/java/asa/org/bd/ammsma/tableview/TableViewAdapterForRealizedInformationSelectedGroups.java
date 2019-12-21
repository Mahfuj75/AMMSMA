
package asa.org.bd.ammsma.tableview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.RealizedMemberData;
import asa.org.bd.ammsma.tableview.holder.CellViewHolder;
import asa.org.bd.ammsma.tableview.holder.ColumnHeaderViewHolder;
import asa.org.bd.ammsma.tableview.holder.RowHeaderViewHolder;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewAdapterForRealizedInformationSelectedGroups extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {


    private final LayoutInflater mInflater;
    private static final int TOTAL_SINGLE_GROUP = -12345;
    private static final int TOTAL_GRAND_GROUP = -54321;
    private List<RealizedMemberData> realizedMemberInformationList;
    private DataChangeListener listener;
    private int positionX;
    private int positionY;

    public TableViewAdapterForRealizedInformationSelectedGroups(Context context, List<RealizedMemberData> realizedMemberInformationList, DataChangeListener listener) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.realizedMemberInformationList = realizedMemberInformationList;
        this.listener = listener;
    }

    public interface DataChangeListener {
        void onDataChange(int positionX,int positionY);
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout;
        layout = mInflater.inflate(R.layout.table_view_cell_layout, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        Cell cell = (Cell) cellItemModel;

        CellViewHolder viewHolder = (CellViewHolder) holder;


        if (realizedMemberInformationList.get(rowPosition).getMemberId() == TOTAL_SINGLE_GROUP) {

            holder.itemView.setBackgroundColor(Color.parseColor("#B3E5FC"));
        } else if (realizedMemberInformationList.get(rowPosition).getMemberId() == TOTAL_GRAND_GROUP) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EDE7F6"));
        } else {

            if (realizedMemberInformationList.get(rowPosition).isCollected()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#B2DFDB"));

            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
        }
        positionX= rowPosition;
        listener.onDataChange(positionX,positionY);
        viewHolder.setCell(cell);
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.table_view_column_header_layout, parent, false);
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object
            columnHeaderItemModel, int columnPosition) {
        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeader);
    }


    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.table_view_row_header_layout, parent, false);
        return new RowHeaderViewHolder(layout);
    }



    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel,
                                          int rowPosition) {
        RowHeader rowHeader = (RowHeader) rowHeaderItemModel;
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        if (realizedMemberInformationList.get(rowPosition).getMemberId() == TOTAL_SINGLE_GROUP) {

            holder.itemView.setBackgroundColor(Color.parseColor("#B3E5FC"));
        } else if (realizedMemberInformationList.get(rowPosition).getMemberId() == TOTAL_GRAND_GROUP) {
            holder.itemView.setBackgroundColor(Color.parseColor("#EDE7F6"));
        } else {

            if (realizedMemberInformationList.get(rowPosition).isCollected()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#B2DFDB"));

            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

            }
        }
        rowHeaderViewHolder.row_header_textView.setText(String.valueOf(rowHeader.getData()));
        positionY = rowPosition;
        listener.onDataChange(positionX,positionY);
    }


    @Override
    public View onCreateCornerView() {
        return mInflater.inflate(R.layout.table_view_corner_layout, null);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int column) {
        return 0;
    }
}
