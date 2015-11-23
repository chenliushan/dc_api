package comp.utils;

/**
 * Created by liushanchen on 15/9/29.
 */
public class KuaipanCommonString {

    //应用注册信息

    public static String KP_TEMP_OAUTH_TOKEN = "";
    public static String KP_TEMP_OAUTH_TOKEN_SECRET = "";
    public static String KP_TEMP_CALLBACK = "";
    public static String KP_TEMP_EXPIRES = "";

    public static String KP_OAUTH_TOKEN = "062731a2613009ba910ee3cd";
    public static String KP_OAUTH_TOKEN_SECRET = "e328022ee786484ab8b126da8381ce4c";
    public static String KP_CHARGED_DIR = "";
    public static String KP_USERID = "";
    public static String KP_EXPIRES = "";

    public static String KP_COMSUMER_KEY = "xcobyIb2lOtRlJNV";
    public static String KP_COMSUMER_SECRET = "945oMKfxuWpdzxWa";
    public static String KP_OAUTH_REQUEST_METHOD = "GET";
    public static String KP_OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
    public static String KP_OAUTH_SIGNATURE = "";
    public static String KP_TIMESTAMP = "";
    public static String KP_OAUTH_VERSION = "1.0";
    public static String KP_OAUTH_NONCE = "";

    public static String KP_REQUEST_TOKEN_URL = "https://openapi.kuaipan.cn/open/requestToken";
    public static String KP_ACCESS_TOKEN_URL = "https://openapi.kuaipan.cn/open/accessToken";
    public static String KP_CALLBACK = "http://0.0.0.0:8080/kuaipan/callback";
    public static String KP_SIGNIN = "https://www.kuaipan.cn/api.php?ac=open&op=authorise&oauth_token=";


    // Upload Parameters
    public static String KP_UPLOAD_URL = "http://api-content.dfs.kuaipan.cn/1/fileops/upload_locate";
    public static String KP_UPLOAD_HOST = "";
    public static String KP_UPLOAD_STAT = "";
    public static String KP_UPLOAD_ROOTPATH = "";
    public static String KP_UPLOAD_PHRASE = "/1/fileops/upload_file";
    public static String KP_UPLOAD_SIGNATURE = "";
    //public static String KP_LOCALROOT = "/Users/allenlee/Desktop/api";
    public static String KP_OVERWRITE = "True";
    public static String KP_UPLOAD_REQUEST_METHOD = "POST";


    // Download Parameters
    public static String KP_DOWNLOAD_URL= "http://api-content.dfs.kuaipan.cn/1/fileops/download_file";
    public static String KP_DOWNLOAD_REQUEST_METHOD = "GET";
    public static String KP_DOWNLOAD_SIGNATURE = "";
    public static String KP_CLOUDROOT = "app_folder";
    public static String KP_DOWNLOAD_REDIRECT = "http://www.kuaipan.cn";

    // Account information paramenters
    public static String KP_ACCINFO_REQUEST_METHOD = "GET";
    public static String KP_ACCINFO_REQUEST_URL = "http://openapi.kuaipan.cn/1/account_info";
    public static String KP_ACCINFO_SIGNATURE = "";

    // Folder information paramenters
    public static String KP_FOLDER_REQUEST_METHOD = "GET";
    public static String KP_FOLDER_REQUEST_URL = "http://openapi.kuaipan.cn/1/metadata";
    public static String KP_FOLDER_SIGNATURE = "";

    // Delete File
    public static String KP_DELETE_URL = "http://openapi.kuaipan.cn/1/fileops/delete";
    public static String KP_DELETE_REQUEST_METHOD = "GET";
    public static String KP_DELETE_SIGNATURE = "";


}
