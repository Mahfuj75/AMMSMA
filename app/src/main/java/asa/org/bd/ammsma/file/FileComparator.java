package asa.org.bd.ammsma.file;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Mahfuj75 on 3/15/2017.
 */

class FileComparator implements Comparator<File> {

    public int compare(File file1, File file2) {

        if (file1 == file2)
            return 0;

        if (file1.isDirectory() && file2.isFile())
            return -1;

        if (file1.isFile() && file2.isDirectory())
            return 1;

        return file1.getName().compareToIgnoreCase(file2.getName());
    }
}