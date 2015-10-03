package comp.services;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import comp.domain.SinaAuth;
import comp.utils.HttpUtils;
import comp.utils.SinaCommonString;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;

/**
 * Created by liushanchen on 15/9/29.
 */
public class SinaAuthorization {
    private SinaAuth sinaAuth;


    public SinaAuthorization() {
        sinaAuth =new SinaAuth();
    }
    public SinaAuthorization(String redirectUri) {
        sinaAuth =new SinaAuth(redirectUri);
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
                MultivaluedMap<String, String> formParams =new MultivaluedMapImpl();
                formParams.add(SinaCommonString.SINA_AUTH_P_client_id,sinaAuth.getClient_id());
                formParams.add(SinaCommonString.SINA_AUTH_P_client_secret, sinaAuth.getClient_secret());
                formParams.add(SinaCommonString.SINA_AUTH_P_grant_type,SinaCommonString.SINA_AUTH_GRANT_TYPE_CODE );
                formParams.add(SinaCommonString.SINA_AUTH_P_code, sinaAuth.getCode());
                formParams.add(SinaCommonString.SINA_AUTH_P_redirect_uri, sinaAuth.getRedirect_uri());

                HttpUtils httpUtils=new HttpUtils();
                HashMap tokenMap=httpUtils.doPost(SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI, formParams);

                sinaAuth.setAccess_token((String)tokenMap.get("access_token"));
                sinaAuth.setRefresh_token((String) tokenMap.get("refresh_token"));
                return "Oauth get["+"access_token: "+sinaAuth.getAccess_token()+";refresh_token:"+sinaAuth.getRefresh_token();

            }
        }else{
            return "use sinaAuth.Refresh_token()";
        }
        return null;
    }
    public String getUserInfo(){
        if(sinaAuth.getRefresh_token()==null){
            if(sinaAuth.getCode()!=null&&sinaAuth.getCode()!=""){
                MultivaluedMap<String, String> formParams =new MultivaluedMapImpl();

                HttpUtils httpUtils=new HttpUtils();
                HashMap tokenMap=httpUtils.doPost(SinaCommonString.SINA_AUTH_ACCOUNT_INFO_URI, formParams,sinaAuth.getAccess_token());


                return tokenMap.toString();
            }
        }else{
            return "use sinaAuth.Refresh_token()";
        }
        return null;
    }

}
