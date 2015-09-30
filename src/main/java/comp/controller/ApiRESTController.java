package comp.controller;

import comp.services.SinaAuthorization;
import comp.utils.HttpClientUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by liushanchen on 15/9/28.
 */
@RestController
public class ApiRESTController {
    SinaAuthorization sinaAuthorization;

    @RequestMapping("/request")
    public String testMyRequest(@RequestParam(value = "uri", defaultValue = "http://www.baidu.com") String uri) {

        return "test_page";
    }

    @RequestMapping("/sinacode_req")
    public ModelAndView SinaAuthCodeReq() {
        sinaAuthorization = new SinaAuthorization();
        ModelAndView model = new ModelAndView("redirect:" + sinaAuthorization.codeReqUrl());
        System.out.println("sendRequest:"+ sinaAuthorization.codeReqUrl());
        return model;

    }

    @RequestMapping("/sinacode")
    public String SinaAuthCode(@RequestParam(value = "code", defaultValue = "") String code) {
        System.out.println("get Response，code："+code);
        sinaAuthorization.getCodeResponse(code);
        return sinaAuthorization.getAccessToken();
//        return "success! code=" + code;
    }

    @RequestMapping("/ondrivecode")
    public String OndriveAuthCode(@RequestParam(value = "code") String code) {
        return code;
    }

    @RequestMapping("/huaweicode")
    public String HuaweiAuthCode(@RequestParam(value = "code") String code) {
        return code;
    }
}
