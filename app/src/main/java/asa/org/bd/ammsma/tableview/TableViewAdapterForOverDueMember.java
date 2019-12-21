
package asa.org.bd.ammsma.tableview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.OverDueMember;
import asa.org.bd.ammsma.tableview.holder.CellViewHolder;
import asa.org.bd.ammsma.tableview.holder.ColumnHeaderViewHolder;
import asa.org.bd.ammsma.tableview.holder.RowHeaderViewHolder;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewAdapterForOverDueMember extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {


    private final LayoutInflater mInflater;
    private List<OverDueMember> overDueMemberList;

    public TableViewAdapterForOverDueMember(Context context,  List<OverDueMember> overDueMemberList) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.overDueMemberList = overDueMemberList;
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
        if(overDueMemberList.get(rowPosition).getGroupId()==-12345)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#B3E5FC"));
        }
        else if(overDueMemberList.get(rowPosition).getGroupId()==-54321)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#73e8ff"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
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
        if(overDueMemberList.get(rowPosition).getGroupId()==-12345)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#B3E5FC"));
        }
        else if(overDueMemberList.get(rowPosition).getGroupId()==-54321)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#73e8ff"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        rowHeaderViewHolder.row_header_textView.setText(String.valueOf(rowHeader.getData()));
    }


    @SuppressLint("InflateParams")
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
