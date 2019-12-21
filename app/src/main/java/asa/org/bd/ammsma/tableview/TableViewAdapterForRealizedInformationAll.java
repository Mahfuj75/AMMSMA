
package asa.org.bd.ammsma.tableview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.extra.RealizedGroupData;
import asa.org.bd.ammsma.tableview.holder.CellViewHolder;
import asa.org.bd.ammsma.tableview.holder.ColumnHeaderViewHolder;
import asa.org.bd.ammsma.tableview.holder.RowHeaderViewHolder;
import asa.org.bd.ammsma.tableview.model.Cell;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;
import asa.org.bd.ammsma.tableview.model.RowHeader;


public class TableViewAdapterForRealizedInformationAll extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {


    private static final String LOG_TAG = TableViewAdapterForRealizedInformationAll.class.getSimpleName();

    private final LayoutInflater mInflater;
    private static final int TOTAL_SINGLE_GROUP = -12345;

    private List<RealizedGroupData> realizedGroupDataArrayList;

    public TableViewAdapterForRealizedInformationAll(Context context, List<RealizedGroupData> realizedGroupDataArrayList) {
        super(context);
        this.mInflater = LayoutInflater.from(mContext);
        this.realizedGroupDataArrayList = realizedGroupDataArrayList;

    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        Log.e(LOG_TAG, " onCreateCellViewHolder has been called");
        View layout;
        layout = mInflater.inflate(R.layout.table_view_cell_layout, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        Cell cell = (Cell) cellItemModel;

        CellViewHolder viewHolder = (CellViewHolder) holder;

        if (realizedGroupDataArrayList.get(rowPosition).getGroupId() == TOTAL_SINGLE_GROUP) {
            holder.itemView.setBackgroundColor(Color.parseColor("#B3E5FC"));

        } else {
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
        // Get Row Header xml Layout
        View layout = mInflater.inflate(R.layout.table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel,
                                          int rowPosition) {
        RowHeader rowHeader = (RowHeader) rowHeaderItemModel;
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        if (realizedGroupDataArrayList.get(rowPosition).getGroupId() == TOTAL_SINGLE_GROUP) {
            holder.itemView.setBackgroundColor(Color.parseColor("#B3E5FC"));

        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        rowHeaderViewHolder.row_header_textView.setText(String.valueOf(rowHeader.getData()));
    }


    @Override
    public View onCreateCornerView() {
        View view = mInflater.inflate(R.layout.table_view_corner_layout, null);
        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText("Group Name");
        return view;
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
