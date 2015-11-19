package comp.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import comp.domain.SinaOauthToken;
import comp.domain.SinaMetadata;

import java.lang.reflect.Type;

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
    public static SinaOauthToken json2OauthToken(String json){
        Type type = new TypeToken<SinaOauthToken>() {
        }.getType();
        SinaOauthToken sinaOauthToken = new Gson().fromJson(json,type);
        return sinaOauthToken;
    }

}
