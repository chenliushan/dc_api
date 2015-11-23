package comp.services;

import comp.fileutils.BuildFile;
import comp.fileutils.SplitFile;
import comp.utils.CommonUtil;
import comp.utils.KPClass.KPULCLass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by liushanchen on 15/11/21.
 */
public class FileService {
    private static Log log = LogFactory.getLog(FileService.class);

    private SinaAuthorization sinaAuthorization;
    private OneDriveAuthorization oneDriveAuthorization;
    private KPAuthorization kpAuthorization;

    public FileService(OneDriveAuthorization oneDriveAuthorization, SinaAuthorization sinaAuthorization, KPAuthorization kpAuthorization) {
        this.oneDriveAuthorization = oneDriveAuthorization;
        this.sinaAuthorization = sinaAuthorization;
    }


    //    String filePath="/Users/liushanchen/Desktop/test/mytest.txt";
    public boolean uploadFile(String path, String filePath) {
        String fileName = CommonUtil.getFileName(filePath);
        log.info("fileName"+fileName);
        String suffix = CommonUtil.getSuffix(fileName);
//        String pathWithoutName = CommonUtil.getFilePath(filePath);
        String pathWithoutName = CommonUtil.UPLOAD_PATH;
        String fileNameOD = pathWithoutName + CommonUtil.getName(fileName) + "OD" + suffix;
        String fileNameSN = pathWithoutName + CommonUtil.getName(fileName) + "SN" + suffix;
        String fileNameKP = pathWithoutName + CommonUtil.getName(fileName) + "KP" + suffix;
         /*
        分割文件
         */

        String[] names = new String[]{fileNameOD, fileNameSN, fileNameKP};
        SplitFile split = new SplitFile(names, filePath);
        for (int i = 0; i < names.length; i++)
            split.writePacket(i);
        split.end();

        /*
        存储文件名
         */
        try {
            System.out.println("fileName:" + CommonUtil.getName(fileName));
            CommonUtil.creatTxtFile();
            CommonUtil.writeTxtFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        上传文件
         */

        if (oneDriveAuthorization != null && sinaAuthorization != null ) {
            String odResult = oneDriveAuthorization.putContent(path, fileNameOD);
            String snResult = sinaAuthorization.uploadFilePut(path, fileNameSN);
            KPULCLass KPUL = new KPULCLass(CommonUtil.getName(fileName) + "KP" + suffix);
            String kpResult =null;
            if(KPUL!=null) kpResult ="";
            if (odResult != null && snResult != null && kpResult != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static String[] filesBeenUpload() {
        /*
        取得文件名
         */
        String[] names = null;
        String fileNames = null;
        try {
            fileNames = CommonUtil.readTxtFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //解析字符串，获取真正的文件名
        if (fileNames != null) {
            names = fileNames.split("\n");
        }
        return names;
    }

    public boolean downloadFile(String fileName) {
        if (!fileName.startsWith("/")) fileName = "/" + fileName;
        String name = CommonUtil.getName(fileName);
        String suffix = CommonUtil.getSuffix(fileName);
        String fileNameOD = name + "OD" + suffix;
        String fileNameSN = name + "SN" + suffix;
        String fileNameKP = name + "KP" + suffix;
        /*
        下载文件
         */
        if (oneDriveAuthorization != null && sinaAuthorization != null ) {
            String odResult = oneDriveAuthorization.downloadFile(fileNameOD);
            boolean snResult = sinaAuthorization.downloadFile(fileNameSN);
            String kpResult = KPAuthorization.downloadFile(fileNameKP);
            if (odResult != null && snResult && kpResult != null) {
             /*
        合并文件
         */
//                String tmpOutPath = "build/copy.zip";
                String tmpOutPath = CommonUtil.DOWNLOAD_FILE + fileName;
//                String [] tmpInPath =  new String [] { "file/a.txt", "file/b.txt", "file/c.txt", "file/d.txt" };
                String pathOD = CommonUtil.DOWNLOAD_PATH + fileNameOD;
                String pathSN = CommonUtil.DOWNLOAD_PATH + fileNameSN;
                String pathKP = CommonUtil.DOWNLOAD_PATH + fileNameKP;

                String[] tmpInPath = new String[]{pathOD,pathSN,pathKP};
                BuildFile tryToBuild = new BuildFile(tmpOutPath, tmpInPath);
                tryToBuild.setupOutFile(tmpOutPath);
                tryToBuild.setupInFile(tmpInPath);
                if (tryToBuild.openOutStream()) {
                    int count = 0;
                    while (count < tmpInPath.length && tryToBuild.openSingleInputStream(count)) {
                        System.out.println("Starting to combine the file " + count);
                        tryToBuild.writePacket(tryToBuild.getInfiles()[count], tryToBuild.getOutfile());
                        System.out.println("End to combine the file " + count);
                        count++;
                    }
                }
                tryToBuild.end();

            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

}

