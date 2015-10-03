package comp.utils;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class HttpClientUtils {

//    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * post请求
     * @param url
     * @param formParams
     * @return
     */
    public static String doPost(String url, Map<String, String> formParams) {
        if (formParams.isEmpty()) {
            return doPost(url);
        }

        try {
//            MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<String, String>();
//            formParams.keySet().stream().forEach(key -> requestEntity.add(key, formParams.get(key).toString());
//            return RestClient.getClient().postForObject(url, formParams, String.class);
            return TestSimpleClient.getClient().postForObject(url, formParams, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "EMPTY";
    }

    /**
     * post请求
     * @param url
     * @return
     */
    public static String doPost(String url) {
        try {
            return RestClient.getClient().postForObject(url, HttpEntity.EMPTY, String.class);
//            return TestSimpleClient.getClient().postForObject(url, HttpEntity.EMPTY, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "EMPTY";
    }
    /**
     * get请求
     * @param url
     * @return
     */
    public static String doGet(String url) {
        try {
            return RestClient.getClient().getForObject(url, String.class);
//            return TestSimpleClient.getClient().getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "EMPTY";
    }

}