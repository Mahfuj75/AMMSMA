package asa.org.bd.ammsma.customAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import asa.org.bd.ammsma.R;


public class FilePickerListAdapter extends ArrayAdapter<File> {

    private List<File> mObjects;

    public FilePickerListAdapter(Context context, List<File> objects) {

        super(context, R.layout.list_item_file_browser, android.R.id.text1, objects);
        mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View row;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert inflater != null;
            row = inflater.inflate(R.layout.list_item_file_browser, parent, false);
        } else
            row = convertView;

        File object = mObjects.get(position);

        ImageView imageView = row.findViewById(R.id.file_picker_image);
        TextView textView = row.findViewById(R.id.file_picker_text);
        textView.setSingleLine(true);
        textView.setText(object.getName());

        if (object.isFile())
            imageView.setImageResource(R.drawable.ic_insert_drive_file);

        else
            imageView.setImageResource(R.drawable.ic_folder);

        return row;
    }
}
