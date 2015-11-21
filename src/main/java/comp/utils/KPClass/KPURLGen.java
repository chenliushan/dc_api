package comp.utils.KPClass;

import comp.services.KPAuthorization;
import comp.utils.KuaipanCommonString;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by allenlee on 21/11/2015.
 */
public class KPURLGen {

    public String baseULsignature(String path) throws UnsupportedEncodingException {
        String commStr1;
        KuaipanCommonString KPString = new KuaipanCommonString();

        commStr1 =  KPString.KP_DOWNLOAD_REQUEST_METHOD+"&"
                + URLEncoder.encode(KPString.KP_DOWNLOAD_URL, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;

        commStr2 = URLEncoder.encode(commStr2, "utf-8");

        String commStr3 = "&path=" + path
                + "&root=" + KPString.KP_CLOUDROOT;
        commStr3 = URLEncoder.encode(commStr3, "utf-8");
        commStr3 = myEncode(commStr3);

        String commStr = commStr1+ commStr2 + commStr3;

        System.out.println("commStr KPULRGen DL: " + commStr);

        return commStr;
    }


    public String baseDLsignature(String path) throws UnsupportedEncodingException {
        String commStr1;
        KuaipanCommonString KPString = new KuaipanCommonString();

        commStr1 =  KPString.KP_DOWNLOAD_REQUEST_METHOD+"&"
                + URLEncoder.encode(KPString.KP_DOWNLOAD_URL, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;

        commStr2 = URLEncoder.encode(commStr2, "utf-8");

        String commStr3 = "&path=" + path
                + "&root=" + KPString.KP_CLOUDROOT;
        commStr3 = URLEncoder.encode(commStr3, "utf-8");
        commStr3 = myEncode(commStr3);

        String commStr = commStr1+ commStr2 + commStr3;

        System.out.println("commStr KPULRGen DL: " + commStr);

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
            signature = baseDLsignature(path);
            KPString.KP_DOWNLOAD_SIGNATURE = KPAuth.hmacsha1(signature, KPString.KP_COMSUMER_SECRET + "&"
                    + KPString.KP_OAUTH_TOKEN_SECRET);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }


        path = URLEncoder.encode(path, "utf-8");
        String url = KPString.KP_DOWNLOAD_URL + "?"
                + "oauth_signature=" +  KPString.KP_DOWNLOAD_SIGNATURE
                + "&oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION
                + "&path=" + path
                + "&root=" + KPString.KP_CLOUDROOT;

        return url;
    }

    //
    public String baseFolderSignature(String path) throws UnsupportedEncodingException {
        String commStr1;
        KuaipanCommonString KPString = new KuaipanCommonString();

        commStr1 =  KPString.KP_FOLDER_REQUEST_METHOD+"&"
                + URLEncoder.encode(KPString.KP_FOLDER_REQUEST_URL +"/" + KPString.KP_CLOUDROOT + path, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;


        commStr2 = URLEncoder.encode(commStr2, "utf-8");
        String commStr = commStr1+ commStr2;
        //System.out.println("commStr: " + commStr);

        return commStr;
    }

    public String getFolderURL(String path) throws IOException {

        KuaipanCommonString KPString = new KuaipanCommonString();
        KPAuthorization KPAuth = new KPAuthorization();

        KPString.KP_TIMESTAMP = KPAuth.set_timestamp();
        KPString.KP_OAUTH_NONCE = KPAuth.set_nonce();

        String signature;
        try {
            signature = baseFolderSignature(path);
            KPString.KP_FOLDER_SIGNATURE = KPAuth.hmacsha1(signature, KPString.KP_COMSUMER_SECRET + "&"
                    + KPString.KP_OAUTH_TOKEN_SECRET);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        String url = KPString.KP_FOLDER_REQUEST_URL + "/" + KPString.KP_CLOUDROOT + path +"?"
                + "oauth_signature=" +  KPString.KP_FOLDER_SIGNATURE
                + "&oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;

        return url;
    }



    // Below are URL generator of Account information enquirey.
    public String baseAccinfosignature() throws UnsupportedEncodingException {
        String commStr1;
        KuaipanCommonString KPString = new KuaipanCommonString();

        commStr1 =  KPString.KP_ACCINFO_REQUEST_METHOD+"&"
                + URLEncoder.encode(KPString.KP_ACCINFO_REQUEST_URL, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;


        commStr2 = URLEncoder.encode(commStr2, "utf-8");
        String commStr = commStr1+ commStr2;

        return commStr;
    }


    public String getAccURL() throws IOException {

        KuaipanCommonString KPString = new KuaipanCommonString();
        KPAuthorization KPAuth = new KPAuthorization();

        KPString.KP_TIMESTAMP = KPAuth.set_timestamp();
        KPString.KP_OAUTH_NONCE = KPAuth.set_nonce();

        String signature;
        try {
            signature = baseAccinfosignature();
            KPString.KP_ACCINFO_SIGNATURE = KPAuth.hmacsha1(signature, KPString.KP_COMSUMER_SECRET + "&"
                    + KPString.KP_OAUTH_TOKEN_SECRET);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        String url = KPString.KP_ACCINFO_REQUEST_URL + "?"
                + "oauth_signature=" +  KPString.KP_ACCINFO_SIGNATURE
                + "&oauth_consumer_key=" + KPString.KP_COMSUMER_KEY
                // + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_nonce=" + KPString.KP_OAUTH_NONCE
                + "&oauth_signature_method=" + KPString.KP_OAUTH_SIGNATURE_METHOD
                + "&oauth_timestamp=" + KPString.KP_TIMESTAMP
                + "&oauth_token=" + KPString.KP_OAUTH_TOKEN
                + "&oauth_version=" + KPString.KP_OAUTH_VERSION;

        return url;
    }

    public String hmacsha1(String data, String key) {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(spec);
            byteHMAC = mac.doFinal(data.getBytes());
            System.out.println(byteHMAC);
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

    public String myEncode(String code){

        String str1 = "%2F";            // Convert '/' to %252F
        String str1_1 = "%252F";
        code = code.replace(str1, str1_1);


        String str2 = "%40";
        String str2_1 = "%2540";        // Convert '@' to %2540
        code = code.replace(str2, str2_1);

        return code;
    }


}
