package comp.domain;

import comp.utils.OneDriveConmmonString;

/**
 * Created by liushanchen on 15/11/19.
 */
public class OnedriveAuth {
    private String root_uri;
    private String client_id;//	Yes	string	申请应用时分配的AppKey。
    private String scope;
    private String redirect_uri;//	Yes	string	需要做urlencode。授权回调地址，站外应用需与设置的回调地址一致，站内应用需填写canvas page的地址。
    private String response_type;//	No	string	返回类型，支持code、token，默认值为code。
    private String client_secret;//	Yes	string	申请应用时分配的secret。
    private String code;	//Yes	string	调用authorize获得的code值。
    private OnedriveOauthToken onedriveOauthToken;

    public OnedriveAuth() {
        this.root_uri = OneDriveConmmonString.AUTH_ROOT_URI;
        this.client_id = OneDriveConmmonString.AUTH_APP_KEY;
        this.client_secret = OneDriveConmmonString.AUTH_APP_SECRET;
        this.redirect_uri = OneDriveConmmonString.REDERECT_URI;
        this.response_type = OneDriveConmmonString.AUTH_RESPONSE_TYPE;
        this.scope=OneDriveConmmonString.AUTH_SCOPE;

    }



    public String getCodeReqUri() {
        String uri= OneDriveConmmonString.AUTH_REQ_URI
                +"?"+OneDriveConmmonString.AUTH_PARAMETER_client_id+"="+client_id
                +"&"+ OneDriveConmmonString.AUTH_PARAMETER_scope+"="+scope
                +"&"+ OneDriveConmmonString.AUTH_PARAMETER_response_type+"="+response_type
                +"&"+ OneDriveConmmonString.AUTH_PARAMETER_redirect_uri+"="+redirect_uri;
        System.out.print("uri:"+uri);
        return uri;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getRoot_uri() {
        return root_uri;
    }

    public OnedriveOauthToken getOnedriveOauthToken() {
        return onedriveOauthToken;
    }

    public void setOnedriveOauthToken(OnedriveOauthToken onedriveOauthToken) {
        this.onedriveOauthToken = onedriveOauthToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
