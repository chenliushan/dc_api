package comp.utils.KPClass;


import comp.utils.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by allenlee on 21/11/2015.
 */
public class KPDELCLass {

    private static Log log = LogFactory.getLog(KPDELCLass.class);

    public String delete(String path) throws IOException{

        String resp = null;
        KPURLGen URL = new KPURLGen();

        try {

            if(!path.startsWith("/")) path = "/" + path;
            resp = URL.getDELURL(path);
            log.info("url: " + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpUtils httpUtils = new HttpUtils();
        resp = httpUtils.doGet(resp);
        return resp;
    }

}
