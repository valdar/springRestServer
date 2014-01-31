package it.andrea.tarocchi.restServer;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public abstract class AbstractRestServerTest {
    protected void deleteDirectory(String file) {
        deleteDirectory(new File(file));
    }
    
    protected void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteDirectory(files[i]);
            }
        }
        file.delete();
    }
    
    protected void deleteAllMatchingfilesIndir(String directory, String wildcardFileMatcher){
		File dir = new File(directory);
		FileFilter fileFilter = new WildcardFileFilter(wildcardFileMatcher);
		File[] files = dir.listFiles(fileFilter);
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i].delete());
		}
    }
}
