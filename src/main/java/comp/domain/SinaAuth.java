package comp.domain;

import comp.services.SinaAuthorization;
import comp.utils.CommonString;
import comp.utils.SinaCommonString;

import java.util.Scanner;

/**
 * Created by liushanchen on 15/9/29.
 */
public class SinaAuth {
    private String code_req_uri;
    private String method;

    private String client_id;//	Yes	string	申请应用时分配的AppKey。
    private String redirect_uri;//	Yes	string	需要做urlencode。授权回调地址，站外应用需与设置的回调地址一致，站内应用需填写canvas page的地址。

    private String response_type;//	No	string	返回类型，支持code、token，默认值为code。
    private String state;//	No	string	需要做urlencode。用于保持请求和回调的状态，在回调时，会在Query Parameter中回传该参数。
    private String display;//	No	string	授权页面的终端类型，取值见下面的说明。

    private String code;	//Yes	string	调用authorize获得的code值。
    private String refresh_token;//	Yes	string	获取到的刷新token。
    private String access_token;//


    public SinaAuth(){

    }

    public String getCodeReqUri() {
        if(method==CommonString.METHOD_GET){

            return code_req_uri+"?client_id="+client_id+"&redirect_uri="+redirect_uri;
        }else{
            return code_req_uri;
        }

    }

    public SinaAuth(String method, String redirect_uri) {
        this.method = method;
        this.redirect_uri = redirect_uri;
        this.code_req_uri= SinaCommonString.SINA_AUTH_URI;
        this.client_id= SinaCommonString.SINA_AUTH_CLIENT_ID;
    }

    public SinaAuth(String method, String client_id, String redirect_uri, String response_type, String state, String display) {
        this.method = method;
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.response_type = response_type;
        this.state = state;
        this.display = display;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
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

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}