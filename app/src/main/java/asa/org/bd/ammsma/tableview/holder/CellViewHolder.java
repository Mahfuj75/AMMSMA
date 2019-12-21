

package asa.org.bd.ammsma.tableview.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.tableview.model.Cell;

public class CellViewHolder extends AbstractViewHolder {

    private final TextView cellTextView;
    private final LinearLayout cell_container;

    public CellViewHolder(View itemView) {
        super(itemView);
        cellTextView =  itemView.findViewById(R.id.cell_data);
        cell_container =  itemView.findViewById(R.id.cell_container);
    }

    public void setCell(Cell cell) {
        cellTextView.setText(String.valueOf(cell.getData()));
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cellTextView.requestLayout();
    }
}