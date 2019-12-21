package asa.org.bd.ammsma.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Mahfuj75 on 3/15/2017.
 */

class ExtensionFilenameFilter implements FilenameFilter {

    private String[] Extensions;

    ExtensionFilenameFilter(String[] extensions) {

        super();
        Extensions = extensions;
    }

    public boolean accept(File dir, String filename) {

        if (new File(dir, filename).isDirectory()) {
            return true;
        }

        if (Extensions != null && Extensions.length > 0) {

            for (String Extension : Extensions) {

                if (filename.endsWith(Extension)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}