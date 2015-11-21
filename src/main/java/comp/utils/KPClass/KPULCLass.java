package comp.utils.KPClass;

import comp.utils.KPUtil;
import comp.utils.KuaipanCommonString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by allenlee on 21/11/2015.
 */
public class KPULCLass {

    public final static int BUFFER_SIZE = 4048;

    /*
    public KuaipanFile uploadFile(String path, InputStream is, long size, boolean overwrite){

        String host = getBaseUploadHost();
        Map<String, String> params = new TreeMap<String, String>();
        params.put("root", session.root);
        params.put("path", path);
        params.put("overwrite", Boolean.toString(overwrite));

        KuaipanURL url = buildPostURL(host,
                "/1/fileops/upload_file", params, session.consumer,
                session.accessToken, false);
        KuaipanHTTPResponse resp = KuaipanHTTPUtility.doUpload(url, is, size,
                lr);

        return resp.fromJson(KuaipanFile.class);
    }

    */


    public String getBaseUploadHost(){

        KPUtil kpUtils = new KPUtil();
        String json = kpUtils.doGet(KuaipanCommonString.KP_UPLOAD_URL);
        kpUtils.json2OUploadInfo(json);
        return KuaipanCommonString.KP_UPLOAD_ROOTPATH;
    }

    private static void multipartUploadData(HttpURLConnection con, InputStream datastream, long size) {
        con.setDoOutput(true);
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
        sb.append("Content-Disposition: form-data; name=\"Filedata\"; filename=\""
                + "myfile" + "\"\r\n");
        sb.append("Content-Type: application/octet-stream\r\n\r\n");
        String endStr = "\r\n"
                + boundary
                + "\r\nContent-Disposition: form-data; name=\"Upload\"\r\n\r\nSubmit Query\r\n"
                + boundary + "--\r\n";
        byte[] endData = endStr.getBytes();
        OutputStream os = null;
        try {
            con.connect();
            os = con.getOutputStream();
            os.write(sb.toString().getBytes());


            bufferedWriting(os, datastream, size);
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

    private static void bufferedWriting(OutputStream to_write, InputStream to_read, long total) {
        long last_triggered_time = 0L;
        int len = 0;
        int count = 0;
        byte[] buf = new byte[BUFFER_SIZE];
        try {
            while ((len = to_read.read(buf)) != -1) {
                to_write.write(buf, 0, len);
                count += len;
                long current_time = System.currentTimeMillis();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
