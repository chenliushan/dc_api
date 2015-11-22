package comp.services;

import comp.utils.CommonUtil;

import java.io.IOException;

/**
 * Created by liushanchen on 15/11/21.
 */
public class FileService {
    private SinaAuthorization sinaAuthorization;
    private OneDriveAuthorization oneDriveAuthorization;
    private KPAuthorization kpAuthorization;

    public FileService(OneDriveAuthorization oneDriveAuthorization, SinaAuthorization sinaAuthorization, KPAuthorization kpAuthorization) {
        this.kpAuthorization = kpAuthorization;
        this.oneDriveAuthorization = oneDriveAuthorization;
        this.sinaAuthorization = sinaAuthorization;
    }


    //    String filePath="/Users/liushanchen/Desktop/test/mytest.txt";
    public boolean uploadFile(String path, String filePath) {
        String fileName = CommonUtil.getFileName(filePath);
        String suffix = CommonUtil.getSuffix(fileName);
        String pathWithoutName = CommonUtil.getFilePath(filePath);
         /*
        分割文件
         */


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
        String fileNameOD = pathWithoutName + CommonUtil.getName(fileName) + "OD" + suffix;
        String fileNameSN = pathWithoutName + CommonUtil.getName(fileName) + "SN" + suffix;
        String fileNameKP = pathWithoutName + CommonUtil.getName(fileName) + "KP" + suffix;
        if (oneDriveAuthorization != null && sinaAuthorization != null && kpAuthorization != null) {
            String odResult = oneDriveAuthorization.putContent(path, fileNameOD);
            String snResult = sinaAuthorization.uploadFilePut(path, fileNameSN);
            String kpResult = "";
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
        String name = CommonUtil.getFileName(fileName);
        String suffix = CommonUtil.getSuffix(fileName);
        String fileNameOD = name + "OD" + suffix;
        String fileNameSN = name + "SN" + suffix;
        String fileNameKP = name + "KP" + suffix;
        /*
        下载文件
         */
        if (oneDriveAuthorization != null && sinaAuthorization != null && kpAuthorization != null) {
            String odResult = oneDriveAuthorization.downloadFile(fileNameOD);
            String snResult = sinaAuthorization.downloadFile(fileNameSN);
            String kpResult = "";
            if (odResult != null && snResult != null && kpResult != null) {
             /*
        合并文件
         */

            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

}

