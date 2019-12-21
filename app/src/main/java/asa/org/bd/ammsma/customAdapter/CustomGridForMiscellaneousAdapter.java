package asa.org.bd.ammsma.customAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import asa.org.bd.ammsma.R;


public class CustomGridForMiscellaneousAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] web;
    private final int[] imageId;

    public CustomGridForMiscellaneousAdapter(Context context, String[] web, int[] imageId) {
        this.mContext = context;
        this.imageId = imageId;
        this.web = web;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            assert inflater != null;
            grid = inflater.inflate(R.layout.grid_single_miscellaneous, null);
            TextView textView = grid.findViewById(R.id.grid_text);
            ImageView imageView = grid.findViewById(R.id.grid_image);
            grid.findViewById(R.id.card_view_customGrid);
            textView.setText(web[position]);
            imageView.setImageResource(imageId[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }
}