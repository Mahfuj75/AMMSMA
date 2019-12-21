package asa.org.bd.ammsma.file;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import asa.org.bd.ammsma.R;
import asa.org.bd.ammsma.customAdapter.FilePickerListAdapter;


/**
 * Created by Mahfuj75 on 3/15/2017.
 */

public class FilePicker extends ListActivity {

    public final static String EXTRA_FILE_PATH = "file_path";
    public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
    public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "";
    @SuppressLint("SdCardPath")
    private final static String DEFAULT_INITIAL_DIRECTORY = "/sdcard/AMMS DATA";

    protected File Directory;
    protected ArrayList<File> Files;
    protected FilePickerListAdapter filePickerListAdapter;
    protected boolean ShowHiddenFiles = false;
    protected String[] acceptedFileExtensions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert layoutInflater != null;
        @SuppressLint("InflateParams")
        View emptyView = Objects.requireNonNull(layoutInflater).inflate(R.layout.empty_view, null);
        ((ViewGroup) getListView().getParent()).addView(emptyView);
        getListView().setEmptyView(emptyView);

        Directory = new File(DEFAULT_INITIAL_DIRECTORY);

        Files = new ArrayList<>();

        filePickerListAdapter = new FilePickerListAdapter(getApplicationContext(), Files);
        setListAdapter(filePickerListAdapter);
        acceptedFileExtensions = new String[]{".001"};

        if (getIntent().hasExtra(EXTRA_FILE_PATH))
            Directory = new File(getIntent().getStringExtra(EXTRA_FILE_PATH));

        if (getIntent().hasExtra(EXTRA_SHOW_HIDDEN_FILES))
            ShowHiddenFiles = getIntent().getBooleanExtra(EXTRA_SHOW_HIDDEN_FILES, false);

        if (getIntent().hasExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS)) {

            ArrayList<String> collection =
                    getIntent().getStringArrayListExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS);

            acceptedFileExtensions = collection.toArray(new String[0]);
        }

    }

    @Override
    protected void onResume() {
        refreshFilesList();
        super.onResume();
    }

    protected void refreshFilesList() {

        Files.clear();
        ExtensionFilenameFilter filter =
                new ExtensionFilenameFilter(acceptedFileExtensions);

        File[] files = Directory.listFiles(filter);

        if (files != null && files.length > 0) {

            for (File file : files) {

                if (file.isHidden() && !ShowHiddenFiles) {

                    continue;
                }

                Files.add(file);
            }

            Collections.sort(Files, new FileComparator());
        }

        filePickerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        if (Directory.getParentFile() != null) {

            Directory = Directory.getParentFile();
            refreshFilesList();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File newFile = (File) l.getItemAtPosition(position);

        if (newFile.isFile()) {

            Intent extra = new Intent();
            extra.putExtra(EXTRA_FILE_PATH, newFile.getAbsolutePath());
            setResult(RESULT_OK, extra);
            finish();
        } else {

            Directory = newFile;
            refreshFilesList();
        }

        super.onListItemClick(l, v, position, id);
    }


}
