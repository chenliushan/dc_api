package comp.utils.KPClass;

import comp.utils.CommonString;
import comp.utils.KPUtil;
import comp.utils.KuaipanCommonString;
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

    public KPULCLass(String path){
        path = CommonString.LOCAL_KPUPLOAD_PATH + path;
        File file = new File(path);
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public KPULhttpresponse uploadFile(String path, boolean overwrite){

        KPURLGen kpULClass = new KPURLGen();
        String host = getBaseUploadHost();
        String url = null;
        String fileName = path;

        boolean status = overwrite;
        try {
            url = kpULClass.getULURL(path, status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Map<String, String> params = new TreeMap<String, String>();
        //params.put("root", session.root);
        //params.put("path", path);
        //params.put("overwrite", Boolean.toString(overwrite));

        //String url = buildPostURL(host,
        //        "/1/fileops/upload_file", params, session.consumer,
        //        session.accessToken, false);
       // KPULhttpresponse resp = startUpload(url, input, fileName);

        //return resp.fromJson(KuaipanFile.class);
        log.info("url: " + url);
        return null;
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
        sb.append("Content-Disposition: form-data; name=\"fileData\"; filename=\""
                + fileName + "\"\r\n");
        sb.append("Content-Type: application/octet-stream\r\n\r\n");
        String endStr = "\r\n"
                + boundary
                + "\r\nContent-Disposition: form-data; name=\"Upload\"\r\n\r\nSubmit Query\r\n"
                + boundary + "--\r\n";
        byte[] endData = endStr.getBytes();

        System.out.println(sb.toString());

        OutputStream os = null;
        //OutputStream ab = null;

         //Test Code.
//        try {
//            ab = new FileOutputStream(new File("/Users/allenlee/Desktop/apiDownload/transfer.pdf"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        try {
            con.connect();
            os = con.getOutputStream();
            os.write(sb.toString().getBytes());
            //ab.write(sb.toString().getBytes());
            //log.info("getResponseMessage: " + con.getResponseMessage());
            bufferedWriting(os, datastream);
            //bufferedWriting(ab, datastream);
            os.write(endData);
            //ab.write(endData);

        } catch (IOException e5) {
            e5.printStackTrace();
        } finally {
            try {

                if (os != null) {
                    os.flush();
                    os.close();
                }


                /*
                if (ab != null) {
                    ab.flush();
                    ab.close();
                }
                */


            } catch (IOException e) {
            }

        }
    }

    private void bufferedWriting(OutputStream to_write, InputStream to_read) {
        long last_triggered_time = 0L;
        int len = 0;
        int count = 0;
        byte[] buf = new byte[BUFFER_SIZE];

        // Test code below

        try {
            while ((len = to_read.read(buf)) != -1) {
                to_write.write(buf, 0, len);
                count += len;
               // long current_time = System.currentTimeMillis();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("File Size output: " + count);
    }


    public KPULhttpresponse startUpload(String url, InputStream datastream, String fileName){

        KPULhttpresponse resp = new KPULhttpresponse();
        HttpURLConnection con = getConnectionFromUrl(url , "POST");


        multipartUploadData(con, datastream, fileName);

        resp.code = getHttpResponseCode(con);
        resp.content = getContentFromConnection(con);
        resp.url = url;

        log.info("code, content, url" + resp.code + ", " + resp.content + ", " + resp.url );

//        try {
//            log.info("Response from getConnectionFromURL: " + con.getResponseMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
            // never come here
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

    public class KPULhttpresponse{

        public int code;
        public String content;
        public String url;

    }


    public String getContentFromConnection(HttpURLConnection con) {

        InputStream input = null;

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
            // bug??
            result = new String(baos.toByteArray());
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return result;
    }

    /*
    public static KuaipanHTTPResponse doUpload(KuaipanURL baseurl, InputStream datastream, long size){
        KuaipanHTTPResponse resp = new KuaipanHTTPResponse();

        HttpURLConnection con = getConnectionFromUrl(baseurl.queryByGetUrl(),
                "POST");

        multipartUploadData(con, datastream, size);

        resp.code = getResponseHTTPStatus(con);
        resp.content = getStringDataFromConnection(con);
        resp.url = baseurl;

        if (con != null)
            con.disconnect();

        return resp;
    }







    public static KuaipanURL buildPostURL(String host, String location,
                                          Map<String, String> params, ConsumerToken consumer,
                                          TokenPair token, boolean isSecure) {
        KuaipanURL kpurl = buildURL("POST", host, location, params, consumer,
                token, isSecure);
        return kpurl;
    }





    private static KuaipanURL buildURL(String method, String host,
                                       String location, Map<String, String> params,
                                       ConsumerToken consumer, TokenPair token, boolean isSecure) {

        TreeMap<String, String> signed_params;
        location = urlEncode(location);
        if (params != null)
            signed_params = new TreeMap<String, String>(params);
        else
            signed_params = new TreeMap<String, String>();

        signed_params.put("oauth_nonce", generateNonce());
        signed_params.put("oauth_timestamp",
                Long.toString((System.currentTimeMillis() / 1000)));
        signed_params.put("oauth_version", "1.0");
        signed_params.put("oauth_signature_method", "HMAC-SHA1");

        String signature;
        StringBuffer requestUrl = new StringBuffer();

        try {
            signature = generateSignature(method, host, location,
                    signed_params, consumer, token, isSecure);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeyException e) {
            return null;
        }

        signed_params.put("oauth_signature", signature);
        signed_params.put("oauth_consumer_key", consumer.oauth_token);
        if (token != null)
            signed_params.put("oauth_token", token.oauth_token);

        requestUrl.append(buildURI(host, location, isSecure));

        return new KuaipanURL(requestUrl.toString(),
                encodeParameters(signed_params));
    }


    public static class UploadHostFactory {
        private static String upload_host = null;
        private static long last_refresh_time = 0;

        private static final long REFRESH_INTERVAL = 3600 * 10 * 1000;

        private UploadHostFactory() {
        }

        public static String getUploadHost(){
            refreshUploadHost();
            return upload_host;
        }

        private static synchronized void refreshUploadHost()
                throws KuaipanIOException {
            if (upload_host == null
                    || (System.currentTimeMillis() - last_refresh_time) > REFRESH_INTERVAL) {
                KuaipanHTTPResponse resp = doGet(new KuaipanURL(
                        UPLOAD_LOCATE_URL));
                if (resp.content == null)
                    throw new KuaipanIOException(resp.toString());

                UploadLocate ul = JsonUtil.fromJson(resp.content,
                        UploadLocate.class);
                if (ul.url == null)
                    throw new KuaipanIOException(resp.toString());

                if (ul.url.startsWith("http://"))
                    ul.url = ul.url.replaceFirst("http://", "");
                else if (ul.url.startsWith("https://"))
                    ul.url = ul.url.replaceFirst("https://", "");

                if (ul.url.endsWith("/"))
                    ul.url = ul.url.substring(0, ul.url.length() - 1);

                upload_host = ul.url;
                last_refresh_time = System.currentTimeMillis();
            }
        }
    }


    private static synchronized void refreshUploadHost(){

        if (upload_host == null
                || (System.currentTimeMillis() - last_refresh_time) > REFRESH_INTERVAL) {
            KuaipanHTTPResponse resp = doGet(new KuaipanURL(
                    UPLOAD_LOCATE_URL));
            if (resp.content == null)
                throw new KuaipanIOException(resp.toString());

            UploadLocate ul = JsonUtil.fromJson(resp.content,
                    UploadLocate.class);
            if (ul.url == null)
                throw new KuaipanIOException(resp.toString());

            if (ul.url.startsWith("http://"))
                ul.url = ul.url.replaceFirst("http://", "");
            else if (ul.url.startsWith("https://"))
                ul.url = ul.url.replaceFirst("https://", "");

            if (ul.url.endsWith("/"))
                ul.url = ul.url.substring(0, ul.url.length() - 1);

            upload_host = ul.url;
            last_refresh_time = System.currentTimeMillis();
        }
    }
    */



}
