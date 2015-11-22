package comp.services;

import comp.utils.CommonUtil;

import java.io.IOException;

/**
 * Created by liushanchen on 15/11/21.
 */
public class TestText {
    public static void main(String args[]){
//        String filePath="/Users/liushanchen/Desktop/test/mytest1.txt";
//        String fileName = CommonUtil.getFileName(filePath);
//        /*
//        存储文件名
//         */
//        try {
//            System.out.println("fileName:"+CommonUtil.getName(fileName));
//            CommonUtil.creatTxtFile();
//            CommonUtil.writeTxtFile(fileName );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        /*
        =================================================================
         */
          /*
        取得文件名
         */
        String[] names=FileService.filesBeenUpload();
        for(int i=0;i<names.length;i++){
            System.out.println("names["+i+"]:"+names[i]);
        }
    }
}
