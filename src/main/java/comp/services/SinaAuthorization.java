package comp.services;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import comp.domain.SinaAuth;
import comp.domain.SinaMetadata;
import comp.utils.HttpUtils;
import comp.utils.SinaCommonString;
import comp.utils.SinaUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.ws.rs.core.MultivaluedMap;
import java.io.*;

/**
 * Created by liushanchen on 15/9/29.
 */
public class SinaAuthorization {
    private static SinaAuth sinaAuth;
    private static SinaMetadata rootMeta;
    private static Log log = LogFactory.getLog(SinaAuthorization.class);

    public SinaAuthorization() {
        sinaAuth = new SinaAuth();
    }

    public SinaAuthorization(String redirectUri) {
        sinaAuth = new SinaAuth(redirectUri);
    }

    public String codeReqUrl() {
        return sinaAuth.getCodeReqUri();
    }

    public void getCodeResponse(String code) {
        if (code != null && !code .equals( "")) {
            sinaAuth.setCode(code);
        } else {
            System.out.print("code is null");
        }

    }

    public SinaAuth getSinaAuth() {
        return sinaAuth;
    }

    public int getAccessToken() {
        if (sinaAuth.getCode() != null && !sinaAuth.getCode() .equals( "")) {
            MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
            formParams.add(SinaCommonString.SINA_AUTH_P_client_id, sinaAuth.getClient_id());
            formParams.add(SinaCommonString.SINA_AUTH_P_client_secret, sinaAuth.getClient_secret());
            formParams.add(SinaCommonString.SINA_AUTH_P_grant_type, SinaCommonString.SINA_AUTH_GRANT_TYPE_CODE);
            formParams.add(SinaCommonString.SINA_AUTH_P_code, sinaAuth.getCode());
            formParams.add(SinaCommonString.SINA_AUTH_P_redirect_uri, sinaAuth.getRedirect_uri());

            HttpUtils httpUtils = new HttpUtils();
//            HashMap tokenMap = httpUtils.doPost(SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI, formParams);
            String response = httpUtils.doPost(SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI, formParams);
            sinaAuth.setSinaOauthToken(SinaUtil.json2OauthToken(response));
            log.info( "getAccessToken" + response);
            return 0;

        }
        return -1;
    }

    public String refreshToken() {
        if (sinaAuth.getSinaOauthToken().getRefresh_token() != null) {

            MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
            formParams.add(SinaCommonString.SINA_AUTH_P_client_id, sinaAuth.getClient_id());
            formParams.add(SinaCommonString.SINA_AUTH_P_client_secret, sinaAuth.getClient_secret());
            formParams.add(SinaCommonString.SINA_AUTH_P_grant_type, SinaCommonString.SINA_AUTH_GRANT_TYPE_REF);
            formParams.add(SinaCommonString.SINA_AUTH_P_refresh_token, sinaAuth.getSinaOauthToken().getRefresh_token());
            formParams.add(SinaCommonString.SINA_AUTH_P_redirect_uri, sinaAuth.getRedirect_uri());

            HttpUtils httpUtils = new HttpUtils();
            String response = httpUtils.doPost(SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI, formParams);
            sinaAuth.setSinaOauthToken(SinaUtil.json2OauthToken(response));
            return "getRefreshToken" + response;

        } else {
            log.info("refresh_token==null");
            return "refresh_token==null";
        }

    }

    public MultivaluedMap addAuthToken(MultivaluedMap formParams) {
        if (sinaAuth != null) {
            formParams.add(SinaCommonString.SINA_AUTH_P_access_token, sinaAuth.getSinaOauthToken().getAccess_token());
            return formParams;
        } else {
            log.info("addAuthToken.sinaAuth==null");
        }
        return null;
    }

    public String addAuthToken(String formParams) {
        if (sinaAuth != null) {
            if (!formParams.contains("?")) {
                formParams += "?" + SinaCommonString.SINA_AUTH_P_access_token + "=" + sinaAuth.getSinaOauthToken().getAccess_token();
            } else {
                formParams += "&" + SinaCommonString.SINA_AUTH_P_access_token + "=" + sinaAuth.getSinaOauthToken().getAccess_token();
            }


            return formParams;
        } else {
            return "sinaAuth == null";
        }

    }

    public String getUserInfo() {
        if (sinaAuth != null) {
            MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
            formParams = addAuthToken(formParams);
            HttpUtils httpUtils = new HttpUtils();
            String response = httpUtils.doPost(SinaCommonString.SINA_AUTH_ACCOUNT_INFO_URI, formParams);
            return response;
        } else {
            return "sinaAuth != null";
        }


    }

    /* 获取文件和目录信息 (只能用GET)*/
    public String getMetadata(String path) {
        if (sinaAuth != null) {
            String getMetadataUri = SinaCommonString.SINA_File_META_URI + path;
            getMetadataUri = addAuthToken(getMetadataUri);
            log.info("getMetadataUri:" + getMetadataUri);
            HttpUtils httpUtils = new HttpUtils();
//            HashMap tokenMap = httpUtils.doGet(getMetadataUri);
            String response = httpUtils.doGet(getMetadataUri);
            if (path.length() <= 1) {
                rootMeta = SinaUtil.json2meta(response);
                log.info(rootMeta.toString());
            }
            return response;
        } else {
            return "sinaAuth==null";
        }
    }


    /*
    * 上传文件 (PUT),若path为空则传到根目录
    * path:某个文件或者dir的revision值
    * filepath：需要上传的文件在本地的路径
    * */
    public String uploadFilePut(String path, String filePath) {
        if (sinaAuth != null) {
            String getMetadataUri = SinaCommonString.SINA_File_UPLOAD_URI + "/";
            if (path.startsWith("/")) {
                path=path.substring(1, path.length());
                log.info("path:"+path);
            }
            getMetadataUri += path;
            getMetadataUri = addAuthToken(getMetadataUri);
            log.info("uploadFilePut:" + getMetadataUri);
            //读取文件
            if (filePath.trim() == "" || filePath == null) {
                filePath = "/Users/liushanchen/Desktop/test.java";
            }
            File f = new File(filePath);
            log.info("filePath:" + f.getPath());

            InputStream inputSteam = null;
            byte[] buf = null;
            try {
                inputSteam = new FileInputStream(f);
                buf = new byte[(int) f.length()];
                int len = inputSteam.read(buf, 0, buf.length);
                log.info("read len:" + len);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //发请求
            HttpUtils httpUtils = new HttpUtils();

            String response = httpUtils.doPut(getMetadataUri, buf);
            try {
                inputSteam.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        } else {
            return "sinaAuth==null";
        }
    }

    /*
        下载文件（get）
         */
    public String downloadFile(String path) {
        if (sinaAuth != null) {
            if (path == null) {
                return "path == null";
            }
            if(!path.startsWith("/"))path="/"+path;
            String getMetadataUri = SinaCommonString.SINA_File_DOWNLOAD_URI + path;
            getMetadataUri=addAuthToken(getMetadataUri);
            HttpUtils httpUtils = new HttpUtils();
            String response = httpUtils.doGet(getMetadataUri);
            return response;
        } else {
            return null;
        }


    }

    /*
    删除文件（只能post）
     */
    public String deleteFile(String path) {
        if (sinaAuth != null && path != null) {
            MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
            formParams.add("root", "sandbox");
            formParams.add("path", path);
            formParams = addAuthToken(formParams);
            HttpUtils httpUtils = new HttpUtils();
            String response = httpUtils.doPost(SinaCommonString.SINA_File_DELETE_URI, formParams);
            return response;
        } else {
            return "sinaAuth != null";
        }


    }

    /*
    获取用户文件和目录操作变化记录。列表每页固定为 2000 条。
     */
    public String getFileDelta() {
        if (sinaAuth != null) {
            MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
            formParams = addAuthToken(formParams);
            HttpUtils httpUtils = new HttpUtils();
            String response = httpUtils.doPost(SinaCommonString.SINA_File_dELTA_URI, formParams);
            return response;
        } else {
            return "sinaAuth != null";
        }


    }
}
