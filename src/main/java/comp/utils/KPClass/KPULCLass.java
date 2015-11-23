package comp.utils.KPClass;

import comp.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by allenlee on 21/11/2015.
 */


public class KPULCLass {

    private static Log log = LogFactory.getLog(KPULCLass.class);
    public final static int BUFFER_SIZE = 4048;
    public InputStream input;

    // Constructor:: create a file and open a FileInputStream and read the file into a buffer.
    public KPULCLass(String path){
        String fileName = path;

        KPURLGen kpURLGen = new KPURLGen();
        String requestURL = null;

        if(!path.startsWith("/"))path="/"+path;
            //path= CommonUtil.UPLOAD_PATH+path;
            path = CommonString.LOCAL_KPUPLOAD_PATH + path;
        log.info("The Upload local path: " + path);
        File file = new File(path);
        try {

            input = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getBaseUploadHost();            // Get the base url by sending the request
        try {
            requestURL = kpURLGen.getULURL("/" + fileName, true);          // Generate upload request URL
            log.info("Request Upload URL: " + requestURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startUpload(requestURL, input, fileName);
    }


    public String getBaseUploadHost(){

        KPUtil kpUtils = new KPUtil();
        String json = kpUtils.doGet(KuaipanCommonString.KP_UPLOAD_URL);
        log.info("getBaseUploadHost json: " + json);
        kpUtils.json2OUploadInfo(json);
        log.info("KPString.KP_UPLOAD_HOST: " + KuaipanCommonString.KP_UPLOAD_HOST);

        return KuaipanCommonString.KP_UPLOAD_ROOTPATH;
    }

    private void multipartUploadData(HttpURLConnection con, InputStream datastream, String fileName) {
        log.info("Starting of MultipartUploadData");
        con.setDoOutput(true);                      // If need to send the body, then need to set it be true
        String boundary = "--------------------------"
                + System.currentTimeMillis();
        con.setRequestProperty("connection", "keep-alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + boundary);
        con.setRequestProperty("Cache-Control", "no-cache");
        boundary = "--" + boundary;
        StringBuffer sb = new StringBuffer();
        sb.append(boundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                + fileName + "\"\r\n");
        sb.append("Content-Type: application/octet-stream\r\n\r\n");
        String endStr = "\r\n"
                + boundary
                + "\r\nContent-Disposition: form-data; name=\"Upload\"\r\n\r\nSubmit Query\r\n"
                + boundary + "--\r\n";
        byte[] endData = endStr.getBytes();

        System.out.println(sb.toString());
        OutputStream os = null;

        try {
            con.connect();
            os = con.getOutputStream();
            os.write(sb.toString().getBytes());
            bufferedWriting(os, datastream);
            os.write(endData);

        } catch (IOException e5) {
            e5.printStackTrace();
        } finally {
            try {

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (IOException e) {
            }

        }
    }

    private void bufferedWriting(OutputStream to_write, InputStream to_read) {
        int len = 0;
        int count = 0;
        byte[] buf = new byte[BUFFER_SIZE];


        try {
            while ((len = to_read.read(buf)) != -1) {
                to_write.write(buf, 0, len);
                count += len;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("File Size output: " + count);
    }


    public KPULhttpresponse startUpload(String url, InputStream datastream, String fileName){

        KPULhttpresponse resp = new KPULhttpresponse();
        HttpURLConnection con = getConnectionFromUrl(url , "POST");

        log.info("startUpload fileName: " + fileName);

        multipartUploadData(con, datastream, fileName);

        resp.code = getHttpResponseCode(con);
        resp.content = getContentFromConnection(con);
        resp.url = url;

        log.info("code, content, url" + resp.code + ", " + resp.content + ", " + resp.url );

        if (con != null)
            con.disconnect();

        return resp;
    }

    public HttpURLConnection getConnectionFromUrl(String baseurl, String method){
        URL url = null;
        HttpURLConnection con = null;

        try {
            url = new URL(baseurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            con = (HttpURLConnection) url.openConnection();
            boolean resp = con.getDefaultUseCaches();
            log.info("URL from the connection: " + resp);

        } catch (IOException e2) {
            // some IO error, maybe timeout.
            e2.printStackTrace();
        }

        try {
            con.setRequestMethod(method);

        } catch (ProtocolException e1) {
            // never come here
            if (con != null) {
                con.disconnect();
                con = null;
                log.info("connect cannot be established");
            }
        }

        return con;
    }


    public int getHttpResponseCode(HttpURLConnection con){

        int tmpCode = 0;

        try {
            tmpCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpCode;
    }


    public String getContentFromConnection(HttpURLConnection con) {

        InputStream input;

        try {
            input = con.getInputStream();
        } catch (IOException e) {
            input = con.getErrorStream();
        }

        try {
            return stream2String(input);
        } finally {
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                }
        }
    }


    private String stream2String(InputStream input) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        try {
            while ((len = input.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = null;
        try {
            result = new String(baos.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = new String(baos.toByteArray());
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return result;
    }


    public class KPULhttpresponse{
        public int code;
        public String content;
        public String url;

    }

}
