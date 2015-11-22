package comp.fileutils;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by allenlee on 30/9/15.
 */
public class BuildFile {

    private FileObject [] infiles;
    private FileObject outfile;


    public BuildFile(String outpath, String [] inpath){
        setupOutFile(outpath);
        setupInFile(inpath);
    }

    // Empty Constructor
    public BuildFile(){}

    public void trial(){
        String tmpOutPath = "build/copy.zip";
        String [] tmpInPath =  new String [] { "file/a.txt", "file/b.txt", "file/c.txt", "file/d.txt" };
        BuildFile tryToBuild = new BuildFile(tmpOutPath, tmpInPath);

        tryToBuild.setupOutFile(tmpOutPath);
        tryToBuild.setupInFile(tmpInPath);

        if(tryToBuild.openOutStream()){

            int count =0;

            while(count < tmpInPath.length && tryToBuild.openSingleInputStream(count) ){

                System.out.println("Starting to combine the file " + count);
                tryToBuild.writePacket(tryToBuild.getInfiles()[count], tryToBuild.getOutfile());
                System.out.println("End to combine the file " + count);
                count++;
            }


        }
        tryToBuild.end();
    }

    public void setupOutFile(String Path){
        outfile = new FileObject();
        outfile.setPath(Path);
        outfile.setFile(new File(outfile.getPath()));

    }

    public boolean openOutStream(){
        boolean status = false;

        try {
            outfile.setOut(new FileOutputStream(outfile.getFile()));
            status = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot open the output file");
            System.exit(1);
        }

        return status;

    }


    public void setupInFile(String [] inpath){
        this.infiles = new FileObject[inpath.length];

        for(int i=0; i< inpath.length; i++){
            this.infiles[i] = new FileObject();
            infiles[i].setPath(inpath[i]);
            infiles[i].setFile(new File(inpath[i]));

        }

    }



    public boolean openSingleInputStream(int i){

        boolean status = false;

        try {


            infiles[i].setIn(new FileInputStream(infiles[i].getFile()));
            status = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot read from file " + infiles[i].getPath());
            System.exit(1);
        }

        return status;
    }

    public void writePacket(FileObject inObj, FileObject outObj){

        FileChannel fin = inObj.getIn().getChannel();
        FileChannel fout = outObj.getOut().getChannel();
        byte [] data = new byte[2];


        try {
            fout.position(fout.size()); // Move to the back of the

            while(fin.position() < fin.size()){

                inObj.getIn().read(data);
                outObj.getOut().write(data);

            }

            //fin.close();
            //fout.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void end() {

        for (int i = 0; i < infiles.length; i++) {
            if (infiles[i].getIn() != null) {


                try {
                    infiles[i].getIn().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        if(outfile.getOut() != null){

            try {
                outfile.getOut().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public FileObject[] getInfiles() {
        return infiles;
    }

    public FileObject getOutfile() {
        return outfile;
    }

    public void setInfiles(FileObject[] infiles) {
        this.infiles = infiles;
    }

    public void setOutfile(FileObject outfile) {
        this.outfile = outfile;
    }

}
