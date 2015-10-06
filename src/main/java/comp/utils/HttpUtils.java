package comp.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
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



    public String doPost(String uri, MultivaluedMap<String, String> formData, String header,String headerValue) {
        //发送post请求
        WebResource.Builder builder = client.resource(uri)
                .header(header, headerValue)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPost uri="+uri);
        log.info("doPost formData="+formData.toString());
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
        log.info("response:"+json);
        return json;
    }

    public String doPost(String uri, MultivaluedMap<String, String> formData) {
        //发送post请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPost uri="+uri);
        log.info("doPost formData="+formData.toString());
        ClientResponse clientResponse = builder.post(ClientResponse.class, formData);
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response:"+json);
        return json;
    }


    public String doGet(String uri) {
        //发送get请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doGet uri="+uri);
        ClientResponse clientResponse = builder.get(ClientResponse.class);
        //以String 获取返回结果
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response:"+json);
        return json;
    }
    public String doGet(String uri,String header) {
        //发送get请求
        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doGet uri="+uri);
        ClientResponse clientResponse = builder.get(ClientResponse.class);
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("response:"+json);
        return json;
    }
    public String doPut(String uri,Object requestEntities) {
        //发送put请求

        WebResource.Builder builder = client.resource(uri)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        log.info("doPut uri="+uri);
        ClientResponse clientResponse = builder.put(ClientResponse.class, requestEntities);
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("response:"+json);
        return json;
    }

}
