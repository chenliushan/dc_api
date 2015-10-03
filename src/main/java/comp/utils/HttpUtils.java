package comp.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
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
    private static final String AUTHORIZATION = "Authorization";
    private static final ObjectMapper mapper = new ObjectMapper();
    public HttpUtils(){
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(JacksonJsonProvider.class);
        this.client = Client.create(config);
    }


    public HashMap doPost(String uri,MultivaluedMap<String, String> formData){
        //发送post请求
        String auth = "Basic ";
        WebResource.Builder builder = client.resource(uri).
                header(AUTHORIZATION, auth)
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        ClientResponse clientResponse = builder.post(ClientResponse.class, formData);

        //对返回结果做初步处理
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap map = null;
        try {
            map = mapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("access_token"+(String) map.get("access_token"));


        return map;
    }
public HashMap doPost(String uri,MultivaluedMap<String, String> formData,String token){
        //发送post请求
        String auth = "Basic ";
        WebResource.Builder builder = client.resource(uri)
                .header(AUTHORIZATION, "bearer ".concat(token))
                .type(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        ClientResponse clientResponse = builder.post(ClientResponse.class, formData);

        //对返回结果做初步处理
        String json = null;
        try {
            json = IOUtils.toString(clientResponse.getEntityInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap map = null;
        try {
            map = mapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

}
