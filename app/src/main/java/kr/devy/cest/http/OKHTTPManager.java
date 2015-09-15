package kr.devy.cest.http;

import android.os.Environment;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * Created by pc on 2015-07-31.
 */
public class OKHTTPManager {

    private static OKHTTPManager instance;
    private OkHttpClient client=null;
    private File file;
    private Cache cache;
    private static final String filename="cest_cache";
    private static final int MAX_SIZE = 1024*1024*10;
    public static synchronized OKHTTPManager getInstance(){
        if(instance ==null){
            instance = new OKHTTPManager();
        }
        return instance;
    }

    private OKHTTPManager(){
        client = new OkHttpClient();
        File root = new File(Environment.getExternalStorageDirectory()+File.separator+"CEST"+File.separator);
        root.mkdirs();
        file = new File(root,filename);
        cache = new Cache(file,MAX_SIZE);
        client.setCache(cache);
    }





}
