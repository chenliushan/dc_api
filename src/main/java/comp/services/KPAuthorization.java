package comp.services;

import comp.utils.HttpUtils;
import comp.utils.KPClass.KPConn;
import comp.utils.KPClass.KPURLGen;
import comp.utils.KuaipanCommonString;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

/**
 * Created by liushanchen on 15/9/29.
 */
public class KPAuthorization {

    KuaipanCommonString KPString = new KuaipanCommonString();

    public String set_CommonString() throws UnsupportedEncodingException {
        String commstr1;
        commstr1 = KPString.KP_OAUTH_REQUEST_METHOD + "&" + URLEncoder.encode(KPString.KP_REQUEST_TOKEN_URL, "utf-8") + "&";
        KPString.KP_CALLBACK = URLEncoder.encode(KPString.KP_CALLBACK,"utf-8");
        String commstr2 = "oauth_callback=" + KPString.KP_CALLBACK
                + "&oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;
        commstr2 = URLEncoder.encode(commstr2, "utf-8");
        return commstr1 + commstr2;
    }


    public String hmacsha1(String data, String key) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ignore) {
        }
        String oauth = new BASE64Encoder().encode(byteHMAC);
        String signatureEncode = "";
        try {
            signatureEncode = URLEncoder.encode(oauth, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            //Logger.getLogger(KuaipanController.class.getName()).log(Level.WARN, null, ex);
            ex.printStackTrace();
        }
        oauth = signatureEncode;
        return oauth;
    }


    public String set_timestamp() {
        Date date = new Date();
        long time = date.getTime();
        return (time + "").substring(0, 10);
    }


    public String set_nonce() {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 32; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public String getRequestToken(){
        String set_basestring;
        KPString.KP_TIMESTAMP = set_timestamp();
        KPString.KP_OAUTH_NONCE = set_nonce();

        try {

            set_basestring = set_CommonString();
            KPString.KP_OAUTH_SIGNATURE = hmacsha1(set_basestring, KPString.KP_COMSUMER_SECRET + "&");
            System.out.printf("oauth_signature: %s\n", KPString.KP_OAUTH_SIGNATURE);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer url = new StringBuffer("https://openapi.kuaipan.cn/open/requestToken?");
        url.append("oauth_signature=" + KPString.KP_OAUTH_SIGNATURE+ "&");
        url.append("oauth_callback=" + KPString.KP_CALLBACK+ "&");
        url.append("oauth_consumer_key=" + KPString.KP_COMSUMER_KEY+ "&");
        url.append("oauth_nonce=" + KPString.KP_OAUTH_NONCE+ "&");
        url.append("oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD + "&");
        url.append("oauth_timestamp=" + KPString.KP_TIMESTAMP + "&");
        url.append("oauth_version=" + KPString.KP_OAUTH_VERSION +"");


        System.out.printf("Request KP Auth Temp Token URL:%s\n" , url);
        String tmp = url.toString();

        return tmp;

    }


    public String signInURL(String OauthToken){

        return KPString.KP_SIGNIN + OauthToken;     // Generate signIn URL;
    }


    public String getAccessToken(String oauth_token, String oauth_verifier, String oauth_token_secret) throws IOException {

        KPString.KP_TIMESTAMP = set_timestamp();
        KPString.KP_OAUTH_NONCE = set_nonce();

        String signature;
        try {
            signature = set_signature(oauth_token);
            KPString.KP_OAUTH_SIGNATURE = hmacsha1(signature, KPString.KP_COMSUMER_SECRET + "&" + oauth_token_secret);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        String url = "https://openapi.kuaipan.cn/open/accessToken?"
                + "&oauth_signature=" + KPString.KP_OAUTH_SIGNATURE
                + "&oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + oauth_token
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;

        //System.out.printf("url:%s\n", url);
        return url;
    }


    public String set_signature(String oauth_token) throws UnsupportedEncodingException {
        String commStr1;
        commStr1 = KPString.KP_OAUTH_REQUEST_METHOD + "&" +
                URLEncoder.encode(KPString.KP_ACCESS_TOKEN_URL, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + oauth_token
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;
        commStr2 = URLEncoder.encode(commStr2, "utf-8");
        String commStr = commStr1+ commStr2;
        return commStr;
    }

    public static String downloadFile(String path) {
        KPURLGen kpDLURL = new KPURLGen();
        String url = null;
        try {
            url = kpDLURL.getDLURL("/" +path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        KPConn kpconn = new KPConn();
        try {
            kpconn.test(url, path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "true";

    }
}
