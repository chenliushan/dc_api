package comp.domain;

import comp.utils.SinaCommonString;

/**
 * Created by liushanchen on 15/9/29.
 */
public class SinaAuth {
    private String code_req_uri;
    private String token_uri;
    private String client_id;//	Yes	string	申请应用时分配的AppKey。
    private String client_secret;//	Yes	string	申请应用时分配的secret。
    private String redirect_uri;//	Yes	string	需要做urlencode。授权回调地址，站外应用需与设置的回调地址一致，站内应用需填写canvas page的地址。

    private String response_type;//	No	string	返回类型，支持code、token，默认值为code。
    private String state;//	No	string	需要做urlencode。用于保持请求和回调的状态，在回调时，会在Query Parameter中回传该参数。
    private String display;//	No	string	授权页面的终端类型，取值见下面的说明。

    private String code;	//Yes	string	调用authorize获得的code值。

    private SinaOauthToken sinaOauthToken;




    public String getCodeReqUri() {

            return code_req_uri+"?client_id="+client_id+"&redirect_uri="+redirect_uri;


    }
    public SinaAuth() {
        this.redirect_uri = SinaCommonString.REDERECT_URI_SINA;
        this.token_uri= SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI;
        this.code_req_uri= SinaCommonString.SINA_AUTH_URI;
        this.client_id= SinaCommonString.SINA_AUTH_CLIENT_ID;
        this.client_secret=SinaCommonString.SINA_AUTH_APP_SECRET;
    }

    public SinaAuth(String redirect_uri) {
        this.redirect_uri = redirect_uri;
        this.token_uri= SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI;
        this.code_req_uri= SinaCommonString.SINA_AUTH_URI;
        this.client_id= SinaCommonString.SINA_AUTH_CLIENT_ID;
        this.client_secret=SinaCommonString.SINA_AUTH_APP_SECRET;
    }




    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public SinaOauthToken getSinaOauthToken() {
        return sinaOauthToken;
    }

    public void setSinaOauthToken(SinaOauthToken sinaOauthToken) {
        this.sinaOauthToken = sinaOauthToken;
    }


}
