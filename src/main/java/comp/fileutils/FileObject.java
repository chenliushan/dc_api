package comp.fileutils;

import java.io.*;
import java.util.Scanner;

/**
 * Created by allenlee on 27/9/15.
 */
public class FileObject {
    private String path;
    private File file;
    private FileOutputStream out;
    private FileInputStream in;


    public FileObject(){}

    public void setOut(FileOutputStream out) {
        this.out = out;
    }

    public FileInputStream getIn() {
        return in;
    }

    public void setIn(FileInputStream in) {
        this.in = in;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileOutputStream getOut() {
        return out;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
