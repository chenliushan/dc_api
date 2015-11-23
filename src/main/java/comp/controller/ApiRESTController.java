package comp.controller;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import comp.services.KPAuthorization;
import comp.services.OneDriveAuthorization;
import comp.services.SinaAuthorization;

import comp.utils.HttpUtils;
import comp.utils.KPClass.KPDLCLass;
import comp.utils.KPClass.KPULCLass;
import comp.utils.KPClass.KPURLGen;
import comp.utils.KPUtil;
import comp.utils.KuaipanCommonString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.jersey.api.client.Client;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by liushanchen on 15/9/28.
 */
@RestController
public class ApiRESTController {
    private static Log log = LogFactory.getLog(ApiRESTController.class);
    private static final String AUTHORIZATION = "Authorization";

    private SinaAuthorization sinaAuthorization;
    private OneDriveAuthorization oneDriveAuthorization;



    /*
    use following url to invoke relative API
    user authorization request "localhost:8080/sina_req"
    user information request "localhost:8080/sina_user_info"

     */


//    @RequestMapping("/home")
//    public ModelAndView home_page() {
//        Model model=new Model() {
//            @Override
//            public Model addAttribute(String attributeName, Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAttribute(Object attributeValue) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Collection<?> attributeValues) {
//                return null;
//            }
//
//            @Override
//            public Model addAllAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public Model mergeAttributes(Map<String, ?> attributes) {
//                return null;
//            }
//
//            @Override
//            public boolean containsAttribute(String attributeName) {
//                return false;
//            }
//
//            @Override
//            public Map<String, Object> asMap() {
//                return null;
//            }
//        };
//        ModelAndView modelAndView = new ModelAndView("/home.html");
////        modelAndView.addAttribute("conn_message", "success");
//        return modelAndView;
//    }
//
//
//    @RequestMapping("/sina_req")
//    public ModelAndView SinaAuthCodeReq() {
//        sinaAuthorization = new SinaAuthorization();
//        ModelAndView model = new ModelAndView("redirect:" + sinaAuthorization.codeReqUrl());
//        log.info("sina_req_sendRequest:" + sinaAuthorization.codeReqUrl());
//        return model;
//
//    }
//
//
//    @RequestMapping("/sina_code")
//    public String SinaAuthCode(@RequestParam(value = "code", defaultValue = "") String code) {
//        log.info("get Sina Response，code：" + code);
//        sinaAuthorization.getCodeResponse(code);
//        return sinaAuthorization.getAccessToken();
//    }
//
//    /*
//    refresh token
//     */
//    @RequestMapping("/sina_token_refresh")
//    public String SinaTokenRefresh() {
//        return sinaAuthorization.refreshToken();
//    }
//
//    @RequestMapping("/sina_user_info")
//    public String SinaUserInfo() {
//        log.info("sina_user_info");
//        if (sinaAuthorization != null) {
//            return sinaAuthorization.getUserInfo();
//        } else {
//            log.info("sinaAuthorization==null");
//        }
//        return "exceptions occur!";
//    }
//
//    /*
//        获取用户文件和目录操作变化记录。列表每页固定为 2000 条。
//         */
//    @RequestMapping("/sina_delta")
//    public String SinaDelta() {
//        log.info("sina_meta");
//        return sinaAuthorization.getFileDelta();
//    }

    /*
    获取文件和目录信息
    可选参数path:文件path
     */
//    @RequestMapping("/sina_meta")
//    public String SinaMetadata(@RequestParam(value = "path", defaultValue = "/") String path) {
//        log.info("sina_meta");
//        return sinaAuthorization.getMetadata(path);
//    }

    /*
   文件上传
   必须参数filepath：需要上传的文件在本地的path
   可选参数path：上传文件在服务器的path，不传参则默认为根路径
    */
//    @RequestMapping("/sina_upload")
//    public String SinaUpload(@RequestParam(value = "path", defaultValue = "/") String path, @RequestParam(value = "filePath", defaultValue = "") String filePath) {
//        log.info("sina_upload");
//        return sinaAuthorization.uploadFilePut(path, filePath);
//    }

    /*
    文件下载
    必须参数path：需要下载的文件在服务器的path
     */
//    @RequestMapping("/sina_download")
//    public String SinaDownload(@RequestParam(value = "path", defaultValue = "/") String path) {
//        log.info("sina_download");
//        return sinaAuthorization.downloadFile(path);
//    }
//
//    /*
//    文件删除
//    必须参数path：需要删除文件在API端的path
//     */
//    @RequestMapping("/sina_delete")
//    public String SinaDelete(@RequestParam(value = "path", defaultValue = "/") String path) {
//        log.info("sina_delete");
//        return sinaAuthorization.deleteFile(path);
//    }


//    @RequestMapping("/google_req")
//    public ModelAndView GoogleReq() {
//        ModelAndView model = new ModelAndView("redirect:" + "https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=873643729764-8qee5kiijt48f8i05k8thuajml9i2m4g.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googlecode&response_type=code&scope=https://www.googleapis.com/auth/drive.metadata.readonly");
//        return model;
//    }

//    @RequestMapping("/onedrive/req")
//    public ModelAndView OnedriveAuthCodeReq() {
//        oneDriveAuthorization = new OneDriveAuthorization();
//        String codeReqUri=oneDriveAuthorization.codeReqUrl();
//        ModelAndView model = new ModelAndView("redirect:" + codeReqUri);
//        log.info("Onedrive sendRequest:" + codeReqUri);
//        return model;
//    }
//
//    @RequestMapping("/onedrive/code")
//    public String OnedriveAuthCode(@RequestParam(value = "code") String code) {
//        log.info("get Onedrive Response，code：" + code);
//        oneDriveAuthorization.getCodeResponse(code);
//        return oneDriveAuthorization.getAccessToken();
//    }

//    @RequestMapping("/onedrive/meta")
//    public String OnedriveMeta() {
//        log.info("get Onedrive default drive metadata：");
//        return oneDriveAuthorization.getMeta();
//    }
//    @RequestMapping("/onedrive/children")
//    public String OnedriveChildren() {
//        log.info("get Onedrive default drive metadata：");
//        return oneDriveAuthorization.getChildren();
//    }
//    //path为服务器上的路径，filePath 为本地文件的路径（/Users/liushanchen/Desktop/OS_report.docx）
//    @RequestMapping("/onedrive/upload")
//    public String OnedriveUpload(@RequestParam(value = "path", defaultValue = "") String path,
//                                 @RequestParam(value = "filePath", defaultValue = "") String filePath) {
//        log.info("upload a file to Onedrive ");
//        return oneDriveAuthorization.putContent(path, filePath);
//    }
//    @RequestMapping("/onedrive/download")
//    public String OnedriveDownload(@RequestParam(value = "filePath", defaultValue = "") String filePath) {
//        log.info("download a file from Onedrive ");
//        return oneDriveAuthorization.downloadFile(filePath);
//    }
//
//
//
//
//    @RequestMapping("/huaweicode")
//    public String HuaweiAuthCode(@RequestParam(value = "code") String code) {
//        return code;
//    }

    @RequestMapping("/kuaipan/auth")
    public String kuaipanAuthTokenReq(HttpServletResponse response) {

        KPAuthorization KPAuth = new KPAuthorization();
        HttpUtils httpAction = new HttpUtils();

        String url = KPAuth.getRequestToken();
        String jsonResponse = httpAction.doGet(url);
        KPUtil.json2OTempauthToken(jsonResponse);

        try {
            response.sendRedirect(KPAuth.signInURL(KuaipanCommonString.KP_TEMP_OAUTH_TOKEN));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect to Kuaipan";
    }


    @RequestMapping("/kuaipan/callback")
    public String kuaipanAuthCallback( @RequestParam String oauth_verifier,
                                       @RequestParam String oauth_token,
                                       HttpServletResponse response) {

        KPAuthorization KPAuth = new KPAuthorization();
        HttpUtils httpUtils = new HttpUtils();
        String url = null;

        try {
            url = KPAuth.getAccessToken(KuaipanCommonString.KP_TEMP_OAUTH_TOKEN, oauth_verifier, KuaipanCommonString.KP_TEMP_OAUTH_TOKEN_SECRET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = httpUtils.doGet(url);
        KPUtil.json2OauthToken(json);

        return "oauth_token: " + KuaipanCommonString.KP_OAUTH_TOKEN + " oauth_token_secret: " + KuaipanCommonString.KP_OAUTH_TOKEN_SECRET +
                "User ID: " + KuaipanCommonString.KP_USERID + "Charged_Dir: " + KuaipanCommonString.KP_CHARGED_DIR +
                "Expries in: "  + KuaipanCommonString.KP_EXPIRES;

    }


    @RequestMapping("/kuaipan/upload")
    public String kuaipanUpload(@RequestParam String name) {

        KPULCLass KPUL = new KPULCLass(name);
        return "Uploading....";
    }


    @RequestMapping("/kuaipan/download")
    public String kuaipanDownload(@RequestParam String name, HttpServletResponse response) {

        KPURLGen kpDLURL = new KPURLGen();
        String url = null;
        try {
            url = kpDLURL.getDLURL("/" +name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        KPDLCLass kpdlcLass = new KPDLCLass();
        try {
            kpdlcLass.startDownload(url, name, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String message = "Downdloading.....";
        return message;
    }

    @RequestMapping("/kuaipan/accinfo")
    public String kuaipanAccInfo() {
        String resp = null;
        KPURLGen URL = new KPURLGen();

        try {
            resp = URL.getAccURL();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpUtils httpUtils = new HttpUtils();
        resp = httpUtils.doGet(resp);
        return resp;
    }


    @RequestMapping("/kuaipan/folderinfo")
    public String kuaipanFolderInfo(@RequestParam String path) {

        String resp = null;
        KPURLGen URL = new KPURLGen();

        try {
            resp = URL.getFolderURL(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

       HttpUtils httpUtils = new HttpUtils();
       resp = httpUtils.doGet(resp);

        return resp;
    }


}
