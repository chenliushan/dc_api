package comp.utils;

/**
 * Created by liushanchen on 15/10/12.
 */
public class OneDriveConmmonString {
    //应用注册信息
    public static String AUTH_APP_KEY = "0000000044163A18";
    public static String AUTH_APP_SECRET = "IpzFxQ9urvBmL8se8pV6kobQXfubE8R1";
    public static String REDERECT_URI = "http://localhost:8080/onedrive/code";
    /*
    GET https://login.live.com/oauth20_authorize.srf?client_id=0000000044163A18&scope=wl.signin wl.offline_access onedrive.readwrite&response_type=token&redirect_uri=http://localhost:8080/onedrive_code
     */
    public static String AUTH_PARAMETER_client_id = "client_id";
    public static String AUTH_PARAMETER_scope = "scope";
    public static String AUTH_PARAMETER_response_type = "response_type";
    public static String AUTH_PARAMETER_redirect_uri = "redirect_uri";
    public static String AUTH_PARAMETER_client_secret = "client_secret";
    public static String AUTH_PARAMETER_code = "code";
    public static String AUTH_PARAMETER_grant_type = "grant_type";

    //获取权限第一步：用户授权，取得code
    public static String AUTH_REQ_URI = "https://login.live.com/oauth20_authorize.srf";
    public static String AUTH_SCOPE = "wl.signin wl.offline_access onedrive.readwrite";
    public static String AUTH_RESPONSE_TYPE = "code";
    public static String AUTH_GRANT_TYPE = "authorization_code";

    //第二部：获取token
    public static String AUTH_TOKEN_URI = "https://login.live.com/oauth20_token.srf";

    //第三部：用token操作onedrive文件
    public static String AUTH_ROOT_URI = "https://api.onedrive.com/v1.0";
    public static String AUTH_HEADER_Authorization = "Authorization";




}
