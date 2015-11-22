package comp.utils.KPClass;

import comp.utils.KuaipanCommonString;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by allenlee on 21/11/2015.
 */
public class URLGenTestexample {

    private String method = "GET";
    private String base_uri = "http://openapi.kuaipan.cn/1/fileops/create_folder";

    private String version = "1.0";
    private String oauth_token = "fa361a4a1dfc4a739869020e586582f9";
    private String oauth_signature_method = "HMAC-SHA1";
    private String oauth_nonce = "58456623";
    private String oauth_timestamp = "1328881571";
    private String oauth_consumer_key = "79a7578ce6cf4a6fa27dbf30c6324df4";
    private String path = "/test@kingsoft.com";
    private String rootpath = "kuaipan";
    private String consumer_secret = "c7ed87c12e784e48983e3bcdc6889dad";
    private String oauth_secret = "0183ce137e4d4170b2ac19d3a9fda677";


    private String Ans = "GET&http%3A%2F%2Fopenapi.kuaipan.cn%2F1%2Ffileops%2Fcreate_folder&oauth_consumer_key%3D79a7578ce6cf4a6fa27" +
            "dbf30c6324df4%26oauth_nonce%3D58456623%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1328881571" +
            "%26oauth_token%3Dfa361a4a1dfc4a739869020e586582f9%26oauth_version%3D1.0%26path%3D%252Ftest%2540kingsoft.com" +
            "%26root%3Dkuaipan";
    private String Ans_key = "c7ed87c12e784e48983e3bcdc6889dad&0183ce137e4d4170b2ac19d3a9fda677";


    private String finalsignature = "pa7Fuh9GQnsPc%2BLcn%2BQu6G7LVEU%3D";




    public String getSignature(String path){
        String signature = null;
        String oauth;
        KPURLGen kpurlgen= new KPURLGen();
        try {
            signature = basesignature(path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        oauth = kpurlgen.hmacsha1(signature, consumer_secret + "&" + oauth_secret);

        return oauth + "\n" + finalsignature ;

    }

    public String basesignature(String path) throws UnsupportedEncodingException {
        String commStr1;
        KuaipanCommonString KPString = new KuaipanCommonString();
        KPURLGen kpurlgen= new KPURLGen();

        commStr1 =  method+"&"
                + URLEncoder.encode(base_uri, "utf-8") + "&";
        String commStr2 = "oauth_consumer_key=" + oauth_consumer_key
                + "&oauth_nonce=" + oauth_nonce
                + "&oauth_signature_method=" + oauth_signature_method
                + "&oauth_timestamp=" + oauth_timestamp
                + "&oauth_token=" + oauth_token
                + "&oauth_version=" + version;


        String commStr3 = "&path=" + path
                + "&root=" + rootpath;

        commStr2 = URLEncoder.encode(commStr2, "utf-8");
        commStr3 = URLEncoder.encode(commStr3, "utf-8");
        commStr3 = kpurlgen.myEncode(commStr3);



        String commStr = commStr1+ commStr2 + commStr3;
//        if (commStr.equals(Ans))
//            commStr =  commStr + "\n ......is corrent";
//        else
//            commStr =  commStr + "\n" + Ans;

        return commStr;
    }



}


