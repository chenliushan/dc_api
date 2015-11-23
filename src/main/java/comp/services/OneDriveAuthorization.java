package comp.services;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import comp.domain.OnedriveAuth;
import comp.domain.OnedriveOauthToken;
import comp.utils.HttpUtils;
import comp.utils.OneDriveConmmonString;
import comp.utils.SinaCommonString;
import comp.utils.SinaUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;

/**
 * Created by liushanchen on 15/10/12.
 */
public class OneDriveAuthorization {
    private OnedriveAuth onedriveAuth;
    private static Log log = LogFactory.getLog(SinaAuthorization.class);

    private HttpUtils httpUtils = new HttpUtils();

    public OneDriveAuthorization() {
        this.onedriveAuth = new OnedriveAuth();

    }

    public OnedriveAuth getOnedriveAuth() {
        return onedriveAuth;
    }

    public String codeReqUrl() {
        return onedriveAuth.getCodeReqUri();
    }

    public void getCodeResponse(String code) {
        if (code != null && !code.equals("")) {
            onedriveAuth.setCode(code);
        } else {
            System.out.print("code is null");
        }
    }

    public int getAccessToken() {
        if (onedriveAuth.getCode() != null && !onedriveAuth.getCode().equals("")) {
            MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
            formParams.add(OneDriveConmmonString.AUTH_PARAMETER_client_id, onedriveAuth.getClient_id());
            formParams.add(OneDriveConmmonString.AUTH_PARAMETER_redirect_uri, onedriveAuth.getRedirect_uri());
            formParams.add(OneDriveConmmonString.AUTH_PARAMETER_client_secret, onedriveAuth.getClient_secret());
            formParams.add(OneDriveConmmonString.AUTH_PARAMETER_code, onedriveAuth.getCode());
            formParams.add(OneDriveConmmonString.AUTH_PARAMETER_grant_type, OneDriveConmmonString.AUTH_GRANT_TYPE);

            OnedriveOauthToken response = httpUtils.onedriveAuthPost(OneDriveConmmonString.AUTH_TOKEN_URI, formParams);
            onedriveAuth.setOnedriveOauthToken(response);
            log.info("getAccessToken: " + onedriveAuth.getOnedriveOauthToken().getAccess_token());
            return 0;
        }
        return -1;
    }

    public String getMeta() {
        if (onedriveAuth != null) {
            String uri = onedriveAuth.getRoot_uri() + "/drive";
            String response = httpUtils.oneDriveGet(uri, onedriveAuth.getOnedriveOauthToken().getAccess_token());
            return response;
        }
        return null;
    }

    public String getChildren() {
        if (onedriveAuth != null) {
            String uri = onedriveAuth.getRoot_uri() + "/drive/root/children";
            String response = httpUtils.oneDriveGet(uri, onedriveAuth.getOnedriveOauthToken().getAccess_token());
            return response;
        }
        return null;
    }

    //PUT /drive/root:/{parent-path}/{filename}:/content
    public String putContent(String path, String filePath) {
        String filename = null;
        if (onedriveAuth != null) {
            String uri = onedriveAuth.getRoot_uri() + "/drive/root:";
            if (path != null && !path.equals("")) {
                if (path.startsWith("/")) {
                    uri = uri + path;
                } else {
                    uri = uri + "/" + path;
                }
            }
            log.info("uri:" + uri);

            if (filePath.trim().equals("") || filePath == null) {
               return null;
            }
            int filenameIndex = filePath.lastIndexOf("/");
            if (filenameIndex > -1) {
                filename = filePath.substring(filenameIndex, filePath.length());
                if (filename != null && filename != "") {
                    uri = uri + filename + ":/content";
                } else {
                    uri = uri + "/test.txt:/content";
                }
                log.info("uploadFilePut:" + uri);
                //读取文件
                File f = new File(filePath);
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
                String response = httpUtils.onedrivePut(uri, onedriveAuth.getOnedriveOauthToken().getAccess_token(), buf);
                try {
                    inputSteam.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }
            return null;
        } else {
            return "onedriveAuth==null";
        }
    }
    public String downloadFile(String path) {
        if (onedriveAuth != null) {
            String uri = onedriveAuth.getRoot_uri() + "/drive/root:" ;
            if (path != null && !path.equals("")) {
                if (path.startsWith("/")) {
                    uri = uri + path;
                } else {
                    uri = uri + "/" + path;
                }
                uri = uri + ":/content" ;
            }else{
                return "path == null";
            }
            String response = httpUtils.doGetDownload(uri, onedriveAuth.getOnedriveOauthToken().getAccess_token());
            return response;
        } else {
            return null;
        }
    }
 public String deleteFile(String path) {
        if (onedriveAuth != null) {
            String uri = onedriveAuth.getRoot_uri() + "/drive/root:" ;
            if (path != null && !path.equals("")) {
                if (path.startsWith("/")) {
                    uri = uri + path;
                } else {
                    uri = uri + "/" + path;
                }
            }else{
                return null;
            }
            String response = httpUtils.onedriveDelete(uri, onedriveAuth.getOnedriveOauthToken().getAccess_token());
            return response;
        } else {
            return "AUTH==NULL";
        }
    }

}
