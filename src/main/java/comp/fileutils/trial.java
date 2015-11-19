package comp.fileutils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by allenlee on 28/9/15.
 */
public class trial {

    public static void main(String args[]){

//        File a = new File("Ubuntu.vdi");
//        Long size = Long.valueOf(2147483647);
//
//        try {
//            FileInputStream in = new FileInputStream(a);
//            FileChannel fc = in.getChannel();
//            System.out.println(fc.size());
//            size = fc.size()-100000;
//
//            System.out.println(size);
//            fc.position(size);
//            System.out.println("file position: " + fc.position());
//            ByteBuffer h = ByteBuffer.allocateDirect(64);
//            fc.read(h);
//            System.out.println("Reading byte: " + h.asIntBuffer());
//
//
//            System.out.println(Integer.MAX_VALUE);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }catch(IOException e){e.printStackTrace();}


        // This is try the SplitFile;
//        SplitFile split = new SplitFile(new String[]{"file/a.txt", "file/b.txt", "file/c.txt", "file/d.txt"}, "input/related_papers.zip");
//        for(int i =0; i< 4; i++)
//            split.writePacket(i);
//        split.end();

//        SplitFile split = new SplitFile();
//        split.dummyWriting(30000);
        //split.dummyRead(1024, 64);



        BuildFile build = new BuildFile();

        build.trial();



//        File file = new File("file/abb.txt");
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            FileOutputStream a = new FileOutputStream(file);
//            a.write(100);
//
//        } catch (FileNotFoundException e) { e.printStackTrace();}
//        catch(IOException e){e.printStackTrace();}


// This is trial for rounding off;
//        System.out.print("Please enter a float: ");
//        Scanner sc = new Scanner(System.in);
//        String a = sc.nextLine();
//
//        float b = Float.parseFloat(a);
//
//        System.out.println("Input /3 " + (b/3));
//
//        System.out.println("Input /3 *100.0 /100.0 " + Math.round(b*100.0/3)/100.0);

    }

}
