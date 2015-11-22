package comp.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import comp.domain.OnedriveOauthToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liushanchen on 15/10/2.
 */
public class HttpUtils {
    private Client client;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Log log = LogFactory.getLog(HttpUtils.class);

    public HttpUtils() {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        this.client = Client.create(config);
    }


    public String doPost(String uri, MultivaluedMap<String, String> formData) {
        //发送post请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPost uri=" + uri);
        log.info("doPost formData=" + formData.toString());
        ClientResponse clientResponse = builder.post(ClientResponse.class, formData);
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response:" + json);
        return json;
    }


    public OnedriveOauthToken onedriveAuthPost(String uri, MultivaluedMap<String, String> formData) {
        WebResource.Builder builder = client.resource(uri)
                .entity(formData, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPost uri=" + uri);

        ClientResponse clientResponse = builder.post(ClientResponse.class);
        //对返回结果做初步处理
        String json = null;
        OnedriveOauthToken onedriveOauthToken = null;
        onedriveOauthToken = clientResponse.getEntity(OnedriveOauthToken.class);
        log.info("onedriveOauthToken:" + onedriveOauthToken);
        return onedriveOauthToken;
    }


    public String doPost(String uri, MultivaluedMap<String, String> formData, String header, String headerValue) {
        //发送post请求
        WebResource.Builder builder = client.resource(uri)
                .header(header, headerValue)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPost uri=" + uri);
        log.info("doPost formData=" + formData.toString());
        ClientResponse clientResponse = builder.post(ClientResponse.class, formData);
        //对返回结果做初步处理
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        HashMap map = null;
//        try {
//            map = mapper.readValue(json, HashMap.class);
//            log.info("map ="+map.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        log.info("response:" + json);
        return json;
    }
    /*
    ====================================================GET======================================================
     */

    public String doGet(String uri) {
        //发送get请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doGet uri=" + uri);
        ClientResponse clientResponse = builder.get(ClientResponse.class);

        log.info("clientResponse:" + clientResponse);
        log.info("getStatus:" + clientResponse.getStatus());
        log.info("getClientResponseStatus:" + clientResponse.getClientResponseStatus());
        log.info("getLength:" + clientResponse.getLength());
        log.info("hasEntity:" + clientResponse.hasEntity());
        log.info("getEntityInputStream:" + clientResponse.getEntityInputStream());
        log.info("getType:" + clientResponse.getType());
        log.info("getHeaders:" + clientResponse.getHeaders());
        log.info("location:" + clientResponse.getHeaders().get("location"));
        log.info("Content-Type:" + clientResponse.getHeaders().get("Content-Type"));
        log.info("Content-Disposition:" + clientResponse.getHeaders().get("Content-Disposition"));


        if (clientResponse.getStatus() == 302) {
            if (clientResponse.getHeaders().get("location") != null) {
                String url = clientResponse.getHeaders().get("location").get(0);
                return doGetDownload(url,null);
            }
        }

        if (clientResponse.hasEntity()) {

            //以String 获取返回结果
            String json = null;
            try {
                json = IOUtils.toString(clientResponse.getEntityInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("response:" + json);
            return json;
        }

        return clientResponse.getStatus() + "\n" + clientResponse.getHeaders();
    }

    //下载用的Get
    public String doGetDownload(String uri,String token) {
        //发送get请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        if(token!=null&&!token.equals("")){
            builder.header(OneDriveConmmonString.AUTH_HEADER_Authorization,"bearer "+token);
        }
        log.info("doGet uri=" + uri);
        ClientResponse clientResponse = builder.get(ClientResponse.class);

        log.info("clientResponse:" + clientResponse);
        log.info("getStatus:" + clientResponse.getStatus());
        log.info("getClientResponseStatus:" + clientResponse.getClientResponseStatus());
        log.info("getLength:" + clientResponse.getLength());
        log.info("hasEntity:" + clientResponse.hasEntity());
        log.info("getEntityInputStream:" + clientResponse.getEntityInputStream());
        log.info("getType:" + clientResponse.getType());
        log.info("getHeaders:" + clientResponse.getHeaders());
        log.info("location:" + clientResponse.getHeaders().get("location"));
        log.info("Content-Type:" + clientResponse.getHeaders().get("Content-Type"));
        log.info("Content-Disposition:" + clientResponse.getHeaders().get("Content-Disposition"));


        if (clientResponse.getStatus() == 302) {
            if (clientResponse.getHeaders().get("location") != null) {
                String url = clientResponse.getHeaders().get("location").get(0);
                return doGetDownload(url,token);
            }
        }
        if (clientResponse.getStatus() == 200 && clientResponse.getLength() > 0) {
            String fileName = "test.txt";
            if (!clientResponse.getHeaders().get("Content-Disposition").isEmpty()) {
                String diposition = clientResponse.getHeaders().get("Content-Disposition").toString();
                if (diposition.endsWith("]")) diposition = diposition.substring(0, diposition.length() - 1);
                log.info("diposition:" + diposition);
                String[] dips = diposition.split(";");
                for (int i = 0; i < dips.length; i++) {
                    if (dips[i].contains("filename")) {
                        String[] tmp = dips[i].split("=");
                        fileName = tmp[1];
                        if (fileName.startsWith("\"")) fileName = fileName.substring(1, fileName.length() - 1);
                        if (fileName.endsWith("\"")) fileName = fileName.substring(0, fileName.length() - 1);
                    }
                }

            }
            try {
                //将返回结果写入文件
                File outfile = new File("/Users/liushanchen/Desktop/test/" + fileName);
                FileOutputStream outputStream = new FileOutputStream(outfile);
                byte[] buf = new byte[1024];
                InputStream fileInputStream = clientResponse.getEntityInputStream();
                int len;
                while ((len = fileInputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return clientResponse.getStatus() + ":" + clientResponse.getClientResponseStatus();
    }

    public String oneDriveGet(String uri, String token) {
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .header(OneDriveConmmonString.AUTH_HEADER_Authorization,"bearer "+token);
        log.info("oneDriveGet uri=" + uri);
        ClientResponse clientResponse = builder.get(ClientResponse.class);


        log.info("clientResponse:" + clientResponse);
        log.info("getStatus:" + clientResponse.getStatus());
        log.info("getClientResponseStatus:" + clientResponse.getClientResponseStatus());
        log.info("getLength:" + clientResponse.getLength());
        log.info("hasEntity:" + clientResponse.hasEntity());
        log.info("getEntityInputStream:" + clientResponse.getEntityInputStream());
        log.info("getType:" + clientResponse.getType());
        log.info("getHeaders:" + clientResponse.getHeaders());
        log.info("location:" + clientResponse.getHeaders().get("location"));
        log.info("Content-Type:" + clientResponse.getHeaders().get("Content-Type"));
        log.info("Content-Disposition:" + clientResponse.getHeaders().get("Content-Disposition"));


        //处理返回结果
        if (clientResponse.getStatus() == 302) {
            if (clientResponse.getHeaders().get("location") != null) {
                String url = clientResponse.getHeaders().get("location").get(0);
                return doGetDownload(url,token);
            }
        }
        if (clientResponse.hasEntity()) {
            //以String 获取返回结果
            String json = null;
            try {
                json = IOUtils.toString(clientResponse.getEntityInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("response:" + json);
            return json;
        }

        return clientResponse.getStatus() + "\n" + clientResponse.getHeaders();
    }

 /*
    ====================================================PUT======================================================
     */

    public String doPut(String uri, Object requestEntities) {
        //发送put请求

        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPut uri=" + uri);
        ClientResponse clientResponse = builder.put(ClientResponse.class, requestEntities);
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response:" + json);
        return json;
    }
    public String onedrivePut(String uri,String token, Object requestEntities) {
        WebResource.Builder builder = client.resource(uri)
                .header(OneDriveConmmonString.AUTH_HEADER_Authorization, "bearer " + token)
                .type(MediaType.TEXT_PLAIN_TYPE);
        log.info("doPut uri=" + uri);
        ClientResponse clientResponse = builder.put(ClientResponse.class, requestEntities);
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response:" + json);
        return json;
    }


    public String doKPGetDownload(String uri,String token) {
        //发送get请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        if(token!=null&&!token.equals("")){
            //builder.header(OneDriveConmmonString.AUTH_HEADER_Authorization,"bearer "+token);
        }
        log.info("doGet uri=" + uri);
        ClientResponse clientResponse = builder.get(ClientResponse.class);

        log.info("clientResponse:" + clientResponse);
        log.info("getStatus:" + clientResponse.getStatus());
        log.info("getClientResponseStatus:" + clientResponse.getClientResponseStatus());
        log.info("getLength:" + clientResponse.getLength());
        log.info("hasEntity:" + clientResponse.hasEntity());
        log.info("getEntityInputStream:" + clientResponse.getEntityInputStream());
        log.info("getType:" + clientResponse.getType());
        log.info("getHeaders:" + clientResponse.getHeaders());
        log.info("location:" + clientResponse.getHeaders().get("location"));
        log.info("Content-Type:" + clientResponse.getHeaders().get("Content-Type"));
        log.info("Content-Disposition:" + clientResponse.getHeaders().get("Content-Disposition"));


        if (clientResponse.getStatus() == 302) {
            if (clientResponse.getHeaders().get("location") != null) {
                String url = clientResponse.getHeaders().get("location").get(0);

                try {
                    url = URLEncoder.encode(url,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                System.out.println("url from getKPdownload: " + url);

                return doGetDownload(url,token);
            }
        }
        if (clientResponse.getStatus() == 200 && clientResponse.getLength() > 0) {
            String fileName = "test.txt";
            if (!clientResponse.getHeaders().get("Content-Disposition").isEmpty()) {
                String diposition = clientResponse.getHeaders().get("Content-Disposition").toString();
                if (diposition.endsWith("]")) diposition = diposition.substring(0, diposition.length() - 1);
                log.info("diposition:" + diposition);
                String[] dips = diposition.split(";");
                for (int i = 0; i < dips.length; i++) {
                    if (dips[i].contains("filename")) {
                        String[] tmp = dips[i].split("=");
                        fileName = tmp[1];
                        if (fileName.startsWith("\"")) fileName = fileName.substring(1, fileName.length() - 1);
                        if (fileName.endsWith("\"")) fileName = fileName.substring(0, fileName.length() - 1);
                    }
                }

            }
            try {
                //将返回结果写入文件
                File outfile = new File(CommonString.LOCAL_KPDOWNLOAD_PATH + fileName);
                FileOutputStream outputStream = new FileOutputStream(outfile);
                byte[] buf = new byte[1024];
                InputStream fileInputStream = clientResponse.getEntityInputStream();
                int len;
                while ((len = fileInputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return clientResponse.getStatus() + ":" + clientResponse.getClientResponseStatus();
    }

}

