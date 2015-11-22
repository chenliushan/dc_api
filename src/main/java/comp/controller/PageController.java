package comp.controller;

import comp.services.FileService;
import comp.services.KPAuthorization;
import comp.services.OneDriveAuthorization;
import comp.services.SinaAuthorization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liushanchen on 15/11/21.
 */
@Controller
public class PageController {
    private static Log log = LogFactory.getLog(PageController.class);
    private OneDriveAuthorization oneDriveAuthorization;
    private boolean onedriveAuth=false;

    private SinaAuthorization sinaAuthorization;
    private boolean sinaAuth=false;

    private KPAuthorization kpAuthorization;
    private boolean kuaipanAuth=true;

    @RequestMapping("/myhome")
    @ResponseBody
    public ModelAndView home_page() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("message", "this123");
        return modelAndView;
    }

    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String setDbDone( Model model) {
        model.addAttribute("message", "success123");
        return "home";

    }
    @RequestMapping("/upload")
    @ResponseBody
    public ModelAndView home_upload(@RequestParam(value = "path", defaultValue = "") String path,
                                    @RequestParam(value = "filePath", defaultValue = "") String filePath) {
        ModelAndView modelAndView = new ModelAndView("home");
        boolean uploadResult=false;
        FileService fileService=new FileService(oneDriveAuthorization,sinaAuthorization,kpAuthorization);
        uploadResult=fileService.uploadFile(path,filePath);
        if(uploadResult){
            modelAndView.addObject("message", "Upload Success!");
        }else{
            modelAndView.addObject("message", "Upload Fail!");
        }

        return modelAndView;
    }

    @RequestMapping("/existfile")
    @ResponseBody
    public ModelAndView home_exist() {
        ModelAndView modelAndView = new ModelAndView("home");
        String[] files= FileService.filesBeenUpload();
        modelAndView.addObject("exist", files);
        return modelAndView;
    }

    @RequestMapping("/download")
    @ResponseBody
    public ModelAndView home_dwonload(@RequestParam(value = "filePath", defaultValue = "") String filePath) {
        ModelAndView modelAndView = new ModelAndView("home");
        boolean Result=false;
        FileService fileService=new FileService(oneDriveAuthorization,sinaAuthorization,kpAuthorization);
        Result=fileService.downloadFile(filePath);
        if(Result){
            modelAndView.addObject("message", "Upload Success!");
        }else{
            modelAndView.addObject("message", "Upload Fail!");
        }

        return modelAndView;
    }
//=============================================ONEDRIVE=================================================
    @RequestMapping("/onedrive/req")
    public ModelAndView OnedriveAuthCodeReq() {
        oneDriveAuthorization = new OneDriveAuthorization();
        String codeReqUri=oneDriveAuthorization.codeReqUrl();
        ModelAndView model = new ModelAndView("redirect:" + codeReqUri);
        log.info("Onedrive sendRequest:" + codeReqUri);
        return model;
    }

    @RequestMapping("/onedrive/code")
    public ModelAndView OnedriveAuthCode(@RequestParam(value = "code") String code) {
        log.info("get Onedrive Response，code：" + code);
        ModelAndView modelAndView = new ModelAndView("home");
        oneDriveAuthorization.getCodeResponse(code);
        if(oneDriveAuthorization.getAccessToken()>-1){
            onedriveAuth=true;
        }
        modelAndView.addObject("sinaAuth", sinaAuth);
        modelAndView.addObject("onedriveAuth", onedriveAuth);
        modelAndView.addObject("kuaipanAuth", kuaipanAuth);
        return modelAndView;
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
    //path(默认为filename)
    @RequestMapping("/onedrive/download")
    public String OnedriveDownload(@RequestParam(value = "filePath", defaultValue = "") String filePath) {
        log.info("download a file from Onedrive ");
        return oneDriveAuthorization.downloadFile(filePath);
    }

//=============================================SINA=================================================

    @RequestMapping("/sina/req")
    public ModelAndView SinaAuthCodeReq() {
        sinaAuthorization = new SinaAuthorization();
        ModelAndView model = new ModelAndView("redirect:" + sinaAuthorization.codeReqUrl());
        log.info("sina_req_sendRequest:" + sinaAuthorization.codeReqUrl());
        return model;

    }


    @RequestMapping("/sina_code")
    public ModelAndView SinaAuthCode(@RequestParam(value = "code", defaultValue = "") String code) {
        log.info("get Sina Response，code：" + code);
        ModelAndView modelAndView = new ModelAndView("home");
        sinaAuthorization.getCodeResponse(code);
        if( sinaAuthorization.getAccessToken()>-1){
            sinaAuth=true;
        }
        modelAndView.addObject("sinaAuth", sinaAuth);
        modelAndView.addObject("onedriveAuth", onedriveAuth);
        modelAndView.addObject("kuaipanAuth", kuaipanAuth);
        return modelAndView;
    }

    /*
    refresh token
     */
    @RequestMapping("/sina/token_refresh")
    public String SinaTokenRefresh() {
        return sinaAuthorization.refreshToken();
    }

    @RequestMapping("/sina/user_info")
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
    @RequestMapping("/sina/delta")
    public String SinaDelta() {
        log.info("sina_meta");
        return sinaAuthorization.getFileDelta();
    }

    /*
    获取文件和目录信息
    可选参数path:文件path
     */
    @RequestMapping("/sina/meta")
    public String SinaMetadata(@RequestParam(value = "path", defaultValue = "/") String path) {
        log.info("sina_meta");
        return sinaAuthorization.getMetadata(path);
    }

    /*
   文件上传
   必须参数filepath：需要上传的文件在本地的path
   可选参数path：上传文件在服务器的path，不传参则默认为根路径
    */
    @RequestMapping("/sina/upload")
    public String SinaUpload(@RequestParam(value = "path", defaultValue = "/") String path, @RequestParam(value = "filePath", defaultValue = "") String filePath) {
        log.info("sina_upload");
        return sinaAuthorization.uploadFilePut(path, filePath);
    }

    /*
    文件下载
    必须参数path：需要下载的文件在服务器的path(默认为filename)
     */
    @RequestMapping("/sina/download")
    public String SinaDownload(@RequestParam(value = "path", defaultValue = "/") String path) {
        log.info("sina_download");
        return sinaAuthorization.downloadFile(path);
    }

    /*
    文件删除
    必须参数path：需要删除文件在API端的path
     */
    @RequestMapping("/sina/delete")
    public String SinaDelete(@RequestParam(value = "path", defaultValue = "/") String path) {
        log.info("sina_delete");
        return sinaAuthorization.deleteFile(path);
    }


}
