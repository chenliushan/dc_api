package comp.controller;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import comp.services.OneDriveAuthorization;
import comp.services.SinaAuthorization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sun.jersey.api.client.Client;

/**
 * Created by liushanchen on 15/9/28.
 */
@RestController
public class ApiRESTController {
    private Client client;
    private static Log log = LogFactory.getLog(ApiRESTController.class);
    private static final String AUTHORIZATION = "Authorization";

    private SinaAuthorization sinaAuthorization;
    private OneDriveAuthorization oneDriveAuthorization;


    public ApiRESTController() {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        this.client = Client.create(config);

    }
    /*
    use following url to invoke relative API
    user authorization request "localhost:8080/sina_req"
    user information request "localhost:8080/sina_user_info"

     */

    @RequestMapping("/sina_req")
    public ModelAndView SinaAuthCodeReq() {
        sinaAuthorization = new SinaAuthorization();
        ModelAndView model = new ModelAndView("redirect:" + sinaAuthorization.codeReqUrl());
        log.info("sina_req_sendRequest:" + sinaAuthorization.codeReqUrl());
        return model;

    }


    @RequestMapping("/sina_code")
    public String SinaAuthCode(@RequestParam(value = "code", defaultValue = "") String code) {
        log.info("get Sina Response，code：" + code);
        sinaAuthorization.getCodeResponse(code);
        return sinaAuthorization.getAccessToken();
    }

    /*
    refresh token
     */
    @RequestMapping("/sina_token_refresh")
    public String SinaTokenRefresh() {
        return sinaAuthorization.refreshToken();
    }

    @RequestMapping("/sina_user_info")
    public String SinaUserInfo() {
        log.info("sina_user_info");
        if (sinaAuthorization != null) {
            return sinaAuthorization.getUserInfo();
        } else {
            log.info("sinaAuthorization==null");
        }
        return "exceptions occur!";
    }

    /*
        获取用户文件和目录操作变化记录。列表每页固定为 2000 条。
         */
    @RequestMapping("/sina_delta")
    public String SinaDelta() {
        log.info("sina_meta");
        return sinaAuthorization.getFileDelta();
    }

    /*
    获取文件和目录信息
    可选参数path:文件path
     */
    @RequestMapping("/sina_meta")
    public String SinaMetadata(@RequestParam(value = "path", defaultValue = "/") String path) {
        log.info("sina_meta");
        return sinaAuthorization.getMetadata(path);
    }

    /*
   文件上传
   必须参数filepath：需要上传的文件在本地的path
   可选参数path：上传文件在服务器的path，不传参则默认为根路径
    */
    @RequestMapping("/sina_upload")
    public String SinaUpload(@RequestParam(value = "path", defaultValue = "/") String path, @RequestParam(value = "filePath", defaultValue = "") String filePath) {
        log.info("sina_upload");
        return sinaAuthorization.uploadFilePut(path, filePath);
    }

    /*
    文件下载
    必须参数path：需要下载的文件在服务器的path
     */
    @RequestMapping("/sina_download")
    public String SinaDownload(@RequestParam(value = "path", defaultValue = "/") String path) {
        log.info("sina_download");
        return sinaAuthorization.downloadFile(path);
    }

    /*
    文件删除
    必须参数path：需要删除文件在API端的path
     */
    @RequestMapping("/sina_delete")
    public String SinaDelete(@RequestParam(value = "path", defaultValue = "/") String path) {
        log.info("sina_delete");
        return sinaAuthorization.deleteFile(path);
    }


//    @RequestMapping("/google_req")
//    public ModelAndView GoogleReq() {
//        ModelAndView model = new ModelAndView("redirect:" + "https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=873643729764-8qee5kiijt48f8i05k8thuajml9i2m4g.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googlecode&response_type=code&scope=https://www.googleapis.com/auth/drive.metadata.readonly");
//        return model;
//    }

    @RequestMapping("/onedrive/req")
    public ModelAndView OnedriveAuthCodeReq() {
        oneDriveAuthorization = new OneDriveAuthorization();
        String codeReqUri=oneDriveAuthorization.codeReqUrl();
        ModelAndView model = new ModelAndView("redirect:" + codeReqUri);
        log.info("Onedrive sendRequest:" + codeReqUri);
        return model;
    }

    @RequestMapping("/onedrive/code")
    public String OnedriveAuthCode(@RequestParam(value = "code") String code) {
        log.info("get Onedrive Response，code：" + code);
        oneDriveAuthorization.getCodeResponse(code);
        return oneDriveAuthorization.getAccessToken();
    }

    @RequestMapping("/onedrive/meta")
    public String OnedriveMeta() {
        log.info("get Onedrive default drive metadata：");
        return oneDriveAuthorization.getMeta();
    }
    @RequestMapping("/onedrive/children")
    public String OnedriveChildren() {
        log.info("get Onedrive default drive metadata：");
        return oneDriveAuthorization.getChildren();
    }
    //path为服务器上的路径，filePath 为本地文件的路径（/Users/liushanchen/Desktop/OS_report.docx）
    @RequestMapping("/onedrive/upload")
    public String OnedriveUpload(@RequestParam(value = "path", defaultValue = "") String path,
                                 @RequestParam(value = "filePath", defaultValue = "") String filePath) {
        log.info("upload a file to Onedrive ");
        return oneDriveAuthorization.putContent(path, filePath);
    }
    @RequestMapping("/onedrive/download")
    public String OnedriveDownload(@RequestParam(value = "filePath", defaultValue = "") String filePath) {
        log.info("download a file from Onedrive ");
        return oneDriveAuthorization.downloadFile(filePath);
    }




    @RequestMapping("/huaweicode")
    public String HuaweiAuthCode(@RequestParam(value = "code") String code) {
        return code;
    }
}
