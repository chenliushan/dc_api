package comp.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import comp.domain.OauthToken;
import comp.domain.SinaMetadata;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by liushanchen on 15/10/6.
 */
public class SinaUtil {

    public static SinaMetadata json2meta(String json){
        Type type = new TypeToken<SinaMetadata>() {
        }.getType();
        SinaMetadata sinaMetadata= new Gson().fromJson(json,type);
        return sinaMetadata;
    }
    public static OauthToken json2OauthToken(String json){
        Type type = new TypeToken<OauthToken>() {
        }.getType();
        OauthToken oauthToken= new Gson().fromJson(json,type);
        return oauthToken;
    }

}
