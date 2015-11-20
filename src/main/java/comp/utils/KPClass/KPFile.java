package comp.utils.KPClass;

import java.util.List;

import com.google.gson.Gson;

/**
 * Created by allenlee on 19/11/2015.
 */
public class KPFile {

    public String file_id;
    public String name;
    public String hash;
    public String root;
    public String path;
    public String rev;
    public String create_time = null;
    public String modify_time = null;
    public boolean is_deleted = false;
    public String type = "file";
    public int size = 0;
    public List<KPFile> files = null;

    public String toString() {
        String json = new Gson().toJson(this);
        return json;
    }

}

