package comp.domain;

import com.sun.jersey.api.client.ClientResponse;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by liushanchen on 15/10/2.
 */
public class SinaOauthToken {
    private String access_token;//"ACCESS_TOKEN",
    private String expires_in;// 1234,
    private String refresh_token;//"REFRESH_TOKEN",
    private String time_left;// 1234,
    private String uid;// "UID"

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getTime_left() {
        return time_left;
    }

    public void setTime_left(String time_left) {
        this.time_left = time_left;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
