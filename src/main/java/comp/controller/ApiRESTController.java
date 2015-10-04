package comp.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.OutBoundHeaders;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import comp.domain.SinaAuth;
import comp.services.DriveQuickstart;
import comp.services.GoogleClass;
import comp.services.SinaAuthorization;
import comp.utils.CommonString;
import comp.utils.SinaCommonString;
import org.apache.commons.codec.binary.Base64;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.PartialRequestBuilder;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * Created by liushanchen on 15/9/28.
 */
@RestController
public class ApiRESTController {
    SinaAuthorization sinaAuthorization;

    private static final String AUTHORIZATION = "Authorization";
    private Client client;
    public ApiRESTController() {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        this.client = Client.create(config);

    }

    @RequestMapping("/request")
    public String testMyRequest(@RequestParam(value = "uri", defaultValue = "http://www.baidu.com") String uri) {

        return "test_page";
    }

    @RequestMapping("/sina_req")
    public ModelAndView SinaAuthCodeReq() {
        sinaAuthorization = new SinaAuthorization();
        ModelAndView model = new ModelAndView("redirect:" + sinaAuthorization.codeReqUrl());
        System.out.println("sina_req_sendRequest:"+ sinaAuthorization.codeReqUrl());
        return model;

    }


    @RequestMapping("/sina_code")
    public String SinaAuthCode(@RequestParam(value = "code", defaultValue = "") String code) {
        System.out.println("get Response，code："+code);
        sinaAuthorization.getCodeResponse(code);
        sinaAuthorization.getAccessToken();
        return sinaAuthorization.getUserInfo();
    }

//    @RequestMapping("/sina_code")
//    public String SinaAuthCode(@RequestParam(value = "code", defaultValue = "") String code) {
//        SinaAuth sinaAuth =new SinaAuth();
//        sinaAuth.setCode(code);
//        MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
//        formData.add("grant_type", "authorization_code");
//        formData.add("code", code);
//        formData.add("redirect_uri","http://localhost:8080/sina_code");
//        formData.add("client_id","1377006082");
//        formData.add("client_secret","31c3b4be74fc9d34f4bea684cb6dc1bd");
//        String auth = "Basic ".concat(new String(Base64.encodeBase64(sinaAuth.getClient_id().concat(":")
//                .concat(sinaAuth.getClient_secret()).getBytes())));
//        Builder builder = client.resource(SinaCommonString.SINA_AUTH_ACCESS_TOKEN_URI).header(AUTHORIZATION, auth)
//                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
//        System.out.println("will send request of token");
//        ClientResponse clientResponse = builder.post(ClientResponse.class, formData);
//        String output=clientResponse.getEntity(String.class);
//        System.out.println(output);
//        return clientResponse.toString();
//    }
    /*
  * Nasty trick to be able to print out the headers after the POST is done
//  */
//    private OutBoundHeaders getHeadersCopy(Builder builder) {
//        Field metaData;
//        try {
//            metaData = PartialRequestBuilder.class.getDeclaredField("metadata");
//            metaData.setAccessible(true);
//            return new OutBoundHeaders((OutBoundHeaders) metaData.get(builder));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    @RequestMapping("/google_req")
    public ModelAndView GoogleReq() {
        ModelAndView model = new ModelAndView("redirect:" + "https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=873643729764-8qee5kiijt48f8i05k8thuajml9i2m4g.apps.googleusercontent.com&redirect_uri=http://localhost:8080/googlecode&response_type=code&scope=https://www.googleapis.com/auth/drive.metadata.readonly");
        return model;
    }
    @RequestMapping("/ondrive_code")
    public String OndriveAuthCode(@RequestParam(value = "code") String code) {
        return code;
    }
    @RequestMapping("/googlecode")
    public String GoogleAuthCode(@RequestParam(value = "code",defaultValue = "") String code) {
        System.out.print("googlecode");
        if(code!=""&&code!=null){
            GoogleClass googleClass=new GoogleClass();
            try {
                return googleClass.getCredentials(code,"").toString();
            } catch (GoogleClass.CodeExchangeException e) {
                e.printStackTrace();
            } catch (GoogleClass.NoRefreshTokenException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @RequestMapping("/huaweicode")
    public String HuaweiAuthCode(@RequestParam(value = "code") String code) {
        return code;
    }
}
