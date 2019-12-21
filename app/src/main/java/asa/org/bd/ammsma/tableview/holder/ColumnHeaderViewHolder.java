

package asa.org.bd.ammsma.tableview.holder;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.sort.SortState;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.tableview.model.ColumnHeader;

public class ColumnHeaderViewHolder extends AbstractSorterViewHolder {

    private static final String LOG_TAG = ColumnHeaderViewHolder.class.getSimpleName();

    private final LinearLayout column_header_container;
    private final TextView column_header_textView;
    private final ImageButton column_header_sortButton;
    public final ITableView tableView;

    private final Drawable arrow_up, arrow_down;

    public ColumnHeaderViewHolder(View itemView, final ITableView tableView) {
        super(itemView);
        this.tableView = tableView;
        column_header_textView =  itemView.findViewById(R.id.column_header_textView);
        column_header_container =  itemView.findViewById(R.id
                .column_header_container);
        column_header_sortButton =  itemView.findViewById(R.id
                .column_header_sortButton);

        // initialize drawables
        arrow_up = ContextCompat.getDrawable(itemView.getContext(), R.drawable.app_icon);
        arrow_down = ContextCompat.getDrawable(itemView.getContext(), R.drawable.app_icon);

        // Set click listener to the sort button
        // Default one
        View.OnClickListener mSortButtonClickListener = view -> {
            if (getSortState() == SortState.ASCENDING) {
                tableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
            } else if (getSortState() == SortState.DESCENDING) {
                tableView.sortColumn(getAdapterPosition(), SortState.ASCENDING);
            } else {
                // Default one
                tableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
            }

        };
        column_header_sortButton.setOnClickListener(mSortButtonClickListener);
    }

    public void setColumnHeader(ColumnHeader columnHeader) {
        column_header_textView.setText(String.valueOf(columnHeader.getData()));
        column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        column_header_textView.requestLayout();
    }

    @Override
    public void onSortingStatusChanged(SortState sortState) {
        Log.e(LOG_TAG, " + onSortingStatusChanged : x:  " + getAdapterPosition() + " old state "
                + getSortState() + " current state : " + sortState + " visiblity: " +
                column_header_sortButton.getVisibility());

        super.onSortingStatusChanged(sortState);

        // It is necessary to remeasure itself.
        column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;

        controlSortState(sortState);

        Log.e(LOG_TAG, " - onSortingStatusChanged : x:  " + getAdapterPosition() + " old state "
                + getSortState() + " current state : " + sortState + " visiblity: " +
                column_header_sortButton.getVisibility());

        column_header_textView.requestLayout();
        column_header_sortButton.requestLayout();
        column_header_container.requestLayout();
        itemView.requestLayout();
    }

    private void controlSortState(SortState sortState) {
        if (sortState == SortState.ASCENDING) {
            column_header_sortButton.setVisibility(View.VISIBLE);
            column_header_sortButton.setImageDrawable(arrow_down);

        } else if (sortState == SortState.DESCENDING) {
            column_header_sortButton.setVisibility(View.VISIBLE);
            column_header_sortButton.setImageDrawable(arrow_up);
        } else {
            column_header_sortButton.setVisibility(View.INVISIBLE);
        }
    }
}
