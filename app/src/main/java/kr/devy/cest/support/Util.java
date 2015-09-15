package kr.devy.cest.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pc on 2015-08-01.
 */
public class Util {

    public static String getCurrentDateTime(long time){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));

    }
}
