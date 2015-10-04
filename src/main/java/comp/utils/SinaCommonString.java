package comp.utils;

/**
 * Created by liushanchen on 15/9/29.
 */
public class SinaCommonString {

    //应用注册信息
    public static String SINA_AUTH_APP_KEY="1377006082";
    public static String SINA_AUTH_APP_SECRET="31c3b4be74fc9d34f4bea684cb6dc1bd";
    public static String REDERECT_URI_SINA="http://localhost:8080/sina_code";

    //获取权限第一步：用户授权，取得code
    public static String SINA_AUTH_URI="https://auth.sina.com.cn/oauth2/authorize";
    public static String SINA_AUTH_CLIENT_ID="1377006082";

    //获取权限第二步：使用code获取access——token、refresh——token  OR  使用refresh——token获取access——token；
    public static String SINA_AUTH_ACCESS_TOKEN_URI="https://auth.sina.com.cn/oauth2/access_token";
    public static String SINA_AUTH_GRANT_TYPE_REF="refresh_token";
    public static String SINA_AUTH_GRANT_TYPE_CODE="authorization_code";

    public static String SINA_AUTH_P_client_id="client_id";
    public static String SINA_AUTH_P_client_secret="client_secret";
    public static String SINA_AUTH_P_grant_type="grant_type";
    public static String SINA_AUTH_P_code="code";
    public static String SINA_AUTH_P_redirect_uri="redirect_uri";
    public static String SINA_AUTH_P_refresh_token="refresh_token";
    public static String SINA_AUTH_P_access_token="access_token";


    public static String SINA_AUTH_ACCOUNT_INFO_URI="https://api.weipan.cn/2/account/info";



}
