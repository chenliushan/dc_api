package comp.services;

import comp.domain.SinaAuth;
import comp.utils.CommonString;
import comp.utils.HttpClientUtils;
import comp.utils.SinaCommonString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liushanchen on 15/9/29.
 */
public class SinaAuthorization {
    private SinaAuth sinaAuth;
    private HttpClientUtils httpClientUtils;
    private String redirectUri;

    public SinaAuthorization() {
        this.redirectUri=CommonString.PROTO+CommonString.IP+"/sinacode";
        sinaAuth =new SinaAuth(CommonString.METHOD_GET,redirectUri);
        httpClientUtils=new HttpClientUtils();

    }
    public SinaAuthorization(String redirectUri) {
        this.redirectUri=redirectUri;
        sinaAuth =new SinaAuth(CommonString.METHOD_GET,redirectUri);
        httpClientUtils=new HttpClientUtils();
    }
    public String sendCodeRequest(){
        System.out.println("sinaAuth:"+ sinaAuth.getCodeReqUri());
        return httpClientUtils.doGet(sinaAuth.getCodeReqUri());
    }
    public String codeReqUrl(){
        return sinaAuth.getCodeReqUri();
    }
    public void getCodeResponse(String code){
        if(code!=null&&code!=""){
            sinaAuth.setCode(code);
        }

    }

    public String getAccessToken(){
        if(sinaAuth.getRefresh_token()==null){
            if(sinaAuth.getCode()!=null&&sinaAuth.getCode()!=""){
                Map<String, String> formParams =new HashMap<String, String>();
                formParams.put(SinaCommonString.SINA_AUTH_P_client_id,sinaAuth.getClient_id());
                formParams.put(SinaCommonString.SINA_AUTH_P_client_secret,SinaCommonString.SINA_AUTH_P_client_secret);
                formParams.put(SinaCommonString.SINA_AUTH_P_grant_type,SinaCommonString.SINA_AUTH_GRANT_TYPE_CODE);
                formParams.put(SinaCommonString.SINA_AUTH_P_code,sinaAuth.getCode());
                formParams.put(SinaCommonString.SINA_AUTH_P_redirect_uri,redirectUri);
                return httpClientUtils.doPost(SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI, formParams);
            }
        }else{
            return "sinaAuth.getRefresh_token()!=null";
        }
        return null;
    }

}
