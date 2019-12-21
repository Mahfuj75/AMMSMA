
package asa.org.bd.ammsma.tableview.holder;

import android.view.View;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import asa.org.bd.ammsma.R;


public class RowHeaderViewHolder extends AbstractViewHolder {
    public final TextView row_header_textView;

    public RowHeaderViewHolder(View itemView) {
        super(itemView);
        row_header_textView = itemView.findViewById(R.id.row_header_textview);
    }
}