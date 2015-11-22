package comp.utils.KPClass;


import comp.utils.CommonString;
import comp.utils.CommonUtil;
import comp.utils.KuaipanCommonString;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.springframework.util.Assert;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;

/**
 * Created by allenlee on 21/11/2015.
 */
public class KPConn {

    public final static int BUFFER_SIZE = 4048;

    public static int doDownload(String baseurl, OutputStream os){
        int code;
        HttpURLConnection con = getConnectionFromUrl(baseurl, "GET");
        code = getResponseHTTPStatus(con);
        String url = baseurl;

        //if (code == ) {
            writeStreamFromConnection(con, os);
        //    resp.content = null;
        //} else {
        //    resp.content = getStringDataFromConnection(con);
        //}

        if (con != null)
            con.disconnect();

        return code;
    }


    /*
    private static int getResponseHTTPStatus(HttpURLConnection con) {
        int code = 400;
        try {
            code = con.getResponseCode();
        } catch (IOException e) {
            // bad HTTP format from server, it may never happen
            if (con != null)
                con.disconnect();
        }
        return code;
    }

    private static String stream2String(InputStream is)
            throws KuaipanIOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[BUFFER_SIZE];
        int len = 0;
        try {
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            //
            throw new KuaipanIOException(e);
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

    private static String getStringDataFromConnection(HttpURLConnection con)
            throws KuaipanIOException {
        InputStream is = null;
        try {
            is = con.getInputStream();
        } catch (IOException e) {
            is = con.getErrorStream();
        }

        try {
            return stream2String(is);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
        }
    }
*/
    private static void writeStreamFromConnection(HttpURLConnection con,
                                                  OutputStream os){
        InputStream is = null;
        try {
            is = con.getInputStream();
        } catch (IOException e) {
            is = con.getErrorStream();
        }
        int total = con.getContentLength();

        try {
            bufferedWriting(os, is, total);
        } finally {

            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
        }
    }

    private static HttpURLConnection getConnectionFromUrl(String baseurl, String method){
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
            }
        }
        return con;
    }


    private static void bufferedWriting(OutputStream to_write, InputStream to_read, long total){
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


    private static int getResponseHTTPStatus(HttpURLConnection con) {
        int code = 578;   // Kuaipan Unknow error code
        try {
            code = con.getResponseCode();
        } catch (IOException e) {
            // bad HTTP format from server, it may never happen
            if (con != null)
                con.disconnect();
            e.printStackTrace();
        }
        return code;
    }

    public void test(String url, String fileName, HttpServletResponse redirection) throws ClientProtocolException, IOException, URISyntaxException {

        DefaultHttpClient hc = new DefaultHttpClient();
        //String url = "http://api-content.dfs.kuaipan.cn/1/fileops/download_file?oauth_signature_method=HMAC-SHA1&oauth_version=1.0&oauth_signature=ZKgUa3jaHUFt2W66FEbJCfE7k%2Fk%3D&oauth_token=aed1a2268d2544b095bf1a88402be494&oauth_nonce=21342098&oauth_timestamp=1330102676&path=%2F%E6%B5%8B1330102674.6%E8%AF%95%2Ftest%E7%9A%84w1330102674.6.wps&oauth_consumer_key=662d78dbbc7944a4ba7a5d491b158a0c&root=app_folder";

        //
        // ！ 注意！ CoreProtocolPNames.HTTP_CONTENT_CHARSET 及 CoreProtocolPNames.HTTP_ELEMENT_CHARSET
        //      的值必须被设成 “UTF-8”，才能保证在location中包含中文字符时，生成重定向跳转URI时不会失败
        // （详细解释）http://hc.apache.org/httpcompone ... l/fundamentals.html 1.7. HTTP request execution parameters
        //

        hc.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.TRUE);
        hc.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");  // 默认为ISO-8859-1
        hc.getParams().setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8");  // 默认为US-ASCII

        HttpGet hGet = new HttpGet(url);
        doWork(hc, hGet, fileName, redirection);
    }

    private void doWork(HttpClient client, HttpGet httpGet, String fileName, HttpServletResponse redirection) throws IOException, ClientProtocolException {
        HttpResponse response = client.execute(httpGet);
        InputStream is = response.getEntity().getContent();

        try {

            File outfile = new File(CommonString.LOCAL_KPDOWNLOAD_PATH + fileName);
            FileOutputStream outputStream = new FileOutputStream(outfile);

            byte buffer[] = new byte[2048];
            //InputStream fileInputStream = clientResponse.getEntityInputStream();
            int len;
            while ((len = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            is.close();
            redirection.sendRedirect(KuaipanCommonString.KP_DOWNLOAD_REDIRECT);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //System.out.println(size);

        //System.out.println(new String(buffer));
        //Assert.assertTrue(size > 0);
    }


    public void test(String url, String fileName) throws ClientProtocolException, IOException, URISyntaxException {
        DefaultHttpClient hc = new DefaultHttpClient();
        //String url = "http://api-content.dfs.kuaipan.cn/1/fileops/download_file?oauth_signature_method=HMAC-SHA1&oauth_version=1.0&oauth_signature=ZKgUa3jaHUFt2W66FEbJCfE7k%2Fk%3D&oauth_token=aed1a2268d2544b095bf1a88402be494&oauth_nonce=21342098&oauth_timestamp=1330102676&path=%2F%E6%B5%8B1330102674.6%E8%AF%95%2Ftest%E7%9A%84w1330102674.6.wps&oauth_consumer_key=662d78dbbc7944a4ba7a5d491b158a0c&root=app_folder";
        // ！ 注意！ CoreProtocolPNames.HTTP_CONTENT_CHARSET 及 CoreProtocolPNames.HTTP_ELEMENT_CHARSET
        //      的值必须被设成 “UTF-8”，才能保证在location中包含中文字符时，生成重定向跳转URI时不会失败
        // （详细解释）http://hc.apache.org/httpcompone ... l/fundamentals.html 1.7. HTTP request execution parameters
        //
        hc.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.TRUE);
        hc.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");  // 默认为ISO-8859-1
        hc.getParams().setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8");  // 默认为US-ASCII

        HttpGet hGet = new HttpGet(url);
        doWork(hc, hGet, fileName);
    }

    private void doWork(HttpClient client, HttpGet httpGet, String fileName) throws IOException, ClientProtocolException {
        HttpResponse response = client.execute(httpGet);
        InputStream is = response.getEntity().getContent();
        try {
            File outfile = new File(CommonUtil.DOWNLOAD_PATH+"/" + fileName);
            FileOutputStream outputStream = new FileOutputStream(outfile);
            byte buffer[] = new byte[2048];
            //InputStream fileInputStream = clientResponse.getEntityInputStream();
            int len;
            while ((len = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
