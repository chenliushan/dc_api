package comp.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import comp.services.KPAuthorization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URLEncoder;

/**
 * Created by liushanchen on 15/10/6.
 */
public class KPUtil extends HttpUtils{

    private static Log log = LogFactory.getLog(KPUtil.class);

    public static void json2OTempauthToken(String json){
        Type type = new TypeToken<KPTempToken>() {
        }.getType();
        KPTempToken tempToken= new Gson().fromJson(json,type);

        KuaipanCommonString.KP_TEMP_OAUTH_TOKEN = tempToken.getOauth_token();
        KuaipanCommonString.KP_TEMP_OAUTH_TOKEN_SECRET = tempToken.getOauth_token_secret();
        KuaipanCommonString.KP_TEMP_CALLBACK = tempToken.getCallback();
        KuaipanCommonString.KP_TEMP_EXPIRES = tempToken.getExpires_in();

    }

    public static void json2OauthToken(String json){
        Type type = new TypeToken<KPToken>() {
        }.getType();
        KPToken tempToken= new Gson().fromJson(json,type);

        KuaipanCommonString.KP_OAUTH_TOKEN = tempToken.getOauth_token();
        KuaipanCommonString.KP_OAUTH_TOKEN_SECRET = tempToken.getOauth_token_secret();
        KuaipanCommonString.KP_CHARGED_DIR = tempToken.getCharged_dir();
        KuaipanCommonString.KP_EXPIRES = tempToken.getExpires_in();
        KuaipanCommonString.KP_USERID = tempToken.getUser_id();

    }


    public static void json2OUploadInfo(String json){
        Type type = new TypeToken<UploadInfo>() {
        }.getType();
         UploadInfo uploadInfo= new Gson().fromJson(json, type);

        KuaipanCommonString.KP_UPLOAD_HOST = uploadInfo.getUrl();


        if (KuaipanCommonString.KP_UPLOAD_HOST.endsWith("/"))
            KuaipanCommonString.KP_UPLOAD_HOST=  KuaipanCommonString.KP_UPLOAD_HOST.
                    substring(0,  KuaipanCommonString.KP_UPLOAD_HOST.length() - 1);

        KuaipanCommonString.KP_UPLOAD_STAT = uploadInfo.getStat();
        KuaipanCommonString.KP_UPLOAD_ROOTPATH =   KuaipanCommonString.KP_UPLOAD_HOST
                                                + KuaipanCommonString.KP_UPLOAD_PHRASE;


        log.info("json of upload: " + KuaipanCommonString.KP_UPLOAD_ROOTPATH);

    }


    public String set_KPDL_signature(String path) throws UnsupportedEncodingException {
        String commStr1;
        KuaipanCommonString KPString = new KuaipanCommonString();

        commStr1 = KPString.KP_DOWNLOAD_REQUEST_METHOD +"&"
                + URLEncoder.encode(KPString.KP_DOWNLOAD_URL, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&path=" + path
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&root=" + KPString.KP_CLOUDROOT
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;

        commStr2 = URLEncoder.encode(commStr2, "utf-8");
        String commStr = commStr1+ commStr2;
        System.out.println("commStr DL: " + commStr);
        return commStr;
    }


//    http://api-content.dfs.kuaipan.cn/1/fileops/download_file?
//    oauth_version=1.0&
//    oauth_consumer_key=adcc22219c3645fb9e2c5413371dcd4f&
//    oauth_token=578e96fb73f9492d9f63fba434fde290&
//    oauth_signature=8g5tMUhQl6oLtUbp0pO9za5x3fY%3D&
//    oauth_nonce=4077316&
//    oauth_timestamp=1312165717&
//    root=kuaipan&
//    path=myfolder%2Ftest.doc




    public String getDLURL(String path) throws IOException {

        KuaipanCommonString KPString = new KuaipanCommonString();
        KPAuthorization KPAuth = new KPAuthorization();

        KPString.KP_TIMESTAMP = KPAuth.set_timestamp();
        KPString.KP_OAUTH_NONCE = KPAuth.set_nonce();

        String signature;
        try {
            signature = set_KPDL_signature(path);
            KPString.KP_DOWNLOAD_SIGNATURE = KPAuth.hmacsha1(signature, KPString.KP_COMSUMER_SECRET + "&"
                    + KPString.KP_OAUTH_TOKEN);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        String url = KPString.KP_DOWNLOAD_URL + "?"
                + "oauth_version=" + KPString.KP_OAUTH_VERSION
                + "&oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
               // + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_signature=" +  KPString.KP_DOWNLOAD_SIGNATURE
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&root=" + KPString.KP_CLOUDROOT
                + "&path=" + path;

        return url;
    }



    // Below is a Class to store the information about Access Token

    private class KPToken {

        //{"oauth_token_secret":"c3c747bf943545f6b5042636badfed87",
        // "oauth_token":"062731a2613009badce35315",
        // "charged_dir":"443377660173746178",
        // "user_id":103231906,
        // "expires_in":31536000}


        private String oauth_token_secret;//"Secret Token",
        private String oauth_token;// oauth_token,
        private String charged_dir;// root dir,
        private String user_id;// user ID
        private String expires_in;// EXpires time

        public String getOauth_token_secret() {
            return oauth_token_secret;
        }

        public void setOauth_token_secret(String oauth_token_secret) {
            this.oauth_token_secret = oauth_token_secret;
        }

        public String getOauth_token() {
            return oauth_token;
        }

        public void setOauth_token(String oauth_token) {
            this.oauth_token = oauth_token;
        }

        public String getCharged_dir() {
            return charged_dir;
        }

        public void setCharged_dir(String charged_dir) {
            this.charged_dir = charged_dir;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }
    }


//    public KPFile uploadFile(String path, InputStream is, long size,
//                                  boolean overwrite){
//
//        String host = KuaipanCommonString.KP_UPLOAD_HOST;
//        Map<String, String> params = new TreeMap<String, String>();
//        params.put("root", );
//        params.put("path", path);
//        params.put("overwrite", Boolean.toString(overwrite));
//
//        KuaipanURL url = OauthUtility.buildPostURL(host,
//                "/1/fileops/upload_file", params, session.consumer,
//                session.accessToken, false);
//        KuaipanHTTPResponse resp = KuaipanHTTPUtility.doUpload(url, is, size,
//                lr);
//
//        return resp.fromJson(KuaipanFile.class);
//    }

    // Below is the class to shore Temp Token


    private class KPTempToken{

        //{"oauth_token_secret":"af16df465fcb43f280bd9b43f3494df0",
        // "oauth_token":"92f3c09a68fb4d52bf6b739fad86a349",
        // "expires_in":1800,
        // "oauth_callback_confirmed":true}

        private String oauth_token_secret;//"Secret Token",
        private String expires_in;// Expires time,
        private String oauth_token;//"Oauth Token",
        private String callback;// Callback status


        public String getOauth_token_secret() {
            return oauth_token_secret;
        }

        public void setOauth_token_secret(String oauth_token_secret) {
            this.oauth_token_secret = oauth_token_secret;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getOauth_token() {
            return oauth_token;
        }

        public void setOauth_token(String oauth_token) {
            this.oauth_token = oauth_token;
        }

        public String getCallback() {
            return callback;
        }

        public void setCallback(String callback) {
            this.callback = callback;
        }

    }

    // This is the class to get the upload information
    private class UploadInfo{

        //{"url": "http://p5.dfs.kuaipan.cn:8080/cdlnode/",
        // "stat": "OK"}

        private String url;
        private String stat;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }
    }


}


