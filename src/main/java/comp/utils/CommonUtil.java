package comp.utils;

import java.io.*;

/**
 * Created by liushanchen on 15/9/29.
 */
public class CommonUtil {
    public static String UPLOAD_PATH="/Users/liushanchen/Desktop/test/splitUpload";
    public static String DOWNLOAD_PATH="/Users/liushanchen/Desktop/test/splitDownload";
    public static String DOWNLOAD_FILE="/Users/liushanchen/Desktop/test/download";
    public static String PATH="/Users/liushanchen/Desktop/test/";

    public static String getFileName(String filePath) {
        if(filePath==null||filePath.equals(""))return null;
        String filename = null;
        int filenameIndex = filePath.lastIndexOf("/");
        if (filenameIndex > -1) {
            filename = filePath.substring(filenameIndex, filePath.length());
        }
        return filename;
    }
    public static String getFilePath(String filePath) {
        if(filePath==null||filePath.equals(""))return null;
        String filename = null;
        int filenameIndex = filePath.lastIndexOf("/");
        if (filenameIndex > -1) {
            filename = filePath.substring(0,filenameIndex);
        }
        return filename;
    }
    public static String getName(String fileName) {
        String name = null;
        if(fileName==null||fileName.equals(""))return null;
        int filenameIndex = fileName.lastIndexOf(".");
        if (filenameIndex > -1) {
            name = fileName.substring(0,filenameIndex);
        }
        return name;
    }
    public static String getSuffix(String fileName) {
        if(fileName==null||fileName.equals(""))return null;
        String name = null;
        int filenameIndex = fileName.lastIndexOf(".");
        if (filenameIndex > -1) {
            name = fileName.substring(filenameIndex,fileName.length());
        }
        return name;
    }

    private static String path = "/Users/liushanchen/Desktop/test/uploadfiles.txt";

    /**
     * 创建文件
     *
     * @throws IOException
     */
    public static boolean creatTxtFile() throws IOException {
        boolean flag = false;
        String filenameTemp= path ;
        File filename = new File(filenameTemp);
        if (!filename.exists()) {
            filename.createNewFile();
            System.out.println("created");
            flag = true;
        }
        return flag;
    }









    /**
     * 写文件
     * @param newStr 新内容
     * @throws IOException
     */
    public static boolean writeTxtFile(String newStr)
            throws IOException {
        // 先读取原有文件内容，然后进行写入操作
        boolean flag = false;
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {// 文件路径
            File file = new File(path );
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该文件原有的内容
            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
            // System.getProperty("line.separator")
            // 行与行之间的分隔符 相当于“\n”
                buf = buf.append(System.getProperty("line.separator"));
            }

            buf.append(filein);
            System.out.println("read");

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            System.out.println("write");
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }




    /**
     * 读文件
     * @throws IOException
     */
    public static String readTxtFile()
            throws IOException {
        // 先读取原有文件内容，然后进行写入操作
//        String filein =  "\r\n";
        String temp = "";
        StringBuffer buf = new StringBuffer();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {// 文件路径
            File file = new File(path );
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            // 保存该文件原有的内容
            for (int j = 1; (temp = br.readLine()) != null; j++) {
                buf = buf.append(temp);
                // System.getProperty("line.separator")
                // 行与行之间的分隔符 相当于“\n”
                buf = buf.append(System.getProperty("line.separator"));
            }
//            buf.append(filein);
            System.out.println("read");
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return buf.toString();
    }

    /**
     * 修改文件
     * @param newStr 要删除的内容
     * @throws IOException
     */
    public static boolean updateTxtFile(String newStr)
            throws IOException {
        // 先读取原有文件内容，然后进行写入操作
        boolean flag = false;
        String filein = newStr + "\r\n";
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {// 文件路径
            File file = new File(path );
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 修改该文件原有的内容
            for (int j = 1; (temp = br.readLine()) != null; j++) {
                if(!temp.equals(newStr)){
                    buf = buf.append(temp);
                    // System.getProperty("line.separator")
                    // 行与行之间的分隔符 相当于“\n”
                    buf = buf.append(System.getProperty("line.separator"));
                }
            }
            System.out.println("update");

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            System.out.println("write");
            flag = true;
        } catch (IOException e1) {
            throw e1;
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }
}
