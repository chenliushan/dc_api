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


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by allenlee on 21/11/2015.
 */
public class KPDLCLass {

    private static Log log = LogFactory.getLog(KPDLCLass.class);

    public void startDownload(String url, String fileName, HttpServletResponse redirection) throws IOException, URISyntaxException {

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
        log.info("HttpResponse: " + response);
        InputStream is = response.getEntity().getContent();

        try {

//            File outfile = new File(CommonString.LOCAL_KPDOWNLOAD_PATH + fileName);
            File outfile = new File(CommonUtil.DOWNLOAD_PATH + fileName);
            FileOutputStream outputStream = new FileOutputStream(outfile);

            byte buffer[] = new byte[2048];
            //InputStream fileInputStream = clientResponse.getEntityInputStream();
            int len;
            while ((len = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.close();
            is.close();
            if(redirection != null)
                redirection.sendRedirect(KuaipanCommonString.KP_DOWNLOAD_REDIRECT);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
