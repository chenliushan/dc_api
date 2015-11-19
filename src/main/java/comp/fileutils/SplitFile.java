package comp.fileutils;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by allenlee on 27/9/15.
 */
public class SplitFile {


    private int splitNum = 1;                          // record how many packet divided into;
    private Long packetsize;                           // size of each packet
    private int piecesize = 2;                         // Control each time read how many bytes;
    private String inputPath;                          // The input file path
    private FileObject[] files;                        // The fileObject array of the output file
    private FileObject infile;                         // The fileObject of the input file

    // Constructor which input the String array output path and input path
    public SplitFile(String[] outpath, String inputPath){
        setupOutFile(outpath);setupInFile(inputPath);
    }

    // Empty Constructor
    public SplitFile(){};



    // The method to setup the input output files and the file input output stream


    public void setupInFile(String inpath){

        this.inputPath = inpath;
        infile = new FileObject();
        infile.setPath(inpath);
        infile.setFile(new File(this.getInputPath()));


        // Setting the input file and get the inputfile stream
        try {
            infile.setIn(new FileInputStream(new File(infile.getPath())));
            infile.setOut(null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Cannot connect to input file");
            System.exit(1);
        }

        setPacketSize();

    }

    public void setupOutFile(String[] outpath){

        this.splitNum = outpath.length;
        this.files = new FileObject[splitNum];


        // Setting up the output files and get the outputfile stream
        for(int i =0; i< splitNum; i++)
            files[i] = new FileObject();

        if(setOutPath(outpath)){
            UpdateOutputStream();
        }else{
            System.out.println("Cannot create output file");
            System.exit(1);
        }

    }


    // Writing a packet

    public boolean writePacket(int index){       // Where index is the sequence of the packet going to write and return
                                                    // the packet completed writing or not.
        Long position;
        Long times;
        Long count;
        Long packetSze;
        byte [] data = new byte[this.piecesize];
        boolean status = false;
        FileChannel fc;
        count = Long.valueOf(0);

        fc = infile.getIn().getChannel();



        try {

            times = (Long) (this.packetsize/this.piecesize);
            position = (Long) (this.packetsize*index);
            System.out.println("index: " +index+"Position: " +position + "/size: " +fc.size());
            if(position+(2*this.packetsize) > fc.size())
                times += (int)((fc.size()-position-this.packetsize)%this.piecesize);
            fc.position(position);


            while(count < times && fc.position() < fc.size()){

                infile.getIn().read(data);
                System.out.println(fc.position());
                files[index].getOut().write(data);

//                if(fc.position() + this.piecesize < fc.size()) {
//
//                    byte [] tmp = new byte[this.piecesize];
//                    data = tmp;
//                }

                count++;
            }

            status = true;
            System.out.println(times);


        } catch (IOException e) {
            e.printStackTrace();
        }

//        position = index * this.packetsize;
//
//        if((index+1)*this.packetsize < infile.getFile().length())
//
//        if((this.packetsize)%(piecesize*8) != 0 )
//            System.out.println("Have problem for the packetsize not divided by 8");
//        FileChannel fc = infile.getIn().getChannel();
//
//        try {
//            fc.position(position);                  // Setting up the start position of the input file
//            BufferedInputStream buf_in = new BufferedInputStream(infile.getIn());
//
//
//            for(long i=position; i<((index+1)*this.packetsize% + position); i++){
//                    buf_in.read(data);
//                    files[index].getOut().write(data);
//            }


        //fc.close();
        //buf_in.close();

        //status = true;


        return status;

    }

    // Start splitting the Input files
    public void start(){

    }
    public void end(){
        for(int i=0; i<splitNum; i++){
            try {
                files[i].getOut().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            infile.getIn().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void dummyWriting(long size){
        byte buf[] = new byte[2];
        File dummy = new File("dummy.txt");
        int cycle ;
        int tmp =0;

        try {
            FileOutputStream out = new FileOutputStream(dummy);
            cycle = (int)size/3;
            for(long i=0; i<size; i++){
                tmp = (int) (i%2);
                buf[tmp] = (byte)( (int)(i/cycle) + (byte)'1');
                if(tmp == 1){
                    out.write(buf);
                    out.flush();
                }
            }

            out.write((byte)'E');
            out.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void dummyRead(long position, long byteRead){

        File dummy = new File("dummy.txt");
        File dummyTransfer = new File("transfer.txt");
        byte [] data = new byte[16];

        try {
            FileInputStream in = new FileInputStream(dummy);
            in.getChannel().position(position);
            BufferedInputStream buf_in = new BufferedInputStream(in);

            FileOutputStream out = new FileOutputStream(dummyTransfer);


            for(long i=position; i< (((byteRead/16)-byteRead%16)+position); i++){
                buf_in.read(data);
                out.write(data);

//                if(byteRead < 100) {
//                    System.out.print("File char content: ");
//                    for (int j = 0; j < 16; j++)
//                        System.out.println();
//                }
            }

            buf_in.close();
            in.close();
            out.close();

        } catch (FileNotFoundException e) {e.printStackTrace();}
          catch(IOException e){e.printStackTrace();}
    }


    // setting up the single output path and assign that with a fileObject
    public boolean setSingleOutPath(String path, int index){
        if(files.length > index){

            if(path != null && path != ""){
                files[index].setPath(path);
                files[index].setFile(new File(path));
//                files[index].setSize(files[index].getFile().length());
            }else{
                System.out.println("Path Name Invalid at path " + index);
                return false;
            }

            return true;

        }else{
            return false;
        }
    }


    // setting up all fileObject by using the setSingleOutPath
    public boolean setOutPath(String []path){

        if(path.length == files.length) {
            for (int i = 0; i < path.length; i++)
                if (!setSingleOutPath(path[i], i))
                    return false;
        }

        return true;
    }



    // Setting up the output file stream, need to passing which fileObject referring to;
    public void setSingleOutputStream(int index){
            if( index < files.length){          // make sure the input index is within the fileObject Range

                try {
                    files[index].setOut(new FileOutputStream(files[index].getFile()));  //Assign the FileOutputStream;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                System.out.println("Index greater than the number of file created while file splitting!!");
            }
    }


    // Using above setSingleOutputStream to update all OutputStream
    public void UpdateOutputStream(){
        for(int i=0; i< files.length; i++){
            if(files[i].getPath() != null && files[i].getPath() != "")
                setSingleOutputStream(i);
        }
    }


    // calculate the packetsize;
    public void setPacketSize(){
        Long tmpsize;
        if(infile.getFile() != null){                                   // Make sure the the input file already be set
            tmpsize = (Long) (infile.getFile().length()/splitNum);      // Equally divided the file into few parts
            tmpsize = tmpsize - (tmpsize%piecesize);                            // Assume each part can divided by unit of byte
            this.packetsize = tmpsize;                                  // return the result to packetsize;
        }else{
            System.out.println("The input file not setup properly");
        }

    }

    public int getSplitNum() {
        return splitNum;
    }

    public void setSplitNum(int splitNum) {
        this.splitNum = splitNum;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public FileObject[] getFiles() {
        return files;
    }

    public void setFiles(FileObject[] files) {
        this.files = files;
    }

    public void setFiles(FileObject file , int index){

        if(index < this.files.length) {

            this.files[index] = file;

        }
    }
}
